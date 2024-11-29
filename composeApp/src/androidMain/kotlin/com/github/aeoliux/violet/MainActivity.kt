package com.github.aeoliux.violet

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.github.aeoliux.violet.app.storage.Database
import com.github.aeoliux.violet.app.storage.DatabaseDriverFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Database.open(DatabaseDriverFactory(context = this).createDriver())
        enableEdgeToEdge()

        setContent {
            App(Keychain(this))
        }
    }
}
