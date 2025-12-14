package com.github.aeoliux.app.content

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Looks6

object NavRoutes {
    val tabs = listOf(
        Triple("Home", Icons.Outlined.Home, Home),
        Triple("Grades", Icons.Outlined.Looks6, Grades),
        Triple("Timetable", Icons.Outlined.CalendarMonth, Timetable)
    )

    object Home
    object Grades
    object Timetable
}