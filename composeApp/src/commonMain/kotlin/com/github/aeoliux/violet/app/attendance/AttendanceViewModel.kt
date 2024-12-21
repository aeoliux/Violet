package com.github.aeoliux.violet.app.attendance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.aeoliux.violet.api.Attendance
import com.github.aeoliux.violet.app.storage.AttendanceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AttendanceViewModel(private val repository: AttendanceRepository): ViewModel() {
    private var _isLoaded = MutableStateFlow(false)
    val isLoaded get() = _isLoaded.asStateFlow()

    private var _attendance = MutableStateFlow(Attendance())
    val attendance get() = _attendance.asStateFlow()

    private var _selectedView = MutableStateFlow(0)
    val selectedView get() = _selectedView.asStateFlow()

    fun launchedEffect() {
        viewModelScope.launch {
            _attendance.update { repository.getAttendance() }
            _isLoaded.update { true }
        }
    }

    fun selectView(tab: Int) {
        viewModelScope.launch {
            _selectedView.update { tab }
        }
    }
}