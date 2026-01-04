package com.github.aeoliux.violet.storage

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

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
    @Query("SELECT * FROM Attendance WHERE NOT typeShort = 'ob' ORDER BY date DESC")
    fun getUnattendance(): Flow<List<Attendance>>

    @Query("SELECT SUM(CASE WHEN typeShort = 'ob' THEN 1 ELSE 0 END) * 100.0 / COUNT(*) FROM Attendance")
    fun getAttendancePercentage(): Flow<Double>

    @Query("""
        SELECT
            semester AS semester,
            SUM(CASE WHEN typeShort = 'ob' THEN 1 ELSE 0 END) *
                100.0 / COUNT(*) AS percentage
        FROM Attendance
        GROUP BY semester
        ORDER BY semester
    """)
    fun getSemestralAttendance(): Flow<List<SemestralAttendance>>
}

@Serializable
data class SemestralAttendance(
    val semester: Int,
    val percentage: Double
)
