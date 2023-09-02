package com.robbyari.monitoring.presentation.screen.all

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.robbyari.monitoring.domain.model.Alat
import com.robbyari.monitoring.domain.model.ReportProblem
import com.robbyari.monitoring.domain.model.Response
import com.robbyari.monitoring.presentation.components.ActionBarDetail
import com.robbyari.monitoring.presentation.components.ItemAlat
import com.robbyari.monitoring.presentation.components.ItemContent
import com.robbyari.monitoring.presentation.components.ItemProblem
import com.robbyari.monitoring.presentation.components.SearchBar
import com.robbyari.monitoring.presentation.theme.LightBlue
import com.robbyari.monitoring.utils.convertFirebaseTimestampToString

@Composable
fun AllScreen(
    id: String?,
    navigateBack: () -> Unit,
    navigateToDayChecking: (String) -> Unit,
    navigateToMonthChecking: (String) -> Unit,
    navigateToCalibrationChecking: (String) -> Unit,
    navigateToRepairScreen: (String) -> Unit,
    viewModel: AllViewModel = hiltViewModel()
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
    val listAlat by viewModel.listAlat.collectAsState()
    val (searchQuery, setSearchQuery) = remember { mutableStateOf("") }
    val composition by rememberLottieComposition(LottieCompositionSpec.Asset("emptybox.json"))

    LaunchedEffect(id) {
        when (id) {
            "report" -> {
                viewModel.fetchReportProblem()
            }

            "daychecking" -> {
                viewModel.fetchDailyCheck()
            }

            "monthchecking" -> {
                viewModel.fetchMonthlyCheck()
            }

            "calibrationchecking" -> {
                viewModel.fetchCalibrationCheck()
            }

            else -> {
                viewModel.fetchListAlat()
            }
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
                else -> "Alat"
            },
            navigateBack = { navigateBack() },
            modifier = Modifier
        )
        Spacer(
            modifier = Modifier
                .height(16.dp)
                .fillMaxWidth()
                .background(Color.White)
        )
        when (id) {
            "report" -> {
                when (reportCheck) {
                    is Response.Loading -> {}
                    is Response.Success -> {
                        val data = (reportCheck as Response.Success<List<ReportProblem>>).data

                        val filteredData = if (searchQuery.isEmpty()) {
                            data
                        } else {
                            data?.filter { reportProblem ->
                                reportProblem.namaAlat?.contains(searchQuery, ignoreCase = true) == true
                                        || reportProblem.unit?.contains(searchQuery, ignoreCase = true) == true
                                        || reportProblem.nameUser?.contains(searchQuery, ignoreCase = true) == true
                            }
                        }

                        LazyColumn(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            item {
                                SearchBar(query = searchQuery, onQueryChange = setSearchQuery, trailingOnClick = { setSearchQuery("") }, trailingIcon = searchQuery.isNotEmpty())
                                Spacer(modifier = Modifier.height(10.dp))
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
                                        navigateToRepairScreen(reportProblem.idReport!!)
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(start = 16.dp, end = 16.dp)
                                        .clip(RoundedCornerShape(7))
                                        .background(Color.White)
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                            }
                            if (filteredData.isNullOrEmpty()) {
                                item {
                                    LottieAnimation(
                                        composition = composition,
                                        restartOnPlay = true,
                                        iterations = LottieConstants.IterateForever,
                                        contentScale = ContentScale.Fit,
                                        alignment = Alignment.Center,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(600.dp)
                                            .align(Alignment.CenterHorizontally)

                                    )
                                }
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

                        val filteredData = if (searchQuery.isEmpty()) {
                            data
                        } else {
                            data?.filter { alat ->
                                alat.namaAlat?.contains(searchQuery, ignoreCase = true) == true || alat.unit?.contains(searchQuery, ignoreCase = true) == true
                            }
                        }

                        LazyColumn(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            item {
                                SearchBar(query = searchQuery, onQueryChange = setSearchQuery, trailingOnClick = { setSearchQuery("") }, trailingIcon = searchQuery.isNotEmpty())
                                Spacer(modifier = Modifier.height(10.dp))
                            }
                            items(filteredData!!.size) { index ->
                                val alat = filteredData[index]
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
                                    modifier = Modifier
                                        .padding(start = 16.dp, end = 16.dp)
                                        .fillMaxWidth()
                                        .height(140.dp)
                                        .clip(RoundedCornerShape(7))
                                        .background(Color.White)
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                            }
                            if (filteredData.isNullOrEmpty()) {
                                item {
                                    LottieAnimation(
                                        composition = composition,
                                        restartOnPlay = true,
                                        iterations = LottieConstants.IterateForever,
                                        contentScale = ContentScale.Fit,
                                        alignment = Alignment.Center,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(600.dp)
                                            .align(Alignment.CenterHorizontally)

                                    )
                                }
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

                        val filteredData = if (searchQuery.isEmpty()) {
                            data
                        } else {
                            data?.filter { alat ->
                                alat.namaAlat?.contains(searchQuery, ignoreCase = true) == true || alat.unit?.contains(searchQuery, ignoreCase = true) == true
                            }
                        }

                        LazyColumn(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            item {
                                SearchBar(query = searchQuery, onQueryChange = setSearchQuery, trailingOnClick = { setSearchQuery("") }, trailingIcon = searchQuery.isNotEmpty())
                                Spacer(modifier = Modifier.height(10.dp))
                            }
                            items(filteredData!!.size) { index ->
                                val alat = filteredData[index]
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
                                    modifier = Modifier
                                        .padding(start = 16.dp, end = 16.dp)
                                        .fillMaxWidth()
                                        .height(140.dp)
                                        .clip(RoundedCornerShape(7))
                                        .background(Color.White)
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                            }
                            if (filteredData.isNullOrEmpty()) {
                                item {
                                    LottieAnimation(
                                        composition = composition,
                                        restartOnPlay = true,
                                        iterations = LottieConstants.IterateForever,
                                        contentScale = ContentScale.Fit,
                                        alignment = Alignment.Center,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(600.dp)
                                            .align(Alignment.CenterHorizontally)
                                    )
                                }
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

                        val filteredData = if (searchQuery.isEmpty()) {
                            data
                        } else {
                            data?.filter { alat ->
                                alat.namaAlat?.contains(searchQuery, ignoreCase = true) == true || alat.unit?.contains(searchQuery, ignoreCase = true) == true
                            }
                        }

                        LazyColumn(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            item {
                                SearchBar(query = searchQuery, onQueryChange = setSearchQuery, trailingOnClick = { setSearchQuery("") }, trailingIcon = searchQuery.isNotEmpty())
                                Spacer(modifier = Modifier.height(10.dp))
                            }
                            items(filteredData!!.size) { index ->
                                val alat = filteredData[index]
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
                                    modifier = Modifier
                                        .padding(start = 16.dp, end = 16.dp)
                                        .fillMaxWidth()
                                        .height(140.dp)
                                        .clip(RoundedCornerShape(7))
                                        .background(Color.White)
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                            }
                            if (filteredData.isNullOrEmpty()) {
                                item {
                                    LottieAnimation(
                                        composition = composition,
                                        restartOnPlay = true,
                                        iterations = LottieConstants.IterateForever,
                                        contentScale = ContentScale.Fit,
                                        alignment = Alignment.Center,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(600.dp)
                                            .align(Alignment.CenterHorizontally)

                                    )
                                }
                            }
                        }

                    }

                    is Response.Failure -> {}
                }
            }

            else -> {
                when (listAlat) {
                    is Response.Loading -> {}
                    is Response.Success -> {
                        val data = (listAlat as Response.Success<List<Alat>>).data

                        val filteredData = if (searchQuery.isEmpty()) {
                            data
                        } else {
                            data?.filter { alat ->
                                alat.namaAlat?.contains(searchQuery, ignoreCase = true) == true || alat.unit?.contains(searchQuery, ignoreCase = true) == true
                            }
                        }

                        LazyColumn(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            item {
                                SearchBar(query = searchQuery, onQueryChange = setSearchQuery, trailingOnClick = { setSearchQuery("") }, trailingIcon = searchQuery.isNotEmpty())
                                Spacer(modifier = Modifier.height(10.dp))
                            }
                            items(filteredData!!.size) { index ->
                                val alat = filteredData[index]
                                ItemAlat(
                                    model = alat.photoUrl!!,
                                    title = alat.namaAlat!!,
                                    noSeri = alat.noSeri!!,
                                    unit = alat.unit!!,
                                    navigateToDetailAlat = {},
                                    modifier = Modifier
                                        .padding(start = 16.dp, end = 16.dp)
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(7))
                                        .background(Color.White)
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                            }
                            if (filteredData.isNullOrEmpty()) {
                                item {
                                    LottieAnimation(
                                        composition = composition,
                                        restartOnPlay = true,
                                        iterations = LottieConstants.IterateForever,
                                        contentScale = ContentScale.Fit,
                                        alignment = Alignment.Center,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(600.dp)
                                            .align(Alignment.CenterHorizontally)

                                    )
                                }
                            }
                        }

                    }

                    is Response.Failure -> {}
                }
            }
        }
    }
}
