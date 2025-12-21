package com.github.aeoliux.violet.app.content.grades

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.aeoliux.violet.app.content.NavRoutes
import com.github.aeoliux.violet.app.layout.LazyLayout
import com.github.aeoliux.violet.app.layout.SectionHeader
import com.github.aeoliux.violet.app.layout.SectionListItem
import com.github.aeoliux.violet.app.layout.SectionListItemComposable
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun GradesView(
    viewModel: GradesViewModel = koinViewModel<GradesViewModel>(),
    onNavKey: (navKey: Any) -> Unit
) {
    val subjects by viewModel.subjects.collectAsState()
    val averages by viewModel.averages.collectAsState()
    val averagesGrouped by viewModel.averagesBySubjectAndSemester.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()

    LazyLayout(
        header = "Grades",
        isRefreshing = isRefreshing,
        onRefresh = { viewModel.refresh() }
    ) {
        averages?.let { averages ->
            item {
                SectionHeader("Averages")
            }

            itemsIndexed(averages.entries.toList()) { index, (label, averagesPerCategory) ->
                SectionListItemComposable(
                    index = index,
                    lastIndex = averages.entries.size - 1,
                    header = {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            averagesPerCategory.forEach { (sublabel, average, theme) ->
                                Column(
                                    modifier = Modifier
                                        .width(150.dp),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .clip(theme.second.toShape())
                                            .height(70.dp)
                                            .width(70.dp)
                                            .background(theme.first),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = average,
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = Color.White
                                        )
                                    }

                                    Text(
                                        text = "$sublabel $label",
                                        fontSize = 10.sp
                                    )
                                }
                            }
                        }
                    }
                )
            }

            item {
                SectionHeader("Subjects")
            }

            itemsIndexed(subjects) { index, subject ->
                SectionListItem(
                    index = index,
                    lastIndex = subjects.lastIndex,
                    header = subject,
                    onClick = { onNavKey(NavRoutes.GradesBySubject(subject)) },
                    trailing = {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = subject
                        )
                    }
                )
            }
        }
    }
}