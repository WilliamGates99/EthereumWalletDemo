package com.xeniac.ethereumwalletdemo.feature_wallet.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.xeniac.ethereumwalletdemo.feature_wallet.data.local.dto.EthWalletInfoDto

@Database(
    entities = [EthWalletInfoDto::class],
    version = 1
)
abstract class EthWalletsDatabase : RoomDatabase() {

    abstract fun dao(): EthWalletDao
}