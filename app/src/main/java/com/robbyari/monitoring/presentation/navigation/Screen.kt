package com.robbyari.monitoring.presentation.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Login : Screen("login")
    object Register: Screen("register")
    object User : Screen("user")
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
    object DetailAlat : Screen("detailalat/{id}") {
        fun createRoute(id: String?) = "detailalat/${id}"
    }
    object Report : Screen("report/{id}") {
        fun createRoute(id: String?) = "report/${id}"
    }
    object Account : Screen("account/{id}") {
        fun createRoute(id: String?) = "account/${id}"
    }
}
