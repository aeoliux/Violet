package com.github.aeoliux.violet.app.content.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Looks6
import androidx.compose.material.icons.filled.MoreTime
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.aeoliux.violet.app.components.ShapeBox
import com.github.aeoliux.violet.app.content.NavRoutes
import com.github.aeoliux.violet.app.content.formatWithWeekday
import com.github.aeoliux.violet.app.layout.LazyLayout
import com.github.aeoliux.violet.app.layout.SectionHeader
import com.github.aeoliux.violet.app.layout.SectionListItem
import com.github.aeoliux.violet.app.layout.SectionListItemComposable
import org.koin.compose.koinInject

@Composable
fun HomeView(
    viewModel: HomeViewModel = koinInject<HomeViewModel>(),
    onNavKey: (navKey: Any) -> Unit
) {
    data class Section(
        val subtitle: String,
        val header: String,
        val icon: ImageVector,
        val content: List<(@Composable (index: Int, lastIndex: Int) -> Unit)>
    )

    val name by viewModel.name.collectAsState()
    val luckyNumber by viewModel.luckyNumber.collectAsState()
    val latestGrades by viewModel.latestGrades.collectAsState()
    val timetable by viewModel.timetable.collectAsState()
    val agenda by viewModel.agenda.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()

    val sections: List<Section> = listOf(
        Section(
            subtitle = "Lucky number",
            header = "Next lucky number is $luckyNumber",
            icon = Icons.Default.Star,
            content = emptyList()
        ),

        Section(
            subtitle = "Timetable",
            header = timetable?.first?.formatWithWeekday() ?: "No lessons soon! ",
            icon = Icons.Default.CalendarToday,
            content = timetable?.let { timetable ->
                timetable.second.size.takeIf { it > 0 }?.let {
                    listOf(
                        { index, lastIndex ->
                            SectionListItemComposable(
                                index = index,
                                lastIndex = lastIndex,
                                onClick = { onNavKey(NavRoutes.Timetable) },
                                header = {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                    ) {
                                        timetable.second.forEach { entry ->
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
                            )
                        }
                    )
                } ?: emptyList()
            } ?: emptyList()
        ),

        Section(
            subtitle = "Latest grades",
            header = "Your latest grades",
            icon = Icons.Default.Looks6,
            content = listOf(
                { index, lastIndex ->
                    SectionListItemComposable(
                        index = index,
                        lastIndex = lastIndex,

                        header = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(80.dp)
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
                    )
                }
            )
        ),

        Section(
            subtitle = "Agenda",
            header = "Upcoming events",
            icon = Icons.Default.MoreTime,
            content = agenda.map { (agenda, theme) ->
                { index, lastIndex ->
                    SectionListItem(
                        index = index,
                        lastIndex = lastIndex,

                        header = "${agenda.category}, ${agenda.subject}",
                        subheaders = listOf(
                            agenda.content
                        ),

                        leading = {
                            ShapeBox(
                                modifier = Modifier
                                    .width(70.dp)
                                    .height(70.dp),
                                label = theme.first,
                                shape = theme.third.toShape(),
                                containerColor = theme.second,
                                contentColor = Color.Black
                            )
                        }
                    )
                }
            }
        )
    )

    LazyLayout(
        isRefreshing = isRefreshing,
        onRefresh = { viewModel.refresh() }
    ) {
        sections.forEach { (subtitle, header, icon, content) ->
            item {
                SectionHeader(subtitle)
            }

            item {
                SectionListItem(
                    index = 0,
                    lastIndex = content.size,
                    header = header,
                    leading = {
                        Icon(
                            imageVector = icon,
                            contentDescription = header,
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                )
            }

            itemsIndexed(content) { index, item ->
                item(index + 1, content.size)
            }
        }
    }
}