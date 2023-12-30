package com.example.ecommercei.adapters

import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.ecommercei.R
import com.example.ecommercei.data.order.Order
import com.example.ecommercei.data.order.OrderStatus
import com.example.ecommercei.data.order.getOrderStatus
import com.example.ecommercei.databinding.OrderItemBinding

class AllOrdersAdapter : Adapter<AllOrdersAdapter.orderViewHolder>() {

    inner class orderViewHolder(private val binding:OrderItemBinding) :ViewHolder(binding.root){
        fun bind(order: Order){
            binding.apply {
                tvOrderId.text = order.orderId.toString()
                tvOrderDate.text = order.date
                val resources = itemView.resources
                val colorDrawable =  when(getOrderStatus(order.orderStatus)){
                    is OrderStatus.Canceled -> {
                        ColorDrawable(resources.getColor(R.color.g_red))
                    }
                    is OrderStatus.Confirmed -> {
                        ColorDrawable(resources.getColor(R.color.g_green))
                    }
                    is OrderStatus.Delivered -> {
                        ColorDrawable(resources.getColor(R.color.g_green))
                    }
                    is OrderStatus.Ordered -> {
                        ColorDrawable(resources.getColor(R.color.g_orange_yellow))
                    }
                    is OrderStatus.Returned -> {
                        ColorDrawable(resources.getColor(R.color.g_red))
                    }
                    is OrderStatus.Shipped -> {
                        ColorDrawable(resources.getColor(R.color.g_green))
                    }
                }
                imageOrderState.setImageDrawable(colorDrawable)

            }
        }
    }

    private val diffCallBack = object : DiffUtil.ItemCallback<Order>(){
        override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem.listOfProduct == newItem.listOfProduct
        }

        override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this,diffCallBack)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): orderViewHolder {
        return orderViewHolder(
            OrderItemBinding.inflate(
                LayoutInflater.from(parent.context),parent,false
            )
        )
    }

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: orderViewHolder, position: Int) {
        val currentOrder = differ.currentList[position]
        holder.bind(currentOrder)

        holder.itemView.setOnClickListener {
            onClick?.invoke(currentOrder)
        }
    }


    var onClick : ((Order) ->Unit)? = null

}