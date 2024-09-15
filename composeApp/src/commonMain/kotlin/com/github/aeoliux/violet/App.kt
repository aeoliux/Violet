package com.github.aeoliux.violet

import androidx.compose.material.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.github.aeoliux.violet.app.login.LoginView
import com.github.aeoliux.violet.app.MainView
import com.github.aeoliux.violet.storage.Database
import com.github.aeoliux.violet.storage.selectAboutMe
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App(keychain: Keychain) {
    MaterialTheme {
        LaunchedEffect(Unit) {
            AppContext.isLoggedIn.value = Database.selectAboutMe() != null
        }

        MainView(keychain)

        if (AppContext.showAlert.value) {
            AlertDialog(
                onDismissRequest = { AppContext.showAlert.value = false },
                confirmButton = { TextButton(onClick = {
                    AppContext.showAlert.value = false
                }) { Text("Ok") } },
                title = { Text(AppContext.alertTitle.value) },
                text = { Text(AppContext.alertMessage.value) }
            )
        }
    }
}
