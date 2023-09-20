package com.robbyari.monitoring.presentation.activity.useractivity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.google.android.gms.common.moduleinstall.ModuleInstall
import com.google.android.gms.common.moduleinstall.ModuleInstallRequest
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import com.robbyari.monitoring.R
import com.robbyari.monitoring.presentation.theme.MonitoringTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val moduleInstall = ModuleInstall.getClient(this)
        val moduleInstallRequest = ModuleInstallRequest.newBuilder()
            .addApi(GmsBarcodeScanning.getClient(this))
            .build()

        moduleInstall.installModules(moduleInstallRequest)
            .addOnFailureListener {
                throw RuntimeException(getString(R.string.failed_to_install_module_scan))
            }

        setContent {
            MonitoringTheme {
                UserApp()
            }
        }
    }
}

