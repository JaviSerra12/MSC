package com.example.msc.domain.model

import com.google.firebase.firestore.DocumentId

//Datos que contienen las compras.
data class Purchases(
    @DocumentId
    val id: String = "",
    val shop: String = "",
    val products: List<Products> = emptyList(),
    val createdAt: Long = System.currentTimeMillis(),
    val user: String = ""
) {
    // Calcula el precio total de la compra
    val totalPrice: Double get() = products.sumOf { it.total() }
}
