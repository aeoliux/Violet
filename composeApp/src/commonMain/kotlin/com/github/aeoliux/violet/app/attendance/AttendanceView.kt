package com.github.aeoliux.violet.app.attendance

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.aeoliux.violet.app.appState.LocalAppState
import com.github.aeoliux.violet.app.main.TabItem

@Composable
fun AttendanceView(vm: AttendanceViewModel = viewModel { AttendanceViewModel() }) {
    val appState = LocalAppState.current

    val attendance by vm.attendance.collectAsState()
    val isLoaded by vm.isLoaded.collectAsState()
    val selectedView by vm.selectedView.collectAsState()

    val views = listOf(
        TabItem("List") { AttendanceListView(attendance) },
        TabItem("Table") { AttendanceTableView(attendance) }
    )

    LaunchedEffect(appState.databaseUpdated.value) {
        vm.launchedEffect()
    }

    if (isLoaded) {
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
                    onClick = { vm.selectView(index) }
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
    } else {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "You've always been at school!",
                fontSize = 20.sp
            )
        }
    }
}