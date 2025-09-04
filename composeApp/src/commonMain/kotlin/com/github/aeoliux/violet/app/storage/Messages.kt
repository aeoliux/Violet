package com.github.aeoliux.violet.app.storage

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import com.github.aeoliux.violet.api.scraping.messages.MessageCategories
import com.github.aeoliux.violet.api.scraping.messages.MessagesList
import kotlinx.datetime.Clock
import com.github.aeoliux.violet.api.scraping.messages.MessageLabel as MessageLabelFinal
import com.github.aeoliux.violet.api.scraping.messages.Message as MessageFinal
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

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

@Entity(tableName = "Messages")
data class Message(
    @PrimaryKey(autoGenerate = true) val key: Int = 0,
    val url: String,
    val content: String,
    val attachments: List<String>
)

@Dao
interface MessagesDao {
    @Insert
    suspend fun insertMessage(message: Message)

    @Query("SELECT * FROM Messages WHERE `url` = :url LIMIT 1")
    suspend fun selectMessage(url: String): Message?

    @Query("SELECT COUNT(*) FROM Messages WHERE `url` = :url")
    suspend fun countById(url: String): Int

    @Query("DELETE FROM Messages")
    suspend fun clearStorage()
}

class MessagesRepository(private val database: AppDatabase) {
    suspend fun clearStorage() = database.getMessagesDao().clearStorage()
    suspend fun doesExist(url: String): Boolean = database.getMessagesDao().countById(url) != 0

    suspend fun getMessage(label: MessageLabelFinal): MessageFinal? =
        database.getMessagesDao().selectMessage(label.url)?.let {
            println(it.attachments)
            MessageFinal(
                sender = label.sender,
                date = label.sentAt?:Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
                topic = label.topic,
                content = it.content,
                attachments = it.attachments.fold(emptyList()) { acc, s -> s.split(",").let { acc.plus(Pair(it[0], it[1])) }}
            )
        }

    suspend fun insertMessage(url: String, message: MessageFinal) =
        database.getMessagesDao().insertMessage(
            Message(
                url = url,
                content = message.content,
                attachments = message.attachments.fold(emptyList()) { acc, p ->
                    val str = "${p.first},${p.second}"
                    acc.plus(str)
                }
            )
        )
}