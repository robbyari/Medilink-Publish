package com.robbyari.monitoring.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.robbyari.monitoring.presentation.theme.Blue

@Composable
fun TitleBar(
    title: String,
    detail: String
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            fontSize = 16.sp,
            color = Color.Black,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = detail,
            fontSize = 16.sp,
            color = Blue,
            fontWeight = FontWeight.Bold
        )
    }
}