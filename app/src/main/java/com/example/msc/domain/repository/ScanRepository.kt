package com.example.msc.domain.repository

import android.net.Uri

interface ScanRepository {
    // Uri es la ruta de la imagen que se va a procesar
    suspend fun processImage(uri: Uri): Result<String>
}
