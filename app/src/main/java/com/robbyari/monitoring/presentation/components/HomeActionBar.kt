package com.robbyari.monitoring.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.robbyari.monitoring.R
import com.robbyari.monitoring.domain.model.User
import com.robbyari.monitoring.presentation.theme.Blue
import com.robbyari.monitoring.utils.getGreeting
import com.valentinilk.shimmer.shimmer

@Composable
fun HomeActionBar(
    nameDataStore: String,
    user: User,
    roleUser: Boolean = false,
    onClickScan: () -> Unit = {},
    navigateToAccountScreen: () -> Unit = {}
) {
    val painter = rememberAsyncImagePainter(user.photoUrl)
    val state = painter.state
    val transition by animateFloatAsState(
        targetValue = if (state is AsyncImagePainter.State.Success) 1f else 0f, label = ""
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier) {
            when (state) {
                is AsyncImagePainter.State.Error -> {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = stringResource(id = R.string.photo_profile),
                        tint = Color.White,
                        modifier = Modifier
                            .size(55.dp)
                            .background(color = Color.LightGray, shape = CircleShape)
                            .padding(8.dp)
                    )
                }

                is AsyncImagePainter.State.Loading -> {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = stringResource(id = R.string.photo_profile),
                        tint = Color.White,
                        modifier = Modifier
                            .shimmer()
                            .size(55.dp)
                            .background(color = Color.Gray, shape = CircleShape)
                            .padding(8.dp)
                    )
                }
                else -> {}
            }
            Image(
                painter = painter,
                contentDescription = stringResource(id = R.string.photo_profile),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(55.dp)
                    .clip(CircleShape)
                    .alpha(transition)
                    .clickable { navigateToAccountScreen() }
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = getGreeting(),
                color = Color.Black,
                fontSize = 16.sp,
            )
            Text(
                text = nameDataStore,
                fontSize = 16.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.width(LocalConfiguration.current.screenWidthDp.dp * 0.5f)
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        if (roleUser) {
            Icon(
                Icons.Filled.QrCodeScanner,
                contentDescription = stringResource(R.string.scan_code),
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

