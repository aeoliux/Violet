package com.github.aeoliux.violet

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.github.aeoliux.violet.app.appState.BackgroundJob
import com.github.aeoliux.violet.app.appState.runBackgroundTask
import com.github.aeoliux.violet.app.appState.taskToBePerformed
import com.github.aeoliux.violet.app.storage.Database
import com.github.aeoliux.violet.app.storage.DatabaseDriverFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        runBackgroundTask = { task ->
            taskToBePerformed = task
            startService(Intent(this, BackgroundJob::class.java))
        }

        Database.open(DatabaseDriverFactory(context = this).createDriver())
        enableEdgeToEdge()

        setContent {
            App(Keychain(this))
        }
    }
}
