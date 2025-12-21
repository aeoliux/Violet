package com.github.aeoliux.violet.app.content.messages

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.aeoliux.violet.app.components.ProfilePicture
import com.github.aeoliux.violet.app.components.ShapeBoxComposable
import com.github.aeoliux.violet.app.layout.BottomAction
import com.github.aeoliux.violet.app.layout.LazyLayout
import com.github.aeoliux.violet.app.layout.SectionHeader
import com.github.aeoliux.violet.app.layout.SectionListItem
import com.github.aeoliux.violet.app.layout.SectionListItemComposable
import com.github.aeoliux.violet.storage.Message
import com.github.aeoliux.violet.storage.MessageLabel
import org.koin.compose.viewmodel.koinViewModel
import kotlin.random.Random

@Composable
fun MessageEditorView(
    messageLabel: MessageLabel?,
    message: Message?,
    viewModel: MessageEditorViewModel = koinViewModel<MessageEditorViewModel>(),
    onBack: () -> Unit
) {
    val respondsTo by viewModel.respondsTo.collectAsState()
    val requestKey by viewModel.requestKey.collectAsState()
    val topic by viewModel.topic.collectAsState()
    val content by viewModel.content.collectAsState()
    val users by viewModel.users.collectAsState()
    val selectedUsers by viewModel.selectedUsers.collectAsState()

    var userSelector by remember { mutableStateOf(false) }

    LaunchedEffect(message, messageLabel) {
        if (message != null && messageLabel != null) {
            viewModel.setTemplate(message, messageLabel)
        } else {
            viewModel.nonResponding()
        }
    }

    if (userSelector) {
        UsersSelector(users) {
            viewModel.selectUsers(it)
            userSelector = false
        }
    } else {
        LazyLayout(
            actions = {
                if ((respondsTo == null || selectedUsers.isEmpty()) && requestKey.isNotEmpty())
                    BottomAction(
                        modifier = Modifier
                            .align(Alignment.BottomStart),
                        shape = MaterialShapes.Cookie4Sided.toShape(),
                        imageVector = Icons.Default.Person,
                        contentDescription = "Select receivers",
                        onClick = { userSelector = true }
                    )

                if (requestKey.isNotEmpty() && selectedUsers.isNotEmpty() && content.isNotEmpty() && topic.isNotEmpty())
                    BottomAction(
                        modifier = Modifier
                            .align(Alignment.BottomEnd),
                        shape = MaterialShapes.SoftBurst.toShape(),
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Send message",
                        onClick = { viewModel.send { onBack() } }
                    )
            }
        ) {
            if (selectedUsers.isNotEmpty()) {
                item {
                    SectionHeader("Selected receivers")
                }

                itemsIndexed(selectedUsers) { index, user ->
                    SectionListItem(
                        index = index,
                        lastIndex = selectedUsers.lastIndex,

                        header = "${user.lastName} ${user.firstName}",
                        leading = {
                            val number = remember { Random.nextInt(0, 6) }

                            ProfilePicture(
                                name = "${user.lastName} ${user.firstName}",
                                containerColor = when {
                                    number == 1 -> Color.Red
                                    number == 2 -> Color.Green
                                    number == 3 -> Color.Black
                                    number == 4 -> Color.Cyan
                                    number == 5 -> Color.Magenta
                                    else -> Color.LightGray
                                },
                                shape = (when (number) {
                                            1 -> MaterialShapes.Cookie4Sided
                                            2 -> MaterialShapes.Flower
                                        3 -> MaterialShapes.SoftBurst
                                            4 -> MaterialShapes.Clover4Leaf
                                            5 -> MaterialShapes.Sunny
                                            else -> MaterialShapes.Cookie9Sided
                                        }).toShape()
                            )
                        }
                    )
                }
            }

            item {
                SectionHeader("Message")
            }

            item {
                SectionListItemComposable(
                    index = 0,
                    lastIndex = 1,

                    header = {
                        TextField(
                            value = topic,
                            onValueChange = { viewModel.setTopic(it) },
                            placeholder = { Text("Topic") },
                            maxLines = 1,
                            colors = TextFieldDefaults.colors(
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedContainerColor = Color.Transparent
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(0.dp, Color.Transparent)
                        )
                    }
                )
            }

            item {
                SectionListItemComposable(
                    index = 1,
                    lastIndex = 1,

                    header = {
                        TextField(
                            value = content,
                            minLines = 10,
                            onValueChange = { viewModel.setContent(it) },
                            placeholder = { Text("Message") },
                            colors = TextFieldDefaults.colors(
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedContainerColor = Color.Transparent
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(0.dp, Color.Transparent)
                        )
                    }
                )
            }
        }
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//        ) {
//            LazyColumn {
//                item {
//                    Text(
//                        text = if (respondsTo == null)
//                            "New message"
//                        else
//                            "Respond",
//                        fontSize = 32.sp,
//                        modifier = Modifier
//                            .padding(bottom = 20.dp)
//                    )
//
//                    if (selectedUsers.isNotEmpty())
//                        Text(
//                            text = "This message will be sent to:",
//                            fontWeight = FontWeight.SemiBold,
//                            fontSize = 18.sp
//                        )
//                }
//
//                itemsIndexed(selectedUsers) { index, user ->
//                    Spacer(Modifier.height(5.dp))
//
//                    Text(
//                        text = "${index + 1}. ${user.lastName} ${user.firstName}",
//                    )
//                }
//
//                item {
//                    HorizontalDivider(Modifier.padding(top = 20.dp, bottom = 20.dp))
//

//                }
//
//                item {
//                    Spacer(Modifier.height(20.dp))
//

//                }
//
//                item {
//                    Spacer(Modifier.height(150.dp))
//                }
//            }
//
//            if ((respondsTo == null || selectedUsers.isEmpty()) && requestKey.isNotEmpty())
//                Box(
//                    modifier = Modifier
//                        .align(Alignment.BottomStart)
//                        .background(Color.Transparent)
//                        .padding(bottom = 15.dp)
//                ) {
//                    ShapeBoxComposable(
//                        shape = MaterialShapes.Cookie4Sided.toShape(),
//                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
//                        modifier = Modifier
//                            .height(80.dp)
//                            .width(80.dp)
//                            .align(Alignment.Center)
//                            .clickable { userSelector = true }
//                    ) {
//                        Icon(
//                            imageVector = Icons.Default.Person,
//                            contentDescription = "Select receivers",
//                            tint = MaterialTheme.colorScheme.secondary,
//                            modifier = Modifier
//                                .fillMaxSize()
//                                .padding(25.dp)
//                        )
//                    }
//                }
//
//            if (requestKey.isNotEmpty() && selectedUsers.isNotEmpty() && content.isNotEmpty() && topic.isNotEmpty())
//                Box(
//                    modifier = Modifier
//                        .align(Alignment.BottomEnd)
//                        .background(Color.Transparent)
//                        .padding(bottom = 15.dp)
//                ) {
//                    ShapeBoxComposable(
//                        shape = MaterialShapes.Cookie9Sided.toShape(),
//                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
//                        modifier = Modifier
//                            .height(80.dp)
//                            .width(80.dp)
//                            .align(Alignment.Center)
//                            .clickable { viewModel.send { onBack() } }
//                    ) {
//                        Icon(
//                            imageVector = Icons.AutoMirrored.Filled.Send,
//                            contentDescription = "Send",
//                            tint = MaterialTheme.colorScheme.secondary,
//                            modifier = Modifier
//                                .fillMaxSize()
//                                .padding(25.dp)
//                        )
//                    }
//                }
//        }
    }
}