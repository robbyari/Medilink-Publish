package com.robbyari.monitoring.domain.model

data class User(
    val uid: String? = "",
    val firstName: String? = "",
    val lastName: String? = "",
    val email: String? = "",
    val password: String? = "",
    val photoUrl: String? = "",
    val role: String? = "",
    val divisi: String? = "",
    val status: Boolean? = false
)
