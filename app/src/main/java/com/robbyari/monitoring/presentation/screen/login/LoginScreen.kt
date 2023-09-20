package com.robbyari.monitoring.presentation.screen.login

import android.widget.Toast
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
import androidx.compose.foundation.layout.systemBarsPadding
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
import androidx.compose.runtime.SideEffect
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.robbyari.monitoring.R
import com.robbyari.monitoring.data.MonitoringRepositoryImpl.Companion.KEY_ROLE
import com.robbyari.monitoring.di.userDataStore
import com.robbyari.monitoring.presentation.components.TextFieldWithIcons
import com.robbyari.monitoring.presentation.theme.Blue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    navigateToHome: () -> Unit,
    navigateToUser: () -> Unit,
    navigateToRegister: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setSystemBarsColor(Color.Transparent, darkIcons = false)
        systemUiController.setNavigationBarColor(Color.Black)
    }

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .height(270.dp)
                .fillMaxWidth()
                .background(Blue),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.loginbackground),
                contentDescription = stringResource(R.string.login_image_background),
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = stringResource(R.string.selamat_datang),
                    fontSize = 20.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp)
                )
                Text(
                    text = stringResource(R.string.welcome_description),
                    fontSize = 16.sp,
                    color = Color.White,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp, top = 10.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(50.dp))
        TextFieldWithIcons(
            icon = Icons.Outlined.Email,
            label = stringResource(R.string.email_text),
            placeholder = stringResource(R.string.masukkan_email),
            value = email,
            onValueChange = { newValue -> email = newValue },
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .fillMaxWidth()
                .padding(20.dp)
        )
        TextFieldWithIcons(
            icon = Icons.Outlined.Key,
            label = stringResource(R.string.password_text),
            placeholder = stringResource(R.string.masukkan_password),
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
                    Toast.makeText(context, context.getString(R.string.email_dan_password_tidak_boleh_kosong), Toast.LENGTH_SHORT).show()
                    isLoading = false
                } else {
                    coroutineScope.launch {
                        val isLoginSuccessful = viewModel.loginUser(email, password)
                        val dataStore: DataStore<Preferences> = context.userDataStore
                        val dataStoreValue = dataStore.data.first()

                        when {
                            isLoginSuccessful && dataStoreValue[KEY_ROLE] == "Teknisi" -> {
                                navigateToHome()
                            }

                            isLoginSuccessful && dataStoreValue[KEY_ROLE] == "User" -> {
                                navigateToUser()
                            }

                            else -> {
                                Toast.makeText(context, context.getString(R.string.user_tidak_ditemukan), Toast.LENGTH_SHORT).show()
                            }
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
                    text = stringResource(id = R.string.masuk),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
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
                    .systemBarsPadding()
                    .padding(20.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(R.string.belum_punya_akun),
                    fontSize = 16.sp,
                    color = Color.Black,
                    modifier = Modifier
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.daftar),
                    color = Blue,
                    fontSize = 16.sp,
                    modifier = Modifier.clickable { navigateToRegister() }
                )
            }
        }
    }
}
