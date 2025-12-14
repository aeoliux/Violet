package com.github.aeoliux.repositories

expect class Keychain {
    fun savePass(password: String)
    fun getPass(): String?
    fun deletePass()
}