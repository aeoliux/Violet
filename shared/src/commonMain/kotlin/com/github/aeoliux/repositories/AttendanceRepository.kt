package com.github.aeoliux.repositories

import com.github.aeoliux.storage.AppDatabase
import com.github.aeoliux.storage.Attendance

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