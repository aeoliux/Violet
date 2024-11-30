package com.github.aeoliux.violet.app.storage

fun Database.wipeData() {
    dbQuery.transaction {
        dbQuery.clearAgenda()
        dbQuery.clearGrades()
        dbQuery.clearAboutMe()
        dbQuery.clearLessons()
        dbQuery.clearAttendances()
        dbQuery.clearClassInfo()
        dbQuery.clearLuckyNumber()
        dbQuery.clearSchoolNotices()
    }
}