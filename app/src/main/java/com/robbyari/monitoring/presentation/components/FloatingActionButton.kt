package com.robbyari.monitoring.presentation.components

import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.robbyari.monitoring.presentation.theme.Blue

@Composable
fun FloatingActionButton() {
    ExtendedFloatingActionButton(
        text = {
            Text(
                text = "Scan",
                fontSize = 16.sp,
                color = Color.White
            )
        },
        onClick = {},
        icon = {
            Icon(
                Icons.Filled.QrCodeScanner,
                "Icon Scan",
                tint = Color.White
            )
        },
        containerColor = Blue,
        modifier = Modifier.systemBarsPadding(),
        shape = RoundedCornerShape(15)
    )
}