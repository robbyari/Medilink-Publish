package com.robbyari.monitoring.presentation.screen.detailalat

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.robbyari.monitoring.R
import com.robbyari.monitoring.domain.model.Alat
import com.robbyari.monitoring.domain.model.Response
import com.robbyari.monitoring.presentation.components.ActionBarDetail
import com.robbyari.monitoring.presentation.theme.Blue
import com.robbyari.monitoring.presentation.theme.LightBlue
import com.robbyari.monitoring.utils.convertFirebaseTimestamp
import com.robbyari.monitoring.utils.convertFirebaseTimestampToString
import com.valentinilk.shimmer.shimmer

@Composable
fun DetailAlatScreen(
    id: String?,
    navigateBack: () -> Unit,
    viewModel: DetailAlatViewModel = hiltViewModel()
) {
    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setSystemBarsColor(Color.White, darkIcons = true)
        systemUiController.setNavigationBarColor(Color.Black)
    }
    val detail by viewModel.detail.collectAsState()
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp * 0.9f

    LaunchedEffect(id) {
        viewModel.getDetail(id!!)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(LightBlue),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (detail) {
            is Response.Loading -> {
                ActionBarDetail(
                    title = stringResource(R.string.detail_alat),
                    navigateBack = navigateBack,
                    modifier = Modifier
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(screenHeight),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = Blue,
                        trackColor = Color.White,
                        modifier = Modifier.size(40.dp)
                    )
                }
            }

            is Response.Success -> {
                val item = (detail as Response.Success).data
                ActionBarDetail(
                    title = stringResource(R.string.detail_alat),
                    navigateBack = navigateBack,
                    modifier = Modifier
                )
                DetailAlatContent(
                    item = item!!
                )
            }

            is Response.Failure -> {}
        }
    }
}

