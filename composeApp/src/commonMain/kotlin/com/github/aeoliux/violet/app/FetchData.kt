package com.github.aeoliux.violet.app

import com.github.aeoliux.violet.*
import com.github.aeoliux.violet.storage.*

suspend fun logIn(keychain: Keychain, login: String?, password: String?) {
    var login = login
    var password = password

    if (login == null || password == null) {
        val usernameAndPass = keychain.getPass()
            ?: throw IllegalStateException("Username and password not found in keychain")
        val indexOfSpace = usernameAndPass.indexOfFirst { it == ' ' }
        login = usernameAndPass.slice(0..<indexOfSpace)
        password = usernameAndPass.slice(indexOfSpace + 1..<usernameAndPass.length)
    }

    Context.client.value.proceedLogin(login, password)
}

suspend fun fetchData(keychain: Keychain, login: String? = null, password: String? = null): Boolean {
    try {
        logIn(keychain, login, password)

        val me = Context.client.value.me()
        Database.setAboutMe(me)

        val grades = Context.client.value.grades()
        Database.insertGrades(grades)

        val classInfo = Context.client.value.classInfo()
        Database.setClassInfo(classInfo)

        val luckyNumber = Context.client.value.luckyNumber()
        Database.setLuckyNumber(luckyNumber)

        Context.semester.value = classInfo.semester
        Context.databaseUpdated.value = !Context.databaseUpdated.value

        return true
    } catch (e: Exception) {
        showAlert("Fetching data error", e)
        return false
    }
}