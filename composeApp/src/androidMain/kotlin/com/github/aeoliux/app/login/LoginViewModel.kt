package com.github.aeoliux.app.login

import com.github.aeoliux.app.RefreshableViewModel
import com.github.aeoliux.repositories.ClientManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LoginViewModel(
    private val clientManager: ClientManager
): RefreshableViewModel() {
    private val _login = MutableStateFlow("")
    val login get() = this._login.asStateFlow()

    private val _password = MutableStateFlow("")
    val password get() = this._password.asStateFlow()

    fun proceed() {
        task {
            clientManager.login(_login.value, _password.value)
        }
    }

    fun setLogin(v: String) = _login.update { v }
    fun setPassword(v: String) = _password.update { v }
}