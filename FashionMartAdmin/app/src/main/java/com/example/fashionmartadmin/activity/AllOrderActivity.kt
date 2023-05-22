package com.example.fashionmartadmin.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.example.fashionmartadmin.R
import com.example.fashionmartadmin.adapter.AllOrderAdapter
import com.example.fashionmartadmin.databinding.ActivityAllOrderBinding
import com.example.fashionmartadmin.modal.AllOrderModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlin.math.log

class AllOrderActivity : AppCompatActivity() {
    private lateinit var binding:ActivityAllOrderBinding
    private lateinit var list: ArrayList<AllOrderModel>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityAllOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        list= ArrayList()


        Firebase.firestore.collection("allOrders").get().addOnSuccessListener {
            list.clear()
            for (doc in it){
                val data=doc.toObject(AllOrderModel::class.java)
                list.add(data)
            }
            binding.orderRecycler2.adapter=AllOrderAdapter(this,list)
            Log.e("list",list.toString())
        }

        Log.e("eee","adapter loaded")
    }
}