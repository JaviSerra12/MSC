package com.example.msc.util

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File
import java.util.Objects

object FileUtils {

     // Crea un URI temporal para guardar la foto capturada por la cámara.
    fun createTempPictureUri(context: Context): Uri {
        val tempFile = File.createTempFile("temp_image", ".jpg", context.cacheDir).apply {
            createNewFile()
            deleteOnExit()
        }

        return FileProvider.getUriForFile(
            Objects.requireNonNull(context),
            "${context.packageName}.provider",
            tempFile
        )
    }
}
