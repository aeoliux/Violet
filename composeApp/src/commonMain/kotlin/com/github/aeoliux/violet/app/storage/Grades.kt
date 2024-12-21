package com.github.aeoliux.violet.app.storage

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import com.github.aeoliux.violet.api.types.GradeType
import kotlinx.datetime.LocalDateTime

@Entity(tableName = "Grades")
data class Grade(
    @PrimaryKey(autoGenerate = true) val key: Int = 0,
    val id: Int,
    val subject: String,

    val grade: String,
    val addDate: LocalDateTime,
    val color: String,
    val gradeType: GradeType,
    val category: String,
    val addedBy: String,
    val weight: Int,
    val semester: Int,
    val comment: String?
)

@Dao
interface GradesDao {
    @Insert
    suspend fun insertGrade(grade: Grade)

    @Query("DELETE FROM Grades")
    suspend fun deleteGrades()

    @Query("SELECT * FROM Grades")
    suspend fun getGrades(): List<Grade>
}

class GradesRepository(private val database: AppDatabase) {
    suspend fun deleteGrades() = database.getGradesDao().deleteGrades()

    suspend fun getGrades(): LinkedHashMap<String, List<com.github.aeoliux.violet.api.types.Grade>> {
        return database.getGradesDao().getGrades().fold(LinkedHashMap()) { acc, item ->
            val grade = com.github.aeoliux.violet.api.types.Grade(
                id = item.id,
                grade = item.grade,
                addDate = item.addDate,
                color = item.color,
                gradeType = item.gradeType,
                category = item.category,
                addedBy = item.addedBy,
                weight = item.weight,
                semester = item.semester,
                comment = item.comment
            )

            acc[item.subject] = acc[item.subject]?.plus(grade)?: listOf(grade)

            acc
        }
    }

    suspend fun insertGrades(grades: LinkedHashMap<String, List<com.github.aeoliux.violet.api.types.Grade>>) {
        grades.forEach { (subject, grades) ->
            grades.forEach { item ->
                database.getGradesDao().insertGrade(Grade(
                    id = item.id,
                    subject = subject,

                    grade = item.grade,
                    addDate = item.addDate,
                    color = item.color,
                    gradeType = item.gradeType,
                    category = item.category,
                    addedBy = item.addedBy,
                    weight = item.weight,
                    semester = item.semester,
                    comment = item.comment
                ))
            }
        }
    }
}