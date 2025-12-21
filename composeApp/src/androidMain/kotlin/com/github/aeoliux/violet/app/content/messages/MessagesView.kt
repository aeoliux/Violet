package com.github.aeoliux.violet.app.content.messages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.github.aeoliux.violet.api.scraping.messages.MessageCategories
import com.github.aeoliux.violet.app.components.ProfilePicture
import com.github.aeoliux.violet.app.components.SearchBar
import com.github.aeoliux.violet.app.components.ShapeBox
import com.github.aeoliux.violet.app.content.NavRoutes
import com.github.aeoliux.violet.app.layout.BottomAction
import com.github.aeoliux.violet.app.layout.LazyLayout
import com.github.aeoliux.violet.app.layout.SectionListItem
import org.koin.compose.koinInject

@Composable
fun MessagesView(
    viewModel: MessagesViewModel = koinInject<MessagesViewModel>(),
    onNavKey: (navKey: Any) -> Unit
) {
    val category by viewModel.category.collectAsState()
    val messages by viewModel.messageLabels.collectAsState()
    val searchQuery by viewModel.query.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()

    LazyLayout(
        header = "Messages",
        isRefreshing = isRefreshing,
        onRefresh = { viewModel.refresh() },
        actions = {
            BottomAction(
                modifier = Modifier
                    .align(Alignment.BottomEnd),
                shape = MaterialShapes.Cookie9Sided.toShape(),
                imageVector = Icons.AutoMirrored.Filled.Send,
                contentDescription = "Send message",
                onClick = { onNavKey(NavRoutes.MessageEditor(null, null)) }
            )
        }
    ) {
        stickyHeader {
            Surface(
                color = MaterialTheme.colorScheme.surfaceContainer,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    SearchBar(searchQuery) { viewModel.setQuery(it) }

                    Spacer(Modifier.height(15.dp))
                }
            }
        }

        item {
            Spacer(Modifier.height(20.dp))

            PrimaryTabRow(
                selectedTabIndex = category,
                containerColor = MaterialTheme.colorScheme.surfaceContainer
            ) {
                MessageCategories.entries.forEachIndexed { index, cat ->
                    Tab(
                        selected = index == category,
                        onClick = { viewModel.setCategory(index) }
                    ) {
                            Text(
                                textAlign = TextAlign.Center,
                                text = cat.name,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp)
                            )
                    }
                }
            }

            Spacer(Modifier.height(20.dp))
        }

        itemsIndexed(messages) { index, message ->
            SectionListItem(
                index = index,
                lastIndex = messages.lastIndex,

                onClick = { onNavKey(message) },

                header = message.messageLabel.sender,
                subheaders = listOf(
                    message.messageLabel.topic
                ),

                leading = {
                    ProfilePicture(
                        name = message.messageLabel.sender,
                        shape = message.theme.second.toShape(),
                        containerColor = message.theme.first
                    )
                },

                trailing = message.parsedDatetime?.let { (date, time) ->
                    {
                        Text("$date, $time")
                    }
                }
            )
        }
    }
}
