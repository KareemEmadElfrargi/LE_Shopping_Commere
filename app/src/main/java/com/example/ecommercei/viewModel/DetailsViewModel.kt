package com.example.ecommercei.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommercei.data.CartProduct
import com.example.ecommercei.firebase.FirebaseCommon
import com.example.ecommercei.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.log

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth:FirebaseAuth,
    private val firebaseCommon: FirebaseCommon
) : ViewModel(){
    private val _addToCard = MutableStateFlow<Resource<CartProduct>>(Resource.Unspecified())
    val addToCard =  _addToCard.asStateFlow()

    fun addUpdateProductInCart(cardProduct: CartProduct){
        viewModelScope.launch { _addToCard.emit(Resource.Loading()) }
        firestore.collection("user")
            .document(auth.uid!!)
            .collection("cart")
            .whereEqualTo("product.id",cardProduct.product.id).get()
            .addOnSuccessListener {
                it.documents.let { documents ->
                    if (documents.isEmpty()){
                        // add new product
                        addNewProduct(cardProduct)

                    }else {
                       val product = documents.first().toObject(CartProduct::class.java)
                        if (product == cardProduct){
                            //increase quantity
                            val documentId = documents.first().id
                            increaseQuantity(documentId,cardProduct)
                        }else {
                            // add new product
                            addNewProduct(cardProduct)
                        }
                    }
                }
            }
            .addOnFailureListener{
                viewModelScope.launch { _addToCard.emit(Resource.Error(it.message.toString())) }
            }
    }

    private fun addNewProduct(cartProduct: CartProduct){
        firebaseCommon.addProductToOurCart(cartProduct){ addedProduct, exception ->
            viewModelScope.launch {
                if (exception == null){
                    _addToCard.emit(Resource.Success(addedProduct!!))
                }else {
                    _addToCard.emit(Resource.Error(exception.message.toString()))
                }
            }

        }
    }

    private fun increaseQuantity(documentId:String,cartProduct: CartProduct){
        firebaseCommon.increaseQuantity(documentId) { _, exception ->
            viewModelScope.launch {
                if (exception == null){
                    _addToCard.emit(Resource.Success(cartProduct))
                }else {
                    _addToCard.emit(Resource.Error(exception.message.toString()))
                }
            }
        }
    }

}