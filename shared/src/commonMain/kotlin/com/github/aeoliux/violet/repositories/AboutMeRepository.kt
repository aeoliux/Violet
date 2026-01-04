package com.github.aeoliux.violet.repositories

import com.github.aeoliux.violet.storage.AboutMe
import com.github.aeoliux.violet.storage.AppDatabase
import kotlinx.coroutines.CancellationException
import kotlinx.io.IOException
import kotlinx.serialization.SerializationException

class AboutMeRepository(
    private val appDatabase: AppDatabase,
    private val client: ClientManager
) {
    fun getAboutMeFlow() = this.appDatabase.getAboutMeDao().getAboutMe()

    @Throws(IOException::class, SerializationException::class, CancellationException::class)
    suspend fun refresh() {
        this.client.with { client ->
            val newAboutMe = client.me()

            this.appDatabase.getAboutMeDao().upsert(AboutMe(
                id = newAboutMe.id,
                firstName = newAboutMe.firstName,
                lastName = newAboutMe.lastName,
                email = newAboutMe.email,
                login = newAboutMe.login
            ))
        }
    }
}