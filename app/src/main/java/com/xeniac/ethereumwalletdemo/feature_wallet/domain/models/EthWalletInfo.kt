package com.xeniac.ethereumwalletdemo.feature_wallet.domain.models

import com.xeniac.ethereumwalletdemo.feature_wallet.data.local.dto.EthWalletInfoDto

data class EthWalletInfo(
    val password: String,
    val name: String,
    val address: String,
    val mnemonicPhrase: String,
    val privateKey: String,
) {
    fun toEthWalletInfoDto(): EthWalletInfoDto = EthWalletInfoDto(
        password = password,
        name = name,
        address = address,
        mnemonicPhrase = mnemonicPhrase,
        privateKey = privateKey
    )
}