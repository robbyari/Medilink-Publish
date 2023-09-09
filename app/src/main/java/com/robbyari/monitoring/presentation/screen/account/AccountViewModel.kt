package com.robbyari.monitoring.presentation.screen.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.robbyari.monitoring.domain.model.Response
import com.robbyari.monitoring.domain.model.User
import com.robbyari.monitoring.domain.repository.MonitoringRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val repo: MonitoringRepository
) : ViewModel() {
    private val _userDataStore = MutableStateFlow(User())
    val userDataStore: StateFlow<User> = _userDataStore

    private val _user = MutableStateFlow<Response<User>>(Response.Loading)
    val user: StateFlow<Response<User>> = _user


    init {
        viewModelScope.launch {
            getUserDataStore()
        }
    }

    suspend fun resetDataStore() : Boolean {
        return repo.resetDataStore()
    }

    suspend fun changeEmail(uid: String, emailUpdate: String): Boolean {
        return repo.changeEmail(uid, emailUpdate)
    }

    suspend fun changePassword(uid: String, passwordUpdate: String): Boolean {
        return repo.changePassword(uid, passwordUpdate)
    }

    suspend fun getUser(uid: String) {
        viewModelScope.launch {
            try {
                repo.getUserDatabase(uid)
                    .collect{
                        _user.value = it
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