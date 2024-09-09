package com.github.aeoliux.violet.api

import com.github.aeoliux.violet.api.bodys.Class
import com.github.aeoliux.violet.api.bodys.Classrooms
import com.github.aeoliux.violet.api.bodys.Colors
import com.github.aeoliux.violet.api.bodys.LuckyNumbers
import com.github.aeoliux.violet.api.bodys.Me
import com.github.aeoliux.violet.api.bodys.Subjects
import com.github.aeoliux.violet.api.bodys.Timetables
import com.github.aeoliux.violet.api.bodys.Users
import com.github.aeoliux.violet.api.bodys.grades.Grades
import com.github.aeoliux.violet.api.bodys.grades.GradesCategories
import com.github.aeoliux.violet.api.bodys.grades.GradesComments
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.cookies.HttpCookies
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.get
import io.ktor.http.parameters
import io.ktor.serialization.kotlinx.json.json
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.serialization.json.Json

class ApiClient {
    var users = LinkedHashMap<UInt, User>()
    var subjects = LinkedHashMap<UInt, String>()
    var colors = LinkedHashMap<UInt, String>()
    var classrooms = LinkedHashMap<UInt, String>()

    private val client = HttpClient {
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

    suspend fun me(): com.github.aeoliux.violet.api.Me {
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

    suspend fun luckyNumber(): Pair<UInt, LocalDate> {
        return data<LuckyNumbers>("LuckyNumbers").parse()
    }

    suspend fun timetable(): Timetable {
        return data<Timetables>("Timetables").toTimetableMap(classrooms)
    }
}

typealias Timetable = LinkedHashMap<LocalDate, LinkedHashMap<LocalTime, List<Lesson>>>
