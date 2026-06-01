package com.example.msc.domain.model

//Datos que contienen los productos.
data class Products(
    val name : String = "",
    val price : Double = 0.0,
    val quantity : Double = 0.0
){
    fun total() : Double {
        return price * quantity
    }
}
