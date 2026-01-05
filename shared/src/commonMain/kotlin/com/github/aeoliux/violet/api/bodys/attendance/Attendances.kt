package com.github.aeoliux.violet.api.bodys.attendance

import com.github.aeoliux.violet.api.Attendance
import com.github.aeoliux.violet.api.bodys.HybridIdDeserializer
import com.github.aeoliux.violet.api.bodys.IdAndUrl
import com.github.aeoliux.violet.api.localDateTimeFormat
import com.github.aeoliux.violet.api.types.AttendanceItem
import com.github.aeoliux.violet.api.types.User
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlin.collections.get

@Serializable
data class AttendanceEntry(
    @Serializable(with = HybridIdDeserializer::class)
    val Id: Int,
    val Date: String,
    val AddDate: String,
    val LessonNo: Int,
    val Semester: Int,
    val Type: IdAndUrl,
    val AddedBy: IdAndUrl
)

@Serializable
data class Attendances(val Attendances: List<AttendanceEntry>) {
    fun toAttendanceMap(
        colors: LinkedHashMap<Int, String>,
        types: List<AttendanceType>,
        users: LinkedHashMap<Int, User>
    ): Attendance {
        return Attendances.fold(LinkedHashMap()) { acc, att ->
            val date = LocalDate.parse(att.Date)
            val type = types.firstOrNull { it.Id == att.Type.Id }?: return@fold acc

            val color = type.ColorRGB?: colors[type.Color?.Id]?: return@fold acc
            val teacher = users[att.AddedBy.Id]?: return@fold acc

            val attendance = AttendanceItem(
                id = att.Id,
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