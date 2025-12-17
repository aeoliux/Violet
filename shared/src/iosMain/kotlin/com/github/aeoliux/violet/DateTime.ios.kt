package com.github.aeoliux.violet

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toInstant
import kotlinx.datetime.toNSDate
import platform.Foundation.NSDate
import platform.Foundation.dateByAddingTimeInterval
import kotlin.time.ExperimentalTime

fun LocalTime.toNSDate(since: NSDate): NSDate {
    val epoch = this.toMillisecondOfDay().toDouble() / 1000
    return since.dateByAddingTimeInterval(epoch)
}

@OptIn(ExperimentalTime::class)
fun LocalDateTime.toNSDate() = this.toInstant(TimeZone.currentSystemDefault()).toNSDate()

@OptIn(ExperimentalTime::class)
fun LocalDate.toNSDate() = this.atStartOfDayIn(TimeZone.currentSystemDefault()).toNSDate()