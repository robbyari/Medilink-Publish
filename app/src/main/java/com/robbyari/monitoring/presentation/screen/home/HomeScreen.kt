package com.robbyari.monitoring.presentation.screen.home

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
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
import com.robbyari.monitoring.utils.convertFirebaseTimestampToString

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    navigateToDayChecking: (String) -> Unit,
    navigateToMonthChecking: (String) -> Unit,
    navigateToCalibrationChecking: (String) -> Unit,
    navigateToRepairScreen: (String) -> Unit,
    navigateToDetailAlat: (String) -> Unit,
    navigateToAllScreen: (String) -> Unit
) {
    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setSystemBarsColor(Color.Transparent, darkIcons = true)
        systemUiController.setNavigationBarColor(Color.Black)
    }

    val userDataStore by viewModel.userDataStore.collectAsState()


    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HomeContent(
            user = userDataStore,
            navigateToDayChecking = {navigateToDayChecking(it)},
            navigateToMonthChecking = {navigateToMonthChecking(it)},
            navigateToCalibrationChecking = {navigateToCalibrationChecking(it)},
            navigateToRepairScreen = {navigateToRepairScreen(it)},
            navigateToAllScreen = {navigateToAllScreen(it)},
            navigateToDetailAlat = {navigateToDetailAlat(it)}
        )
    }
}

