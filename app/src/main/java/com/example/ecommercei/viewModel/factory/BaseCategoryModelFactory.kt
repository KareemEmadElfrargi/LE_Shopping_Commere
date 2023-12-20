package com.example.ecommercei.viewModel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ecommercei.data.Category
import com.example.ecommercei.viewModel.CategoryViewModel
import com.google.firebase.firestore.FirebaseFirestore

class BaseCategoryModelFactory(
    private val firestore: FirebaseFirestore,
    private val category: Category
)
    : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return  CategoryViewModel(firestore,category) as T
    }
}