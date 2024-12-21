package com.github.aeoliux.violet.app.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.aeoliux.violet.Keychain
import com.github.aeoliux.violet.api.ApiClient
import com.github.aeoliux.violet.app.agenda.AgendaView
import com.github.aeoliux.violet.app.appState.Model
import com.github.aeoliux.violet.app.appState.runBackgroundTask
import com.github.aeoliux.violet.app.attendance.AttendanceView
import com.github.aeoliux.violet.app.grades.GradesView
import com.github.aeoliux.violet.app.home.HomeView
import com.github.aeoliux.violet.app.messages.MessagesView
import com.github.aeoliux.violet.app.schoolNotices.SchoolNoticesView
import com.github.aeoliux.violet.app.storage.AboutUserRepository
import com.github.aeoliux.violet.app.storage.AgendaRepository
import com.github.aeoliux.violet.app.storage.AttendanceRepository
import com.github.aeoliux.violet.app.storage.GradesRepository
import com.github.aeoliux.violet.app.storage.LuckyNumberRepository
import com.github.aeoliux.violet.app.storage.MessageLabelsRepository
import com.github.aeoliux.violet.app.storage.SchoolNoticesRepository
import com.github.aeoliux.violet.app.storage.TimetableRepository
import com.github.aeoliux.violet.app.timetable.TimetableView
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class TabItem(val text: String, val destination: @Composable () -> Unit)

class MainViewModel(
    private val client: ApiClient,
    private val keychain: Keychain,
    private val aboutUserRepository: AboutUserRepository,
    private val agendaRepository: AgendaRepository,
    private val attendanceRepository: AttendanceRepository,
    private val gradesRepository: GradesRepository,
    private val luckyNumberRepository: LuckyNumberRepository,
    private val messageLabelsRepository: MessageLabelsRepository,
    private val schoolNoticesRepository: SchoolNoticesRepository,
    private val timetableRepository: TimetableRepository
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

    fun refresh(status: MutableState<Boolean>) {
        viewModelScope.launch {
            runBackgroundTask {
                _isRefreshing.update { true }
                Model.logIn(client, keychain)
                Model.getData(
                    client, aboutUserRepository, agendaRepository, attendanceRepository, gradesRepository, luckyNumberRepository, messageLabelsRepository, schoolNoticesRepository, timetableRepository
                )
                _isRefreshing.update { false }

                status.value = !status.value
            }
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