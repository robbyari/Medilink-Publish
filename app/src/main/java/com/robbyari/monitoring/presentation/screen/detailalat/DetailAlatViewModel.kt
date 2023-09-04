package com.robbyari.monitoring.presentation.screen.detailalat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.robbyari.monitoring.domain.model.Alat
import com.robbyari.monitoring.domain.model.Response
import com.robbyari.monitoring.domain.repository.MonitoringRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailAlatViewModel @Inject constructor(
    private val repo: MonitoringRepository
) : ViewModel() {

    private val _detail = MutableStateFlow<Response<Alat>>(Response.Loading)
    val detail: StateFlow<Response<Alat>> = _detail

    suspend fun getDetail(id: String) {
        viewModelScope.launch {
            try {
                repo.getDetailAlat(id)
                    .collect { response ->
                        _detail.value = response
                    }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}