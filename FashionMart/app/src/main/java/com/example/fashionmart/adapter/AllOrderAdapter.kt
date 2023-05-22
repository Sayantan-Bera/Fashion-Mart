package com.example.fashionmart.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.INVISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.fashionmart.databinding.OrdersItemLayoutBinding
import com.example.fashionmart.model.AllOrderModel

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AllOrderAdapter(val context: Context, private val list: ArrayList<AllOrderModel>):RecyclerView.Adapter<AllOrderAdapter.AllOrderViewHolder>() {
    inner class AllOrderViewHolder(val binding:OrdersItemLayoutBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllOrderViewHolder {
        return AllOrderViewHolder(OrdersItemLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: AllOrderViewHolder, position: Int) {
        holder.binding.productName.text=list[position].name
        holder.binding.productPrice.text="Price: ₹"+list[position].price

        holder.binding.status.text=list[position].status

    }

}