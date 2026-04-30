package com.example.msc.domain.model

import com.google.firebase.firestore.DocumentId
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Date

//Datos que contienen las compras.
data class Purchases(
    @DocumentId
    val id: String = "",
    val shop: String = "",
    val products: List<Products> = emptyList(),
    val createdAt: Long = System.currentTimeMillis(),
    val user: String = ""

) {
    //Calcula el precio total de la compra.
    fun totalPrice(): Double {
        return products.sumOf { it.total() }
    }

    //Companion object hace que se pueda acceder a la funcion sin crear un objeto de la clase.
    companion object {
        //Calcula el precio segun el mes
        fun totalPriceByMonth(purchases: List<Purchases>): Map<String, Double> {

            //Da formato a System.currentTimeMillis()
            val formatter = SimpleDateFormat("MMMM yyyy", Locale.getDefault())

            //Devuelve el valor de los productos agrupados por mes.
            return purchases.groupBy { formatter.format(Date(it.createdAt)) }
                .mapValues { (_, purchaseList) ->
                    purchaseList.sumOf { it.totalPrice() }
                }
        }
    }
}
