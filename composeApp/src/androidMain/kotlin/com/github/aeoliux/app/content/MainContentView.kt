package com.github.aeoliux.app.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.github.aeoliux.app.content.grades.GradesView
import com.github.aeoliux.app.content.home.HomeView
import com.github.aeoliux.app.content.timetable.TimetableView

@Composable
fun MainContentView() {
    val backStack = remember { mutableStateListOf<Any>(NavRoutes.Home) }

    Scaffold(
        modifier = Modifier.background(MaterialTheme.colorScheme.background),
        topBar = { Box(Modifier.background(MaterialTheme.colorScheme.background).fillMaxWidth().height(50.dp)) {} },
        bottomBar = {
            BottomAppBar {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    NavRoutes.tabs.forEach {
                        TextButton({
                            backStack.clear()
                            backStack.add(it.third)
                        }) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = it.second,
                                    contentDescription = it.first,
                                    tint = MaterialTheme.colorScheme.onBackground
                                )
                                Text(
                                    text = it.first,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                            }
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        Box(
            Modifier.padding(paddingValues).background(MaterialTheme.colorScheme.background)
        ) {
            NavDisplay(
                modifier = Modifier.padding(20.dp),
                backStack = backStack,
                onBack = { backStack.removeLastOrNull() },
                entryProvider = { key ->
                    when (key) {
                        is NavRoutes.Home -> NavEntry(key) { HomeView() }
                        is NavRoutes.Grades -> NavEntry(key) { GradesView() }
                        is NavRoutes.Timetable -> NavEntry(key) { TimetableView() }
                        else -> NavEntry(Unit) { Text("Unknown route") }
                    }
                }
            )
        }
    }
}