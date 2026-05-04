package com.example.msc.domain.usecase.purchases

import com.example.msc.domain.model.Purchases
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Obtiene el gasto por mes.
class GetMonthlyExpensesUseCase {

    // Recibe una lista de compras y devuelve un mapa con el gasto por mes.
    operator fun invoke(purchases: List<Purchases>): Map<String, Double> {

        // Da formato a System,currentTimeMillis().
        val formatter = SimpleDateFormat("MMMM yyyy", Locale.getDefault())

        // Devuelve el valor de los productos agrupados por mes.
        return purchases.groupBy { formatter.format(Date(it.createdAt)) }
            .mapValues { (_, purchaseList) ->
                purchaseList.sumOf { it.totalPrice }
            }
    }
}
