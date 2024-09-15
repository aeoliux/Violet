package com.github.aeoliux.violet.app.login

import androidx.compose.foundation.interaction.FocusInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.aeoliux.violet.AppContext
import com.github.aeoliux.violet.Keychain
import com.github.aeoliux.violet.app.components.LoadingIndicator
import com.github.aeoliux.violet.app.fetchData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun LoginView(keychain: Keychain) {
    val coroutineScope = rememberCoroutineScope()
    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showLoadingIndicator by remember { mutableStateOf(false) }

    val loginFocus = remember { FocusRequester() }
    val passwordFocus = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    val logIn = {
        coroutineScope.launch {
            showLoadingIndicator = true

            AppContext.isLoggedIn.value = fetchData(keychain, login, password)
            if (AppContext.isLoggedIn.value)
                keychain.savePass("$login $password")

            showLoadingIndicator = false
        }

        Unit
    }

    if (showLoadingIndicator)
        LoadingIndicator()
    else
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(50.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(Modifier.padding(bottom = 5.dp)) {
                Column(
                    Modifier.wrapContentSize().padding(10.dp),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Log in to your S***rgia account",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )

                    Text(
                        text = "Login and password are stored locally in the keychain of this device and they are being sent only to L*brus"
                    )
                }
            }

            TextField(login, { login = it },
                placeholder = { Text("Login") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
                    .focusRequester(loginFocus),
                keyboardActions = KeyboardActions(onNext = { passwordFocus.requestFocus() }),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next
                ),
            )
            TextField(password, { password = it },
                placeholder = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
                    .focusRequester(passwordFocus),
                keyboardActions = KeyboardActions(onDone = {
                    focusManager.clearFocus()
                    logIn()
                })
            )
            Button(logIn) {
                Text("Log in")
            }
        }
}