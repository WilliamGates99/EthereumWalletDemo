package com.xeniac.ethereumwalletdemo.core.ui.navigation

sealed class Screen(val route: String) {
    data object CreateWalletScreen : Screen("create_wallet_screen")
    data object SignMessageScreen : Screen("sign_message_screen")

    fun withArgs(vararg args: Any): String {
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }
}