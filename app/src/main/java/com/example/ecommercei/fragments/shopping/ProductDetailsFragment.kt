package com.example.ecommercei.fragments.shopping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.ecommercei.R
import com.example.ecommercei.activitys.ShoppingActivity
import com.example.ecommercei.adapters.ColorsAdapter
import com.example.ecommercei.adapters.SizesAdapter
import com.example.ecommercei.adapters.ViewPager2ImagesAdapter
import com.example.ecommercei.data.CartProduct
import com.example.ecommercei.databinding.FragmentDetailsProductsBinding
import com.example.ecommercei.utils.Resource
import com.example.ecommercei.utils.hideButtonNavigationView
import com.example.ecommercei.viewModel.DetailsViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductDetailsFragment:Fragment() {
    private val args by navArgs<ProductDetailsFragmentArgs>()
    private lateinit var binding : FragmentDetailsProductsBinding
    private val viewPagerAdapter by lazy { ViewPager2ImagesAdapter() }
    private val sizesAdapter by lazy { SizesAdapter() }
    private val colorsAdapter by lazy { ColorsAdapter() }
    private var selectedColor:Int?=null
    private var selectedSize:String?=null
    private val viewModel by viewModels<DetailsViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        hideButtonNavigationView()
        binding = FragmentDetailsProductsBinding.inflate(inflater)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val product = args.product

        setupSizesRecycleView()
        setupColorsRecycleView()
        setupViewPager()


        binding.closeImg.setOnClickListener {
            findNavController().navigateUp()
        }

        sizesAdapter.onItemClick = {
            selectedSize = it
        }

        colorsAdapter.onItemClick = {
            selectedColor = it
        }

        binding.buttonAddToCard.setOnClickListener {
            viewModel.addUpdateProductInCart(CartProduct(product,
                1,selectedColor,selectedSize))
        }

        lifecycleScope.launch {
            viewModel.addToCard.collectLatest {
                when(it){
                    is Resource.Loading ->{
                        binding.buttonAddToCard.startAnimation()
                    }
                    is Resource.Success -> {
                        binding.buttonAddToCard.revertAnimation()
                        binding.buttonAddToCard.setBackgroundColor(resources.getColor(R.color.black))
                    }
                    is Resource.Error -> {
                        binding.buttonAddToCard.revertAnimation()
                        Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT).show()
                    }
                    is Resource.Unspecified -> Unit
                }
            }
        }

        binding.apply {
            tvProductName.text = product.name
            tvProductPrice.text = "$ ${product.price}"
            tvProductDescription.text = product.description


            if (product.colors.isNullOrEmpty()){
                tvProductColors.visibility = View.GONE
            }
            if (product.sizes.isNullOrEmpty()){
                tvProductSizes.visibility = View.GONE
            }
        }
        viewPagerAdapter.differ.submitList(product.images)

        product.sizes?.let {
            sizesAdapter.differ.submitList(it)
        }
        product.colors?.let {
            colorsAdapter.differ.submitList(it)
        }
    }

    private fun setupViewPager() {

        binding.apply {
             viewpagerProductImages.adapter = viewPagerAdapter
        }
    }

    private fun setupColorsRecycleView() {
        binding.recycleViewColors.apply {
            adapter = colorsAdapter
        }
    }
    private fun setupSizesRecycleView() {
        binding.recycleViewSize.apply {
            adapter = sizesAdapter
        }
    }
}