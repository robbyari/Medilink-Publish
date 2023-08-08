package com.robbyari.monitoring.presentation.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Login : Screen("login")
}
