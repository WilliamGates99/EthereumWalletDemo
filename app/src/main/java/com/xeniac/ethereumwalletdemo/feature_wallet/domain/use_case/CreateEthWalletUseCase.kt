package com.xeniac.ethereumwalletdemo.feature_wallet.domain.use_case

import com.xeniac.ethereumwalletdemo.feature_wallet.domain.models.CreateEthWalletResult
import com.xeniac.ethereumwalletdemo.feature_wallet.domain.repository.EthWalletRepository
import com.xeniac.ethereumwalletdemo.feature_wallet.domain.util.ValidatePassword

class CreateEthWalletUseCase(
    private val ethWalletRepository: EthWalletRepository,
    private val validatePassword: ValidatePassword
) {

    suspend operator fun invoke(
        password: String,
        walletFileDir: String,
    ): CreateEthWalletResult {
        val passwordError = validatePassword(password)

        val hasError = passwordError != null

        if (hasError) {
            return CreateEthWalletResult(
                passwordResultError = passwordError
            )
        }

        val result = ethWalletRepository.createOfflineEthWallet(
            password = password,
            walletFileDir = walletFileDir
        )

        return CreateEthWalletResult(
            result = result
        )
    }
}