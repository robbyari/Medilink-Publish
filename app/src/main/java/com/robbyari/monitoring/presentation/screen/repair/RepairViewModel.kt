package com.robbyari.monitoring.presentation.screen.repair

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.robbyari.monitoring.domain.model.ReportProblem
import com.robbyari.monitoring.domain.model.Response
import com.robbyari.monitoring.domain.repository.MonitoringRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RepairViewModel @Inject constructor(
    private val repo: MonitoringRepository
) : ViewModel() {
    private val _detail = MutableStateFlow<Response<ReportProblem>>(Response.Loading)
    val detail: StateFlow<Response<ReportProblem>> = _detail

    fun getDetail(id: String?) {
        viewModelScope.launch {
            if (id != null) {
                try {
                    repo.getDetailReportProblem(id)
                        .collect { response ->
                            _detail.value = response
                        }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}