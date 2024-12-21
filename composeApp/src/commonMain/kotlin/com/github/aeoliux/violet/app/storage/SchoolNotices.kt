package com.github.aeoliux.violet.app.storage

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import com.github.aeoliux.violet.api.types.SchoolNotice as SchoolNoticeFinal
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

@Entity(tableName = "SchoolNotices")
data class SchoolNotice(
    @PrimaryKey(autoGenerate = true) val key: Int = 0,
    val id: String,

    val startDate: LocalDate,
    val endDate: LocalDate,
    val subject: String,
    val content: String,
    val addedBy: String,
    val createdAt: LocalDateTime
)

@Dao
interface SchoolNoticesDao {
    @Insert
    suspend fun insertSchoolNotice(schoolNotice: SchoolNotice)

    @Query("DELETE FROM SchoolNotices")
    suspend fun deleteSchoolNotices()

    @Query("SELECT * FROM SchoolNotices ORDER BY startDate DESC")
    suspend fun getSchoolNotices(): List<SchoolNotice>
}

class SchoolNoticesRepository(private val database: AppDatabase) {
    suspend fun deleteSchoolNotices() = database.getSchoolNoticesDao().deleteSchoolNotices()

    suspend fun insertSchoolNotices(schoolNotices: List<SchoolNoticeFinal>) = schoolNotices.forEach {
        database.getSchoolNoticesDao().insertSchoolNotice(
            SchoolNotice(
                id = it.id,
                startDate = it.startDate,
                endDate = it.endDate,
                subject = it.subject,
                content = it.content,
                addedBy = it.addedBy,
                createdAt = it.createdAt
            )
        )
    }

    suspend fun getSchoolNotices(): List<SchoolNotice> = database.getSchoolNoticesDao().getSchoolNotices()
}