@Composable
fun DetailAlatContent(
    item: Alat
) {
    val painter = rememberAsyncImagePainter(item.photoUrl)
    val state = painter.state
    val transition by animateFloatAsState(
        targetValue = if (state is AsyncImagePainter.State.Success) 1f else 0f, label = ""
    )

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
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
                    painter = painter,
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
                    text = item.namaAlat!!,
                    fontSize = 16.sp,
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
        Divider(Modifier.fillMaxWidth(), 2.dp, Color.LightGray)
        Spacer(modifier = Modifier.height(16.dp))
        Column(
            Modifier.fillMaxSize()
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .weight(0.5f)
                        .padding(end = 8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.statustext),
                        fontSize = 16.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Start,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text = item.status ?: stringResource(R.string.space),
                        fontSize = 16.sp,
                        color = Color.Black,
                        textAlign = TextAlign.Start,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                Column(
                    modifier = Modifier
                        .weight(0.5f)
                        .padding(start = 8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.tahun_inventaris),
                        fontSize = 16.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Start,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text = convertFirebaseTimestamp(item.tahunInventaris!!),
                        fontSize = 16.sp,
                        color = Color.Black,
                        textAlign = TextAlign.Start,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .weight(0.5f)
                        .padding(end = 8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.pengecekan_harian_text),
                        fontSize = 16.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Start,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text = if (item.cekHarian == true) stringResource(R.string.iya) else stringResource(R.string.tidak),
                        fontSize = 16.sp,
                        color = Color.Black,
                        textAlign = TextAlign.Start,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                Column(
                    modifier = Modifier
                        .weight(0.5f)
                        .padding(start = 8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.pengecekan_bulanan_text),
                        fontSize = 16.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Start,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text = if (item.cekBulanan == true) stringResource(R.string.iya) else stringResource(R.string.tidak),
                        fontSize = 16.sp,
                        color = Color.Black,
                        textAlign = TextAlign.Start,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .weight(0.5f)
                        .padding(end = 8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.pengecekan_kalibrasi_text),
                        fontSize = 16.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Start,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text = if (item.cekKalibrasi == true) stringResource(R.string.iya) else stringResource(R.string.tidak),
                        fontSize = 16.sp,
                        color = Color.Black,
                        textAlign = TextAlign.Start,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                Column(
                    modifier = Modifier
                        .weight(0.5f)
                        .padding(start = 8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.instansi_kalibrasi),
                        fontSize = 16.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Start,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text = item.instansiKalibrasi ?: "-",
                        fontSize = 16.sp,
                        color = Color.Black,
                        textAlign = TextAlign.Start,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .weight(0.5f)
                        .padding(end = 8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.merk),
                        fontSize = 16.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Start,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text = item.merk ?: "-",
                        fontSize = 16.sp,
                        color = Color.Black,
                        textAlign = TextAlign.Start,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                Column(
                    modifier = Modifier
                        .weight(0.5f)
                        .padding(start = 8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.supplier),
                        fontSize = 16.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Start,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text = item.supplier ?: "-",
                        fontSize = 16.sp,
                        color = Color.Black,
                        textAlign = TextAlign.Start,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Divider(Modifier.fillMaxWidth(), 1.dp, Color.LightGray)
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .weight(0.5f)
                        .padding(end = 8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.terakhir_dicek_oleh),
                        fontSize = 16.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Start,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text = item.terakhirDicekOleh ?: "-",
                        fontSize = 16.sp,
                        color = Color.Black,
                        textAlign = TextAlign.Start,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                Column(
                    modifier = Modifier
                        .weight(0.5f)
                        .padding(start = 8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.jadwal_harian),
                        fontSize = 16.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Start,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text = if (item.cekHarian == true) convertFirebaseTimestampToString(item.pengecekanHarian!!) else "-",
                        fontSize = 16.sp,
                        color = Color.Black,
                        textAlign = TextAlign.Start,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .weight(0.5f)
                        .padding(end = 8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.jadwal_bulanan),
                        fontSize = 16.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Start,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text = if (item.cekBulanan == true) convertFirebaseTimestampToString(item.pengecekanBulanan!!) else "-",
                        fontSize = 16.sp,
                        color = Color.Black,
                        textAlign = TextAlign.Start,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                Column(
                    modifier = Modifier
                        .weight(0.5f)
                        .padding(start = 8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.jadwal_kalibrasi),
                        fontSize = 16.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Start,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text = if (item.cekKalibrasi == true) convertFirebaseTimestampToString(item.kalibrasi!!) else "-",
                        fontSize = 16.sp,
                        color = Color.Black,
                        textAlign = TextAlign.Start,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Divider(Modifier.fillMaxWidth(), 1.dp, Color.LightGray)
            Spacer(modifier = Modifier.height(16.dp))
            Column(
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp)
            ) {
                Text(
                    text = stringResource(R.string.list_cek_harian),
                    fontSize = 16.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Start,
                    overflow = TextOverflow.Ellipsis,
                )
                val keys = item.listCekHarian?.keys?.toList() ?: emptyList()

                if (keys.isNotEmpty()) {
                    val formattedText = buildString {
                        for (i in 0 until keys.size - 1) {
                            append("${keys[i]}, ")
                        }
                        append("${keys.last()}.")
                    }

                    Text(
                        text = formattedText,
                        fontSize = 16.sp,
                        color = Color.Black,
                        textAlign = TextAlign.Start,
                        overflow = TextOverflow.Ellipsis,
                    )
                } else {
                    Text(
                        text = stringResource(R.string.space),
                        fontSize = 16.sp,
                        color = Color.Black,
                        textAlign = TextAlign.Start,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Divider(Modifier.fillMaxWidth(), 1.dp, Color.LightGray)
            Spacer(modifier = Modifier.height(16.dp))
            Column(
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp)
            ) {
                Text(
                    text = stringResource(R.string.list_cek_bulanan),
                    fontSize = 16.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Start,
                    overflow = TextOverflow.Ellipsis,
                )
                val keys = item.listCekBulanan?.keys?.toList() ?: emptyList()

                if (keys.isNotEmpty()) {
                    val formattedText = buildString {
                        for (i in 0 until keys.size - 1) {
                            append("${keys[i]}, ")
                        }
                        append("${keys.last()}.")
                    }

                    Text(
                        text = formattedText,
                        fontSize = 16.sp,
                        color = Color.Black,
                        textAlign = TextAlign.Start,
                        overflow = TextOverflow.Ellipsis,
                    )
                } else {
                    Text(
                        text = stringResource(R.string.space),
                        fontSize = 16.sp,
                        color = Color.Black,
                        textAlign = TextAlign.Start,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Divider(Modifier.fillMaxWidth(), 1.dp, Color.LightGray)
            Spacer(modifier = Modifier.height(16.dp))
            Column(
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp)
            ) {
                Text(
                    text = stringResource(R.string.list_cek_kalibrasi),
                    fontSize = 16.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Start,
                    overflow = TextOverflow.Ellipsis,
                )
                val keys = item.listCekKalibrasi?.keys?.toList() ?: emptyList()

                if (keys.isNotEmpty()) {
                    val formattedText = buildString {
                        for (i in 0 until keys.size - 1) {
                            append("${keys[i]}, ")
                        }
                        append("${keys.last()}.")
                    }

                    Text(
                        text = formattedText,
                        fontSize = 16.sp,
                        color = Color.Black,
                        textAlign = TextAlign.Start,
                        overflow = TextOverflow.Ellipsis,
                    )
                } else {
                    Text(
                        text = stringResource(R.string.space),
                        fontSize = 16.sp,
                        color = Color.Black,
                        textAlign = TextAlign.Start,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

