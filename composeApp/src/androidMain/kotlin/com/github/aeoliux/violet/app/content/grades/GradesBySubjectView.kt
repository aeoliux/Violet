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
import com.github.aeoliux.violet.app.layout.LazyLayout
import com.github.aeoliux.violet.app.layout.SectionHeader
import com.github.aeoliux.violet.app.layout.SectionListItem
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

    LazyLayout(
        header = subject,
        isRefreshing = isRefreshing,
        onRefresh = { viewModel.refresh() }
    ) {
        grades.bySemester.forEach { semester ->
            semester
                .takeIf { it.proposal != null || it.final != null || it.constituent.isNotEmpty() }
                ?.let { grades ->
                    item {
                        SectionHeader("Semester ${semester.semester} - summary")
                    }

                    val semestral = listOf(
                        Pair("Proposal", grades.proposal),
                        Pair("Final", grades.final)
                    )
                        .filter { it.second != null }

                    item {
                        SectionListItem(
                            index = 0,
                            lastIndex = semestral.size,

                            header = "Average",
                            subheaders = listOf("Your semestral average"),
                            leading = {
                                ShapeBox(
                                    modifier = Modifier
                                        .height(70.dp)
                                        .width(70.dp),
                                    label = grades.average,
                                    shape = grades.averageTheme.second.toShape(),
                                    containerColor = grades.averageTheme.first,
                                    contentColor = Color.White
                                )
                            }
                        )
                    }

                    itemsIndexed(semestral) { index, (label, grade) ->
                        SectionListItem(
                            index = index + 1,
                            lastIndex = semestral.size,

                            onClick = { onNavEntry(grade.grade) },

                            header = label,
                            leading = {
                                ShapeBox(
                                    modifier = Modifier
                                        .height(70.dp)
                                        .width(70.dp),
                                    label = grade.grade.grade,
                                    shape = grade.shape.toShape(),
                                    containerColor = grade.color,
                                    contentColor = Color.Black
                                )
                            },
                            subheaders = listOf(
                                grade!!.grade.addedBy
                            )
                        )
                    }

                    item {
                        SectionHeader("Semester ${semester.semester} - grades")
                    }

                    itemsIndexed(grades.constituent) { index, (grade, color, shape) ->
                        SectionListItem(
                            index = index,
                            lastIndex = grades.constituent.lastIndex,

                            onClick = { onNavEntry(grade) },

                            header = grade.category,
                            subheaders = listOf(
                                grade.addedBy
                            ),

                            leading = {
                                ShapeBox(
                                    modifier = Modifier
                                        .height(70.dp)
                                        .width(70.dp),
                                    label = grade.grade,
                                    shape = shape.toShape(),
                                    containerColor = color,
                                    contentColor = Color.Black
                                )
                            }
                        )
                    }
                }
        }
    }
}