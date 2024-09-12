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
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.github.aeoliux.violet.api.toColorLong
import com.github.aeoliux.violet.api.types.Attendance
import com.github.aeoliux.violet.api.types.max
import com.github.aeoliux.violet.api.types.min
import kotlinx.datetime.LocalDate

@Composable
fun AttendanceTableView(
    attendance: LinkedHashMap<LocalDate, LinkedHashMap<UInt, Attendance>>,
) {
    val minLesson = attendance.min()
    val maxLesson = attendance.max()
    val scrollState = rememberScrollState()

    Row {
        Column(Modifier.wrapContentSize()) {
            Spacer(Modifier.height(60.dp))
            attendance.forEach { (date, _) ->
                Column(
                    modifier = Modifier.wrapContentSize().padding(5.dp).height(50.dp),
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(date.toString(), fontWeight = FontWeight.Bold)
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
                        Text(it.toString(), fontWeight = FontWeight.SemiBold)
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
                                .background(it?.color?.toColorLong()?: Color.White),
                            verticalArrangement = Arrangement.SpaceEvenly,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(it?.typeShort ?: "", fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }
        }
    }
}