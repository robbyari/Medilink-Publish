package com.robbyari.monitoring.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.robbyari.monitoring.R
import com.robbyari.monitoring.presentation.theme.Blue
import com.robbyari.monitoring.presentation.theme.LightBlue

@Composable
fun AlertDeleteAccount(
    onDelete: () -> Unit,
    onDismissRequest: () -> Unit
) {
    Column {
        AlertDialog(
            onDismissRequest = onDismissRequest,
            title = {
                Text(
                    text = stringResource(R.string.hapus_akun),
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    fontSize = 20.sp,
                    maxLines = 1
                )
            },
            containerColor = LightBlue,
            confirmButton = {
                Column(
                    modifier = Modifier,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.apakah_kamu_ingin_menghapus_akun),
                        textAlign = TextAlign.Center,
                        color = Color.Black,
                        fontSize = 16.sp,
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            onClick = onDismissRequest,
                            colors = ButtonDefaults.buttonColors(containerColor = Blue),
                            shape = RoundedCornerShape(20),
                            modifier = Modifier
                                .weight(1f)
                                .height(52.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.batal),
                                color = Color.White,
                                fontSize = 16.sp,
                                maxLines = 1
                            )
                        }
                        Spacer(modifier = Modifier.width(15.dp))
                        Button(
                            onClick = onDelete,
                            colors = ButtonDefaults.buttonColors(containerColor = Blue),
                            shape = RoundedCornerShape(20),
                            modifier = Modifier
                                .weight(1f)
                                .height(52.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.hapus),
                                color = Color.White,
                                fontSize = 16.sp,
                                maxLines = 1
                            )
                        }
                    }
                }
            },
            icon = {
                Icon(
                    imageVector = Icons.Outlined.Delete,
                    contentDescription = stringResource(R.string.delete_icon),
                    tint = Color.Black,
                    modifier = Modifier.size(50.dp)
                )
            },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        )
    }
}