package com.robbyari.monitoring.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.ImagesearchRoller
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.robbyari.monitoring.R
import com.robbyari.monitoring.presentation.theme.Green
import com.valentinilk.shimmer.shimmer

@Composable
fun HistoryContent(
    title: String,
    unit: String,
    date: String,
    status: Boolean,
    showStatus: Boolean = false,
    photoAlat: String,
    notes: String,
    repairedBy: String = ""
) {
    val collapse = remember { mutableStateOf(false) }
    val painter = rememberAsyncImagePainter(photoAlat)
    val state = painter.state
    val transition by animateFloatAsState(
        targetValue = if (state is AsyncImagePainter.State.Success) 1f else 0f, label = ""
    )

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
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, top = 10.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                if (showStatus) {
                    Text(
                        text = if (!status) stringResource(R.string.belum_diperbaiki) else stringResource(R.string.sudah_diperbaiki),
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
                    maxLines = if (collapse.value) 10 else 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.width(LocalConfiguration.current.screenHeightDp.dp * 0.3f)
                )
                Text(
                    text = "$title ($unit)",
                    fontSize = 16.sp,
                    color = Color.Black,
                    maxLines = if (collapse.value) 10 else 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.width(LocalConfiguration.current.screenHeightDp.dp * 0.3f)
                )
                if (collapse.value) {
                    Text(
                        text = stringResource(R.string.diperbaiki_oleh) + repairedBy.ifEmpty { "-" },
                        fontSize = 16.sp,
                        color = Color.Black,
                        textAlign = TextAlign.Start,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.width(LocalConfiguration.current.screenHeightDp.dp * 0.3f)
                    )
                }
            }
            Box(modifier = Modifier.size(55.dp)) {
                when (state) {
                    is AsyncImagePainter.State.Error -> {
                        Icon(
                            Icons.Default.BrokenImage,
                            contentDescription = stringResource(id = R.string.photo_alat),
                            tint = Color.White,
                            modifier = Modifier
                                .size(55.dp)
                                .background(color = Color.Gray, shape = RoundedCornerShape(11))
                                .padding(8.dp)
                        )
                    }

                    is AsyncImagePainter.State.Loading -> {
                        Icon(
                            Icons.Default.ImagesearchRoller,
                            contentDescription = stringResource(id = R.string.photo_alat),
                            tint = Color.White,
                            modifier = Modifier
                                .shimmer()
                                .size(55.dp)
                                .background(color = Color.Gray, shape = RoundedCornerShape(11))
                                .padding(8.dp)
                        )
                    }
                    else -> {}
                }
                Image(
                    painter = painter,
                    contentDescription = stringResource(id = R.string.photo_alat),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(55.dp)
                        .clip(RoundedCornerShape(11))
                        .alpha(transition)
                )
            }
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
