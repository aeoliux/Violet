package com.github.aeoliux.violet.app.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.github.aeoliux.violet.app.appState.LocalAppState
import com.github.aeoliux.violet.app.components.Header
import com.github.aeoliux.violet.app.login.LoginView
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainView(vm: MainViewModel = koinViewModel<MainViewModel>()) {
    val appState = LocalAppState.current

    val isRefreshing by vm.isRefreshing.collectAsState()

    val scrollState = rememberScrollState()
    val navController = rememberNavController()
    val currentStack by navController.currentBackStackEntryAsState()
    val tabs = remember {
        vm.tabs
            .plus(TabItem("Menu", {
                MenuView({
                    navController.navigate(it)
                }, vm.tabs)
            }, true, Icons.Rounded.Menu))
            .plus(TabItem("Log in", {
                LoginView({
                    navController.navigate(vm.tabs[0].text)
                    vm.refresh(appState.databaseUpdated)
                })
            }))
    }

    var title by remember { mutableStateOf("Unknown") }
    LaunchedEffect(currentStack) {
        title = currentStack?.destination?.route ?: "Unknown"
    }

    Scaffold(
        modifier = Modifier,
        topBar = {
            TopAppBar(
                modifier = Modifier,
                title = {
                    Row(Modifier.fillMaxSize(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = title, fontWeight = FontWeight.Bold, fontSize = 30.sp)
                        }
                        if (appState.isLoggedIn.value) {
                            Row(
                                horizontalArrangement = Arrangement.End,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
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
                },
            )
        },
        bottomBar = {
            BottomAppBar {

                Row(
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    if (appState.isLoggedIn.value) {
                        tabs.filter { it.important }.forEach {
                            TextButton({
                                navController.navigate(it.text)
                            }) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.SpaceEvenly
                                ) {
                                    it.icon?.let { icon ->
                                        Icon(
                                            imageVector = icon,
                                            contentDescription = it.text,
                                            tint = MaterialTheme.colorScheme.onBackground
                                        )
                                    }
                                    Text(
                                        text = it.text,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 18.sp,
                                        color = MaterialTheme.colorScheme.onBackground
                                    )
                                }
                            }
                        }
                    }
                }
            }
        },
        content = {
            Box(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
                Column {
                    NavHost(navController, if (appState.isLoggedIn.value) "Home" else "Log in") {
                        tabs.forEach { v ->
                            composable(v.text) {
                                Column(
                                    Modifier.fillMaxSize().verticalScroll(scrollState),
                                    verticalArrangement = Arrangement.Top,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Spacer(Modifier.height(70.dp))
                                    v.destination()
                                    Spacer(Modifier.height(120.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    )
}