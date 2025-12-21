package com.github.aeoliux.violet.app.content.messages

import android.text.method.LinkMovementMethod
import android.widget.TextView
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Label
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.NewLabel
import androidx.compose.material.icons.filled.TurnLeft
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat
import com.github.aeoliux.violet.api.scraping.messages.MessageCategories
import com.github.aeoliux.violet.app.components.ProfilePicture
import com.github.aeoliux.violet.app.components.ShapeBox
import com.github.aeoliux.violet.app.components.ShapeBoxComposable
import com.github.aeoliux.violet.app.content.NavRoutes
import com.github.aeoliux.violet.app.content.prettyFormatted
import com.github.aeoliux.violet.app.layout.BottomAction
import com.github.aeoliux.violet.app.layout.LazyLayout
import com.github.aeoliux.violet.app.layout.SectionHeader
import com.github.aeoliux.violet.app.layout.SectionListItem
import com.github.aeoliux.violet.app.layout.SectionListItemComposable
import com.github.aeoliux.violet.storage.Message
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MessageView(
    messageLabel: MessagesViewModel.MessageMetadata,
    viewModel: MessageViewModel = koinViewModel<MessageViewModel>(),
    onNavKey: (navKey: Any) -> Unit
) {
    val message by viewModel.message.collectAsState()
    val metadata by viewModel.metadata.collectAsState()

    LaunchedEffect(messageLabel) {
        viewModel.setMetadata(messageLabel)
    }

    LazyLayout(
        actions = {
            if (message != null && messageLabel.messageLabel.category == MessageCategories.Received)
                BottomAction(
                    modifier = Modifier
                        .align(Alignment.BottomEnd),
                    shape = MaterialShapes.Cookie9Sided.toShape(),
                    imageVector = Icons.Default.TurnLeft,
                    contentDescription = "Respond",
                    onClick = { onNavKey(NavRoutes.MessageEditor(message, messageLabel.messageLabel)) }
                )
        }
    ) {
        metadata?.let { metadata ->
            item {
                SectionHeader("Info")
            }

            item {
                SectionListItem(
                    index = 0,
                    lastIndex = if (metadata.messageLabel.sentAt == null) 1 else 2,
                    header = metadata.messageLabel.sender,
                    leading = {
                        ProfilePicture(
                            name = metadata.messageLabel.sender,
                            shape = metadata.theme.second.toShape(),
                            containerColor = metadata.theme.first
                        )
                    }
                )
            }

            item {
                SectionListItem(
                    index = 1,
                    lastIndex = if (metadata.messageLabel.sentAt == null) 1 else 2,
                    header = "Topic",
                    leading = {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Label,
                            contentDescription = "Topic"
                        )
                    },
                    subheaders = listOf(
                        metadata.messageLabel.topic
                    )
                )
            }

            metadata.messageLabel.sentAt?.let { sentAt ->
                item {
                    SectionListItem(
                        index = 2,
                        lastIndex = 2,
                        header = "Sent at",
                        leading = {
                            Icon(
                                imageVector = Icons.Default.CalendarToday,
                                contentDescription = "Sent at"
                            )
                        },
                        subheaders = listOf(
                            sentAt.prettyFormatted()
                        )
                    )
                }
            }
        }

        message?.let { message ->
            item {
                SectionHeader("Message")
            }

            item {
                SectionListItem(
                    index = 0,
                    lastIndex = 1,
                    header = "Message content",

                    leading = {
                        Icon(
                            imageVector = Icons.Default.Mail,
                            contentDescription = "Message content"
                        )
                    }
                )
            }

            item {
                SectionListItemComposable(
                    index = 1,
                    lastIndex = 1,

                    header = {
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
                )
            }
        }
    }
}