package com.robbyari.monitoring.presentation.components

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
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
import com.robbyari.monitoring.presentation.theme.Blue

@Composable
fun ItemContent(
    model: String,
    title: String,
    noSeri: String,
    unit: String,
    date: String,
    isScanDay: Boolean = false,
    isScanMonth: Boolean = false,
    isScanCalibration: Boolean = false,
    navigateToDayChecking: () -> Unit = {},
    navigateToMonthChecking: () -> Unit = {},
    navigateToCalibrationChecking: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Box(
        modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(7))
                .align(Alignment.Center)
                .clickable {
                    when {
                        isScanDay -> navigateToDayChecking()
                        isScanMonth -> navigateToMonthChecking()
                        isScanCalibration -> navigateToCalibrationChecking()
                    }
                }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = model,
                    contentDescription = "Total Alat",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .height(45.dp)
                        .width(45.dp)
                        .clip(RoundedCornerShape(11))
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = title,
                        fontSize = 16.sp,
                        color = Color.Black,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.width(170.dp)
                    )
                    Text(
                        text = noSeri,
                        fontSize = 16.sp,
                        color = Color.Black,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.width(170.dp)
                    )
                    Text(
                        text = unit,
                        fontSize = 16.sp,
                        color = Color.Black,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.width(170.dp)
                    )
                }
            }
            Divider(modifier = Modifier.padding(top = 8.dp))
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Terakhir Dicek: $date",
                    fontSize = 16.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.width(350.dp)
                )
            }
        }
        Icon(
            imageVector = Icons.Default.Info,
            contentDescription = "Icon Scan",
            tint = Blue,
            modifier = Modifier
                .padding(top = 10.dp, end = 10.dp)
                .align(Alignment.TopEnd)
                .size(30.dp)
                .clip(CircleShape)
                .clickable {  }
        )
    }
}