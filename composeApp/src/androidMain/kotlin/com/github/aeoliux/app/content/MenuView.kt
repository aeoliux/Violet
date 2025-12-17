package com.github.aeoliux.app.content

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MenuView(
    onNavKey: (navKey: Any) -> Unit
) {
    val tabs = listOf(
        Triple("Messages", Icons.Default.Mail, NavRoutes.Messages),
        Triple("Agenda", Icons.Default.CalendarMonth, NavRoutes.Agenda),
        Triple("Attendance", Icons.Default.PersonAdd, NavRoutes.Attendance)
    )

    LazyColumn {
        item {
            Text(text = "Menu", fontSize = 32.sp)
            Spacer(Modifier.height(10.dp))
        }

        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                tabs.forEachIndexed { index, (label, icon, route) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .clickable { onNavKey(route) },
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row {
                            Spacer(Modifier.width(15.dp))

                            Icon(
                                imageVector = icon,
                                contentDescription = label,
                                tint = MaterialTheme.colorScheme.onSurface
                            )

                            Spacer(Modifier.width(15.dp))

                            Text(
                                text = label,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = label,
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier
                                .padding(end = 15.dp)
                        )
                    }

                    if (index < tabs.lastIndex)
                        HorizontalDivider(
                            color = MaterialTheme.colorScheme.surfaceContainer,
                            thickness = 3.dp
                        )
                }
            }
        }

        item {
            Spacer(Modifier.height(25.dp))
        }
    }
}