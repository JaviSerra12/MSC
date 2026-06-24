package com.example.msc.data.repository

import android.content.Context
import android.net.Uri
import com.example.msc.domain.repository.ScanRepository
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.tasks.await
import kotlin.math.abs

class MLKitScanRepository(
    private val context: Context
) : ScanRepository {

    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    override suspend fun processImage(uri: Uri): Result<String> {
        return try {
            val image = InputImage.fromFilePath(context, uri)
            val visionText = recognizer.process(image).await()
            
            if (visionText.text.isBlank()) {
                Result.failure(Exception("No se encontró texto en la imagen"))
            } else {
                val allLines = visionText.textBlocks.flatMap { it.lines }
                val thresholdY = 15
                val rows = mutableListOf<MutableList<com.google.mlkit.vision.text.Text.Line>>()
                val sortedByTop = allLines.sortedBy { it.boundingBox?.top ?: 0 }
                
                for (line in sortedByTop) {
                    val lineTop = line.boundingBox?.top ?: 0
                    val existingRow = rows.find { row ->
                        val rowTop = row.first().boundingBox?.top ?: 0
                        abs(rowTop - lineTop) < thresholdY
                    }
                    
                    if (existingRow != null) {
                        existingRow.add(line)
                    } else {
                        rows.add(mutableListOf(line))
                    }
                }
                
                val resultText = rows.joinToString("\n") { row ->
                    row.sortedBy { it.boundingBox?.left ?: 0 }
                        .joinToString(" ") { it.text }
                }

                Result.success(resultText)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun processImageWithAi(uri: Uri): Result<String> {
        return Result.failure(Exception("MLKit no soporta procesamiento con IA generativa"))
    }
}
