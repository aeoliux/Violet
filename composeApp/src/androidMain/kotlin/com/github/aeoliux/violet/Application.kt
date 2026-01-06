package com.github.aeoliux.violet

import android.app.Application
import android.content.Context
import com.github.aeoliux.violet.app.content.agenda.AgendaViewModel
import com.github.aeoliux.violet.app.content.attendance.AttendanceViewModel
import com.github.aeoliux.violet.app.content.grades.GradesBySubjectViewModel
import com.github.aeoliux.violet.app.content.grades.GradesViewModel
import com.github.aeoliux.violet.app.content.home.HomeViewModel
import com.github.aeoliux.violet.app.content.messages.MessageEditorViewModel
import com.github.aeoliux.violet.app.content.messages.MessageViewModel
import com.github.aeoliux.violet.app.content.messages.MessagesViewModel
import com.github.aeoliux.violet.app.content.schoolNotices.SchoolNoticesViewModel
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

class Application: Application() {
    override fun onCreate() {
        val platformModule = module {
            single { this@Application } bind Context::class
            single { getRoomDatabase(getDatabaseBuilder(this@Application)) } bind AppDatabase::class
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
            viewModelOf(::SchoolNoticesViewModel)
        }

        initializeKoin(platformModule)

        super.onCreate()
    }
}