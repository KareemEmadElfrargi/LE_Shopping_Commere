package com.example.ecommercei.fragments.shopping

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.ecommercei.R
import com.example.ecommercei.adapters.CartProductAdapter
import com.example.ecommercei.databinding.FragmentCartBinding
import com.example.ecommercei.firebase.FirebaseCommon
import com.example.ecommercei.utils.Resource
import com.example.ecommercei.utils.VerticalItemDecoration
import com.example.ecommercei.viewModel.CartViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CartFragment:Fragment() {
    private lateinit var binding:FragmentCartBinding
    private val cartAdapter by lazy { CartProductAdapter() }
    //It means that the ViewModel will be shared among all fragments within that activity
    private val viewModel by activityViewModels<CartViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCartBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupCartRv()

        var totalPrice = 0f
        lifecycleScope.launch {
            viewModel.productsPrice.collectLatest { price ->
                price?.let {
                    totalPrice = price
                    binding.tvTotalPrice.text = "$ $price"
                }
            }
        }

        cartAdapter.apply {
            onProductClick = { cartProduct ->
                val bundle = Bundle().apply {
                    putParcelable("product",cartProduct.product)
                }
                findNavController().navigate(R.id.action_cartFragment_to_productDetailsFragment,bundle)
            }

            onPlusClick = { cartProduct ->
                viewModel.changeQuantity(cartProduct,FirebaseCommon.QuantityChanging.INCREASE)
            }
            onMinusClick = { cartProduct ->
                viewModel.changeQuantity(cartProduct,FirebaseCommon.QuantityChanging.DECREASE)
            }
        }

        binding.buttonCheckout.setOnClickListener {
            val action = CartFragmentDirections.actionCartFragmentToBillingFragment(totalPrice,cartAdapter.differ.currentList.toTypedArray())
            findNavController().navigate(action)
        }


        lifecycleScope.launch {
            viewModel.deleteDialog.collectLatest { cartProduct ->
                val alertDialog = AlertDialog.Builder(requireContext()).apply {
                    setTitle("Delete item From Cart")
                    setMessage("Do you want delete this item from your cart?")
                    setNegativeButton("Cancel"){ dialog, _ ->
                        dialog.dismiss()
                    }
                    setPositiveButton("Yes"){ dialog, _ ->
                        viewModel.deleteCartProduct(cartProduct)
                        dialog.dismiss()
                    }
                }
                alertDialog.apply {
                    create()
                    show()
                }
            }
        }

        lifecycleScope.launch {
            viewModel.cartProducts.collectLatest {
                when(it){
                    is Resource.Loading -> {
                        binding.progressbarCart.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        binding.progressbarCart.visibility = View.INVISIBLE
                        if (it.data!!.isEmpty()){
                        showEmptyCart()
                        hideOtherViews()
                    }else {
                        hideEmptyCart()
                        showOtherViews()
                        cartAdapter.differ.submitList(it.data)
                        }

                    }
                    is Resource.Error -> {
                        binding.progressbarCart.visibility = View.INVISIBLE
                        Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT).show()
                    }
                    else -> Unit
                }

            }
        }
    }

    private fun showOtherViews() {
        binding.apply {
            rvCart.visibility = View.VISIBLE
            totalBoxContainer.visibility = View.VISIBLE
            buttonCheckout.visibility = View.VISIBLE
        }
    }

    private fun hideOtherViews() {
        binding.apply {
            rvCart.visibility = View.GONE
            totalBoxContainer.visibility = View.GONE
            buttonCheckout.visibility = View.GONE
        }
    }
    private fun hideEmptyCart() {
        binding.apply {
            layoutCartEmpty.visibility = View.GONE
        }
    }

    private fun showEmptyCart() {
        binding.apply {
            layoutCartEmpty.visibility = View.VISIBLE
        }
    }

    private fun setupCartRv() {
        binding.rvCart.apply {
            adapter = cartAdapter
            addItemDecoration(VerticalItemDecoration())
        }
    }
}