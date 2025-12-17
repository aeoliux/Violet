package com.github.aeoliux.violet.repositories

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class RepositoryHelper: KoinComponent {
    val clientManager: ClientManager by inject()

    val aboutMeRepository: AboutMeRepository by inject()
    val gradesRepository: GradesRepository by inject()
    val luckyNumberRepository: LuckyNumberRepository by inject()
    val timetableRepository: TimetableRepository by inject()
    val agendaRepository: AgendaRepository by inject()
    val messagesRepository: MessagesRepository by inject()
    val attendanceRepository: AttendanceRepository by inject()

    suspend fun fullRefresh() {
        this.aboutMeRepository.refresh()
        this.gradesRepository.refresh()
        this.luckyNumberRepository.refresh()
        this.timetableRepository.refresh()
        this.agendaRepository.refresh()
        this.messagesRepository.refresh()
        this.attendanceRepository.refresh()
    }
}