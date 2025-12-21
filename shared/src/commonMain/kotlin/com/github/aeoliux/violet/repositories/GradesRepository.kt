package com.github.aeoliux.violet.repositories

import com.github.aeoliux.violet.api.types.GradeType
import com.github.aeoliux.violet.storage.AppDatabase
import com.github.aeoliux.violet.storage.Grade
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

class GradesRepository(
    private val appDatabase: AppDatabase,
    private val clientManager: ClientManager
) {
    fun getGeneralAveragesFlow() = this.appDatabase.getGradesDao().let { dao ->
        combine(
            dao.countAverage(),
            dao.countAverage(GradeType.FinalProposition),
            dao.countSemestralAverage(1),
            dao.countSemestralAverage(1, GradeType.SemesterProposition),
            dao.countSemestralAverage(2),
            dao.countSemestralAverage(2, GradeType.SemesterProposition)
        ) { it }
    }

    fun getAveragesForSubject(subject: String) = this.appDatabase
        .getGradesDao()
        .getAveragesForSubject(subject)

    fun getAveragesBySubjectAndSemester() = this.appDatabase
        .getGradesDao()
        .getAveragesBySubjectAndSemester()
        .map { averages ->
            val subjects = averages.map { it.subject }

            subjects.associateWithTo(linkedMapOf()) { subject ->
                val bySubject = averages.filter { it.subject == subject }

                bySubject
                    .sortedBy { it.semester }
                    .map { it.average }
            }
        }

    fun getSubjectsListFlow() = this.appDatabase
        .getGradesDao()
        .getSubjects()

    fun getGradesFlow(subject: String) = this.appDatabase
        .getGradesDao()
        .getGradesForSubject(subject)
        .map { grades ->
            listOf(
                grades.filter { it.semester == 1 },
                grades.filter { it.semester == 2 }
            )
        }

    fun getLatestGrades(amount: Int = 5) = this.appDatabase
        .getGradesDao()
        .getLatestGrades(amount)

    suspend fun refresh() = this.clientManager.with { client ->
        val newGrades = client.grades()
        val newGradesMapped = newGrades.flatMap { (subject, grades) ->
            grades.map {
                Grade(
                    id = it.id,
                    subject = subject,
                    grade = it.grade,
                    gradeValue = this.gradeValue(it.grade),
                    addDate = it.addDate,
                    color = it.color,
                    gradeType = it.gradeType,
                    category = it.category,
                    addedBy = it.addedBy,
                    weight = it.weight,
                    semester = it.semester,
                    comment = it.comment,
                )
            }
        }

        this.appDatabase.getGradesDao().upsertMultiple(newGradesMapped)
    }

    internal fun gradeValue(grade: String): Double = grade
        .toCharArray()
        .takeIf { it.size <= 2 }
        ?.let {
            it
                .takeIf { it.size == 2 } ?: it.plus(' ')
        }
        ?.let { Pair(it[0].digitToIntOrNull() ?: return@let null, it[1]) }
        ?.let { Pair(it.first, when (it.second) {
            '+' -> 0.5
            '-' -> -0.25
            else -> 0.0
        })}
        ?.let { it.first + it.second }
        ?: 0.0
}

fun String.trimToTheLimit(limit: Int = 4): String = this
    .takeIf { it.length > limit }
    ?.substring(0, limit)
    ?: this

fun String.fill(with: String = "0", to: Int = 4): String = this
    .takeIf { it.length < to }
    ?.plus(
        (0..<(to - this.length))
            .joinToString { with }
    )
    ?: this