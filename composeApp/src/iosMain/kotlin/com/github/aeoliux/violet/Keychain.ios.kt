package com.github.aeoliux.violet

actual class Keychain(
    val savePassFunc: (password: String) -> Unit,
    val getPassFunc: () -> String?
) {
    actual fun savePass(password: String) {
        savePassFunc(password)
    }

    actual fun getPass(): String? {
        return getPassFunc()
    }
}