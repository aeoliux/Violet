package com.github.aeoliux.violet.app.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.aeoliux.violet.app.appState.LocalAppState

@Composable
fun SettingsView(
    vm: SettingsViewModel = viewModel { SettingsViewModel() }
) {
    val appState = LocalAppState.current

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Button({
            vm.logOut(appState.keychain)
            appState.isLoggedIn.value = false
        }) {
            Text("Log out")
        }
    }
}