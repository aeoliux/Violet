package com.github.aeoliux.violet.repositories

import com.github.aeoliux.violet.api.ApiClient
import com.github.aeoliux.violet.storage.AboutMe
import com.github.aeoliux.violet.storage.AppDatabase
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.io.IOException
import kotlinx.serialization.SerializationException
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class ClientManager(
    private val appDatabase: AppDatabase,
    private val alertState: AlertState,
    private val keychain: Keychain,
) {
    val logStateFlow = this.appDatabase.getAboutMeDao().checkIfLoggedIn()

    private val clientMut = MutableStateFlow(ApiClientState(ApiClient()))
    val client get() = this.clientMut.asStateFlow()

    var lastConnectionTimestamp: Long = 0

    @Throws(IOException::class, SerializationException::class, CancellationException::class, IllegalStateException::class)
    suspend fun login(login: String, password: String) = this.alertState.task {
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
    @Throws(IOException::class, SerializationException::class, CancellationException::class, IllegalStateException::class)
    internal suspend fun connectWithStoredCredentials() {
        val currentTimestamp = Clock.System.now().epochSeconds
        if (currentTimestamp < this.lastConnectionTimestamp + 60)
            return

        val login = this.appDatabase.getAboutMeDao().getAboutMe().first().login
        val password = this.keychain.getPass() ?: throw IOException("Password not in keychain")
        this.connect(login, password)
    }

    @Throws(IOException::class, SerializationException::class, CancellationException::class, IllegalStateException::class)
    @OptIn(ExperimentalTime::class)
    internal suspend fun connect(login: String, password: String) {
        this.clientMut.value.client.connect(login, password)
        this.clientMut.update { ApiClientState(it.client, true) }

        this.lastConnectionTimestamp = Clock.System.now().epochSeconds
    }

    @Throws(IOException::class, SerializationException::class, CancellationException::class, IllegalStateException::class)
    suspend fun <T> with(closure: suspend (client: ApiClient) -> T): T? = this.alertState.task {
        this.connectWithStoredCredentials()
        this.clientMut.value.with(closure)
    }

    data class ApiClientState(
        val client: ApiClient,
        val connected: Boolean = false
    ) {
        @Throws(IOException::class, SerializationException::class, CancellationException::class, IllegalStateException::class)
        suspend fun <T> with(closure: suspend (client: ApiClient) -> T): T {
            when (this.connected) {
                true -> return closure(this.client)
                false -> throw IOException("Client not connected to API")
            }
        }
    }
}

