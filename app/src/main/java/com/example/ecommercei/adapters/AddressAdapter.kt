package com.example.ecommercei.adapters

import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Adapter
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.ecommercei.R
import com.example.ecommercei.data.Address
import com.example.ecommercei.databinding.AddressRvItemBinding

class AddressAdapter : RecyclerView.Adapter<AddressAdapter.AddressViewHolder>() {

    inner class AddressViewHolder(val binding : AddressRvItemBinding): ViewHolder(binding.root) {
        fun bind(address: Address , isSelected :Boolean){
            binding.apply {
                buttonAddress.text = address.addressTitle
                if (isSelected){
                    buttonAddress.background = ColorDrawable(itemView.context.getColor(R.color.g_blue))
                } else {
                    buttonAddress.background = ColorDrawable(itemView.context.getColor(R.color.white))
                }
            }
        }
    }

    private val diffUtil = object : DiffUtil.ItemCallback<Address>(){
        override fun areItemsTheSame(oldItem: Address, newItem: Address): Boolean {
            return oldItem.addressTitle == newItem.addressTitle && oldItem.fullName == newItem.fullName
        }

        override fun areContentsTheSame(oldItem: Address, newItem: Address): Boolean {
            return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this@AddressAdapter,diffUtil)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        return AddressViewHolder(
            AddressRvItemBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }
    var selectedAddress = -1
    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        val address = differ.currentList[position]
        holder.bind(address,selectedAddress == position)
        holder.binding.buttonAddress.setOnClickListener {
            if (selectedAddress >= 0){
                notifyItemChanged(selectedAddress)
            }
            selectedAddress = holder.adapterPosition
            notifyItemChanged(selectedAddress)
            onClick?.invoke(address)
        }

    }

    override fun getItemCount(): Int = differ.currentList.size

    var onClick : ((Address)->Unit)? = null

}