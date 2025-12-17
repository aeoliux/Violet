package com.github.aeoliux.violet.app.content

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.format
import kotlinx.datetime.format.DayOfWeekNames
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.char

fun LocalDate.formatWithWeekday(): String = this.format(
    LocalDate.Format {
        dayOfWeek(DayOfWeekNames.ENGLISH_FULL)
        chars(", ")
        day()
        char(' ')
        monthName(MonthNames.ENGLISH_FULL)
    }
)

fun LocalDateTime.prettyFormatted(): String = this.format(
    LocalDateTime.Format {
        day()
        char(' ')
        monthName(MonthNames.ENGLISH_FULL)
        chars(", ")
        hour()
        char(':')
        minute()
    }
)

fun LocalTime.onlyHourAndMinute(): String = this.format(
    LocalTime.Format {
        hour()
        char(':')
        minute()
    }
)

fun LocalDate.minimalFormat(): String = this.format(
    LocalDate.Format {
        day()
        char('.')
        monthNumber()
    }
)