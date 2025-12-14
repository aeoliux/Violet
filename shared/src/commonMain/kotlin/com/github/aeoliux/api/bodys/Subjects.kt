package com.github.aeoliux.api.bodys

import kotlinx.serialization.Serializable

@Serializable
data class Subject(
    val Id: Int,
    val Name: String
)

@Serializable
data class Subjects(val Subjects: List<Subject>) {
    fun toMap(): LinkedHashMap<Int, String> {
        return this.Subjects.fold(LinkedHashMap()) { acc, subject ->
            acc[subject.Id] = subject.Name
            acc
        }
    }
}