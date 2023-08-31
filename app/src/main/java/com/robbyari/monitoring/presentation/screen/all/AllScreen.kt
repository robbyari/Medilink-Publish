package com.robbyari.monitoring.presentation.screen.all

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.robbyari.monitoring.domain.model.Alat
import com.robbyari.monitoring.domain.model.ReportProblem
import com.robbyari.monitoring.domain.model.Response
import com.robbyari.monitoring.presentation.components.ActionBarDetail
import com.robbyari.monitoring.presentation.components.ItemContent
import com.robbyari.monitoring.presentation.components.ItemProblem
import com.robbyari.monitoring.presentation.theme.LightBlue
import com.robbyari.monitoring.utils.convertFirebaseTimestampToString

@Composable
fun AllScreen(
    id: String?,
    navigateBack: () -> Unit,
    viewModel : AllViewModel = hiltViewModel()
) {
    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setSystemBarsColor(Color.White, darkIcons = true)
        systemUiController.setNavigationBarColor(Color.Black)
    }

    val reportCheck by viewModel.reportProblemCheck.collectAsState()
    val dailyCheck by viewModel.dailyCheck.collectAsState()
    val monthlyCheck by viewModel.monthlyCheck.collectAsState()
    val calibrationCheck by viewModel.calibrationCheck.collectAsState()

    LaunchedEffect(id) {
        when (id) {
            "report" -> {viewModel.fetchReportProblem()}
            "daychecking" -> {viewModel.fetchDailyCheck()}
            "monthchecking" -> {viewModel.fetchMonthlyCheck()}
            "calibrationchecking" -> {viewModel.fetchCalibrationCheck()}
            else -> {}
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .background(LightBlue)
    ) {
        ActionBarDetail(
            title = when (id) {
                "report" -> "Laporan Rusak"
                "daychecking" -> "Pengecekan Harian"
                "monthchecking" -> "Pengecekan Bulanan"
                "calibrationchecking" -> "Kalibrasi Alat"
                else -> ""
            },
            navigateBack = {navigateBack()},
            modifier = Modifier
        )
        Spacer(modifier = Modifier.height(16.dp).fillMaxWidth().background(Color.White))
        when (id) {
            "report" -> {
                when (reportCheck) {
                    is Response.Loading -> {}
                    is Response.Success -> {
                        val data = (reportCheck as Response.Success<List<ReportProblem>>).data
                        val filteredData = data?.filter { reportProblem ->
                            reportProblem.status == false
                        }

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

                        LazyColumn(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            item {
                                Spacer(modifier = Modifier.height(16.dp))
                            }
                            items(filteredData!!.size) { index ->
                                val reportProblem = filteredData[index]
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
                                        //navigateToRepairScreen(reportProblem.idReport!!)
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(start = 16.dp, end = 16.dp)
                                        .clip(RoundedCornerShape(7))
                                        .background(Color.White)
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                            }
                        }
                    }

                    is Response.Failure -> {}
                }
            }
            "daychecking" -> {
                when (dailyCheck) {
                    is Response.Loading -> {}
                    is Response.Success -> {
                        val data = (dailyCheck as Response.Success<List<Alat>>).data

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

                        LazyColumn(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            item {
                                Spacer(modifier = Modifier.height(16.dp))
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
                                    navigateToDayChecking = {
                                      //  navigateToDayChecking(alat.id!!)
                                    },
                                    modifier = Modifier
                                        .padding(start = 16.dp, end = 16.dp)
                                        .fillMaxWidth()
                                        .height(140.dp)
                                        .clip(RoundedCornerShape(7))
                                        .background(Color.White)
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                            }
                            item {
                                Spacer(modifier = Modifier.height(16.dp))
                            }
                        }
                    }
                    is Response.Failure -> {}
                }
            }
            "monthchecking" -> {
                when (monthlyCheck) {
                    is Response.Loading -> {
                        Log.d("Cek Bulanan", "Loading")
                    }

                    is Response.Success -> {
                        val data = (monthlyCheck as Response.Success<List<Alat>>).data

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

                        LazyColumn(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            item {
                                Spacer(modifier = Modifier.height(16.dp))
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
                                    navigateToMonthChecking = {
                                      //  navigateToMonthChecking(alat.id!!)
                                    },
                                    modifier = Modifier
                                        .padding(start = 16.dp, end = 16.dp)
                                        .fillMaxWidth()
                                        .height(140.dp)
                                        .clip(RoundedCornerShape(7))
                                        .background(Color.White)
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                            }
                        }
                    }
                    is Response.Failure -> {
                        Log.d("Cek Bulanan", "Failure")
                    }
                }
            }
            "calibrationchecking" -> {
                when (calibrationCheck) {
                    is Response.Loading -> {}
                    is Response.Success -> {
                        val data = (calibrationCheck as Response.Success<List<Alat>>).data

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

                        LazyColumn(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            item {
                                Spacer(modifier = Modifier.height(16.dp))
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
                                    navigateToCalibrationChecking = {
                                      //  navigateToCalibrationChecking(alat.id!!)
                                    },
                                    modifier = Modifier
                                        .padding(start = 16.dp, end = 16.dp)
                                        .fillMaxWidth()
                                        .height(140.dp)
                                        .clip(RoundedCornerShape(7))
                                        .background(Color.White)
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                            }
                        }

                    }

                    is Response.Failure -> {}
                }
            }
            else -> {}
        }
    }
}
