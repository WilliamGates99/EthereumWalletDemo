package com.xeniac.ethereumwalletdemo.feature_sign.data.repository

import com.xeniac.ethereumwalletdemo.core.util.Resource
import com.xeniac.ethereumwalletdemo.core.util.UiText
import com.xeniac.ethereumwalletdemo.feature_sign.domain.repository.SignMessageRepository
import com.xeniac.ethereumwalletdemo.feature_sign.util.CryptoManager
import timber.log.Timber
import javax.inject.Inject

class SignMessageRepositoryImpl @Inject constructor(
    private val cryptoManager: CryptoManager
) : SignMessageRepository {

    override suspend fun signMessageUsingPrivateKey(
        message: String,
        privateKey: String
    ): Resource<String> {
        return try {
            val bytes = message.encodeToByteArray()
            val signedMessage = cryptoManager.encrypt(
                privateKey = privateKey,
                bytes = bytes
            ).decodeToString()

            Timber.i("Message signed successfully using the private key. Signed Message: $signedMessage")
            Resource.Success(signedMessage)
        } catch (e: Exception) {
            val errorMessage = e.message.toString()
            Timber.e("Sign message using the private key failed: $errorMessage")
            Resource.Error(UiText.DynamicString(errorMessage))
        }
    }
}