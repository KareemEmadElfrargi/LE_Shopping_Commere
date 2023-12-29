package com.example.ecommercei.data.order

import com.example.ecommercei.data.CartProduct

data class Order(
    val orderStatus :String,
    val totalPrice :Float,
    val listOfProduct :List<CartProduct>,
    val address:String
)
