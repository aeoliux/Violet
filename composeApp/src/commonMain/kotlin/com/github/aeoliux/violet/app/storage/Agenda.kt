package com.github.aeoliux.violet.app.storage

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import com.github.aeoliux.violet.api.types.AgendaItem
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

@Entity(tableName = "Agenda")
data class Agenda (
    @PrimaryKey(autoGenerate = true) val key: Int = 0,
    val id: Int,
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

class AgendaRepository(private val database: AppDatabase) {
    suspend fun deleteAgenda() = database.getAgendaDao().deleteAgenda()

    suspend fun getAgenda(): com.github.aeoliux.violet.api.Agenda {
        return database.getAgendaDao().getAgenda().fold(com.github.aeoliux.violet.api.Agenda()) { acc, item ->
            val agenda = AgendaItem(
                id = item.id,
                content = item.content,
                category = item.category,
                createdBy = item.createdBy,
                addedAt = item.addedAt,
                color = item.color,
                subject = item.subject,
                classroom = item.classroom,
                timeFrom = item.timeFrom,
                timeTo = item.timeTo
            )

            if (acc[item.date] == null)
                acc[item.date] = LinkedHashMap()

            acc[item.date]!![item.lessonNo] = acc[item.date]!![item.lessonNo]?.plus(agenda)?: listOf(agenda)

            acc
        }
    }

    suspend fun insertAgenda(agenda: com.github.aeoliux.violet.api.Agenda) {
        agenda.forEach { (date, agenda) ->
            agenda.forEach { (lessonNo, agenda) ->
                agenda.forEach { item ->
                    database.getAgendaDao().insertAgenda(Agenda(
                        id = item.id,
                        date = date,
                        lessonNo = lessonNo,

                        content = item.content,
                        category = item.category,
                        createdBy = item.createdBy,
                        addedAt = item.addedAt,
                        color = item.color,
                        subject = item.subject,
                        classroom = item.classroom,
                        timeFrom = item.timeFrom,
                        timeTo = item.timeTo
                    ))
                }
            }
        }
    }
}