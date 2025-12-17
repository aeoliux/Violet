package com.github.aeoliux.violet.storage

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

@Entity(tableName = "Timetable")
data class Timetable(
    @PrimaryKey(autoGenerate = false)
    val id: Int = 0,
    val date: LocalDate,
    val time: LocalTime,

    val timeTo: LocalTime,
    val lessonNo: Int,
    val subject: String,
    val teacher: String,
    val classroom: String,
    val isCanceled: Boolean,
    val subclassName: String?
)

@Dao
interface TimetableDao: BaseDao<Timetable> {
    @Query("SELECT * FROM Timetable ORDER BY date ASC")
    fun getTimetable(): Flow<List<Timetable>>

    @Query("SELECT * FROM Timetable WHERE date = :date OR date = :or ORDER BY date ASC")
    fun getTimetableByDate(date: LocalDate, or: LocalDate): Flow<List<Timetable>>
}