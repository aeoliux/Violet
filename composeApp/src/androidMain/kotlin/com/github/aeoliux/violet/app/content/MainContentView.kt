package com.github.aeoliux.violet.app.content

import android.graphics.drawable.shapes.OvalShape
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.github.aeoliux.violet.app.content.agenda.AgendaView
import com.github.aeoliux.violet.app.content.attendance.AttendanceView
import com.github.aeoliux.violet.app.content.grades.GradeView
import com.github.aeoliux.violet.app.content.grades.GradesBySubjectView
import com.github.aeoliux.violet.app.content.grades.GradesView
import com.github.aeoliux.violet.app.content.home.HomeView
import com.github.aeoliux.violet.app.content.messages.MessageEditorView
import com.github.aeoliux.violet.app.content.messages.MessageView
import com.github.aeoliux.violet.app.content.messages.MessagesView
import com.github.aeoliux.violet.app.content.messages.MessagesViewModel
import com.github.aeoliux.violet.app.content.schoolNotices.SchoolNoticesView
import com.github.aeoliux.violet.app.content.timetable.TimetableView
import com.github.aeoliux.violet.storage.Grade

@Composable
fun MainContentView() {
    val backStack = remember { mutableStateListOf<Any>(NavRoutes.Home) }
    var selectedTab by remember { mutableIntStateOf(0) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        contentColor = MaterialTheme.colorScheme.onSurface,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer
                ),
                navigationIcon = {
                    if (backStack.size > 1)
                        IconButton(
                            modifier = Modifier
                                .padding(start = 16.dp),
                            onClick = { backStack.removeLastOrNull() },
                            shape = CircleShape,
                            colors = IconButtonDefaults.iconButtonColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                },
                title = {
                    Text(
                        text = backStack.lastOrNull()?.let {
                            when (it) {
                                is TitledNavKey -> it.title
                                else -> ""
                            }
                        } ?: "",
                        style = MaterialTheme.typography.titleLarge,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .padding(start = 16.dp, end = 16.dp)
                    )
                }
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surfaceContainerLow
            ) {
                NavRoutes.tabs.forEachIndexed { index, (label, icon, key) ->
                    NavigationBarItem(
                        selected = selectedTab == index,
                        onClick = {
                            backStack.removeLastOrNull()
                            backStack.add(key)
                            selectedTab = index
                        },
                        icon = {
                            Icon(
                                imageVector = icon,
                                contentDescription = label
                            )
                        },
                        label = { Text(label) }
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(
            Modifier
                .padding(paddingValues)
                .background(Color.Transparent)
        ) {
            NavDisplay(
                modifier = Modifier.padding(start = 20.dp, end = 20.dp),
                backStack = backStack,
                onBack = { backStack.removeLastOrNull() },
                entryProvider = { key ->
                    when (key) {
                        is NavRoutes.Home -> NavEntry(key) { HomeView { backStack.add(it) } }
                        is NavRoutes.Grades -> NavEntry(key) { GradesView { backStack.add(it) } }
                        is NavRoutes.Timetable -> NavEntry(key) { TimetableView() }
                        is NavRoutes.Menu -> NavEntry(key) { MenuView { backStack.add(it) } }
                        is NavRoutes.GradesBySubject -> NavEntry(key) { GradesBySubjectView(key.subject) { backStack.add(it) } }
                        is NavRoutes.Messages -> NavEntry(key) { MessagesView { backStack.add(it) } }
                        is NavRoutes.Agenda -> NavEntry(key) { AgendaView { backStack.add(it) }}
                        is NavRoutes.Attendance -> NavEntry(key) { AttendanceView { backStack.add(it) } }
                        is NavRoutes.SchoolNotices -> NavEntry(key) { SchoolNoticesView { backStack.add(it) } }
                        is NavRoutes.MessageEditor -> NavEntry(key) { MessageEditorView(key.messageLabel, key.message) { backStack.removeLastOrNull() } }
                        is MessagesViewModel.MessageMetadata -> NavEntry(key) { MessageView(key) { backStack.add(it) } }
                        is Grade -> NavEntry(key) { GradeView(key) }
                        else -> NavEntry(Unit) { Text("Unknown route") }
                    }
                }
            )
        }
    }
}