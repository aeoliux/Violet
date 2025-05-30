package com.github.aeoliux.violet.app.login

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.aeoliux.violet.Keychain
import com.github.aeoliux.violet.api.ApiClient
import com.github.aeoliux.violet.app.storage.AboutUserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val client: ApiClient,
    private val keychain: Keychain,
    private val aboutUserRepository: AboutUserRepository
): ViewModel() {
    private var _login = MutableStateFlow("")
    val login get() = _login.asStateFlow()

    private var _password = MutableStateFlow("")
    val password get() = _password.asStateFlow()

    private var _showLoadingIndicator = MutableStateFlow(false)
    val showLoadingIndicator get() = _showLoadingIndicator.asStateFlow()

    fun logIn(output: MutableState<Boolean>, onSuccess: suspend () -> Unit) {
        viewModelScope.launch {
            _showLoadingIndicator.update { true }

            try {
                client.proceedLogin(_login.value, _password.value)
                val me = client.me()
                keychain.savePass("${_login.value} ${_password.value}")
                aboutUserRepository.insertMe(me)

                _login.update { "" }
                _password.update { "" }

                onSuccess()
            } catch (e: Exception) {

                println(e)
            }

            output.value = !output.value
            _showLoadingIndicator.update { false }
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