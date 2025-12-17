package com.github.aeoliux.app.content.attendance

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.aeoliux.app.components.ShapeBox
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AttendanceView(
    viewModel: AttendanceViewModel = koinViewModel<AttendanceViewModel>(),
    onNavKey: (Any) -> Unit
) {
    val attendance by viewModel.attendance.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()

    PullToRefreshBox(
        modifier = Modifier.fillMaxSize(),
        isRefreshing = isRefreshing,
        onRefresh = { viewModel.refresh() }
    ) {
        LazyColumn(Modifier.fillMaxSize()) {
            item {
                Text(text = "Attendance", fontSize = 32.sp, modifier = Modifier.padding(bottom = 40.dp))
            }

            items(attendance.entries.toList()) { (date, entries) ->
                Text(
                    text = date,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .padding(start = 25.dp, bottom = 5.dp)
                )

                Card(
                    modifier = Modifier
                        .fillMaxWidth(),
                    colors = CardDefaults.cardColors(contentColor = MaterialTheme.colorScheme.onSurface)
                ) {
                    entries.forEachIndexed { index, entry ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp)
                                .padding(15.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
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

                            Spacer(Modifier.width(15.dp))

                            Column(
                                modifier = Modifier
                                    .fillMaxSize(),
                                verticalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = entry.attendance.type,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.SemiBold
                                )

                                Text(
                                    text = "Lesson no. ${entry.attendance.lessonNo}"
                                )

                                Text(
                                    text = "By ${entry.attendance.addedBy}"
                                )
                            }
                        }

                        if (index < entries.lastIndex)
                            HorizontalDivider(
                                color = MaterialTheme.colorScheme.surfaceContainer,
                                thickness = 3.dp
                            )
                    }
                }

                Spacer(Modifier.height(25.dp))
            }
        }
    }
}