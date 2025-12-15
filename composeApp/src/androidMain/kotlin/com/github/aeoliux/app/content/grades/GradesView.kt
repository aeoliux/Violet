package com.github.aeoliux.app.content.grades

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
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
import com.github.aeoliux.app.content.NavRoutes
import org.koin.compose.viewmodel.koinViewModel
import kotlin.math.roundToLong

@Composable
fun GradesView(
    viewModel: GradesViewModel = koinViewModel<GradesViewModel>(),
    onNavKey: (navKey: Any) -> Unit
) {
    val subjects by viewModel.subjects.collectAsState()
    val averages by viewModel.averages.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()

    PullToRefreshBox(
        modifier = Modifier.fillMaxSize(),
        isRefreshing = isRefreshing,
        onRefresh = { viewModel.refresh() }
    ) {
        LazyColumn(Modifier.fillMaxSize()) {
            item {
                Text(text = "Grades", fontSize = 32.sp, modifier = Modifier.padding(bottom = 10.dp))
            }

            averages?.let { averages ->
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 20.dp)
                        ) {
                            averages.forEach { (label, averages) ->
                                averages?.let { averages ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceEvenly
                                    ) {
                                        averages.forEach { (sublabel, average, theme) ->
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

                                    Spacer(Modifier.height(20.dp))
                                }
                            }
                        }
                    }

                    Spacer(Modifier.height(40.dp))
                }
            }


            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
                ) {
                    subjects.forEachIndexed { index, subject ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .clickable { onNavKey(NavRoutes.GradesBySubject(subject)) },
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                modifier = Modifier
                                    .width(280.dp)
                                    .padding(start = 15.dp),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                text = subject
                            )

                            Row(Modifier.padding(end = 15.dp)) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = subject
                                )
                            }
                        }

                        if (index < subjects.lastIndex)
                            HorizontalDivider(
                                color = MaterialTheme.colorScheme.background,
                                thickness = 5.dp
                            )
                    }
                }
            }

            item {
                Spacer(Modifier.height(25.dp))
            }
        }
    }
}