package com.robbyari.monitoring.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
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
import com.robbyari.monitoring.presentation.theme.Blue
import com.robbyari.monitoring.presentation.theme.Green

@Composable
fun ItemProblem(
    title: String,
    noSeri: String,
    unit: String,
    date: String,
    status: Boolean,
    showStatus: Boolean = false,
    nameUser: String,
    photoUser: String,
    notes: String,
    navigateToRepair: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Box(
        modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(7))
                .align(Alignment.Center)
                .clickable {
                    navigateToRepair()
                }
        ) {
            Row(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = photoUser,
                    contentDescription = "Total Alat",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .height(45.dp)
                        .width(45.dp)
                        .clip(RoundedCornerShape(11))
                )
                Spacer(modifier = Modifier.width(8.dp))
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
                                .padding(start = 3.dp, end = 3.dp)
                        )
                    }
                    Text(
                        text = nameUser,
                        fontSize = 16.sp,
                        color = Color.Black,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.width(270.dp)
                    )
                    Text(
                        text = noSeri,
                        fontSize = 16.sp,
                        color = Color.Black,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.width(270.dp)
                    )
                    Text(
                        text = "$title ($unit)",
                        fontSize = 16.sp,
                        color = Color.Black,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.width(270.dp)
                    )
                }
            }
            Divider(modifier = Modifier.padding(top = 8.dp))
            Column(
                modifier = Modifier.background(Blue)
            ) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = date,
                    fontSize = 16.sp,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = "\"$notes\"",
                    fontSize = 16.sp,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontStyle = FontStyle.Italic,
                    modifier = Modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}