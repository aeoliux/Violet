package com.github.aeoliux.violet.app.storage

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Upsert

@Entity(tableName = "LuckyNumber")
data class LuckyNumber(
    @PrimaryKey var key: Int = 0,
    var luckyNumber: Int
)

@Dao
interface LuckyNumberDao {
    @Upsert
    suspend fun insertLuckyNumber(luckyNumber: LuckyNumber)

    @Query("DELETE FROM LuckyNumber")
    suspend fun deleteLuckyNumber()

    @Query("SELECT luckyNumber FROM LuckyNumber LIMIT 1")
    suspend fun getLuckyNumber(): Int
}

class LuckyNumberRepository(private val database: AppDatabase) {
    suspend fun insertLuckyNumber(luckyNumber: Int) = database.getLuckyNumberDao()
        .insertLuckyNumber(
            LuckyNumber(luckyNumber = luckyNumber)
        )

    suspend fun getLuckyNumber() = database.getLuckyNumberDao().getLuckyNumber()
    suspend fun deleteLuckyNumber() = database.getLuckyNumberDao().deleteLuckyNumber()
}