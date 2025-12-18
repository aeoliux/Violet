package com.github.aeoliux.violet

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import com.github.aeoliux.violet.app.content.attendance.AttendanceViewModel
import com.github.aeoliux.violet.app.content.agenda.AgendaViewModel
import com.github.aeoliux.violet.app.content.grades.GradesBySubjectViewModel
import com.github.aeoliux.violet.app.content.grades.GradesViewModel
import com.github.aeoliux.violet.app.content.home.HomeViewModel
import com.github.aeoliux.violet.app.content.messages.MessageEditorViewModel
import com.github.aeoliux.violet.app.content.messages.MessageViewModel
import com.github.aeoliux.violet.app.content.messages.MessagesViewModel
import com.github.aeoliux.violet.app.content.timetable.TimetableViewModel
import com.github.aeoliux.violet.app.login.LoginViewModel
import com.github.aeoliux.violet.repositories.Keychain
import com.github.aeoliux.violet.storage.AppDatabase
import com.github.aeoliux.violet.storage.getDatabaseBuilder
import com.github.aeoliux.violet.storage.getRoomDatabase
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val platformModule = module {
            single { this@MainActivity } bind Context::class
            single { getRoomDatabase(getDatabaseBuilder(this@MainActivity)) } bind AppDatabase::class
            singleOf(::Keychain)

            viewModelOf(::LoginViewModel)
            viewModelOf(::HomeViewModel)
            viewModelOf(::GradesViewModel)
            viewModelOf(::TimetableViewModel)
            viewModelOf(::GradesBySubjectViewModel)
            viewModelOf(::MessagesViewModel)
            viewModelOf(::MessageViewModel)
            viewModelOf(::AgendaViewModel)
            viewModelOf(::AttendanceViewModel)
            viewModelOf(::MessageEditorViewModel)
        }

        initializeKoin(platformModule)

        enableEdgeToEdge()
        setContent {
            App(if (isSystemInDarkTheme())
                dynamicDarkColorScheme(this)
            else
                dynamicLightColorScheme(this))
        }
    }
}

@Composable
fun AppAndroidPreview() {
    App(lightColorScheme())
}