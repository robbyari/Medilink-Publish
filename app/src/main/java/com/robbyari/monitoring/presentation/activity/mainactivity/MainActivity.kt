package com.robbyari.monitoring.presentation.activity.mainactivity

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.robbyari.monitoring.presentation.theme.MonitoringTheme
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private var locationCallback: LocationCallback? = null
    private var fusedLocationClient: FusedLocationProviderClient? = null
    private var locationRequired = false

    private val launcherMultiplePermissions = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissionsMap ->
        val areGranted = permissionsMap.values.reduce { acc, next -> acc && next }
        if (areGranted) {
            locationRequired = true
            startLocationUpdates(locationCallback, fusedLocationClient)
            Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }

    private val permissions = arrayOf(
        android.Manifest.permission.ACCESS_COARSE_LOCATION,
        android.Manifest.permission.ACCESS_FINE_LOCATION,
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            MonitoringTheme {
                val isDistanceGreaterThan100Meters = remember {
                    mutableStateOf(true)
                }

                val cityNameState = remember { mutableStateOf("Unknown") }
                val context = LocalContext.current
                fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
                locationCallback = object : LocationCallback() {
                    override fun onLocationResult(p0: LocationResult) {
                        try {
                            for (lo in p0.locations) {
                                val geocoder = Geocoder(context, Locale.getDefault())
                                val addresses = geocoder.getFromLocation(lo.latitude, lo.longitude, 1)
                                val address = addresses?.get(0)

                                val fullAddress = buildString {
                                    append(address?.thoroughfare)
                                    append(", ")
                                    append(address?.subLocality)
                                    append(", ")
                                    append(address?.subAdminArea)
                                    append(", ")
                                    append(address?.postalCode)
                                    append("") }

                                cityNameState.value = fullAddress

                                // Koordinat target
                                val targetLatitude = -6.3147011
                                val targetLongitude = 106.7912588

                                val targetLocation = Location("Target")
                                targetLocation.latitude = targetLatitude
                                targetLocation.longitude = targetLongitude

                                // Mengukur jarak dalam meter
                                val distance = lo.distanceTo(targetLocation)

                                // Memeriksa apakah jarak lebih dari 100 meter
                                isDistanceGreaterThan100Meters.value = distance > 300
                            }
                        } catch (e: Exception) {
                            Toast.makeText(this@MainActivity, "No internet available", Toast.LENGTH_LONG).show()
                        }
                    }
                }
                if (permissions.all {
                        ContextCompat.checkSelfPermission(
                            context,
                            it
                        ) == PackageManager.PERMISSION_GRANTED
                    }) {
                    startLocationUpdates(locationCallback, fusedLocationClient)
                } else {
                    launcherMultiplePermissions.launch(permissions)
                }

                MainApp(location = cityNameState.value, isDistanceGreaterThan100Meters = isDistanceGreaterThan100Meters.value)
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates(
        locationCallback: LocationCallback?,
        fusedLocationClient: FusedLocationProviderClient?
    ) {
        locationCallback?.let {
            val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000)
                .build()
            fusedLocationClient?.requestLocationUpdates(
                locationRequest,
                it,
                Looper.getMainLooper()
            )
        }
    }


    override fun onResume() {
        super.onResume()
        if (locationRequired) {
            startLocationUpdates(locationCallback, fusedLocationClient)
        }
    }

    override fun onPause() {
        super.onPause()
        locationCallback?.let { fusedLocationClient?.removeLocationUpdates(it) }
    }

    /* override fun onBackPressed() {
         moveTaskToBack(true)
     }*/
}

