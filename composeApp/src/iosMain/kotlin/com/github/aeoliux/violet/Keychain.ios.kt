package com.github.aeoliux.violet

actual class Keychain {
    actual suspend fun savePass(password: String) {

    }

    actual suspend fun getPass(): String? {
        return null
    }
}