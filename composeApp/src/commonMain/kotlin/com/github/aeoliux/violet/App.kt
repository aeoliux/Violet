package com.github.aeoliux.violet

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.github.aeoliux.violet.app.appState.AppState
import com.github.aeoliux.violet.app.appState.LocalAppState
import com.github.aeoliux.violet.app.main.AppAlertDialog
import com.github.aeoliux.violet.app.main.MainView
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App(keychain: Keychain) {
    MaterialTheme(
        colorScheme = if (isSystemInDarkTheme())
            darkColorScheme()
        else
            lightColorScheme(),
        typography = Typography()
    ) {
        val appState = remember { AppState().setKeychain(keychain) }

        LaunchedEffect(Unit) {
            appState.appLaunchedEffect()
        }

        CompositionLocalProvider(LocalAppState provides appState) {
            Box(Modifier.background(MaterialTheme.colorScheme.background)) {
                Box(Modifier.statusBarsPadding()) {
                    MainView()
                    AppAlertDialog()
                }
            }
        }
    }
}
