package com.github.aeoliux.violet.app.content.messages

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.aeoliux.violet.app.components.SearchBar
import com.github.aeoliux.violet.app.components.ShapeBox
import org.koin.compose.koinInject

@Composable
fun MessagesView(
    viewModel: MessagesViewModel = koinInject<MessagesViewModel>(),
    onNavKey: (navKey: MessagesViewModel.MessageMetadata) -> Unit
) {
    val messages by viewModel.messageLabels.collectAsState()
    val categoriesOrdered by viewModel.categoriesOrdered.collectAsState()

    val searchQuery by viewModel.query.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()

    var expanded by remember { mutableStateOf(false) }

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = { viewModel.refresh() }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {
            item {
                Text(
                    text = "Messages",
                    fontSize = 32.sp,
                    modifier = Modifier.padding(bottom = 10.dp)
                )
            }

            item {
                SearchBar(searchQuery) { viewModel.setQuery(it) }

                HorizontalDivider(Modifier.padding(top = 10.dp, bottom = 10.dp))
            }

            item {
                Card(
                    modifier = (if (expanded)
                        Modifier.wrapContentHeight()
                    else
                        Modifier.height(50.dp))
                        .fillMaxWidth()
                ) {
                    categoriesOrdered.forEachIndexed { index, category ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .clickable {
                                    expanded = !expanded
                                    viewModel.setCategory(category)
                                },
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = category.name,
                                modifier = Modifier
                                    .padding(start = 15.dp)
                            )

                            if (index == 0)
                                Icon(
                                    imageVector = if (expanded)
                                        Icons.Default.KeyboardArrowUp
                                    else
                                        Icons.Default.KeyboardArrowDown,
                                    contentDescription = "Expand/Hide",
                                    modifier = Modifier
                                        .padding(end = 15.dp)
                                )
                        }
                    }
                }
            }

            itemsIndexed(messages) { index, metadata ->
                HorizontalDivider(Modifier.padding(top = 10.dp, bottom = 10.dp))

                Row(
                    modifier = Modifier
                        .height(50.dp)
                        .fillMaxWidth()
                        .clickable { onNavKey(metadata) },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ShapeBox(
                        label = metadata.senderLabel,
                        shape = metadata.theme.second.toShape(),
                        containerColor = metadata.theme.first
                    )

                    Spacer(Modifier.width(15.dp))

                    Column(
                        modifier = Modifier
                            .fillMaxHeight(),
                        verticalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Text(
                            text = metadata.messageLabel.topic,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,

                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                                .width(240.dp)
                        )

                        Text(
                            text = metadata.messageLabel.sender,
                            fontSize = 14.sp,

                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                                .width(240.dp)
                        )
                    }

                    metadata.parsedDatetime?.let { datetime ->
                        Column(
                            modifier = Modifier
                                .fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.End
                        ) {
                            Text(
                                text = datetime.first
                            )

                            Text(
                                text = datetime.second
                            )
                        }
                    }
                }
            }

            item {
                Spacer(Modifier.height(25.dp))
            }
        }
    }
}
