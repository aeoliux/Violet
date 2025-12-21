package com.github.aeoliux.violet.app.content.agenda

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.github.aeoliux.violet.app.components.ShapeBox
import com.github.aeoliux.violet.app.content.formatWithWeekday
import com.github.aeoliux.violet.app.layout.LazyLayout
import com.github.aeoliux.violet.app.layout.SectionHeader
import com.github.aeoliux.violet.app.layout.SectionListItem
import com.github.aeoliux.violet.storage.Agenda
import org.koin.compose.koinInject

@Composable
fun AgendaView(
    viewModel: AgendaViewModel = koinInject<AgendaViewModel>(),
    onNavKey: (navKey: Agenda) -> Unit
) {
    val agenda by viewModel.agenda.collectAsState()

    val isRefreshing by viewModel.isRefreshing.collectAsState()

    LazyLayout(
        header = "Agenda",
        isRefreshing = isRefreshing,
        onRefresh = { viewModel.refresh() }
    ) {
        item {
            TextButton(
                onClick = { viewModel.showEarlier() },
                modifier = Modifier
                    .padding(start = 5.dp)
            ) {
                Text("Show previous events...")
            }

            HorizontalDivider()
        }

        agenda.entries.forEach { (date, agenda) ->
            item {
                SectionHeader(date.formatWithWeekday())
            }

            itemsIndexed(agenda) { index, (item, theme) ->
                SectionListItem(
                    index = index,
                    lastIndex = agenda.lastIndex,

                    header = "${item.category}, ${item.subject}",
                    subheaders = listOf(
                        item.content
                    ),

                    leading = {
                        ShapeBox(
                            modifier = Modifier
                                .width(70.dp)
                                .height(70.dp),
                            label = theme.first,
                            shape = theme.third.toShape(),
                            containerColor = theme.second,
                            contentColor = Color.Black
                        )
                    }
                )
            }
        }
    }

//    PullToRefreshBox(
//        isRefreshing = isRefreshing,
//        onRefresh = { viewModel.refresh() }
//    ) {
//        LazyColumn(
//            modifier = Modifier
//                .fillMaxSize()
//        ) {
//            item {
//                Text(text = "Agenda", fontSize = 32.sp)
//            }
//
//            item {
//                Spacer(Modifier.height(45.dp))
//
//                TextButton({ viewModel.showEarlier() }) {
//                    Text(
//                        text = "Show previous events..."
//                    )
//                }
//
//                HorizontalDivider(Modifier.padding(bottom = 15.dp))
//            }
//
//            itemsIndexed(agenda.entries.toList()) { index, (date, agenda) ->
//                Text(
//                    text = date.formatWithWeekday()
//                )
//
//                Spacer(Modifier.height(15.dp))
//
//                agenda.forEachIndexed { index, (entry, theme) ->
//                    Card(
//                        colors = CardDefaults.cardColors(contentColor = MaterialTheme.colorScheme.onSurface),
//                        modifier = Modifier
//                            .fillMaxWidth(),
//                    ) {
//                        Row(
//                            modifier = Modifier
//                                .padding(15.dp),
//                            verticalAlignment = Alignment.CenterVertically
//                        ) {
//                            ShapeBox(
//                                modifier = Modifier
//                                    .width(70.dp)
//                                    .height(70.dp),
//                                label = theme.first,
//                                shape = theme.third.toShape(),
//                                containerColor = theme.second,
//                                contentColor = Color.Black
//                            )
//
//                            Spacer(Modifier.width(15.dp))
//
//                            Column(
//                                modifier = Modifier
//                                    .fillMaxWidth()
//                            ) {
//                                Text(
//                                    text = "${entry.category}, ${entry.subject}",
//                                    fontSize = 18.sp,
//                                    fontWeight = FontWeight.SemiBold
//                                )
//
//                                Text(
//                                    text = entry.content
//                                )
//                            }
//                        }
//                    }
//
//                    Spacer(Modifier.height(15.dp))
//                }
//
//                HorizontalDivider(Modifier.padding(bottom = 15.dp))
//            }
//
//            item {
//                Spacer(Modifier.height(10.dp))
//            }
//        }
//    }
}