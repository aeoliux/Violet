package com.github.aeoliux.violet.api.bodys

import com.github.aeoliux.violet.api.types.Lesson
import com.github.aeoliux.violet.api.Timetable
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.serialization.Serializable

@Serializable
data class TimetableItemSubject(
    val Name: String,
    val Short: String
)

@Serializable
data class TimetableItemTeacher(
    val FirstName: String,
    val LastName: String,
)

@Serializable
data class TimetableItem(
    val Lesson: IdAsStringAndUrl,
    val Classroom: IdAsStringAndUrl? = null,
    val DateFrom: String,
    val DateTo: String,
    val DayNo: String,
    val Subject: TimetableItemSubject,
    val Teacher: TimetableItemTeacher,
    val IsCanceled: Boolean,
    val HourFrom: String,
    val HourTo: String,
    val VirtualClassName: String? = null
)

@Serializable
data class Timetables(
    val Timetable: Map<String, List<List<TimetableItem>>>
) {
    fun toTimetableMap(classrooms: LinkedHashMap<Int, String>): Timetable {
        val map = Timetable()

        Timetable.keys.forEach { date ->
            val timetable = Timetable[date] ?: return@forEach
            val dateParsed = LocalDate.parse(date)

            map[dateParsed] = timetable.foldIndexed(LinkedHashMap()) { index, acc, lessons ->
                lessons.forEach forEach2@{
                    val time = LocalTime.parse(
                        if (it.HourFrom.length == 5)
                            it.HourFrom.plus(":00")
                        else
                            it.HourFrom
                    )

                    val lesson = Lesson(
                        id = it.Lesson.Id.toInt(),
                        lessonNo = index.toInt(),
                        subject = it.Subject.Name,
                        teacher = "${it.Teacher.FirstName} ${it.Teacher.LastName}",
                        classroom = classrooms[it.Classroom?.Id?.toInt()]?: "unknown classroom",
                        isCanceled = it.IsCanceled,
                        subclassName = it.VirtualClassName
                    )

                    acc[time] = acc[time]?.plus(lesson) ?: listOf(lesson)
                }

                acc
            }
        }

        return map
    }
}