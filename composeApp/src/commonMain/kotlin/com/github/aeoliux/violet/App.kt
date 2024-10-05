package com.github.aeoliux.violet

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import com.github.aeoliux.violet.app.appState.AppState
import com.github.aeoliux.violet.app.appState.LocalAppState
import com.github.aeoliux.violet.app.main.AppAlertDialog
import com.github.aeoliux.violet.app.main.MainView
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App(keychain: Keychain) {
    MaterialTheme {
        val appState = remember { AppState().setKeychain(keychain) }

        LaunchedEffect(Unit) {
            appState.appLaunchedEffect()
        }

        CompositionLocalProvider(LocalAppState provides appState) {
            MainView()
            AppAlertDialog()
        }
    }
}
