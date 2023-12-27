package com.example.ecommercei.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommercei.data.CartProduct
import com.example.ecommercei.firebase.FirebaseCommon
import com.example.ecommercei.helper.getProductPrice
import com.example.ecommercei.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class CartViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth,
    private val firebaseCommon: FirebaseCommon
): ViewModel() {
    private val _cartProducts =
        MutableStateFlow<Resource<List<CartProduct>>>(Resource.Unspecified())
    val cartProducts = _cartProducts.asStateFlow()

    private val _deleteDialog = MutableSharedFlow<CartProduct>()
     val deleteDialog = _deleteDialog.asSharedFlow()

    private var cartProductDocument = emptyList<DocumentSnapshot>()

    fun deleteCartProduct(cartProduct:CartProduct){
        val index  = cartProducts.value.data?.indexOf(cartProduct)
        if (index!=null && index!=-1){
            val documentId = cartProductDocument[index].id
            firestore.collection("user").document(firebaseAuth.uid!!).collection("cart")
                .document(documentId).delete()

        }


    }
     val productsPrice = cartProducts.map {
        when(it){
            is Resource.Success ->{
                calculatePrice(it.data!!)
            }

            else -> null
        }
    }


    private fun calculatePrice(data: List<CartProduct>): Float {
        return data.sumByDouble { cartProduct ->
            (cartProduct.product.offerPercentage.getProductPrice(cartProduct.product.price) * cartProduct.quantity).toDouble()
        }.toFloat()
    }

    init {
        getCartProduct()
    }


    private fun getCartProduct(){
        viewModelScope.launch {
            _cartProducts.emit(Resource.Loading())
        }

        firestore.collection("user")
            .document(firebaseAuth.uid!!)
            .collection("cart")
            .addSnapshotListener{ value,error ->
                if ( error!=null || value == null){
                    viewModelScope.launch {
                        _cartProducts.emit(Resource.Error(error?.message.toString()))
                    }
                } else {
                    cartProductDocument = value.documents

                    val cartProducts = value.toObjects(CartProduct::class.java)
                    viewModelScope.launch {
                       _cartProducts.emit(Resource.Success(cartProducts))
                    }
                }
            }

    }

    fun changeQuantity(cartProduct: CartProduct,quantityChanging: FirebaseCommon.QuantityChanging) {

        val index  = cartProducts.value.data?.indexOf(cartProduct)
        if (index!=null && index!=-1){
            val documentId = cartProductDocument[index].id
            when(quantityChanging){
                FirebaseCommon.QuantityChanging.INCREASE ->{
                    viewModelScope.launch { _cartProducts.emit(Resource.Loading()) }
                    increaseQuantity(documentId)
                }

                FirebaseCommon.QuantityChanging.DECREASE -> {
                    if (cartProduct.quantity == 1){
                       viewModelScope.launch{ _deleteDialog.emit(cartProduct)}
                        return
                    }
                    viewModelScope.launch { _cartProducts.emit(Resource.Loading()) }
                    drecreaseQuantity(documentId)
                }
            }
        }

    }
    private fun increaseQuantity(documentId: String) {
        firebaseCommon.increaseQuantity(documentId){ result , exception ->
            if (exception!=null){
                viewModelScope.launch { _cartProducts.emit(Resource.Error(exception.message.toString())) }
            }
        }
    }
    private fun drecreaseQuantity(documentId: String) {
        firebaseCommon.decreaseQuantity(documentId){ _ , exception ->
            if (exception!=null){
                viewModelScope.launch { _cartProducts.emit(Resource.Error(exception.message.toString())) }
            }
        }
    }

}