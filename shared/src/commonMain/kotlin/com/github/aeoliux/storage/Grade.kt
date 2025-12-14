package com.github.aeoliux.storage

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Query
import com.github.aeoliux.api.types.GradeType
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDateTime

@Entity(tableName = "Grades")
data class Grade(
    @PrimaryKey(autoGenerate = false)
    val id: Int = 0,
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
interface GradesDao: BaseDao<Grade> {
    @Query("SELECT * FROM Grades")
    fun getGrades(): Flow<List<Grade>>

    @Query("SELECT * FROM Grades ORDER BY addDate DESC LIMIT :amount")
    fun getLatestGrades(amount: Int): Flow<List<Grade>>
}