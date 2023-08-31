package com.robbyari.monitoring.presentation.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Login : Screen("login")
    object DayChecking : Screen("daychecking/{id}") {
        fun createRoute(id: String?) = "daychecking/${id}"
    }
    object MonthChecking : Screen("monthchecking/{id}") {
        fun createRoute(id: String?) = "monthchecking/${id}"
    }
    object CalibrationChecking : Screen("calibrationchecking/{id}") {
        fun createRoute(id: String?) = "calibrationchecking/${id}"
    }
    object Repair : Screen("repair/{idReportProblem}") {
        fun createRoute(idReportProblem: String?) = "repair/${idReportProblem}"
    }
    object All : Screen("all/{id}") {
        fun createRoute(id: String?) = "all/${id}"
    }
}
