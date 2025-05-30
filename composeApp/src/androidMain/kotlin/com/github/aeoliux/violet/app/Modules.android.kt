package com.github.aeoliux.violet.app

import com.github.aeoliux.violet.Keychain
import com.github.aeoliux.violet.app.storage.AppDatabase
import com.github.aeoliux.violet.app.storage.AppDatabaseConstructor
import com.github.aeoliux.violet.app.storage.initializeDatabase
import org.koin.core.module.Module
import org.koin.dsl.bind
import org.koin.dsl.module

actual val keychainModule: Module = module {
    single {
        Keychain(get())
    } bind Keychain::class
}

actual val databaseModule: Module = module {
    single {
        initializeDatabase(get())
    } bind AppDatabase::class
}