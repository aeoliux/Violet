package com.github.aeoliux.violet.storage

import androidx.room.TypeConverter
import com.github.aeoliux.violet.api.scraping.messages.MessageCategories
import com.github.aeoliux.violet.api.types.GradeType
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime

class RoomTypeConverters {
    @TypeConverter
    fun fromLocalDateToString(localDate: LocalDate?): String? = localDate?.toString()

    @TypeConverter
    fun fromStringToLocalDate(localDate: String?): LocalDate? = localDate?.let {
        LocalDate.parse(it)
    }

    @TypeConverter
    fun fromLocalDateTimeToString(localDateTime: LocalDateTime?): String? = localDateTime?.toString()

    @TypeConverter
    fun fromStringToLocalDateTime(localDateTime: String?): LocalDateTime? = localDateTime?.let {
        LocalDateTime.parse(it)
    }

    @TypeConverter
    fun fromLocalTimeToString(localTime: LocalTime?): String? = localTime?.toString()

    @TypeConverter
    fun fromStringToLocalTime(localTime: String?): LocalTime? = localTime?.let {
        LocalTime.parse(it)
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