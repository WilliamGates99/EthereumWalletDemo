package com.xeniac.ethereumwalletdemo.feature_wallet.data.local.dto

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.xeniac.ethereumwalletdemo.feature_wallet.domain.models.EthWalletInfo

@Entity(tableName = "eth_wallets")
data class EthWalletInfoDto(
    val password: String,
    val name: String,
    val address: String,
    val mnemonicPhrase: String,
    val privateKey: String,
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null
) {
    fun toEthWalletInfo(): EthWalletInfo = EthWalletInfo(
        password = password,
        name = name,
        address = address,
        mnemonicPhrase = mnemonicPhrase,
        privateKey = privateKey
    )
}