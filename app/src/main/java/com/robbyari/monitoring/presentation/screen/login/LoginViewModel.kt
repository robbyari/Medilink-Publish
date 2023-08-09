package com.robbyari.monitoring.presentation.screen.login

import androidx.lifecycle.ViewModel
import com.robbyari.monitoring.domain.repository.MonitoringRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repo: MonitoringRepository
) : ViewModel() {

    suspend fun loginUser(email: String, password: String): Boolean {
        val isLoginSuccessful = repo.loginUser(email, password)
        if (isLoginSuccessful) {
            repo.setEmail(email)
        }
        return isLoginSuccessful
    }

}