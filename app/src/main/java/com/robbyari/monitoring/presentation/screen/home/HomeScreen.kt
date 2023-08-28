package com.robbyari.monitoring.presentation.screen.home

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HomeRepairService
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material.icons.filled.WorkHistory
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.robbyari.monitoring.domain.model.Alat
import com.robbyari.monitoring.domain.model.ReportProblem
import com.robbyari.monitoring.domain.model.Response
import com.robbyari.monitoring.domain.model.User
import com.robbyari.monitoring.presentation.components.CardContent
import com.robbyari.monitoring.presentation.components.HomeActionBar
import com.robbyari.monitoring.presentation.components.ItemContent
import com.robbyari.monitoring.presentation.components.ItemProblem
import com.robbyari.monitoring.presentation.components.TitleBar
import com.robbyari.monitoring.presentation.theme.LightBlue
import com.robbyari.monitoring.utils.BottomShadow
import com.robbyari.monitoring.utils.convertFirebaseTimestampToString
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    navigateToDayChecking: (String) -> Unit,
    navigateToMonthChecking: (String) -> Unit,
    navigateToCalibrationChecking: (String) -> Unit
) {
    val userDataStore by viewModel.userDataStore.collectAsState()
    val context = LocalContext.current

    val barcodeResult: Response<String> by viewModel.barcodeResult.collectAsState()
    val navigateToDayCheckingState = remember { mutableStateOf(false) }
    val navigateToMonthCheckingState = remember { mutableStateOf(false) }
    val navigateCalibrationCheckingState = remember { mutableStateOf(false) }

    LaunchedEffect(barcodeResult) {
        when (barcodeResult) {
            is Response.Success -> {
                val scannedValue = (barcodeResult as Response.Success<String>).data
                if (scannedValue != null) {
                    if (navigateToDayCheckingState.value) {
                        navigateToDayChecking(scannedValue)
                    }
                    if (navigateToMonthCheckingState.value) {
                        navigateToMonthChecking(scannedValue)
                    }
                    if (navigateCalibrationCheckingState.value) {
                        navigateToCalibrationChecking(scannedValue)
                    }
                }
            }
            else -> {}
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HomeContent(nameDataStore = "${userDataStore.firstName} ${userDataStore.lastName}", user = userDataStore, navigateToDayCheckingState, navigateToMonthCheckingState, navigateCalibrationCheckingState)
    }
}

@Composable
fun HomeContent(
    nameDataStore: String,
    user: User,
    navigateToDayChecking: MutableState<Boolean>,
    navigateToMonthChecking:  MutableState<Boolean>,
    navigateToCalibrationChecking:  MutableState<Boolean>,
    viewModel: HomeViewModel = hiltViewModel(),
) {

    val coroutineScope = rememberCoroutineScope()
    val reportCheck by viewModel.reportProblemCheck.collectAsState()
    val reportCheckCount = remember { mutableStateOf(0) }
    val dailyCheck by viewModel.dailyCheck.collectAsState()
    val dailyCheckCount = remember { mutableStateOf(0) }
    val monthlyCheck by viewModel.monthlyCheck.collectAsState()
    val monthlyCheckCount = remember { mutableStateOf(0) }
    val calibrationCheck by viewModel.calibrationCheck.collectAsState()
    val calibrationCheckCount = remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        viewModel.fetchReportProblem()
    }

    LaunchedEffect(Unit) {
        viewModel.fetchDailyCheck()
    }

    LaunchedEffect(Unit) {
        viewModel.fetchMonthlyCheck()
    }

    LaunchedEffect(Unit) {
        viewModel.fetchCalibrationCheck()
    }

    Column {
        Spacer(modifier = Modifier.height(10.dp))
        HomeActionBar(nameDataStore = nameDataStore, user = user)
        Spacer(modifier = Modifier.height(16.dp))
        BottomShadow()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(LightBlue)
                .verticalScroll(rememberScrollState())
                .weight(1f, false)
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            LazyRow(
                modifier = Modifier.fillMaxWidth()
            ) {
                item {
                    Spacer(modifier = Modifier.width(16.dp))
                }
                item {
                    CardContent(icon = Icons.Filled.HomeRepairService, title = "Total Alat", total = 125)
                    Spacer(modifier = Modifier.width(16.dp))
                }
                item {
                    CardContent(icon = Icons.Filled.ReportProblem, title = "Laporan Rusak", total = reportCheckCount.value)
                    Spacer(modifier = Modifier.width(16.dp))
                }
                item {
                    CardContent(icon = Icons.Filled.WorkHistory, title = "Pengecekan Harian", total = dailyCheckCount.value)
                    Spacer(modifier = Modifier.width(16.dp))
                }
                item {
                    CardContent(icon = Icons.Filled.WorkHistory, title = "Pemeliharaan Bulanan", total = monthlyCheckCount.value)
                    Spacer(modifier = Modifier.width(16.dp))
                }
                item {
                    CardContent(icon = Icons.Filled.WorkHistory, title = "Kalibrasi Alat", total = calibrationCheckCount.value)
                    Spacer(modifier = Modifier.width(16.dp))
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            TitleBar(title = "Laporan Rusak", detail = "Semua")
            Spacer(modifier = Modifier.height(16.dp))
            when (reportCheck) {
                is Response.Loading -> {}
                is Response.Success -> {
                    val data = (reportCheck as Response.Success<List<ReportProblem>>).data
                    reportCheckCount.value = data?.size ?: 0

                    if (data.isNullOrEmpty()) {
                        Box(
                            modifier = Modifier
                                .padding(start = 16.dp, end = 16.dp)
                                .fillMaxWidth()
                                .height(130.dp)
                                .background(Color.White, shape = RoundedCornerShape(9)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "Tidak ada", color = Color.Black, fontSize = 16.sp)
                        }
                    }

                    LazyRow(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        item {
                            Spacer(modifier = Modifier.width(16.dp))
                        }
                        items(data!!.size) { index ->
                            val reportProblem = data[index]
                            ItemProblem(
                                model = reportProblem.photoUrl!!,
                                title = reportProblem.namaAlat!!,
                                noSeri = reportProblem.noSeri!!,
                                unit = reportProblem.unit!!,
                                date = convertFirebaseTimestampToString(reportProblem.createdAt!!),
                                nameUser = reportProblem.nameUser!!,
                                photoUser = reportProblem.photoUser!!,
                                notes = reportProblem.notesUser!!,
                                status = reportProblem.status!!,
                                onScanRepaired = {
                                    coroutineScope.launch {
                                        navigateToDayChecking.value = true
                                        viewModel.startScan()
                                    }
                                }
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                        }
                    }
                }
                is Response.Failure -> {}
            }
            Spacer(modifier = Modifier.height(16.dp))
            TitleBar(title = "Pengecekan Harian", detail = "Semua")
            Spacer(modifier = Modifier.height(16.dp))
            when (dailyCheck) {
                is Response.Loading -> {}
                is Response.Success -> {
                    val data = (dailyCheck as Response.Success<List<Alat>>).data
                    dailyCheckCount.value = data?.size ?: 0

                    if (data.isNullOrEmpty()) {
                        Box(
                            modifier = Modifier
                                .padding(start = 16.dp, end = 16.dp)
                                .fillMaxWidth()
                                .height(130.dp)
                                .background(Color.White, shape = RoundedCornerShape(9)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "Tidak ada", color = Color.Black, fontSize = 16.sp)
                        }
                    }

                    LazyRow(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        item {
                            Spacer(modifier = Modifier.width(16.dp))
                        }
                        items(data!!.size) { index ->
                            val alat = data[index]
                            ItemContent(
                                model = alat.photoUrl!!,
                                title = alat.namaAlat!!,
                                noSeri = alat.noSeri!!,
                                unit = alat.unit!!,
                                date = convertFirebaseTimestampToString(alat.pengecekanHarian!!),
                                isScanDay = true,
                                onScanDay = {
                                    coroutineScope.launch {
                                        navigateToDayChecking.value = true
                                        viewModel.startScan()
                                    }
                                }
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                        }
                    }
                }
                is Response.Failure -> {}
            }
            Spacer(modifier = Modifier.height(16.dp))
            TitleBar(title = "Pemeliharaan Bulanan", detail = "Semua")
            Spacer(modifier = Modifier.height(16.dp))
            when (monthlyCheck) {
                is Response.Loading -> {Log.d("Cek Bulanan", "Loading")}
                is Response.Success -> {
                    val data = (monthlyCheck as Response.Success<List<Alat>>).data
                    monthlyCheckCount.value = data?.size ?: 0

                    if (data.isNullOrEmpty()) {
                        Box(
                            modifier = Modifier
                                .padding(start = 16.dp, end = 16.dp)
                                .fillMaxWidth()
                                .height(130.dp)
                                .background(Color.White, shape = RoundedCornerShape(9)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "Tidak ada", color = Color.Black, fontSize = 16.sp)
                        }
                    }

                    LazyRow(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        item {
                            Spacer(modifier = Modifier.width(16.dp))
                        }
                        items(data!!.size) { index ->
                            val alat = data[index]
                            ItemContent(
                                model = alat.photoUrl!!,
                                title = alat.namaAlat!!,
                                noSeri = alat.noSeri!!,
                                unit = alat.unit!!,
                                date = convertFirebaseTimestampToString(alat.pengecekanBulanan!!),
                                isScanMonth = true,
                                onScanMonth = {
                                    coroutineScope.launch {
                                        navigateToMonthChecking.value = true
                                        viewModel.startScan()
                                    }
                                }
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                        }
                    }
                }

                is Response.Failure -> {Log.d("Cek Bulanan", "Failure")}
            }
            Spacer(modifier = Modifier.height(16.dp))
            TitleBar(title = "Kalibrasi Alat", detail = "Semua")
            Spacer(modifier = Modifier.height(16.dp))
            when (calibrationCheck) {
                is Response.Loading -> {}
                is Response.Success -> {
                    val data = (calibrationCheck as Response.Success<List<Alat>>).data
                    calibrationCheckCount.value = data?.size ?: 0

                    if (data.isNullOrEmpty()) {
                        Box(
                            modifier = Modifier
                                .padding(start = 16.dp, end = 16.dp)
                                .fillMaxWidth()
                                .height(130.dp)
                                .background(Color.White, shape = RoundedCornerShape(9)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "Tidak ada", color = Color.Black, fontSize = 16.sp)
                        }
                    }

                    LazyRow(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        item {
                            Spacer(modifier = Modifier.width(16.dp))
                        }
                        items(data!!.size) { index ->
                            val alat = data[index]
                            ItemContent(
                                model = alat.photoUrl!!,
                                title = alat.namaAlat!!,
                                noSeri = alat.noSeri!!,
                                unit = alat.unit!!,
                                date = convertFirebaseTimestampToString(alat.kalibrasi!!),
                                isScanCalibration = true,
                                onScanCalibration = {
                                    coroutineScope.launch {
                                        navigateToCalibrationChecking.value = true
                                        viewModel.startScan()
                                    }
                                }
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                        }
                    }

                }
                is Response.Failure -> {}
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}