package com.example.ecommercei.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ecommercei.R
import com.example.ecommercei.databinding.ViewpagerImageItemBinding

class ViewPager2ImagesAdapter :RecyclerView.Adapter<ViewPager2ImagesAdapter.ViewPager2ImagesViewHolder>() {
    inner class ViewPager2ImagesViewHolder(val binding:ViewpagerImageItemBinding):
        RecyclerView.ViewHolder(binding.root){
            fun bind(imagePath:String){
                Glide.with(itemView).load(imagePath).into(binding.imageProductsDetails)
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
    val differ = AsyncListDiffer(this@ViewPager2ImagesAdapter,diffCallBack)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPager2ImagesViewHolder {
        return  ViewPager2ImagesViewHolder(
            ViewpagerImageItemBinding.inflate(
                LayoutInflater.from(parent.context),parent,false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ViewPager2ImagesViewHolder, position: Int) {
        val currentImage = differ.currentList[position]
        holder.bind(currentImage)
    }
}