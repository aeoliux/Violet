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
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.cookies.AcceptAllCookiesStorage
import io.ktor.client.plugins.cookies.HttpCookies
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.http.parameters
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CancellationException
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.toLocalDateTime
import kotlinx.io.IOException
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class ApiClient {
    var users = LinkedHashMap<Int, User>()
    var subjects = LinkedHashMap<Int, String>()
    var colors = LinkedHashMap<Int, String>()
    var classrooms = LinkedHashMap<Int, String>()

    var cookieStorage = AcceptAllCookiesStorage()

    val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                coerceInputValues = true
            })
        }

        install(HttpCookies) {
            storage = cookieStorage
        }

        install(DefaultRequest) {
            header(HttpHeaders.UserAgent, "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/139.0.0.0 Safari/537.36 OPR/123.0.0.0")
        }
    }

    @Throws(IOException::class, SerializationException::class, CancellationException::class, IllegalStateException::class)
    suspend fun connect(login: String, password: String) {
        client.get(Endpoints.authStep1)
        client.submitForm(url = Endpoints.authStep2, formParameters = parameters {
            append("action", "login")
            append("login", login)
            append("pass", password)
        })
        client.get(Endpoints.authStep3)

        if (client.get(Endpoints.url("Me")).status.value != 200)
            error("Invalid credentials")

        users = data<Users>("Users").toUserMap()
        subjects = data<Subjects>("Subjects").toMap()
        colors = data<Colors>("Colors").toMap()
        classrooms = data<Classrooms>("Classrooms").toMap()
    }

    @Throws(IOException::class, SerializationException::class, CancellationException::class)
    private suspend inline fun <reified T> data(location: String): T {
        return client.get(Endpoints.url(location)).body()
    }

    @Throws(IOException::class, SerializationException::class, CancellationException::class)
    suspend fun me(): com.github.aeoliux.violet.api.types.Me {
        return data<Me>("Me").toMeData()
    }

    @Throws(IOException::class, SerializationException::class, CancellationException::class)
    suspend fun classInfo(): ClassInfo {
        return data<Class>("Classes").toClassInfo(users)
    }

    @Throws(IOException::class, SerializationException::class, CancellationException::class)
    suspend fun grades(): LinkedHashMap<String, List<Grade>> {
        val categories = data<GradesCategories>("Grades/Categories")
        val comments = data<GradesComments>("Grades/Comments")
        val gradesData = data<Grades>("Grades")

        return gradesData.toGrades(categories, comments, users, subjects, colors)
    }

    @Throws(IOException::class, SerializationException::class, CancellationException::class)
    suspend fun luckyNumber(): Pair<Int, LocalDate> {
        return data<LuckyNumbers>("LuckyNumbers").parse()
    }

    @OptIn(ExperimentalTime::class)
    @Throws(IOException::class, SerializationException::class, CancellationException::class)
    suspend fun timetable(
        weekStarts: LocalDate
    ): Timetable =
        data<Timetables>("Timetables?weekStart=${weekStarts}").toTimetableMap(classrooms)

    @Throws(IOException::class, SerializationException::class, CancellationException::class, IllegalStateException::class)
    suspend fun attendance(): Attendance {
        return data<Attendances>("Attendances").toAttendanceMap(
            colors,
            data<AttendancesTypes>("Attendances/Types").Types,
            users
        )
    }

    @Throws(IOException::class, SerializationException::class, CancellationException::class)
    suspend fun agenda(): Agenda {
        return data<Homeworks>("HomeWorks")
            .toAgenda(
                data<HomeworkCategories>("HomeWorks/Categories").toMap(colors),
                users,
                classrooms,
                subjects
            )
    }

    @Throws(IOException::class, SerializationException::class, CancellationException::class)
    suspend fun schoolNotices(): List<SchoolNotice> {
        return data<SchoolNotices>("SchoolNotices").toSNList(users)
    }
}

typealias Timetable = LinkedHashMap<LocalDate, LinkedHashMap<LocalTime, List<Lesson>>>
typealias Attendance = LinkedHashMap<LocalDate, LinkedHashMap<Int, AttendanceItem>>
typealias Agenda = LinkedHashMap<LocalDate, LinkedHashMap<Int, List<AgendaItem>>>