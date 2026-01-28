package com.github.aeoliux.violet.storage

import androidx.room.TypeConverter
import com.github.aeoliux.violet.api.scraping.messages.MessageCategories
import com.github.aeoliux.violet.api.types.GradeType
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
class RoomTypeConverters {
    @TypeConverter
    fun fromLocalDateToLong(localDate: LocalDate?): Long? = localDate?.toEpochDays()

    @TypeConverter
    fun fromLongToLocalDate(localDate: Long?): LocalDate? = localDate?.let {
        LocalDate.fromEpochDays(it)
    }

    @TypeConverter
    fun fromLocalDateTimeToLong(localDateTime: LocalDateTime?): Long? = localDateTime?.toInstant(
        TimeZone.currentSystemDefault()
    )?.epochSeconds

    @TypeConverter
    fun fromLongToLocalDateTime(localDateTime: Long?): LocalDateTime? = localDateTime?.let {
        Instant.fromEpochSeconds(it).toLocalDateTime(TimeZone.currentSystemDefault())
    }

    @TypeConverter
    fun fromLocalTimeToLong(localTime: LocalTime?): Long? = localTime?.toSecondOfDay()?.toLong()

    @TypeConverter
    fun fromLongToLocalTime(localTime: Long?): LocalTime? = localTime?.let {
        LocalTime.fromSecondOfDay(it.toInt())
    }

    @TypeConverter
    fun fromMessageCategoryToInt(messageCategory: MessageCategories?): Int? = messageCategory?.categoryId

    @TypeConverter
    fun fromIntToMessageCategory(messageCategory: Int): MessageCategories = MessageCategories.fromInt(messageCategory)

    @TypeConverter
    fun fromGradeTypeToInt(gradeType: GradeType?): Int? = gradeType?.toInt()

    @TypeConverter
    fun fromIntToGradeType(gradeType: Int): GradeType = GradeType.fromInt(gradeType)

    @TypeConverter
    fun fromPrimitiveStringListToString(l: List<String>?): String? = l?.joinToString(";")

    @TypeConverter
    fun fromStringToPrimitiveStringList(l: String?): List<String>? = l?.split(";")
}