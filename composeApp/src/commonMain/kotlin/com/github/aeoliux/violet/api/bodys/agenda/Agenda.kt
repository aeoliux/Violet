package com.github.aeoliux.violet.api.bodys.agenda

import com.github.aeoliux.violet.api.Agenda
import com.github.aeoliux.violet.api.bodys.IdAndUrl
import com.github.aeoliux.violet.api.localDateTimeFormat
import com.github.aeoliux.violet.api.types.AgendaItem
import com.github.aeoliux.violet.api.types.User
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class HomeworkClassroom(
    val Id: UInt,
    val Symbol: String,
    val Name: String,
    val Size: UInt
)

@Serializable
data class Homework(
    val Id: UInt,
    val Content: String,
    val Date: String,
    val Category: IdAndUrl,
    val LessonNo: UInt? = null,
    val TimeFrom: String,
    val TimeTo: String,
    val CreatedBy: IdAndUrl,
    val Classroom: HomeworkClassroom? = null,
    val Subject: IdAndUrl? = null,
    val AddDate: String
)

@Serializable
data class Homeworks(val HomeWorks: List<Homework>) {
    fun toAgenda(
        categories: LinkedHashMap<UInt, Pair<String, String>>,
        users: LinkedHashMap<UInt, User>,
        classrooms: LinkedHashMap<UInt, String>,
        subjects: LinkedHashMap<UInt, String>
    ): Agenda {
        return HomeWorks.fold(Agenda()) { acc, homework ->
            val newHomework = AgendaItem(
                content = homework.Content,
                category = categories[homework.Category.Id]?.first?: "",
                createdBy = users[homework.CreatedBy.Id]?.teacher()?: "",
                addedAt = LocalDateTime.parse(homework.AddDate, localDateTimeFormat),
                color = categories[homework.Category.Id]?.second?: "FFFFFF",
                subject = subjects[homework.Subject?.Id?: 0],
                classroom = classrooms[homework.Classroom?.Id?: 0],
                timeFrom = homework.TimeFrom,
                timeTo = homework.TimeTo
            )

            val date = LocalDate.parse(homework.Date)
            if (acc[date] == null)
                acc[date] = LinkedHashMap()

            acc[date]!![homework.LessonNo?: 0u] =
                acc[date]!![homework.LessonNo?: 0]?.plus(newHomework)?: listOf(newHomework)

            acc
        }
    }
}