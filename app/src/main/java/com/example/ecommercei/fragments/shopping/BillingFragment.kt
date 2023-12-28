package com.example.ecommercei.fragments.shopping

import android.location.Address
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavArgs
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.ecommercei.R
import com.example.ecommercei.adapters.AddressAdapter
import com.example.ecommercei.adapters.BillingProductAdapter
import com.example.ecommercei.data.CartProduct
import com.example.ecommercei.databinding.FragmentBillingBinding
import com.example.ecommercei.utils.HorizentailItemDecoration
import com.example.ecommercei.utils.Resource
import com.example.ecommercei.viewModel.BillingViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BillingFragment : Fragment() {
    private lateinit var binding : FragmentBillingBinding
    private val addressAdapter by lazy { AddressAdapter() }
    private val billingProductAdapter by lazy { BillingProductAdapter() }
    private val viewModel by viewModels<BillingViewModel>()
    private val args by navArgs<BillingFragmentArgs>()
    private var product = emptyList<CartProduct>()
    private var totalPrice = 0f


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        product = args.products.toList()
        totalPrice = args.totalPrice
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBillingBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupBillingProductRv()
        setupAddressRv()


        binding.imageAddAddress.setOnClickListener {
            findNavController().navigate(R.id.action_billingFragment_to_addressFragment)
        }
        lifecycleScope.launch{
            viewModel.address.collectLatest {
                when (it){
                    is Resource.Loading -> {
                        binding.progressbarAddress.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        binding.progressbarAddress.visibility = View.GONE
                        addressAdapter.differ.submitList(it.data)
                    }
                    is Resource.Error -> {
                        binding.progressbarAddress.visibility = View.GONE
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                    is Resource.Unspecified -> Unit
                }
            }
        }

        billingProductAdapter.differ.submitList(product)
        binding.tvTotalPrice.text = "$ $totalPrice"
    }
    private fun setupAddressRv() {
        binding.rvAddress.apply {
            adapter = addressAdapter
            addItemDecoration(HorizentailItemDecoration())
        }
    }

    private fun setupBillingProductRv() {
        binding.rvProducts.apply {
            adapter = billingProductAdapter
            addItemDecoration(HorizentailItemDecoration())
        }
    }
}