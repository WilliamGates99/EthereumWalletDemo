package com.xeniac.ethereumwalletdemo.di

import com.xeniac.ethereumwalletdemo.feature_sign.data.repository.SignMessageRepositoryImpl
import com.xeniac.ethereumwalletdemo.feature_sign.domain.repository.SignMessageRepository
import com.xeniac.ethereumwalletdemo.feature_wallet.data.repository.EthWalletRepositoryImpl
import com.xeniac.ethereumwalletdemo.feature_wallet.domain.repository.EthWalletRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindEthWalletRepository(
        ethWalletRepository: EthWalletRepositoryImpl
    ): EthWalletRepository

    @Binds
    @Singleton
    abstract fun bindSignMessageRepository(
        signMessageRepository: SignMessageRepositoryImpl
    ): SignMessageRepository
}