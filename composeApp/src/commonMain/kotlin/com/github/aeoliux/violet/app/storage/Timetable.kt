package com.github.aeoliux.violet.app.storage

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import com.github.aeoliux.violet.api.types.Lesson
import com.github.aeoliux.violet.api.Timetable as TimetableFinal
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime

@Entity(tableName = "Timetable")
data class Timetable(
    @PrimaryKey(autoGenerate = true) val key: Int = 0,
    val id: Int,
    val date: LocalDate,
    val time: LocalTime,

    val lessonNo: Int,
    val subject: String,
    val teacher: String,
    val classroom: String,
    val isCanceled: Boolean,
    val subclassName: String?
)

@Dao
interface TimetableDao {
    @Insert
    suspend fun insertTimetable(timetable: Timetable)

    @Query("DELETE FROM Timetable")
    suspend fun deleteTimetable()

    @Query("SELECT * FROM Timetable")
    suspend fun getTimetable(): List<Timetable>
}

class TimetableRepository(private val database: AppDatabase) {
    suspend fun deleteTimetable() = database.getTimetableDao().deleteTimetable()

    suspend fun insertTimetable(timetable: TimetableFinal) = timetable.forEach { (date, it) ->
        it.forEach { (time, it) ->
            it.forEach {
                database.getTimetableDao().insertTimetable(Timetable(
                    id = it.id,
                    date = date,
                    time = time,

                    lessonNo = it.lessonNo,
                    subject = it.subject,
                    teacher = it.teacher,
                    classroom = it.classroom,
                    isCanceled = it.isCanceled,
                    subclassName = it.subclassName
                ))
            }
        }
    }

    suspend fun getTimetable(): TimetableFinal = database.getTimetableDao()
        .getTimetable()
        .fold(TimetableFinal()) { acc, it ->
            val lesson = Lesson(
                id = it.id,
                lessonNo = it.lessonNo,
                subject = it.subject,
                teacher = it.teacher,
                classroom = it.classroom,
                isCanceled = it.isCanceled,
                subclassName = it.subclassName
            )

            if (acc[it.date] == null)
                acc[it.date] = LinkedHashMap()

            acc[it.date]!![it.time] = acc[it.date]!![it.time]?.plus(lesson)?: listOf(lesson)

            acc
        }
}