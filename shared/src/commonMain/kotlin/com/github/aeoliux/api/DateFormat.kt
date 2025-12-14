package com.github.aeoliux.api

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format.char

val localDateTimeFormat = LocalDateTime.Format {
    year()
    char('-')
    monthNumber()
    char('-')
    dayOfMonth()
    char(' ')
    hour()
    char(':')
    minute()
    char(':')
    second()
}