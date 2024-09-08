package com.github.aeoliux.violet.api.bodys

import kotlinx.serialization.Serializable

@Serializable
data class Color(
    val Id: UInt,
    val RGB: String
)

@Serializable
data class Colors(val Colors: List<Color>) {
    fun toMap(): LinkedHashMap<UInt, String> {
        return Colors.fold(LinkedHashMap()) { acc, color ->
            acc[color.Id] = color.RGB
            acc
        }
    }
}