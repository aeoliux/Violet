package com.github.aeoliux.violet.api.types

import com.github.aeoliux.violet.api.Attendance
import kotlinx.datetime.LocalDate

fun Attendance.minOrMax(accStartsWith: UInt, comparator: (acc: UInt, new: UInt) -> Boolean): UInt {
    return this.keys.fold(accStartsWith) { acc, key ->
        val attendances = this[key]!!

        val buf = attendances.keys.fold(0u) { acc, key ->
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

fun Attendance.min(): UInt {
    return this.minOrMax(UInt.MAX_VALUE) { acc, new -> acc > new }
}

fun Attendance.max(): UInt {
    return this.minOrMax(UInt.MIN_VALUE) { acc, new -> new > acc }
}