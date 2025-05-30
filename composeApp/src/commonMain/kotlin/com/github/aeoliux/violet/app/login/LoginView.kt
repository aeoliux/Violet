package com.github.aeoliux.violet.app.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.aeoliux.violet.app.appState.LocalAppState
import com.github.aeoliux.violet.app.components.LoadingIndicator
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LoginView(onSuccess: suspend () -> Unit, vm: LoginViewModel = koinViewModel<LoginViewModel>()) {
    val appState = LocalAppState.current

    val login by vm.login.collectAsState()
    val password by vm.password.collectAsState()
    val showLoadingIndicator by vm.showLoadingIndicator.collectAsState()

    val loginFocus = remember { FocusRequester() }
    val passwordFocus = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

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

            TextField(login, { vm.updateLogin(it) },
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
            TextField(password, { vm.updatePassword(it) },
                placeholder = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
                    .focusRequester(passwordFocus),
                keyboardActions = KeyboardActions(onDone = {
                    focusManager.clearFocus()
                    vm.logIn(appState.databaseUpdated, onSuccess)
                }),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                )
            )
            Button({
                vm.logIn(appState.databaseUpdated, {
                    onSuccess()
                })
            }) {
                Text("Log in")
            }
        }
}