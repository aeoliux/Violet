package com.github.aeoliux.violet.repositories

expect class Keychain {
    fun savePass(password: String)
    fun getPass(): String?
    fun deletePass()
}