package com.github.aeoliux.violet.app.content.messages

import android.text.method.LinkMovementMethod
import android.widget.TextView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Label
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat
import com.github.aeoliux.violet.app.components.ShapeBox
import com.github.aeoliux.violet.app.content.prettyFormatted
import com.github.aeoliux.violet.storage.Message
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MessageView(
    messageLabel: MessagesViewModel.MessageMetadata,
    viewModel: MessageViewModel = koinViewModel<MessageViewModel>(),
    onNavKey: (navKey: Message) -> Unit
) {
    val message by viewModel.message.collectAsState()
    val metadata by viewModel.metadata.collectAsState()

    LaunchedEffect(messageLabel) {
        viewModel.setMetadata(messageLabel)
    }

    LazyColumn {
        metadata?.let { metadata ->
            item {
                Card(
                    colors = CardDefaults.cardColors(contentColor = MaterialTheme.colorScheme.onSurface),
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .padding(20.dp)
                            .fillMaxWidth()
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            ShapeBox(
                                label = metadata.senderLabel,
                                shape = metadata.theme.second.toShape(),
                                containerColor = metadata.theme.first
                            )

                            Spacer(Modifier.width(20.dp))

                            Text(
                                text = metadata.messageLabel.sender,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier
                                    .width(280.dp)
                            )
                        }

                        HorizontalDivider(Modifier.padding(top = 20.dp, bottom = 20.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Label,
                                contentDescription = "Topic"
                            )

                            Spacer(Modifier.width(15.dp))

                            Text(
                                text = "Topic"
                            )
                        }

                        Text(
                            text = metadata.messageLabel.topic,
                            modifier = Modifier
                                .padding(top = 15.dp)
                        )

                        metadata.messageLabel.sentAt?.let { datetime ->
                            HorizontalDivider(Modifier.padding(top = 20.dp, bottom = 20.dp))

                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CalendarToday,
                                    contentDescription = "Date"
                                )

                                Spacer(Modifier.width(15.dp))

                                Text(
                                    text = "Sent at"
                                )
                            }

                            Text(
                                text = datetime.prettyFormatted(),
                                modifier = Modifier
                                    .padding(top = 15.dp)
                            )
                        }
                    }
                }
            }
        }

        message?.let { message ->
            item {
                Spacer(Modifier.height(15.dp))

                Card(
                    colors = CardDefaults.cardColors(contentColor = MaterialTheme.colorScheme.onSurface),
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .padding(20.dp)
                    ) {
                        Row {
                            Icon(
                                imageVector = Icons.Default.Mail,
                                contentDescription = "Message content"
                            )

                            Spacer(Modifier.width(15.dp))

                            Text(
                                text = "Message content"
                            )
                        }

                        HorizontalDivider(Modifier.padding(top = 20.dp, bottom = 20.dp))

                        val color = MaterialTheme.colorScheme.onSurface

                        AndroidView(
                            factory = {
                                TextView(it).apply {
                                    setTextColor(color.toArgb())
                                    movementMethod = LinkMovementMethod.getInstance()
                                }
                            },
                            update = { textView ->
                                textView.text = HtmlCompat.fromHtml(
                                    message.content,
                                    HtmlCompat.FROM_HTML_MODE_COMPACT
                                )
                            }
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