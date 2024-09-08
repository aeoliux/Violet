package com.github.aeoliux.violet.app.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import com.github.aeoliux.violet.Context
import com.github.aeoliux.violet.Keychain
import com.github.aeoliux.violet.app.components.LoadingIndicator
import com.github.aeoliux.violet.app.fetchData
import com.github.aeoliux.violet.showAlert
import kotlinx.coroutines.launch

@Composable
fun LoginView(keychain: Keychain) {
    val coroutineScope = rememberCoroutineScope()
    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showLoadingIndicator by remember { mutableStateOf(false) }

    if (showLoadingIndicator)
        LoadingIndicator()
    else
        Column(verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize()) {
            TextField(login, { login = it })
            TextField(password, { password = it }, visualTransformation = PasswordVisualTransformation())
            Button({ coroutineScope.launch {
                showLoadingIndicator = true

                Context.isLoggedIn.value = fetchData(keychain, login, password)
                if (Context.isLoggedIn.value)
                    keychain.savePass("$login $password")

                showLoadingIndicator = false
            } }) {
                Text("Log in")
            }
        }
}