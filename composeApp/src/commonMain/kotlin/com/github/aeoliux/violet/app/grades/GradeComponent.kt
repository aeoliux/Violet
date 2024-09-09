package com.github.aeoliux.violet.app.grades

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.github.aeoliux.violet.api.Grade
import com.github.aeoliux.violet.api.toColorLong
import kotlinx.coroutines.launch

@Composable
fun GradeComponent(grade: Grade) {
    var showDescription by remember { mutableStateOf(false) }

    Row(
        Modifier
            .wrapContentHeight()
            .clickable(onClick = { showDescription = true })
            .padding(start = 15.dp, end = 15.dp, top = 10.dp, bottom = 10.dp)
    ) {
        Column(
            Modifier
                .wrapContentHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Column(
                Modifier
                    .height(50.dp)
                    .width(50.dp)
                    .padding(5.dp)
                    .background(grade.color.toColorLong()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(grade.grade)
            }
        }

        Column(
            Modifier
                .wrapContentHeight()
                .fillMaxWidth(),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.Center
        ) {
            Text(grade.addedBy)
            Text(grade.category)
        }
    }

    if (showDescription) {
        Dialog(
            onDismissRequest = { showDescription = false }
        ) {
            Column(
                Modifier
                    .wrapContentSize()
                    .background(Color.White)
                    .padding(20.dp)
                    .clip(
                        RoundedCornerShape(10.dp)
                    )
            ) {
                Row {
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

                Row(
                    Modifier.padding(10.dp).fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(onClick = { showDescription = false }) {
                        Text("Ok")
                    }
                }
            }
        }
    }
}