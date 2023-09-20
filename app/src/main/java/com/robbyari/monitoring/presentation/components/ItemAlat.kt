package com.robbyari.monitoring.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.ImagesearchRoller
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.robbyari.monitoring.R
import com.valentinilk.shimmer.shimmer

@Composable
fun ItemAlat(
    model: String,
    title: String,
    noSeri: String,
    unit: String,
    navigateToDetailAlat: () -> Unit,
    modifier: Modifier = Modifier
) {
    val painter = rememberAsyncImagePainter(model)
    val state = painter.state
    val transition by animateFloatAsState(
        targetValue = if (state is AsyncImagePainter.State.Success) 1f else 0f, label = ""
    )

    Box(
        modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(7))
                .align(Alignment.Center)
                .clickable {
                    navigateToDetailAlat()
                }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 10.dp, bottom = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier) {
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
        }
    }
}