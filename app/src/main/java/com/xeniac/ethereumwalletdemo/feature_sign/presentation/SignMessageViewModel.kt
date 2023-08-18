package com.xeniac.ethereumwalletdemo.feature_sign.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xeniac.ethereumwalletdemo.R
import com.xeniac.ethereumwalletdemo.core.domain.states.CustomTextFieldState
import com.xeniac.ethereumwalletdemo.core.util.Resource
import com.xeniac.ethereumwalletdemo.core.util.UiEvent
import com.xeniac.ethereumwalletdemo.core.util.UiText
import com.xeniac.ethereumwalletdemo.feature_sign.domain.use_case.SignMessageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignMessageViewModel @Inject constructor(
    private val signMessageUseCase: SignMessageUseCase
) : ViewModel() {

    private val _message = MutableStateFlow(CustomTextFieldState())
    val message = _message.asStateFlow()

    private val _signedMessage = MutableStateFlow(CustomTextFieldState())
    val signedMessage = _signedMessage.asStateFlow()

    private val _isSignMessageLoading = MutableStateFlow(false)
    val isSignMessageLoading = _isSignMessageLoading.asStateFlow()

    private val _signMessageEventChannel = Channel<UiEvent>()
    val signMessageEventChannel = _signMessageEventChannel.receiveAsFlow()

    fun onEvent(event: SignMessageEvent) {
        when (event) {
            is SignMessageEvent.MessageChanged -> {
                _message.value = message.value.copy(text = event.message)
            }
            is SignMessageEvent.SignMessageWithPrivateKey -> signMessageWithPrivateKey(event.privateKey)
        }
    }

    private fun signMessageWithPrivateKey(privateKey: String) = viewModelScope.launch {
        _isSignMessageLoading.value = true

        val signMessageResult = signMessageUseCase(
            privateKey = privateKey,
            message = message.value.text
        )

        if (signMessageResult.messageResultError != null) {
            _message.value = message.value.copy(
                errorText = UiText.StringResource(R.string.sign_message_textfield_error_message_blank)
            )
        }

        when (signMessageResult.result) {
            is Resource.Success -> {
                signMessageResult.result.data?.let { encryptedMessage ->
                    _signedMessage.value = signedMessage.value.copy(
                        text = encryptedMessage
                    )
                }
                _isSignMessageLoading.value = false
            }
            is Resource.Error -> {
                signMessageResult.result.message?.let { message ->
                    _signMessageEventChannel.send(UiEvent.ShowSnackbar(message))
                }
                _isSignMessageLoading.value = false
            }
            null -> {
                _isSignMessageLoading.value = false
            }
        }
    }
}