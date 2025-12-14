package com.github.aeoliux.api.bodys

import com.github.aeoliux.api.localDateTimeFormat
import com.github.aeoliux.api.types.User
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.serialization.Serializable
import kotlin.time.ExperimentalTime

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
    @OptIn(ExperimentalTime::class)
    fun toSNList(
        users: LinkedHashMap<Int, User>
    ): List<com.github.aeoliux.api.types.SchoolNotice> {
        return SchoolNotices.map {
            com.github.aeoliux.api.types.SchoolNotice(
                id = it.Id,
                startDate = LocalDate.parse(it.StartDate),
                endDate = LocalDate.parse(it.EndDate),
                subject = it.Subject,
                content = it.Content,
                addedBy = users[it.AddedBy.Id]?.teacher() ?: "",
                createdAt = LocalDateTime.parse(it.CreationDate, localDateTimeFormat)
            )
        }.sortedWith { a, b ->
            (b.createdAt.toInstant(TimeZone.currentSystemDefault()).epochSeconds
                    - a.createdAt.toInstant(TimeZone.currentSystemDefault()).epochSeconds).toInt()
        }
    }
}