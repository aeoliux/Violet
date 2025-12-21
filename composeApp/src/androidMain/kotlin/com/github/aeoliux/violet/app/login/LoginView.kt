package com.github.aeoliux.violet.app.login

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LoginView(viewModel: LoginViewModel = koinViewModel<LoginViewModel>()) {
    val login by viewModel.login.collectAsState()
    val password by viewModel.password.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()

    val loginFocusRequester = remember { FocusRequester() }
    val passwordFocusRequester = remember { FocusRequester() }

    PullToRefreshBox(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 70.dp),
        isRefreshing = isRefreshing,
        onRefresh = {  }
    ) {
        Box(
            Modifier
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .padding(40.dp)
            ) {
                Text(
                    text = "Log in to S*nergia",
                    fontSize = 32.sp,
                    color = MaterialTheme.colorScheme.primary
                )

                Text(
                    text = "Use your S*nergia credentials. Password is only sent to L*brus and it will only be stored locally on the device in secure Android keychain.",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.primary
                )

                HorizontalDivider(Modifier.padding(top = 10.dp, bottom = 10.dp))

                TextField(
                    value = login,
                    placeholder = { Text("Login") },
                    onValueChange = { viewModel.setLogin(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 5.dp, bottom = 5.dp)
                        .focusRequester(loginFocusRequester),

                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Unspecified,
                        autoCorrectEnabled = false,
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),

                    keyboardActions = KeyboardActions(
                        onNext = { passwordFocusRequester.requestFocus() }
                    )
                )

                TextField(
                    value = password,
                    placeholder = { Text("Password") },
                    onValueChange = { viewModel.setPassword(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 5.dp, bottom = 5.dp)
                        .focusRequester(passwordFocusRequester),

                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Unspecified,
                        autoCorrectEnabled = false,
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),

                    keyboardActions = KeyboardActions(
                        onDone = { viewModel.proceed() }
                    ),

                    visualTransformation = PasswordVisualTransformation()
                )

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 5.dp, bottom = 5.dp),
                    onClick = { viewModel.proceed() }
                ) {
                    Text("Log in")
                }
            }
        }
    }
}