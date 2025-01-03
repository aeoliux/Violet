package com.github.aeoliux.violet.app.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.aeoliux.violet.app.appState.LocalAppState
import com.github.aeoliux.violet.app.components.LoadingIndicator
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeView(vm: HomeViewModel = koinViewModel<HomeViewModel>()) {
    val appState = LocalAppState.current

    val me by vm.me.collectAsState()
    val classInfo by vm.classInfo.collectAsState()
    val isLoaded by vm.isLoaded.collectAsState()
    val luckyNumber by vm.luckyNumber.collectAsState()

    LaunchedEffect(appState.databaseUpdated.value) {
        vm.launchedEffect(appState.semester)
    }

    if (isLoaded) {
        Card(Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(10.dp)) {
            Column(
                Modifier.fillMaxWidth().wrapContentHeight().padding(10.dp),
                verticalArrangement = Arrangement.Top
            ) {
                Text(text = "Hi, ${appState.safe("Human (?)", me?.firstName?:"")}", fontSize = 40.sp, fontWeight = FontWeight.Bold)
                Text(text = "Lucky number is/will be $luckyNumber", fontSize = 20.sp)
            }
        }
    } else {
        LoadingIndicator()
    }
}