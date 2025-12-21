package com.github.aeoliux.violet.app.content.grades

import androidx.compose.material3.MaterialShapes
import androidx.compose.ui.graphics.Color
import androidx.graphics.shapes.RoundedPolygon
import androidx.lifecycle.viewModelScope
import com.github.aeoliux.violet.app.RefreshableViewModel
import com.github.aeoliux.violet.repositories.AlertState
import com.github.aeoliux.violet.repositories.GradesRepository
import com.github.aeoliux.violet.repositories.fill
import com.github.aeoliux.violet.repositories.trimToTheLimit
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class GradesViewModel(
    private val gradesRepository: GradesRepository
): RefreshableViewModel() {
    val subjects = this.gradesRepository
        .getSubjectsListFlow()
        .stateIn(viewModelScope, SharingStarted.Lazily, listOf())

    val averagesBySubjectAndSemester = this.gradesRepository
        .getAveragesBySubjectAndSemester()
        .map { averages ->
            averages.forEach { (subject, bySemester) ->
                bySemester.forEachIndexed { semester, average ->
                    println("$subject;${semester + 1} = $average")
                }
            }

            averages
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, linkedMapOf())

    val averages = this.gradesRepository
        .getGeneralAveragesFlow()
        .map { averages ->
            listOf(
                Pair("yearly", 0),
                Pair("1st semester", 2),
                Pair("2nd semester", 4)
            )
                .associateTo(linkedMapOf()) { label ->
                    val pair = listOf(
                        averages[label.second]
                            .let {
                                Triple(
                                    first = "Final",
                                    second = it
                                        .toString()
                                        .trimToTheLimit()
                                        .fill(),
                                    third = it.themeForAverage()
                                )
                            },
                        averages[label.second + 1]
                            .let {
                                Triple(
                                    first = "Proposal",
                                    second = it
                                        .toString()
                                        .trimToTheLimit()
                                        .fill(),
                                    third = it.themeForAverage()
                                )
                            }
                    )

                    Pair(
                        first = label.first,
                        second = pair
                            .takeIf { pair[0].second != "0.00" || pair[1].second != "0.00" }
                    )
                }
                .filterTo(linkedMapOf()) { (label, averages) -> averages != null && averages.isNotEmpty() }
                .mapValues { it.value!! }

        }
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    fun refresh() = task {
        this.gradesRepository.refresh()
    }
}

fun Double.themeForAverage(): Pair<Color, RoundedPolygon> = when {
    this >= 6 -> Pair(Color.Magenta, MaterialShapes.Burst)
    this >= 4.75 -> Pair(Color.Cyan, MaterialShapes.SoftBurst)
    this >= 3.75 -> Pair(Color.Blue, MaterialShapes.VerySunny)
    this >= 2.75 -> Pair(Color.Green, MaterialShapes.Sunny)
    this >= 1.75 -> Pair(Color.Yellow, MaterialShapes.Cookie4Sided)
    this > 0.0   -> Pair(Color.Red, MaterialShapes.Slanted)
    this == 0.0 -> Pair(Color.Gray, MaterialShapes.Flower)

    else -> Pair(Color.Black, MaterialShapes.Gem)
}