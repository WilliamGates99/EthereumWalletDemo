package com.xeniac.ethereumwalletdemo.core.presentation

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.xeniac.ethereumwalletdemo.core.ui.navigation.Screen
import com.xeniac.ethereumwalletdemo.core.ui.navigation.SetupRootNavGraph
import com.xeniac.ethereumwalletdemo.core.ui.theme.EthereumWalletDemoTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        installSplashScreen()

        setContent {
            EthereumWalletDemoTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    SetupRootNavGraph(
                        navController = navController,
                        startDestination = Screen.CreateWalletScreen.route
                    )
                }
            }
        }
    }
}