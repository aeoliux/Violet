package com.github.aeoliux.violet.app.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun <T> ComboBox(
    options: List<String>,
    values: List<T>,
    selectedIndex: Int = 0,
    onChange: (opt: T) -> Unit = {}
) {
    var selectedIndex by remember { mutableStateOf(selectedIndex) }

    Card(
        Modifier.fillMaxWidth()
            .wrapContentHeight()
            .padding(start = 10.dp, end = 10.dp, top = 2.dp, bottom = 2.dp)
    ) {
        ExpandableList(
            header = { Text(
                text = options[selectedIndex],
                modifier = Modifier.padding(15.dp)
            ) },
        ) {
            options.forEachIndexed { index, it ->
                Row(
                    Modifier
                        .fillMaxSize()
                        .clickable {
                            selectedIndex = index
                            onChange(values[selectedIndex])
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier
                            .padding(15.dp),
                        text = it
                    )
                }
            }
        }
    }
}