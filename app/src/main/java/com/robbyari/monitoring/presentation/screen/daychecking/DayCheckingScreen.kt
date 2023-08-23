package com.robbyari.monitoring.presentation.screen.daychecking

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.robbyari.monitoring.domain.model.Alat
import com.robbyari.monitoring.domain.model.DailyChecking
import com.robbyari.monitoring.domain.model.Response
import com.robbyari.monitoring.presentation.components.ActionBarDetail
import com.robbyari.monitoring.presentation.components.BodyContentChecking
import com.robbyari.monitoring.presentation.components.DetailHeaderContent
import com.robbyari.monitoring.presentation.theme.Blue
import com.robbyari.monitoring.presentation.theme.MonitoringTheme
import com.robbyari.monitoring.utils.convertStringToFirebaseTimestamp
import com.robbyari.monitoring.utils.createImageFile
import com.robbyari.monitoring.utils.generateTimestamp
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Objects

@Composable
fun DayCheckingScreen(
    id: String?,
    location: String?,
    backHandler: () -> Unit,
    isDistanceGreaterThan100Meters: Boolean,
    navigateBack: () -> Unit,
    viewModel: DayCheckingViewModel = hiltViewModel()
) {
    BackHandler(onBack = backHandler)
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val checkedItems = remember { mutableStateMapOf<String, Boolean>() }
    val checkedItemCount = checkedItems.count{it.value}
    val totalItemCount = remember { mutableStateOf(0) }
    val progressPercentage = if (totalItemCount.value > 0) (checkedItemCount * 100) / totalItemCount.value else 0
    val (notes, onQueryChange) = remember { mutableStateOf("Semua fungsi baik") }

    val userDataStore by viewModel.userDataStore.collectAsState()
    var isLoading by remember { mutableStateOf(false) }
    val timestampString by remember { mutableStateOf(generateTimestamp()) }

    val addDayChecking by viewModel.addDayChecking.collectAsState()
    LaunchedEffect(addDayChecking) {
        when (addDayChecking) {
            is Response.Success -> {
                navigateBack()
                isLoading = false
            } else -> {}
        }
    }

    val detailState: Response<Alat> by viewModel.detail.collectAsState()

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

    Box(
        modifier = Modifier.fillMaxSize(),
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
                            title = "Pengecekan Harian",
                            navigateBack = backHandler,
                            modifier = Modifier
                        )
                        DetailHeaderContent(data = data)
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
                    contentDescription = "Icon Kirim",
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
                            val dailyCheckingItem = DailyChecking(
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
                            viewModel.addToDayChecking(timeStamp, dailyCheckingItem)
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


@Preview(showSystemUi = true, showBackground = true)
@Composable
fun DayCheckingScreenPreview() {
    MonitoringTheme {

    }
}