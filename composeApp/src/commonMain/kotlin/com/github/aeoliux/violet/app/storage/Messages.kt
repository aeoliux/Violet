package com.github.aeoliux.violet.app.storage

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import com.github.aeoliux.violet.api.scraping.messages.MessageCategories
import com.github.aeoliux.violet.api.scraping.messages.MessagesList
import com.github.aeoliux.violet.api.scraping.messages.MessageLabel as MessageLabelFinal
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
interface MessageLabelsDao {
    @Insert
    suspend fun insertMessageLabel(label: MessageLabel)

    @Query("DELETE FROM MessageLabels")
    suspend fun deleteMessageLabels()

    @Query("SELECT * FROM MessageLabels")
    suspend fun getMessageLabels(): List<MessageLabel>
}

class MessageLabelsRepository(private val database: AppDatabase) {
    suspend fun deleteMessageLabels() = database.getMessageLabelsDao().deleteMessageLabels()

    suspend fun insertMessageLabels(labels: MessagesList) = labels.forEach { (category, labels) ->
        labels.forEach {
            database.getMessageLabelsDao().insertMessageLabel(
                MessageLabel(
                category = category,

                url = it.url,
                sender = it.sender,
                topic = it.topic,
                sentAt = it.sentAt,
                hasAttachment = it.hasAttachment
            )
            )
        }
    }

    suspend fun getMessageLabels(): MessagesList =
        database.getMessageLabelsDao()
            .getMessageLabels()
            .fold(MessagesList()) { acc, it ->
                val att = MessageLabelFinal(
                    url = it.url,
                    sender = it.sender,
                    topic = it.topic,
                    sentAt = it.sentAt,
                    hasAttachment = it.hasAttachment
                )

                acc[it.category] = acc[it.category]?.plus(att)?: listOf(att)

                acc
            }
}