package com.github.aeoliux.violet.storage

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Query
import com.github.aeoliux.violet.api.types.GradeType
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDateTime

@Entity(tableName = "Grades")
data class Grade(
    @PrimaryKey(autoGenerate = false)
    val id: Int = 0,
    val subject: String,

    val grade: String,
    val gradeValue: Double,
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
interface GradesDao: BaseDao<Grade> {
    @Query("SELECT * FROM Grades ORDER BY addDate DESC LIMIT :amount")
    fun getLatestGrades(amount: Int): Flow<List<Grade>>

    @Query("SELECT AVG(gradeValue) AS avg FROM Grades WHERE gradeType = :gradeType")
    fun countAverage(gradeType: GradeType = GradeType.Final): Flow<Double>

    @Query("SELECT AVG(gradeValue) AS avg FROM Grades WHERE gradeType = :gradeType AND semester = :semester")
    fun countSemestralAverage(semester: Int, gradeType: GradeType = GradeType.Semester): Flow<Double>

    @Query("SELECT DISTINCT subject FROM Grades")
    fun getSubjects(): Flow<List<String>>

    @Query("SELECT * FROM Grades WHERE subject = :subject")
    fun getGradesForSubject(subject: String): Flow<List<Grade>>

    @Query("""
        SELECT subject, semester, SUM(gradeValue * weight) / SUM(weight) AS average
        FROM Grades
        GROUP BY subject, semester
        HAVING gradeType = :gradeType
    """)
    fun getAveragesBySubjectAndSemester(gradeType: GradeType = GradeType.Constituent): Flow<List<AverageBySubject>>

    @Query("""
        SELECT semester, SUM(gradeValue * weight) / SUM(weight) AS average
        FROM Grades
        WHERE subject = :subject
        GROUP BY semester
        HAVING gradeType = :gradeType
    """)
    fun getAveragesForSubject(subject: String, gradeType: GradeType = GradeType.Constituent): Flow<List<AverageForSemester>>
}

data class AverageForSemester(
    val semester: Int,
    val average: Double
)

data class AverageBySubject(
    val subject: String,
    val semester: Int,
    val average: Double
)