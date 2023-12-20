package com.example.ecommercei.fragments.categories

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.ecommercei.data.Category
import com.example.ecommercei.utils.Resource
import com.example.ecommercei.viewModel.CategoryViewModel
import com.example.ecommercei.viewModel.factory.BaseCategoryModelFactory
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
@AndroidEntryPoint
class CupboardFragment:BaseCategoryFragment() {
    @Inject
    lateinit var firestore: FirebaseFirestore
    val viewModel by viewModels<CategoryViewModel> {
        BaseCategoryModelFactory(firestore, Category.Cupboard)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            viewModel.offerProducts.collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        showOfferLoading()
                    }

                    is Resource.Error -> {
                        Snackbar.make(requireView(), it.message.toString(), Snackbar.LENGTH_SHORT)
                            .show()
                        hideOfferLoading()
                    }

                    is Resource.Success -> {
                        hideOfferLoading()
                        offerAdapter.differ.submitList(it.data)
                    }

                    is Resource.Unspecified -> {
                        Unit
                    }
                }
            }

        }

        lifecycleScope.launch {
            viewModel.bestProducts.collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        showBestProductLoading()
                    }

                    is Resource.Error -> {
                        Snackbar.make(requireView(), it.message.toString(), Snackbar.LENGTH_SHORT)
                            .show()
                        hideBestProductLoading()
                    }

                    is Resource.Success -> {
                        hideBestProductLoading()
                        bestProductAdapter.differ.submitList(it.data)
                    }

                    is Resource.Unspecified -> {
                        Unit
                    }
                }
            }

        }
    }

    override fun onBestProductPagingRequest() {

    }

    override fun onOfferPagingRequest() {

    }
}