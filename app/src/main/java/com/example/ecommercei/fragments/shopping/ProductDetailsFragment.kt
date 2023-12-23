package com.example.ecommercei.fragments.shopping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.ecommercei.adapters.ColorsAdapter
import com.example.ecommercei.adapters.SizesAdapter
import com.example.ecommercei.adapters.ViewPager2ImagesAdapter
import com.example.ecommercei.databinding.FragmentDetailsProductsBinding

class ProductDetailsFragment:Fragment() {
    private val args by navArgs<ProductDetailsFragmentArgs>()
    private lateinit var binding : FragmentDetailsProductsBinding
    private val viewPagerAdapter by lazy { ViewPager2ImagesAdapter() }
    private val sizesAdapter by lazy { SizesAdapter() }
    private val colorsAdapter by lazy { ColorsAdapter() }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailsProductsBinding.inflate(inflater)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val product = args.product

        setupSizesRecycleView()
        setupColorsRecycleView()
        setupViewPager()

        binding.apply {
            tvProductName.text = product.name
            tvProductPrice.text = "$ ${product.price}"
            tvProductDescription.text = product.description
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