package com.github.aeoliux

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import com.github.aeoliux.app.content.grades.GradesBySubjectViewModel
import com.github.aeoliux.app.content.grades.GradesViewModel
import com.github.aeoliux.app.content.home.HomeViewModel
import com.github.aeoliux.app.content.timetable.TimetableViewModel
import com.github.aeoliux.app.login.LoginViewModel
import com.github.aeoliux.repositories.Keychain
import com.github.aeoliux.storage.AppDatabase
import com.github.aeoliux.storage.getDatabaseBuilder
import com.github.aeoliux.storage.getRoomDatabase
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
        }

        initializeKoin(platformModule)

        enableEdgeToEdge()
        setContent {
            App()
        }
    }
}

@Composable
fun AppAndroidPreview() {
    App()
}