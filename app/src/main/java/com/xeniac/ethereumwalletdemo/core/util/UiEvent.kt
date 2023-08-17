package com.xeniac.ethereumwalletdemo.core.util

sealed class UiEvent : Event() {
    data class ShowSnackbar(val message: UiText) : UiEvent()
    data class Navigate(val route: String) : UiEvent()
    data object NavigateUp : UiEvent()
    data class ShowNetworkErrorText(val message: UiText) : UiEvent()
}