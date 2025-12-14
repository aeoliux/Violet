package com.github.aeoliux.storage

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Query

@Entity(tableName = "Messages")
data class Message(
    @PrimaryKey(autoGenerate = true) val key: Int = 0,
    val url: String,
    val content: String,
    val attachments: List<String>
)

@Dao
interface MessagesDao: BaseDao<Message> {
    @Query("SELECT * FROM Messages WHERE `url` = :url LIMIT 1")
    suspend fun selectMessage(url: String): Message?

//    @Query("SELECT COUNT(*) FROM Messages WHERE `url` = :url")
//    suspend fun countById(url: String): Int

    @Query("DELETE FROM Messages")
    suspend fun clearStorage()
}