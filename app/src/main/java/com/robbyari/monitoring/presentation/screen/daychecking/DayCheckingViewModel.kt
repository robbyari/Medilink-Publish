package com.robbyari.monitoring.presentation.screen.daychecking

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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
class DayCheckingViewModel @Inject constructor(
    private val repo: MonitoringRepository
) : ViewModel() {

    private val _detail = MutableStateFlow<Response<Alat>>(Response.Loading)
    val detail: StateFlow<Response<Alat>> = _detail

    var addImageToStorageResponse by mutableStateOf<Response<Uri>>(Response.Success(null))
        private set

    private val idMock = "Nu62hgGla4hD6qz"

    init {
        viewModelScope.launch {
            getDetail(idMock)
        }
    }

    fun addImageToStorage(imageUri: Uri) = viewModelScope.launch {
        addImageToStorageResponse = Response.Loading
        addImageToStorageResponse = repo.addImageToFirebaseStorage(imageUri)
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