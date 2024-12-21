package com.github.aeoliux.violet.api.types

import com.github.aeoliux.violet.api.Attendance
import kotlinx.datetime.LocalDate

fun Attendance.minOrMax(accStartsWith: Int, comparator: (acc: Int, new: Int) -> Boolean): Int {
    return this.keys.fold(accStartsWith) { acc, key ->
        val attendances = this[key]!!

        val buf = attendances.keys.fold(0) { acc, key ->
            if (comparator(acc, key))
                key
            else
                acc
        }

        if (comparator(acc, buf))
            buf
        else
            acc
    }
}

fun Attendance.min(): Int {
    return this.minOrMax(Int.MAX_VALUE) { acc, new -> acc > new }
}

fun Attendance.max(): Int {
    return this.minOrMax(Int.MIN_VALUE) { acc, new -> new > acc }
}