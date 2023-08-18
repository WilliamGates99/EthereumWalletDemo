package com.xeniac.ethereumwalletdemo.feature_wallet.domain.repository

import com.xeniac.ethereumwalletdemo.core.util.Resource
import com.xeniac.ethereumwalletdemo.feature_wallet.domain.models.EthWalletInfo

interface EthWalletRepository {

    suspend fun createOfflineEthWallet(
        password: String,
        walletFileDir: String
    ): Resource<Nothing>

    suspend fun upsertEthWalletInfo(ethWalletInfo: EthWalletInfo): Resource<Nothing>

    suspend fun deleteAllEthWallets(): Resource<Nothing>

    suspend fun getFirstEthWalletInfo(): Resource<EthWalletInfo>
}