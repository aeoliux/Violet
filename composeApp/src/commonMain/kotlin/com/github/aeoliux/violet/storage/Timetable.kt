package com.github.aeoliux.violet.storage

import com.github.aeoliux.violet.api.types.Lesson
import com.github.aeoliux.violet.api.Timetable
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

fun Database.selectLessons(): Timetable? {
    try {
        val lessons = dbQuery.selectLessons().executeAsList()

        return lessons.fold(Timetable()) { acc, lesson ->
            val newLesson = Lesson(
                lessonNo = lesson.lessonNo.toUInt(),
                subject = lesson.subject,
                teacher = lesson.teacher,
                classroom = lesson.classroom,
                isCanceled = lesson.isCanceled,
                subclassName = lesson.subclassName
            )

            val date = LocalDate.parse(lesson.date)
            val time = LocalTime.parse(lesson.time)

            if (acc[date] == null)
                acc[date] = LinkedHashMap()

            acc[date]!![time] = acc[date]!![time]?.plus(newLesson) ?: listOf(newLesson)

            acc
        }
    } catch (_: NullPointerException) {
        return null
    }
}

fun Database.insertLessons(lessons: Timetable) {
    dbQuery.transaction {
        dbQuery.clearLessons()

        lessons.forEach { (date, lessons) ->
            lessons.forEach { (time, lessons) ->
                lessons.forEach { lesson ->
                    dbQuery.insertLesson(
                        lessonNo = lesson.lessonNo.toLong(),
                        date = date.toString(),
                        time = time.toString(),
                        subject = lesson.subject,
                        teacher = lesson.teacher,
                        classroom = lesson.classroom,
                        isCanceled = lesson.isCanceled,
                        subclassName = lesson.subclassName
                    )
                }
            }
        }
    }
}