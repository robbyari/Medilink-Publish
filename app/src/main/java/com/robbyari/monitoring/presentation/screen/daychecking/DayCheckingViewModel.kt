package com.robbyari.monitoring.presentation.screen.daychecking

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.robbyari.monitoring.domain.model.Alat
import com.robbyari.monitoring.domain.model.DailyChecking
import com.robbyari.monitoring.domain.model.Response
import com.robbyari.monitoring.domain.model.User
import com.robbyari.monitoring.domain.repository.MonitoringRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DayCheckingViewModel @Inject constructor(
    private val repo: MonitoringRepository
) : ViewModel() {

    private val _userDataStore = MutableStateFlow(User())
    val userDataStore: StateFlow<User> = _userDataStore

    private val _detail = MutableStateFlow<Response<Alat>>(Response.Loading)
    val detail: StateFlow<Response<Alat>> = _detail

    private val _addDayChecking = MutableStateFlow<Response<Boolean>>(Response.Loading)
    val addDayChecking: StateFlow<Response<Boolean>> = _addDayChecking

    private val idMock = "Nu62hgGla4hD6qz"

    init {
        viewModelScope.launch {
            getDetail(idMock)
            getUserDataStore()
        }
    }

    private suspend fun getUserDataStore() {
        _userDataStore.value = repo.getUserDataStore()
    }

    suspend fun addImageToStorage(imageUri: Uri): String {
        return repo.addImageToFirebaseStorage(imageUri)
    }

    suspend fun addToDayChecking(idDocument: String, item: DailyChecking) {
        viewModelScope.launch {
            try {
                repo.addToDayChecking(idDocument, item)
                    .collect {
                        _addDayChecking.value = it
                    }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private suspend fun getDetail(id: String) {
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