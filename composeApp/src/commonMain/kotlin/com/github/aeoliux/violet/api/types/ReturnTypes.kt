package com.github.aeoliux.violet.api.types

import kotlinx.datetime.LocalDate
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
) {
    fun teacher(): String {
        return "$firstName $lastName"
    }
}

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

data class Lesson(
    val lessonNo: UInt,
    val subject: String,
    val teacher: String,
    val classroom: String,
    val isCanceled: Boolean,
    val subclassName: String?
)

data class AttendanceItem(
    val addedBy: String,
    val addDate: LocalDateTime,
    val semester: UInt,
    val typeShort: String,
    val type: String,
    val color: String,
)

data class AgendaItem(
    val content: String,
    val category: String,
    val createdBy: String,
    val addedAt: LocalDateTime,
    val color: String,
    val subject: String?,
    val classroom: String?,
    val timeFrom: String,
    val timeTo: String,
)

data class SchoolNotice(
    val startDate: LocalDate,
    val endDate: LocalDate,
    val subject: String,
    val content: String,
    val addedBy: String,
    val createdAt: LocalDateTime
)