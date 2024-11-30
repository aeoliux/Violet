package com.github.aeoliux.violet.app.agenda

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.github.aeoliux.violet.api.toColorLong
import com.github.aeoliux.violet.app.appState.LocalAppState
import com.github.aeoliux.violet.app.components.Header
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Composable
fun AgendaView(vm: AgendaViewModel = viewModel { AgendaViewModel() }) {
    val appState = LocalAppState.current
    val isLoaded by vm.isLoaded.collectAsState()
    val agenda by vm.agenda.collectAsState()
    val isSomethingSelected by vm.isSomethingSelected.collectAsState()
    val selectedAgendaItem by vm.selectedAgendaItem.collectAsState()

    LaunchedEffect(appState.databaseUpdated.value) {
        vm.launchedEffect()
    }

    if (isLoaded) {
        var dateIndex = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date.toEpochDays()
        val startDate = dateIndex

        while (dateIndex < startDate + 90) {
            val date = LocalDate.fromEpochDays(dateIndex)

            Card(
                Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(start = 10.dp, end = 10.dp, top = 2.dp, bottom = 2.dp)
            ) {
                val agenda = agenda[date]

                Column(
                    Modifier
                        .padding(10.dp)
                        .wrapContentHeight()
                ) {
                    Row {
                        Text(
                            text = "${date.dayOfMonth} ${date.month.name}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    }

                    agenda?.forEach { (lessonNo, agenda) ->
                        agenda.forEach { agendaItem ->
                            Row(Modifier
                                .padding(2.dp)
                                .border(width = 2.dp, color = agendaItem.color.toColorLong(), shape = RoundedCornerShape(10.dp))
                                .fillMaxWidth()) {
                                Column(Modifier.padding(10.dp).clickable { vm.showAgendaInfo(agendaItem) }) {
                                    Text(
                                        text = "${agendaItem.category} - ${agendaItem.content}",
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 17.sp
                                    )

                                    Text("${agendaItem.timeFrom} ${
                                        if (lessonNo != 0u)
                                            " ($lessonNo)"
                                        else
                                            ""
                                    }, ${agendaItem.createdBy}, ${agendaItem.subject}")
                                }
                            }
                        }
                    }
                }
            }

            dateIndex++
        }

        if (isSomethingSelected && selectedAgendaItem != null)
            AgendaDialog(selectedAgendaItem!!) { vm.closeAgendaDialog() }
    }
}