package com.xeniac.ethereumwalletdemo.di

import android.content.Context
import androidx.room.Room
import com.xeniac.ethereumwalletdemo.core.util.ETH_WALLETS_DATABASE_NAME
import com.xeniac.ethereumwalletdemo.feature_wallet.data.local.EthWalletsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun provideEthWalletsDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context = context,
        klass = EthWalletsDatabase::class.java,
        name = ETH_WALLETS_DATABASE_NAME
    ).build()

    @Singleton
    @Provides
    fun provideEthWalletsDao(database: EthWalletsDatabase) = database.dao()
}