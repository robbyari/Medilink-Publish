package com.robbyari.monitoring.data

import android.content.Context
import android.net.Uri
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
import com.robbyari.monitoring.R
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
    private val context: Context,
) : MonitoringRepository {

    private val options = GmsBarcodeScannerOptions.Builder()
        .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
        .enableAutoZoom()
        .build()

    private val scanner = GmsBarcodeScanning.getClient(context, options)

    override suspend fun loginUser(email: String, password: String): Boolean {
        return try {
            val querySnapshot = db.collection(context.getString(R.string.user))
                .whereEqualTo(context.getString(R.string.email), email)
                .whereEqualTo(context.getString(R.string.password), password)
                .whereEqualTo(context.getString(R.string.status), true)
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
            val user = checkUser(email)
            userDataStorePreferences.edit { preferences ->
                preferences[KEY_EMAIL] = email
                preferences[KEY_NAME] = user.name ?: ""
                preferences[KEY_PHOTO_URL] = user.photoUrl ?: ""
                preferences[KEY_ROLE] = user.role ?: ""
                preferences[KEY_UID] = user.uid ?: ""
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

        val query = db.collection(context.getString(R.string.alat))
            .whereEqualTo(context.getString(R.string.cekharian), true)
            .whereLessThan(context.getString(R.string.pengecekanharian), currentDate)

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

        val query = db.collection(context.getString(R.string.alat))
            .whereEqualTo(context.getString(R.string.cekbulanan), true)
            .whereLessThan(context.getString(R.string.pengecekanbulanan), currentDate)

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

        val query = db.collection(context.getString(R.string.alat))
            .whereEqualTo(context.getString(R.string.cekkalibrasi), true)
            .whereLessThan(context.getString(R.string.kalibrasi), currentDate)

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

    override suspend fun getBarcodeText(): String {
        val barcodeValue = scanner.startScan().await()
        return barcodeValue.rawValue ?: ""
    }

    override suspend fun getDetailAlat(id: String): Flow<Response<Alat>> = flow {
        emit(Response.Loading)
        try {
            val getDetail = db.collection(context.getString(R.string.alat)).document(id).get().await()
            val detail = getDetail.toObject(Alat::class.java)

            if (detail != null) {
                emit(Response.Success(detail))
            } else {
                emit(Response.Failure(Exception(context.getString(R.string.data_not_found))))
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

            val storageReference = storage.reference.child(context.getString(R.string.images)).child(context.getString(R.string.bukti_pengecekan)).child(uniqueFilename)
            storageReference.putFile(imageUri).await()

            val downloadUrl = storageReference.downloadUrl.await()
            downloadUrl.toString()
        } catch (e: Exception) {
            e.printStackTrace()
            context.getString(R.string.failed)
        }
    }

    override suspend fun addPhotoProfileToFirebaseStorage(imageUri: Uri, idUser: String): String {
        return try {
            val uniqueFilename = "image_$idUser.jpg"

            val storageReference = storage.reference.child(context.getString(R.string.images)).child(context.getString(R.string.photo_profile)).child(uniqueFilename)
            storageReference.putFile(imageUri).await()

            val downloadUrl = storageReference.downloadUrl.await()
            downloadUrl.toString()
        } catch (e: Exception) {
            e.printStackTrace()
            context.getString(R.string.failed)
        }
    }

    override suspend fun addToDayChecking(idDocument: String, item: Checking): Flow<Response<Boolean>> = flow {
        emit(Response.Loading)
        try {
            val documentRef = db.collection(context.getString(R.string.daychecking))
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
            val documentRef = db.collection(context.getString(R.string.monthchecking))
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
            val documentRef = db.collection(context.getString(R.string.calibrationchecking))
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
                    val name = preferences[KEY_NAME]
                    val email = preferences[KEY_EMAIL]
                    val photo = preferences[KEY_PHOTO_URL]
                    val role = preferences[KEY_ROLE]
                    val uid = preferences[KEY_UID]

                    User(
                        name = name,
                        email = email,
                        photoUrl = photo,
                        role = role,
                        uid = uid
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
            val documentRef = db.collection(context.getString(R.string.reportproblem))
                .document(idDocument)

            documentRef.set(item).await()
            emit(Response.Success(true))
        } catch (e: Exception) {
            emit(Response.Failure(e))
        }
    }

    override suspend fun getReportProblem(): Flow<Response<List<ReportProblem>>> = callbackFlow {
        val query = db.collection(context.getString(R.string.reportproblem))

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
            val data = db.collection(context.getString(R.string.reportproblem))
                .document(id).get().await()
            val detail = data.toObject(ReportProblem::class.java)

            if (detail != null) {
                emit(Response.Success(detail))
            } else {
                emit(Response.Failure(Exception(context.getString(R.string.data_not_found))))
            }

        } catch (e: Exception) {
            emit(Response.Failure(e))
        }

    }

    override suspend fun updateToReportProblem(item: ReportProblem): Flow<Response<Boolean>> = flow {
        emit(Response.Loading)
        try {
            val doc = FirebaseFirestore.getInstance().collection(context.getString(R.string.reportproblem))
            val id = item.idReport!!

            val document = doc.document(id).get().await()

            if (document.exists()) {
                doc.document(id).update(context.getString(R.string.status), item.status)
                doc.document(id).update(context.getString(R.string.repairedby), item.repairedBy)
                doc.document(id).update(context.getString(R.string.repairedat), item.repairedAt)
                doc.document(id).update(context.getString(R.string.phototeknisi), item.photoTeknisi)
                doc.document(id).update(context.getString(R.string.photorepair), item.photoRepair)
                doc.document(id).update(context.getString(R.string.notesrepair), item.notesRepair)
                emit(Response.Success(true))
            }
        } catch (e: Exception) {
            emit(Response.Failure(e))
        }
    }

    override suspend fun countItemAlat(): Int {
        return try {
            val collectionRef = db.collection(context.getString(R.string.alat))
            val snapshot = collectionRef.get().await()

            snapshot.size()
        } catch (e: Exception) {
            e.printStackTrace()
            0
        }
    }

    override suspend fun getListAlat(): Flow<Response<List<Alat>>> = callbackFlow {
        val query = db.collection(context.getString(R.string.alat))

        val listener = query.addSnapshotListener { querySnapshot, exception ->
            if (exception != null) {
                trySend(Response.Failure(exception))
                return@addSnapshotListener
            }

            val listAlat = mutableListOf<Alat>()
            for (document in querySnapshot!!.documents) {
                val alat = document.toObject(Alat::class.java)
                alat?.let { listAlat.add(it) }
            }

            trySend(Response.Success(listAlat))
        }

        awaitClose {
            listener.remove()
        }
    }

    override suspend fun changeEmail(uid: String, emailUpdate: String): Boolean {
        return try {
            val doc = db.collection(context.getString(R.string.user))
            val documentSnapshot = doc.document(uid).get().await()

            if (documentSnapshot.exists()) {
                doc.document(uid).update(context.getString(R.string.email), emailUpdate)
                userDataStorePreferences.edit { preferences ->
                    preferences[KEY_EMAIL] = emailUpdate
                }
                documentSnapshot.exists()
            } else {
                false
            }

        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    override suspend fun getUserDatabase(uid: String): Flow<Response<User>> = callbackFlow {
        val query = db.collection(context.getString(R.string.user)).document(uid)

        val listener = query.addSnapshotListener { querySnapshot, exception ->
            if (exception != null) {
                trySend(Response.Failure(exception))
                return@addSnapshotListener
            }

            val document = querySnapshot?.toObject(User::class.java)
            if (document != null) {
                trySend(Response.Success(document))
            } else {
                trySend(Response.Failure(Exception(context.getString(R.string.data_not_found))))
            }
        }

        awaitClose {
            listener.remove()
        }

    }

    override suspend fun changePassword(uid: String, passwordUpdate: String): Boolean {
        return try {
            val doc = db.collection(context.getString(R.string.user))
            val documentSnapshot = doc.document(uid).get().await()

            if (documentSnapshot.exists()) {
                doc.document(uid).update(context.getString(R.string.password), passwordUpdate)

                documentSnapshot.exists()
            } else {
                false
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    override suspend fun changePhotoProfile(uid: String, urlUpdate: String): Boolean {
        return try {
            val doc = db.collection(context.getString(R.string.user))
            val documentSnapshot = doc.document(uid).get().await()

            if (documentSnapshot.exists()) {
                doc.document(uid).update(context.getString(R.string.photourl), urlUpdate)
                userDataStorePreferences.edit { preferences ->
                    preferences[KEY_PHOTO_URL] = urlUpdate
                }

                documentSnapshot.exists()
            } else {
                false
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    override suspend fun resetDataStore(): Boolean {
        return try {
            userDataStorePreferences.edit { preferences ->
                preferences[KEY_EMAIL] = ""
                preferences[KEY_NAME] = ""
                preferences[KEY_PHOTO_URL] = ""
                preferences[KEY_ROLE] = ""
                preferences[KEY_UID] = ""
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    override suspend fun registerUser(user: User): Boolean {
        return try {
            val documentRef = db.collection(context.getString(R.string.user))
                .document(user.uid!!)

            documentRef.set(user).await()

            val result = documentRef.get().await()
            return result.exists()
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    override suspend fun deleteAccount(id: String) {
        try {
            resetDataStore()
            db.collection("User").document(id).delete().await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private suspend fun checkUser(email: String): User {
        return try {
            val querySnapshot = db.collection(context.getString(R.string.user))
                .whereEqualTo(context.getString(R.string.email), email)
                .get()
                .await()

            if (!querySnapshot.isEmpty) {
                val userDocument = querySnapshot.documents.first()
                val user = userDocument.toObject(User::class.java)
                user ?: throw Exception(context.getString(R.string.user_not_found))
            } else {
                throw Exception(context.getString(R.string.user_not_found))
            }
        } catch (e: Exception) {
            throw e
        }
    }

    private suspend fun updateDailyChecking(id: String, petugasHariIni: String, timestamp: Timestamp) {
        try {
            val doc = FirebaseFirestore.getInstance().collection(context.getString(R.string.alat))
            val documentSnapshot = doc.document(id).get().await()

            if (documentSnapshot.exists()) {
                doc.document(id).update(context.getString(R.string.pengecekanharian), timestamp)
                doc.document(id).update(context.getString(R.string.terakhirdicekoleh), petugasHariIni)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private suspend fun updateMonthlyChecking(id: String, petugasHariIni: String, timestamp: Timestamp) {
        try {
            val doc = FirebaseFirestore.getInstance().collection(context.getString(R.string.alat))
            val documentSnapshot = doc.document(id).get().await()

            if (documentSnapshot.exists()) {
                doc.document(id).update(context.getString(R.string.pengecekanbulanan), timestamp)
                doc.document(id).update(context.getString(R.string.terakhirdicekoleh), petugasHariIni)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private suspend fun updateCalibrationChecking(id: String, petugasHariIni: String, timestamp: Timestamp) {
        try {
            val doc = FirebaseFirestore.getInstance().collection(context.getString(R.string.alat))
            val documentSnapshot = doc.document(id).get().await()

            if (documentSnapshot.exists()) {
                doc.document(id).update(context.getString(R.string.kalibrasi), timestamp)
                doc.document(id).update(context.getString(R.string.terakhirdicekoleh), petugasHariIni)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        val KEY_UID = stringPreferencesKey("uid")
        val KEY_NAME = stringPreferencesKey("name")
        val KEY_EMAIL = stringPreferencesKey("email")
        val KEY_PHOTO_URL = stringPreferencesKey("photourl")
        val KEY_ROLE = stringPreferencesKey("role")
    }
}