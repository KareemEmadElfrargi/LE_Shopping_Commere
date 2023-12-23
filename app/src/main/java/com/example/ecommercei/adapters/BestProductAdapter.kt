package com.example.ecommercei.adapters

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ecommercei.data.Product
import com.example.ecommercei.databinding.ProductRvItemBinding

class BestProductAdapter:RecyclerView.Adapter<BestProductAdapter.BestProductsViewHolder>() {

    inner class BestProductsViewHolder(private val binding: ProductRvItemBinding):
        RecyclerView.ViewHolder(binding.root){
        fun bind(product: Product){
            binding.apply {
                Glide.with(itemView).load(product.images[0]).into(imgProduct)
                product.offerPercentage?.let { offerPercentage ->
                    val remainingPricePercentage = 100f - offerPercentage
                    val priceAfterOffer = (remainingPricePercentage/100) * product.price
                    tvNewPrice.text = "$ ${String.format("%.2f",priceAfterOffer)}"
                    tvPrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG // add line middle of Text
                }
                if (product.offerPercentage == null){
                    tvNewPrice.visibility = View.INVISIBLE
                }
                tvPrice.text = "$ ${product.price}"
                tvName.text = product.name
            }
        }
    }

    private val diffCallback = object : DiffUtil.ItemCallback<Product>(){
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this@BestProductAdapter,diffCallback)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BestProductsViewHolder {
        val inflate = LayoutInflater.from(parent.context)
        return BestProductsViewHolder(
            ProductRvItemBinding.inflate(inflate)
        )
    }
    override fun getItemCount(): Int = differ.currentList.size
    override fun onBindViewHolder(holder: BestProductsViewHolder, position: Int) {
        val currentProduct = differ.currentList[position]
        holder.bind(currentProduct)
        holder.itemView.setOnClickListener {
            onClick?.invoke(currentProduct)
        }
    }
    var onClick : ((product:Product)->Unit)? = null
}