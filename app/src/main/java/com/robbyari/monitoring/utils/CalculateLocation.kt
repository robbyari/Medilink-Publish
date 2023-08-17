package com.robbyari.monitoring.utils

import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
    val r = 6371
    val dLat = Math.toRadians(lat2 - lat1)
    val dLon = Math.toRadians(lon2 - lon1)
    val a = sin(dLat / 2) * sin(dLat / 2) +
            cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
            sin(dLon / 2) * sin(dLon / 2)
    val c = 2 * kotlin.math.atan2(sqrt(a), sqrt(1 - a))
    return r * c * 1000
}