package com.robbyari.monitoring.data

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.robbyari.monitoring.domain.model.Response
import com.robbyari.monitoring.domain.model.User
import com.robbyari.monitoring.domain.repository.MonitoringRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MonitoringRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore
) : MonitoringRepository {

    override suspend fun loginUser(email: String, password: String): Flow<Response<User>> = flow {
        emit(Response.Loading)
        try {
            val querySnapshot = db.collection("User")
                .whereEqualTo("email", email)
                .whereEqualTo("password", password)
                .whereEqualTo("status", true)
                .get()
                .await()

            if (!querySnapshot.isEmpty) {
                val userDocument = querySnapshot.documents.first()
                val user = userDocument.toObject(User::class.java)
                emit(Response.Success(user))
            } else {
                Log.d("Error", "loginUser: User not found or inactive")
            }
        } catch (e: Exception) {
            emit(Response.Failure(e))
        }
    }

}