package com.robbyari.monitoring.presentation.screen.repair

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.robbyari.monitoring.R
import com.robbyari.monitoring.domain.model.ReportProblem
import com.robbyari.monitoring.domain.model.Response
import com.robbyari.monitoring.presentation.components.ActionBarDetail
import com.robbyari.monitoring.presentation.components.TextFieldNote
import com.robbyari.monitoring.presentation.theme.Blue
import com.robbyari.monitoring.presentation.theme.LightBlue
import com.robbyari.monitoring.utils.convertStringToFirebaseTimestamp
import com.robbyari.monitoring.utils.createImageFile
import com.robbyari.monitoring.utils.generateTimestamp
import kotlinx.coroutines.launch
import java.util.Objects

@Composable
fun RepairScreen(
    idReportProblem: String?,
    location: String?,
    isDistanceGreaterThan100Meters: Boolean,
    navigateBack: () -> Unit,
    viewModel: RepairViewModel = hiltViewModel()
) {
    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setSystemBarsColor(Color.White, darkIcons = true)
        systemUiController.setNavigationBarColor(Color.Black)
    }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val detail by viewModel.detail.collectAsState()
    val userDataStore by viewModel.userDataStore.collectAsState()
    val timestampString by remember { mutableStateOf(generateTimestamp()) }
    val (notes, onQueryChange) = remember { mutableStateOf("Sudah diperbaiki") }
    var isLoading by remember { mutableStateOf(false) }

    val updateToReportProblem by viewModel.updateToReportProblem.collectAsState()

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

    LaunchedEffect(idReportProblem) {
        viewModel.getDetail(idReportProblem)
    }

    LaunchedEffect(updateToReportProblem) {
        when (updateToReportProblem) {
            is Response.Success -> {
                navigateBack()
                isLoading = false
            }

            else -> {}
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState()).background(LightBlue)
        ) {
            when (detail) {
                is Response.Loading -> {}
                is Response.Success -> {
                    val data = (detail as Response.Success).data
                    if (data != null) {
                        ActionBarDetail(
                            title = "Perbaikan",
                            navigateBack = navigateBack,
                            modifier = Modifier
                        )
                        Spacer(modifier = Modifier.height(16.dp).fillMaxWidth().background(Color.White))
                        RepairContent(
                            item = data,
                            notes = notes,
                            capturedImageUri = capturedImageUri.path?.isNotEmpty() == true,
                            painter = rememberAsyncImagePainter(capturedImageUri),
                            onQueryChange = onQueryChange,
                            repairedBy = "${userDataStore.firstName} ${userDataStore.lastName}",
                            time = timestampString,
                            location = if (isDistanceGreaterThan100Meters) "Diluar Jangkauan" else "RS Prikasih",
                            onClickPicture = {
                                val permissionCheckResult = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                                if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                                    cameraLauncher.launch(uri)
                                } else {
                                    cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                                }
                            }
                        )
                    }
                }

                is Response.Failure -> {}
            }

        }
        TextButton(
            onClick = {
                isLoading = true
                if (capturedImageUri != Uri.EMPTY) {
                    coroutineScope.launch {
                        val data = (detail as Response.Success<ReportProblem>).data
                        val photoUrl = viewModel.addImageToStorage(capturedImageUri)

                        val checkingItem = ReportProblem(
                            idReport = data?.idReport,
                            idAlat = data?.idAlat,
                            photoUrl = data?.photoUrl,
                            namaAlat = data?.namaAlat,
                            noSeri = data?.noSeri,
                            unit = data?.unit,
                            nameUser = data?.nameUser,
                            photoUser = data?.photoUser,
                            createdAt = data?.createdAt,
                            divisi = data?.divisi,
                            notesUser = data?.notesUser,
                            notesRepair = notes,
                            photoTeknisi = userDataStore.photoUrl,
                            photoRepair = photoUrl,
                            repairedAt = convertStringToFirebaseTimestamp(timestampString),
                            status = true,
                            repairedBy = "${userDataStore.firstName} ${userDataStore.lastName}"
                        )
                        viewModel.updateToReportProblem(checkingItem)
                    }
                } else {
                    isLoading = false
                    Toast.makeText(context, "Gambar tidak boleh kosong!", Toast.LENGTH_SHORT).show()
                }
            },
            enabled = !isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(end = 16.dp, start = 16.dp, bottom = 16.dp)
                .height(50.dp)
                .background(Blue, shape = RoundedCornerShape(20))
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

@Composable
fun RepairContent(
    item: ReportProblem,
    capturedImageUri: Boolean = false,
    painter: Painter = painterResource(id = R.drawable.logoprikasih),
    repairedBy: String,
    time: String,
    location: String,
    notes: String,
    onQueryChange: (String) -> Unit,
    onClickPicture: () -> Unit,
) {
    Column(modifier = Modifier) {
        Row(
            modifier = Modifier.background(Color.White).padding(start = 16.dp, end = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = item.photoUser,
                contentDescription = "Photo Alat",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(65.dp)
                    .clip(RoundedCornerShape(11))
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = item.noSeri!!,
                    fontSize = 16.sp,
                    color = Color.Black,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = "${item.namaAlat} (${item.unit})",
                    fontSize = 20.sp,
                    color = Color.Black,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = item.nameUser!!,
                    fontSize = 16.sp,
                    color = Color.Black,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp).fillMaxWidth().background(Color.White))
        Text(
            text = "Catatan: ",
            fontSize = 16.sp,
            color = Color.Gray,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(start = 16.dp, end = 16.dp)
        )
        Text(
            text = item.notesUser!!,
            fontSize = 16.sp,
            color = Color.Black,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(start = 16.dp, end = 16.dp)
        )
        Spacer(modifier = Modifier.height(8.dp).fillMaxWidth().background(Color.White))
        Divider(Modifier, 2.dp, color = Color.LightGray)
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp)) {
            Column(modifier = Modifier
                .weight(0.5f)
                .padding(end = 8.dp)) {
                Text(
                    text = "Petugas : ",
                    fontSize = 16.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Start,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = repairedBy,
                    fontSize = 16.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Start,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            Column(modifier = Modifier
                .weight(0.5f)
                .padding(start = 8.dp)) {
                Text(
                    text = "Waktu perbaikan : ",
                    fontSize = 16.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Start,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = time,
                    fontSize = 16.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Start,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Lokasi : ",
            fontSize = 16.sp,
            color = Color.Gray,
            textAlign = TextAlign.Start,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp)
        )
        Text(
            text = location,
            fontSize = 16.sp,
            color = Color.Black,
            textAlign = TextAlign.Start,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp)
        )
        Spacer(modifier = Modifier.height(25.dp))
        Row(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp)
                .clip(RoundedCornerShape(7))
                .clickable { onClickPicture() }
                .fillMaxWidth()
                .height(180.dp)
                .background(Color.White)
                .border(BorderStroke(1.dp, Color.LightGray), shape = RoundedCornerShape(7)),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (capturedImageUri) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.baseline_image),
                                contentDescription = "Foto Bukti",
                                modifier = Modifier.size(100.dp)
                            )
                            Text(
                                text = "Upload",
                                color = Color.Gray,
                                modifier = Modifier
                            )
                        }
                        Image(
                            painter = painter,
                            contentScale = ContentScale.Fit,
                            contentDescription = "Image from camera",
                            modifier = Modifier
                                .height(160.dp)
                                .width(120.dp)
                        )
                    }
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.baseline_image),
                        contentDescription = "Foto Bukti",
                        modifier = Modifier.size(100.dp)
                    )
                    Text(
                        text = "Upload",
                        color = Color.Gray,
                        modifier = Modifier
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        TextFieldNote(notes, onQueryChange, Modifier.padding(start = 16.dp, end = 16.dp))
        Spacer(modifier = Modifier.height(100.dp))
    }
}