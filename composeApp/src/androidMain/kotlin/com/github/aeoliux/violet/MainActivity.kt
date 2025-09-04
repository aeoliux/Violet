package com.github.aeoliux.violet

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.github.aeoliux.violet.app.appModules
import com.github.aeoliux.violet.app.appState.BackgroundJob
import com.github.aeoliux.violet.app.appState.runBackgroundTask
import com.github.aeoliux.violet.app.appState.saveTo
import com.github.aeoliux.violet.app.appState.taskToBePerformed
import com.github.aeoliux.violet.app.keychainModule
import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.binds
import org.koin.dsl.module

class MainActivity : ComponentActivity() {
    public lateinit var createDocument: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        createDocument = registerForActivityResult(ActivityResultContracts.CreateDocument("application/octet-stream")) { uri ->
            uri?.let {
                saveTo(it, this)
            }
        }

        runBackgroundTask = { task ->
            taskToBePerformed = task
            startService(Intent(this, BackgroundJob::class.java))
        }

        startKoin {
            modules(appModules.plus(module {
                single { this@MainActivity } binds arrayOf(Context::class, MainActivity::class)
            }))
        }

        enableEdgeToEdge()

        setContent {
            App()
        }
    }
}
