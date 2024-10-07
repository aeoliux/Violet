package com.github.aeoliux.violet.app.storage

import com.github.aeoliux.violet.api.Agenda
import com.github.aeoliux.violet.api.types.AgendaItem
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

fun Database.selectAgenda(): Agenda? {
    try {
        val agendas = dbQuery.selectAgenda().executeAsList()

        return agendas.fold(LinkedHashMap()) { acc, agenda ->
            val newAgenda = AgendaItem(
                content = agenda.content,
                category = agenda.category,
                createdBy = agenda.createdBy,
                addedAt = LocalDateTime.parse(agenda.addedAt),
                color = agenda.color,
                subject = agenda.subject,
                classroom = agenda.classroom,
                timeFrom = agenda.timeFrom,
                timeTo = agenda.timeTo
            )

            val date = LocalDate.parse(agenda.date)
            if (acc[date] == null)
                acc[date] = LinkedHashMap()

            acc[date]!![agenda.lessonNo.toUInt()] =
                acc[date]!![agenda.lessonNo.toUInt()]?.plus(newAgenda)?: listOf(newAgenda)

            acc
        }
    } catch (e: NullPointerException) {
        return null
    }
}

fun Database.insertAgenda(agendas: Agenda) {
    dbQuery.transaction {
        dbQuery.clearAgenda()

        agendas.forEach { (date, agendas) ->
            agendas.forEach { (lessonNo, agendas) ->
                agendas.forEach { agenda ->
                    dbQuery.insertAgenda(
                        date = date.toString(),
                        lessonNo = lessonNo.toLong(),

                        content = agenda.content,
                        category = agenda.category,
                        createdBy = agenda.createdBy,
                        addedAt = agenda.addedAt.toString(),
                        color = agenda.color,
                        subject = agenda.subject,
                        classroom = agenda.classroom,
                        timeFrom = agenda.timeFrom,
                        timeTo = agenda.timeTo
                    )
                }
            }
        }
    }
}