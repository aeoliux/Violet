package com.github.aeoliux.violet.app.appState

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

fun LocalDate.formatDate(): String {
    return "${this.dayOfMonth} ${this.month.name}"
}

fun LocalDate.formatWeek(): String {
    return "${this.dayOfWeek.name}, " + this.formatDate()
}

fun LocalDateTime.formatDateTime(): String {
    return "${this.date.formatDate()} ${this.time}"
}