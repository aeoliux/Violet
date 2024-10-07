package com.github.aeoliux.violet.app.agenda

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.aeoliux.violet.api.Agenda
import com.github.aeoliux.violet.api.types.AgendaItem
import com.github.aeoliux.violet.app.storage.Database
import com.github.aeoliux.violet.app.storage.selectAgenda
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AgendaViewModel: ViewModel() {
    private var _agenda = MutableStateFlow(Agenda())
    val agenda get() = _agenda.asStateFlow()

    private var _isLoaded = MutableStateFlow(false)
    val isLoaded get() = _isLoaded.asStateFlow()

    private var _isSomethingSelected = MutableStateFlow(false)
    val isSomethingSelected get() = _isSomethingSelected.asStateFlow()

    private var _selectedAgendaItem = MutableStateFlow<AgendaItem?>(null)
    val selectedAgendaItem get() = _selectedAgendaItem.asStateFlow()

    fun launchedEffect() {
        viewModelScope.launch {
            _agenda.update { Database.selectAgenda()?: Agenda() }
            _isLoaded.update { true }
        }
    }

    fun showAgendaInfo(agendaItem: AgendaItem) {
        _selectedAgendaItem.update { agendaItem }
        _isSomethingSelected.update { true }
    }

    fun closeAgendaDialog() {
        _isSomethingSelected.update { false }
        _selectedAgendaItem.update { null }
    }
}