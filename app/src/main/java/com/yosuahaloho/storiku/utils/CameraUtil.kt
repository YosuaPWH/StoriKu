package com.yosuahaloho.storiku.utils

import android.app.Application
import android.content.Context
import com.yosuahaloho.storiku.R
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * Created by Yosua on 27/04/2023
 */
private const val FILENAME_FORMAT = "dd-MMM-yyyy"

val timeStamp: String = SimpleDateFormat(
    FILENAME_FORMAT,
    Locale.US
).format(System.currentTimeMillis())

fun createFile(context: Context): File {
    val mediaDir = context.externalMediaDirs.firstOrNull()?.let {
        File(it, context.resources.getString(R.string.app_name)).apply { mkdirs() }
    }

    val outputDirectory = if (
        mediaDir != null && mediaDir.exists()
    ) mediaDir else context.filesDir

    return File(outputDirectory, "Photo-$timeStamp.jpg")
}