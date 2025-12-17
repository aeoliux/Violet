package com.github.aeoliux.violet.storage

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
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
interface SchoolNoticesDao: BaseDao<SchoolNotice> {
    @Query("SELECT * FROM SchoolNotices ORDER BY startDate DESC")
    fun getSchoolNotices(): Flow<List<SchoolNotice>>
}