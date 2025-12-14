package com.github.aeoliux.repositories

actual class Keychain(
    val savePassFunc: (password: String) -> Unit,
    val getPassFunc: () -> String?,
    val deletePassFunc: () -> Unit
) {
    actual fun savePass(password: String) {
        savePassFunc(password)
    }

    actual fun getPass(): String? {
        return getPassFunc()
    }

    actual fun deletePass() {
        deletePassFunc()
    }
}