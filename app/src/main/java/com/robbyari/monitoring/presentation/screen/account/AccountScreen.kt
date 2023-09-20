package com.robbyari.monitoring.presentation.screen.account

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.NavigateNext
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.robbyari.monitoring.R
import com.robbyari.monitoring.domain.model.Response
import com.robbyari.monitoring.domain.model.User
import com.robbyari.monitoring.presentation.activity.loginactivity.LoginActivity
import com.robbyari.monitoring.presentation.components.ActionBarDetail
import com.robbyari.monitoring.presentation.components.AlertDeleteAccount
import com.robbyari.monitoring.presentation.components.AlertLogout
import com.robbyari.monitoring.presentation.components.ChangeEmailDialog
import com.robbyari.monitoring.presentation.components.ChangePasswordDialog
import com.robbyari.monitoring.presentation.theme.Blue
import com.robbyari.monitoring.presentation.theme.LightBlue
import com.valentinilk.shimmer.shimmer
import kotlinx.coroutines.launch

@Composable
fun AccountScreen(
    id: String?,
    navigateBack: () -> Unit,
    viewModel: AccountViewModel = hiltViewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    val user by viewModel.user.collectAsState()

    LaunchedEffect(id) {
        coroutineScope.launch {
            viewModel.getUser(id!!)
        }
    }

    when (user) {
        is Response.Loading -> {}
        is Response.Success -> {
            val data = (user as Response.Success).data
            if (data != null) {
                AccountContent(
                    idUser = id,
                    data = data,
                    navigateBack = navigateBack
                )
            }
        }

        is Response.Failure -> {}
    }
}

