package com.github.aeoliux.violet.app.storage

import kotlinx.datetime.LocalDate

fun Database.selectLuckyNumber(): Pair<UInt, LocalDate>? {
    try {
        val resp = dbQuery.selectLuckyNumber().executeAsOne()

        return Pair(resp.number.toUInt(), LocalDate.parse(resp.date))
    } catch (_: NullPointerException) {
        return null
    }
}

fun Database.setLuckyNumber(luckyNumber: Pair<UInt, LocalDate>) {
    dbQuery.transaction {
        dbQuery.clearLuckyNumber()
        dbQuery.insertLuckyNumber(luckyNumber.first.toLong(), luckyNumber.second.toString())
    }
}