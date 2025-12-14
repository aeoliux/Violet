package com.github.aeoliux.repositories

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class RepositoryHelper: KoinComponent {
    val aboutMeRepository: AboutMeRepository by inject()
    val clientManager: ClientManager by inject()
    val gradesRepository: GradesRepository by inject()
    val luckyNumberRepository: LuckyNumberRepository by inject()
    val timetableRepository: TimetableRepository by inject()
}