@Composable
fun HomeContent(
    user: User,
    navigateToDayChecking: (String) -> Unit,
    navigateToMonthChecking: (String) -> Unit,
    navigateToCalibrationChecking: (String) -> Unit,
    navigateToRepairScreen: (String) -> Unit,
    navigateToAllScreen: (String) -> Unit,
    navigateToDetailAlat: (String) -> Unit,
    viewModel: HomeViewModel = hiltViewModel(),
) {

    val reportCheck by viewModel.reportProblemCheck.collectAsState()
    val reportCheckCount = remember { mutableStateOf(0) }
    val dailyCheck by viewModel.dailyCheck.collectAsState()
    val dailyCheckCount = remember { mutableStateOf(0) }
    val monthlyCheck by viewModel.monthlyCheck.collectAsState()
    val monthlyCheckCount = remember { mutableStateOf(0) }
    val calibrationCheck by viewModel.calibrationCheck.collectAsState()
    val calibrationCheckCount = remember { mutableStateOf(0) }
    val countItemAlat by viewModel.countItemAlat.collectAsState()

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
        Spacer(modifier = Modifier.height(6.dp))
        HomeActionBar(nameDataStore = "${user.firstName} ${user.lastName}", user = user)
        Spacer(modifier = Modifier.height(16.dp))
        Divider(thickness = 3.dp, color = Color.LightGray)
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
                    CardContent(
                        icon = Icons.Filled.HomeRepairService,
                        title = "Total Alat",
                        total = countItemAlat,
                        modifier = Modifier.clip(RoundedCornerShape(7)).clickable { navigateToAllScreen("alat") }
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                }
                item {
                    CardContent(
                        icon = Icons.Filled.ReportProblem,
                        title = "Laporan Rusak",
                        total = reportCheckCount.value,
                        modifier = Modifier.clip(RoundedCornerShape(7)).clickable { navigateToAllScreen("report") }
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                }
                item {
                    CardContent(
                        icon = Icons.Filled.WorkHistory,
                        title = "Pengecekan Harian",
                        total = dailyCheckCount.value,
                        modifier = Modifier.clip(RoundedCornerShape(7)).clickable { navigateToAllScreen("daychecking") }
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                }
                item {
                    CardContent(
                        icon = Icons.Filled.WorkHistory,
                        title = "Pemeliharaan Bulanan",
                        total = monthlyCheckCount.value,
                        modifier = Modifier.clip(RoundedCornerShape(7)).clickable { navigateToAllScreen("monthchecking") }
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                }
                item {
                    CardContent(
                        icon = Icons.Filled.WorkHistory,
                        title = "Kalibrasi Alat",
                        total = calibrationCheckCount.value,
                        modifier = Modifier.clip(RoundedCornerShape(7)).clickable { navigateToAllScreen("calibrationchecking") }
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            TitleBar(title = "Laporan Rusak", detail = "Semua", navigateToAllScreen = { navigateToAllScreen("report") })
            Spacer(modifier = Modifier.height(16.dp))
            when (reportCheck) {
                is Response.Loading -> {}
                is Response.Success -> {
                    val data = (reportCheck as Response.Success<List<ReportProblem>>).data
                    val filteredData = data?.filter { reportProblem ->
                        reportProblem.status == false
                    }

                    reportCheckCount.value = filteredData?.size ?: 0

                    if (filteredData.isNullOrEmpty()) {
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
                        items(filteredData!!.size) { index ->
                            val reportProblem = filteredData[index]
                            val screenWidth = LocalConfiguration.current.screenWidthDp.dp
                            ItemProblem(
                                title = reportProblem.namaAlat!!,
                                noSeri = reportProblem.noSeri!!,
                                unit = reportProblem.unit!!,
                                date = convertFirebaseTimestampToString(reportProblem.createdAt!!),
                                nameUser = reportProblem.nameUser!!,
                                photoUser = reportProblem.photoUser!!,
                                notes = reportProblem.notesUser!!,
                                status = reportProblem.status!!,
                                navigateToRepair = {
                                    navigateToRepairScreen(reportProblem.idReport!!)
                                },
                                modifier = Modifier
                                    .width(screenWidth)
                                    .padding(
                                        start = if (index == 0) 16.dp else 0.dp,
                                        end = 16.dp
                                    )
                                    .clip(RoundedCornerShape(7))
                                    .background(Color.White)
                            )
                        }
                    }
                }

                is Response.Failure -> {}
            }
            Spacer(modifier = Modifier.height(16.dp))
            TitleBar(title = "Pengecekan Harian", detail = "Semua", navigateToAllScreen = { navigateToAllScreen("daychecking") })
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
                        items(data!!.size) { index ->
                            val alat = data[index]
                            val screenWidth = LocalConfiguration.current.screenWidthDp.dp
                            ItemContent(
                                model = alat.photoUrl!!,
                                title = alat.namaAlat!!,
                                noSeri = alat.noSeri!!,
                                unit = alat.unit!!,
                                date = convertFirebaseTimestampToString(alat.pengecekanHarian!!),
                                isScanDay = true,
                                navigateToDayChecking = {
                                    navigateToDayChecking(alat.id!!)
                                },
                                navigateToDetailAlat = {navigateToDetailAlat(alat.id!!)},
                                modifier = Modifier
                                    .width(screenWidth)
                                    .padding(
                                        start = if (index == 0) 16.dp else 0.dp,
                                        end = 16.dp
                                    )
                                    .height(140.dp)
                                    .clip(RoundedCornerShape(7))
                                    .background(Color.White)
                            )
                        }
                    }
                }

                is Response.Failure -> {}
            }
            Spacer(modifier = Modifier.height(16.dp))
            TitleBar(title = "Pemeliharaan Bulanan", detail = "Semua", navigateToAllScreen = {navigateToAllScreen("monthchecking")})
            Spacer(modifier = Modifier.height(16.dp))
            when (monthlyCheck) {
                is Response.Loading -> {
                    Log.d("Cek Bulanan", "Loading")
                }

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
                        items(data!!.size) { index ->
                            val alat = data[index]
                            val screenWidth = LocalConfiguration.current.screenWidthDp.dp
                            ItemContent(
                                model = alat.photoUrl!!,
                                title = alat.namaAlat!!,
                                noSeri = alat.noSeri!!,
                                unit = alat.unit!!,
                                date = convertFirebaseTimestampToString(alat.pengecekanBulanan!!),
                                isScanMonth = true,
                                navigateToMonthChecking = {
                                    navigateToMonthChecking(alat.id!!)
                                },
                                navigateToDetailAlat = {navigateToDetailAlat(alat.id!!)},
                                modifier = Modifier
                                    .width(screenWidth)
                                    .padding(
                                        start = if (index == 0) 16.dp else 0.dp,
                                        end = 16.dp
                                    )
                                    .height(140.dp)
                                    .clip(RoundedCornerShape(7))
                                    .background(Color.White)
                            )
                        }
                    }
                }

                is Response.Failure -> {
                    Log.d("Cek Bulanan", "Failure")
                }

            }
            Spacer(modifier = Modifier.height(16.dp))
            TitleBar(title = "Kalibrasi Alat", detail = "Semua", navigateToAllScreen = {navigateToAllScreen("calibrationchecking")})
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
                        items(data!!.size) { index ->
                            val alat = data[index]
                            val screenWidth = LocalConfiguration.current.screenWidthDp.dp
                            ItemContent(
                                model = alat.photoUrl!!,
                                title = alat.namaAlat!!,
                                noSeri = alat.noSeri!!,
                                unit = alat.unit!!,
                                date = convertFirebaseTimestampToString(alat.kalibrasi!!),
                                isScanCalibration = true,
                                navigateToCalibrationChecking = {
                                    navigateToCalibrationChecking(alat.id!!)
                                },
                                navigateToDetailAlat = {navigateToDetailAlat(alat.id!!)},
                                modifier = Modifier
                                    .width(screenWidth)
                                    .padding(
                                        start = if (index == 0) 16.dp else 0.dp,
                                        end = 16.dp
                                    )
                                    .height(140.dp)
                                    .clip(RoundedCornerShape(7))
                                    .background(Color.White)
                            )
                        }
                    }

                }

                is Response.Failure -> {}
            }
            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}