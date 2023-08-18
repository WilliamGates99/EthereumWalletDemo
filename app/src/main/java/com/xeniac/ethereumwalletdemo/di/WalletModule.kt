package com.xeniac.ethereumwalletdemo.di

import com.xeniac.ethereumwalletdemo.feature_wallet.domain.repository.EthWalletRepository
import com.xeniac.ethereumwalletdemo.feature_wallet.domain.use_case.CreateEthWalletUseCase
import com.xeniac.ethereumwalletdemo.feature_wallet.domain.util.ValidatePassword
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class WalletModule {

    @Singleton
    @Provides
    fun provideValidateUsername(): ValidatePassword = ValidatePassword()

    @Singleton
    @Provides
    fun provideCreateEthWalletUseCase(
        ethWalletRepository: EthWalletRepository,
        validatePassword: ValidatePassword
    ): CreateEthWalletUseCase = CreateEthWalletUseCase(
        ethWalletRepository, validatePassword
    )
}