package com.robbyari.monitoring.presentation.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Login : Screen("login")
    object DayChecking : Screen("daychecking/{id}") {
        fun createRoute(id: String?) = "daychecking/${id}"
    }
}
