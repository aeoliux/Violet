package com.github.aeoliux.violet.app.content.agenda

import androidx.compose.material3.MaterialShapes
import androidx.lifecycle.viewModelScope
import com.github.aeoliux.violet.app.RefreshableViewModel
import com.github.aeoliux.violet.app.content.toColorLong
import com.github.aeoliux.violet.repositories.AgendaRepository
import com.github.aeoliux.violet.repositories.AlertState
import com.github.aeoliux.violet.storage.Agenda
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

@OptIn(ExperimentalCoroutinesApi::class)
class AgendaViewModel(
    private val agendaRepository: AgendaRepository
): RefreshableViewModel() {
    private var monthsBack = MutableStateFlow(0)
    private var monthsForward = MutableStateFlow(1)

    val agenda = combine(
        monthsBack,
        monthsForward
    ) { monthsBack, monthsForward ->
        Pair(monthsBack, monthsForward)
    }
        .flatMapLatest { (monthsBack, monthsForward) ->
            this.agendaRepository.getAgendaFlow(monthsBack, monthsForward)
                .map {
                    it.entries.associateTo(linkedMapOf()) { (date, agenda) ->
                        Pair(
                            first = date,
                            second = agenda
                                .map {
                                    Pair(it, it.theme())
                                }
                        )
                    }
                }
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, linkedMapOf())

    fun showEarlier() = this.monthsBack.update { it + 1 }
    fun showLater() = this.monthsForward.update { it + 1 }

    fun refresh() = this.task {
        this.agendaRepository.refresh()
    }
}

fun Agenda.theme() = Triple(
    first = this.category.first().uppercase(),
    second = this.color.toColorLong(),
    third = when (this.category.substring(0, 2).lowercase()) {
        "sp" -> MaterialShapes.Burst
        "kl" -> MaterialShapes.SoftBurst
        "ka" -> MaterialShapes.Cookie4Sided
        "pr" -> MaterialShapes.Sunny
        "eg" -> MaterialShapes.VerySunny
        "in" -> MaterialShapes.Pill
        else -> MaterialShapes.Ghostish
    }
)
