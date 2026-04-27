package com.example.msc.domain.model

data class Products(
    val name : String = "",
    val price : Double = 0.0,
    val quantity : Int = 0
){
    fun total() : Double {
        return price * quantity
    }
}
