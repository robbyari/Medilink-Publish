package com.robbyari.monitoring.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.robbyari.monitoring.R
import com.robbyari.monitoring.presentation.theme.Blue

@Composable
fun ShowAlertRegister(
    close: () -> Unit,
    role: Boolean = false
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.Asset("success.json"))
    AlertDialog(
        onDismissRequest = close,
        confirmButton = {
            Column(
                modifier = Modifier,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LottieAnimation(
                    composition = composition,
                    restartOnPlay = true,
                    iterations = LottieConstants.IterateForever,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(200.dp),
                )
                Text(
                    text = stringResource(id = R.string.success),
                    textAlign = TextAlign.Center,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.height(10.dp))
                if (role) {
                    Text(
                        text = stringResource(R.string.hubungi_admin_untuk_mengaktifkan_akun_anda),
                        textAlign = TextAlign.Center,
                        color = Color.Black,
                        fontSize = 16.sp
                    )
                } else {
                    Text(
                        text = stringResource(R.string.akun_berhasil_dibuat_silahkan_login_kembali),
                        textAlign = TextAlign.Center,
                        color = Color.Black,
                        fontSize = 16.sp
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = close,
                        colors = ButtonDefaults.buttonColors(containerColor = Blue),
                        shape = RoundedCornerShape(25),
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                    ) {
                        Text(
                            text = if (role) stringResource(id = R.string.tutup) else stringResource(R.string.masuk),
                            textAlign = TextAlign.Center,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )

                    }
                }
            }
        },
        containerColor = Color.White,
        modifier = Modifier
    )
}