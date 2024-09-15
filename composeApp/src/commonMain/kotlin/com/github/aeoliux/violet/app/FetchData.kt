package com.github.aeoliux.violet.app

import com.github.aeoliux.violet.*
import com.github.aeoliux.violet.storage.*

suspend fun logIn(keychain: Keychain, login: String? = null, password: String? = null) {
    var login = login
    var password = password

    if (login == null || password == null) {
        val usernameAndPass = keychain.getPass()
            ?: throw IllegalStateException("Username and password not found in keychain")
        val indexOfSpace = usernameAndPass.indexOfFirst { it == ' ' }
        login = usernameAndPass.slice(0..<indexOfSpace)
        password = usernameAndPass.slice(indexOfSpace + 1..<usernameAndPass.length)
    }

    AppContext.client.value.proceedLogin(login, password)
}

suspend fun fetchData(keychain: Keychain, login: String? = null, password: String? = null): Boolean {
    try {
        AppContext.statusMessage.value = "Logging in to Synergia..."
        logIn(keychain, login, password)

        AppContext.statusMessage.value = "Getting your personal data..."
        val me = AppContext.client.value.me()
        Database.setAboutMe(me)

        AppContext.statusMessage.value = "Checking if you got 1..."
        val grades = AppContext.client.value.grades()
        Database.insertGrades(grades)

        AppContext.statusMessage.value = "What class are you in?"
        val classInfo = AppContext.client.value.classInfo()
        Database.setClassInfo(classInfo)

        AppContext.statusMessage.value = "What is today's lucky number?"
        val luckyNumber = AppContext.client.value.luckyNumber()
        Database.setLuckyNumber(luckyNumber)

        AppContext.statusMessage.value = "Let's check the timetable!"
        val timetable = AppContext.client.value.timetable()
        Database.insertLessons(timetable)

        AppContext.statusMessage.value = "Have you been at school?"
        val attendances = AppContext.client.value.attendance()
        Database.insertAttendances(attendances)

        AppContext.statusMessage.value = "Saving some data and refreshing the view..."
        AppContext.semester.value = classInfo.semester
        AppContext.databaseUpdated.value = !AppContext.databaseUpdated.value

        AppContext.statusMessage.value = null

        return true
    } catch (e: Exception) {
        showAlert("Fetching data error", e)
        return false
    }
}