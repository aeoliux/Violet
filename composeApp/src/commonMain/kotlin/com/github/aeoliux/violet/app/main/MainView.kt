package com.github.aeoliux.violet.app.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.DrawerValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.ModalDrawer
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.aeoliux.violet.app.appState.LocalAppState
import com.github.aeoliux.violet.app.login.LoginView
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainView() {
    val appState = LocalAppState.current
    val vm = viewModel { MainViewModel(appState) }

    val selectedView by vm.selectedView.collectAsState()
    val isRefreshing by vm.isRefreshing.collectAsState()

    val coroutineScope = rememberCoroutineScope()
    val refreshState = rememberPullRefreshState(isRefreshing, { vm.refresh() })
    val scrollState = rememberScrollState()
    val drawerState = rememberDrawerState(DrawerValue.Closed)

    ModalDrawer(
        drawerState = drawerState,
        drawerContent = {
            Column(
                Modifier
                    .fillMaxHeight()
            ) {
                vm.tabs.forEachIndexed { i, tab ->
                    Row(
                        modifier = Modifier
                            .clickable {
                                coroutineScope.launch {
                                    drawerState.close()
                                    vm.selectView(i)
                                }
                            }
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            modifier = Modifier.padding(10.dp),
                            text = tab.text
                        )
                    }
                }
            }
        },
        content = {
            Scaffold(
                topBar = { TopAppBar(
                    navigationIcon =
                        if (appState.isLoggedIn.value) ({
                            IconButton(onClick = {
                                coroutineScope.launch { drawerState.open() }
                            }) {
                                Icon(
                                    imageVector = Icons.Filled.Menu,
                                    contentDescription = ""
                                )
                            }
                        }) else null,
                    title = {
                        Column {
                            Text(
                                text = if (appState.isLoggedIn.value)
                                    vm.tabs[selectedView].text
                                else
                                    "Log in to S***rgia",
                                fontWeight = FontWeight.Bold
                            )

                            AnimatedVisibility(
                                appState.statusMessage.value != null
                            ) {
                                Text(
                                    modifier = Modifier.padding(top = 2.dp),
                                    text = appState.statusMessage.value ?: "",
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }
                ) }
            ) {
                if (appState.isLoggedIn.value) {
                    Box(Modifier.fillMaxSize().pullRefresh(refreshState)) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(10.dp)
                                .verticalScroll(scrollState),
                            ) {
                            Column(
                                Modifier.padding(2.dp).fillMaxSize(),
                                verticalArrangement = Arrangement.Top,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                vm.tabs[selectedView].destination()
                            }
                        }

                        PullRefreshIndicator(
                            isRefreshing,
                            refreshState,
                            Modifier.align(Alignment.TopCenter)
                        )
                    }
                } else {
                    LoginView()
                }
            }
        }
    )
}