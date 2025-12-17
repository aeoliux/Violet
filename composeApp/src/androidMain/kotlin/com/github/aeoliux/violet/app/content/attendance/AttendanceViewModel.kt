package com.github.aeoliux.violet.app.content.attendance

import androidx.compose.material3.MaterialShapes
import androidx.compose.ui.graphics.Color
import androidx.graphics.shapes.RoundedPolygon
import androidx.lifecycle.viewModelScope
import com.github.aeoliux.violet.app.RefreshableViewModel
import com.github.aeoliux.violet.app.content.formatWithWeekday
import com.github.aeoliux.violet.app.content.toColorLong
import com.github.aeoliux.violet.repositories.AlertState
import com.github.aeoliux.violet.repositories.AttendanceRepository
import com.github.aeoliux.violet.storage.Attendance
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class AttendanceViewModel(
    private val attendanceRepository: AttendanceRepository,
): RefreshableViewModel() {
    val attendance = this.attendanceRepository
        .getUnattendanceFlow()
        .map { attendance ->
            val dates = attendance.map { it.date }.distinct()

            dates.associateTo(linkedMapOf()) { date ->
                val entries = attendance.filter { it.date == date }

                Pair(
                    first = date.formatWithWeekday(),
                    second = entries
                        .sortedByDescending { it.lessonNo }
                        .map {
                            AttendanceEntry(it, it.themeToAttendanceType())
                        }
                )
            }
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, linkedMapOf())

    fun refresh() = this.task {
        this.attendanceRepository.refresh()
    }

    data class AttendanceEntry(
        val attendance: Attendance,
        val theme: Pair<Color, RoundedPolygon>
    )
}

fun Attendance.themeToAttendanceType(): Pair<Color, RoundedPolygon> = Pair(
    first = this.color.toColorLong(),
    second = when (this.typeShort) {
        "nb" -> MaterialShapes.Burst
        "sp" -> MaterialShapes.SoftBurst
        "u" -> MaterialShapes.VerySunny
        "su" -> MaterialShapes.Sunny
        "zw" -> MaterialShapes.Cookie4Sided
        "pr" -> MaterialShapes.Clover4Leaf
        "wk" -> MaterialShapes.Pill
        else -> MaterialShapes.Slanted
    }
)