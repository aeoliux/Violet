package com.github.aeoliux.violet.storage

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.SQLiteDriver
import androidx.sqlite.driver.AndroidSQLiteDriver

fun getDatabaseBuilder(context: Context): RoomDatabase.Builder<AppDatabase> {
    val appContext = context.applicationContext
    val dbFile = appContext.getDatabasePath("librekeeper.db")
    return Room.databaseBuilder<AppDatabase>(
        context = appContext,
        name = dbFile.absolutePath
    )
}

actual val sqliteDriver: SQLiteDriver = AndroidSQLiteDriver()