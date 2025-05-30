package com.github.aeoliux.violet.app.attendance

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.aeoliux.violet.api.Attendance
import com.github.aeoliux.violet.api.toColorLong
import com.github.aeoliux.violet.app.appState.LocalAppState
import com.github.aeoliux.violet.app.appState.formatDate
import com.github.aeoliux.violet.app.appState.formatDateTime
import com.github.aeoliux.violet.app.components.ExpandableList

@Composable
fun AttendanceListView(
    attendance: Attendance
) {
    val appState = LocalAppState.current

    attendance.forEach { (date, attendances) ->
        Card(
            Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(start = 10.dp, end = 10.dp, top = 2.dp, bottom = 2.dp)
        ) {
            ExpandableList(
                header = {
                    Text(
                        modifier = Modifier.padding(15.dp),
                        text = date.formatDate()
                    )
                }
            ) {
                attendances.forEach { (lessonNo, attendanceInfo) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(end = 10.dp)
                                .background(attendanceInfo.color.toColorLong())
                                .size(50.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = attendanceInfo.typeShort, color = Color.Black)
                        }
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                        ) {
                            Text(
                                text = "${attendanceInfo.type} at lesson $lessonNo",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 20.sp
                            )
                            Text(
                                text = "Added at ${attendanceInfo.addDate.formatDateTime()} by ${
                                    appState.safe("Someone", attendanceInfo.addedBy)
                                }"
                            )
                        }
                    }
                }
            }
        }
    }
}