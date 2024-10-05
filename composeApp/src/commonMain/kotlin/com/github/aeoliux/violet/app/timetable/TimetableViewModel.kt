package com.github.aeoliux.violet.app.timetable

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.aeoliux.violet.api.Timetable
import com.github.aeoliux.violet.storage.Database
import com.github.aeoliux.violet.storage.selectLessons
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.toLocalDateTime

class TimetableViewModel(
    private val model: TimetableModel = TimetableModel()
): ViewModel() {
    private var _timetable = MutableStateFlow<Timetable>(LinkedHashMap())
    val timetable get() = _timetable.asStateFlow()

    private var _isLoaded = MutableStateFlow(false)
    val isLoaded get() = _isLoaded.asStateFlow()

    private var _selectedTab = MutableStateFlow(model.weekDay())
    val selectedTab get() = _selectedTab.asStateFlow()

    private var _selectedDate = MutableStateFlow(
        Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    )
    val selectedDate get() = _selectedDate.asStateFlow()

    fun launchedEffect() {
        viewModelScope.launch {
            val t = model.loadTimetable()
            _timetable.update { t }

            if (t.keys.size != 0) {
                _isLoaded.update { true }
                changeTab()
            }
        }
    }

    fun changeTab(index: Int = -1) {
        viewModelScope.launch {
            val tabIndex = model.getTab(index)
            _selectedTab.update { tabIndex }
            _selectedDate.update {
                _timetable.value.keys.elementAt(tabIndex)
            }
        }
    }
}