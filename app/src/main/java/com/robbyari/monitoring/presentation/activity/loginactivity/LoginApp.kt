package com.robbyari.monitoring.presentation.activity.loginactivity

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.robbyari.monitoring.presentation.navigation.Screen
import com.robbyari.monitoring.presentation.screen.login.LoginScreen
import com.robbyari.monitoring.presentation.screen.register.RegisterScreen

@Composable
fun LoginApp(
    modifier: Modifier = Modifier,
    navigateMainActivity: () -> Unit,
    navigateToUserActivity: () -> Unit,
    navController: NavHostController = rememberNavController(),
) {
    Scaffold(
        bottomBar = { },
        modifier = modifier,
        containerColor = Color.Transparent,
        contentWindowInsets = WindowInsets(top = 0.dp)
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Login.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(
                Screen.Login.route
            ) {
                LoginScreen(
                    navigateToHome = {
                        navigateMainActivity()
                    },
                    navigateToRegister = {
                        navController.navigate(Screen.Register.route)
                    },
                    navigateToUser = {
                        navigateToUserActivity()
                    }
                )
            }
            composable(
                Screen.Register.route
            ) {
                RegisterScreen(
                    navigateBack = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}