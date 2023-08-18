package com.xeniac.ethereumwalletdemo.feature_sign.domain.util

import com.xeniac.ethereumwalletdemo.core.util.ResultError

class ValidateMessage {

    operator fun invoke(message: String): ResultError? {
        if (message.isBlank()) {
            return ResultError.BlankField
        }

        return null
    }
}