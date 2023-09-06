package com.robbyari.monitoring.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.robbyari.monitoring.presentation.theme.Green

@Composable
fun HistoryContent(
    title: String,
    unit: String,
    date: String,
    status: Boolean,
    showStatus: Boolean = false,
    photoAlat: String,
    notes: String,
) {
    val collapse = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp)
            .fillMaxSize()
            .background(Color.White, RoundedCornerShape(7))
            .clip(RoundedCornerShape(7))
            .clickable {
                collapse.value = !collapse.value
            }

    ) {
        Row(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column {
                if (showStatus) {
                    Text(
                        text = if (!status) "Belum Diperbaiki" else "Sudah Diperbaiki",
                        fontSize = 16.sp,
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .background(if (!status) Color.Red else Green, RoundedCornerShape(20))
                            .padding(2.dp)
                    )
                }
                Text(
                    text = date,
                    fontSize = 16.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Start,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.width(270.dp)
                )
                Text(
                    text = "$title ($unit)",
                    fontSize = 16.sp,
                    color = Color.Black,
                    maxLines = if (collapse.value) 10 else 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.width(270.dp)
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            AsyncImage(
                model = photoAlat,
                contentDescription = "Photo alat",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(45.dp)
                    .width(45.dp)
                    .clip(RoundedCornerShape(11))
            )
        }
        Divider(modifier = Modifier.padding(top = 8.dp))
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "\"$notes\"",
                fontSize = 16.sp,
                color = Color.Black,
                textAlign = TextAlign.Start,
                maxLines = if (collapse.value) 10 else 1,
                overflow = TextOverflow.Ellipsis,
                fontStyle = FontStyle.Italic,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}
