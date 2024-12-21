package com.github.aeoliux.violet.app.grades

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.github.aeoliux.violet.api.types.Grade
import com.github.aeoliux.violet.app.appState.LocalAppState
import com.github.aeoliux.violet.app.components.Dialog

@Composable
fun GradeDialog(grade: Grade, onDismiss: () -> Unit) {
    val appState = LocalAppState.current

    Dialog({ onDismiss() }) {
        Column(Modifier.wrapContentSize()) {
            Text("Grade: ${grade.grade}")
            Text("Added by: ${appState.safe("Someone", grade.addedBy)}")
            Text("Category: ${grade.category}")
            Text("Weight: ${grade.weight}")
            Text("Date: ${grade.addDate}")
            grade.comment?.let {
                Text("Comment: $it")
            }
        }
    }
}