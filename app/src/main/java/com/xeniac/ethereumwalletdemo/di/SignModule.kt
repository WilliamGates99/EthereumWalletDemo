package com.xeniac.ethereumwalletdemo.di

import com.xeniac.ethereumwalletdemo.feature_sign.domain.repository.SignMessageRepository
import com.xeniac.ethereumwalletdemo.feature_sign.domain.use_case.SignMessageUseCase
import com.xeniac.ethereumwalletdemo.feature_sign.domain.util.ValidateMessage
import com.xeniac.ethereumwalletdemo.feature_sign.util.CryptoManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class SignModule {

    @Singleton
    @Provides
    fun provideCryptoManager(): CryptoManager = CryptoManager()

    @Singleton
    @Provides
    fun provideValidateMessage(): ValidateMessage = ValidateMessage()

    @Singleton
    @Provides
    fun provideSignMessageUseCase(
        signMessageRepository: SignMessageRepository,
        validateMessage: ValidateMessage
    ): SignMessageUseCase = SignMessageUseCase(
        signMessageRepository, validateMessage
    )
}