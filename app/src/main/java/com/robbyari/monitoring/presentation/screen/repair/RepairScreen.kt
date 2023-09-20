package com.robbyari.monitoring.presentation.screen.repair

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.ImagesearchRoller
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImagePainter
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
import com.valentinilk.shimmer.shimmer
import kotlinx.coroutines.launch
import java.util.Objects

@Composable
fun RepairScreen(
    idReportProblem: String?,
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
            Toast.makeText(context, context.getString(R.string.permission_granted), Toast.LENGTH_SHORT).show()
            cameraLauncher.launch(uri)
        } else {
            Toast.makeText(context, context.getString(R.string.permission_denied), Toast.LENGTH_SHORT).show()
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
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .background(LightBlue)
        ) {
            when (detail) {
                is Response.Loading -> {}
                is Response.Success -> {
                    val data = (detail as Response.Success).data
                    if (data != null) {
                        ActionBarDetail(
                            title = stringResource(R.string.perbaikan),
                            navigateBack = navigateBack,
                            modifier = Modifier
                        )
                        RepairContent(
                            item = data,
                            notes = notes,
                            capturedImageUri = capturedImageUri.path?.isNotEmpty() == true,
                            painter = rememberAsyncImagePainter(capturedImageUri),
                            onQueryChange = onQueryChange,
                            repairedBy = "${userDataStore.name}",
                            time = timestampString,
                            location = if (isDistanceGreaterThan100Meters) stringResource(id = R.string.diluar_jangkauan) else stringResource(id = R.string.rs_prikasih),
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
                            role = data?.role,
                            notesUser = data?.notesUser,
                            notesRepair = notes,
                            photoTeknisi = userDataStore.photoUrl,
                            photoRepair = photoUrl,
                            repairedAt = convertStringToFirebaseTimestamp(timestampString),
                            status = true,
                            repairedBy = "${userDataStore.name}"
                        )
                        viewModel.updateToReportProblem(checkingItem)
                    }
                } else {
                    isLoading = false
                    Toast.makeText(context, context.getString(R.string.gambar_tidak_boleh_kosong), Toast.LENGTH_SHORT).show()
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
                    text = stringResource(id = R.string.kirim),
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
    painter: Painter = painterResource(id = R.drawable.medilinklogo512),
    repairedBy: String,
    time: String,
    location: String,
    notes: String,
    onQueryChange: (String) -> Unit,
    onClickPicture: () -> Unit,
) {
    val image = rememberAsyncImagePainter(item.photoUser)
    val state = image.state
    val transition by animateFloatAsState(
        targetValue = if (state is AsyncImagePainter.State.Success) 1f else 0f, label = ""
    )

    Column(modifier = Modifier) {
        Row(
            modifier = Modifier
                .background(Color.White)
                .padding(start = 16.dp, end = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier) {
                when (state) {
                    is AsyncImagePainter.State.Error -> {
                        Icon(
                            Icons.Default.BrokenImage,
                            contentDescription = stringResource(id = R.string.photo_alat),
                            tint = Color.White,
                            modifier = Modifier
                                .size(65.dp)
                                .background(color = Color.Gray, shape = RoundedCornerShape(11))
                                .padding(8.dp)
                        )
                    }

                    is AsyncImagePainter.State.Loading -> {
                        Icon(
                            Icons.Default.ImagesearchRoller,
                            contentDescription = stringResource(id = R.string.photo_alat),
                            tint = Color.White,
                            modifier = Modifier
                                .shimmer()
                                .size(65.dp)
                                .background(color = Color.Gray, shape = RoundedCornerShape(11))
                                .padding(8.dp)
                        )
                    }
                    else -> {}
                }
                Image(
                    painter = image,
                    contentDescription = stringResource(id = R.string.photo_alat),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(65.dp)
                        .clip(RoundedCornerShape(11))
                        .alpha(transition)
                )
            }
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
        Spacer(modifier = Modifier
            .height(8.dp)
            .fillMaxWidth()
            .background(Color.White))
        Text(
            text = stringResource(id = R.string.catatan),
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
        Spacer(modifier = Modifier
            .height(8.dp)
            .fillMaxWidth()
            .background(Color.White))
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
                    text = stringResource(R.string.petugas),
                    fontSize = 16.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Start,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = repairedBy,
                    fontSize = 16.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Start,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            Column(modifier = Modifier
                .weight(0.5f)
                .padding(start = 8.dp)) {
                Text(
                    text = stringResource(R.string.waktu_perbaikan),
                    fontSize = 16.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Start,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = time,
                    fontSize = 16.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Start,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(id = R.string.lokasi),
            fontSize = 16.sp,
            color = Color.Gray,
            textAlign = TextAlign.Start,
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
                                contentDescription = stringResource(id = R.string.foto_bukti),
                                modifier = Modifier.size(100.dp)
                            )
                            Text(
                                text = stringResource(id = R.string.upload),
                                fontSize = 16.sp,
                                color = Color.Gray,
                                modifier = Modifier
                            )
                        }
                        Image(
                            painter = painter,
                            contentScale = ContentScale.Fit,
                            contentDescription = stringResource(R.string.gambar_dari_kamera),
                            modifier = Modifier
                                .height(160.dp)
                                .width(120.dp)
                        )
                    }
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.baseline_image),
                        contentDescription = stringResource(id = R.string.foto_bukti),
                        modifier = Modifier.size(100.dp)
                    )
                    Text(
                        text = stringResource(id = R.string.upload),
                        fontSize = 16.sp,
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