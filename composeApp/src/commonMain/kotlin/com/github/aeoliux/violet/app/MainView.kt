package com.github.aeoliux.violet.app

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.github.aeoliux.violet.Keychain
import com.github.aeoliux.violet.app.grades.GradesView
import com.github.aeoliux.violet.app.home.HomeView
import com.github.aeoliux.violet.app.timetable.TimetableView
import kotlinx.coroutines.launch

data class TabItem(val text: String, val destination: @Composable () -> Unit)

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainView(keychain: Keychain) {
    val tabs = listOf(
        TabItem("Home") { HomeView() },
        TabItem("Grades") { GradesView() },
        TabItem("Timetable") { TimetableView() }
    )

    val coroutineScope = rememberCoroutineScope()
    var selectedTabItem by remember { mutableStateOf(0) }
    var isRefreshing by remember { mutableStateOf(false) }
    val refreshState = rememberPullRefreshState(isRefreshing, {
        coroutineScope.launch {
            isRefreshing = true
            fetchData(keychain)
            isRefreshing = false
        }
    })
    var scrollState = rememberScrollState()

    Scaffold(
        topBar = { TopAppBar({
            Text(text = tabs[selectedTabItem].text, fontWeight = FontWeight.Bold)
        }) },
        bottomBar = {
            BottomNavigation(
                Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                tabs.forEachIndexed { i, it ->
                    BottomNavigationItem(
                        modifier = Modifier
                            .height(50.dp)
                            .fillMaxWidth(),
                        selected = selectedTabItem == i,
                        onClick = { selectedTabItem = i },
                        icon = {
                            Text(
                                text = it.text,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    )
                }
            }
        }
    ) {
        Box(Modifier.fillMaxSize().pullRefresh(refreshState)) {
//            LazyColumn(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(10.dp),
//                verticalArrangement = Arrangement.Top,
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                item {
//                    tabs[selectedTabItem].destination()
//                }
//            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp)
                    .padding(bottom = 55.dp)
                    .verticalScroll(scrollState),

            ) {
                Column(Modifier.padding(2.dp),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    tabs[selectedTabItem].destination()
                }
            }

            PullRefreshIndicator(isRefreshing, refreshState, Modifier.align(Alignment.TopCenter))
        }
    }
}