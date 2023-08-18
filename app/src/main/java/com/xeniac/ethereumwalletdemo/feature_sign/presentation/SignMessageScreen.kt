package com.xeniac.ethereumwalletdemo.feature_sign.presentation

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import com.xeniac.ethereumwalletdemo.core.ui.components.CustomBigButton
import com.xeniac.ethereumwalletdemo.core.ui.components.CustomOutlinedTextField
import com.xeniac.ethereumwalletdemo.core.ui.components.CustomSnackbar
import com.xeniac.ethereumwalletdemo.core.util.UiEvent
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SignMessageScreen(
    privateKey: String,
    onNavigateUp: () -> Unit,
    viewModel: SignMessageViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scrollSate = rememberScrollState()

    val messageState by viewModel.message.collectAsStateWithLifecycle()
    val signedMessageState by viewModel.signedMessage.collectAsStateWithLifecycle()

    val isSignMessageLoading by viewModel.isSignMessageLoading.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = context) {
        viewModel.signMessageEventChannel.collectLatest { event ->
            when (event) {
                is UiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(
                        message = event.message.asString(context),
                        duration = SnackbarDuration.Long
                    )
                }
                else -> {
                    /* NO-OP */
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
                value = messageState.text,
                title = stringResource(id = R.string.sign_message_textfield_title_message),
                hint = stringResource(id = R.string.sign_message_textfield_hint_message_blank),
                errorText = messageState.errorText?.asString(),
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done,
                onValueChange = { newMessage ->
                    viewModel.onEvent(SignMessageEvent.MessageChanged(newMessage.trim()))
                },
                keyboardAction = {
                    viewModel.onEvent(SignMessageEvent.SignMessageWithPrivateKey(privateKey))
                }
            )

            Spacer(Modifier.height(12.dp))

            CustomOutlinedTextField(
                isReadOnly = true,
                maxLines = 3,
                value = privateKey,
                title = stringResource(id = R.string.sign_message_textfield_title_private_key),
                hint = "",
                errorText = null,
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.None,
                onValueChange = { /* NO-OP */ }
            )

            Spacer(Modifier.height(12.dp))

            CustomOutlinedTextField(
                isReadOnly = true,
                maxLines = 3,
                value = signedMessageState.text,
                title = stringResource(id = R.string.sign_message_textfield_title_signed_message),
                hint = "",
                errorText = signedMessageState.errorText?.asString(),
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.None,
                onValueChange = { /* NO-OP */ }
            )

            Spacer(Modifier.height(24.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                CustomBigButton(
                    btnText = stringResource(id = R.string.sign_message_btn_sign_message),
                    isLoading = isSignMessageLoading,
                    onClick = {
                        viewModel.onEvent(SignMessageEvent.SignMessageWithPrivateKey(privateKey))
                    }
                )

                Spacer(Modifier.width(4.dp))

                CustomBigButton(
                    btnText = stringResource(id = R.string.sign_message_btn_return),
                    isLoading = false,
                    onClick = onNavigateUp
                )
            }
        }
    }
}