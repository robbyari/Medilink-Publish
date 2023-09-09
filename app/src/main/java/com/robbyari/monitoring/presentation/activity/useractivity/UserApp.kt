package com.robbyari.monitoring.presentation.activity.useractivity

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.robbyari.monitoring.presentation.navigation.Screen
import com.robbyari.monitoring.presentation.screen.account.AccountScreen
import com.robbyari.monitoring.presentation.screen.report.ReportScreen
import com.robbyari.monitoring.presentation.screen.user.UserScreen

@Composable
fun UserApp(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        bottomBar = { },
        modifier = modifier,
        containerColor = Color.Transparent,
        contentWindowInsets = WindowInsets(top = 0.dp)
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.User.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.User.route) {
                UserScreen(
                    navigateToReportScreen = { id ->
                        navController.navigate(Screen.Report.createRoute(id))
                    },
                    navigateToAccountScreen = { id ->
                        navController.navigate(Screen.Account.createRoute(id))
                    }
                )
            }
            composable(
                route = Screen.Report.route,
                arguments = listOf(navArgument("id") { type = NavType.StringType })
            ) {
                val arg = it.arguments?.getString("id")
                ReportScreen(
                    id = arg,
                    navigateBack = { navController.popBackStack() }
                )
            }
            composable(
                route = Screen.Account.route,
                arguments = listOf(navArgument("id") { type = NavType.StringType })
            ) {
                val arg = it.arguments?.getString("id")
                AccountScreen(
                    id = arg,
                    navigateBack = { navController.popBackStack() },
                )
            }
        }
    }
}