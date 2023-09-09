package com.robbyari.monitoring.presentation.screen.report

import android.widget.Toast
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.firebase.Timestamp
import com.robbyari.monitoring.domain.model.ReportProblem
import com.robbyari.monitoring.domain.model.Response
import com.robbyari.monitoring.presentation.components.ActionBarDetail
import com.robbyari.monitoring.presentation.components.ShowAlertDialog
import com.robbyari.monitoring.presentation.components.TextFieldNote
import com.robbyari.monitoring.presentation.theme.Blue
import com.robbyari.monitoring.presentation.theme.LightBlue
import com.robbyari.monitoring.utils.convertStringToFirebaseTimestamp
import com.robbyari.monitoring.utils.generateTimestamp
import com.valentinilk.shimmer.shimmer
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ReportScreen(
    id: String?,
    navigateBack: () -> Unit,
    viewModel: ReportViewModel = hiltViewModel()
) {
    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setSystemBarsColor(Color.White, darkIcons = true)
        systemUiController.setNavigationBarColor(Color.Black)
    }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp * 0.9f


    val detailAlat by viewModel.detailAlat.collectAsState()
    val userDataStore by viewModel.userDataStore.collectAsState()
    val (notes, onQueryChange) = remember { mutableStateOf("") }
    val timestampString = remember { mutableStateOf(generateTimestamp()) }
    val isLoading =  remember { mutableStateOf(false) }
    val addToReportProblem by viewModel.addToReportProblem.collectAsState()
    val showSuccessDialog = remember { mutableStateOf(false) }

    LaunchedEffect(id) {
        viewModel.fetchDetailAlat(id)
    }

    LaunchedEffect(addToReportProblem) {
        when (addToReportProblem) {
            is Response.Success -> {
                isLoading.value = false
                showSuccessDialog.value = true
            }
            else -> {}
        }
    }

    if (showSuccessDialog.value) {
        ShowAlertDialog {
            showSuccessDialog.value = false
            navigateBack()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(LightBlue)
        ) {
            ActionBarDetail(
                title = "Laporan Masalah",
                navigateBack = { navigateBack() },
                modifier = Modifier
            )
            Spacer(
                modifier = Modifier
                    .height(16.dp)
                    .fillMaxWidth()
                    .background(Color.White)
            )
            when (detailAlat) {
                is Response.Loading -> {
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .height(screenHeight),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(40.dp),
                            color = Blue,
                            trackColor = Color.White
                        )
                    }
                }
                is Response.Success -> {
                    val item = (detailAlat as Response.Success).data
                    Row(
                        modifier = Modifier
                            .background(Color.White)
                            .padding(start = 16.dp, end = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val painter = rememberAsyncImagePainter(item?.photoUrl)
                        val state = painter.state
                        val transition by animateFloatAsState(
                            targetValue = if (state is AsyncImagePainter.State.Success) 1f else 0f, label = ""
                        )

                        Box(modifier = Modifier) {
                            when (state) {
                                is AsyncImagePainter.State.Error -> {
                                    Icon(
                                        Icons.Default.BrokenImage,
                                        contentDescription = "Photo alat",
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
                                        contentDescription = "Photo alat",
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
                                painter = painter,
                                contentDescription = "Photo alat",
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
                                text = item?.noSeri!!,
                                fontSize = 16.sp,
                                color = Color.Black,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Text(
                                text = item.namaAlat!!,
                                fontSize = 20.sp,
                                color = Color.Black,
                                overflow = TextOverflow.Ellipsis,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Text(
                                text = item.unit!!,
                                fontSize = 16.sp,
                                color = Color.Black,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                    Spacer(
                        modifier = Modifier
                            .height(16.dp)
                            .fillMaxWidth()
                            .background(Color.White)
                    )
                    Divider(Modifier, 2.dp, Color.LightGray)
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp),
                    ) {
                        Column(modifier = Modifier.weight(0.5f)) {
                            Text(
                                text = "Pelapor : ",
                                fontSize = 16.sp,
                                color = Color.Gray,
                                textAlign = TextAlign.Start,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                            Text(
                                text = "${userDataStore.firstName} ${userDataStore.lastName}",
                                fontSize = 16.sp,
                                color = Color.Black,
                                textAlign = TextAlign.Start,
                                maxLines = 3,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.padding(end = 16.dp)
                            )
                        }
                        Column(modifier = Modifier.weight(0.5f)) {
                            Text(
                                text = "Waktu pelaporan : ",
                                fontSize = 16.sp,
                                color = Color.Gray,
                                textAlign = TextAlign.Start,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                            Text(
                                text = timestampString.value,
                                fontSize = 16.sp,
                                color = Color.Black,
                                textAlign = TextAlign.Start,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.padding(end = 16.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp)) {
                        Text(
                            text = "Divisi : ",
                            fontSize = 16.sp,
                            color = Color.Gray,
                            textAlign = TextAlign.Start,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                        Text(
                            text = userDataStore.divisi ?: "",
                            fontSize = 16.sp,
                            color = Color.Black,
                            textAlign = TextAlign.Start,
                            maxLines = 3,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(end = 16.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    TextFieldNote(notes = notes, onQueryChange = onQueryChange, modifier = Modifier.padding(start = 16.dp, end = 16.dp))
                }

                is Response.Failure -> {
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .height(screenHeight),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Data tidak ditemukan",
                            color = Color.Black,
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
        TextButton(
            onClick = {
                isLoading.value = true
                when (detailAlat) {
                    is Response.Success -> {
                        val data = (detailAlat as Response.Success).data
                        val timeStamp = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(Date())
                        val reportProblem = ReportProblem(
                            idReport = timeStamp,
                            idAlat = id,
                            photoUrl = data?.photoUrl,
                            namaAlat = data?.namaAlat,
                            noSeri = data?.noSeri,
                            unit = data?.unit,
                            idUser = userDataStore.uid,
                            nameUser = "${userDataStore.firstName} ${userDataStore.lastName}",
                            photoUser = userDataStore.photoUrl,
                            createdAt = convertStringToFirebaseTimestamp(timestampString.value),
                            divisi = userDataStore.divisi,
                            notesUser = notes,
                            notesRepair = "",
                            photoTeknisi = "",
                            repairedAt = Timestamp(0, 0),
                            photoRepair = "",
                            status = false,
                            repairedBy = "",
                        )
                        if (notes.isNotEmpty()) {
                            coroutineScope.launch {
                                viewModel.addToReportProblem(idDocument = timeStamp, item = reportProblem)
                            }
                        } else {
                            Toast.makeText(context, "Catatan tidak boleh kosong!", Toast.LENGTH_SHORT).show()
                        }
                    }
                    else -> {
                        Toast.makeText(context, "Data tidak ditemukan", Toast.LENGTH_SHORT).show()
                        isLoading.value = false
                    }
                }
            },
            enabled = !isLoading.value,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(end = 16.dp, start = 16.dp, bottom = 16.dp)
                .height(50.dp)
                .background(Blue, shape = RoundedCornerShape(20))
        )
        {
            if (isLoading.value) {
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