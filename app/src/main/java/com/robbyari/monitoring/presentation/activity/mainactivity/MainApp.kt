package com.robbyari.monitoring.presentation.activity.mainactivity

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.robbyari.monitoring.presentation.navigation.Screen
import com.robbyari.monitoring.presentation.screen.calibrationchecking.CalibrationCheckingScreen
import com.robbyari.monitoring.presentation.screen.daychecking.DayCheckingScreen
import com.robbyari.monitoring.presentation.screen.home.HomeScreen
import com.robbyari.monitoring.presentation.screen.monthchecking.MonthCheckingScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainApp(
    location: String?,
    modifier: Modifier = Modifier,
    isDistanceGreaterThan100Meters: Boolean,
    navController: NavHostController = rememberNavController(),
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route


    Scaffold(
        modifier = modifier,
        containerColor = Color.Transparent,
        contentWindowInsets = WindowInsets(top = 0.dp),
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                HomeScreen(
                    navigateToDayChecking = { id ->
                        navController.navigate(Screen.DayChecking.createRoute(id))
                    },
                    navigateToMonthChecking = { id ->
                        navController.navigate(Screen.MonthChecking.createRoute(id))
                    },
                    navigateToCalibrationChecking = { id ->
                        navController.navigate(Screen.CalibrationChecking.createRoute(id))
                    }
                )
            }
            composable(
                route = Screen.DayChecking.route,
                arguments = listOf(navArgument("id") { type = NavType.StringType })
            ) {
                val id = it.arguments?.getString("id") ?: ""
                DayCheckingScreen(
                    id = id,
                    location = location,
                    isDistanceGreaterThan100Meters = isDistanceGreaterThan100Meters,
                    navigateBack = { navController.popBackStack() },
                    backHandler = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Home.route) {
                                inclusive = true
                            }
                        }
                    }
                )
            }
            composable(
                route = Screen.MonthChecking.route,
                arguments = listOf(navArgument("id") { type = NavType.StringType })
            ) {
                val id = it.arguments?.getString("id") ?: ""
                MonthCheckingScreen(
                    id = id,
                    location = location,
                    isDistanceGreaterThan100Meters = isDistanceGreaterThan100Meters,
                    navigateBack = { navController.popBackStack() },
                    backHandler = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Home.route) {
                                inclusive = true
                            }
                        }
                    }
                )
            }
            composable(
                route = Screen.CalibrationChecking.route,
                arguments = listOf(navArgument("id") { type = NavType.StringType })
            ) {
                val id = it.arguments?.getString("id") ?: ""
                CalibrationCheckingScreen(
                    id = id,
                    location = location,
                    isDistanceGreaterThan100Meters = isDistanceGreaterThan100Meters,
                    navigateBack = { navController.popBackStack() },
                    backHandler = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Home.route) {
                                inclusive = true
                            }
                        }
                    }
                )
            }
        }
    }
}