@Composable
fun AccountContent(
    idUser: String?,
    data: User,
    navigateBack: () -> Unit,
    viewModel: AccountViewModel = hiltViewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val (email, onQueryChangeEmail) = remember { mutableStateOf("") }
    val (password, onQueryChangePassword) = remember { mutableStateOf("") }
    val changeEmailDialog = remember { mutableStateOf(false) }
    val changePasswordDialog = remember { mutableStateOf(false) }
    val alertLogout = remember { mutableStateOf(false) }
    val alertDeleteAccount = remember { mutableStateOf(false) }

    val imageLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { result ->
            if (result != null) {
                coroutineScope.launch {
                    val imageUrl = viewModel.addPhotoProfileToFirebaseStorage(result, idUser!!)
                    viewModel.changePhotoProfile(idUser, imageUrl)
                }
            }
        }

    val painter = rememberAsyncImagePainter(data.photoUrl)
    val state = painter.state
    val transition by animateFloatAsState(
        targetValue = if (state is AsyncImagePainter.State.Success) 1f else 0f, label = ""
    )

    if (changeEmailDialog.value) {
        ChangeEmailDialog(
            notes = email,
            submit = {
                coroutineScope.launch {
                    if (email.isNotEmpty()) {
                        changeEmailDialog.value = !viewModel.changeEmail(idUser!!, email)
                    } else {
                        Toast.makeText(context, context.getString(R.string.email_tidak_boleh_kosong), Toast.LENGTH_SHORT).show()
                    }
                }
            },
            onQueryChange = onQueryChangeEmail,
            close = {
                changeEmailDialog.value = false
            },
            emailNow = data.email!!
        )
    }

    if (changePasswordDialog.value) {
        ChangePasswordDialog(
            notes = password,
            submit = {
                coroutineScope.launch {
                    if (password.isNotEmpty()) {
                        changePasswordDialog.value = !viewModel.changePassword(idUser!!, password)
                    } else {
                        Toast.makeText(context, context.getString(R.string.password_tidak_boleh_kosong), Toast.LENGTH_SHORT).show()
                    }
                }
            },
            onQueryChange = onQueryChangePassword,
            close = {
                changePasswordDialog.value = false
            },
            passwordNow = data.password!!
        )
    }

    if (alertLogout.value) {
        AlertLogout(
            onDismissRequest = { alertLogout.value = false },
            onLogout = {
                coroutineScope.launch {
                    val reset = viewModel.resetDataStore()
                    val intent = Intent(context, LoginActivity::class.java)
                    if (reset) {
                        context.startActivity(intent)
                        (context as Activity).finish()
                    }
                }
            }
        )
    }

    if (alertDeleteAccount.value) {
        AlertDeleteAccount(
            onDismissRequest = { alertDeleteAccount.value = false },
            onDelete = {
                coroutineScope.launch {
                    viewModel.deleteAccount(idUser!!)
                    Toast.makeText(context, context.getText(R.string.akun_anda_berhasil_dihapus), Toast.LENGTH_SHORT).show()
                    val intent = Intent(context, LoginActivity::class.java)
                    context.startActivity(intent)
                    (context as Activity).finish()
                }
            }
        )
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(LightBlue)
    ) {
        ActionBarDetail(
            title = stringResource(R.string.akun_saya),
            navigateBack = { navigateBack() },
            modifier = Modifier
        )
        Spacer(
            modifier = Modifier
                .height(8.dp)
                .fillMaxSize()
                .background(Color.White)
        )
        Box(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth()
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            when (state) {
                is AsyncImagePainter.State.Error -> {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = stringResource(id = R.string.photo_profile),
                        tint = Color.White,
                        modifier = Modifier
                            .border(BorderStroke(2.dp, Color.LightGray), CircleShape)
                            .size(150.dp)
                            .padding(10.dp)
                            .background(color = Color.LightGray, shape = CircleShape)
                            .padding(18.dp)
                    )
                }

                is AsyncImagePainter.State.Loading -> {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = stringResource(id = R.string.photo_profile),
                        tint = Color.White,
                        modifier = Modifier
                            .border(BorderStroke(2.dp, Color.LightGray), CircleShape)
                            .size(150.dp)
                            .padding(10.dp)
                            .shimmer()
                            .background(color = Color.Gray, shape = CircleShape)
                            .padding(18.dp)
                    )
                }

                else -> {}
            }
            Image(
                painter = painter,
                contentDescription = stringResource(id = R.string.photo_profile),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .border(BorderStroke(2.dp, Color.LightGray), CircleShape)
                    .size(150.dp)
                    .padding(10.dp)
                    .clip(CircleShape)
                    .alpha(transition)
                    .clickable { imageLauncher.launch("image/*") }
            )
            Icon(
                Icons.Default.Edit,
                contentDescription = stringResource(id = R.string.photo_profile),
                tint = Color.Black,
                modifier = Modifier
                    .padding(bottom = 15.dp)
                    .size(25.dp)
                    .align(Alignment.BottomCenter)
            )
        }
        Text(
            text = data.email!!,
            color = Color.Black,
            textAlign = TextAlign.Center,
            fontSize = 16.sp,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(top = 8.dp, start = 16.dp, end = 16.dp)
        )
        Text(
            text = "${data.name}",
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            fontSize = 20.sp,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(top = 20.dp, start = 16.dp, end = 16.dp)
        )
        Text(
            text = data.role!!,
            color = Color.Black,
            textAlign = TextAlign.Center,
            fontSize = 16.sp,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(start = 16.dp, end = 16.dp)
        )
        Spacer(
            modifier = Modifier
                .height(20.dp)
                .fillMaxSize()
                .background(Color.White)
        )
        Divider(Modifier, 2.dp, Color.LightGray)
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .clickable {
                    changeEmailDialog.value = true
                }
                .padding(start = 16.dp, end = 16.dp)
        ) {
            Text(
                text = stringResource(id = R.string.ganti_email),
                color = Color.Black,
                fontSize = 16.sp,
                modifier = Modifier
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = Icons.Default.NavigateNext,
                contentDescription = stringResource(R.string.next),
                tint = Color.Black,
                modifier = Modifier.size(24.dp)
            )
        }
        Divider(Modifier, 1.dp, Color.LightGray)
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .clickable { changePasswordDialog.value = true }
                .padding(start = 16.dp, end = 16.dp)
        ) {
            Text(
                text = stringResource(id = R.string.ganti_password),
                color = Color.Black,
                fontSize = 16.sp,
                modifier = Modifier
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = Icons.Default.NavigateNext,
                contentDescription = stringResource(R.string.next),
                tint = Color.Black,
                modifier = Modifier.size(24.dp)
            )
        }
        Divider(Modifier, 1.dp, Color.LightGray)
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .clickable {
                    alertLogout.value = true
                }
                .padding(start = 16.dp, end = 16.dp)
        ) {
            Text(
                text = stringResource(id = R.string.logout),
                color = Color.Black,
                fontSize = 16.sp,
                modifier = Modifier
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = Icons.Default.Logout,
                contentDescription = stringResource(R.string.next),
                tint = Color.Black,
                modifier = Modifier.size(24.dp)
            )
        }
        Divider(Modifier, 1.dp, Color.LightGray)
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .clickable {
                    alertDeleteAccount.value = true
                }
                .padding(start = 16.dp, end = 16.dp)
        ) {
            Text(
                text = stringResource(id = R.string.hapus_akun),
                color = Blue,
                fontSize = 16.sp,
                modifier = Modifier
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = Icons.Outlined.Delete,
                contentDescription = stringResource(R.string.delete_icon),
                tint = Color.Black,
                modifier = Modifier.size(24.dp)
            )
        }
        Divider(Modifier, 1.dp, Color.LightGray)
    }
}