package com.github.aeoliux

import com.github.aeoliux.repositories.AboutMeRepository
import com.github.aeoliux.repositories.AlertState
import com.github.aeoliux.repositories.ClientManager
import com.github.aeoliux.repositories.GradesRepository
import com.github.aeoliux.repositories.LuckyNumberRepository
import com.github.aeoliux.repositories.TimetableRepository
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
}

fun initializeKoin(platformModule: Module) {
    startKoin {
        modules(platformModule, appModule)
    }
}