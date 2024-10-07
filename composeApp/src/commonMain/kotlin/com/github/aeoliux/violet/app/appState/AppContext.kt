package com.github.aeoliux.violet.app.appState

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.mutableStateOf
import com.github.aeoliux.violet.Keychain
import com.github.aeoliux.violet.api.ApiClient
import com.github.aeoliux.violet.app.storage.Database
import com.github.aeoliux.violet.app.storage.selectAboutMe

class AppState(
    var client: MutableState<ApiClient> = mutableStateOf(ApiClient()),
    var isLoggedIn: MutableState<Boolean> = mutableStateOf(false),
    var semester: MutableState<UInt> = mutableStateOf(0u),

    var showAlert: MutableState<Boolean> = mutableStateOf(false),
    var alertTitle: MutableState<String> = mutableStateOf(""),
    var alertMessage: MutableState<String> = mutableStateOf(""),

    var databaseUpdated: MutableState<Boolean> = mutableStateOf(false),
    var statusMessage: MutableState<String?> = mutableStateOf<String?>(null),
) {
    lateinit var keychain: Keychain

    fun setKeychain(keychain: Keychain): AppState {
        this.keychain = keychain
        return this
    }

    fun appLaunchedEffect() {
        isLoggedIn.value = Database.selectAboutMe() != null
    }
}

val LocalAppState = compositionLocalOf { AppState() }