package com.github.aeoliux.violet.app.agenda

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.github.aeoliux.violet.api.types.AgendaItem
import com.github.aeoliux.violet.app.appState.LocalAppState
import com.github.aeoliux.violet.app.components.Dialog

@Composable
fun AgendaDialog(agendaItem: AgendaItem, onDismiss: () -> Unit) {
    val appState = LocalAppState.current

    Dialog({ onDismiss() }) {
        Column(Modifier.wrapContentSize()) {
            Text("Category: ${agendaItem.category}")
            agendaItem.subject?.let { Text("Subject: $it") }
            agendaItem.classroom?.let { Text("Classroom: ${appState.safe("your favorite", it)}") }
            Text("Added by: ${appState.safe("Someone", agendaItem.createdBy)}")
            Text("Added at: ${agendaItem.addedAt}")
            Text("Starts at: ${agendaItem.timeFrom}")
            Text("Content: ${agendaItem.content}")
        }
    }
}