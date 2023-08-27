package com.robbyari.monitoring.utils

import com.google.firebase.Timestamp
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

fun convertStringToFirebaseTimestamp(dateTimeString: String): Timestamp {
    val pattern = "dd MMM yyyy - HH:mm"
    val dateFormat = SimpleDateFormat(pattern, Locale.getDefault())
    val date = dateFormat.parse(dateTimeString)
    val timestamp = date?.time ?: 0
    return Timestamp(timestamp / 1000, ((timestamp % 1000) * 1000).toInt())
}

fun add30DaysToTimestamp(timestamp: Timestamp): Timestamp {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = timestamp.seconds * 1000L

    calendar.add(Calendar.DAY_OF_MONTH, 30)

    val newTimestamp = Timestamp(calendar.time)
    return newTimestamp
}

fun addOneYearToTimestamp(timestamp: Timestamp): Timestamp {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = timestamp.seconds * 1000L

    calendar.add(Calendar.YEAR, 1)

    val newTimestamp = Timestamp(calendar.time)
    return newTimestamp
}