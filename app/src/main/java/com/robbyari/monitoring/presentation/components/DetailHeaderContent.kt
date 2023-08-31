package com.robbyari.monitoring.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import coil.compose.AsyncImage
import com.robbyari.monitoring.domain.model.Alat
import com.robbyari.monitoring.utils.convertFirebaseTimestampToString

@Composable
fun DetailHeaderContent(
    data: Alat,
    dayChecking: Boolean = false,
    monthChecking: Boolean = false,
    calibrationChecking: Boolean = false,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(top = 16.dp)
    ) {
        Row(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = data.photoUrl,
                contentDescription = "Photo Alat",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(65.dp)
                    .clip(RoundedCornerShape(11))
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = data.noSeri!!,
                    fontSize = 16.sp,
                    color = Color.Black,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.width(200.dp)
                )
                Text(
                    text = data.namaAlat!!,
                    fontSize = 20.sp,
                    color = Color.Black,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.width(280.dp)
                )
                Text(
                    text = data.unit!!,
                    fontSize = 16.sp,
                    color = Color.Black,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.width(200.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(Modifier.fillMaxWidth()) {
            Column(modifier = Modifier
                .weight(0.5f)
                .padding(start = 16.dp)) {
                Text(
                    text = "Petugas terakhir : ",
                    fontSize = 16.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Start,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = data.terakhirDicekOleh!!,
                    fontSize = 16.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Start,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(end = 16.dp)
                )
            }
            Column(modifier = Modifier
                .weight(0.5f)
                .padding(end = 16.dp)) {
                Text(
                    text = "Terakhir dicek : ",
                    fontSize = 16.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Start,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text =
                    when (true) {
                        dayChecking -> {
                            convertFirebaseTimestampToString(data.pengecekanHarian!!)
                        }
                        monthChecking -> {
                            convertFirebaseTimestampToString(data.pengecekanBulanan!!)
                        }
                        calibrationChecking -> {
                            convertFirebaseTimestampToString(data.kalibrasi!!)
                        }
                        else -> {"Tidak ada"}
                    },
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
        Divider(Modifier, 2.dp, color = Color.LightGray)
    }
}