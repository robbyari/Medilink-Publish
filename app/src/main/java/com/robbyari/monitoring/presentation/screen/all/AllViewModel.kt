package com.robbyari.monitoring.presentation.screen.all

import androidx.lifecycle.ViewModel
import com.robbyari.monitoring.domain.model.Alat
import com.robbyari.monitoring.domain.model.ReportProblem
import com.robbyari.monitoring.domain.model.Response
import com.robbyari.monitoring.domain.repository.MonitoringRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@HiltViewModel
class AllViewModel @Inject constructor(
    private val repo: MonitoringRepository
) : ViewModel() {
    private val _reportProblemCheck = MutableStateFlow<Response<List<ReportProblem>>>(Response.Loading)
    val reportProblemCheck: StateFlow<Response<List<ReportProblem>>> = _reportProblemCheck

    private val _dailyCheck = MutableStateFlow<Response<List<Alat>>>(Response.Loading)
    val dailyCheck: StateFlow<Response<List<Alat>>> = _dailyCheck

    private val _monthlyCheck = MutableStateFlow<Response<List<Alat>>>(Response.Loading)
    val monthlyCheck: StateFlow<Response<List<Alat>>> = _monthlyCheck

    private val _calibrationCheck = MutableStateFlow<Response<List<Alat>>>(Response.Loading)
    val calibrationCheck: StateFlow<Response<List<Alat>>> = _calibrationCheck

    private val _listAlat = MutableStateFlow<Response<List<Alat>>>(Response.Loading)
    val listAlat: StateFlow<Response<List<Alat>>> = _listAlat

    suspend fun fetchListAlat() {
        _listAlat.value = Response.Loading
        try {
            repo.getListAlat().collectLatest {
                _listAlat.emit(it)
            }
        } catch (e: Exception) {
            _listAlat.emit(Response.Failure(e))
        }
    }

    suspend fun fetchReportProblem() {
        _reportProblemCheck.value = Response.Loading
        try {
            repo.getReportProblem().collectLatest {
                _reportProblemCheck.emit(it)
            }
        } catch (e: Exception) {
            _reportProblemCheck.emit(Response.Failure(e))
        }
    }

    suspend fun fetchDailyCheck() {
        _dailyCheck.value = Response.Loading
        try {
            repo.getDailyCheck().collectLatest {
                _dailyCheck.emit(it)
            }
        } catch (e: Exception) {
            _dailyCheck.emit(Response.Failure(e))
        }
    }

    suspend fun fetchMonthlyCheck() {
        _monthlyCheck.value = Response.Loading
        try {
            repo.getMonthlyCheck().collectLatest {
                _monthlyCheck.emit(it)
            }
        } catch (e: Exception) {
            _monthlyCheck.emit(Response.Failure(e))
        }
    }

    suspend fun fetchCalibrationCheck() {
        _calibrationCheck.value = Response.Loading
        try {
            repo.getCalibrationCheck().collectLatest {
                _calibrationCheck.emit(it)
            }
        } catch (e: Exception) {
            _calibrationCheck.emit(Response.Failure(e))
        }
    }
}