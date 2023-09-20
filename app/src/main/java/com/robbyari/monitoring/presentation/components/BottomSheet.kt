package com.robbyari.monitoring.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.robbyari.monitoring.R
import com.robbyari.monitoring.domain.model.Alat
import com.robbyari.monitoring.domain.model.User
import com.robbyari.monitoring.presentation.theme.Blue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(
    onDismiss: () -> Unit,
    time: String,
    alat: Alat,
    user: User,
    notes: String,
    submit: () -> Unit,
    onQueryChange: (String) -> Unit,
    showLoading: Boolean
) {
    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setNavigationBarColor(Color.Black, darkIcons = true)
    }

    val modalBottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )

    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = modalBottomSheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() },
        containerColor = Color.White,
    ) {
        Text(
            text = stringResource(R.string.laporkan_masalah),
            fontSize = 16.sp,
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(20.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp)
            ) {
                AsyncImage(
                    model = alat.photoUrl,
                    contentDescription = stringResource(R.string.photo_alat),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(65.dp)
                        .clip(RoundedCornerShape(11))
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = alat.noSeri!!,
                        fontSize = 16.sp,
                        color = Color.Black,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.width(200.dp)
                    )
                    Text(
                        text = alat.namaAlat!!,
                        fontSize = 16.sp,
                        color = Color.Black,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.width(280.dp)
                    )
                    Text(
                        text = alat.unit!!,
                        fontSize = 16.sp,
                        color = Color.Black,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.width(200.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Divider(Modifier.fillMaxWidth(), 2.dp, Color.LightGray)
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp),
            ) {
                Column(modifier = Modifier.weight(0.5f)) {
                    Text(
                        text = stringResource(R.string.pelapor),
                        fontSize = 16.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Start,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text = "${user.name }",
                        fontSize = 16.sp,
                        color = Color.Black,
                        textAlign = TextAlign.Start,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(end = 16.dp)
                    )
                }
                Column(modifier = Modifier.weight(0.5f)) {
                    Text(
                        text = stringResource(R.string.waktu_pelaporan),
                        fontSize = 16.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Start,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text = time,
                        fontSize = 16.sp,
                        color = Color.Black,
                        textAlign = TextAlign.Start,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(end = 16.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp)) {
                Text(
                    text = stringResource(R.string.divisi),
                    fontSize = 16.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Start,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = user.role ?: "",
                    fontSize = 16.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Start,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(end = 16.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            TextFieldNote(notes, onQueryChange, Modifier.padding(start = 16.dp, end = 16.dp))
            Spacer(modifier = Modifier.height(20.dp))
            TextButton(
                onClick = submit,
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp)
                    .height(50.dp)
                    .background(Blue, shape = RoundedCornerShape(20))
                    .fillMaxWidth()
            )
            {
                if (showLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(30.dp))
                } else {
                    Text(
                        text = stringResource(R.string.kirim),
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            Spacer(modifier = Modifier.height(50.dp))
        }
    }
}


