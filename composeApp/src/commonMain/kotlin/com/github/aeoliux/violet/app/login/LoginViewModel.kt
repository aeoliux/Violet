package com.github.aeoliux.violet.app.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.aeoliux.violet.app.appState.AppState
import com.github.aeoliux.violet.app.appState.fetchData
import com.github.aeoliux.violet.app.appState.logIn
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val appState: AppState
): ViewModel() {
    private var _login = MutableStateFlow("")
    val login get() = _login.asStateFlow()

    private var _password = MutableStateFlow("")
    val password get() = _password.asStateFlow()

    private var _showLoadingIndicator = MutableStateFlow(false)
    val showLoadingIndicator get() = _showLoadingIndicator.asStateFlow()

    fun logIn() {
        viewModelScope.launch {
            _showLoadingIndicator.update { true }

            appState.fetchData(login.value, password.value)

            _showLoadingIndicator.update { false }

            _login.update { "" }
            _password.update { "" }
        }
    }

    fun updateLogin(login: String) {
        viewModelScope.launch {
            _login.update { login }
        }
    }

    fun updatePassword(password: String) {
        viewModelScope.launch {
            _password.update { password }
        }
    }
}