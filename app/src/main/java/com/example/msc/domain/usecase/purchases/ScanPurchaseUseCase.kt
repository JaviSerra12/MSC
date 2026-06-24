package com.example.msc.domain.usecase.purchases

import android.net.Uri
import com.example.msc.domain.repository.ScanRepository

class ScanPurchaseUseCase(
    private val scanRepository: ScanRepository
) {
    suspend operator fun invoke(uri: Uri, useAi: Boolean = false): Result<String> {
        return if (useAi) {
            scanRepository.processImageWithAi(uri)
        } else {
            scanRepository.processImage(uri)
        }
    }
}
