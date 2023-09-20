package com.robbyari.monitoring.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.robbyari.monitoring.R
import com.robbyari.monitoring.presentation.theme.LightBlue

@Composable
fun BodyContentChecking(
    onClickCamera: () -> Unit,
    capturedImageUri: Boolean = false,
    painter: Painter = painterResource(id = R.drawable.medilinklogo512),
    location: String? = "",
    time: String? = "",
    nameUser: String,
    checkedItems: SnapshotStateMap<String, Boolean>,
    checkedItemCount: Int,
    totalItemCount: Int,
    progressPercentage: Int,
    listCek: Map<String, Boolean>? = emptyMap(),
    notes: String,
    onQueryChange: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .background(LightBlue)
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp),
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        Row(Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.weight(0.5f)) {
                Text(
                    text = stringResource(R.string.petugas_hari_ini),
                    fontSize = 16.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Start,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = nameUser,
                    fontSize = 16.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Start,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(end = 16.dp)
                )
            }
            Column(modifier = Modifier.weight(0.5f)) {
                Text(
                    text = stringResource(R.string.waktu_pengecekan),
                    fontSize = 16.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Start,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = time ?: stringResource(R.string.empty),
                    fontSize = 16.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Start,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(end = 16.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.lokasi),
            fontSize = 16.sp,
            color = Color.Gray,
            textAlign = TextAlign.Start,
            overflow = TextOverflow.Ellipsis,
        )
        Text(
            text = location ?: stringResource(R.string.empty),
            fontSize = 16.sp,
            color = Color.Black,
            textAlign = TextAlign.Start,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(end = 16.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.pengecekan),
            fontSize = 16.sp,
            color = Color.Gray,
            textAlign = TextAlign.Start,
            overflow = TextOverflow.Ellipsis,
        )
        Spacer(modifier = Modifier.height(6.dp))
        Column {
            listCek?.forEach { (item, _) ->
                CheckboxItem(
                    item = item,
                    isChecked = checkedItems[item] ?: false,
                ) { isChecked ->
                    checkedItems[item] = isChecked
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        LinearProgressIndicator(
            progress = progressPercentage / 100f,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.selesai, checkedItemCount, totalItemCount),
            fontSize = 16.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(7))
                .clickable { onClickCamera() }
                .fillMaxWidth()
                .height(180.dp)
                .background(Color.White)
                .border(BorderStroke(1.dp, Color.LightGray), shape = RoundedCornerShape(7)),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (capturedImageUri) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.baseline_image),
                                contentDescription = stringResource(R.string.foto_bukti),
                                modifier = Modifier.size(100.dp)
                            )
                            Text(
                                text = stringResource(R.string.upload),
                                color = Color.Gray,
                                modifier = Modifier
                            )
                        }
                        Image(
                            painter = painter,
                            contentScale = ContentScale.Fit,
                            contentDescription = stringResource(R.string.image_from_camera),
                            modifier = Modifier
                                .height(160.dp)
                                .width(120.dp)
                        )
                    }
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.baseline_image),
                        contentDescription = stringResource(R.string.foto_bukti),
                        modifier = Modifier.size(100.dp)
                    )
                    Text(
                        text = stringResource(R.string.upload),
                        color = Color.Gray,
                        modifier = Modifier
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        TextFieldNote(notes, onQueryChange)
    }

}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TextFieldNote(
    notes: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    TextField(
        value = notes,
        onValueChange = onQueryChange,
        label = { Text(stringResource(R.string.catatan)) },
        colors = TextFieldDefaults.colors(
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Gray,
            disabledTextColor = Color.Transparent,
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            disabledContainerColor = Color.White,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
        ),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(
            onSearch = {
                keyboardController?.hide()
                focusManager.clearFocus()
            },
        ),
        modifier = modifier
            .clip(RoundedCornerShape(7))
            .height(180.dp)
            .fillMaxWidth()
            .border(BorderStroke(1.dp, Color.LightGray), shape = RoundedCornerShape(7))
    )
}