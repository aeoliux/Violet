package com.github.aeoliux.violet.storage

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "LuckyNumber")
data class LuckyNumber(
    @PrimaryKey var key: Int = 0,
    var luckyNumber: Int
)

@Dao
interface LuckyNumberDao: BaseDao<LuckyNumber> {
    @Query("SELECT luckyNumber FROM LuckyNumber LIMIT 1")
    fun getLuckyNumber(): Flow<Int>
}