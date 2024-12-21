package com.github.aeoliux.violet.api.bodys

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class LuckyNumber(
    val LuckyNumber: Int,
    val LuckyNumberDay: String,
)

@Serializable
data class LuckyNumbers(val LuckyNumber: LuckyNumber) {
    fun parse(): Pair<Int, LocalDate> {
        return Pair(LuckyNumber.LuckyNumber, LocalDate.parse(LuckyNumber.LuckyNumberDay))
    }
}