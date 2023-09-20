package com.robbyari.monitoring.domain.model

data class User(
    val uid: String? = "",
    val name: String? = "",
    val email: String? = "",
    val password: String? = "",
    val photoUrl: String? = "",
    val role: String? = "",
    val status: Boolean? = false
)
