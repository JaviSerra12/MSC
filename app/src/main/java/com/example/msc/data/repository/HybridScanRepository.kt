package com.example.msc.data.repository

import android.net.Uri
import com.example.msc.domain.repository.ScanRepository

class HybridScanRepository(
    private val mlKitRepo: MLKitScanRepository,
    private val geminiRepo: GeminiScanRepository
) : ScanRepository {

    override suspend fun processImage(uri: Uri): Result<String> {
        // Usa ML Kit para el procesamiento Local
        return mlKitRepo.processImage(uri)
    }

    override suspend fun processImageWithAi(uri: Uri): Result<String> {
        // Usa Gemini para el procesamiento con IA
        return geminiRepo.processImageWithAi(uri)
    }
}
