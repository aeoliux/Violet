package com.github.aeoliux.violet.app.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.List
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.sharp.Home
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.aeoliux.violet.Keychain
import com.github.aeoliux.violet.api.ApiClient
import com.github.aeoliux.violet.app.agenda.AgendaView
import com.github.aeoliux.violet.app.appState.AppState
import com.github.aeoliux.violet.app.appState.Model
import com.github.aeoliux.violet.app.appState.WebView
import com.github.aeoliux.violet.app.appState.runBackgroundTask
import com.github.aeoliux.violet.app.attendance.AttendanceView
import com.github.aeoliux.violet.app.grades.GradesView
import com.github.aeoliux.violet.app.home.HomeView
import com.github.aeoliux.violet.app.messages.MessagesView
import com.github.aeoliux.violet.app.schoolNotices.SchoolNoticesView
import com.github.aeoliux.violet.app.settings.SettingsView
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

data class TabItem(val text: String, val destination: @Composable () -> Unit, val important: Boolean = false, val icon: ImageVector? = null)

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
    private val timetableRepository: TimetableRepository,
): ViewModel() {

    val tabs = listOf(
        TabItem("Home", { HomeView() }, true, Icons.Rounded.Home),
        TabItem("Grades", { GradesView() }, true, Icons.Rounded.Star),
        TabItem("Timetable", { TimetableView() }, true, Icons.Rounded.List),
        TabItem("Attendance", { AttendanceView() }, false, Icons.Rounded.CheckCircle),
        TabItem("Agenda", { AgendaView() }, false, Icons.Rounded.DateRange),
        TabItem("School notices", { SchoolNoticesView() }, false, Icons.Rounded.Notifications),
        TabItem("Messages", { MessagesView() }, false, Icons.Rounded.Email),
        TabItem("Open in WebView", { WebView(
            "https://synergia.librus.pl/uczen/index",
            listOf("https://synergia.librus.pl"),
            null,
            { url, cookies -> Unit },
            Modifier.fillMaxSize()
        ) }, false, Icons.Rounded.Share),
        TabItem("Settings", { SettingsView() }, false, Icons.Rounded.Settings),
    )

    private var _isRefreshing = MutableStateFlow(false)
    val isRefreshing get() = _isRefreshing.asStateFlow()

    fun refresh(status: MutableState<Boolean>) {
        viewModelScope.launch {
            runBackgroundTask {
                _isRefreshing.update { true }
                try {
                    Model.logIn(client, keychain)
                    Model.getData(
                        client, aboutUserRepository, agendaRepository, attendanceRepository, gradesRepository, luckyNumberRepository, messageLabelsRepository, schoolNoticesRepository, timetableRepository
                    )
                } catch (e: Exception) {
                    println(e.message)
                }

                _isRefreshing.update { false }

                status.value = !status.value
            }
        }
    }
}