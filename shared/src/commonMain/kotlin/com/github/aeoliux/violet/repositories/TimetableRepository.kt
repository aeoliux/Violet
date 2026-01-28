package com.github.aeoliux.violet.repositories

import com.github.aeoliux.violet.api.ApiClient
import com.github.aeoliux.violet.api.types.Lesson
import com.github.aeoliux.violet.storage.AppDatabase
import com.github.aeoliux.violet.storage.Timetable
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlinx.io.IOException
import kotlinx.serialization.SerializationException
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
    fun getCurrentTimetable(): Flow<Pair<LocalDate, List<Timetable>>?> {
        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

        return this.appDatabase
            .getTimetableDao()
            .getTimetableSince(now.date)
            .map { timetable ->
                val dates = timetable.map { it.date }.distinct()

                val byDate = dates.associateWithTo(linkedMapOf()) { date ->
                    timetable.filter { it.date == date }
                }

                byDate.entries
                    .sortedBy { it.key }
                    .firstOrNull {
                        it.value
                            .maxByOrNull { it.timeTo }
                            ?.let { it.timeTo > now.time || it.date > now.date }
                            ?: false
                    }
                    ?.let {
                        Pair(
                            first = it.key,
                            second = it.value
                                .filter { it.timeTo > now.time || it.date > now.date }
                                .sortedBy { it.lessonNo }
                        )
                    }
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

    @OptIn(ExperimentalTime::class)
    suspend fun refresh() = this.clientManager.with { client ->
        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

        val timetable = this.loopFetchTimetable(
            client = client,
            forDay = now.date,
            now = now
        )
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

    internal suspend fun loopFetchTimetable(
        client: ApiClient,
        forDay: LocalDate,
        now: LocalDateTime,
        tries: Int = 1,
        maxTries: Int = 5
    ): LinkedHashMap<LocalDate, LinkedHashMap<LocalTime, List<Lesson>>> {
        val next = client.timetable(
            this.weekDayForDate(forDay)
        )

        val lengthSinceToday = next
            .filter { it.key >= now.date }
            .entries
            .fold(0) { acc, (_, timetable) ->
                acc + timetable.entries.fold(0) { acc, (_, timetable) -> acc + timetable.size}
            }

        return if (lengthSinceToday > 0 || tries >= maxTries)
            next
        else
            this.loopFetchTimetable(
                client = client,
                forDay = forDay.plus(DatePeriod(days = 7)),
                now = now,
                tries = tries + 1,
                maxTries = maxTries
            )
    }

    @OptIn(ExperimentalTime::class)
    fun weekDayForDate(date: LocalDate): LocalDate {
        val weekDay = date.dayOfWeek.isoDayNumber
        return if (weekDay > 5) {
            LocalDate.fromEpochDays(date.toEpochDays() + 8 - weekDay)
        } else {
            LocalDate.fromEpochDays(date.toEpochDays() - weekDay + 1)
        }
    }
}