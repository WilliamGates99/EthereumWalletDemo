package com.xeniac.ethereumwalletdemo.feature_wallet.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.xeniac.ethereumwalletdemo.feature_wallet.data.local.dto.EthWalletInfoDto

@Dao
interface EthWalletDao {

    @Upsert
    suspend fun upsertEthWalletInfo(ethWalletInfoDto: EthWalletInfoDto)

    @Query("DELETE FROM eth_wallets")
    suspend fun deleteAllEthWallets()

    @Query("SELECT * FROM eth_wallets LIMIT 1")
    suspend fun getFirstEthWalletInfo(): EthWalletInfoDto?
}