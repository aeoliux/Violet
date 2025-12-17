package com.github.aeoliux.violet.repositories

import com.github.aeoliux.violet.storage.AppDatabase
import com.github.aeoliux.violet.storage.Timetable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlin.collections.map
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class TimetableRepository(
    private val appDatabase: AppDatabase,
    private val clientManager: ClientManager
) {
    fun getTimetableFlow() = this.appDatabase
        .getTimetableDao()
        .getTimetable()
        .map { timetable ->
            val dates = timetable.map { it.date }

            dates.associateWithTo(linkedMapOf()) { date ->
                val byDate = timetable.filter { it.date == date }

                this.associateTimetableByTimestamp(byDate)
            }
        }

    @OptIn(ExperimentalTime::class)
    fun getCurrentTimetable(): Flow<Pair<LocalDate, LinkedHashMap<LocalTime, List<Timetable>>>> {
        val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        val weekDay = today.dayOfWeek.isoDayNumber
        val day = if (weekDay > 5) {
            LocalDate.fromEpochDays(today.toEpochDays() + 8 - weekDay)
        } else {
            today
        }

        val nextDay = day.plus(1, DateTimeUnit.DAY)

        return this.appDatabase
            .getTimetableDao()
            .getTimetableByDate(day, nextDay)
            .map { timetable ->
                val mapped = listOf(
                    timetable.filter { it.date == day },
                    timetable.filter { it.date == nextDay }
                ).map {
                    byDate -> this.associateTimetableByTimestamp(byDate)
                }


                mapped[0].entries
                    .lastOrNull()
                    ?.takeIf { (time, firstDay) ->
                        val now = Clock.System.now()
                            .toLocalDateTime(TimeZone.currentSystemDefault())

                        firstDay.isNotEmpty() &&
                                (now.date < day ||
                                        (now.date >= day &&
                                                now.time < firstDay.maxBy { it.timeTo }.timeTo))
                    }
                    ?.let { Pair(day, mapped[0]) }
                    ?: Pair(nextDay, mapped[1])
            }
    }

    internal fun associateTimetableByTimestamp(timetable: List<Timetable>): LinkedHashMap<LocalTime, List<Timetable>> {
        val timestamps = timetable
            .map { it.time }
            .sorted()

        return timestamps
            .associateWithTo(linkedMapOf()) {
                time -> timetable.filter { it.time == time }
            }
    }

    suspend fun refresh() = this.clientManager.with { client ->
        val timetable = client
            .timetable()
            .flatMap { (date, timetable) ->
                timetable.flatMap { (time, timetable) ->
                    timetable.map { entry ->
                        Timetable(
                            id = entry.id,
                            date = date,
                            time = time,
                            timeTo = entry.timeTo,
                            lessonNo = entry.lessonNo,
                            subject = entry.subject,
                            teacher = entry.teacher,
                            classroom = entry.classroom,
                            isCanceled = entry.isCanceled,
                            subclassName = entry.subclassName
                        )
                    }
                }
            }

        this.appDatabase
            .getTimetableDao()
            .upsertMultiple(timetable)
    }
}