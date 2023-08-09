package com.robbyari.monitoring.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

fun generateTimestamp(): String {
    val currentTime = Calendar.getInstance().time
    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

    val timeString = timeFormat.format(currentTime)
    val dateString = dateFormat.format(currentTime)

    return "$timeString - $dateString"
}