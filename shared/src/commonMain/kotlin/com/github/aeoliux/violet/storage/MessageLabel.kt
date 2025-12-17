package com.github.aeoliux.violet.storage

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Query
import com.github.aeoliux.violet.api.scraping.messages.MessageCategories
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDateTime

@Entity(tableName = "MessageLabels")
data class MessageLabel(
    @PrimaryKey(autoGenerate = false) val key: Int = 0,
    val category: MessageCategories,

    val url: String,
    val sender: String,
    val topic: String,
    val sentAt: LocalDateTime?,
    val hasAttachment: Boolean,
)

@Dao
interface MessageLabelsDao: BaseDao<MessageLabel> {
    @Query("SELECT * FROM MessageLabels ORDER BY sentAt DESC")
    fun getMessageLabels(): Flow<List<MessageLabel>>

    @Query("SELECT * FROM MessageLabels WHERE sender LIKE '%' || :query || '%' OR topic LIKE '%' || :query || '%' ORDER BY sentAt DESC")
    fun getMessageLabelsByQuery(query: String): Flow<List<MessageLabel>>
}
