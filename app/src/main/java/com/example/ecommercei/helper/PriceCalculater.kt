package com.example.ecommercei.helper

fun Float?.getProductPrice(price:Float):Float{
   // this -> offerPercentage

    if (this == null)
        return price
    val remainingPricePercentage = 100f - this
    val priceAfterOffer = (remainingPricePercentage/100) * price
    return priceAfterOffer
}