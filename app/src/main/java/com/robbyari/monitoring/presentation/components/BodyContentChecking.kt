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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.robbyari.monitoring.R
import com.robbyari.monitoring.presentation.theme.LightBlue

@Composable
fun BodyContentChecking(
    onClickCamera: () -> Unit,
    capturedImageUri: Boolean = false,
    painter: Painter = painterResource(id = R.drawable.logoprikasih),
    location: String? = "",
    time: String? = ""
) {
    Column(
        modifier = Modifier
            .background(LightBlue)
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp),
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Row(Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.weight(0.5f)) {
                Text(
                    text = "Petugas hari ini : ",
                    fontSize = 16.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Start,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = "Agus Sudrajat",
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
                    text = "Waktu pengecekan : ",
                    fontSize = 16.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Start,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = time ?: "Empty",
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
        Text(
            text = "Lokasi : ",
            fontSize = 16.sp,
            color = Color.Gray,
            textAlign = TextAlign.Start,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Text(
            text = location ?: "Empty",
            fontSize = 16.sp,
            color = Color.Black,
            textAlign = TextAlign.Start,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(end = 16.dp)
        )
        Spacer(modifier = Modifier.height(20.dp))
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(7))
                .clickable { onClickCamera() }
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
        TextFieldNote()
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextFieldNote() {
    var text by remember { mutableStateOf("Semua fungsi baik") }

    TextField(
        value = text,
        onValueChange = { text = it },
        label = { Text("Catatan:") },
        colors = TextFieldDefaults.textFieldColors(
            textColor = Color.Gray,
            disabledTextColor = Color.Transparent,
            containerColor = Color.White,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        ),
        modifier = Modifier
            .clip(RoundedCornerShape(7))
            .height(180.dp)
            .fillMaxWidth()
            .border(BorderStroke(1.dp, Color.LightGray), shape = RoundedCornerShape(7))
    )
}