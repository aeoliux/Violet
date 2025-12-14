package com.github.aeoliux.repositories

import com.github.aeoliux.storage.AppDatabase
import com.github.aeoliux.storage.Grade
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GradesRepository(
    private val appDatabase: AppDatabase,
    private val clientManager: ClientManager
) {
    fun getGradesFlow(): Flow<LinkedHashMap<String, List<Grade>>> = this.appDatabase
        .getGradesDao()
        .getGrades()
        .map { grades ->
            val subjects = grades.map { it.subject }.distinct()

            subjects.associateWithTo(linkedMapOf()) { subject ->
                grades.filter { it.subject == subject }
            }
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
}