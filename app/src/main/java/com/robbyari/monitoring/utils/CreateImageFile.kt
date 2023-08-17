package com.robbyari.monitoring.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date

fun Context.createImageFile(): File {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"
    val image = File.createTempFile(
        imageFileName,
        ".jpg",
        externalCacheDir
    )

    return image
}

fun addTextWatermarkToImage(imageFile: File, watermarkText: String) {
    val originalBitmap = BitmapFactory.decodeFile(imageFile.absolutePath)

    val combinedBitmap = Bitmap.createBitmap(
        originalBitmap.width , originalBitmap.height, originalBitmap.config
    )

    val canvas = Canvas(combinedBitmap)
    canvas.drawBitmap(originalBitmap, 0f, 0f, null)

    val paint = Paint().apply {
        color = Color.BLACK
        textSize = 16f // Ukuran teks
        isAntiAlias = true
    }

    val textX = 50f // Koordinat X untuk teks
    val textY = combinedBitmap.height - 50f // Koordinat Y untuk teks

    canvas.drawText(watermarkText, textX, textY, paint)

    val outputStream = FileOutputStream(imageFile)
    combinedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
    outputStream.close()
}