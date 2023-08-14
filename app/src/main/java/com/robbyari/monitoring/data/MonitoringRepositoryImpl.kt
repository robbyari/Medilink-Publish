package com.robbyari.monitoring.data

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.google.firebase.firestore.FirebaseFirestore
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import com.robbyari.monitoring.domain.model.Alat
import com.robbyari.monitoring.domain.model.Response
import com.robbyari.monitoring.domain.model.User
import com.robbyari.monitoring.domain.repository.MonitoringRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import java.io.IOException
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MonitoringRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore,
    private val userDataStorePreferences: DataStore<Preferences>,
    context: Context,
) : MonitoringRepository {

    private val options = GmsBarcodeScannerOptions.Builder()
        .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
        .enableAutoZoom()
        .build()

    private val scanner = GmsBarcodeScanning.getClient(context, options)

    override suspend fun loginUser(email: String, password: String): Boolean {
        return try {
            val querySnapshot = db.collection("User")
                .whereEqualTo("email", email)
                .whereEqualTo("password", password)
                .whereEqualTo("status", true)
                .get()
                .await()

            !querySnapshot.isEmpty
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    override suspend fun setEmail(name: String): Response<Unit> {
        return try {
            userDataStorePreferences.edit { preferences ->
                preferences[KEY_NAME] = name
            }
            Response.Success(Unit)
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }

    override suspend fun getEmail(): String {
        return try {
            val flow = userDataStorePreferences.data
                .catch { exception ->
                    if (exception is IOException) {
                        emit(emptyPreferences())
                    } else {
                        throw exception
                    }
                }
                .map { preferences ->
                    preferences[KEY_NAME]
                }
            val value = flow.firstOrNull() ?: ""
            value
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    override suspend fun getUser(email: String): User {
        return try {
            val querySnapshot = db.collection("User")
                .whereEqualTo("email", email)
                .get()
                .await()

            if (!querySnapshot.isEmpty) {
                val userDocument = querySnapshot.documents.first()
                val user = userDocument.toObject(User::class.java)
                user ?: throw Exception("User not found")
            } else {
                throw Exception("User not found")
            }
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun getDailyCheck(): Flow<Response<List<Alat>>> = callbackFlow {
        val currentDate = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.time

        val query = db.collection("Alat")
            .whereEqualTo("cekHarian", true)
            .whereLessThan("pengecekanHarian", currentDate)

        val listener = query.addSnapshotListener { querySnapshot, exception ->
            if (exception != null) {
                trySend(Response.Failure(exception))
                return@addSnapshotListener
            }

            val dailyCheckList = mutableListOf<Alat>()
            for (document in querySnapshot!!.documents) {
                val alat = document.toObject(Alat::class.java)
                alat?.let { dailyCheckList.add(it) }
            }

            trySend(Response.Success(dailyCheckList))
        }

        awaitClose {
            listener.remove()
        }
    }

    override suspend fun getBarcodeText(): Flow<Response<String>> = flow {
        emit(Response.Loading)
        try {
            val barcodeValue = scanner.startScan().await()
            emit(Response.Success(barcodeValue.rawValue))
        } catch (e: Exception) {
            emit(Response.Failure(e))
        }
    }

    override suspend fun getDetailAlat(id: String): Flow<Response<Alat>> = flow {
        emit(Response.Loading)
        try {
            val getDetail = db.collection("Alat").document(id).get().await()
            val detail = getDetail.toObject(Alat::class.java)

            if (detail != null) {
                emit(Response.Success(detail))
            } else {
                Log.d("Gagal", "Data kosong")
                emit(Response.Failure(Exception("Data tidak ada")))
            }

        } catch (e: Exception) {
            e.printStackTrace()
            emit(Response.Failure(e))
        }
    }

    companion object {
        val KEY_NAME = stringPreferencesKey(
            name = "settings"
        )
    }

}