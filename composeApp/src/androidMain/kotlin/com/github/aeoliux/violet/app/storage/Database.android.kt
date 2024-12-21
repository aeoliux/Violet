package com.github.aeoliux.violet.app.storage

import android.annotation.SuppressLint
import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabaseConstructor


object AppDatabaseConstructor {
    fun initialize(context: Context): AppDatabase {
        val dbFile = context.getDatabasePath("violetRoom.db")
        val dbBuilder = Room.databaseBuilder<AppDatabase>(
            context = context,
            name = dbFile.absolutePath
        )

        return getRoomDatabase(dbBuilder)
    }
}