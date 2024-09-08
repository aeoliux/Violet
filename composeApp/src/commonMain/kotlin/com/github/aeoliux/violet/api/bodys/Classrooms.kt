package com.github.aeoliux.violet.api.bodys

import kotlinx.serialization.Serializable

@Serializable
data class Classroom(
    val Id: UInt,
    val Name: String,
    val Symbol: String,
    val Description: String = ""
)

@Serializable
data class Classrooms(val Classrooms: List<Classroom>)