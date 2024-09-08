package com.github.aeoliux.violet.app.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ExpandableList(
    header: @Composable () -> Unit,
    expanded: Boolean = false,
    onChange: (() -> Unit)? = null,
    content: @Composable () -> Unit) {
    var isExpanded by remember { mutableStateOf(expanded) }

    Column(Modifier.fillMaxWidth()) {
        Row(
            Modifier
                .fillMaxWidth()
                .clickable {
                    isExpanded = !isExpanded
                    if (onChange != null)
                        onChange()
                }
                .height(50.dp)
                .padding(start = 15.dp),
            horizontalArrangement = Arrangement.Start,
        ) {
            Column(Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                header()
            }

            Column(Modifier
                .fillMaxSize()
                .width(50.dp)
                .padding(end = 15.dp),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = if (isExpanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                    contentDescription = if (isExpanded) "Expand" else "Collapse"
                )
            }
        }

        if (isExpanded) {
            Divider(
                Modifier
                    .padding(start = 10.dp, end = 10.dp)
            )

            Column(
                Modifier
                    .fillMaxWidth()
            ) {
                content()
            }
        }
    }
}