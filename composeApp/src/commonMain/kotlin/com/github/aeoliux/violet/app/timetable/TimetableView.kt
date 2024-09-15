package com.github.aeoliux.violet.app.timetable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Card
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.Tab
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.aeoliux.violet.AppContext
import com.github.aeoliux.violet.api.Timetable
import com.github.aeoliux.violet.storage.Database
import com.github.aeoliux.violet.storage.selectLessons
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.toLocalDateTime

@Composable
fun TimetableView() {
    var timetable by remember { mutableStateOf(Timetable()) }
    var isLoaded by remember { mutableStateOf(false) }
    var selectedTabIndex by remember { mutableStateOf(weekDay() - 1)}
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }

    LaunchedEffect(AppContext.databaseUpdated.value) {
        val timetableTemp = Database.selectLessons()
        if (timetableTemp != null) {
            timetable = timetableTemp
            selectedDate = timetable.keys.elementAt(selectedTabIndex)
            isLoaded = true
        }
    }

    if (isLoaded) {
        ScrollableTabRow(
            selectedTabIndex = selectedTabIndex,
            backgroundColor = Color.White,
            contentColor = Color.Black
        ) {
            timetable.keys.sorted().forEachIndexed { index, date ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = {
                        selectedDate = date
                        selectedTabIndex = index
                    }
                ) {
                    Text(modifier = Modifier.padding(16.dp), text = date.toString())
                }
            }
        }

//        Column(
//            Modifier
//                .fillMaxSize()
//        ) {
            timetable[selectedDate]?.keys?.sorted()?.forEach { time ->
                val lessons = timetable[selectedDate]!![time]!!

                Row(Modifier.fillMaxWidth().wrapContentHeight()) {
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

fun weekDay(): Int {
    var date = Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault())
        .date

    if (date.dayOfWeek.isoDayNumber > 5)
        date = LocalDate.fromEpochDays(date.toEpochDays() + (date.dayOfWeek.isoDayNumber - 6))

    return date.dayOfWeek.isoDayNumber
}