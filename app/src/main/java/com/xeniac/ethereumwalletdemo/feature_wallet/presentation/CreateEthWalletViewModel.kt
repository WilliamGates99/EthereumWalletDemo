package com.xeniac.ethereumwalletdemo.feature_wallet.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xeniac.ethereumwalletdemo.R
import com.xeniac.ethereumwalletdemo.core.domain.repository.ConnectivityObserver
import com.xeniac.ethereumwalletdemo.core.domain.states.CustomTextFieldState
import com.xeniac.ethereumwalletdemo.core.util.Resource
import com.xeniac.ethereumwalletdemo.core.util.ResultError
import com.xeniac.ethereumwalletdemo.core.util.UiEvent
import com.xeniac.ethereumwalletdemo.core.util.UiText
import com.xeniac.ethereumwalletdemo.feature_wallet.domain.repository.EthWalletRepository
import com.xeniac.ethereumwalletdemo.feature_wallet.domain.use_case.CreateEthWalletUseCase
import com.xeniac.ethereumwalletdemo.feature_wallet.domain.util.ValidatePassword
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateEthWalletViewModel @Inject constructor(
    private val ethWalletRepository: EthWalletRepository,
    private val createEthWalletUseCase: CreateEthWalletUseCase
) : ViewModel() {

    private val _password = MutableStateFlow(CustomTextFieldState())
    val password = _password.asStateFlow()

    private val _mnemonicPhrase = MutableStateFlow(CustomTextFieldState())
    val mnemonicPhrase = _mnemonicPhrase.asStateFlow()

    private val _walletAddress = MutableStateFlow(CustomTextFieldState())
    val walletAddress = _walletAddress.asStateFlow()

    private val _privateKey = MutableStateFlow(CustomTextFieldState())
    val privateKey = _privateKey.asStateFlow()

    private val _isCreateOfflineEthWalletLoading = MutableStateFlow(false)
    val isCreateOfflineEthWalletLoading = _isCreateOfflineEthWalletLoading.asStateFlow()

    private val _getFirstEthWalletInfoLoading = MutableStateFlow(false)
    val getFirstEthWalletInfoLoading = _getFirstEthWalletInfoLoading.asStateFlow()

    private val _createOfflineEthWalletEventChannel = Channel<UiEvent>()
    val createOfflineEthWalletEventChannel = _createOfflineEthWalletEventChannel.receiveAsFlow()

    init {
        onEvent(CreateEthWalletEvent.GetFirstEthWalletInfo)
    }

    fun onEvent(event: CreateEthWalletEvent) {
        when (event) {
            is CreateEthWalletEvent.PasswordChanged -> {
                _password.value = password.value.copy(text = event.password)

                val resultError = ValidatePassword().invoke(password.value.text)
                if (resultError == null) {
                    _password.value = password.value.copy(
                        isValid = true,
                        errorText = null
                    )
                } else {
                    _password.value = password.value.copy(
                        isValid = false
                    )
                }
            }
            is CreateEthWalletEvent.GenerateEthWallet -> createOfflineEthWallet(
                event.networkStatus, event.walletFileDir
            )
            is CreateEthWalletEvent.GetFirstEthWalletInfo -> getFirstEthWalletInfo()
        }
    }

    private fun createOfflineEthWallet(
        networkStatus: ConnectivityObserver.Status,
        walletFileDir: String
    ) = viewModelScope.launch {
        if (networkStatus == ConnectivityObserver.Status.AVAILABLE) {
            _isCreateOfflineEthWalletLoading.value = true

            val createEthWalletResult = createEthWalletUseCase(
                password = password.value.text,
                walletFileDir = walletFileDir
            )

            if (createEthWalletResult.passwordResultError != null) {
                when (createEthWalletResult.passwordResultError) {
                    is ResultError.BlankField -> {
                        _password.value = password.value.copy(
                            errorText = UiText.StringResource(R.string.create_eth_wallet_textfield_error_password_blank)
                        )
                    }
                    is ResultError.ShortPassword -> {
                        _password.value = password.value.copy(
                            errorText = UiText.StringResource(R.string.create_eth_wallet_textfield_error_password_short)
                        )
                    }
                    is ResultError.InvalidPassword -> {
                        _password.value = password.value.copy(
                            errorText = UiText.StringResource(R.string.create_eth_wallet_textfield_error_password_invalid)
                        )
                    }
                }
            }

            when (createEthWalletResult.result) {
                is Resource.Success -> {
                    _createOfflineEthWalletEventChannel.send(UiEvent.GetEthWalletInfo)
                    _isCreateOfflineEthWalletLoading.value = false
                }
                is Resource.Error -> {
                    createEthWalletResult.result.message?.let { message ->
                        _createOfflineEthWalletEventChannel.send(UiEvent.ShowSnackbar(message))
                    }
                    _isCreateOfflineEthWalletLoading.value = false
                }
                null -> {
                    _isCreateOfflineEthWalletLoading.value = false
                }
            }
        } else {
            _createOfflineEthWalletEventChannel.send(
                UiEvent.ShowSnackbar(
                    UiText.StringResource(R.string.error_network_connection_unavailable)
                )
            )
        }
    }

    private fun getFirstEthWalletInfo() = viewModelScope.launch {
        _getFirstEthWalletInfoLoading.value = true
        when (val result = ethWalletRepository.getFirstEthWalletInfo()) {
            is Resource.Success -> {
                result.data?.let { ethWalletInfo ->
                    _mnemonicPhrase.value = mnemonicPhrase.value.copy(
                        text = ethWalletInfo.mnemonicPhrase
                    )
                    _walletAddress.value = walletAddress.value.copy(
                        text = ethWalletInfo.address
                    )
                    _privateKey.value = privateKey.value.copy(
                        text = ethWalletInfo.privateKey
                    )
                }
                _getFirstEthWalletInfoLoading.value = false
            }
            is Resource.Error -> {
                result.message?.let { message ->
                    _createOfflineEthWalletEventChannel.send(UiEvent.ShowSnackbar(message))
                }
                _getFirstEthWalletInfoLoading.value = false
            }
        }
    }
}