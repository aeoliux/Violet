package com.github.aeoliux.app.content

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
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