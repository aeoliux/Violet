package com.github.aeoliux.violet.app.timetable

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.aeoliux.violet.app.appState.LocalAppState
import com.github.aeoliux.violet.app.appState.formatWeek
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun TimetableView(vm: TimetableViewModel = koinViewModel<TimetableViewModel>()) {
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
                        text = date.formatWeek(),
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
                        Column {
                            val border = BorderStroke(3.dp, Color.Red)

                            if (lesson.isCanceled)
                                Row(
                                    Modifier.wrapContentWidth().background(Color.Red)
                                        .padding(top = 5.dp)
                                ) {
                                    Text(text = "Cancelled", color = Color.White, modifier = Modifier.padding(5.dp))
                                }

                            Card(
                                if (lesson.isCanceled)
                                    Modifier
                                        .fillMaxWidth()
                                        .border(border)
                                else
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(top = 5.dp)
                            ) {
                                Column(Modifier.padding(10.dp)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Column(
                                            modifier = Modifier.padding(end = 20.dp),
                                        ) {
                                            Text(
                                                text = lesson.lessonNo.toString(),
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 40.sp,
                                            )
                                        }

                                        Column {
                                            Text(
                                                text = lesson.subject,
                                                fontWeight = FontWeight.SemiBold,
                                                fontSize = 20.sp,
                                            )
                                        }
                                    }

                                    Text(
                                        text = "$time - ${lesson.timeTo}, ${
                                            appState.safe(
                                                "a teacher probably",
                                                lesson.teacher
                                            )
                                        }, ${appState.safe("a classroom", lesson.classroom)}",
                                    )
                                }
                            }
                        }
                    }
                }
            }
//        }
    }
}