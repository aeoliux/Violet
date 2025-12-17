package com.github.aeoliux.violet.app.content

import androidx.compose.ui.graphics.Color

fun String.toColorLong(): Color {
    return Color("FF$this".toLong(16))
}