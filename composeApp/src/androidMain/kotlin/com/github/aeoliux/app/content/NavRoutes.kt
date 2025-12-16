package com.github.aeoliux.app.content

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Looks6
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Looks6
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Looks6

object NavRoutes {
    val tabs = listOf(
        Triple("Home", Icons.Default.Home, Home),
        Triple("Grades", Icons.Default.Looks6, Grades),
        Triple("Timetable", Icons.Default.CalendarToday, Timetable),
        Triple("Menu", Icons.Default.Menu, NavRoutes.Menu)
    )

    object Home
    object Grades
    data class GradesBySubject(val subject: String)
    object Timetable
    object Menu
    object Messages
    object Agenda
}