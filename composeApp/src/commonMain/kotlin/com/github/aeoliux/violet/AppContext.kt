package com.github.aeoliux.violet

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.github.aeoliux.violet.api.ApiClient

object AppContext {
    var client: MutableState<ApiClient> = mutableStateOf(ApiClient())
    var isLoggedIn: MutableState<Boolean> = mutableStateOf(false)
    var semester: MutableState<UInt> = mutableStateOf(0u)

    val showAlert: MutableState<Boolean> = mutableStateOf(false)
    var alertTitle: MutableState<String> = mutableStateOf("")
    var alertMessage: MutableState<String> = mutableStateOf("")

    var databaseUpdated = mutableStateOf(false)
    var statusMessage = mutableStateOf<String?>(null)
}