package com.github.aeoliux.violet.app.attendance

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.aeoliux.violet.api.toColorLong
import com.github.aeoliux.violet.api.types.Attendance
import com.github.aeoliux.violet.app.components.ExpandableList
import kotlinx.datetime.LocalDate

@Composable
fun AttendanceListView(
    attendance: LinkedHashMap<LocalDate, LinkedHashMap<UInt, Attendance>>
) {
    attendance.forEach { (date, attendances) ->
        Card(Modifier.fillMaxWidth().wrapContentHeight().padding(10.dp)) {
            ExpandableList(
                header = {
                    Text(date.toString())
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
                            Text(text = attendanceInfo.typeShort)
                        }
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                        ) {
                            Text("At ${attendanceInfo.addDate} ${attendanceInfo.addedBy} added ${attendanceInfo.type} on lesson no. ${lessonNo}")
                        }
                    }
                }
            }
        }
    }
}