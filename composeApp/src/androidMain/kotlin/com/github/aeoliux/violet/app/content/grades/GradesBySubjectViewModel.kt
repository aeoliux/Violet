package com.github.aeoliux.violet.app.content.grades

import androidx.lifecycle.viewModelScope
import com.github.aeoliux.violet.app.RefreshableViewModel
import com.github.aeoliux.violet.app.content.toColorLong
import com.github.aeoliux.violet.repositories.AlertState
import com.github.aeoliux.violet.repositories.GradesRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class GradesBySubjectViewModel(
    private val gradesRepository: GradesRepository
): RefreshableViewModel() {
    private var subject = MutableStateFlow<String?>(null)

    private var _expandedSemester = MutableStateFlow(0)
    val expandedSemester get() = _expandedSemester.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val grades = subject
        .flatMapLatest { subject ->
            subject?.let {
                this.gradesRepository.getGradesFlow(it)
            } ?: emptyFlow()
        }
        .map { grades ->
            grades.map { grades ->
                grades.map { grade ->
                    Pair(
                        first = grade,
                        second = grade.gradeValue
                            .themeForAverage()
                            .let { Pair(grade.color.toColorLong(), it.second) }
                    )
                }
            }
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun refresh() = this.task {
        this.gradesRepository.refresh()
    }

    fun setSubject(subject: String) = this.subject.update { subject }
    fun setExpandedSemester(s: Int) = this._expandedSemester.update { s }
}