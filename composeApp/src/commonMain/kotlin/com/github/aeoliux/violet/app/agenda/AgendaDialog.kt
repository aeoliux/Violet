package com.github.aeoliux.violet.app.agenda

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.github.aeoliux.violet.api.types.AgendaItem
import com.github.aeoliux.violet.app.components.Dialog

@Composable
fun AgendaDialog(agendaItem: AgendaItem, onDismiss: () -> Unit) {
    Dialog({ onDismiss() }) {
        Column(Modifier.wrapContentSize()) {
            Text("Category: ")
            if (agendaItem.subject != null) Text("Subject: ")
            if (agendaItem.classroom != null) Text("Classroom: ")
            Text("Added by: ")
            Text("Added at: ")
            Text("Starts at: ")

            Text("Content: ")
        }
        Column(Modifier.wrapContentHeight()) {
            Text(agendaItem.category)
            if (agendaItem.subject != null) Text(agendaItem.subject)
            if (agendaItem.classroom != null) Text(agendaItem.classroom)
            Text(agendaItem.createdBy)
            Text(agendaItem.addedAt.toString())
            Text(agendaItem.timeFrom)
            Text(agendaItem.content)
        }
    }
}