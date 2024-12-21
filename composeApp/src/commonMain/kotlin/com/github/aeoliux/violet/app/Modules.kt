package com.github.aeoliux.violet.app

import com.github.aeoliux.violet.api.ApiClient
import com.github.aeoliux.violet.app.agenda.AgendaViewModel
import com.github.aeoliux.violet.app.appState.AppState
import com.github.aeoliux.violet.app.attendance.AttendanceViewModel
import com.github.aeoliux.violet.app.grades.GradesViewModel
import com.github.aeoliux.violet.app.home.HomeViewModel
import com.github.aeoliux.violet.app.login.LoginViewModel
import com.github.aeoliux.violet.app.main.MainViewModel
import com.github.aeoliux.violet.app.messages.MessageViewModel
import com.github.aeoliux.violet.app.messages.MessagesViewModel
import com.github.aeoliux.violet.app.schoolNotices.SchoolNoticesViewModel
import com.github.aeoliux.violet.app.settings.SettingsViewModel
import com.github.aeoliux.violet.app.storage.AboutUserRepository
import com.github.aeoliux.violet.app.storage.AgendaRepository
import com.github.aeoliux.violet.app.storage.AttendanceRepository
import com.github.aeoliux.violet.app.storage.GradesRepository
import com.github.aeoliux.violet.app.storage.LuckyNumberRepository
import com.github.aeoliux.violet.app.storage.MessageLabelsRepository
import com.github.aeoliux.violet.app.storage.SchoolNoticesRepository
import com.github.aeoliux.violet.app.storage.TimetableRepository
import com.github.aeoliux.violet.app.timetable.TimetableViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

expect val keychainModule: Module
expect val databaseModule: Module

val clientModule = module {
    singleOf(::ApiClient)
}

val viewModelsModule = module {
    singleOf(::AppState)

    viewModelOf(::MainViewModel)
    viewModelOf(::LoginViewModel)
    viewModelOf(::HomeViewModel)
    viewModelOf(::AgendaViewModel)
    viewModelOf(::GradesViewModel)
    viewModelOf(::AttendanceViewModel)
    viewModelOf(::MessagesViewModel)
    viewModelOf(::MessageViewModel)
    viewModelOf(::SchoolNoticesViewModel)
    viewModelOf(::TimetableViewModel)
    viewModelOf(::SettingsViewModel)
}

val repositoriesModule = module {
    singleOf(::LuckyNumberRepository)
    singleOf(::AgendaRepository)
    singleOf(::GradesRepository)
    singleOf(::AttendanceRepository)
    singleOf(::MessageLabelsRepository)
    singleOf(::SchoolNoticesRepository)
    singleOf(::TimetableRepository)
    singleOf(::AboutUserRepository)
}

val appModules = listOf(
    keychainModule,
    databaseModule,
    clientModule,
    repositoriesModule,
    viewModelsModule
)