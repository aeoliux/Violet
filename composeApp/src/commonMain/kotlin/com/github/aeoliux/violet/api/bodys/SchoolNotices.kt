package com.github.aeoliux.violet.api.bodys

import com.github.aeoliux.violet.api.localDateTimeFormat
import com.github.aeoliux.violet.api.types.User
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.serialization.Serializable

@Serializable
data class SchoolNotice(
    val Id: String,
    val StartDate: String,
    val EndDate: String,
    val Subject: String,
    val Content: String,
    val AddedBy: IdAndUrl,
    val CreationDate: String,
    val WasRead: Boolean
)

@Serializable
data class SchoolNotices(val SchoolNotices: List<SchoolNotice>) {
    fun toSNList(
        users: LinkedHashMap<Int, User>
    ): List<com.github.aeoliux.violet.api.types.SchoolNotice> {
        return SchoolNotices.fold<SchoolNotice, List<com.github.aeoliux.violet.api.types.SchoolNotice>>(emptyList()) { acc, cur ->
            acc.plus(
                com.github.aeoliux.violet.api.types.SchoolNotice(
                    id = cur.Id,
                    startDate = LocalDate.parse(cur.StartDate),
                    endDate = LocalDate.parse(cur.EndDate),
                    subject = cur.Subject,
                    content = cur.Content,
                    addedBy = users[cur.AddedBy.Id]?.teacher()?: "",
                    createdAt = LocalDateTime.parse(cur.CreationDate, localDateTimeFormat)
                )
            )
        }.sortedWith { a, b ->
            (b.createdAt.toInstant(TimeZone.currentSystemDefault()).epochSeconds - a.createdAt.toInstant(
                TimeZone.currentSystemDefault()
            ).epochSeconds).toInt()
        }
    }
}