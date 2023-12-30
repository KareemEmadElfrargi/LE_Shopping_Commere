package com.example.ecommercei.data.order

import android.os.Parcelable
import com.example.ecommercei.data.CartProduct
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random.Default.nextLong
@Parcelize
data class Order(
    val orderStatus :String="",
    val totalPrice :Float=0f,
    val listOfProduct :List<CartProduct> = emptyList(),
    val address:String = "",
    val date:String = SimpleDateFormat("yyy-MM-dd", Locale.ENGLISH).format(Date()),
    val orderId :Long = nextLong(0,100_000_0000_000) +totalPrice.toLong()
):Parcelable
