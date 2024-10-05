package com.github.aeoliux.violet.api.bodys.attendance

import com.github.aeoliux.violet.api.Attendance
import com.github.aeoliux.violet.api.bodys.IdAndUrl
import com.github.aeoliux.violet.api.localDateTimeFormat
import com.github.aeoliux.violet.api.types.AttendanceItem
import com.github.aeoliux.violet.api.types.User
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class AttendanceEntry(
    val Id: UInt,
    val Date: String,
    val AddDate: String,
    val LessonNo: UInt,
    val Semester: UInt,
    val Type: IdAndUrl,
    val AddedBy: IdAndUrl
)

@Serializable
data class Attendances(val Attendances: List<AttendanceEntry>) {
    fun toAttendanceMap(
        colors: LinkedHashMap<UInt, String>,
        types: List<AttendanceType>,
        users: LinkedHashMap<UInt, User>
    ): Attendance {
        return Attendances.fold(LinkedHashMap()) { acc, att ->
            val date = LocalDate.parse(att.Date)
            val type = types.firstOrNull { it.Id == att.Type.Id }?: return@fold acc
            if (type.Order == 1u || type.Id == 100u)
                return@fold acc

            val color = type.ColorRGB?: colors[type.Color?.Id]?: return@fold acc
            val teacher = users[att.AddedBy.Id]?: return@fold acc

            val attendance = AttendanceItem(
                addedBy = teacher.teacher(),
                addDate = LocalDateTime.parse(att.AddDate, format = localDateTimeFormat),
                semester = att.Semester,
                typeShort = type.Short,
                type = type.Name,
                color = color
            )

            if (acc[date] == null)
                acc[date] = LinkedHashMap()

            acc[date]!![att.LessonNo] = attendance

            acc
        }
    }
}