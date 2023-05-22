package com.example.fashionmart.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fashionmart.R
import com.example.fashionmart.adapter.CategoryProductAdapter
import com.example.fashionmart.adapter.ProductAdapter
import com.example.fashionmart.modal.AddProductModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*

class CategoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)
        getProducts(intent.getStringExtra("cat"))
    }

    private fun getProducts(category: String?) {
        val list= ArrayList<AddProductModel>()
        Firebase.firestore.collection("products").whereEqualTo("productCategory",category)
            .get().addOnSuccessListener {
                list.clear()
                for(doc in it.documents){
                    val data=doc.toObject(AddProductModel::class.java)
                    list.add(data!!)
                }
                val recyclerView=findViewById<RecyclerView>(R.id.categoryRecycler2)
                recyclerView.layoutManager=GridLayoutManager(this,2)
                recyclerView.adapter= CategoryProductAdapter(this,list)
            }
    }
}