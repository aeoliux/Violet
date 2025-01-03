package com.github.aeoliux.violet.app.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.aeoliux.violet.app.appState.LocalAppState
import com.github.aeoliux.violet.app.components.Header
import com.github.aeoliux.violet.app.login.LoginView
import com.github.aeoliux.violet.app.settings.SettingsView
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MainView(vm: MainViewModel = koinViewModel<MainViewModel>()) {
    val appState = LocalAppState.current

    val selectedView by vm.selectedView.collectAsState()
    val settings by vm.settings.collectAsState()
    val isRefreshing by vm.isRefreshing.collectAsState()

    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    val drawerState = rememberDrawerState(DrawerValue.Closed)

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(Modifier.background(MaterialTheme.colorScheme.surface)) {
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
            Box(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
                if (appState.isLoggedIn.value) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(scrollState)
                    ) {
                        Row(Modifier.wrapContentHeight()) {
                            Column {
                                IconButton({ coroutineScope.launch { drawerState.open() } }) {
                                    Icon(
                                        imageVector = Icons.Filled.ArrowBack,
                                        contentDescription = "Show menu",
                                        tint = MaterialTheme.colorScheme.onBackground
                                    )
                                }
                            }

                            Column(
                                Modifier.fillMaxHeight(),
                                verticalArrangement = Arrangement.Center,
                            ) {
                                appState.statusMessage.value?.let {
                                    Text(color = MaterialTheme.colorScheme.onBackground, text = it)
                                }
                            }

                            Column(
                                Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.End
                            ) {
                                Row {
                                    IconButton({ appState.safeMode.value = !appState.safeMode.value }) {
                                        Icon(
                                            imageVector = if (appState.safeMode.value)
                                                Icons.Filled.Warning
                                            else
                                                Icons.Rounded.Warning,
                                            tint = if (appState.safeMode.value)
                                                MaterialTheme.colorScheme.error
                                            else
                                                MaterialTheme.colorScheme.onBackground,
                                            contentDescription = "Enable/disable safe mode"
                                        )
                                    }

                                    IconButton({ vm.showOrHideSettings() }) {
                                        Icon(
                                            imageVector = Icons.Filled.Settings,
                                            tint = MaterialTheme.colorScheme.onBackground,
                                            contentDescription = "Show/hide settings"
                                        )
                                    }

                                    if (isRefreshing) {
                                        CircularProgressIndicator(
                                            Modifier
                                                .padding(end = 10.dp)
                                        )
                                    } else {
                                        IconButton({
                                            vm.refresh(appState.databaseUpdated)
                                        }) {
                                            Icon(
                                                imageVector = Icons.Filled.Refresh,
                                                contentDescription = "Sync data",
                                                tint = MaterialTheme.colorScheme.onBackground
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        Column(
                            Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Top,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            if (settings) {
                                SettingsView()
                            } else {
                                Header(vm.tabs[selectedView].text)
                                vm.tabs[selectedView].destination()
                            }

                            Spacer(Modifier.height(20.dp))
                        }
                    }
                } else {
                    LoginView()
                }
            }
        }
    )
}