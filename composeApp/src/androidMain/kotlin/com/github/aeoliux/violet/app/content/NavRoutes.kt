package com.github.aeoliux.violet.app.content

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Looks6
import androidx.compose.material.icons.filled.Menu
import com.github.aeoliux.violet.storage.Message
import com.github.aeoliux.violet.storage.MessageLabel

object NavRoutes {
    val tabs = listOf(
        Triple("Home", Icons.Default.Home, Home),
        Triple("Grades", Icons.Default.Looks6, Grades),
        Triple("Timetable", Icons.Default.CalendarToday, Timetable),
        Triple("Menu", Icons.Default.Menu, NavRoutes.Menu)
    )

    object Home
    object Grades
    object Timetable
    object Menu
    object Messages
    object Agenda
    object Attendance

    data class GradesBySubject(val subject: String)
    data class MessageEditor(val message: Message?, val messageLabel: MessageLabel?)
}