package com.github.aeoliux.violet.app.grades

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.github.aeoliux.violet.api.types.Grade
import com.github.aeoliux.violet.api.types.GradeType
import com.github.aeoliux.violet.app.appState.LocalAppState
import com.github.aeoliux.violet.app.components.ExpandableList
import com.github.aeoliux.violet.app.components.LoadingIndicator
import org.koin.compose.viewmodel.koinViewModel
import kotlin.math.roundToInt

@Composable
fun GradesView(vm: GradesViewModel = koinViewModel<GradesViewModel>()) {
    val appState = LocalAppState.current

    val isLoaded by vm.isLoaded.collectAsState()
    val grades by vm.grades.collectAsState()

    LaunchedEffect(appState.databaseUpdated.value) {
        vm.launchedEffect()
    }

    if (isLoaded) {
        grades.forEach { (subject, grades) ->
            if (grades.isEmpty())
                return@forEach

            Card(
                modifier = Modifier.padding(
                    start = 10.dp,
                    end = 10.dp,
                    top = 2.dp,
                    bottom = 2.dp
                )
            ) {
                ExpandableList({
                    val average = remember { grades.filter { it.gradeType == GradeType.Constituent }.countAverage() }

                    GradesExpandableListHeader(
                        text = subject,
                        gradeProposal = grades.find { it.gradeType == GradeType.FinalProposition },
                        gradeFinal = grades.find { it.gradeType == GradeType.Final },
                        average = average
                    )
                }) {
                    GradeSemesterView(1, grades.filter { it.semester == 1 })
                    GradeSemesterView(2, grades.filter { it.semester == 2 })
                }
            }
        }
    } else
        LoadingIndicator()
}

@Composable
internal fun GradesExpandableListHeader(text: String, gradeProposal: Grade?, gradeFinal: Grade?, average: Float? = null) {
    Row (
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = text, maxLines = 1, overflow = TextOverflow.Ellipsis, modifier = Modifier.padding(15.dp).fillMaxWidth(fraction = 0.50f))
        Row(
            modifier = Modifier.fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            gradeProposal?.let {
                GradeBox(it, 5.dp, true)
            }
            gradeFinal?.let {
                GradeBox(it, 5.dp, false)
            }
            average?.let {
                Text(text = it.roundToString(2), modifier = Modifier.padding(start = 5.dp))
            }
        }
    }
}

@Composable
internal fun GradeSemesterView(semester: Int, grades: List<Grade>) {
    val appState = LocalAppState.current
    val regularGrades = remember { grades.filter { it.gradeType == GradeType.Constituent } }
    val average = remember { regularGrades.countAverage() }

    ExpandableList(
        header = {
            GradesExpandableListHeader(
                text ="Semester $semester",
                gradeProposal = grades.find { it.gradeType == GradeType.SemesterProposition },
                gradeFinal = grades.find { it.gradeType == GradeType.Semester },
                average = average
            )
        },
        expanded = appState.semester.value == semester
    ) {
        regularGrades.forEach { grade ->
            GradeComponent(grade)

            Divider(
                Modifier
                    .padding(start = 10.dp, end = 10.dp)
            )
        }
    }
}