package com.xeniac.ethereumwalletdemo.core.util

sealed class UiEvent : Event() {
    data class ShowSnackbar(val message: UiText) : UiEvent()
    data object GetEthWalletInfo : UiEvent()
}