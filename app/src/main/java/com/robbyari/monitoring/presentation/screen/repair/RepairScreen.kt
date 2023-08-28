package com.robbyari.monitoring.presentation.screen.repair

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.robbyari.monitoring.domain.model.Response
import com.robbyari.monitoring.presentation.components.ActionBarDetail
import com.robbyari.monitoring.presentation.components.RepairBody
import com.robbyari.monitoring.presentation.theme.Blue

@Composable
fun RepairScreen(
    id: String?,
    location: String?,
    backHandler: () -> Unit,
    isDistanceGreaterThan100Meters: Boolean,
    navigateBack: () -> Unit,
    viewModel: RepairViewModel = hiltViewModel()
) {
    val detail by viewModel.detail.collectAsState()

    LaunchedEffect(id) {
        viewModel.getDetail("20230827152627")
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        Column(
            modifier = Modifier
        ) {
            ActionBarDetail(
                title = "Perbaikan",
                navigateBack = backHandler,
                modifier = Modifier
            )
            Spacer(modifier = Modifier.height(16.dp))
            when (detail) {
                is Response.Loading -> {}
                is Response.Success -> {
                    val data = (detail as Response.Success).data
                    if (data != null) {
                        RepairBody(data)
                    }
                }
                is Response.Failure -> {}
            }

        }
        TextButton(
            onClick = {

            },
            enabled = true,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(end = 16.dp, start = 16.dp, bottom = 16.dp)
                .height(50.dp)
                .background(Blue, shape = RoundedCornerShape(20))
        )
        {
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