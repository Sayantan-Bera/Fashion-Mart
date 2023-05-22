package com.example.fashionmartadmin.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fashionmartadmin.R
import com.example.fashionmartadmin.databinding.ItemCategoryLayoutBinding
import com.example.fashionmartadmin.modal.CategoryModel
import java.util.*

class CategoryAdapter(var context:Context,var list: ArrayList<CategoryModel>) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(LayoutInflater.from(context).inflate(R.layout.item_category_layout,parent,false))
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.binding.categoryName.text=list[position].cate
        Glide.with(context).load(list[position].img).into(holder.binding.categoryImage)
    }

    override fun getItemCount(): Int {
        return list.size
    }
    inner class CategoryViewHolder(view : View):RecyclerView.ViewHolder(view){
        var binding=ItemCategoryLayoutBinding.bind(view)
    }
}