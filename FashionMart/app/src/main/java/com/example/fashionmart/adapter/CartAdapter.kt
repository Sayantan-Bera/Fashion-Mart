package com.example.fashionmart.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fashionmart.activity.ProductDetailsActivity
import com.example.fashionmart.databinding.ItemCartProductLayoutBinding
import com.example.fashionmart.databinding.ItemCategoryProductLayoutBinding
import com.example.fashionmart.databinding.LayoutCategoryItemBinding
import com.example.fashionmart.roomdb.AppDatabase
import com.example.fashionmart.roomdb.ProductModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CartAdapter(val context:Context,val list: List<ProductModel>):RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    inner class CartViewHolder(val binding: ItemCartProductLayoutBinding):
            RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
       val binding=ItemCartProductLayoutBinding.inflate(LayoutInflater.from(context),parent,false)
        return CartViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        Glide.with(context).load(list[position].productImage).into(holder.binding.coverImage)
        holder.binding.productName.text=list[position].productName
        holder.binding.productPrice.text="Price: ₹"+list[position].productSp

        holder.itemView.setOnClickListener {
            val intent=Intent(context,ProductDetailsActivity::class.java)
            intent.putExtra("id",list[position].productId)
            context.startActivity(intent)
        }
        val dao=AppDatabase.getInstance(context).productDao()
        holder.binding.delete.setOnClickListener {
            GlobalScope.launch(Dispatchers.IO) {
                dao.deleteProduct(
                    ProductModel(
                        list[position].productId,
                        list[position].productName,
                        list[position].productImage,
                        list[position].productSp
                    )
                )
            }
            }
    }
}