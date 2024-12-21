package com.github.aeoliux.violet.api

import com.github.aeoliux.violet.api.bodys.Class
import com.github.aeoliux.violet.api.bodys.Classrooms
import com.github.aeoliux.violet.api.bodys.Colors
import com.github.aeoliux.violet.api.bodys.LuckyNumbers
import com.github.aeoliux.violet.api.bodys.Me
import com.github.aeoliux.violet.api.bodys.SchoolNotices
import com.github.aeoliux.violet.api.bodys.Subjects
import com.github.aeoliux.violet.api.bodys.Timetables
import com.github.aeoliux.violet.api.bodys.Users
import com.github.aeoliux.violet.api.bodys.agenda.HomeworkCategories
import com.github.aeoliux.violet.api.bodys.agenda.Homeworks
import com.github.aeoliux.violet.api.bodys.attendance.Attendances
import com.github.aeoliux.violet.api.bodys.attendance.AttendancesTypes
import com.github.aeoliux.violet.api.bodys.grades.Grades
import com.github.aeoliux.violet.api.bodys.grades.GradesCategories
import com.github.aeoliux.violet.api.bodys.grades.GradesComments
import com.github.aeoliux.violet.api.types.AgendaItem
import com.github.aeoliux.violet.api.types.AttendanceItem
import com.github.aeoliux.violet.api.types.ClassInfo
import com.github.aeoliux.violet.api.types.Grade
import com.github.aeoliux.violet.api.types.Lesson
import com.github.aeoliux.violet.api.types.SchoolNotice
import com.github.aeoliux.violet.api.types.User
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.cookies.HttpCookies
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.get
import io.ktor.http.parameters
import io.ktor.serialization.kotlinx.json.json
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.json.Json

class ApiClient constructor() {
    var users = LinkedHashMap<Int, User>()
    var subjects = LinkedHashMap<Int, String>()
    var colors = LinkedHashMap<Int, String>()
    var classrooms = LinkedHashMap<Int, String>()

    val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                coerceInputValues = true
            })
        }

        install(HttpCookies)
    }

    suspend fun proceedLogin(login: String, password: String) {
        client.get(Endpoints.authStep1)
        client.submitForm(url = Endpoints.authStep2, formParameters = parameters {
            append("action", "login")
            append("login", login)
            append("pass", password)
        })
        client.get(Endpoints.authStep3)

        if (client.get(Endpoints.url("Me")).status.value != 200)
            throw IllegalStateException("Invalid credentials")

        users = data<Users>("Users").toUserMap()
        subjects = data<Subjects>("Subjects").toMap()
        colors = data<Colors>("Colors").toMap()
        classrooms = data<Classrooms>("Classrooms").toMap()
    }

    private suspend inline fun <reified T> data(location: String): T {
        return client.get(Endpoints.url(location)).body()
    }

    suspend fun me(): com.github.aeoliux.violet.api.types.Me {
        return data<Me>("Me").toMeData()
    }

    suspend fun classInfo(): ClassInfo {
        return data<Class>("Classes").toClassInfo(users)
    }

    suspend fun grades(): LinkedHashMap<String, List<Grade>> {
        val categories = data<GradesCategories>("Grades/Categories")
        val comments = data<GradesComments>("Grades/Comments")
        val gradesData = data<Grades>("Grades")

        return gradesData.toGrades(categories, comments, users, subjects, colors)
    }

    suspend fun luckyNumber(): Pair<Int, LocalDate> {
        return data<LuckyNumbers>("LuckyNumbers").parse()
    }

    suspend fun timetable(): Timetable {
        val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        val weekDay = today.dayOfWeek.isoDayNumber
        val weekStarts = if (weekDay > 5) {
            LocalDate.fromEpochDays(today.toEpochDays() + 8 - weekDay)
        } else {
            LocalDate.fromEpochDays(today.toEpochDays() - weekDay + 1)
        }

        return data<Timetables>("Timetables?weekStart=${weekStarts}").toTimetableMap(classrooms)
    }

    suspend fun attendance(): Attendance {
        return data<Attendances>("Attendances").toAttendanceMap(
            colors,
            data<AttendancesTypes>("Attendances/Types").Types,
            users
        )
    }

    suspend fun agenda(): Agenda {
        return data<Homeworks>("HomeWorks")
            .toAgenda(
                data<HomeworkCategories>("HomeWorks/Categories").toMap(colors),
                users,
                classrooms,
                subjects
            )
    }

    suspend fun schoolNotices(): List<SchoolNotice> {
        return data<SchoolNotices>("SchoolNotices").toSNList(users)
    }
}

typealias Timetable = LinkedHashMap<LocalDate, LinkedHashMap<LocalTime, List<Lesson>>>
typealias Attendance = LinkedHashMap<LocalDate, LinkedHashMap<Int, AttendanceItem>>
typealias Agenda = LinkedHashMap<LocalDate, LinkedHashMap<Int, List<AgendaItem>>>