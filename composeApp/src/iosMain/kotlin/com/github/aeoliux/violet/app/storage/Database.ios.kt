package com.github.aeoliux.violet.app.storage

import androidx.room.Room
import androidx.room.RoomDatabaseConstructor
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

fun initializeDatabase(): AppDatabase {
    val dbFilePath = documentDirectory() + "/violetRoom.db"
    val dbBuilder = Room.databaseBuilder<AppDatabase>(
        name = dbFilePath
    )

    return getRoomDatabase(dbBuilder)
}

@OptIn(ExperimentalForeignApi::class)
private fun documentDirectory(): String {
    val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null
    )
    return requireNotNull(documentDirectory?.path)
}


