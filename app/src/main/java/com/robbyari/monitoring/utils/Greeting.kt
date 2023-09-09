package com.robbyari.monitoring.utils

import java.util.Calendar

fun getGreeting(): String {
    val calendar = Calendar.getInstance()
    val hour = calendar.get(Calendar.HOUR_OF_DAY)

    return when {
        hour < 12 -> "Selamat Pagi"
        hour < 15 -> "Selamat Siang"
        hour < 18 -> "Selamat Sore"
        else -> "Selamat Malam"
    }
}