package com.example.msc.domain.usecase.purchases

import android.net.Uri
import com.example.msc.domain.repository.ScanRepository

class ScanPurchaseUseCase(
    private val scanRepository: ScanRepository
) {
    suspend operator fun invoke(uri: Uri): Result<String> {
        return scanRepository.processImage(uri)
    }
}
