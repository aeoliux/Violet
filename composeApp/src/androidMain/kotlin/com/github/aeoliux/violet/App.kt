package com.github.aeoliux.violet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.github.aeoliux.violet.app.content.MainContentView
import com.github.aeoliux.violet.app.login.LoginView
import com.github.aeoliux.violet.repositories.ClientManager
import org.koin.compose.koinInject

@Composable
fun App(colorScheme: ColorScheme) {
    val clientManager: ClientManager = koinInject()
    var logState by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        clientManager.logStateFlow.collect { logState = it }
    }

    MaterialExpressiveTheme(
        colorScheme = colorScheme,
        typography = Typography()
    ) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (logState) {
                true -> MainContentView()
                false -> LoginView()
            }
        }
    }
}