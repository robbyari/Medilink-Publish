package com.robbyari.monitoring.presentation.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Task
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.robbyari.monitoring.domain.model.Response
import com.robbyari.monitoring.domain.model.User
import com.robbyari.monitoring.presentation.components.CardContent
import com.robbyari.monitoring.presentation.components.HomeActionBar
import com.robbyari.monitoring.presentation.theme.LightBlue

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val emailState by viewModel.email.collectAsState()
    val user by viewModel.user.collectAsState()

    LaunchedEffect(emailState) {
        viewModel.getUser(emailState)
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (user) {
            is Response.Loading -> {}
            is Response.Success -> {
                val data = (user as Response.Success<User>).data
                if (data != null) {
                    HomeContent(user = data)
                }

            }
            is Response.Failure -> {}
        }
    }
}

@Composable
fun HomeContent(user: User) {
    Column {
        Spacer(modifier = Modifier.height(38.dp))
        HomeActionBar(user = user)
        Spacer(modifier = Modifier.height(16.dp))
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(LightBlue)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            LazyRow (
                modifier = Modifier.fillMaxWidth()
            ) {
                item {
                    Spacer(modifier = Modifier.width(16.dp))
                }
                item {
                    CardContent(icon = Icons.Default.Task, title = "Total Alat", total = 125)
                    Spacer(modifier = Modifier.width(16.dp))
                }
                item {
                    CardContent(icon = Icons.Default.Task, title = "Total Alat", total = 125)
                    Spacer(modifier = Modifier.width(16.dp))
                }
            }
        }
    }
}