package com.github.aeoliux.api.bodys

import kotlinx.serialization.Serializable

@Serializable
data class Color(
    val Id: Int,
    val RGB: String
)

@Serializable
data class Colors(val Colors: List<Color>) {
    fun toMap(): LinkedHashMap<Int, String> {
        return Colors.fold(LinkedHashMap()) { acc, color ->
            acc[color.Id] = color.RGB
            acc
        }
    }
}