package com.xeniac.ethereumwalletdemo.feature_wallet.domain.util

import com.xeniac.ethereumwalletdemo.core.util.ResultError
import com.xeniac.ethereumwalletdemo.feature_wallet.util.MIN_PASSWORD_LENGTH

class ValidatePassword {

    operator fun invoke(password: String): ResultError? {
        if (password.isBlank()) {
            return ResultError.BlankField
        }

        val isPasswordShort = password.length < MIN_PASSWORD_LENGTH
        if (isPasswordShort) {
            return ResultError.ShortPassword
        }

        val passwordContainsLettersAndDigits = password.any { it.isDigit() } &&
                password.any { it.isLetter() }

        if (!passwordContainsLettersAndDigits) {
            return ResultError.InvalidPassword
        }

        return null
    }
}