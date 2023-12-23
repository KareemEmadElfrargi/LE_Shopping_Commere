package com.example.ecommercei.adapters

import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommercei.databinding.ColorRvItemBinding
import com.example.ecommercei.databinding.SizeRvItemBinding

class SizesAdapter : RecyclerView.Adapter<SizesAdapter.SizesViewHolder>() {
    private var selectedPosition = -1
    inner class SizesViewHolder(private val binding : SizeRvItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(size:String,position: Int){
            binding.tvSize.text = size
            if (position == selectedPosition){
                binding.apply {
                    imageShadow.visibility = View.VISIBLE
                }
            } else {
                binding.apply {
                    imageShadow.visibility = View.INVISIBLE
                }
            }
        }
    }
    private val diffCallBack = object : DiffUtil.ItemCallback<String>(){
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this@SizesAdapter,diffCallBack)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SizesViewHolder {
        return SizesViewHolder(
            SizeRvItemBinding.inflate(
                LayoutInflater.from(parent.context),parent,false
            )
        )
    }
    override fun getItemCount(): Int  = differ.currentList.size
    override fun onBindViewHolder(holder: SizesViewHolder, position: Int) {
        val size = differ.currentList[position]
        holder.bind(size,position)
        holder.itemView.setOnClickListener {
            if (selectedPosition >=0){
                notifyItemChanged(selectedPosition)
            }
            selectedPosition = holder.adapterPosition
            notifyItemChanged(selectedPosition)
            onItemClick?.invoke(size)
        }
    }
    var onItemClick : ((String)-> Unit )? = null
}