package com.xeniac.ethereumwalletdemo.feature_wallet.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBarsIgnoringVisibility
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.xeniac.ethereumwalletdemo.R
import com.xeniac.ethereumwalletdemo.core.data.repository.NetworkConnectivityObserver
import com.xeniac.ethereumwalletdemo.core.domain.repository.ConnectivityObserver
import com.xeniac.ethereumwalletdemo.core.ui.components.CustomBigButton
import com.xeniac.ethereumwalletdemo.core.ui.components.CustomOutlinedTextField
import com.xeniac.ethereumwalletdemo.core.ui.components.CustomSnackbar
import com.xeniac.ethereumwalletdemo.core.util.UiEvent
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CreateEthWalletScreen(
    onSignMessageNavigate: () -> Unit,
    viewModel: CreateEthWalletViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scrollSate = rememberScrollState()

    val walletFileDir = context.filesDir.absolutePath

    val passwordState by viewModel.password.collectAsStateWithLifecycle()
    val mnemonicPhraseState by viewModel.mnemonicPhrase.collectAsStateWithLifecycle()
    val walletAddressState by viewModel.walletAddress.collectAsStateWithLifecycle()
    val privateKeyState by viewModel.privateKey.collectAsStateWithLifecycle()

    val isConnectToEthBlockchainLoading by viewModel.isConnectToEthBlockchainLoading.collectAsStateWithLifecycle()
    val isCreateOfflineEthWalletLoading by viewModel.isCreateOfflineEthWalletLoading.collectAsStateWithLifecycle()
    val getFirstEthWalletInfoLoading by viewModel.getFirstEthWalletInfoLoading.collectAsStateWithLifecycle()

    val isRegenerateBtnLoading = isConnectToEthBlockchainLoading ||
            isCreateOfflineEthWalletLoading || getFirstEthWalletInfoLoading
    val isSignMessageBtnEnabled = privateKeyState.text.isNotBlank()

    val connectivityObserver by remember { mutableStateOf(NetworkConnectivityObserver(context)) }
    var networkStatus by remember { mutableStateOf(ConnectivityObserver.Status.UNAVAILABLE) }

    LaunchedEffect(key1 = context) {
        connectivityObserver.observe().onEach { status ->
            networkStatus = status
        }.launchIn(this)
    }

    LaunchedEffect(key1 = context) {
        viewModel.connectToEthBlockchainEventChannel.collectLatest { event ->
            when (event) {
                is UiEvent.GetEthWalletInfo -> {
                    viewModel.onEvent(CreateEthWalletEvent.GetFirstEthWalletInfo)
                }
                is UiEvent.ShowSnackbar -> {
                    val snackbarResult = snackbarHostState.showSnackbar(
                        message = event.message.asString(context),
                        actionLabel = context.getString(R.string.error_btn_retry),
                        duration = SnackbarDuration.Long
                    )

                    when (snackbarResult) {
                        SnackbarResult.ActionPerformed -> {
                            viewModel.onEvent(
                                CreateEthWalletEvent.ConnectToEthBlockchain(networkStatus)
                            )
                        }
                        SnackbarResult.Dismissed -> {
                            /* NO-OP */
                        }
                    }
                }
            }
        }
    }

    LaunchedEffect(key1 = context) {
        viewModel.createOfflineEthWalletEventChannel.collectLatest { event ->
            when (event) {
                is UiEvent.GetEthWalletInfo -> {
                    viewModel.onEvent(CreateEthWalletEvent.GetFirstEthWalletInfo)
                }
                is UiEvent.ShowSnackbar -> {
                    val snackbarResult = snackbarHostState.showSnackbar(
                        message = event.message.asString(context),
                        actionLabel = context.getString(R.string.error_btn_retry),
                        duration = SnackbarDuration.Long
                    )

                    when (snackbarResult) {
                        SnackbarResult.ActionPerformed -> {
                            viewModel.onEvent(
                                CreateEthWalletEvent.GenerateEthWallet(
                                    networkStatus = networkStatus,
                                    walletFileDir = walletFileDir
                                )
                            )
                        }
                        SnackbarResult.Dismissed -> {
                            /* NO-OP */
                        }
                    }
                }
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = {
            CustomSnackbar(
                snackbarHostState = snackbarHostState,
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets.navigationBarsIgnoringVisibility)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .windowInsetsPadding(WindowInsets.ime)
                .verticalScroll(scrollSate)
                .padding(
                    horizontal = 16.dp,
                    vertical = 24.dp
                )
        ) {
            CustomOutlinedTextField(
                value = passwordState.text,
                title = stringResource(id = R.string.create_eth_wallet_textfield_title_password),
                hint = stringResource(id = R.string.create_eth_wallet_textfield_hint_password),
                errorText = passwordState.errorText?.asString(),
                isPasswordTextField = true,
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done,
                onValueChange = { newPassword ->
                    viewModel.onEvent(CreateEthWalletEvent.PasswordChanged(newPassword.trim()))
                }
            )

            Spacer(Modifier.height(12.dp))

            CustomOutlinedTextField(
                isReadOnly = true,
                maxLines = 3,
                value = mnemonicPhraseState.text,
                title = stringResource(id = R.string.create_eth_wallet_textfield_title_mnemonic),
                hint = "",
                errorText = mnemonicPhraseState.errorText?.asString(),
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done,
                onValueChange = { /* NO-OP */ }
            )

            Spacer(Modifier.height(12.dp))

            CustomOutlinedTextField(
                isReadOnly = true,
                maxLines = 2,
                value = walletAddressState.text,
                title = stringResource(id = R.string.create_eth_wallet_textfield_title_wallet_address),
                hint = "",
                errorText = walletAddressState.errorText?.asString(),
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done,
                onValueChange = { /* NO-OP */ }
            )

            Spacer(Modifier.height(12.dp))

            CustomOutlinedTextField(
                isReadOnly = true,
                maxLines = 2,
                value = privateKeyState.text,
                title = stringResource(id = R.string.create_eth_wallet_textfield_title_private_key),
                hint = "",
                errorText = privateKeyState.errorText?.asString(),
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done,
                onValueChange = { /* NO-OP */ }
            )

            Spacer(Modifier.height(24.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                CustomBigButton(
                    btnText = stringResource(id = R.string.create_eth_wallet_btn_regenerate),
                    isLoading = isRegenerateBtnLoading,
                    onClick = {
                        viewModel.onEvent(
                            CreateEthWalletEvent.GenerateEthWallet(
                                networkStatus = networkStatus,
                                walletFileDir = walletFileDir
                            )
                        )
                    }
                )

                Spacer(Modifier.width(4.dp))

                CustomBigButton(
                    btnText = stringResource(id = R.string.create_eth_wallet_btn_sign_message),
                    isLoading = false,
                    isEnabled = isSignMessageBtnEnabled,
                    onClick = onSignMessageNavigate
                )
            }
        }
    }
}