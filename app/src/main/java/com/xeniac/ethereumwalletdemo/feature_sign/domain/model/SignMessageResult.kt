package com.xeniac.ethereumwalletdemo.feature_sign.domain.model

import com.xeniac.ethereumwalletdemo.core.util.Resource
import com.xeniac.ethereumwalletdemo.core.util.ResultError

data class SignMessageResult(
    val messageResultError: ResultError? = null,
    val result: Resource<String>? = null
)