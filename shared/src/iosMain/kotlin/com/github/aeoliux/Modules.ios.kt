package com.github.aeoliux

import com.github.aeoliux.repositories.Keychain
import com.github.aeoliux.storage.AppDatabase
import com.github.aeoliux.storage.getDatabaseBuilder
import com.github.aeoliux.storage.getRoomDatabase
import org.koin.core.module.Module
import org.koin.dsl.bind
import org.koin.dsl.module

fun getPlatformModules(keychain: Keychain): Module = module {
    single { keychain } bind Keychain::class
    single { getRoomDatabase(getDatabaseBuilder()) } bind AppDatabase::class
}