package com.robbyari.monitoring.presentation.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.robbyari.monitoring.domain.model.Alat
import com.robbyari.monitoring.domain.model.Response
import com.robbyari.monitoring.domain.model.User
import com.robbyari.monitoring.presentation.components.CardContent
import com.robbyari.monitoring.presentation.components.HomeActionBar
import com.robbyari.monitoring.presentation.components.ItemContent
import com.robbyari.monitoring.presentation.components.TitleBar
import com.robbyari.monitoring.presentation.theme.LightBlue
import com.robbyari.monitoring.utils.BottomShadow
import com.robbyari.monitoring.utils.convertFirebaseTimestampToString

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val emailState by viewModel.email.collectAsState()
    val user by viewModel.user.collectAsState()

    LaunchedEffect(emailState) {
        viewModel.getUser(emailState)
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (user) {
            is Response.Loading -> {}
            is Response.Success -> {
                val data = (user as Response.Success<User>).data
                if (data != null) {
                    HomeContent(user = data)
                }

            }

            is Response.Failure -> {}
        }
    }
}

@Composable
fun HomeContent(
    user: User,
    viewModel: HomeViewModel = hiltViewModel()
) {

    val dailyCheck: Response<List<Alat>> by viewModel.dailyCheck.collectAsState()

    Column {
        Spacer(modifier = Modifier.height(38.dp))
        HomeActionBar(user = user)
        Spacer(modifier = Modifier.height(16.dp))
        BottomShadow()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(LightBlue)
                .verticalScroll(rememberScrollState())
                .weight(1f, false)
        ) {
            Spacer(modifier = Modifier.height(11.dp))
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
                    CardContent(icon = Icons.Filled.ReportProblem, title = "Laporan Rusak", total = 0)
                    Spacer(modifier = Modifier.width(16.dp))
                }
                item {
                    CardContent(icon = Icons.Filled.WorkHistory, title = "Pengecekan Harian", total = 7)
                    Spacer(modifier = Modifier.width(16.dp))
                }
                item {
                    CardContent(icon = Icons.Filled.WorkHistory, title = "Pemeliharaan Bulanan", total = 2)
                    Spacer(modifier = Modifier.width(16.dp))
                }
                item {
                    CardContent(icon = Icons.Filled.WorkHistory, title = "Kalibrasi Alat", total = 0)
                    Spacer(modifier = Modifier.width(16.dp))
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            TitleBar(title = "Laporan Rusak", detail = "Semua")
            Box(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .height(130.dp)
                    .background(Color.White, shape = RoundedCornerShape(9)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Tidak ada", color = Color.Black, fontSize = 16.sp)
            }
            TitleBar(title = "Pengecekan Harian", detail = "Semua")
            Spacer(modifier = Modifier.height(16.dp))
            when (dailyCheck) {
                is Response.Loading -> {}
                is Response.Success -> {
                    val data = (dailyCheck as Response.Success<List<Alat>>).data
                    LazyRow(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        item {
                            Spacer(modifier = Modifier.width(16.dp))
                        }
                        items(data!!.size) {index ->
                            val alat = data[index]
                            ItemContent(
                                model = alat.photoUrl!!,
                                title = alat.namaAlat!!,
                                noSeri = alat.noSeri!!,
                                unit = alat.unit!!,
                                date = convertFirebaseTimestampToString(alat.pengecekanHarian!!)
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
            LazyRow(
                modifier = Modifier.fillMaxWidth()
            ) {
                item {
                    Spacer(modifier = Modifier.width(16.dp))
                }
                item {
                    ItemContent(
                        model = "https://upload.wikimedia.org/wikipedia/commons/1/10/Hospital_Patient_Monitor_%2817239884329%29.jpg",
                        title = "Patient Monitor",
                        noSeri = "RY/23/2023",
                        unit = "Flamboyan",
                        date = "Agustus 2023"
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            TitleBar(title = "Kalibrasi Alat", detail = "Semua")
            Spacer(modifier = Modifier.height(16.dp))
            LazyRow(
                modifier = Modifier.fillMaxWidth()
            ) {
                item {
                    Spacer(modifier = Modifier.width(16.dp))
                }
                item {
                    ItemContent(
                        model = "https://upload.wikimedia.org/wikipedia/commons/1/10/Hospital_Patient_Monitor_%2817239884329%29.jpg",
                        title = "Patient Monitor",
                        noSeri = "RY/23/2023",
                        unit = "Flamboyan",
                        date = "Agustus 2023"
                    )
                }
            }
            Spacer(modifier = Modifier.height(50.dp))
        }
    }
}