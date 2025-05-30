package com.github.aeoliux.violet.app.storage

import android.content.Context
import androidx.room.Room

fun initializeDatabase(context: Context): AppDatabase {
    val dbFile = context.getDatabasePath("violetRoom.db")
    val dbBuilder = Room.databaseBuilder<AppDatabase>(
        context = context,
        name = dbFile.absolutePath
    )

    return getRoomDatabase(dbBuilder)
}