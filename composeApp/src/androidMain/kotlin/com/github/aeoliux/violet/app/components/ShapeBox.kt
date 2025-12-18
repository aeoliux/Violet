package com.github.aeoliux.violet.app.components

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
@SuppressLint("ModifierParameter")
fun ShapeBox(
    label: String,
    shape: Shape,
    containerColor: Color,
    contentColor: Color = Color.White,
    fontSize: TextUnit = 18.sp,
    modifier: Modifier = Modifier
        .height(50.dp)
        .width(50.dp),
) {
    ShapeBoxComposable(
        shape = shape,
        containerColor = containerColor,
        modifier = modifier
    ) {
        Text(
            text = label,
            fontSize = fontSize,
            fontWeight = FontWeight.SemiBold,
            color = contentColor
        )
    }
}

@Composable
@SuppressLint("ModifierParameter")
fun ShapeBoxComposable(
    shape: Shape,
    containerColor: Color,
    modifier: Modifier = Modifier
        .height(50.dp)
        .width(50.dp),
    label: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .clip(shape)
            .background(containerColor),
        contentAlignment = Alignment.Center
    ) {
        label()
    }
}