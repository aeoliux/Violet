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

    object Home: TitledNavKey {
        override val title: String = "Home"
    }

    object Grades: TitledNavKey {
        override val title: String = "Grades"
    }
    object Timetable: TitledNavKey {
        override val title: String = "Timetable"
    }

    object Menu: TitledNavKey {
        override val title: String = "Menu"
    }

    object Messages: TitledNavKey {
        override val title: String = "Messages"
    }
    object Agenda: TitledNavKey {
        override val title: String = "Agenda"
    }

    object Attendance: TitledNavKey {
        override val title: String = "Attendance"
    }

    data class GradesBySubject(val subject: String): TitledNavKey {
        override val title: String = subject
    }

    data class MessageEditor(val message: Message?, val messageLabel: MessageLabel?): TitledNavKey {
        override val title: String = if (message == null || messageLabel == null)
            "New message"
        else
            "Respond"
    }
}

interface TitledNavKey {
    val title: String
}