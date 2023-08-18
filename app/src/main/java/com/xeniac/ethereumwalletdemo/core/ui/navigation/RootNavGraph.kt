package com.xeniac.ethereumwalletdemo.core.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.xeniac.ethereumwalletdemo.feature_sign.presentation.SignMessageScreen
import com.xeniac.ethereumwalletdemo.feature_wallet.presentation.CreateEthWalletScreen

@Composable
fun SetupRootNavGraph(
    navController: NavHostController,
    startDestination: String
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        route = NavGraphs.ROOT_ROUTE
    ) {
        composable(route = Screen.CreateEthWalletScreen.route) {
            CreateEthWalletScreen(
                onSignMessageNavigate = {
                    navController.navigate(Screen.SignMessageScreen.route)
                }
            )
        }

        composable(route = Screen.SignMessageScreen.route) {
            SignMessageScreen(onNavigateUp = navController::navigateUp)
        }
    }
}

@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(
    navController: NavHostController
): T {
    val navGraphRoute = destination.parent?.route ?: return hiltViewModel()
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return hiltViewModel(parentEntry)
}

object NavGraphs {
    const val ROOT_ROUTE = "root"
}