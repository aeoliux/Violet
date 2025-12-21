package com.github.aeoliux.violet.app.components

import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape

@Composable
fun ProfilePicture(
    name: String,
    shape: Shape,
    containerColor: Color,
) {
    var label by remember { mutableStateOf("") }

    LaunchedEffect(name) {
        val split = name.split(" ")

        label = ((split.getOrNull(0)?.uppercase()?.substring(0, 1) ?: "")
                +
                (split.getOrNull(1)?.uppercase()?.substring(0, 1) ?: ""))
    }

    ShapeBox(
        label = label,
        shape = shape,
        containerColor = containerColor
    )
}