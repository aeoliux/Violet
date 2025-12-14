package com.github.aeoliux.repositories

import com.github.aeoliux.api.ApiClient
import com.github.aeoliux.storage.AboutMe
import com.github.aeoliux.storage.AppDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.io.IOException
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class ClientManager(
    private val appDatabase: AppDatabase,
    private val keychain: Keychain
) {
    val logStateFlow = this.appDatabase.getAboutMeDao().checkIfLoggedIn()

    private val clientMut = MutableStateFlow(ApiClientState(ApiClient()))
    val client get() = this.clientMut.asStateFlow()

    var lastConnectionTimestamp: Long = 0

    suspend fun login(login: String, password: String) {
        this.connect(login, password)

        val newAboutMe = this.clientMut.value.client.me()
        this.appDatabase.getAboutMeDao().apply {
            upsert(AboutMe(
                id = newAboutMe.id,
                firstName = newAboutMe.firstName,
                lastName = newAboutMe.lastName,
                email = newAboutMe.email,
                login = newAboutMe.login
            ))
        }

        keychain.savePass(password)
    }

    @OptIn(ExperimentalTime::class)
    suspend fun connectWithStoredCredentials() {
        val currentTimestamp = Clock.System.now().epochSeconds
        if (currentTimestamp < this.lastConnectionTimestamp + 60)
            return

        val login = this.appDatabase.getAboutMeDao().getAboutMe().first().login
        val password = this.keychain.getPass() ?: throw IOException("Password not in keychain")
        this.connect(login, password)
    }

    @OptIn(ExperimentalTime::class)
    suspend fun connect(login: String, password: String) {
        this.clientMut.value.client.connect(login, password)
        this.clientMut.update { ApiClientState(it.client, true) }

        this.lastConnectionTimestamp = Clock.System.now().epochSeconds
    }

    suspend fun <T> with(closure: suspend (client: ApiClient) -> T): T {
        this.connectWithStoredCredentials()
        return this.clientMut.value.with(closure)
    }

    data class ApiClientState(
        val client: ApiClient,
        val connected: Boolean = false
    ) {
        suspend fun <T> with(closure: suspend (client: ApiClient) -> T): T {
            when (this.connected) {
                true -> return closure(this.client)
                false -> throw IOException("Client not connected to API")
            }
        }
    }
}

