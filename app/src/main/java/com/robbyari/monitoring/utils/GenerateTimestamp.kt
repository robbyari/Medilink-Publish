package com.robbyari.monitoring.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun generateTimestamp(): String {
    val timestamp = System.currentTimeMillis()
    val dateFormat = SimpleDateFormat("dd MMM yyyy - HH:mm", Locale.getDefault())
    return dateFormat.format(Date(timestamp))
}