package com.example.fashionmart.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.fashionmart.R
import com.example.fashionmart.adapter.AllOrderAdapter
import com.example.fashionmart.databinding.ActivityMyOrdersBinding
import com.example.fashionmart.model.AllOrderModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MyOrdersActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMyOrdersBinding
    private lateinit var list:ArrayList<AllOrderModel>
    override fun onCreate(savedInstanceState: Bundle?) {
        binding=ActivityMyOrdersBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        list= ArrayList()


        val preferences=this.getSharedPreferences("user", MODE_PRIVATE)
        Firebase.firestore.collection("allOrders")
            .whereEqualTo("userId",preferences.getString("number","")!!).get().addOnSuccessListener {
            list.clear()
            for (doc in it){
                val data=doc.toObject(AllOrderModel::class.java)
                list.add(data)
            }
            binding.recyclerView2.adapter= AllOrderAdapter(this,list)
            Log.e("list",list.toString())
        }
    }
}