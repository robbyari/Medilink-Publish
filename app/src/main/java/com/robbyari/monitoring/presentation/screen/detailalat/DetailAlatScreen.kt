package com.robbyari.monitoring.presentation.screen.detailalat

import androidx.compose.foundation.background
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
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.robbyari.monitoring.domain.model.Alat
import com.robbyari.monitoring.domain.model.Response
import com.robbyari.monitoring.presentation.components.ActionBarDetail
import com.robbyari.monitoring.presentation.theme.LightBlue
import com.robbyari.monitoring.utils.convertFirebaseTimestamp
import com.robbyari.monitoring.utils.convertFirebaseTimestampToString

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

    LaunchedEffect(id) {
        viewModel.getDetail("Nu62hgGla4hD6qz")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(LightBlue)
    ) {
        when (detail) {
            is Response.Loading -> {}
            is Response.Success -> {
                val item = (detail as Response.Success).data
                ActionBarDetail(
                    title = "Detail Alat",
                    navigateBack = navigateBack,
                    modifier = Modifier
                )
                Spacer(
                    modifier = Modifier
                        .height(16.dp)
                        .fillMaxWidth()
                        .background(Color.White)
                )
                DetailAlatContent(
                    id = id,
                    item = item!!
                )
            }

            is Response.Failure -> {}
        }
    }
}

@Composable
fun DetailAlatContent(
    id: String?,
    item: Alat,
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .background(Color.White)
                .padding(start = 16.dp, end = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = item.photoUrl,
                contentDescription = "Photo alat",
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
                        text = "Status : ",
                        fontSize = 16.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Start,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text = item.status ?: "=",
                        fontSize = 16.sp,
                        color = Color.Black,
                        textAlign = TextAlign.Start,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                Column(
                    modifier = Modifier
                        .weight(0.5f)
                        .padding(start = 8.dp)
                ) {
                    Text(
                        text = "Tahun inventaris : ",
                        fontSize = 16.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Start,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text = convertFirebaseTimestamp(item.tahunInventaris!!),
                        fontSize = 16.sp,
                        color = Color.Black,
                        textAlign = TextAlign.Start,
                        maxLines = 2,
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
                        text = "Pengecekan harian : ",
                        fontSize = 16.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Start,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text = if (item.cekHarian == true) "Iya" else "Tidak",
                        fontSize = 16.sp,
                        color = Color.Black,
                        textAlign = TextAlign.Start,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                Column(
                    modifier = Modifier
                        .weight(0.5f)
                        .padding(start = 8.dp)
                ) {
                    Text(
                        text = "Pengecekan bulanan : ",
                        fontSize = 16.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Start,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text = if (item.cekBulanan == true) "Iya" else "Tidak",
                        fontSize = 16.sp,
                        color = Color.Black,
                        textAlign = TextAlign.Start,
                        maxLines = 2,
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
                        text = "Pengecekan kalibrasi : ",
                        fontSize = 16.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Start,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text = if (item.cekKalibrasi == true) "Iya" else "Tidak",
                        fontSize = 16.sp,
                        color = Color.Black,
                        textAlign = TextAlign.Start,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                Column(
                    modifier = Modifier
                        .weight(0.5f)
                        .padding(start = 8.dp)
                ) {
                    Text(
                        text = "Instansi kalibrasi : ",
                        fontSize = 16.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Start,
                        maxLines = 1,
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
                        text = "Merk : ",
                        fontSize = 16.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Start,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text = item.merk ?: "-",
                        fontSize = 16.sp,
                        color = Color.Black,
                        textAlign = TextAlign.Start,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                Column(
                    modifier = Modifier
                        .weight(0.5f)
                        .padding(start = 8.dp)
                ) {
                    Text(
                        text = "Supplier : ",
                        fontSize = 16.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Start,
                        maxLines = 1,
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
                        text = "Terakhir dicek oleh : ",
                        fontSize = 16.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Start,
                        maxLines = 1,
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
                        text = "Jadwal harian : ",
                        fontSize = 16.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Start,
                        maxLines = 1,
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
                        text = "Jadwal bulanan : ",
                        fontSize = 16.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Start,
                        maxLines = 1,
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
                        text = "Jadwal kalibrasi : ",
                        fontSize = 16.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Start,
                        maxLines = 1,
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
                    text = "List cek harian : ",
                    fontSize = 16.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Start,
                    maxLines = 1,
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
                        text = "-",
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
                    text = "List cek bulanan : ",
                    fontSize = 16.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Start,
                    maxLines = 1,
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
                        text = "-",
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
                    text = "List cek kalibrasi : ",
                    fontSize = 16.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Start,
                    maxLines = 1,
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
                        text = "-",
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

