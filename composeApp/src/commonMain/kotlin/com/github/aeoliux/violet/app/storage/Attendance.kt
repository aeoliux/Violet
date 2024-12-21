package com.github.aeoliux.violet.app.storage

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import com.github.aeoliux.violet.api.types.AttendanceItem
import com.github.aeoliux.violet.api.Attendance as AttendanceMap
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

@Entity(tableName = "Attendance")
data class Attendance(
    @PrimaryKey(autoGenerate = true) val key: Int = 0,
    val id: Int,
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
interface AttendanceDao {
    @Insert
    suspend fun insertAttendance(attendance: Attendance)

    @Query("DELETE FROM Attendance")
    suspend fun deleteAttendance()

    @Query("SELECT * FROM Attendance ORDER BY date DESC")
    suspend fun getAttendance(): List<Attendance>
}

class AttendanceRepository(private val database: AppDatabase) {
    suspend fun deleteAttendance() = database.getAttendanceDao().deleteAttendance()

    suspend fun insertAttendance(attendance: AttendanceMap) {
        attendance.forEach { (date, attendance) ->
            attendance.forEach { (lessonNo, item) ->
                database.getAttendanceDao().insertAttendance(Attendance(
                    id = item.id,
                    date = date,
                    lessonNo = lessonNo,
                    addedBy = item.addedBy,
                    addDate = item.addDate,
                    semester = item.semester,
                    typeShort = item.typeShort,
                    type = item.type,
                    color = item.color
                ))
            }
        }
    }

    suspend fun getAttendance(): AttendanceMap {
        return database.getAttendanceDao().getAttendance().fold(AttendanceMap()) { acc, item ->
            val attendance = AttendanceItem(
                id = item.id,
                addedBy = item.addedBy,
                addDate = item.addDate,
                semester = item.semester,
                typeShort = item.typeShort,
                type = item.type,
                color = item.color
            )

            if (acc[item.date] == null)
                acc[item.date] = LinkedHashMap()

            acc[item.date]!![item.lessonNo] = attendance

            acc
        }
    }
}