package com.xeniac.ethereumwalletdemo.feature_wallet.domain.models

import com.xeniac.ethereumwalletdemo.core.util.Resource
import com.xeniac.ethereumwalletdemo.core.util.ResultError

data class CreateEthWalletResult(
    val passwordResultError: ResultError? = null,
    val result: Resource<Nothing>? = null
)