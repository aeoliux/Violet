package com.github.aeoliux.app.content.grades

import androidx.lifecycle.viewModelScope
import com.github.aeoliux.app.RefreshableViewModel
import com.github.aeoliux.repositories.GradesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class GradesViewModel(
    private val gradesRepository: GradesRepository
): RefreshableViewModel() {
    val grades = this.gradesRepository
        .getGradesFlow()
        .stateIn(viewModelScope, SharingStarted.Lazily, linkedMapOf())

    fun refresh() = task {
        this.gradesRepository.refresh()
    }
}