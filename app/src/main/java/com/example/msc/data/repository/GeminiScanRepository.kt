package com.example.msc.data.repository

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import com.example.msc.BuildConfig
import com.example.msc.domain.repository.ScanRepository
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GeminiScanRepository(
    private val context: Context
) : ScanRepository {

    private val generativeModel = GenerativeModel(
        modelName = "gemini-2.5-flash",
        apiKey = BuildConfig.GEMINI_API_KEY
    )

    override suspend fun processImage(uri: Uri): Result<String> {
        // En esta implementación, processImage usará el OCR normal si se llama, 
        // pero aquí implementaremos la lógica de Gemini para ambos por simplicidad si se usa este repo.
        return processImageWithAi(uri)
    }

    override suspend fun processImageWithAi(uri: Uri): Result<String> {
        return withContext(Dispatchers.IO) {
            try {
                val inputStream = context.contentResolver.openInputStream(uri)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                inputStream?.close()

                if (bitmap == null) {
                    return@withContext Result.failure(Exception("No se pudo cargar la imagen"))
                }

                val prompt = """
                    Eres un experto en extracción de datos de tickets de compra.
                    Analiza la imagen de este ticket y extrae todos los productos, cantidades y precios.
                    Devuelve el texto de forma que cada línea represente un producto con este formato:
                    CANTIDAD NOMBRE_PRODUCTO PRECIO
                    
                    Ejemplo:
                    1 LECHE 1.20
                    2 PAN DE MOLDE 2.50
                    
                    Solo devuelve las líneas de productos. No incluyas el nombre de la tienda, la fecha, el total, ni ningún texto explicativo.
                    Si no encuentras la cantidad, pon 1.
                """.trimIndent()

                val response = generativeModel.generateContent(
                    content {
                        image(bitmap)
                        text(prompt)
                    }
                )

                val resultText = response.text
                if (resultText.isNullOrBlank()) {
                    Result.failure(Exception("Gemini no pudo extraer texto del ticket"))
                } else {
                    Result.success(resultText)
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}
