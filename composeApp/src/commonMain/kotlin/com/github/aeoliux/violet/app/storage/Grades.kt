package com.github.aeoliux.violet.app.storage

import com.github.aeoliux.violet.api.types.Grade
import kotlinx.datetime.LocalDateTime

fun Database.selectGrades(): LinkedHashMap<String, List<Grade>>? {
    try {
        val grades = dbQuery.selectAllGrades().executeAsList()

        return grades.fold(LinkedHashMap()) { acc, grade ->
            val newGrade = Grade(
                grade = grade.grade,
                addDate = LocalDateTime.parse(grade.addDate),
                color = grade.color,
                gradeType = grade.gradeType,
                category = grade.category,
                addedBy = grade.addedBy,
                weight = grade.weight.toUInt(),
                semester = grade.semester.toUInt(),
                comment = grade.comment
            )

            acc[grade.subject] = acc[grade.subject]?.plus(newGrade)?: listOf(newGrade)
            acc
        }
    } catch (_: NullPointerException) {
        return null
    }
}

fun Database.insertGrades(grades: LinkedHashMap<String, List<Grade>>) {
    dbQuery.transaction {
        dbQuery.clearGrades()

        grades.forEach { (subject, grades) ->
            grades.forEach { grade ->
                dbQuery.insertGrade(
                    subject = subject,
                    grade = grade.grade,
                    addDate = grade.addDate.toString(),
                    color = grade.color,
                    gradeType = grade.gradeType,
                    category = grade.category,
                    addedBy = grade.addedBy,
                    weight = grade.weight.toLong(),
                    semester = grade.semester.toLong(),
                    comment = grade.comment
                )
            }
        }
    }
}