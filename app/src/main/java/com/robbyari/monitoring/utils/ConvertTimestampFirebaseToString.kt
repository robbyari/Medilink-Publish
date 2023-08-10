package com.robbyari.monitoring.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

fun convertFirebaseTimestampToString(timestamp: com.google.firebase.Timestamp): String {
    val calendar = Calendar.getInstance()
    calendar.timeZone = TimeZone.getDefault() // Set zona waktu sesuai perangkat
    calendar.timeInMillis = timestamp.seconds * 1000 + timestamp.nanoseconds / 1000000

    val dateFormatter = SimpleDateFormat("dd MMMM yyyy - HH:mm", Locale("id", "ID"))

    return dateFormatter.format(calendar.time)
}