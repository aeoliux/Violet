package com.github.aeoliux

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.github.aeoliux.app.content.MainContentView
import com.github.aeoliux.app.login.LoginView
import com.github.aeoliux.repositories.ClientManager
import org.koin.compose.koinInject

@Composable
fun App() {
    val clientManager: ClientManager = koinInject()
    var logState by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        clientManager.logStateFlow.collect { logState = it }
    }

    MaterialExpressiveTheme(
        colorScheme = if (isSystemInDarkTheme())
            darkColorScheme()
        else
            lightColorScheme(),
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