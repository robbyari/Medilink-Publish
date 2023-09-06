package com.robbyari.monitoring.presentation.screen.user

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.robbyari.monitoring.domain.model.ReportProblem
import com.robbyari.monitoring.domain.model.Response
import com.robbyari.monitoring.domain.model.User
import com.robbyari.monitoring.presentation.components.CardContent
import com.robbyari.monitoring.presentation.components.HistoryContent
import com.robbyari.monitoring.presentation.components.HomeActionBar
import com.robbyari.monitoring.presentation.theme.Blue
import com.robbyari.monitoring.presentation.theme.LightBlue
import com.robbyari.monitoring.utils.convertFirebaseTimestampToString

@Composable
fun UserScreen(
    viewModel: UserViewModel = hiltViewModel()
) {
    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setSystemBarsColor(Color.White, darkIcons = true)
        systemUiController.setNavigationBarColor(Color.Black)
    }

    val userDataStore by viewModel.userDataStore.collectAsState()
    val barcodeResult: Response<String> by viewModel.barcodeResult.collectAsState()

    LaunchedEffect(barcodeResult) {
        when (barcodeResult) {
            is Response.Success -> {
                val scannedValue = (barcodeResult as Response.Success<String>).data
                if (scannedValue != null) {

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
        UserContent(
            user = userDataStore
        )
    }
}


@Composable
fun UserContent(
    user: User,
    viewModel: UserViewModel = hiltViewModel()
) {
    val reportProblem by viewModel.reportProblem.collectAsState()
    val composition by rememberLottieComposition(LottieCompositionSpec.Asset("emptybox.json"))
    val reportCount = remember { mutableStateOf(0) }
    val reportActiveCount = remember { mutableStateOf(0) }
    val reportDoneCount = remember { mutableStateOf(0) }

    Spacer(modifier = Modifier.height(6.dp))
    HomeActionBar(
        nameDataStore = "${user.firstName} ${user.lastName}",
        user = user,
        roleUser = true,
        onClickScan = {}
    )
    Spacer(modifier = Modifier.height(16.dp))
    Divider(thickness = 3.dp, color = Color.LightGray)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(LightBlue),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
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
                        title = "Laporan Aktif",
                        total = reportActiveCount.value,
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                }
                item {
                    CardContent(
                        icon = Icons.Filled.ReportProblem,
                        title = "Laporan Selesai",
                        total = reportDoneCount.value,
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                }
                item {
                    CardContent(
                        icon = Icons.Filled.WorkHistory,
                        title = "Total Laporan",
                        total = reportCount.value,
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Histori",
                    fontSize = 16.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "(${reportCount.value})",
                    fontSize = 16.sp,
                    color = Blue,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                )
            }
        }
        when (reportProblem) {
            is Response.Loading -> {}
            is Response.Success -> {
                val data = (reportProblem as Response.Success<List<ReportProblem>>).data

                val filteredData = data?.reversed()?.filter { reportProblem ->
                    reportProblem.idUser == user.uid
                }

                val reportActive = data?.filter { reportProblem ->
                    reportProblem.status == false && reportProblem.idUser == user.uid
                }

                val reportDone = data?.filter { reportProblem ->
                    reportProblem.status == true && reportProblem.idUser == user.uid
                }

                reportActiveCount.value = reportActive?.size ?: 0
                reportDoneCount.value = reportDone?.size ?: 0
                reportCount.value = filteredData?.size ?: 0

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
                items(filteredData!!.size) { index ->
                    val item = filteredData[index]
                    HistoryContent(
                        title = item.namaAlat!!,
                        unit = item.unit!!,
                        date = convertFirebaseTimestampToString(item.createdAt!!),
                        photoAlat = item.photoUrl!!,
                        notes = item.notesUser!!,
                        status = item.status!!,
                        showStatus = true
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }
                if (filteredData.isEmpty()) {
                    item {
                        LottieAnimation(
                            composition = composition,
                            restartOnPlay = true,
                            iterations = LottieConstants.IterateForever,
                            contentScale = ContentScale.Crop,
                            alignment = Alignment.Center,
                            modifier = Modifier
                                .padding(top = 80.dp)
                                .size(300.dp)
                        )
                    }
                }
            }
            is Response.Failure -> {}
        }
        item {
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}