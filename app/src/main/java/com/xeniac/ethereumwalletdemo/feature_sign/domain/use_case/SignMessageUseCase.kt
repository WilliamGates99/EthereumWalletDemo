package com.xeniac.ethereumwalletdemo.feature_sign.domain.use_case

import com.xeniac.ethereumwalletdemo.feature_sign.domain.model.SignMessageResult
import com.xeniac.ethereumwalletdemo.feature_sign.domain.repository.SignMessageRepository
import com.xeniac.ethereumwalletdemo.feature_sign.domain.util.ValidateMessage

class SignMessageUseCase(
    private val signMessageRepository: SignMessageRepository,
    private val validateMessage: ValidateMessage
) {

    suspend operator fun invoke(
        privateKey: String,
        message: String
    ): SignMessageResult {
        val messageError = validateMessage(message)

        val hasError = messageError != null

        if (hasError) {
            return SignMessageResult(
                messageResultError = messageError
            )
        }

        val result = signMessageRepository.signMessageUsingPrivateKey(
            privateKey = privateKey,
            message = message
        )

        return SignMessageResult(
            result = result
        )
    }
}