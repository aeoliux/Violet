package com.github.aeoliux.violet.app.content.attendance

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.aeoliux.violet.app.components.ShapeBox
import com.github.aeoliux.violet.app.layout.LazyLayout
import com.github.aeoliux.violet.app.layout.SectionHeader
import com.github.aeoliux.violet.app.layout.SectionListItem
import com.github.aeoliux.violet.app.layout.getListItemShape
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AttendanceView(
    viewModel: AttendanceViewModel = koinViewModel<AttendanceViewModel>(),
    onNavKey: (Any) -> Unit
) {
    val attendance by viewModel.attendance.collectAsState()
    val overallPercentage by viewModel.overallPercentage.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()

    var selectedTab by remember { mutableIntStateOf(0) }

    LazyLayout(
        header = "Attendance",
        isRefreshing = isRefreshing,
        onRefresh = { viewModel.refresh() }
    ) {
        stickyHeader {
            PrimaryTabRow(
                containerColor = MaterialTheme.colorScheme.surfaceContainer,
                selectedTabIndex = selectedTab
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 }
                ) {
                    Text(
                        text = "List",
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                    )
                }

                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 }
                ) {
                    Text(
                        text = "Summary",
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                    )
                }
            }
        }

        if (selectedTab == 0) {
            attendance.entries.forEach { (date, entries) ->
                item {
                    SectionHeader(date)
                }

                itemsIndexed(entries) { index, entry ->
                    SectionListItem(
                        index = index,
                        lastIndex = entries.lastIndex,
                        leading = {
                            ShapeBox(
                                label = entry.attendance.typeShort,
                                shape = entry.theme.second.toShape(),
                                containerColor = entry.theme.first,
                                contentColor = Color.Black,
                                fontSize = 20.sp,
                                modifier = Modifier
                                    .height(60.dp)
                                    .width(60.dp)
                            )
                        },
                        header = entry.attendance.type,
                        subheaders = listOf(
                            "${entry.attendance.addedBy}, lesson no. ${entry.attendance.lessonNo}"
                        )
                    )
                }
            }
        } else {
            item {
                SectionHeader("Summary")
            }

            item {
                SectionListItem(
                    index = 0,
                    lastIndex = 0,

                    header = "Attendance in this school year",
                    leading = {
                        ShapeBox(
                            label = "${overallPercentage.percentage}%",
                            shape = overallPercentage.theme.second.toShape(),
                            containerColor = overallPercentage.theme.first,
                            contentColor = Color.White,
                            fontSize = 20.sp,
                            modifier = Modifier
                                .height(70.dp)
                                .width(70.dp)
                        )
                    }
                )
            }
        }
    }
}