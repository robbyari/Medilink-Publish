package com.robbyari.monitoring.presentation.screen.repair

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.robbyari.monitoring.domain.model.ReportProblem
import com.robbyari.monitoring.domain.model.Response
import com.robbyari.monitoring.domain.model.User
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

    private val _userDataStore = MutableStateFlow(User())
    val userDataStore: StateFlow<User> = _userDataStore

    private val _detail = MutableStateFlow<Response<ReportProblem>>(Response.Loading)
    val detail: StateFlow<Response<ReportProblem>> = _detail

    private val _updateToReportProblem = MutableStateFlow<Response<Boolean>>(Response.Loading)
    val updateToReportProblem: StateFlow<Response<Boolean>> = _updateToReportProblem

    init {
        viewModelScope.launch {
            getUserDataStore()
        }
    }

    suspend fun addImageToStorage(imageUri: Uri): String {
        return repo.addImageToFirebaseStorage(imageUri)
    }

    suspend fun updateToReportProblem(item: ReportProblem) {
        viewModelScope.launch {
            try {
                repo.updateToReportProblem(item)
                    .collect {
                        _updateToReportProblem.value = it
                    }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private suspend fun getUserDataStore() {
        _userDataStore.value = repo.getUserDataStore()
    }

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