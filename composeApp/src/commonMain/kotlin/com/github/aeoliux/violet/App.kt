package com.github.aeoliux.violet

import androidx.compose.material.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import app.cash.sqldelight.db.SqlDriver
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
            Context.isLoggedIn.value = Database.selectAboutMe() != null
        }

        if (Context.isLoggedIn.value) {
            MainView(keychain)
        } else {
            LoginView(keychain)
        }

        if (Context.showAlert.value) {
            AlertDialog(
                onDismissRequest = { Context.showAlert.value = false },
                confirmButton = { TextButton(onClick = {
                    Context.showAlert.value = false
                }) { Text("Ok") } },
                title = { Text(Context.alertTitle.value) },
                text = { Text(Context.alertMessage.value) }
            )
        }
    }
}
