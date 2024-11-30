package com.github.aeoliux.violet.app.appState

suspend fun AppState.logIn(login: String? = null, password: String? = null) {
    try {
        var login = login
        var password = password


        if (login == null || password == null) {
            val usernameAndPass = keychain.getPass()
                ?: throw IllegalStateException("Username and password not found in keychain")
            println(usernameAndPass)
            val indexOfSpace = usernameAndPass.indexOfFirst { it == ' ' }
            login = usernameAndPass.slice(0..<indexOfSpace)
            password = usernameAndPass.slice(indexOfSpace + 1..<usernameAndPass.length)
        }

        client.value.proceedLogin(login, password)
        val data = "$login $password"
        keychain.savePass(data)
        isLoggedIn.value = true
    } catch (e: Exception) {
        showAlert("Log in", e)
    }
}