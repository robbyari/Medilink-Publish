package com.robbyari.monitoring.domain.repository

import android.net.Uri
import com.robbyari.monitoring.domain.model.Alat
import com.robbyari.monitoring.domain.model.Checking
import com.robbyari.monitoring.domain.model.ReportProblem
import com.robbyari.monitoring.domain.model.Response
import com.robbyari.monitoring.domain.model.User
import kotlinx.coroutines.flow.Flow

interface MonitoringRepository {
    suspend fun loginUser(email: String, password: String): Boolean
    suspend fun setUser(email: String): Response<Unit>
    suspend fun getDailyCheck(): Flow<Response<List<Alat>>>
    suspend fun getMonthlyCheck(): Flow<Response<List<Alat>>>
    suspend fun getCalibrationCheck(): Flow<Response<List<Alat>>>
    suspend fun getBarcodeText(): Flow<Response<String>>
    suspend fun getDetailAlat(id: String): Flow<Response<Alat>>
    suspend fun addImageToFirebaseStorage(imageUri: Uri): String
    suspend fun addToDayChecking(idDocument: String ,item: Checking): Flow<Response<Boolean>>
    suspend fun addToMonthChecking(idDocument: String ,item: Checking): Flow<Response<Boolean>>
    suspend fun addToCalibrationChecking(idDocument: String ,item: Checking): Flow<Response<Boolean>>
    suspend fun getUserDataStore(): User
    suspend fun addToReportProblem(idDocument: String, item: ReportProblem): Flow<Response<Boolean>>
    suspend fun getReportProblem(): Flow<Response<List<ReportProblem>>>
    suspend fun getDetailReportProblem(id: String): Flow<Response<ReportProblem>>
    suspend fun updateToReportProblem(item: ReportProblem): Flow<Response<Boolean>>
    suspend fun countItemAlat(): Int
    suspend fun getListAlat(): Flow<Response<List<Alat>>>
    suspend fun changeEmail(uid: String, emailUpdate: String): Boolean
    suspend fun getUserDatabase(uid: String): Flow<Response<User>>
    suspend fun changePassword(uid: String, passwordUpdate: String): Boolean
    suspend fun resetDataStore() : Boolean
}