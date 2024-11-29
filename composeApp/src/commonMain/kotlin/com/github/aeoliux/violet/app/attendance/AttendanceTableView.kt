package com.github.aeoliux.violet.app.attendance

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.github.aeoliux.violet.api.Attendance
import com.github.aeoliux.violet.api.toColorLong
import com.github.aeoliux.violet.api.types.max
import com.github.aeoliux.violet.api.types.min

@Composable
fun AttendanceTableView(
    attendance: Attendance,
) {
    val minLesson = attendance.min()
    val maxLesson = attendance.max()
    val scrollState = rememberScrollState()

    Row {
        Column(Modifier.wrapContentSize()) {
            Spacer(Modifier.height(60.dp))
            attendance.forEach { (date, _) ->
                Column(
                    modifier = Modifier.wrapContentSize().padding(start = 10.dp, end = 10.dp, top = 5.dp, bottom = 5.dp).height(50.dp),
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        date.toString(),
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }

        Column(Modifier.horizontalScroll(scrollState).wrapContentSize()) {
            Row(Modifier.padding(5.dp).fillMaxSize().wrapContentHeight()) {
                (minLesson..maxLesson).toList().forEach {
                    Column(
                        modifier = Modifier.size(50.dp).padding(5.dp),
                        verticalArrangement = Arrangement.SpaceEvenly,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            it.toString(),
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            }

            attendance.forEach { (_, attendances) ->
                Row(Modifier.padding(5.dp).fillMaxSize().wrapContentHeight()) {
                    (minLesson..maxLesson).toList().forEach { lessonNo ->
                        val it = attendances[lessonNo]

                        Column(
                            modifier = Modifier
                                .size(50.dp)
                                .padding(5.dp)
                                .background(it?.color?.toColorLong()?: MaterialTheme.colorScheme.background),
                            verticalArrangement = Arrangement.SpaceEvenly,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(it?.typeShort ?: "", fontWeight = FontWeight.SemiBold, color = Color.Black)
                        }
                    }
                }
            }
        }
    }
}