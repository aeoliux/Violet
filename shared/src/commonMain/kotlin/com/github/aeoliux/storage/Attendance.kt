package com.github.aeoliux.storage

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

@Entity(tableName = "Attendance")
data class Attendance(
    @PrimaryKey(autoGenerate = false)
    val id: Int = 0,
    val date: LocalDate,
    val lessonNo: Int,

    val addedBy: String,
    val addDate: LocalDateTime,
    val semester: Int,
    val typeShort: String,
    val type: String,
    val color: String,
)

@Dao
interface AttendanceDao: BaseDao<Attendance> {
    @Query("SELECT * FROM Attendance ORDER BY date DESC")
    fun getAttendance(): Flow<Attendance>
}