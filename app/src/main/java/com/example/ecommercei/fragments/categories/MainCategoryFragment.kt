package com.example.ecommercei.fragments.categories

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.ecommercei.adapters.BestDealsAdapter
import com.example.ecommercei.adapters.BestProductAdapter
import com.example.ecommercei.adapters.HomeViewPagerAdapter
import com.example.ecommercei.adapters.SpecialProductsAdapter
import com.example.ecommercei.databinding.FragmentCategoryMainBinding
import com.example.ecommercei.utils.Resource
import com.example.ecommercei.viewModel.MainCategoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
private val TAG = "MainCategoryFragment"
@AndroidEntryPoint
class MainCategoryFragment: Fragment() {
    private lateinit var binding : FragmentCategoryMainBinding
    private lateinit var specialProductAdapter : SpecialProductsAdapter
    private lateinit var bestDealsAdapter : BestDealsAdapter
    private lateinit var bestProductAdapter : BestProductAdapter
    private  val viewModel : MainCategoryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCategoryMainBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSpecialProductRecycleView()
        setupBestDealsRecycleView()
        setupBestProductsRecycleView()



        lifecycleScope.launch {
            viewModel.specialProduct.collect {
             when(it){
                 is Resource.Loading -> {
                    showLoading()
                 }
                 is Resource.Error -> {
                     hideLoading()
                     Log.e(TAG,it.message.toString())
                 }
                 is Resource.Success -> {
                     Log.i(TAG,it.data.toString())
                     specialProductAdapter.differ.submitList(it.data)
                     hideLoading()
                 }
                 is Resource.Unspecified -> Unit
             }
            }
        }

        lifecycleScope.launch {
            viewModel.bestDealsProduct.collect {
                when(it){
                    is Resource.Loading -> {
                        showLoading()
                    }
                    is Resource.Error -> {
                        hideLoading()
                        Log.e(TAG,it.message.toString())
                    }
                    is Resource.Success -> {
                        Log.i(TAG,it.data.toString())
                        bestDealsAdapter.differ.submitList(it.data)
                        hideLoading()
                    }
                    is Resource.Unspecified -> Unit
                }
            }
        }

        lifecycleScope.launch {
            viewModel.bestProduct.collect {
                when(it){
                    is Resource.Loading -> {
                        showLoading()
                    }
                    is Resource.Error -> {
                        hideLoading()
                        Log.e(TAG,it.message.toString())
                    }
                    is Resource.Success -> {
                        Log.i(TAG,it.data.toString())
                        bestProductAdapter.differ.submitList(it.data)
                        hideLoading()
                    }
                    is Resource.Unspecified -> Unit
                }
            }
        }





    }


    private fun hideLoading() {
        binding.mainCategoryProgressBar.visibility = View.GONE
    }

    private fun showLoading() {
        binding.mainCategoryProgressBar.visibility = View.VISIBLE
    }
    private fun setupSpecialProductRecycleView() {
        specialProductAdapter = SpecialProductsAdapter()
        binding.recycleViewSpecialProducts.adapter = specialProductAdapter
    }
    private fun setupBestProductsRecycleView() {
        bestProductAdapter = BestProductAdapter()
        binding.recycleViewBestProducts.adapter = bestProductAdapter
    }
    private fun setupBestDealsRecycleView() {
        bestDealsAdapter = BestDealsAdapter()
        binding.recycleViewBestDealsProducts.adapter = bestDealsAdapter
    }

}