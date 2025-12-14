package com.github.aeoliux.api.bodys

import kotlinx.serialization.Serializable

@Serializable
data class Classroom(
    val Id: Int,
    val Name: String,
    val Symbol: String,
    val Description: String = ""
)

@Serializable
data class Classrooms(val Classrooms: List<Classroom>) {
    fun toMap(): LinkedHashMap<Int, String> {
        return Classrooms.fold(LinkedHashMap()) { acc, classroom ->
            acc[classroom.Id] = classroom.Name
            acc
        }
    }
}