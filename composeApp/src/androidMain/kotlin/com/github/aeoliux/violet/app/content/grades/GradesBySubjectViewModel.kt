package com.github.aeoliux.violet.app.content.grades

import androidx.compose.ui.graphics.Color
import androidx.graphics.shapes.RoundedPolygon
import androidx.lifecycle.viewModelScope
import com.github.aeoliux.violet.api.types.GradeType
import com.github.aeoliux.violet.app.RefreshableViewModel
import com.github.aeoliux.violet.app.content.toColorLong
import com.github.aeoliux.violet.repositories.AlertState
import com.github.aeoliux.violet.repositories.GradesRepository
import com.github.aeoliux.violet.repositories.fill
import com.github.aeoliux.violet.repositories.trimToTheLimit
import com.github.aeoliux.violet.storage.Grade
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
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
                combine(
                    this.gradesRepository.getGradesFlow(it),
                    this.gradesRepository.getAveragesForSubject(it)
                ) { grades, averages -> Pair(grades, averages) }
            } ?: emptyFlow()
        }
        .map { (grades, averages) ->
            val final = grades
                .firstNotNullOfOrNull {
                    it.firstOrNull { it.gradeType == GradeType.Final }
                }
                ?.toThemed()

            val proposal = grades
                .firstNotNullOfOrNull {
                    it.firstOrNull { it.gradeType == GradeType.FinalProposition }
                }
                ?.toThemed()

            GradesGrouped(
                final = final,
                proposal = proposal,
                bySemester = grades
                    .mapIndexed { semester, grades ->
                        val final = grades
                            .firstOrNull { it.gradeType == GradeType.Semester }
                            ?.toThemed()

                        val proposal = grades
                            .firstOrNull { it.gradeType == GradeType.SemesterProposition }
                            ?.toThemed()

                        println(averages)
                        val average = (averages.firstOrNull { it.semester == semester + 1 }?.average ?: 0.0)

                        GradesBySemester(
                            semester = semester + 1,
                            final = final,
                            proposal = proposal,
                            average = average
                                .toString()
                                .trimToTheLimit()
                                .fill(),
                            averageTheme = average.themeForAverage(),
                            constituent = grades
                                .filter { it.gradeType == GradeType.Constituent }
                                .map { it.toThemed() }
                        )
                    }
                    .reversed()
            )
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, GradesGrouped(null, null, emptyList()))

    fun refresh() = this.task {
        this.gradesRepository.refresh()
    }

    fun setSubject(subject: String) = this.subject.update { subject }
    fun setExpandedSemester(s: Int) = this._expandedSemester.update { s }

    data class GradesGrouped(
        val final: GradeAndTheme?,
        val proposal: GradeAndTheme?,
        val bySemester: List<GradesBySemester>
    )

    data class GradesBySemester(
        val semester: Int,
        val average: String,
        val averageTheme: Pair<Color, RoundedPolygon>,
        val final: GradeAndTheme?,
        val proposal: GradeAndTheme?,
        val constituent: List<GradeAndTheme>
    )

    data class GradeAndTheme(
        val grade: Grade,
        val color: Color,
        val shape: RoundedPolygon
    )

    fun Grade.toThemed(): GradeAndTheme = GradeAndTheme(
        grade = this,
        color = this.color.toColorLong(),
        shape = this.gradeValue.themeForAverage().second
    )
}