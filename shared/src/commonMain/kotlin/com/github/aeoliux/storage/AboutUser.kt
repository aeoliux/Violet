package com.github.aeoliux.storage

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@Entity(tableName = "AboutMe")
data class AboutMe(
    @PrimaryKey(autoGenerate = false)
    val id: Int = 0,
    val firstName: String,
    val lastName: String,
    val email: String,
    val login: String,
)

@Dao
interface AboutMeDao: BaseDao<AboutMe> {
    @Query("SELECT * FROM AboutMe LIMIT 1")
    fun getAboutMe(): Flow<AboutMe>

    @Query("SELECT COUNT(*) FROM AboutMe LIMIT 1")
    fun _checkIf1(): Flow<Int>

    fun checkIfLoggedIn() = this._checkIf1().map { it == 1 }
}