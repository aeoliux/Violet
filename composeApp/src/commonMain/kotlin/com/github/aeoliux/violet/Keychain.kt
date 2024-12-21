package com.github.aeoliux.violet

expect class Keychain {
    fun savePass(password: String)
    fun getPass(): String?
    fun deletePass()
}