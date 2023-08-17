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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.robbyari.monitoring.domain.model.Alat
import com.robbyari.monitoring.domain.model.Response
import com.robbyari.monitoring.presentation.components.ActionBarDetail
import com.robbyari.monitoring.presentation.components.BodyContentChecking
import com.robbyari.monitoring.presentation.components.DetailHeaderContent
import com.robbyari.monitoring.presentation.theme.Blue
import com.robbyari.monitoring.presentation.theme.MonitoringTheme
import com.robbyari.monitoring.utils.createImageFile
import com.robbyari.monitoring.utils.generateTimestamp
import java.util.Objects

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun DayCheckingScreen(
    id: String?,
    location: String?,
    backHandler: () -> Unit,
    isDistanceGreaterThan100Meters: Boolean,
    viewModel: DayCheckingViewModel = hiltViewModel()
) {
    BackHandler(onBack = backHandler)
    val context = LocalContext.current
    val file = context.createImageFile()
    val uri = FileProvider.getUriForFile(
        Objects.requireNonNull(context),
        "com.robbyari.monitoring.provider", file
    )

    var capturedImageUri by remember {
        mutableStateOf<Uri>(Uri.EMPTY)
    }

    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
            capturedImageUri = uri
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

    val detailState: Response<Alat> by viewModel.detail.collectAsState()

    Box(
        modifier = Modifier.fillMaxSize(),
    ) {

        when (detailState) {
            is Response.Loading -> {
                Log.d("Loading", "")
            }

            is Response.Success -> {
                val data = (detailState as Response.Success<Alat>).data
                if (data != null) {
                    Column(
                        Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .systemBarsPadding()) {
                        ActionBarDetail(
                            title = "Pengecekan Harian",
                            navigateBack = backHandler,
                            modifier = Modifier
                        )
                        DetailHeaderContent(data = data)
                        BodyContentChecking(
                            time = generateTimestamp(),
                            location = if (isDistanceGreaterThan100Meters) "isDistanceGreaterThan100Meters" else "RS Prikasih",
                            capturedImageUri = capturedImageUri.path?.isNotEmpty() == true,
                            painter = rememberAsyncImagePainter(capturedImageUri),
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

}



@Preview(showSystemUi = true, showBackground = true)
@Composable
fun DayCheckingScreenPreview() {
    MonitoringTheme {

    }
}