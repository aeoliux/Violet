package com.github.aeoliux.violet

import com.github.aeoliux.violet.repositories.AboutMeRepository
import com.github.aeoliux.violet.repositories.AgendaRepository
import com.github.aeoliux.violet.repositories.AlertState
import com.github.aeoliux.violet.repositories.AttendanceRepository
import com.github.aeoliux.violet.repositories.ClientManager
import com.github.aeoliux.violet.repositories.GradesRepository
import com.github.aeoliux.violet.repositories.LuckyNumberRepository
import com.github.aeoliux.violet.repositories.MessagesRepository
import com.github.aeoliux.violet.repositories.TimetableRepository
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val appModule = module {
    singleOf(::ClientManager)
    singleOf(::AboutMeRepository)
    singleOf(::LuckyNumberRepository)
    singleOf(::GradesRepository)
    singleOf(::TimetableRepository)
    singleOf(::AlertState)
    singleOf(::MessagesRepository)
    singleOf(::AgendaRepository)
    singleOf(::AttendanceRepository)
}

fun initializeKoin(platformModule: Module) {
    startKoin {
        modules(platformModule, appModule)
    }
}