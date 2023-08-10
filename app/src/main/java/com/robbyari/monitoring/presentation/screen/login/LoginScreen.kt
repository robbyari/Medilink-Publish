package com.robbyari.monitoring.presentation.screen.login

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Key
import androidx.compose.material.icons.outlined.RemoveRedEye
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.robbyari.monitoring.R
import com.robbyari.monitoring.presentation.components.TextFieldWithIcons
import com.robbyari.monitoring.presentation.theme.Blue
import com.robbyari.monitoring.presentation.theme.MonitoringTheme
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    navigateToHome: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        Box(
            modifier = Modifier
                .height(250.dp)
                .fillMaxWidth()
                .background(Blue),
            contentAlignment = Alignment.BottomCenter
        ) {
            Image(
                painter = painterResource(id = R.drawable.loginbackground),
                contentDescription = "Login Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Bottom,
            ) {
                Text(
                    text = "Selamat Datang",
                    fontSize = 30.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp)
                )
                Text(
                    text = "Lakukan pengecekan teliti, laporkan alat yang rusak, dan pastikan kelancaran operasional dengan pengelolaan yang efektif.",
                    fontSize = 16.sp,
                    color = Color.White,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp, bottom = 40.dp, top = 20.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(40.dp))
        TextFieldWithIcons(
            icon = Icons.Outlined.Email,
            label = "Email",
            placeholder = "Masukkan Email",
            value = email,
            onValueChange = { newValue -> email = newValue },
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .fillMaxWidth()
                .padding(20.dp)
        )
        TextFieldWithIcons(
            icon = Icons.Outlined.Key,
            label = "Password",
            placeholder = "Masukkan Password",
            value = password,
            onValueChange = { newValue -> password = newValue },
            isPasswordVisible = true,
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp),
            trailingIcon = Icons.Outlined.RemoveRedEye
        )
        Spacer(modifier = Modifier.height(20.dp))
        TextButton(
            onClick = {
                isLoading = true
                if (email.isBlank() || password.isBlank()) {
                    Toast.makeText(context, "Email dan password tidak boleh kosong", Toast.LENGTH_SHORT).show()
                    isLoading = false
                } else {
                    coroutineScope.launch {
                        val isLoginSuccessful = viewModel.loginUser(email, password)
                        if (isLoginSuccessful) {
                            navigateToHome()
                        } else {
                            Toast.makeText(context, "User tidak ditemukan", Toast.LENGTH_SHORT).show()
                        }
                        isLoading = false
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .height(57.dp)
                .clip(RoundedCornerShape(20))
                .background(Blue),
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = Color.White)
            } else {
                Text(
                    text = "Masuk",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
        }
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Bottom
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 50.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Belum Punya Akun?",
                    modifier = Modifier
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Daftar Sekarang",
                    color = Blue,
                    modifier = Modifier
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginScreenPreview() {
    MonitoringTheme {
       // LoginScreen()
    }
}