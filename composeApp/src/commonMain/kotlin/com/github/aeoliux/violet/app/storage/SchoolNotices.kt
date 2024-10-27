package com.github.aeoliux.violet.app.storage

import com.github.aeoliux.violet.api.types.SchoolNotice
import comgithubaeoliuxvioletstorage.SelectListOfSchoolNotices
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime


fun Database.selectListOfSchoolNotices(): List<SelectListOfSchoolNotices>? {
    try {
        val result = dbQuery.selectListOfSchoolNotices().executeAsList()
        return result
    } catch (_: NullPointerException) {
        return null
    }
}

fun Database.selectSchoolNotice(id: Long): SchoolNotice? {
    val result = dbQuery.selectSchoolNotice(id).executeAsOneOrNull()

    return if (result != null)
        SchoolNotice(
            startDate = LocalDate.parse(result.startDate),
            endDate = LocalDate.parse(result.endDate),
            subject = result.subject,
            content = result.content,
            addedBy = result.addedBy,
            createdAt = LocalDateTime.parse(result.createdAt)
        )
    else
        null
}

fun Database.insertSchoolNotices(sn: List<SchoolNotice>) {
    dbQuery.transaction {
        dbQuery.clearSchoolNotices()

        sn.forEach { sn ->
            dbQuery.insertSchoolNotices(
                startDate = sn.startDate.toString(),
                endDate = sn.endDate.toString(),
                subject = sn.subject,
                content = sn.content,
                addedBy = sn.addedBy,
                createdAt = sn.createdAt.toString()
            )
        }
    }
}