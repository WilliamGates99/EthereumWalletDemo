package com.xeniac.ethereumwalletdemo.feature_wallet.presentation

import com.xeniac.ethereumwalletdemo.core.domain.repository.ConnectivityObserver

sealed class CreateEthWalletEvent {
    data class PasswordChanged(val password: String) : CreateEthWalletEvent()

    data class GenerateEthWallet(
        val networkStatus: ConnectivityObserver.Status,
        val walletFileDir: String
    ) : CreateEthWalletEvent()

    data object GetFirstEthWalletInfo : CreateEthWalletEvent()
}