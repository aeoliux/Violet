package com.github.aeoliux.violet.app.content.grades

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.aeoliux.violet.app.components.ShapeBox
import com.github.aeoliux.violet.storage.Grade
import org.koin.compose.koinInject

@Composable
fun GradesBySubjectView(
    subject: String,
    viewModel: GradesBySubjectViewModel = koinInject<GradesBySubjectViewModel>(),
    onNavEntry: (navEntry: Grade) -> Unit
) {
    val grades by viewModel.grades.collectAsState()
    val expandedSemester by viewModel.expandedSemester.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()

    LaunchedEffect(subject) {
        viewModel.setSubject(subject)
    }

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = { viewModel.refresh() }
    ) {
        LazyColumn {
            item {
                Text(
                    text = subject,
                    fontSize = 32.sp,
                    modifier = Modifier.padding(bottom = 10.dp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            itemsIndexed(grades) { semester, grades ->
                if (grades.isNotEmpty()) {
                    Spacer(Modifier.height(30.dp))

                    Text(
                        modifier = Modifier
                            .padding(start = 15.dp, bottom = 5.dp),
                        text = "Semester ${semester + 1}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        grades.forEachIndexed { index, (grade, theme) ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(80.dp)
                                    .clickable { onNavEntry(grade) },
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Spacer(Modifier.width(15.dp))

                                ShapeBox(
                                    modifier = Modifier
                                        .height(55.dp)
                                        .width(55.dp),
                                    label = grade.grade,
                                    shape = theme.second.toShape(),
                                    containerColor = theme.first,
                                    contentColor = Color.Black
                                )

                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(end = 15.dp),
                                    horizontalAlignment = Alignment.End,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = grade.addedBy
                                    )

                                    Text(
                                        text = grade.category
                                    )
                                }
                            }

                            if (index < grades.lastIndex)
                                HorizontalDivider(
                                    color = MaterialTheme.colorScheme.surfaceContainer,
                                    thickness = 3.dp
                                )
                        }
                    }
                }
            }

            item {
                Spacer(Modifier.height(25.dp))
            }
        }
    }
}