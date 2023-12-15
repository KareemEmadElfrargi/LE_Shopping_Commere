package com.example.ecommercei.viewModel

import android.content.res.Resources
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommercei.data.Product
import com.example.ecommercei.utils.Resource
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainCategoryViewModel @Inject constructor(
    val firestore: FirebaseFirestore
)
    :ViewModel() {

    private val _specialProduct = MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())
    val specialProduct: StateFlow<Resource<List<Product>>> = _specialProduct

    private val _bestDealsProduct = MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())
    val bestDealsProduct: StateFlow<Resource<List<Product>>> = _bestDealsProduct

    private val _bestProduct = MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())
    val bestProduct: StateFlow<Resource<List<Product>>> = _bestProduct

    init {
        fetchSpecialProduct()
        fetchBestDealsProduct()
        fetchBestProduct()
    }

    private fun fetchSpecialProduct(){
            viewModelScope.launch {
                _specialProduct.emit(Resource.Loading())
            }
            firestore.collection("Products").whereEqualTo("category","Special Product").get()
                .addOnSuccessListener { result ->
                    val specialProductList = result.toObjects(Product::class.java)
                    Log.i("find",specialProductList.toString())
                    viewModelScope.launch {
                        _specialProduct.emit(Resource.Success(specialProductList))
                    }
                }.addOnFailureListener {
                    viewModelScope.launch {
                        _specialProduct.emit(Resource.Error(it.message.toString()))
                    }
                }

        }

    private fun fetchBestDealsProduct(){
        viewModelScope.launch {
            _bestDealsProduct.emit(Resource.Loading())
        }
        firestore.collection("Products").whereEqualTo("category","Best Deals").get()
            .addOnSuccessListener { result ->
                val bestDealsProductList = result.toObjects(Product::class.java)
                viewModelScope.launch {
                    _bestDealsProduct.emit(Resource.Success(bestDealsProductList))
                }
            }.addOnFailureListener {
                viewModelScope.launch {
                    _bestDealsProduct.emit(Resource.Error(it.message.toString()))
                }
            }

    }

    private fun fetchBestProduct(){
        viewModelScope.launch {
            _bestProduct.emit(Resource.Loading())
        }
        firestore.collection("Products").get()
            .addOnSuccessListener { result ->
                val bestProductList = result.toObjects(Product::class.java)
                viewModelScope.launch {
                    _bestProduct.emit(Resource.Success(bestProductList))
                }
            }.addOnFailureListener {
                viewModelScope.launch {
                    _bestProduct.emit(Resource.Error(it.message.toString()))
                }
            }

    }
}