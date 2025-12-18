package com.github.aeoliux.violet.app.content.messages

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.aeoliux.violet.api.types.User
import kotlin.collections.getValue
import kotlin.collections.setValue

@Composable
fun UsersSelector(
    users: LinkedHashMap<Int, User>,
    onSelection: (users: List<User>) -> Unit
) {
    val selectedUsers = remember { mutableStateListOf<User>() }

    var usersSorted by remember { mutableStateOf(emptyList<User>()) }

    LaunchedEffect(users) {
        usersSorted = users.entries
            .sortedBy { (_, user) -> "${user.lastName} ${user.firstName}".lowercase() }
            .map { it.value }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {
            itemsIndexed(usersSorted) { index, user ->
                if (user.group == 4 || user.group == 1) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                val i = selectedUsers.indexOfFirst { it.senderId == user.senderId }
                                if (i == -1)
                                    selectedUsers.add(user)
                                else
                                    selectedUsers.removeAt(i)
                            }
                            .background(
                                selectedUsers
                                    .firstOrNull { it.senderId == user.senderId }
                                    ?.let { MaterialTheme.colorScheme.primaryContainer }
                                    ?: MaterialTheme.colorScheme.surfaceContainer
                            )
                    ) {
                        Spacer(Modifier.height(20.dp))

                        Column(
                            modifier = Modifier
                                .padding(start = 15.dp)
                        ) {
                            Text(
                                text = "${user.lastName} ${user.firstName}",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 18.sp
                            )

                            Text(
                                text = if (user.group == 1) "Principal" else "Teacher"
                            )
                        }

                        Spacer(Modifier.height(20.dp))
                    }

                    if (index < users.entries.size - 1)
                        HorizontalDivider()
                }
            }

            item {
                Spacer(Modifier.height(100.dp))
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        ) {
            Button(
                modifier = Modifier
                    .padding(15.dp)
                    .fillMaxWidth(),
                onClick = { onSelection(selectedUsers) }
            ) {
                Text("Confirm")
            }
        }
    }
}