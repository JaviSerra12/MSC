package com.example.msc.domain.repository

import android.net.Uri

interface ScanRepository {
    suspend fun processImage(uri: Uri): Result<String>
    suspend fun processImageWithAi(uri: Uri): Result<String>
}
