package com.github.aeoliux.violet.storage

import com.github.aeoliux.violet.api.Attendance
import com.github.aeoliux.violet.api.types.AttendanceItem
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

fun Database.selectAttendances(): Attendance? {
    return try {
        dbQuery.selectAttendances().executeAsList().fold(LinkedHashMap()) { acc, attendance ->
            val date = LocalDate.parse(attendance.date)
            val lessonNo = attendance.lessonNo.toUInt()

            if (acc[date] == null)
                acc[date] = LinkedHashMap()

            acc[date]!![lessonNo] = AttendanceItem(
                addedBy = attendance.addedBy,
                addDate = LocalDateTime.parse(attendance.addDate),
                semester = attendance.semester.toUInt(),
                typeShort = attendance.typeShort,
                type = attendance.type,
                color = attendance.color
            )

            acc
        }
    } catch (_: NullPointerException) {
        null
    }
}

fun Database.insertAttendances(attendances: Attendance) {
    dbQuery.transaction {
        dbQuery.clearAttendances()

        attendances.forEach { (date, attendances) ->
            attendances.forEach { (lessonNo, attendance) ->
                dbQuery.insertAttendance(
                    date = date.toString(),
                    lessonNo = lessonNo.toLong(),

                    addedBy = attendance.addedBy,
                    addDate = attendance.addDate.toString(),
                    semester = attendance.semester.toLong(),
                    typeShort = attendance.typeShort,
                    type = attendance.type,
                    color = attendance.color
                )
            }
        }
    }
}