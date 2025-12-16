package com.github.aeoliux.storage

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.DateTimePeriod
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

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
@OptIn(ExperimentalTime::class)
interface AgendaDao: BaseDao<Agenda> {
    @Query("SELECT * FROM Agenda WHERE date >= :after ORDER BY date ASC LIMIT :amount")
    fun getLatestAgenda(
        amount: Int,
        after: LocalDate = Clock.System.now()
            .toLocalDateTime(TimeZone.currentSystemDefault())
            .date
    ): Flow<List<Agenda>>

    @Query("SELECT * FROM Agenda WHERE date BETWEEN :after AND :until ORDER BY date ASC")
    fun getAgendaByDate(after: LocalDate, until: LocalDate): Flow<List<Agenda>>

    fun getAgenda(
        monthsBack: Int,
        monthsForward: Int
    ) = this.getAgendaByDate(
        after = Clock.System.now()
            .toLocalDateTime(TimeZone.currentSystemDefault())
            .date
            .minus(DatePeriod(months = monthsBack)),
        until = Clock.System.now()
            .toLocalDateTime(TimeZone.currentSystemDefault())
            .date
            .plus(DatePeriod(months = monthsForward))
    )
}
