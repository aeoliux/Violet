package com.github.aeoliux.violet.app.appState

import com.github.aeoliux.violet.storage.Database
import com.github.aeoliux.violet.storage.insertAttendances
import com.github.aeoliux.violet.storage.insertGrades
import com.github.aeoliux.violet.storage.insertLessons
import com.github.aeoliux.violet.storage.setAboutMe
import com.github.aeoliux.violet.storage.setClassInfo
import com.github.aeoliux.violet.storage.setLuckyNumber

suspend fun AppState.fetchData(login: String? = null, password: String? = null) {
    try {
        statusMessage.value = "Logging in to Synergia..."
        logIn(login, password)
        if (!isLoggedIn.value) {
            statusMessage.value = "Invalid credentials"
            return
        }

        statusMessage.value = "Getting your personal data..."
        val me = client.value.me()
        Database.setAboutMe(me)

        statusMessage.value = "Checking if you got 1..."
        val grades = client.value.grades()
        Database.insertGrades(grades)

        statusMessage.value = "What class are you in?"
        val classInfo = client.value.classInfo()
        Database.setClassInfo(classInfo)

        statusMessage.value = "What is today's lucky number?"
        val luckyNumber = client.value.luckyNumber()
        Database.setLuckyNumber(luckyNumber)

        statusMessage.value = "Let's check the timetable!"
        val timetable = client.value.timetable()
        Database.insertLessons(timetable)

        statusMessage.value = "Have you been at school?"
        val attendances = client.value.attendance()
        Database.insertAttendances(attendances)

        statusMessage.value = "Saving some data and refreshing the view..."
        semester.value = classInfo.semester
        databaseUpdated.value = !databaseUpdated.value

        statusMessage.value = null
    } catch (e: Exception) {
        showAlert("Fetching data error", e)
    }
}