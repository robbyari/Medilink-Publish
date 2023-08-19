package com.robbyari.monitoring.domain.repository

import android.net.Uri
import com.robbyari.monitoring.domain.model.Alat
import com.robbyari.monitoring.domain.model.Response
import com.robbyari.monitoring.domain.model.User
import kotlinx.coroutines.flow.Flow

interface MonitoringRepository {
    suspend fun loginUser(email: String, password: String): Boolean
    suspend fun setEmail(name: String): Response<Unit>
    suspend fun getEmail(): String
    suspend fun getUser(email: String): User
    suspend fun getDailyCheck(): Flow<Response<List<Alat>>>
    suspend fun getBarcodeText(): Flow<Response<String>>
    suspend fun getDetailAlat(id: String): Flow<Response<Alat>>
    suspend fun addImageToFirebaseStorage(imageUri: Uri): Response<Uri>
}