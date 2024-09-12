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
        logIn(keychain, login, password)

        val me = AppContext.client.value.me()
        Database.setAboutMe(me)

        val grades = AppContext.client.value.grades()
        Database.insertGrades(grades)

        val classInfo = AppContext.client.value.classInfo()
        Database.setClassInfo(classInfo)

        val luckyNumber = AppContext.client.value.luckyNumber()
        Database.setLuckyNumber(luckyNumber)

        val timetable = AppContext.client.value.timetable()
        Database.insertLessons(timetable)

        val attendances = AppContext.client.value.attendance()
        Database.insertAttendances(attendances)

        AppContext.semester.value = classInfo.semester
        AppContext.databaseUpdated.value = !AppContext.databaseUpdated.value

        return true
    } catch (e: Exception) {
        showAlert("Fetching data error", e)
        return false
    }
}