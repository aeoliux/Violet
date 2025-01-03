package com.github.aeoliux.violet.app.grades

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.aeoliux.violet.api.types.Grade
import com.github.aeoliux.violet.app.appState.LocalAppState
import com.github.aeoliux.violet.app.components.ExpandableList
import com.github.aeoliux.violet.app.components.LoadingIndicator
import org.koin.compose.viewmodel.koinViewModel

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
                    Text(modifier = Modifier.padding(15.dp), text = subject)
                }) {
                    GradeSemesterView(1, grades)
                    GradeSemesterView(2, grades)
                }
            }
        }
    } else
        LoadingIndicator()
}

@Composable
internal fun GradeSemesterView(semester: Int, grades: List<Grade>) {
    val appState = LocalAppState.current

    ExpandableList(
        header = {
            Text(modifier = Modifier.padding(15.dp), text ="Semester $semester")
        },
        expanded = appState.semester.value == semester
    ) {
        grades.forEach { grade ->
            if (grade.semester == semester) {
                GradeComponent(grade)

                Divider(
                    Modifier
                        .padding(start = 10.dp, end = 10.dp)
                )
            }
        }
    }
}