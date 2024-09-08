package com.github.aeoliux.violet.storage

import com.github.aeoliux.violet.api.ClassInfo

fun Database.selectClassInfo(): ClassInfo? {
    try {
        val classInfo = dbQuery.selectClassInfo().executeAsOne()

        return ClassInfo(
            number = classInfo.number.toUInt(),
            symbol = classInfo.symbol,
            classTutors = classInfo.classTutors,
            semester = classInfo.semester.toUInt()
        )
    } catch (_: NullPointerException) {
        return null
    }
}

fun Database.setClassInfo(classInfo: ClassInfo) {
    dbQuery.transaction {
        dbQuery.clearClassInfo()
        dbQuery.insertClassInfo(
            number = classInfo.number.toLong(),
            symbol = classInfo.symbol,
            classTutors = classInfo.classTutors,
            semester = classInfo.semester.toLong()
        )
    }
}