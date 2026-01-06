package com.github.aeoliux.violet.repositories

import com.github.aeoliux.violet.storage.AppDatabase
import com.github.aeoliux.violet.storage.SchoolNotice
import com.github.aeoliux.violet.utils.crc32

class SchoolNoticesRepository(
    private val appDatabase: AppDatabase,
    private val clientManager: ClientManager
) {
    fun getSchoolNoticesFlow() = this.appDatabase
        .getSchoolNoticesDao()
        .getSchoolNotices()

    suspend fun refresh() = this.clientManager.with { client ->
        val notices = client.schoolNotices()
            .map {
                SchoolNotice(
                    id = crc32(it.id.encodeToByteArray()).toInt(),
                    startDate = it.startDate,
                    endDate = it.endDate,
                    subject = it.subject,
                    content = it.content,
                    addedBy = it.addedBy,
                    createdAt = it.createdAt
                )
            }

        this.appDatabase
            .getSchoolNoticesDao()
            .upsertMultiple(notices)
    }
}
