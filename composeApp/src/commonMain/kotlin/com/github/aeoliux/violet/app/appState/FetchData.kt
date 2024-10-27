package com.github.aeoliux.violet.app.appState

import com.github.aeoliux.violet.app.storage.Database
import com.github.aeoliux.violet.app.storage.insertAgenda
import com.github.aeoliux.violet.app.storage.insertAttendances
import com.github.aeoliux.violet.app.storage.insertGrades
import com.github.aeoliux.violet.app.storage.insertLessons
import com.github.aeoliux.violet.app.storage.insertSchoolNotices
import com.github.aeoliux.violet.app.storage.selectClassInfo
import com.github.aeoliux.violet.app.storage.setAboutMe
import com.github.aeoliux.violet.app.storage.setClassInfo
import com.github.aeoliux.violet.app.storage.setLuckyNumber

suspend fun AppState.fetchData(login: String? = null, password: String? = null) {
    try {
        setFetchStatus("Logging in to Synergia...") { logIn(login, password) }
        if (!isLoggedIn.value) {
            statusMessage.value = "Invalid credentials"
            return
        }

        setFetchStatus("Getting your personal data...") {
            Database.setAboutMe(
                client.value.me()
            )
        }

        setFetchStatus("Checking if you got 1...") {
            Database.insertGrades(
                client.value.grades()
            )
        }

        val classInfo = setFetchStatus("What class are you in?") {
            val classInfo = client.value.classInfo()
            Database.setClassInfo(classInfo)

            classInfo
        }

        setFetchStatus("What is today's lucky number?") {
            Database.setLuckyNumber(
                client.value.luckyNumber()
            )
        }

        setFetchStatus("Let's check the timetable!") {
            Database.insertLessons(
                client.value.timetable()
            )
        }

        setFetchStatus("Have you been at school?") {
            Database.insertAttendances(
                client.value.attendance()
            )
        }

        setFetchStatus("What do you need to do now?") {
            Database.insertAgenda(
                client.value.agenda()
            )
        }

        setFetchStatus("Does your school do anything?") {
            Database.insertSchoolNotices(
                client.value.schoolNotices()
            )
        }

        statusMessage.value = "Saving some data and refreshing the view..."
        semester.value = classInfo.semester
        databaseUpdated.value = !databaseUpdated.value

        statusMessage.value = null
    } catch (e: Exception) {
        showAlert("Fetching data error", e)
    }
}

suspend fun <T> AppState.setFetchStatus(status: String, operation: suspend () -> T): T {
    statusMessage.value = status
    val ret = operation()
    statusMessage.value = null

    return ret
}