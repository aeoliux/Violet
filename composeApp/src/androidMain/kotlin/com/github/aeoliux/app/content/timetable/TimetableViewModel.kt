package com.github.aeoliux.app.content.timetable

import androidx.lifecycle.viewModelScope
import com.github.aeoliux.app.RefreshableViewModel
import com.github.aeoliux.repositories.TimetableRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock

class TimetableViewModel(
    private val timetableRepository: TimetableRepository
): RefreshableViewModel() {
    private var _weekDays = MutableStateFlow(emptyList<LocalDate>())
    val weekDays get() = _weekDays.asStateFlow()

    private var _selectedDay = MutableStateFlow(0)
    val selectedDay get() = _selectedDay.asStateFlow()

    var tabChosen = false

    val timetable = combine(
        this.timetableRepository
            .getTimetableFlow(),
        this._selectedDay
    ) { timetable, selectedDay ->
        this._weekDays.update { timetable.keys.toList() }

        val day = this._weekDays.value.getOrNull(selectedDay) ?: return@combine linkedMapOf()

        if (!this.tabChosen) {
            val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

            this._selectedDay.update {
                timetable.keys
                    .indexOfFirst { it == today }
                    .takeIf { it != -1 }
                    ?: 0
            }

            this.tabChosen = true
        }

        timetable[day] ?: linkedMapOf()
    }.stateIn(viewModelScope, SharingStarted.Lazily, linkedMapOf())

    fun selectDay(day: Int) = _selectedDay.update { day }

    fun refresh() = task {
        this.timetableRepository.refresh()
    }
}