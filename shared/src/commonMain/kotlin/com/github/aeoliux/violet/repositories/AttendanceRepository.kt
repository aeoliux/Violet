package com.github.aeoliux.violet.repositories

import com.github.aeoliux.violet.storage.AppDatabase
import com.github.aeoliux.violet.storage.Attendance

class AttendanceRepository(
    private val appDatabase: AppDatabase,
    private val clientManager: ClientManager
) {
    fun getUnattendanceFlow() = this.appDatabase
        .getAttendanceDao()
        .getUnattendance()

    suspend fun refresh() = this.clientManager.with { client ->
        val attendance = client
            .attendance()
            .flatMap { (date, entries) ->
                entries.map { (lessonNo, entry) ->
                    Attendance(
                        id = entry.id,
                        date = date,
                        lessonNo = lessonNo,
                        addedBy = entry.addedBy,
                        addDate = entry.addDate,
                        semester = entry.semester,
                        typeShort = entry.typeShort,
                        type = entry.type,
                        color = entry.color
                    )
                }
            }

        this.appDatabase
            .getAttendanceDao()
            .upsertMultiple(attendance)
    }
}