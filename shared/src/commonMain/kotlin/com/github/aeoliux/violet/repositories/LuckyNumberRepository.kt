package com.github.aeoliux.violet.repositories

import com.github.aeoliux.violet.storage.AppDatabase
import com.github.aeoliux.violet.storage.LuckyNumber
import kotlinx.coroutines.flow.firstOrNull

class LuckyNumberRepository(
    private val appDatabase: AppDatabase,
    private val client: ClientManager
) {
    fun getLuckyNumberFlow() = this.appDatabase.getLuckyNumberDao().getLuckyNumber()

    suspend fun refresh() = this.client.with { client ->
        val new = client.luckyNumber()
        val current = this.appDatabase.getLuckyNumberDao().getLuckyNumber().firstOrNull()

        this.appDatabase.getLuckyNumberDao().apply {
            current?.let { delete(LuckyNumber(luckyNumber = it)) }

            upsert(LuckyNumber(luckyNumber = new.first))
        }
    }
}