package com.robbyari.monitoring.presentation.screen.register

import android.widget.Toast
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
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBackIos
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Key
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.RemoveRedEye
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.robbyari.monitoring.R
import com.robbyari.monitoring.domain.model.User
import com.robbyari.monitoring.presentation.components.DropdownRoleUser
import com.robbyari.monitoring.presentation.components.ShowAlertRegister
import com.robbyari.monitoring.presentation.components.TextFieldWithIcons
import com.robbyari.monitoring.presentation.theme.Blue
import com.robbyari.monitoring.utils.generateRandomString
import com.robbyari.monitoring.utils.isValidEmail
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(
    navigateBack: () -> Unit,
    viewModel: RegisterViewModel = hiltViewModel()
) {
    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setSystemBarsColor(Color.Transparent, darkIcons = false)
        systemUiController.setNavigationBarColor(Color.Black)
    }

    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var role by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var isExpanded by remember { mutableStateOf(false) }
    val showSuccessDialog = remember { mutableStateOf(false) }

    if (showSuccessDialog.value) {
        ShowAlertRegister(
            role = role == "Teknisi",
            close = {
                showSuccessDialog.value = false
                navigateBack()
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .height(160.dp)
                .fillMaxWidth()
                .background(Blue),
            contentAlignment = Alignment.TopStart
        ) {
            Image(
                painter = painterResource(id = R.drawable.loginbackground),
                contentDescription = stringResource(id = R.string.login_image_background),
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            IconButton(
                onClick = navigateBack,
                modifier = Modifier
                    .padding(start = 16.dp, top = 40.dp)
                    .border(2.dp, color = Color.LightGray, shape = RoundedCornerShape(15))
                    .size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.ArrowBackIos,
                    contentDescription = stringResource(R.string.kembali),
                    tint = Color.Gray,
                    modifier = Modifier
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomStart)
                    .padding(bottom = 20.dp),
                verticalArrangement = Arrangement.Bottom,
            ) {
                Text(
                    text = stringResource(R.string.daftar_sekarang),
                    fontSize = 20.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(40.dp))
        TextFieldWithIcons(
            icon = Icons.Outlined.Person,
            label = stringResource(R.string.nama_lengkap),
            placeholder = stringResource(R.string.masukkan_nama),
            value = name,
            onValueChange = { newValue -> name = newValue },
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .fillMaxWidth()
                .padding(20.dp)
        )
        TextFieldWithIcons(
            icon = Icons.Outlined.Email,
            label = stringResource(id = R.string.email_text),
            placeholder = stringResource(id = R.string.masukkan_email),
            value = email,
            onValueChange = { newValue -> email = newValue },
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp, bottom = 20.dp)
        )
        TextFieldWithIcons(
            icon = Icons.Outlined.Key,
            label = stringResource(id = R.string.password_text),
            placeholder = stringResource(id = R.string.masukkan_password),
            value = password,
            onValueChange = { newValue -> password = newValue },
            isPasswordVisible = true,
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp, bottom = 20.dp),
            trailingIcon = Icons.Outlined.RemoveRedEye
        )
        DropdownRoleUser(
            isExpanded = isExpanded,
            role = role,
            onIsExpandedChange = { newIsExpanded ->
                isExpanded = newIsExpanded
            },
            onRoleChange = { newRole ->
                role = newRole
            }
        )
        Spacer(modifier = Modifier.height(20.dp))
        TextButton(
            onClick = {
                isLoading = true
                val dataRegister = User(
                    uid = generateRandomString(20),
                    name = name,
                    email = email,
                    password = password,
                    photoUrl = "",
                    role = role,
                    status = role != "Teknisi"
                )

                val toastMessage = when {
                    name.isBlank() -> context.getString(R.string.nama_tidak_boleh_kosong)
                    email.isBlank() -> context.getString(R.string.email_tidak_boleh_kosong)
                    password.isBlank() -> context.getString(R.string.password_tidak_boleh_kosong)
                    !isValidEmail(email) -> context.getString(R.string.email_tidak_valid)
                    password.length < 6 -> context.getString(R.string.password_minimal_6_karakter)
                    role.isBlank() -> context.getString(R.string.divisi_tidak_boleh_kosong)
                    else -> {
                        coroutineScope.launch {
                            val isRegisterSuccessful = viewModel.registerUser(dataRegister)
                            if (isRegisterSuccessful) {
                                showSuccessDialog.value = true
                            }
                            isLoading = false
                        }
                        ""
                    }
                }

                if (toastMessage.isNotBlank()) {
                    Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show()
                    isLoading = false
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
                    text = stringResource(id = R.string.daftar),
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
                    .padding(start = 20.dp, end = 20.dp, bottom = 20.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(R.string.sudah_punya_akun),
                    fontSize =16.sp,
                    color = Color.Black,
                    modifier = Modifier
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.login),
                    fontSize =16.sp,
                    color = Blue,
                    modifier = Modifier.clickable { navigateBack() }
                )
            }
        }
    }
}