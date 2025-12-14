package com.github.aeoliux.storage

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

@Entity(tableName = "Agenda")
data class Agenda (
    @PrimaryKey(autoGenerate = false)
    val id: Int = 0,
    val date: LocalDate,
    val lessonNo: Int,

    val content: String,
    val category: String,
    val createdBy: String,
    val addedAt: LocalDateTime,
    val color: String,
    val subject: String?,
    val classroom: String?,
    val timeFrom: String,
    val timeTo: String
)

@Dao
interface AgendaDao {
    @Insert
    suspend fun insertAgenda(agenda: Agenda)

    @Query("DELETE FROM Agenda")
    suspend fun deleteAgenda()

    @Query("SELECT * FROM Agenda ORDER BY date ASC")
    suspend fun getAgenda(): List<Agenda>
}
