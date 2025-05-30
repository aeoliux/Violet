package com.github.aeoliux.violet.app.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MenuView(onSelect: (String) -> Unit, tabs: List<TabItem>) {
    tabs.forEach {
        TextButton(onClick = { onSelect(it.text) }) {
            Card(Modifier.padding(0.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(15.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        it.icon?.let { icon ->
                            Icon(modifier = Modifier.padding(end = 5.dp), imageVector = icon, contentDescription = it.text, tint = MaterialTheme.colorScheme.onBackground)
                        }

                        Text(text = it.text, color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
                    }

                    Icon(imageVector = Icons.Filled.ArrowForward, contentDescription = it.text)
                }
            }
        }
    }
}