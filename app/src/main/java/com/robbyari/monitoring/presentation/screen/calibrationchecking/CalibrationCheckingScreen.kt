package com.robbyari.monitoring.presentation.screen.calibrationchecking

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.firebase.Timestamp
import com.robbyari.monitoring.domain.model.Alat
import com.robbyari.monitoring.domain.model.Checking
import com.robbyari.monitoring.domain.model.ReportProblem
import com.robbyari.monitoring.domain.model.Response
import com.robbyari.monitoring.presentation.components.ActionBarDetail
import com.robbyari.monitoring.presentation.components.BodyContentChecking
import com.robbyari.monitoring.presentation.components.BottomSheet
import com.robbyari.monitoring.presentation.components.DetailHeaderContent
import com.robbyari.monitoring.presentation.components.ShowAlertDialog
import com.robbyari.monitoring.presentation.theme.Blue
import com.robbyari.monitoring.presentation.theme.LightBlue
import com.robbyari.monitoring.utils.convertStringToFirebaseTimestamp
import com.robbyari.monitoring.utils.createImageFile
import com.robbyari.monitoring.utils.generateTimestamp
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Objects

@Composable
fun CalibrationCheckingScreen(
    id: String?,
    location: String?,
    isDistanceGreaterThan100Meters: Boolean,
    navigateBack: () -> Unit,
    viewModel: CalibrationCheckingViewModel = hiltViewModel()
) {
    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setSystemBarsColor(Color.White, darkIcons = true)
        systemUiController.setNavigationBarColor(Color.Black)
    }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val checkedItems = remember { mutableStateMapOf<String, Boolean>() }
    val checkedItemCount = checkedItems.count { it.value }
    val totalItemCount = remember { mutableStateOf(0) }
    val progressPercentage = if (totalItemCount.value > 0) (checkedItemCount * 100) / totalItemCount.value else 0
    val (notes, onQueryChange) = remember { mutableStateOf("Semua fungsi baik") }

    val userDataStore by viewModel.userDataStore.collectAsState()
    var isLoading by remember { mutableStateOf(false) }
    val timestampString by remember { mutableStateOf(generateTimestamp()) }

    val addDayChecking by viewModel.addCalibrationChecking.collectAsState()

    val detailState: Response<Alat> by viewModel.detail.collectAsState()

    val (notesReport, onQueryChangeReport) = remember { mutableStateOf("") }
    var isLoadingReportProblem by remember { mutableStateOf(false) }
    var showSheet by remember { mutableStateOf(false) }
    val addToReportProblem by viewModel.addToReportProblem.collectAsState()
    val showSuccessDialog = remember { mutableStateOf(false) }

    val file = context.createImageFile()
    val uri = FileProvider.getUriForFile(
        Objects.requireNonNull(context),
        "com.robbyari.monitoring.provider", file
    )

    var capturedImageUri by remember {
        mutableStateOf<Uri>(Uri.EMPTY)
    }

    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { result ->
            if (result) {
                capturedImageUri = uri
            }
        }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        if (it) {
            Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
            cameraLauncher.launch(uri)
        } else {
            Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(addDayChecking) {
        when (addDayChecking) {
            is Response.Success -> {
                navigateBack()
                isLoading = false
            }

            else -> {}
        }
    }

    LaunchedEffect(addToReportProblem) {
        when (addToReportProblem) {
            is Response.Success -> {
                isLoadingReportProblem = false
                showSheet = false
                showSuccessDialog.value = true
            }

            else -> {}
        }
    }

    LaunchedEffect(id) {
        if (id != null) {
            viewModel.getDetail(id)
        }
    }

    if (showSheet) {
        val timeStamp = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(Date())
        val data = (detailState as Response.Success<Alat>).data ?: Alat()
        val reportProblem = ReportProblem(
            idReport = timeStamp,
            idAlat = data.id,
            photoUrl = data.photoUrl,
            namaAlat = data.namaAlat,
            noSeri = data.noSeri,
            unit = data.unit,
            nameUser = "${userDataStore.firstName} ${userDataStore.lastName}",
            photoUser = userDataStore.photoUrl,
            createdAt = convertStringToFirebaseTimestamp(timestampString),
            divisi = userDataStore.divisi,
            notesUser = notesReport,
            notesRepair = "",
            photoTeknisi = "",
            repairedAt = Timestamp(0, 0),
            photoRepair = "",
            status = false,
            repairedBy = "",
        )
        BottomSheet(
            alat = data,
            user = userDataStore,
            time = timestampString,
            notes = notesReport,
            onQueryChange = onQueryChangeReport,
            onDismiss = {
                showSheet = false
            },
            showLoading = isLoadingReportProblem,
            submit = {
                if (notesReport.isNotEmpty()) {
                    isLoadingReportProblem = true
                    coroutineScope.launch {
                        viewModel.addToReportProblem(timeStamp, reportProblem)
                    }
                } else {
                    Toast.makeText(context, "Catatan tidak boleh kosong!", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

    if (showSuccessDialog.value) {
        ShowAlertDialog {
            showSuccessDialog.value = false
        }
    }

    Box(
        modifier = Modifier.fillMaxSize().background(LightBlue),
    ) {
        when (detailState) {
            is Response.Loading -> {
                Log.d("Loading", "")
            }

            is Response.Success -> {
                val data = (detailState as Response.Success<Alat>).data
                totalItemCount.value = data?.listCek?.size ?: 0
                if (data != null) {
                    Column(
                        Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .systemBarsPadding()
                    ) {
                        ActionBarDetail(
                            title = "Pengecekan Kalibrasi",
                            navigateBack = navigateBack,
                            modifier = Modifier
                        )
                        DetailHeaderContent(data = data, calibrationChecking = true)
                        BodyContentChecking(
                            time = timestampString,
                            location = if (isDistanceGreaterThan100Meters) "Diluar Jangkauan" else "RS Prikasih",
                            capturedImageUri = capturedImageUri.path?.isNotEmpty() == true,
                            painter = rememberAsyncImagePainter(capturedImageUri),
                            listCek = data.listCek,
                            nameUser = "${userDataStore.firstName} ${userDataStore.lastName}",
                            checkedItems = checkedItems,
                            checkedItemCount = checkedItemCount,
                            progressPercentage = progressPercentage,
                            totalItemCount = totalItemCount.value,
                            notes = notes,
                            onQueryChange = onQueryChange,
                            onClickCamera = {
                                val permissionCheckResult = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                                if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                                    cameraLauncher.launch(uri)
                                } else {
                                    cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                                }

                            }
                        )
                        Spacer(modifier = Modifier.height(100.dp))
                    }
                }
            }

            is Response.Failure -> {
                Log.d("Failure", "")
            }

        }

        Row(
            modifier = Modifier
                .systemBarsPadding()
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(
                onClick = {
                    showSheet = true
                },
                modifier = Modifier
                    .padding(start = 16.dp)
                    .height(50.dp)
                    .background(Blue, shape = RoundedCornerShape(20))
                    .weight(0.11f)
            )
            {
                Icon(
                    imageVector = Icons.Default.ReportProblem,
                    contentDescription = "Icon laporkan masalah",
                    tint = Color.White,
                    modifier = Modifier
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            TextButton(
                onClick = {
                    isLoading = true
                    if (capturedImageUri != Uri.EMPTY) {
                        coroutineScope.launch {
                            val data = (detailState as Response.Success<Alat>).data
                            val photoUrl = viewModel.addImageToStorage(capturedImageUri)
                            val timeStamp = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(Date())
                            val updatedListCek = data?.listCek?.toMutableMap() ?: mutableMapOf()
                            for ((key, _) in updatedListCek) {
                                if (checkedItems.containsKey(key)) {
                                    updatedListCek[key] = true
                                }
                            }
                            val checkingItem = Checking(
                                id = data?.id,
                                noSeri = data?.noSeri,
                                namaAlat = data?.namaAlat,
                                unit = data?.unit,
                                petugasHariIni = "${userDataStore.firstName} ${userDataStore.lastName}",
                                waktuPegecekan = convertStringToFirebaseTimestamp(timestampString),
                                lokasi = if (isDistanceGreaterThan100Meters) "Diluar Jangkauan" else "RS Prikasih",
                                listCek = updatedListCek,
                                photoUrl = photoUrl,
                                progress = "$checkedItemCount/${totalItemCount.value} Selesai",
                                catatan = notes
                            )
                            viewModel.addToCalibrationChecking(timeStamp, checkingItem)
                        }
                    } else {
                        isLoading = false
                        Toast.makeText(context, "Gambar tidak boleh kosong!", Toast.LENGTH_SHORT).show()
                    }
                },
                enabled = !isLoading,
                modifier = Modifier
                    .padding(end = 16.dp)
                    .height(50.dp)
                    .background(Blue, shape = RoundedCornerShape(20))
                    .weight(0.5f)
            )
            {
                if (isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(30.dp))
                } else {
                    Text(
                        text = "Kirim",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}