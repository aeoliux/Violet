package com.github.aeoliux.violet.storage

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.Delete
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.TypeConverters
import androidx.room.Upsert
import androidx.sqlite.SQLiteDriver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

@Database(
    entities = [
        AboutMe::class,
        Agenda::class,
        Attendance::class,
        ClassInfo::class,
        Grade::class,
        LuckyNumber::class,
        Message::class,
        MessageLabel::class,
        SchoolNotice::class,
        Timetable::class
               ],
    version = 1
)
@TypeConverters(RoomTypeConverters::class)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun getAboutMeDao(): AboutMeDao
    abstract fun getAgendaDao(): AgendaDao
    abstract fun getAttendanceDao(): AttendanceDao
    abstract fun getClassInfoDao(): ClassInfoDao
    abstract fun getGradesDao(): GradesDao
    abstract fun getMessageLabelsDao(): MessageLabelsDao
    abstract fun getSchoolNoticesDao(): SchoolNoticesDao
    abstract fun getTimetableDao(): TimetableDao
    abstract fun getLuckyNumberDao(): LuckyNumberDao
    abstract fun getMessagesDao(): MessagesDao
}

fun getRoomDatabase(
    builder: RoomDatabase.Builder<AppDatabase>
): AppDatabase {
    return builder
        .setDriver(sqliteDriver)
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
}

expect val sqliteDriver: SQLiteDriver

@Suppress("KotlinNoActualForExpect")
expect object AppDatabaseConstructor: RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}

interface BaseDao<T> {
    @Upsert
    suspend fun upsert(t: T)

    @Delete
    suspend fun delete(t: T)

    @Upsert
    suspend fun upsertMultiple(t: List<T>)

    @Delete
    suspend fun deleteMatching(t: List<T>)
}