package com.github.aeoliux.violet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.github.aeoliux.violet.app.storage.AppDatabase
import com.github.aeoliux.violet.app.storage.Database
import com.github.aeoliux.violet.app.storage.DatabaseDriverFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Database.open(DatabaseDriverFactory(context = this).createDriver())

        setContent {
            App(Keychain(this))
        }
    }
}
