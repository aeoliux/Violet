package com.github.aeoliux.violet.app.messages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.aeoliux.violet.api.scraping.messages.Message
import com.github.aeoliux.violet.app.appState.AppState
import com.github.aeoliux.violet.app.appState.LocalAppState
import com.github.aeoliux.violet.app.appState.formatDateTime
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MessageView(
    url: String,
    appState: AppState = LocalAppState.current,
    vm: MessageViewModel = koinViewModel<MessageViewModel>(),
    onClose: () -> Unit
) {
    val message by vm.message.collectAsState()

    LaunchedEffect(Unit) {
        vm.fetchMessage(url)
    }

    Row(Modifier.fillMaxWidth()) {
        IconButton(
            onClick = {
                vm.closeMessage()
                onClose()
            },
            modifier = Modifier.padding(start = 15.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                tint = MaterialTheme.colorScheme.onBackground,
                contentDescription = "Go back"
            )
        }
    }

    Divider(Modifier.padding(15.dp))

    Column(Modifier.padding(start = 15.dp, end = 15.dp)) {
        message?.let {
            val labels = listOf("From", "Topic", "Sent at")
            val vals = listOf(appState.safe("a human (?)", it.sender), it.topic, it.date.formatDateTime())

            labels.forEachIndexed { index, label ->
                val value = vals[index]
                value?.let {
                    Row {
                        Text(
                            fontWeight = FontWeight.SemiBold,
                            text = label,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text(
                            text = "$it",
                            textAlign = TextAlign.End,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }

                    Divider(Modifier.padding(top = 15.dp, bottom = 15.dp))
                }
            }

            Card {
                Column(Modifier.fillMaxWidth().padding(10.dp)) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        fontSize = 12.sp,
                        text = it.content,
                        style = LocalTextStyle.current.merge(
                            TextStyle(
                                lineHeight = 1.5.em,
                                lineHeightStyle = LineHeightStyle(
                                    trim = LineHeightStyle.Trim.Both,
                                    alignment = LineHeightStyle.Alignment.Center
                                ),
                            )
                        )
                    )
                }
            }
        }?: CircularProgressIndicator()
    }
}