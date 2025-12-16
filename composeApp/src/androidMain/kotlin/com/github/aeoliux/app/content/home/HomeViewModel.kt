package com.github.aeoliux.app.content.home

import androidx.lifecycle.viewModelScope
import com.github.aeoliux.app.RefreshableViewModel
import com.github.aeoliux.app.content.agenda.theme
import com.github.aeoliux.app.content.grades.themeForAverage
import com.github.aeoliux.app.content.toColorLong
import com.github.aeoliux.repositories.AboutMeRepository
import com.github.aeoliux.repositories.AgendaRepository
import com.github.aeoliux.repositories.GradesRepository
import com.github.aeoliux.repositories.LuckyNumberRepository
import com.github.aeoliux.repositories.MessagesRepository
import com.github.aeoliux.repositories.TimetableRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock

class HomeViewModel(
    private val luckyNumberRepository: LuckyNumberRepository,
    private val aboutMeRepository: AboutMeRepository,
    private val gradesRepository: GradesRepository,
    private val timetableRepository: TimetableRepository,
    private val agendaRepository: AgendaRepository,
    private val messagesRepository: MessagesRepository,
): RefreshableViewModel() {
    val name = this.aboutMeRepository
        .getAboutMeFlow()
        .map { Pair(it.firstName, it.lastName) }
        .stateIn(viewModelScope, SharingStarted.Lazily, Pair("", ""))

    val luckyNumber = this.luckyNumberRepository
        .getLuckyNumberFlow()
        .stateIn(viewModelScope, SharingStarted.Lazily, 0)

    val latestGrades = this.gradesRepository
        .getLatestGrades(5)
        .map { grades ->
            grades.map { grade ->
                Pair(
                    first = grade,
                    second = grade.gradeValue
                        .themeForAverage()
                        .let { Pair(grade.color.toColorLong(), it.second) }
                )
            }
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val timetable = this.timetableRepository
        .getCurrentTimetable()
        .stateIn(viewModelScope, SharingStarted.Lazily, Pair(
            Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date,
            linkedMapOf()
        ))

    val agenda = this.agendaRepository
        .getLatestAgendaFlow(5)
        .map {
            it.map {
                Pair(it, it.theme())
            }
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun refresh() = task {
        this.aboutMeRepository.refresh()
        this.luckyNumberRepository.refresh()
        this.timetableRepository.refresh()
        this.gradesRepository.refresh()
        this.agendaRepository.refresh()
        this.messagesRepository.refresh()
    }
}