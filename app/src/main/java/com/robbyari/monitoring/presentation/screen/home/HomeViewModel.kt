package com.robbyari.monitoring.presentation.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.robbyari.monitoring.domain.model.Alat
import com.robbyari.monitoring.domain.model.Response
import com.robbyari.monitoring.domain.model.User
import com.robbyari.monitoring.domain.repository.MonitoringRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
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

    private val _dailyCheck = MutableStateFlow<Response<List<Alat>>>(Response.Loading)
    val dailyCheck: StateFlow<Response<List<Alat>>> = _dailyCheck

    private val _barcodeResult = MutableStateFlow<Response<String>>(Response.Loading)
    val barcodeResult: StateFlow<Response<String>> = _barcodeResult

    init {
        viewModelScope.launch {
            getEmail()
            fetchDailyCheck()
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

    private suspend fun fetchDailyCheck() {
        _dailyCheck.value = Response.Loading
        try {
            repo.getDailyCheck().collectLatest {
                _dailyCheck.emit(it)
            }
        } catch (e: Exception) {
            _dailyCheck.emit(Response.Failure(e))
        }
    }

    suspend fun startScan() {
        try {
            repo.getBarcodeText().collectLatest {
                _barcodeResult.emit(it)
            }
        } catch (e: Exception) {
            _barcodeResult.emit(Response.Failure(e))
        }
    }

}