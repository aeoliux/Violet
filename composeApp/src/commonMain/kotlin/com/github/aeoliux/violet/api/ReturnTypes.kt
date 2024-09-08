package com.github.aeoliux.violet.api

import kotlinx.datetime.LocalDateTime

data class Me(
    val id: UInt,
    val firstName: String,
    val lastName: String,
    val email: String,
    val login: String,
)

data class User(
    val firstName: String,
    val lastName: String,
    val isSchoolAdministrator: Boolean,
    val isEmployee: Boolean
)

enum class GradeType {
    Constituent,
    Semester,
    SemesterProposition,
    Final,
    FinalProposition,
}

data class Grade(
    val grade: String,
    val addDate: LocalDateTime,
    val color: String,
    val gradeType: GradeType,
    val category: String,
    val addedBy: String,
    val weight: UInt,
    val semester: UInt,
    val comment: String?,
)

data class ClassInfo(
    val number: UInt,
    val symbol: String,
    val classTutors: List<String>,
    val semester: UInt
)