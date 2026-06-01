package com.example.msc.data.ticketTemplates

import com.example.msc.domain.model.Products

interface TicketTemplate {
    // Determina si el texto escaneado pertenece a un establecimiento especifico (ej. Mercadona, Lidl).
    fun isMatch(text: String): Boolean
    
    // Transforma una lista de lineas en una lista de Products
    fun parse(lines: List<String>): List<Products>

    // Patron 1: Intenta extraer datos en el orden [Cantidad] [Nombre] [PrecioTotal]. | "2,00 LECHE 3.20"
    fun parseQtyNamePrice(line: String, priceRegex: Regex): Products? {
        // Busca el precio al final de la linea.
        val priceMatch = priceRegex.findAll(line).lastOrNull() ?: return null
        // El precio es igual al ultimo numero encontrado y cambiamos la coma por un punto.
        val totalPrice = priceMatch.value.replace(",", ".").toDoubleOrNull() ?: 0.0
        // El nombre es lo que queda sin el precio.
        val remaining = line.replace(priceMatch.value, "").trim()
        
        // Busca la cantidad al principio de la linea (puede incluir 'x' o '*').
        val qtyMatch = """^(\d+([,.]\d{1,2})?)\s*[xX*]?""".toRegex().find(remaining)
            ?: return null // Si no empieza por un numero, este patron no aplica.

        // Cambiamos la coma por un punto y obtenemos la cantidad
        val quantity = qtyMatch.groupValues[1].replace(",", ".").toDoubleOrNull() ?: 1.0
        // El nombre es lo que queda después de quitar cantidad y precio.
        val name = remaining.replaceFirst(qtyMatch.value, "").trim()
        
        return if (name.length > 2) Products(name.uppercase(), totalPrice / quantity, quantity) else null
    }

    // Patron 2: Intenta extraer datos en el orden [Nombre] [Cantidad] [PrecioTotal]. | "LECHE 2,00 3.20"
    fun parseNameQtyPrice(line: String, priceRegex: Regex): Products? {
        val matches = priceRegex.findAll(line).toList()
        if (matches.isEmpty()) return null
        
        // El ultimo numero encontrado es el precio total.
        val totalPriceMatch = matches.last()
        val totalPrice = totalPriceMatch.value.replace(",", ".").toDoubleOrNull() ?: 0.0
        val remaining = line.replace(totalPriceMatch.value, "").trim()
        
        // Busca un numero que actue como cantidad justo antes del precio.
        val qtyRegex = """(\d+([,.]\d{1,2})?)\s*[xX*]?$""".toRegex()
        val qtyMatch = qtyRegex.find(remaining) ?: return null
        
        val quantity = qtyMatch.groupValues[1].replace(",", ".").toDoubleOrNull() ?: 1.0
        // El nombre es el texto al inicio de la linea.
        val name = remaining.replace(qtyMatch.value, "").trim()
        
        return if (name.length > 2) Products(name.uppercase(), totalPrice / quantity, quantity) else null
    }

    // Patron 3: Intenta extraer datos en el orden [Nombre] [PrecioTotal] [Cantidad]. | Ejemplo: "LECHE 3.20 2,00"
    fun parseNamePriceQty(line: String, priceRegex: Regex): Products? {
        // Divide por espacios para identificar las ultimas columnas.
        val parts = line.split(Regex("\\s+")).filter { it.isNotBlank() }
        if (parts.size < 3) return null
        
        // Asume que el ultimo elemento es la cantidad y el penultimo el precio.
        val quantity = parts.last().replace(",", ".").toDoubleOrNull() ?: return null
        val pricePart = parts[parts.size - 2]
        val totalPrice = pricePart.replace(",", ".").toDoubleOrNull() ?: return null
        
        val name = parts.dropLast(2).joinToString(" ")
        return if (name.length > 2) Products(name.uppercase(), totalPrice / quantity, quantity) else null
    }

    // Patron Simple: Intenta extraer solo [Nombre] [PrecioTotal], asumiendo cantidad 1.0. | Ejemplo: "LECHE 1.60"
    fun parseNamePrice(line: String, priceRegex: Regex): Products? {
        val priceMatch = priceRegex.findAll(line).lastOrNull() ?: return null
        val totalPrice = priceMatch.value.replace(",", ".").toDoubleOrNull() ?: 0.0
        val name = line.replace(priceMatch.value, "").trim()
        
        // Evitamos procesar lineas que son solo numeros o simbolos.
        if (name.all { it.isDigit() || it == ',' || it == '.' || it == ' ' }) return null
        
        return if (name.length > 2) Products(name.uppercase(), totalPrice, 1.0) else null
    }

    // Valida si el texto extraido como nombre es un producto real y no "basura" del ticket.
    fun isValidProduct(name: String, blackList: List<String>): Boolean {
        // Ignora nombres demasiado cortos.
        if (name.length < 3) return false
        val upperName = name.uppercase()
        
        // Excluye la linea si todavia contiene fragmentos que parecen precios (ej. ",00").
        val priceLikeRegex = """\d+[,.]\d{2}""".toRegex()
        if (priceLikeRegex.containsMatchIn(name)) return false

        // Excluye si contiene palabras prohibidas (TOTAL, IVA, etc.).
        if (blackList.any { upperName.contains(it) }) return false
        // Excluye si no contiene ninguna letra (solo numeros o simbolos).
        if (name.all { !it.isLetter() && !it.isWhitespace() }) return false
        return true
    }
}
