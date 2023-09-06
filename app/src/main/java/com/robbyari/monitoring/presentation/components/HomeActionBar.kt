package com.robbyari.monitoring.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.robbyari.monitoring.domain.model.User
import com.robbyari.monitoring.presentation.theme.Blue

@Composable
fun HomeActionBar(
    nameDataStore: String,
    user: User,
    roleUser: Boolean = false,
    onClickScan: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (!user.photoUrl.isNullOrEmpty()) {
            AsyncImage(
                model = user.photoUrl,
                contentDescription = "Photo Profil",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(55.dp)
                    .clip(CircleShape)
            )
        } else {
            Icon(
                Icons.Default.Person,
                contentDescription = "Photo Profil",
                tint = Color.White,
                modifier = Modifier
                    .size(48.dp)
                    .background(color = Color.LightGray, shape = CircleShape)
                    .padding(8.dp)
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Selamat Pagi!",
                color = Color.Black,
                fontSize = 16.sp,
            )
            Text(
                text = nameDataStore,
                fontSize = 20.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        if (roleUser) {
            Icon(
                Icons.Filled.QrCodeScanner,
                contentDescription = "Scan Code",
                tint = Color.White,
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(20))
                    .clickable { onClickScan() }
                    .background(Blue, shape = RoundedCornerShape(20))
                    .border(2.dp, color = Color.LightGray, shape = RoundedCornerShape(20))
                    .padding(8.dp)
            )
        }
    }
}

