package com.robbyari.monitoring.presentation.screen.daychecking

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.robbyari.monitoring.domain.model.Alat
import com.robbyari.monitoring.domain.model.Response
import com.robbyari.monitoring.presentation.components.ActionBarDetail
import com.robbyari.monitoring.presentation.components.DetailHeaderContent
import com.robbyari.monitoring.presentation.theme.Blue
import com.robbyari.monitoring.presentation.theme.MonitoringTheme

@Composable
fun DayCheckingScreen(
    id: String?,
    backHandler: () -> Unit,
    viewModel: DayCheckingViewModel = hiltViewModel()
) {
    BackHandler(onBack = backHandler)

    val detailState: Response<Alat> by viewModel.detail.collectAsState()

    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        ActionBarDetail(
            title = "Pengecekan Harian",
            navigateBack = backHandler,
            modifier = Modifier
                .align(Alignment.TopCenter)
        )

        TextButton(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .systemBarsPadding()
                .padding(start = 20.dp, end = 20.dp, bottom = 20.dp)
                .fillMaxWidth()
                .background(Blue, shape = RoundedCornerShape(20))
                .align(Alignment.BottomCenter)
        )
        {
            Text(
                text = "Kirim",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        when( detailState ) {
            is Response.Loading -> {Log.d("Loading", "")}
            is Response.Success -> {
                val data = (detailState as Response.Success<Alat>).data
                if (data != null) {
                    DetailHeaderContent(data = data)
                    Log.d("Cek data detail", data.namaAlat!!)
                }
            }
            is Response.Failure -> {Log.d("Failure", "")}
        }
        Log.d("Cek", if (id.isNullOrEmpty()) "Kosong bruh" else "")
    }
}



@Preview(showSystemUi = true, showBackground = true)
@Composable
fun DayCheckingScreenPreview() {
    MonitoringTheme {

    }
}