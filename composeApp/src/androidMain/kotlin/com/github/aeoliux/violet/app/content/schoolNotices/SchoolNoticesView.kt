package com.github.aeoliux.violet.app.content.schoolNotices

import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.github.aeoliux.violet.app.content.minimalFormat
import com.github.aeoliux.violet.app.layout.LazyLayout
import com.github.aeoliux.violet.app.layout.SectionHeader
import com.github.aeoliux.violet.app.layout.SectionListItem
import com.github.aeoliux.violet.storage.SchoolNotice
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SchoolNoticesView(
    viewModel: SchoolNoticesViewModel = koinViewModel<SchoolNoticesViewModel>(),
    onSelect: (SchoolNotice) -> Unit
) {
    val schoolNotices by viewModel.schoolNotices.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()

    LazyLayout(
        header = "School notices",
        isRefreshing = isRefreshing,
        onRefresh = { viewModel.refresh() }
    ) {
        items(schoolNotices) { notice ->
            SectionHeader("${notice.addedBy}, ${notice.startDate.minimalFormat()} - ${notice.endDate.minimalFormat()}")
            SectionListItem(
                index = 0,
                lastIndex = 0,

                header = notice.subject,
                subheaders = listOf(
                    notice.content
                )
            )
        }
    }
}