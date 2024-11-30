package com.github.aeoliux.violet.app.timetable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.aeoliux.violet.app.appState.LocalAppState
import com.github.aeoliux.violet.app.components.Header

@Composable
fun TimetableView(vm: TimetableViewModel = viewModel { TimetableViewModel() }) {
    val appState = LocalAppState.current

    val timetable by vm.timetable.collectAsState()
    val isLoaded by vm.isLoaded.collectAsState()

    val selectedTab by vm.selectedTab.collectAsState()
    val selectedDate by vm.selectedDate.collectAsState()

    LaunchedEffect(appState.databaseUpdated.value) { vm.launchedEffect() }

    if (isLoaded) {
        ScrollableTabRow(
            modifier = Modifier.padding(start = 10.dp, end = 10.dp, top = 2.dp, bottom = 2.dp),
            selectedTabIndex = selectedTab,
            contentColor = Color.Black
        ) {
            timetable.keys.sorted().forEachIndexed { index, date ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { vm.changeTab(index) }
                ) {
                    Text(
                        modifier = Modifier.padding(16.dp),
                        text = date.toString(),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }

//        Column(
//            Modifier
//                .fillMaxSize()
//        ) {
            timetable[selectedDate]?.keys?.sorted()?.forEach { time ->
                val lessons = timetable[selectedDate]!![time]!!

                Row(
                    Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(start = 10.dp, end = 10.dp, top = 2.dp, bottom = 2.dp)
                ) {
                    lessons.forEach { lesson ->
                        val decoration = if (lesson.isCanceled) TextDecoration.LineThrough else TextDecoration.None

                        Card(
                            Modifier
                                .fillMaxWidth()
                                .padding(top = 5.dp)
                        ) {
                            Column(Modifier.padding(10.dp)) {
                                Row {
                                    Column(Modifier.padding(end = 20.dp)) {
                                        Text(
                                            text = lesson.lessonNo.toString(),
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 40.sp,
                                            textDecoration = decoration
                                        )
                                    }

                                    Column(verticalArrangement = Arrangement.Center) {
                                        Text(
                                            text = lesson.subject,
                                            fontWeight = FontWeight.SemiBold,
                                            fontSize = 20.sp,
                                            textDecoration = decoration
                                        )
                                    }
                                }

                                Text(
                                    text = "with ${lesson.teacher} starts at $time in ${lesson.classroom}",
                                    textDecoration = decoration
                                )
                            }
                        }
                    }
                }
            }
//        }
    }
}