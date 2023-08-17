package com.robbyari.monitoring.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

fun convertFirebaseTimestampToString(timestamp: com.google.firebase.Timestamp): String {
    val calendar = Calendar.getInstance()
    calendar.timeZone = TimeZone.getDefault()
    calendar.timeInMillis = timestamp.seconds * 1000 + timestamp.nanoseconds / 1000000

    val dateFormatter = SimpleDateFormat("dd MMM yyyy - HH:mm", Locale("id", "ID"))

    return dateFormatter.format(calendar.time)
}