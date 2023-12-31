package com.robbyari.monitoring

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.lifecycleScope
import com.robbyari.monitoring.data.MonitoringRepositoryImpl.Companion.KEY_EMAIL
import com.robbyari.monitoring.data.MonitoringRepositoryImpl.Companion.KEY_ROLE
import com.robbyari.monitoring.di.userDataStore
import com.robbyari.monitoring.presentation.activity.loginactivity.LoginActivity
import com.robbyari.monitoring.presentation.activity.mainactivity.MainActivity
import com.robbyari.monitoring.presentation.activity.useractivity.UserActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@AndroidEntryPoint
@SuppressLint("CustomSplashScreen")
class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val splashScreen = installSplashScreen()
            splashScreen.setKeepOnScreenCondition { true }
        }
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            delay(2000)

            val dataStore: DataStore<Preferences> = applicationContext.userDataStore
            val dataStoreValue = dataStore.data.first()

            val intent = when {
                dataStoreValue[KEY_EMAIL] != null && dataStoreValue[KEY_ROLE] == "Teknisi" -> Intent(this@SplashActivity, MainActivity::class.java)
                dataStoreValue[KEY_EMAIL] != null && dataStoreValue[KEY_ROLE] == "User" -> Intent(this@SplashActivity, UserActivity::class.java)
                else -> Intent(this@SplashActivity, LoginActivity::class.java)
            }

            startActivity(intent)
            finish()
        }
    }
}