package com.example.fashionmart.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fashionmart.activity.ProductDetailsActivity
import com.example.fashionmart.databinding.ItemCategoryProductLayoutBinding
import com.example.fashionmart.databinding.LayoutProductItemBinding
import com.example.fashionmart.modal.AddProductModel
import java.util.*

class CategoryProductAdapter(val context:Context,val list: ArrayList<AddProductModel>):RecyclerView.Adapter<CategoryProductAdapter.CategoryProductViewHolder>() {
    inner class CategoryProductViewHolder(val binding:ItemCategoryProductLayoutBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryProductViewHolder {
        val binding= ItemCategoryProductLayoutBinding.inflate(LayoutInflater.from(context),parent,false)
        return CategoryProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryProductViewHolder, position: Int) {
        Glide.with(context).load(list[position].productCoverImg).into(holder.binding.imageView2)
        holder.binding.textView5.text=list[position].productName
        holder.binding.button3.text="Price: ₹"+list[position].productSp
        holder.binding.textView7.paintFlags = holder.binding.textView7.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        holder.binding.textView7.text="₹"+list[position].productMrp
        holder.itemView.setOnClickListener{
            val intent=Intent(context,ProductDetailsActivity::class.java)
            intent.putExtra("id",list[position].productId)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
       return list.size
    }
}