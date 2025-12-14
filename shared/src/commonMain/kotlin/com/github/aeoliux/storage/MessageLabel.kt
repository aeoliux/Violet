package com.github.aeoliux.storage

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Query
import com.github.aeoliux.api.scraping.messages.MessageCategories
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDateTime

@Entity(tableName = "MessageLabels")
data class MessageLabel(
    @PrimaryKey(autoGenerate = true) val key: Int = 0,
    val category: MessageCategories,

    val url: String,
    val sender: String,
    val topic: String,
    val sentAt: LocalDateTime?,
    val hasAttachment: Boolean,
)

@Dao
interface MessageLabelsDao: BaseDao<MessageLabel> {
    @Query("SELECT * FROM MessageLabels")
    fun getMessageLabels(): Flow<List<MessageLabel>>
}
