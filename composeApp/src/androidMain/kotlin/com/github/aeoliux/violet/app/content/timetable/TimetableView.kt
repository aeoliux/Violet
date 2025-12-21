package com.github.aeoliux.violet.app.content.timetable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.aeoliux.violet.app.content.formatWithWeekday
import com.github.aeoliux.violet.app.layout.LazyLayout
import com.github.aeoliux.violet.app.layout.SectionHeader
import com.github.aeoliux.violet.app.layout.SectionListItem
import com.github.aeoliux.violet.app.layout.SectionListItemComposable
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun TimetableView(viewModel: TimetableViewModel = koinViewModel<TimetableViewModel>()) {
    val timetable by viewModel.timetable.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val selectedDay by viewModel.selectedDay.collectAsState()
    val weekDays by viewModel.weekDays.collectAsState()

    LazyLayout(
        header = "Timetable",
        isRefreshing = isRefreshing,
        onRefresh = { viewModel.refresh() }
    ) {
        if (weekDays.isNotEmpty())
            item {
                PrimaryScrollableTabRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp),
                    selectedTabIndex = selectedDay,
                    containerColor = MaterialTheme.colorScheme.surfaceContainer,
                    contentColor = MaterialTheme.colorScheme.onSurface
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

        if (timetable.isNotEmpty())
            item {
                SectionHeader("Lessons")
            }

        itemsIndexed(timetable) { index, entry ->
            SectionListItemComposable(
                index = index,
                lastIndex = timetable.size - 1,
                header = {
                    Text(
                        text = entry.subject.plus(if (entry.isCanceled) " (Canceled)" else ""),
                        color = if (entry.isCanceled)
                            MaterialTheme.colorScheme.error
                        else
                            MaterialTheme.colorScheme.onSurface
                    )
                },
                subheaders = {
                    Text(
                        text = "${entry.time} - ${entry.timeTo}, ${entry.teacher}, ${entry.classroom}",
                        color = if (entry.isCanceled)
                            MaterialTheme.colorScheme.error
                        else
                            MaterialTheme.colorScheme.onSurface
                    )
                },
                trailing = {
                    Text(
                        text = entry.lessonNo.toString(),
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            )
        }
    }
}