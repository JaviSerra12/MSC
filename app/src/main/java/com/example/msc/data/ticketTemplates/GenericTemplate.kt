package com.example.msc.data.ticketTemplates

import com.example.msc.domain.model.Products
import com.example.msc.ui.screen.scanScreen.ParsingPattern


class GenericTemplate : TicketTemplate {
    // Detecta precios (numero + coma/punto + 2 digitos)
    private val priceRegex = """(\d+[,.]\d{2})""".toRegex()
    
    // Palabras bloqueadas
    private val blackList = listOf(
        "TOTAL", "SUBTOTAL", "EFECTIVO", "TARJETA", "PAGO", "IVA", 
        "CAMBIO", "IMP", "ARTICULOS", "FACTURA", "IMPORTE", "NIF", "CLIENTE"
    )

    // Esta plantilla siempre devuelve true ya que sirve como respaldo para cualquier ticket
    override fun isMatch(text: String): Boolean = true

    // Implementacion por defecto que usa el modo de deteccion automatica
    override fun parse(lines: List<String>): List<Products> {
        return parseWithPattern(lines, ParsingPattern.AUTOMATIC)
    }

    // Procesa las lineas del ticket usando un patron especifico o deteccion automatica
    fun parseWithPattern(lines: List<String>, pattern: ParsingPattern): List<Products> {
        val products = mutableListOf<Products>()

        for (line in lines) {
            val upperLine = line.uppercase()
            
            // Si detecta la palabra TOTAL (y no es SUBTOTAL), dejamos de leer lineas
            if (upperLine.contains("TOTAL") && !upperLine.contains("SUBTOTAL")) {
                break
            }

            // Seleccionael metodo de parseo segun el patron que se elija
            val product = when (pattern) {
                ParsingPattern.AUTOMATIC -> {
                    // En modo automatico prueba todos los patrones hasta que uno sirva
                    parseQtyNamePrice(line, priceRegex)
                        ?: parseNameQtyPrice(line, priceRegex)
                        ?: parseNamePriceQty(line, priceRegex)
                        ?: parseNamePrice(line, priceRegex)
                }
                ParsingPattern.QTY_NAME_PRICE -> parseQtyNamePrice(line, priceRegex)
                ParsingPattern.NAME_QTY_PRICE -> parseNameQtyPrice(line, priceRegex)
                ParsingPattern.NAME_PRICE_QTY -> parseNamePriceQty(line, priceRegex)
                ParsingPattern.NAME_PRICE -> parseNamePrice(line, priceRegex)
            }

            // Si se detecta un producto valida que no este en la lista negra antes de añadirlo
            product?.let { 
                if (isValidProduct(it.name, blackList)) {
                    products.add(it)
                }
            }
        }
        return products
    }
}
