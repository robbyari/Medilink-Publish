package com.robbyari.monitoring.presentation.screen.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.robbyari.monitoring.domain.model.ReportProblem
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
class UserViewModel @Inject constructor(
    private val repo: MonitoringRepository
) : ViewModel() {
    private val _userDataStore = MutableStateFlow(User())
    val userDataStore: StateFlow<User> = _userDataStore

    private val _reportProblem = MutableStateFlow<Response<List<ReportProblem>>>(Response.Loading)
    val reportProblem: StateFlow<Response<List<ReportProblem>>> = _reportProblem

    private val _barcodeResult = MutableStateFlow<Response<String>>(Response.Loading)
    val barcodeResult: StateFlow<Response<String>> = _barcodeResult

    init {
        viewModelScope.launch {
            getUserDataStore()
            fetchReportProblem()
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

    private suspend fun fetchReportProblem() {
        _reportProblem.value = Response.Loading
        try {
            repo.getReportProblem().collectLatest {
                _reportProblem.emit(it)
            }
        } catch (e: Exception) {
            _reportProblem.emit(Response.Failure(e))
        }
    }

    private suspend fun getUserDataStore() {
        _userDataStore.value = repo.getUserDataStore()
    }
}