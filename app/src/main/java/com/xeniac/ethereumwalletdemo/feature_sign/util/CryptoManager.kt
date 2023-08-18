package com.xeniac.ethereumwalletdemo.feature_sign.util

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.io.InputStream
import java.security.KeyStore
import java.security.KeyStore.SecretKeyEntry
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

class CryptoManager {

    companion object {
        private const val ALGORITHM = KeyProperties.KEY_ALGORITHM_AES
        private const val BLOCK_MODE = KeyProperties.BLOCK_MODE_CBC
        private const val PADDING = KeyProperties.ENCRYPTION_PADDING_PKCS7
        private const val TRANSFORMATION = "$ALGORITHM/$BLOCK_MODE/$PADDING"
    }

    private val keyStore = KeyStore.getInstance("AndroidKeyStore").apply {
        load(null)
    }

    private fun getEncryptCipher(privateKey: String): Cipher {
        return Cipher.getInstance(TRANSFORMATION).apply {
            init(
                Cipher.ENCRYPT_MODE,
                getKey(privateKey)
            )
        }
    }

    private fun getDecryptCipherForIv(privateKey: String, iv: ByteArray): Cipher {
        return Cipher.getInstance(TRANSFORMATION).apply {
            init(
                Cipher.DECRYPT_MODE,
                getKey(privateKey),
                IvParameterSpec(iv)
            )
        }
    }

    private fun getKey(privateKey: String): SecretKey {
        val existingKey = keyStore.getEntry(privateKey, null) as? SecretKeyEntry
        return existingKey?.secretKey ?: createKey(privateKey)
    }

    private fun createKey(privateKey: String): SecretKey {
        return KeyGenerator.getInstance(ALGORITHM).apply {
            init(
                KeyGenParameterSpec.Builder(
                    privateKey,
                    KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
                )
                    .setBlockModes(BLOCK_MODE)
                    .setEncryptionPaddings(PADDING)
                    .setUserAuthenticationRequired(false)
                    .setRandomizedEncryptionRequired(true)
                    .build()
            )
        }.generateKey()
    }

    fun encrypt(privateKey: String, bytes: ByteArray): ByteArray {
        val encryptCipher = getEncryptCipher(privateKey)
        return encryptCipher.doFinal(bytes)
    }

    fun decrypt(privateKey: String, inputStream: InputStream): ByteArray {
        return inputStream.use {
            val ivSize = it.read()
            val iv = ByteArray(ivSize)
            it.read(iv)

            val encryptedBytesSize = it.read()
            val encryptedBytes = ByteArray(encryptedBytesSize)
            it.read(encryptedBytes)

            getDecryptCipherForIv(
                privateKey = privateKey,
                iv = iv
            ).doFinal(encryptedBytes)
        }
    }
}