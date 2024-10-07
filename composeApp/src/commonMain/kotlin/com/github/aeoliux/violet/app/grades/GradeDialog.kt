package com.github.aeoliux.violet.app.grades

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.github.aeoliux.violet.api.types.Grade
import com.github.aeoliux.violet.app.components.Dialog

@Composable
fun GradeDialog(grade: Grade, onDismiss: () -> Unit) {
    Dialog({ onDismiss() }) {
        Column(Modifier.wrapContentWidth().wrapContentHeight()) {
            Text("Grade: ")
            Text("Added by: ")
            Text("Category: ")
            Text("Weight: ")
            Text("Date: ")
            if (grade.comment != null) Text("Comment: ")
        }
        Column(Modifier.wrapContentHeight()) {
            Text(grade.grade)
            Text(grade.addedBy)
            Text(grade.category)
            Text(grade.weight.toString())
            Text(grade.addDate.toString())
            if (grade.comment != null) Text(grade.comment)
        }
    }
}