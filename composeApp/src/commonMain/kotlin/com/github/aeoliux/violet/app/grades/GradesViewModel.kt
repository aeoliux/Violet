package com.github.aeoliux.violet.app.grades

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.aeoliux.violet.api.types.Grade
import com.github.aeoliux.violet.app.storage.GradesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GradesViewModel(private val repository: GradesRepository): ViewModel() {
    private var _isLoaded = MutableStateFlow(false)
    val isLoaded get() = _isLoaded.asStateFlow()

    private var _grades = MutableStateFlow(LinkedHashMap<String, List<Grade>>())
    val grades get() = _grades.asStateFlow()

    fun launchedEffect() {
        viewModelScope.launch {
            _grades.update { repository.getGrades() }
            _isLoaded.update { true }
        }
    }
}