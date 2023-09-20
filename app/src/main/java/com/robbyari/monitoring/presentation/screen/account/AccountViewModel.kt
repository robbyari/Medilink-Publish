package com.robbyari.monitoring.presentation.screen.account

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.robbyari.monitoring.domain.model.Response
import com.robbyari.monitoring.domain.model.User
import com.robbyari.monitoring.domain.repository.MonitoringRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val repo: MonitoringRepository
) : ViewModel() {

    private val _user = MutableStateFlow<Response<User>>(Response.Loading)
    val user: StateFlow<Response<User>> = _user

    suspend fun resetDataStore(): Boolean {
        return repo.resetDataStore()
    }

    suspend fun changeEmail(uid: String, emailUpdate: String): Boolean {
        return repo.changeEmail(uid, emailUpdate)
    }

    suspend fun changePassword(uid: String, passwordUpdate: String): Boolean {
        return repo.changePassword(uid, passwordUpdate)
    }

    suspend fun changePhotoProfile(uid: String, urlUpdate: String): Boolean {
        return repo.changePhotoProfile(uid, urlUpdate)
    }

    suspend fun addPhotoProfileToFirebaseStorage(imageUri: Uri, idUser: String): String {
        return repo.addPhotoProfileToFirebaseStorage(imageUri, idUser)
    }

    suspend fun deleteAccount(id: String) {
        return repo.deleteAccount(id)
    }

    suspend fun getUser(uid: String) {
        try {
            repo.getUserDatabase(uid)
                .collect {
                    _user.value = it
                }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}