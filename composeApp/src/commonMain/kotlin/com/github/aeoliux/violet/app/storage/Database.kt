package com.github.aeoliux.violet.app.storage

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.TypeConverters
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

@Database(entities = [
    Agenda::class,
    Grade::class,
    Attendance::class,
    LuckyNumber::class,
    MessageLabel::class,
    SchoolNotice::class,
    Timetable::class,
    Me::class,
    ClassInfo::class
                     ], version = 1, exportSchema = false)
@TypeConverters(RoomTypeConverters::class)
@ConstructedBy(AppDatabaseCtor::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun getAgendaDao(): AgendaDao
    abstract fun getGradesDao(): GradesDao
    abstract fun getAttendanceDao(): AttendanceDao
    abstract fun getMessageLabelsDao(): MessageLabelsDao
    abstract fun getSchoolNoticesDao(): SchoolNoticesDao
    abstract fun getTimetableDao(): TimetableDao
    abstract fun getAboutUserDao(): AboutUserDao
    abstract fun getLuckyNumberDao(): LuckyNumberDao
}

fun getRoomDatabase(
    builder: RoomDatabase.Builder<AppDatabase>
): AppDatabase {
    return builder
        .fallbackToDestructiveMigrationOnDowngrade(true)
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
}

expect object AppDatabaseCtor: RoomDatabaseConstructor<AppDatabase>