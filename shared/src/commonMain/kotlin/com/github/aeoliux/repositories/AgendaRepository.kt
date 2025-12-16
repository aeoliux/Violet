package com.github.aeoliux.repositories

import com.github.aeoliux.storage.Agenda
import com.github.aeoliux.storage.AppDatabase
import kotlinx.coroutines.flow.map

class AgendaRepository(
    private val appDatabase: AppDatabase,
    private val clientManager: ClientManager
) {
    fun getAgendaFlow(
        monthsBack: Int,
        monthsForward: Int
    ) = this.appDatabase
        .getAgendaDao()
        .getAgenda(monthsBack, monthsForward)
        .map { agenda ->
            val dates = agenda.map { it.date }

            dates.associateWithTo(linkedMapOf()) { date ->
                agenda
                    .filter { it.date == date }
                    .sortedBy { it.lessonNo }
            }
        }

    fun getLatestAgendaFlow(amount: Int = 5) = this.appDatabase
        .getAgendaDao()
        .getLatestAgenda(amount)

    suspend fun refresh() = this.clientManager.with { client ->
        val agenda = client.agenda()
            .entries
            .flatMap { (date, entries) ->
                entries.entries
                    .flatMap { (lessonNo, entries) ->
                        entries.map {
                            Agenda(
                                id = it.id,
                                date = date,
                                lessonNo = lessonNo,
                                content = it.content,
                                category = it.category,
                                createdBy = it.createdBy,
                                addedAt = it.addedAt,
                                color = it.color,
                                subject = it.subject,
                                classroom = it.classroom,
                                timeFrom = it.timeFrom,
                                timeTo = it.timeTo
                            )
                        }
                    }
            }

        this.appDatabase
            .getAgendaDao()
            .upsertMultiple(agenda)
    }
}