package com.robbyari.monitoring.presentation.components

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.robbyari.monitoring.R
import com.robbyari.monitoring.domain.model.ReportProblem

@Composable
fun RepairBody(
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