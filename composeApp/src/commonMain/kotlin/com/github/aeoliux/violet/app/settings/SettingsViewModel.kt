package com.github.aeoliux.violet.app.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.aeoliux.violet.Keychain
import com.github.aeoliux.violet.app.appState.Model
import com.github.aeoliux.violet.app.storage.AboutUserRepository
import com.github.aeoliux.violet.app.storage.AgendaRepository
import com.github.aeoliux.violet.app.storage.AttendanceRepository
import com.github.aeoliux.violet.app.storage.GradesRepository
import com.github.aeoliux.violet.app.storage.LuckyNumberRepository
import com.github.aeoliux.violet.app.storage.MessageLabelsRepository
import com.github.aeoliux.violet.app.storage.SchoolNoticesRepository
import com.github.aeoliux.violet.app.storage.TimetableRepository
import kotlinx.coroutines.launch

class SettingsViewModel(
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
    fun logOut() {
        viewModelScope.launch {
            Model.deleteData(
                keychain, aboutUserRepository, agendaRepository, attendanceRepository, gradesRepository, luckyNumberRepository, messageLabelsRepository, schoolNoticesRepository, timetableRepository
            )
        }
    }
}