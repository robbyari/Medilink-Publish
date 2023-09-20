package com.robbyari.monitoring.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.shimmer

@Composable
fun BoxLoadingData() {
    Box(
        Modifier
            .shimmer()
            .padding(start = 16.dp, end = 16.dp)
            .background(Color.Gray, shape = RoundedCornerShape(7))
            .height(130.dp)
            .fillMaxWidth()
    )
}