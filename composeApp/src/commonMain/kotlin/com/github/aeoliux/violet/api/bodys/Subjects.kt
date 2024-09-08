package com.github.aeoliux.violet.api.bodys

import kotlinx.serialization.Serializable

@Serializable
data class Subject(
    val Id: UInt,
    val Name: String
)

@Serializable
data class Subjects(val Subjects: List<Subject>) {
    fun toMap(): LinkedHashMap<UInt, String> {
        return this.Subjects.fold(LinkedHashMap()) { acc, subject ->
            acc[subject.Id] = subject.Name
            acc
        }
    }
}