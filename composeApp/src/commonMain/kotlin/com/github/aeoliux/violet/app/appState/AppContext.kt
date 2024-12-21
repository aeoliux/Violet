package com.github.aeoliux.violet.app.appState

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.mutableStateOf

class AppState {
    var isLoggedIn: MutableState<Boolean> = mutableStateOf(false)
    var semester: MutableState<Int> = mutableStateOf(0)

    var showAlert: MutableState<Boolean> = mutableStateOf(false)
    var alertTitle: MutableState<String> = mutableStateOf("")
    var alertMessage: MutableState<String> = mutableStateOf("")

    var databaseUpdated: MutableState<Boolean> = mutableStateOf(false)
    var statusMessage: MutableState<String?> = mutableStateOf(null)

    var safeMode: MutableState<Boolean> = mutableStateOf(false)

    fun <T> safe(yes: T, no: T): T {
        return if (safeMode.value)
            yes
        else
            no
    }
}

val LocalAppState = compositionLocalOf { AppState() }