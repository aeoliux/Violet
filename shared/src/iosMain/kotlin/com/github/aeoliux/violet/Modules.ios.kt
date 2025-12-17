package com.github.aeoliux.violet

import com.github.aeoliux.violet.repositories.Keychain
import com.github.aeoliux.violet.storage.AppDatabase
import com.github.aeoliux.violet.storage.getDatabaseBuilder
import com.github.aeoliux.violet.storage.getRoomDatabase
import org.koin.core.module.Module
import org.koin.dsl.bind
import org.koin.dsl.module

fun getPlatformModules(keychain: Keychain): Module = module {
    single { keychain } bind Keychain::class
    single { getRoomDatabase(getDatabaseBuilder()) } bind AppDatabase::class
}