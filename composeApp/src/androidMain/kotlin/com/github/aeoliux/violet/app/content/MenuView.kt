package com.github.aeoliux.violet.app.content

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Announcement
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Announcement
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
import com.github.aeoliux.violet.app.layout.LazyLayout
import com.github.aeoliux.violet.app.layout.SectionHeader
import com.github.aeoliux.violet.app.layout.SectionListItem

@Composable
fun MenuView(
    onNavKey: (navKey: Any) -> Unit
) {
    val tabs = listOf(
        Triple("Agenda", Icons.Default.CalendarMonth, NavRoutes.Agenda),
        Triple("Attendance", Icons.Default.PersonAdd, NavRoutes.Attendance),
        Triple("School notices", Icons.AutoMirrored.Filled.Announcement, NavRoutes.SchoolNotices),
        Triple("Messages", Icons.Default.Mail, NavRoutes.Messages)
    )

    LazyLayout(
        header = "Menu"
    ) {
        item {
            SectionHeader("Actions")
        }

        itemsIndexed(tabs) { index, (name, icon, route) ->
            SectionListItem(
                index = index,
                lastIndex = tabs.lastIndex,

                leading = {
                    Icon(
                        imageVector = icon,
                        contentDescription = name,
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                },
                header = name,
                trailing = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = name
                    )
                },
                onClick = {
                    onNavKey(route)
                }
            )
        }
    }
}