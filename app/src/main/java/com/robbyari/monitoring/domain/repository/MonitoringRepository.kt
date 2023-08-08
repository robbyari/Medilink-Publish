package com.robbyari.monitoring.domain.repository

import com.robbyari.monitoring.domain.model.Response
import com.robbyari.monitoring.domain.model.User
import kotlinx.coroutines.flow.Flow

interface MonitoringRepository {
    suspend fun loginUser(email: String, password: String): Flow<Response<User>>
}