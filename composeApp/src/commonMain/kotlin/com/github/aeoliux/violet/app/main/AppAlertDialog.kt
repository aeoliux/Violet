package com.github.aeoliux.violet.app.main

import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import com.github.aeoliux.violet.app.appState.LocalAppState

@Composable
fun AppAlertDialog() {
    val appState = LocalAppState.current

    if (appState.showAlert.value) {
        AlertDialog(
            onDismissRequest = { appState.showAlert.value = false },
            confirmButton = { TextButton(onClick = {
                appState.showAlert.value = false
            }) { Text("Ok") } },
            title = { Text(appState.alertTitle.value) },
            text = { Text(appState.alertMessage.value) }
        )
    }
}