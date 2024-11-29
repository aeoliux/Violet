package com.github.aeoliux.violet.app.grades

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.github.aeoliux.violet.api.types.Grade
import com.github.aeoliux.violet.api.toColorLong

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
                Text(grade.grade, color = Color.Black)
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
        GradeDialog(grade) { showDescription = false }
    }
}