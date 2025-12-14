package com.github.aeoliux.app.content.timetable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.aeoliux.app.content.formatWithWeekday
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun TimetableView(viewModel: TimetableViewModel = koinViewModel<TimetableViewModel>()) {
    val timetable by viewModel.timetable.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val selectedDay by viewModel.selectedDay.collectAsState()
    val weekDays by viewModel.weekDays.collectAsState()

    PullToRefreshBox(
        modifier = Modifier.fillMaxSize(),
        isRefreshing = isRefreshing,
        onRefresh = { viewModel.refresh() }
    ) {
        LazyColumn(Modifier.fillMaxSize()) {
            item {
                Text(text = "Timetable", fontSize = 32.sp, modifier = Modifier.padding(bottom = 10.dp))
            }

            if (weekDays.isNotEmpty())
                item {
                    PrimaryScrollableTabRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp),
                        selectedTabIndex = selectedDay,
                        containerColor = MaterialTheme.colorScheme.background
                    ) {
                        weekDays.forEachIndexed { index, date ->
                            Tab(
                                selected = index == selectedDay,
                                onClick = { viewModel.selectDay(index) }
                            ) {
                                Text(
                                    modifier = Modifier.padding(16.dp),
                                    text = date.formatWithWeekday()
                                )
                            }
                        }
                    }
                }

            items(items = timetable.entries.toList()) { (_, entries) ->
                Column(
                    Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    entries.forEach { entry ->
                        CompositionLocalProvider(
                            LocalContentColor provides if (entry.isCanceled)
                                MaterialTheme.colorScheme.error
                            else
                                MaterialTheme.colorScheme.onBackground
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(if (entry.isCanceled) 96.dp else 80.dp)
                                    .background(MaterialTheme.colorScheme.background),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .width(350.dp)
                                        .background(MaterialTheme.colorScheme.background),
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    if (entry.isCanceled)
                                        Text(
                                            modifier = Modifier
                                                .width(350.dp),
                                            overflow = TextOverflow.Ellipsis,
                                            maxLines = 1,
                                            text = "Canceled!",
                                            fontSize = 14.sp
                                        )

                                    Text(
                                        modifier = Modifier
                                            .width(350.dp),
                                        overflow = TextOverflow.Ellipsis,
                                        maxLines = 1,
                                        text = entry.subject,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 16.sp
                                    )

                                    Text(
                                        modifier = Modifier
                                            .width(350.dp),
                                        overflow = TextOverflow.Ellipsis,
                                        maxLines = 1,
                                        text = "${entry.time} - ${entry.timeTo}, ${entry.classroom}",
                                        fontSize = 12.sp
                                    )

                                    Text(
                                        modifier = Modifier
                                            .width(350.dp),
                                        overflow = TextOverflow.Ellipsis,
                                        maxLines = 1,
                                        text = entry.teacher,
                                        fontSize = 12.sp
                                    )
                                }

                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(MaterialTheme.colorScheme.background),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.End
                                ) {
                                    Text(
                                        text = "${entry.lessonNo}",
                                        fontSize = 24.sp
                                    )
                                }
                            }
                        }
                    }
                }

                HorizontalDivider(Modifier.padding(top = 10.dp, bottom = 10.dp))
            }
        }
    }
}