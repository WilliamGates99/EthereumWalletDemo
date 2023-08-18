package com.xeniac.ethereumwalletdemo.feature_sign.presentation

sealed class SignMessageEvent {
    data class MessageChanged(val message: String) : SignMessageEvent()

    data class SignMessageWithPrivateKey(val privateKey: String) : SignMessageEvent()
}