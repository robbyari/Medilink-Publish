package com.robbyari.monitoring.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.robbyari.monitoring.presentation.theme.LightBlue

@Composable
fun ItemContent(
    model: String,
    title: String,
    noSeri: String,
    unit: String,
    date: String,
) {
    Column(
        modifier = Modifier
            .width(380.dp)
            .height(140.dp)
            .clip(RoundedCornerShape(7))
            .background(Color.White)
    ) {
        Row(
            modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = model,
                contentDescription = "Total Alat",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(40.dp)
                    .width(40.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = noSeri,
                    fontSize = 16.sp,
                    color = Color.Black,
                )
            }
            Spacer(modifier = Modifier.width(8.dp).weight(1f))
            Text(
                text = unit,
                fontSize = 16.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .background(LightBlue, shape = RoundedCornerShape(20))
                    .padding(8.dp)
            )
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
                modifier = Modifier
            )
        }
    }
}