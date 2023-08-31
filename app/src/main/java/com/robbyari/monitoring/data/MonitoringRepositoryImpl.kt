package com.robbyari.monitoring.data

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import com.robbyari.monitoring.domain.model.Alat
import com.robbyari.monitoring.domain.model.Checking
import com.robbyari.monitoring.domain.model.ReportProblem
import com.robbyari.monitoring.domain.model.Response
import com.robbyari.monitoring.domain.model.User
import com.robbyari.monitoring.domain.repository.MonitoringRepository
import com.robbyari.monitoring.utils.add30DaysToTimestamp
import com.robbyari.monitoring.utils.addOneYearToTimestamp
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
    private val storage: FirebaseStorage,
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

    override suspend fun setUser(email: String): Response<Unit> {
        return try {
            val user = getUser(email)
            userDataStorePreferences.edit { preferences ->
                preferences[KEY_EMAIL] = email
                preferences[KEY_FIRSTNAME] = user.firstName ?: ""
                preferences[KEY_LASTNAME] = user.lastName ?: ""
                preferences[KEY_PHOTO_URL] = user.photoUrl ?: ""
                preferences[KEY_ROLE] = user.role ?: ""
                preferences[KEY_DIVISI] = user.divisi ?: ""
            }
            Response.Success(Unit)
        } catch (e: Exception) {
            Response.Failure(e)
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

    override suspend fun getMonthlyCheck(): Flow<Response<List<Alat>>> = callbackFlow {
        val currentDate = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.time

        val query = db.collection("Alat")
            .whereEqualTo("cekBulanan", true)
            .whereLessThan("pengecekanBulanan", currentDate)

        val listener = query.addSnapshotListener { querySnapshot, exception ->
            if (exception != null) {
                trySend(Response.Failure(exception))
                return@addSnapshotListener
            }

            val monthlyCheckList = mutableListOf<Alat>()
            for (document in querySnapshot!!.documents) {
                val alat = document.toObject(Alat::class.java)
                alat?.let { monthlyCheckList.add(it) }
            }

            trySend(Response.Success(monthlyCheckList))
        }

        awaitClose {
            listener.remove()
        }
    }

    override suspend fun getCalibrationCheck(): Flow<Response<List<Alat>>> = callbackFlow {
        val currentDate = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.time

        val query = db.collection("Alat")
            .whereEqualTo("cekKalibrasi", true)
            .whereLessThan("kalibrasi", currentDate)

        val listener = query.addSnapshotListener { querySnapshot, exception ->
            if (exception != null) {
                trySend(Response.Failure(exception))
                return@addSnapshotListener
            }

            val kalibrasiCheckList = mutableListOf<Alat>()
            for (document in querySnapshot!!.documents) {
                val alat = document.toObject(Alat::class.java)
                alat?.let { kalibrasiCheckList.add(it) }
            }

            trySend(Response.Success(kalibrasiCheckList))
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

    override suspend fun addImageToFirebaseStorage(imageUri: Uri): String {
        return try {
            val timestamp = System.currentTimeMillis()
            val uniqueFilename = "image_$timestamp.jpg"

            val storageReference = storage.reference.child("Images").child("Bukti Pengecekan").child(uniqueFilename)
            storageReference.putFile(imageUri).await()

            val downloadUrl = storageReference.downloadUrl.await()
            downloadUrl.toString()
        } catch (e: Exception) {
            e.printStackTrace()
            "Gagal Upload Gambar"
        }
    }

    override suspend fun addToDayChecking(idDocument: String, item: Checking): Flow<Response<Boolean>> = flow {
        emit(Response.Loading)
        try {
            val documentRef = db.collection("DayChecking")
                .document(idDocument)

            documentRef.set(item).await()
            updateDailyChecking(item.id ?: "", item.petugasHariIni ?: "", item.waktuPegecekan ?: Timestamp(0, 0))
            emit(Response.Success(true))
        } catch (e: Exception) {
            emit(Response.Failure(e))
        }
    }

    override suspend fun addToMonthChecking(idDocument: String, item: Checking): Flow<Response<Boolean>> = flow {
        emit(Response.Loading)
        try {
            val documentRef = db.collection("MonthChecking")
                .document(idDocument)

            documentRef.set(item).await()
            updateMonthlyChecking(item.id ?: "", item.petugasHariIni ?: "", add30DaysToTimestamp(item.waktuPegecekan ?: Timestamp(0, 0)))
            emit(Response.Success(true))
        } catch (e: Exception) {
            emit(Response.Failure(e))
        }
    }

    override suspend fun addToCalibrationChecking(idDocument: String, item: Checking): Flow<Response<Boolean>> = flow {
        emit(Response.Loading)
        try {
            val documentRef = db.collection("CalibrationChecking")
                .document(idDocument)

            documentRef.set(item).await()
            updateCalibrationChecking(item.id ?: "", item.petugasHariIni ?: "", addOneYearToTimestamp(item.waktuPegecekan ?: Timestamp(0, 0)))
            emit(Response.Success(true))
        } catch (e: Exception) {
            emit(Response.Failure(e))
        }
    }

    override suspend fun getUserDataStore(): User {
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
                    val firstname = preferences[KEY_FIRSTNAME]
                    val lastname = preferences[KEY_LASTNAME]
                    val email = preferences[KEY_EMAIL]
                    val photo = preferences[KEY_PHOTO_URL]
                    val role = preferences[KEY_ROLE]
                    val divisi = preferences[KEY_DIVISI]

                    User(
                        firstName = firstname,
                        lastName = lastname,
                        email = email,
                        photoUrl = photo,
                        role = role,
                        divisi = divisi
                    )
                }
            val value = flow.firstOrNull() ?: User()
            value
        } catch (e: Exception) {
            e.printStackTrace()
            User()
        }
    }

    override suspend fun addToReportProblem(idDocument: String, item: ReportProblem): Flow<Response<Boolean>> = flow {
        emit(Response.Loading)
        try {
            val documentRef = db.collection("ReportProblem")
                .document(idDocument)

            documentRef.set(item).await()
            emit(Response.Success(true))
        } catch (e: Exception) {
            emit(Response.Failure(e))
        }
    }

    override suspend fun getReportProblem(): Flow<Response<List<ReportProblem>>> = callbackFlow {
        val query = db.collection("ReportProblem")

        val listener = query.addSnapshotListener { querySnapshot, exception ->
            if (exception != null) {
                trySend(Response.Failure(exception))
                return@addSnapshotListener
            }

            val reportProblem = mutableListOf<ReportProblem>()
            for (document in querySnapshot!!.documents) {
                val data = document.toObject(ReportProblem::class.java)
                data?.let { reportProblem.add(it) }
            }

            trySend(Response.Success(reportProblem))
        }

        awaitClose {
            listener.remove()
        }
    }

    override suspend fun getDetailReportProblem(id: String): Flow<Response<ReportProblem>> = flow {
        emit(Response.Loading)
        try {
            val data = db.collection("ReportProblem")
                .document(id).get().await()
            val detail = data.toObject(ReportProblem::class.java)

            if (detail != null) {
                emit(Response.Success(detail))
            } else {
                emit(Response.Failure(Exception("Data tidak ditemukan")))
            }

        } catch (e: Exception) {
            emit(Response.Failure(e))
        }

    }

    override suspend fun updateToReportProblem(item: ReportProblem): Flow<Response<Boolean>> = flow {
        emit(Response.Loading)
        try {
            val doc = FirebaseFirestore.getInstance().collection("ReportProblem")
            val id = item.idReport!!

            val document = doc.document(id).get().await()

            if (!document.exists()) {
                Log.d("Kosong", "Tidak ada dokumen dengan idAlat: ${item.idReport}")
            } else {
                doc.document(id).update("status", item.status)
                doc.document(id).update("repairedBy", item.repairedBy)
                doc.document(id).update("repairedAt", item.repairedAt)
                doc.document(id).update("photoTeknisi", item.photoTeknisi)
                doc.document(id).update("photoRepair", item.photoRepair)
                doc.document(id).update("notesRepair", item.notesRepair)
                Log.d("Berhasil Update", "")
                emit(Response.Success(true))
            }
        } catch (e: Exception) {
            emit(Response.Failure(e))
        }
    }

    private suspend fun getUser(email: String): User {
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

    private suspend fun updateDailyChecking(id: String, petugasHariIni: String, timestamp: Timestamp) {
        try {
            val doc = FirebaseFirestore.getInstance().collection("Alat")
            val documentSnapshot = doc.document(id).get().await()

            if (!documentSnapshot.exists()) {
                Log.d("Kosong", "")
            }

            if (documentSnapshot.exists()) {
                doc.document(id).update("pengecekanHarian", timestamp)
                doc.document(id).update("terakhirDicekOleh", petugasHariIni)
                Log.d("Berhasil Update", "")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private suspend fun updateMonthlyChecking(id: String, petugasHariIni: String, timestamp: Timestamp) {
        try {
            val doc = FirebaseFirestore.getInstance().collection("Alat")
            val documentSnapshot = doc.document(id).get().await()

            if (!documentSnapshot.exists()) {
                Log.d("Kosong", "")
            }

            if (documentSnapshot.exists()) {
                doc.document(id).update("pengecekanBulanan", timestamp)
                doc.document(id).update("terakhirDicekOleh", petugasHariIni)
                Log.d("Berhasil Update", "")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private suspend fun updateCalibrationChecking(id: String, petugasHariIni: String, timestamp: Timestamp) {
        try {
            val doc = FirebaseFirestore.getInstance().collection("Alat")
            val documentSnapshot = doc.document(id).get().await()

            if (!documentSnapshot.exists()) {
                Log.d("Kosong", "")
            }

            if (documentSnapshot.exists()) {
                doc.document(id).update("kalibrasi", timestamp)
                doc.document(id).update("terakhirDicekOleh", petugasHariIni)
                Log.d("Berhasil Update", "")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        val KEY_UID = stringPreferencesKey("uid")
        val KEY_FIRSTNAME = stringPreferencesKey("firstname")
        val KEY_LASTNAME = stringPreferencesKey("lastname")
        val KEY_EMAIL = stringPreferencesKey("email")
        val KEY_PASSWORD = stringPreferencesKey("password")
        val KEY_PHOTO_URL = stringPreferencesKey("photourl")
        val KEY_ROLE = stringPreferencesKey("role")
        val KEY_DIVISI = stringPreferencesKey("divisi")
        val KEY_STATUS = stringPreferencesKey("status")
    }

}