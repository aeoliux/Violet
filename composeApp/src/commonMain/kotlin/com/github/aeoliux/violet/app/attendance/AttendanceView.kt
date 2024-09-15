package com.github.aeoliux.violet.app.attendance

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.github.aeoliux.violet.AppContext
import com.github.aeoliux.violet.api.types.Attendance
import com.github.aeoliux.violet.app.TabItem
import com.github.aeoliux.violet.storage.Database
import com.github.aeoliux.violet.storage.selectAttendances
import kotlinx.datetime.LocalDate

@Composable
fun AttendanceView() {
    var attendance by remember {
        mutableStateOf<LinkedHashMap<LocalDate, LinkedHashMap<UInt, Attendance>>>(
            LinkedHashMap()
        )
    }
    var selectedView by remember { mutableStateOf(-1) }
    val views = listOf(
        TabItem("List") { AttendanceListView(attendance) },
        TabItem("Table") { AttendanceTableView(attendance) }
    )

    LaunchedEffect(AppContext.databaseUpdated.value) {
        attendance = Database.selectAttendances()?: LinkedHashMap()
        selectedView = 0
    }

    if (selectedView != -1) {
        TabRow(
            selectedTabIndex = selectedView,
            backgroundColor = Color.White,
            contentColor = Color.Black,
//            indicator = { positions ->
//                TabRowDefaults.Indicator(
//                    modifier = Modifier
//                        .tabIndicatorOffset(positions[selectedView])
//                        .fillMaxWidth()
//                )
//            }
        ) {
            views.forEachIndexed { index, it ->
                Tab(
                    modifier = Modifier.fillMaxWidth(),
                    selected = selectedView == index,
                    onClick = { selectedView = index }
                ) {
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(modifier = Modifier.padding(16.dp), text = it.text)
                    }
                }
            }
        }

        views[selectedView].destination()
    }
}
