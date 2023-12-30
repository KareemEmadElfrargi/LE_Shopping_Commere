package com.example.ecommercei.fragments.Setting

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.ecommercei.R
import com.example.ecommercei.adapters.BillingProductAdapter
import com.example.ecommercei.data.order.OrderStatus
import com.example.ecommercei.data.order.getOrderStatus
import com.example.ecommercei.databinding.FragmentOrderDetailBinding
import com.example.ecommercei.utils.VerticalItemDecoration

class OrderDetailFragment:Fragment() {
    private lateinit var binding : FragmentOrderDetailBinding
    private  val billingProductAdapter by lazy { BillingProductAdapter() }
    private val args by navArgs<OrderDetailFragmentArgs>()


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
        val order = args.order
        setUpRecycleView()

        binding.apply {
            tvOrderId.text =  "Order #${order.orderId}"
            stepView.setSteps(
                mutableListOf(
                    OrderStatus.Ordered.status,
                    OrderStatus.Confirmed.status,
                    OrderStatus.Shipped.status,
                    OrderStatus.Delivered.status,
                )
            )

            val currentOrderStatus = when(getOrderStatus(order.orderStatus)){
                is OrderStatus.Ordered -> 0
                is OrderStatus.Confirmed -> 1
                is OrderStatus.Shipped -> 2
                is OrderStatus.Delivered -> 3
                else -> 0
            }

           stepView.go(currentOrderStatus,false)
            if (currentOrderStatus ==3){
                stepView.done(true)
            }

            tvFullName.text = order.address?.fullName
            tvAddress.text = "${order.address?.street} ${order.address?.city}"
            tvPhoneNumber.text = order.address?.phone

            tvTotalPrice.text = order.totalPrice.toString()

        }

        billingProductAdapter.differ.submitList(order.listOfProduct)

    }

    private fun setUpRecycleView() {
        binding.rvProducts.apply {
            adapter = billingProductAdapter
            addItemDecoration(VerticalItemDecoration())
        }
    }

}