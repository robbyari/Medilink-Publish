package com.robbyari.monitoring.presentation.screen.register

import androidx.lifecycle.ViewModel
import com.robbyari.monitoring.domain.model.User
import com.robbyari.monitoring.domain.repository.MonitoringRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val repo: MonitoringRepository
) : ViewModel() {
    suspend fun registerUser(user: User): Boolean {
        return repo.registerUser(user)
    }
}