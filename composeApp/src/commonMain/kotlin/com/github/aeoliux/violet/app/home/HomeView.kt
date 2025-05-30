package com.github.aeoliux.violet.app.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.aeoliux.violet.api.types.Lesson
import com.github.aeoliux.violet.app.appState.LocalAppState
import com.github.aeoliux.violet.app.components.LoadingIndicator
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeView(vm: HomeViewModel = koinViewModel<HomeViewModel>()) {
    val appState = LocalAppState.current

    val me by vm.me.collectAsState()
    val classInfo by vm.classInfo.collectAsState()
    val isLoaded by vm.isLoaded.collectAsState()
    val luckyNumber by vm.luckyNumber.collectAsState()
    val nextLessons by vm.nextLessons.collectAsState()

    LaunchedEffect(appState.databaseUpdated.value) {
        vm.launchedEffect(appState.semester)
    }

    if (isLoaded) {
        Card(Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(10.dp)) {
            Column(
                Modifier.fillMaxWidth().wrapContentHeight().padding(10.dp),
                verticalArrangement = Arrangement.Top
            ) {
                HomeViewHeader("Hi, ${appState.safe("Human (?)", me?.firstName?:"")}")
                Text(text = "Lucky number is/will be $luckyNumber", fontSize = 20.sp)
            }
        }

        Card(Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(10.dp)) {
            Column(
                Modifier.fillMaxWidth().wrapContentHeight().padding(10.dp),
                verticalArrangement = Arrangement.Top
            ) {
                val currentDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
                val currentTime = currentDateTime.time

                if (nextLessons.first == null)
                    HomeViewHeader("No lessons today!")
                else {
                    HomeViewHeader("${nextLessons.first?.dayOfWeek?.name}")

                    nextLessons.
                        second?.
                        filter { (time, lessons) ->
                            lessons.fold(true) { acc, lesson ->
                                acc && !lesson.isCanceled && ((currentTime < lesson.timeTo && currentTime >= time) || currentTime < time || currentDateTime.date < nextLessons.first!!)
                            }
                        }?.let {
                            it[it.keys.firstOrNull()]?.let { lessons ->
                                val constructMessage: () -> String = {
                                    lessons.fold("") { acc, lesson ->
                                        acc + "${lesson.subject}, ${it.keys.first()} - ${lesson.timeTo}, ${lesson.classroom}"
                                    }
                                }

                                val text = if (currentTime >= it.keys.first() && currentDateTime.date == nextLessons.first)
                                    "Now: " + constructMessage()
                                else
                                    "Next: " + constructMessage()

                                Text(text = text, fontWeight = FontWeight.SemiBold, fontSize = 20.sp)
                            }

                            it.forEach { (time, lesson) ->
                                if (time == it.keys.first())
                                    return@forEach

                                lesson.forEach { lesson ->
                                    if (!lesson.isCanceled) Text("$time: ${lesson.subject}, ${lesson.classroom}")
                                }
                            }
                        }
                }
            }
        }
    } else {
        LoadingIndicator()
    }
}

@Composable
fun HomeViewHeader(text: String) {
    Text(text = text, fontSize = 30.sp, fontWeight = FontWeight.Bold)
}