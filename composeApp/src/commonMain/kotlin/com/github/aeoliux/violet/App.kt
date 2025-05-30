package com.github.aeoliux.violet

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.github.aeoliux.violet.app.appState.AppState
import com.github.aeoliux.violet.app.appState.LocalAppState
import com.github.aeoliux.violet.app.main.AppAlertDialog
import com.github.aeoliux.violet.app.main.MainView
import com.github.aeoliux.violet.app.storage.AboutUserRepository
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject

@Composable
@Preview
fun App() {
    MaterialTheme(
        colorScheme = if (isSystemInDarkTheme())
            darkColorScheme()
        else
            lightColorScheme(),
        typography = Typography()
    ) {
        val appState = remember { AppState() }
        val aboutUserRepository = koinInject<AboutUserRepository>()

        LaunchedEffect(appState.databaseUpdated.value) {
            appState.isLoggedIn.value = aboutUserRepository.getMe() != null
        }

        CompositionLocalProvider(LocalAppState provides appState) {
            Box(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
                Box(Modifier.fillMaxSize().statusBarsPadding()) {
                    MainView()
                    AppAlertDialog()
                }
            }
        }
    }
}
