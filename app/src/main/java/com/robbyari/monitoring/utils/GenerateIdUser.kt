package com.robbyari.monitoring.utils

import kotlin.random.Random

fun generateRandomString(length: Int): String {
    val charset = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
    return (1..length)
        .map { charset[Random.nextInt(0, charset.length)] }
        .joinToString("")
}