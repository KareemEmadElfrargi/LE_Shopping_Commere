package com.example.ecommercei.fragments.Setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.ecommercei.adapters.BillingProductAdapter
import com.example.ecommercei.databinding.FragmentOrderDetailBinding
import com.example.ecommercei.utils.VerticalItemDecoration

class OrderDetailFragment:Fragment() {
    private lateinit var binding : FragmentOrderDetailBinding
    private  val billingProductAdapter by lazy { BillingProductAdapter() }
    private val args by navArgs<>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOrderDetailBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecycleView()



    }

    private fun setUpRecycleView() {
        binding.rvProducts.apply {
            adapter = billingProductAdapter
            addItemDecoration(VerticalItemDecoration())
        }
    }

}