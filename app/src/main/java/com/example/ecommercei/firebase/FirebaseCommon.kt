package com.example.ecommercei.firebase

import android.util.Log
import com.example.ecommercei.data.CartProduct
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject

class FirebaseCommon(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
) {

    private val cartCollection = firestore
        .collection("user")
        .document(firebaseAuth.uid!!)
        .collection("cart")


    fun addProductToOurCart(cartProduct: CartProduct, onResult:(CartProduct?,Exception?) -> Unit){
        cartCollection.document().set(cartProduct).addOnSuccessListener {
            onResult(cartProduct,null)
        }.addOnFailureListener {
            onResult(null,it)
        }
    }

    fun increaseQuantity(documentId:String , onResult: (String?, Exception?) -> Unit){
        firestore.runTransaction { transition ->
            val documentReference = cartCollection.document(documentId)
            val document = transition.get(documentReference)
            val productObject = document.toObject(CartProduct::class.java)
            productObject?.let { cartProduct ->
                val newQuantity = (cartProduct.quantity) + 1
                Log.i("FirebaseCommon",newQuantity.toString())
                val newProductObject = cartProduct.copy(quantity = newQuantity)
                transition.set(documentReference,newProductObject)
            }
        }.addOnSuccessListener {
            onResult(documentId,null)
            Log.i("FirebaseCommon",documentId)
        }.addOnFailureListener {
            onResult(null,it)
        }
    }
}