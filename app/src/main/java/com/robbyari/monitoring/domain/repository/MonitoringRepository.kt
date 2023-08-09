package com.robbyari.monitoring.domain.repository

import com.robbyari.monitoring.domain.model.Response
import com.robbyari.monitoring.domain.model.User

interface MonitoringRepository {
    suspend fun loginUser(email: String, password: String): Boolean
    suspend fun setEmail(name: String): Response<Unit>
    suspend fun getEmail(): String
    suspend fun getUser(email: String): User
}