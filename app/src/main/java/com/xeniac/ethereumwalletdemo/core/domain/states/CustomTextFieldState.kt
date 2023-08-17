package com.xeniac.ethereumwalletdemo.core.domain.states

import com.xeniac.ethereumwalletdemo.core.util.UiText

data class CustomTextFieldState(
    val text: String = "",
    val isValid: Boolean = false,
    val errorText: UiText? = null
)
