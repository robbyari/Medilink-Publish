package com.robbyari.monitoring.presentation.screen.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.robbyari.monitoring.domain.model.Response
import com.robbyari.monitoring.domain.model.User
import com.robbyari.monitoring.domain.repository.MonitoringRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repo: MonitoringRepository
) : ViewModel() {

    private val _loginResponse: MutableStateFlow<Response<User>> = MutableStateFlow(Response.Loading)
    val loginResponse: StateFlow<Response<User>> = _loginResponse

    fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            try {
                val response = repo.loginUser(email, password).first()
                _loginResponse.value = response
                Log.d("Cek", "loginUser: $response")
            } catch (e: Exception) {
                _loginResponse.value = Response.Failure(e)
            }
        }
    }

}