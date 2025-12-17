package com.github.aeoliux.violet.storage

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "ClassInfo")
data class ClassInfo(
    @PrimaryKey val key: Int = 0,

    val number: Int,
    val symbol: String,
    val classTutors: List<String>,
    val semester: Int
)

@Dao
interface ClassInfoDao: BaseDao<ClassInfo> {
    @Query("SELECT * FROM ClassInfo LIMIT 1")
    fun getClassInfo(): Flow<ClassInfo>
}