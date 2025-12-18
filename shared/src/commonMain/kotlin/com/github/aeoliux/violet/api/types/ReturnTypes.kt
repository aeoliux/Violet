package com.github.aeoliux.violet.api.types

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime

data class Me(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val email: String,
    val login: String,
)

data class User(
    val id: Int,
    val senderId: Int,
    val firstName: String,
    val lastName: String,
    val isSchoolAdministrator: Boolean,
    val isEmployee: Boolean,
    val group: Int
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
    FinalProposition;

    companion object {
        fun fromInt(index: Int): GradeType = entries[index]
    }

    fun toInt(): Int = entries.indexOfFirst { this == it }
}

data class Grade(
    val id: Int,
    val grade: String,
    val addDate: LocalDateTime,
    val color: String,
    val gradeType: GradeType,
    val category: String,
    val addedBy: String,
    val weight: Int,
    val semester: Int,
    val comment: String?,
) {
    fun value(): Float {
        return (this.grade.getOrNull(0)?.digitToIntOrNull()?.toFloat() ?: 0.0f) +
                when (this.grade.getOrNull(1)) {
                    '+' -> 0.5f
                    '-' -> -0.25f
                    null -> 0.0f
                    else -> 0.0f
                }
    }
}

data class ClassInfo(
    val number: Int,
    val symbol: String,
    val classTutors: List<String>,
    val semester: Int
)

data class Lesson(
    val id: Int,
    val lessonNo: Int,
    val timeTo: LocalTime,
    val subject: String,
    val teacher: String,
    val classroom: String,
    val isCanceled: Boolean,
    val subclassName: String?
)

data class AttendanceItem(
    val id: Int,
    val addedBy: String,
    val addDate: LocalDateTime,
    val semester: Int,
    val typeShort: String,
    val type: String,
    val color: String,
)

data class AgendaItem(
    val id: Int,
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
    val id: String,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val subject: String,
    val content: String,
    val addedBy: String,
    val createdAt: LocalDateTime
)