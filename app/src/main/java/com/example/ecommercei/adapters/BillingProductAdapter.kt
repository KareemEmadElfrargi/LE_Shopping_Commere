package com.example.ecommercei.adapters

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.ecommercei.data.Address
import com.example.ecommercei.data.CartProduct
import com.example.ecommercei.databinding.BillingProductsRvItemBinding
import com.example.ecommercei.helper.getProductPrice

class BillingProductAdapter :
    RecyclerView.Adapter<BillingProductAdapter.BillingProductViewHolder>() {

    inner class BillingProductViewHolder(val binding: BillingProductsRvItemBinding) :
        ViewHolder(binding.root) {
        fun bind(billingProduct: CartProduct) {
            binding.apply {
                Glide.with(itemView).load(billingProduct.product.images[0]).into(imageCartProduct)
                tvProductCartName.text = billingProduct.product.name
                tvBillingProductQuantity.text = billingProduct.quantity.toString()

                val priceAfterPercentage = billingProduct.product.offerPercentage
                    .getProductPrice(billingProduct.product.price)
                tvProductCartPrice.text = "$ ${String.format("%.2f", priceAfterPercentage)}"

                imageCartProductColor.setImageDrawable(
                    ColorDrawable(
                        billingProduct.selectedColor ?: Color.TRANSPARENT
                    )
                )
                tvCartProductSize.text = billingProduct.selectedSize ?: "".also {
                    imageCartProductSize.setImageDrawable(ColorDrawable(Color.TRANSPARENT))

                }
            }
        }
    }

        private val diffUtil = object : DiffUtil.ItemCallback<CartProduct>() {
            override fun areItemsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean {
                return oldItem.product == newItem.product
            }

            override fun areContentsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean {
                return oldItem == newItem
            }

        }
        val differ = AsyncListDiffer(this, diffUtil)
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): BillingProductViewHolder {
            return BillingProductViewHolder(
                BillingProductsRvItemBinding.inflate(
                    LayoutInflater.from(parent.context),parent,false
                )
            )
        }


        override fun getItemCount(): Int = differ.currentList.size
        override fun onBindViewHolder(holder: BillingProductViewHolder, position: Int) {
            val billingProduct = differ.currentList[position]
            holder.bind(billingProduct)
        }
    }