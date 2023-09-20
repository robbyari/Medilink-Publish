package com.robbyari.monitoring.presentation.activity.loginactivity

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import com.robbyari.monitoring.presentation.activity.mainactivity.MainActivity
import com.robbyari.monitoring.presentation.activity.useractivity.UserActivity
import com.robbyari.monitoring.presentation.theme.MonitoringTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            MonitoringTheme {
                LoginApp(
                    navigateMainActivity = {
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    },
                    navigateToUserActivity = {
                        val intent = Intent(this@LoginActivity, UserActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                )
            }
        }
    }
}