package com.robbyari.monitoring.presentation.screen.report

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.robbyari.monitoring.domain.model.Alat
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
class ReportViewModel @Inject constructor(
    private val repo: MonitoringRepository
) : ViewModel() {

    private val _detailAlat = MutableStateFlow<Response<Alat>>(Response.Loading)
    val detailAlat: StateFlow<Response<Alat>> = _detailAlat

    private val _userDataStore = MutableStateFlow(User())
    val userDataStore: StateFlow<User> = _userDataStore

    private val _addToReportProblem = MutableStateFlow<Response<Boolean>>(Response.Loading)
    val addToReportProblem: StateFlow<Response<Boolean>> = _addToReportProblem

    init {
        viewModelScope.launch {
            getUserDataStore()
        }
    }

    suspend fun fetchDetailAlat(id: String?) {
        _detailAlat.value = Response.Loading
        try {
            repo.getDetailAlat(id!!).collectLatest {
                _detailAlat.emit(it)
            }
        } catch (e: Exception) {
            _detailAlat.emit(Response.Failure(e))
        }
    }

    suspend fun addToReportProblem(idDocument: String, item: ReportProblem) {
        viewModelScope.launch {
            try {
                repo.addToReportProblem(idDocument, item)
                    .collect {
                        _addToReportProblem.value = it
                    }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private suspend fun getUserDataStore() {
        _userDataStore.value = repo.getUserDataStore()
    }
}