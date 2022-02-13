package com.annevonwolffen.gallery_impl.presentation.utils

import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date

fun createImageFile(context: Context): File {
    val timeStamp: String = SimpleDateFormat.getDateTimeInstance().format(Date())
    val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File.createTempFile(
        "DailyPhotosDiary_${timeStamp}_",
        ".jpg",
        storageDir
    )
}

fun createFileFromUri(uri: Uri, context: Context): File {
    val outputFile = createImageFile(context)
    val inputStream = context.contentResolver.openInputStream(uri)
    inputStream?.use { input ->
        val outputStream = FileOutputStream(outputFile)
        outputStream.use { output ->
            val buffer = ByteArray(4 * 1024) // buffer size
            while (true) {
                val byteCount = input.read(buffer)
                if (byteCount < 0) break
                output.write(buffer, 0, byteCount)
            }
            output.flush()
        }
    }
    return outputFile
}

fun File.getUriForFile(context: Context): Uri = FileProvider.getUriForFile(
    context,
    "com.annevonwolffen.fileprovider",
    this
)