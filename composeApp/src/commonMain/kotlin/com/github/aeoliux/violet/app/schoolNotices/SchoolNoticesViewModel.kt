package com.github.aeoliux.violet.app.schoolNotices

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.aeoliux.violet.app.storage.SchoolNotice
import com.github.aeoliux.violet.app.storage.SchoolNoticesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SchoolNoticesViewModel(private val repository: SchoolNoticesRepository): ViewModel() {
    private var _listOfSchoolNotices: MutableStateFlow<List<SchoolNotice>> = MutableStateFlow(emptyList())
    val listOfSchoolNotices get() = _listOfSchoolNotices.asStateFlow()

    fun launchedEffect() {
        viewModelScope.launch {
            _listOfSchoolNotices.update { repository.getSchoolNotices() }
        }
    }
}