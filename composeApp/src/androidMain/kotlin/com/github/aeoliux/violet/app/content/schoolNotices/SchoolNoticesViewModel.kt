package com.github.aeoliux.violet.app.content.schoolNotices

import androidx.lifecycle.viewModelScope
import com.github.aeoliux.violet.app.RefreshableViewModel
import com.github.aeoliux.violet.repositories.SchoolNoticesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class SchoolNoticesViewModel(
    private val schoolNoticesRepository: SchoolNoticesRepository
): RefreshableViewModel() {
    val schoolNotices = this.schoolNoticesRepository
        .getSchoolNoticesFlow()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun refresh() = this.task {
        this.schoolNoticesRepository.refresh()
    }
}