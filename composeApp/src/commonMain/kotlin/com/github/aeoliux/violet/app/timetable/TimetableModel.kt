package com.github.aeoliux.violet.app.timetable

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.toLocalDateTime

class TimetableModel {
    fun weekDay(): Int {
        var date = Clock.System.now()
            .toLocalDateTime(TimeZone.currentSystemDefault())
            .date

        if (date.dayOfWeek.isoDayNumber > 5)
            date = LocalDate.fromEpochDays(date.toEpochDays() + 8 - date.dayOfWeek.isoDayNumber)

        return date.dayOfWeek.isoDayNumber
    }

    fun getTab(index: Int = -1): Int {
        return when (index) {
            -1 -> {
                weekDay() - 1
            }

            else -> {
                index
            }
        }
    }
}