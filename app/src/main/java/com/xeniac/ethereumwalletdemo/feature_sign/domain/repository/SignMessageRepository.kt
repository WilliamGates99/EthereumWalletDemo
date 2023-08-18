package com.xeniac.ethereumwalletdemo.feature_sign.domain.repository

import com.xeniac.ethereumwalletdemo.core.util.Resource

interface SignMessageRepository {

    suspend fun signMessageUsingPrivateKey(message: String, privateKey: String): Resource<String>
}