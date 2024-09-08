package com.github.aeoliux.violet

expect class Keychain {
    suspend fun savePass(password: String)
    suspend fun getPass(): String?
}