package com.robbyari.monitoring.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.robbyari.monitoring.R
import com.robbyari.monitoring.domain.model.Alat
import com.robbyari.monitoring.utils.convertFirebaseTimestampToString
import com.valentinilk.shimmer.shimmer

@Composable
fun DetailHeaderContent(
    data: Alat,
    dayChecking: Boolean = false,
    monthChecking: Boolean = false,
    calibrationChecking: Boolean = false,
) {
    val painter = rememberAsyncImagePainter(data.photoUrl)
    val state = painter.state
    val transition by animateFloatAsState(
        targetValue = if (state is AsyncImagePainter.State.Success) 1f else 0f, label = ""
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
    ) {
        Row(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp),
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
                                .size(65.dp)
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
                                .size(65.dp)
                                .background(color = Color.Gray, shape = RoundedCornerShape(11))
                                .padding(8.dp)
                        )
                    }
                    else -> {}
                }
                Image(
                    painter = painter,
                    contentDescription = stringResource(id = R.string.total_alat),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(65.dp)
                        .clip(RoundedCornerShape(11))
                        .alpha(transition)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = data.noSeri!!,
                    fontSize = 16.sp,
                    color = Color.Black,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.width(200.dp)
                )
                Text(
                    text = data.namaAlat!!,
                    fontSize = 16.sp,
                    color = Color.Black,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.width(280.dp)
                )
                Text(
                    text = data.unit!!,
                    fontSize = 16.sp,
                    color = Color.Black,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.width(200.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(Modifier.fillMaxWidth()) {
            Column(modifier = Modifier
                .weight(0.5f)
                .padding(start = 16.dp)) {
                Text(
                    text = stringResource(R.string.petugas_terakhir),
                    fontSize = 16.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Start,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = data.terakhirDicekOleh!!,
                    fontSize = 16.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Start,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(end = 16.dp)
                )
            }
            Column(modifier = Modifier
                .weight(0.5f)
                .padding(end = 16.dp)) {
                Text(
                    text = stringResource(R.string.terakhir_dicek),
                    fontSize = 16.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Start,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text =
                    when (true) {
                        dayChecking -> {
                            convertFirebaseTimestampToString(data.pengecekanHarian!!)
                        }
                        monthChecking -> {
                            convertFirebaseTimestampToString(data.pengecekanBulanan!!)
                        }
                        calibrationChecking -> {
                            convertFirebaseTimestampToString(data.kalibrasi!!)
                        }
                        else -> {
                            stringResource(id = R.string.tidak_ada)}
                    },
                    fontSize = 16.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Start,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(end = 16.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Divider(Modifier, 2.dp, color = Color.LightGray)
    }
}