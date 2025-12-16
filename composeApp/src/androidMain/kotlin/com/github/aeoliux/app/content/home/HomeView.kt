package com.github.aeoliux.app.content.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Looks6
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation3.runtime.NavKey
import com.github.aeoliux.app.components.ShapeBox
import com.github.aeoliux.app.content.NavRoutes
import com.github.aeoliux.app.content.formatWithWeekday
import com.github.aeoliux.app.content.minimalFormat
import com.github.aeoliux.app.content.toColorLong
import org.koin.compose.koinInject

@Composable
fun HomeView(
    viewModel: HomeViewModel = koinInject<HomeViewModel>(),
    onNavKey: (navKey: Any) -> Unit
) {
    val name by viewModel.name.collectAsState()
    val luckyNumber by viewModel.luckyNumber.collectAsState()
    val latestGrades by viewModel.latestGrades.collectAsState()
    val timetable by viewModel.timetable.collectAsState()
    val agenda by viewModel.agenda.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()

    PullToRefreshBox(
        modifier = Modifier
            .fillMaxSize(),
        isRefreshing = isRefreshing,
        onRefresh = { viewModel.refresh() }
    ) {
        LazyColumn (
            modifier = Modifier
                .fillMaxSize()
        ) {
            item {
                Text(text = "Hi, ${name.first}", fontSize = 32.sp)
                Spacer(Modifier.height(5.dp))
                Text("Today's/Tomorrow's lucky number is $luckyNumber", fontSize = 20.sp)
            }

            item {
                HorizontalDivider(Modifier.padding(top = 20.dp, bottom = 20.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = "Timetable"
                    )
                    Spacer(Modifier.width(5.dp))

                    Text(text = timetable.first.formatWithWeekday(), fontSize = 20.sp)
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                        .clickable { onNavKey(NavRoutes.Timetable) }
                ) {
                    timetable.second.forEach { (_, timetable) ->
                        timetable.forEach { entry ->
                            CompositionLocalProvider(
                                LocalContentColor provides if (entry.isCanceled)
                                    MaterialTheme.colorScheme.error
                                else
                                    MaterialTheme.colorScheme.onBackground
                            ) {
                                Row {
                                    Text(
                                        modifier = Modifier
                                            .width(50.dp)
                                            .padding(end = 5.dp),
                                        text = "${entry.time}"
                                    )

                                    Text(
                                        modifier = Modifier.widthIn(0.dp, 240.dp),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,

                                        fontWeight = FontWeight.SemiBold,
                                        text = entry.subject
                                    )

                                    Text(
                                        modifier = Modifier
                                            .padding(start = 5.dp),
                                        text = entry.classroom
                                    )

                                    Text(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(start = 5.dp),
                                        text = "${entry.lessonNo}",
                                        textAlign = TextAlign.End
                                    )
                                }
                            }
                        }
                    }
                }
            }

            item {
                HorizontalDivider(Modifier.padding(top = 20.dp, bottom = 20.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Looks6,
                        contentDescription = "Grades"
                    )

                    Spacer(Modifier.width(5.dp))

                    Text(text = "Latest grades", fontSize = 20.sp)
                }

                Spacer(Modifier.height(10.dp))

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp),
                ) {
                    latestGrades.forEach { (grade, theme) ->
                        ShapeBox(
                            modifier = Modifier
                                .height(70.dp)
                                .width(70.dp)
                                .clickable { onNavKey(grade) },
                            fontSize = 20.sp,
                            label = grade.grade,
                            shape = theme.second.toShape(),
                            containerColor = theme.first,
                            contentColor = Color.Black
                        )
                    }
                }
            }

            item {
                HorizontalDivider(Modifier.padding(top = 20.dp, bottom = 20.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CalendarMonth,
                        contentDescription = "Latest agenda"
                    )

                    Spacer(Modifier.width(5.dp))

                    Text(text = "Agenda - upcoming events", fontSize = 20.sp)
                }

                Spacer(Modifier.height(15.dp))

                agenda.forEach { (agenda, theme) ->
                    Row(
                        modifier = Modifier
                            .height(60.dp)
                    ) {
                        ShapeBox(
                            label = theme.first,
                            shape = theme.third.toShape(),
                            containerColor = theme.second,
                            contentColor = Color.Black,
                            fontSize = 20.sp,
                            modifier = Modifier
                                .height(60.dp)
                                .width(60.dp)
                        )

                        Spacer(Modifier.width(15.dp))

                        Column(
                            modifier = Modifier
                                .fillMaxSize(),
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "${agenda.category}, ${agenda.subject}",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold,

                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier
                                    .width(300.dp)
                            )

                            Text(
                                text = agenda.date.formatWithWeekday(),
                                fontSize = 14.sp
                            )
                        }
                    }

                    Spacer(Modifier.height(15.dp))

                }

                HorizontalDivider(Modifier.padding(top = 20.dp, bottom = 20.dp))
            }

            item {
                Spacer(Modifier.height(25.dp))
            }
        }
    }
}