package com.github.aeoliux.violet.storage

import com.github.aeoliux.violet.api.types.Me

fun Database.selectAboutMe(): Me? {
    try {
        val me = dbQuery.selectAboutMe().executeAsOne()

        return Me(
            id = me.id.toUInt(),
            firstName = me.firstName,
            lastName = me.lastName,
            email = me.email,
            login = me.login
        )
    } catch (_: NullPointerException) {
        return null
    }
}

fun Database.setAboutMe(me: Me) {
    dbQuery.transaction {
        dbQuery.clearAboutMe()

        dbQuery.insertAboutMe(
            id = me.id.toLong(),
            firstName = me.firstName,
            lastName = me.lastName,
            email = me.email,
            login = me.login
        )
    }
}