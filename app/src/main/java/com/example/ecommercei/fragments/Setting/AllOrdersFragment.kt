package com.example.ecommercei.fragments.Setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.ecommercei.adapters.AllOrdersAdapter
import com.example.ecommercei.databinding.FragmentOrdersBinding
import com.example.ecommercei.utils.Resource
import com.example.ecommercei.viewModel.AllordersViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
@AndroidEntryPoint
class AllOrdersFragment:Fragment() {
    private lateinit var binding : FragmentOrdersBinding
    val orderViewModel by viewModels<AllordersViewModel>()
    val ordersAdapter by lazy { AllOrdersAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOrdersBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupOrderRecycleView()

        lifecycleScope.launch {
            orderViewModel.allOrders.collectLatest { resOrder->
                when(resOrder){
                    is Resource.Loading -> {
                        binding.progressbarAllOrders.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        binding.progressbarAllOrders.visibility = View.GONE
                        ordersAdapter.differ.submitList(resOrder.data)
                        if (resOrder.data.isNullOrEmpty()){
                            binding.tvEmptyOrders.visibility = View.VISIBLE
                        }else {
                            //binding.tvEmptyOrders.visibility = View.GONE
                        }

                    }
                    is Resource.Error -> {
                        binding.progressbarAllOrders.visibility = View.GONE
                    }
                    is Resource.Unspecified -> Unit
                }
            }
        }

        ordersAdapter.onClick = { order ->
            val action = AllOrdersFragmentDirections.actionOrdersFragmentToOrderDetailFragment(order)
            findNavController().navigate(action)
        }
    }

    private fun setupOrderRecycleView() {
        binding.rvAllOrders.adapter = ordersAdapter
    }
}