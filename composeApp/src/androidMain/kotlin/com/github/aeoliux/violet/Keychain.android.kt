package com.github.aeoliux.violet

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.nio.ByteBuffer
import java.nio.charset.Charset
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

actual class Keychain(private val context: Context) {
    private val keyStore = KeyStore.getInstance("AndroidKeyStore").apply {
        load(null)
    }
    private var key: SecretKey? = keyStore.getKey("synergiaCredentialsKey", null) as? SecretKey
    private val sharedPreferences = context.getSharedPreferences("SecurePrefs", Context.MODE_PRIVATE)

    init {
        if (key == null) {
            val keyGenerator =
                KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
            keyGenerator.init(
                KeyGenParameterSpec.Builder(
                    "synergiaCredentialsKey",
                    KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
                )
                    .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                    .setKeySize(256)
                    .build()
            )

            key = keyGenerator.generateKey()
        }
    }

    @OptIn(ExperimentalEncodingApi::class)
    actual fun savePass(password: String) {
        println(password)
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.ENCRYPT_MODE, key)
        val iv = cipher.iv
        val cipherText = cipher.doFinal(password.toByteArray(Charset.forName("UTF-8")))

        val encryptedPasswordBuffer = ByteBuffer.allocate(iv.size + cipherText.size)
        encryptedPasswordBuffer.put(iv)
        encryptedPasswordBuffer.put(cipherText)

        val encryptedPassword = Base64.encode(encryptedPasswordBuffer.array())

        sharedPreferences.edit().putString("synergiaCredentials", encryptedPassword).apply()
    }

    @OptIn(ExperimentalEncodingApi::class)
    actual fun getPass(): String? {
        val encryptedPassword = sharedPreferences.getString("synergiaCredentials", null)?: return null
        val encryptedPasswordBuffer = ByteBuffer.wrap(
            Base64.decode(encryptedPassword)
        )

        val iv = ByteArray(12)
        encryptedPasswordBuffer.get(iv)
        val cipherText = ByteArray(encryptedPasswordBuffer.remaining())
        encryptedPasswordBuffer.get(cipherText)

        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        val gcmParameterSpec = GCMParameterSpec(128, iv)
        cipher.init(Cipher.DECRYPT_MODE, key, gcmParameterSpec)

        val final = String(cipher.doFinal(cipherText), Charsets.UTF_8)
        println(final)
        return final
    }

    actual fun deletePass() {
        sharedPreferences.edit().remove("synergiaCredentials").apply()
    }
}