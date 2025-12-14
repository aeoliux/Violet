package com.github.aeoliux.app.content

import androidx.compose.ui.graphics.Color

fun String.toColorLong(): Color {
    return Color("FF$this".toLong(16))
}