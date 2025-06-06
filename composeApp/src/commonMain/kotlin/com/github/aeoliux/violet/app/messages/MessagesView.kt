package com.github.aeoliux.violet.app.messages

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.aeoliux.violet.api.scraping.messages.MessageCategories
import com.github.aeoliux.violet.app.appState.AppState
import com.github.aeoliux.violet.app.appState.LocalAppState
import com.github.aeoliux.violet.app.components.ComboBox
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MessagesView(
    vm: MessagesViewModel = koinViewModel<MessagesViewModel>()
) {
    val appState: AppState = LocalAppState.current

    val messages by vm.messages.collectAsState()
    val selectedMessage by vm.selectedMessage.collectAsState()
    val selectedCategory by vm.messagesCategory.collectAsState()
    val categories = listOf(MessageCategories.Received, MessageCategories.Sent, MessageCategories.Bin)

    LaunchedEffect(appState.databaseUpdated.value) {
        vm.selectMessages()
    }

    if (selectedMessage != null) {
        selectedMessage?.let {
            MessageView(
                url = it,
                onClose = { vm.selectMessage(null) }
            )
        }
    } else {
        ComboBox(
            options = listOf("Received", "Sent", "Bin"),
            values = listOf(MessageCategories.Received, MessageCategories.Sent, MessageCategories.Bin),
            selectedIndex = categories.indexOf(selectedCategory)
        ) {
            vm.selectCategory(it)
        }

        Divider(Modifier.padding(10.dp).fillMaxWidth())

        messages[selectedCategory]?.forEach {
            Card(
                Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(start = 10.dp, end = 10.dp, top = 2.dp, bottom = 2.dp)
                    .clickable {
                        vm.selectMessage(it.url)
                    }
            ) {
                Column(
                    Modifier.padding(10.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(appState.safe("Someone", it.sender))
                    Text(it.topic)
                }
            }
        }
    }
}