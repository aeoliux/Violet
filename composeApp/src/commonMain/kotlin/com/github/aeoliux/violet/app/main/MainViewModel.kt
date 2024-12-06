package com.github.aeoliux.violet.app.main

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.aeoliux.violet.api.scraping.messages.MessageLabel
import com.github.aeoliux.violet.app.agenda.AgendaView
import com.github.aeoliux.violet.app.appState.AppState
import com.github.aeoliux.violet.app.appState.fetchData
import com.github.aeoliux.violet.app.appState.runBackgroundTask
import com.github.aeoliux.violet.app.attendance.AttendanceView
import com.github.aeoliux.violet.app.grades.GradesView
import com.github.aeoliux.violet.app.home.HomeView
import com.github.aeoliux.violet.app.messages.MessagesView
import com.github.aeoliux.violet.app.schoolNotices.SchoolNoticesView
import com.github.aeoliux.violet.app.timetable.TimetableView
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class TabItem(val text: String, val destination: @Composable () -> Unit)

class MainViewModel(
    private val appState: AppState
): ViewModel() {
    val tabs = listOf(
        TabItem("Home") { HomeView() },
        TabItem("Grades") { GradesView() },
        TabItem("Timetable") { TimetableView() },
        TabItem("Attendance") { AttendanceView() },
        TabItem("Agenda") { AgendaView() },
        TabItem("School notices") { SchoolNoticesView() },
        TabItem("Messages") { MessagesView() }
    )

    private var _isRefreshing = MutableStateFlow(false)
    val isRefreshing get() = _isRefreshing.asStateFlow()

    private var _selectedView = MutableStateFlow(0)
    val selectedView get() = _selectedView.asStateFlow()

    private var _settings = MutableStateFlow(false)
    val settings get() = _settings.asStateFlow()

    fun refresh() {
        viewModelScope.launch {
            _isRefreshing.update { true }
            runBackgroundTask({ appState.fetchData() })
            _isRefreshing.update { false }
        }
    }

    fun selectView(view: Int) {
        viewModelScope.launch {
            _selectedView.update { view }
        }
    }

    fun showOrHideSettings() {
        viewModelScope.launch {
            _settings.update { !it }
        }
    }
}