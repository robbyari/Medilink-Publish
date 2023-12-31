package com.robbyari.monitoring.presentation.activity.mainactivity

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.robbyari.monitoring.presentation.navigation.Screen
import com.robbyari.monitoring.presentation.screen.account.AccountScreen
import com.robbyari.monitoring.presentation.screen.all.AllScreen
import com.robbyari.monitoring.presentation.screen.calibrationchecking.CalibrationCheckingScreen
import com.robbyari.monitoring.presentation.screen.daychecking.DayCheckingScreen
import com.robbyari.monitoring.presentation.screen.detailalat.DetailAlatScreen
import com.robbyari.monitoring.presentation.screen.home.HomeScreen
import com.robbyari.monitoring.presentation.screen.monthchecking.MonthCheckingScreen
import com.robbyari.monitoring.presentation.screen.repair.RepairScreen

@Composable
fun MainApp(
    modifier: Modifier = Modifier,
    isDistanceGreaterThan100Meters: Boolean,
    navController: NavHostController = rememberNavController(),
) {
    val systemUiController = rememberSystemUiController()

    SideEffect {
        systemUiController.setSystemBarsColor(Color.Transparent, darkIcons = true)
        systemUiController.setNavigationBarColor(Color.Black)
    }

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
                    },
                    navigateToRepairScreen = { idReportProblem ->
                        navController.navigate(Screen.Repair.createRoute(idReportProblem))
                    },
                    navigateToAllScreen = { id ->
                        navController.navigate(Screen.All.createRoute(id))
                    },
                    navigateToDetailAlat = { id ->
                        navController.navigate(Screen.DetailAlat.createRoute(id))
                    },
                    navigateToAccountScreen = {id ->
                        navController.navigate(Screen.Account.createRoute(id))
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
                    isDistanceGreaterThan100Meters = isDistanceGreaterThan100Meters,
                    navigateBack = { navController.popBackStack() },
                )
            }
            composable(
                route = Screen.MonthChecking.route,
                arguments = listOf(navArgument("id") { type = NavType.StringType })
            ) {
                val id = it.arguments?.getString("id") ?: ""
                MonthCheckingScreen(
                    id = id,
                    isDistanceGreaterThan100Meters = isDistanceGreaterThan100Meters,
                    navigateBack = { navController.popBackStack() },
                )
            }
            composable(
                route = Screen.CalibrationChecking.route,
                arguments = listOf(navArgument("id") { type = NavType.StringType })
            ) {
                val id = it.arguments?.getString("id") ?: ""
                CalibrationCheckingScreen(
                    id = id,
                    isDistanceGreaterThan100Meters = isDistanceGreaterThan100Meters,
                    navigateBack = { navController.popBackStack() }
                )
            }
            composable(
                route = Screen.Repair.route,
                arguments = listOf(
                    navArgument("idReportProblem") { type = NavType.StringType }
                )
            ) {
                val idReportProblem = it.arguments?.getString("idReportProblem") ?: ""
                RepairScreen(
                    idReportProblem = idReportProblem,
                    isDistanceGreaterThan100Meters = isDistanceGreaterThan100Meters,
                    navigateBack = { navController.popBackStack() }
                )
            }
            composable(
                route = Screen.All.route,
                arguments = listOf(
                    navArgument("id") {type = NavType.StringType}
                )
            ) {
                val arg = it.arguments?.getString("id")
                AllScreen(
                    id = arg,
                    navigateBack = {navController.popBackStack()},
                    navigateToDayChecking = { id ->
                        navController.navigate(Screen.DayChecking.createRoute(id))
                    },
                    navigateToMonthChecking = { id ->
                        navController.navigate(Screen.MonthChecking.createRoute(id))
                    },
                    navigateToCalibrationChecking = { id ->
                        navController.navigate(Screen.CalibrationChecking.createRoute(id))
                    },
                    navigateToRepairScreen = { idReportProblem ->
                        navController.navigate(Screen.Repair.createRoute(idReportProblem))
                    },
                    navigateToDetailAlat = { id ->
                        navController.navigate(Screen.DetailAlat.createRoute(id))
                    }
                )
            }
            composable(
                route = Screen.DetailAlat.route,
                arguments = listOf(
                    navArgument("id") {type = NavType.StringType}
                )
            ) {
                val arg = it.arguments?.getString("id")
                DetailAlatScreen(
                    id = arg,
                    navigateBack = {navController.popBackStack()}
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