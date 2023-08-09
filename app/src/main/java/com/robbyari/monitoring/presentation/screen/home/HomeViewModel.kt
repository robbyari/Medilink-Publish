package com.robbyari.monitoring.presentation.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.robbyari.monitoring.domain.model.Response
import com.robbyari.monitoring.domain.model.User
import com.robbyari.monitoring.domain.repository.MonitoringRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repo: MonitoringRepository
) : ViewModel() {

    private val _user = MutableStateFlow<Response<User>>(Response.Loading)
    val user: StateFlow<Response<User>> = _user

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    init {
        viewModelScope.launch {
            getEmail()
        }
    }
    private suspend fun getEmail() {
        _email.value = repo.getEmail()
    }
    suspend fun getUser(email: String) {
        try {
            val result = repo.getUser(email)
            _user.emit(Response.Success(result))
        } catch (e: Exception) {
            _user.emit(Response.Failure(e))
        }
    }

}