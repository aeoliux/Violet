package com.github.aeoliux.repositories

import com.github.aeoliux.storage.AboutMe
import com.github.aeoliux.storage.AppDatabase
import kotlinx.coroutines.flow.first

class AboutMeRepository(
    private val appDatabase: AppDatabase,
    private val client: ClientManager
) {
    fun getAboutMeFlow() = this.appDatabase.getAboutMeDao().getAboutMe()

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