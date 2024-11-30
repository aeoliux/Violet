package com.github.aeoliux.violet.app.agenda

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.github.aeoliux.violet.api.types.AgendaItem
import com.github.aeoliux.violet.app.components.Dialog

@Composable
fun AgendaDialog(agendaItem: AgendaItem, onDismiss: () -> Unit) {
    Dialog({ onDismiss() }) {
        Column(Modifier.wrapContentSize()) {
            Text("Category: ${agendaItem.category}")
            agendaItem.subject?.let { Text("Subject: $it") }
            agendaItem.classroom?.let { Text("Classroom: $it") }
            Text("Added by: ${agendaItem.createdBy}")
            Text("Added at: ${agendaItem.addedAt}")
            Text("Starts at: ${agendaItem.timeFrom}")
            Text("Content: ${agendaItem.content}")
        }
    }
}