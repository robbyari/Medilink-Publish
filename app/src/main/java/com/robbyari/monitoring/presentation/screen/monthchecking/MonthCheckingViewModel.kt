package com.robbyari.monitoring.presentation.screen.monthchecking

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.robbyari.monitoring.domain.model.Alat
import com.robbyari.monitoring.domain.model.Checking
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
class MonthCheckingViewModel @Inject constructor(
    private val repo: MonitoringRepository
) : ViewModel() {

    private val _userDataStore = MutableStateFlow(User())
    val userDataStore: StateFlow<User> = _userDataStore

    private val _detail = MutableStateFlow<Response<Alat>>(Response.Loading)
    val detail: StateFlow<Response<Alat>> = _detail

    private val _addMonthChecking = MutableStateFlow<Response<Boolean>>(Response.Loading)
    val addMonthChecking: StateFlow<Response<Boolean>> = _addMonthChecking

    private val _addToReportProblem = MutableStateFlow<Response<Boolean>>(Response.Loading)
    val addToReportProblem: StateFlow<Response<Boolean>> = _addToReportProblem

    init {
        viewModelScope.launch {
            getUserDataStore()
        }
    }

    private suspend fun getUserDataStore() {
        _userDataStore.value = repo.getUserDataStore()
    }

    suspend fun addImageToStorage(imageUri: Uri): String {
        return repo.addImageToFirebaseStorage(imageUri)
    }

    suspend fun addToMonthChecking(idDocument: String, item: Checking) {
        viewModelScope.launch {
            try {
                repo.addToMonthChecking(idDocument, item)
                    .collect {
                        _addMonthChecking.value = it
                    }
            } catch (e: Exception) {
                e.printStackTrace()
            }
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