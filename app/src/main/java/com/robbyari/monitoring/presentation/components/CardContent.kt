package com.robbyari.monitoring.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.robbyari.monitoring.R
import com.robbyari.monitoring.presentation.theme.Blue
import com.robbyari.monitoring.presentation.theme.LightBlue
import com.robbyari.monitoring.utils.generateTimestamp

@Composable
fun CardContent(
    icon: ImageVector,
    title: String,
    total: Int,
    navigateToAllScreen: () -> Unit,
) {
    Box(modifier = Modifier
        .width(270.dp)
        .height(180.dp)
        .clip(RoundedCornerShape(7))
        .clickable { navigateToAllScreen() }
    ) {
        Image(
            painter = painterResource(id = R.drawable.bluebackground),
            contentDescription = "Image Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
        )
        Column(
            modifier = Modifier
                .width(270.dp)
                .height(180.dp)
                .clip(RoundedCornerShape(7))
        ) {
            Row(
                modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = "Total Alat",
                    tint = Blue,
                    modifier = Modifier
                        .background(color = LightBlue, shape = CircleShape)
                        .padding(8.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    fontSize = 16.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                )
            }
            Text(
                text = total.toString(),
                fontSize = 36.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 20.dp, end = 20.dp)
            )
            Divider(modifier = Modifier.padding(top = 8.dp))
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Update: ${generateTimestamp()}",
                    fontSize = 16.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                )
            }
        }
    }
}


