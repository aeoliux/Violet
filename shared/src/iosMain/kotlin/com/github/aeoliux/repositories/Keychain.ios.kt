package com.github.aeoliux.repositories

actual class Keychain {
    actual fun savePass(password: String) {
    }

    actual fun getPass(): String? {
        TODO("Not yet implemented")
    }

    actual fun deletePass() {
    }
}