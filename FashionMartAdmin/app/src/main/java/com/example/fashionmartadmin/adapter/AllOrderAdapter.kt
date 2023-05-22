package com.example.fashionmartadmin.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.INVISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.fashionmartadmin.databinding.AllOrdersAdminLayoutBinding
import com.example.fashionmartadmin.modal.AllOrderModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AllOrderAdapter(val context: Context, private val list: ArrayList<AllOrderModel>):RecyclerView.Adapter<AllOrderAdapter.AllOrderViewHolder>() {
    inner class AllOrderViewHolder(val binding:AllOrdersAdminLayoutBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllOrderViewHolder {
        return AllOrderViewHolder(AllOrdersAdminLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: AllOrderViewHolder, position: Int) {
        holder.binding.productName.text=list[position].name
        holder.binding.productPrice.text="₹"+list[position].price

        holder.binding.cancel.setOnClickListener {
//            holder.binding.proceed.text="Cancelled"

            updateStatus("Cancelled",list[position].orderId!!)
//            notifyDataSetChanged()
//            holder.binding.cancel.isEnabled=false
//            holder.binding.cancel.text="Cancelled"F
//            holder.binding.proceed.visibility= GONE
        }
        when(list[position].status){
            "Ordered"->{
                holder.binding.proceed.text="Dispatched"
                holder.binding.proceed.setOnClickListener {
                    updateStatus("Dispatched",list[position].orderId!!)
//                    holder.binding.proceed.text="Delivered"
//                    notifyDataSetChanged()
                }

            }
            "Dispatched"->{
                holder.binding.proceed.text="Delivered"
                holder.binding.proceed.setOnClickListener {
                    updateStatus("Delivered",list[position].orderId!!)
//                    holder.binding.proceed.isEnabled=false
//                    holder.binding.cancel.visibility= GONE
//                    holder.binding.proceed.text="Already Delivered"
//                    notifyDataSetChanged()
                }
            }
            "Delivered"->{
                holder.binding.proceed.isEnabled=false
                holder.binding.cancel.visibility= GONE
                holder.binding.proceed.text="Already Delivered"
//                holder.binding.proceed.setOnClickListener {
//                    updateStatus("Delivered",list[position].orderId!!)
//                }
            }
            "Cancelled"->{
                holder.binding.cancel.isEnabled=false
                holder.binding.cancel.text="Cancelled"
                holder.binding.proceed.visibility= GONE
            }
        }
    }
    private fun updateStatus(str:String, doc:String){
        val data= hashMapOf<String,Any>()
        data["status"]=str
        Firebase.firestore.collection("allOrders")
            .document(doc).update(data).addOnSuccessListener {
                Toast.makeText(context,"Status updated",Toast.LENGTH_SHORT).show()
            }.addOnFailureListener{
                Toast.makeText(context,"Something went wrong",Toast.LENGTH_SHORT).show()
            }
    }
}