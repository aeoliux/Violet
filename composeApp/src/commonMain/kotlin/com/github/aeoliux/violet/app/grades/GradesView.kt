package com.github.aeoliux.violet.app.grades

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.aeoliux.violet.Context
import com.github.aeoliux.violet.api.Grade
import com.github.aeoliux.violet.app.components.ExpandableList
import com.github.aeoliux.violet.app.components.LoadingIndicator
import com.github.aeoliux.violet.storage.Database
import com.github.aeoliux.violet.storage.selectGrades

@Composable
fun GradesView() {
    var isLoaded by remember { mutableStateOf(false) }
    var grades by remember { mutableStateOf(LinkedHashMap<String, List<Grade>>()) }

    LaunchedEffect(Context.databaseUpdated.value) {
        grades = Database.selectGrades()?: LinkedHashMap()

        isLoaded = true
    }

    if (isLoaded)
        grades.forEach { (subject, grades) ->
            if (grades.isEmpty())
                return@forEach

            Card {
                ExpandableList({ Text(subject) }) {
                    GradeSemesterView(1u, grades)
                    GradeSemesterView(2u, grades)
                }
            }

            Spacer(
                Modifier.height(10.dp)
            )
        }
    else
        LoadingIndicator()
}

@Composable
internal fun GradeSemesterView(semester: UInt, grades: List<Grade>) {
    ExpandableList({ Text("Semester $semester") }, expanded = Context.semester.value == semester) {
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