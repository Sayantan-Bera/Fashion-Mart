package com.example.fashionmartadmin.activity

import android.content.Intent
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.fashionmartadmin.R
import com.example.fashionmartadmin.databinding.ActivityProductDetailsBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.ArrayList

class ProductDetailsActivity : AppCompatActivity() {
    private lateinit var binding:ActivityProductDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityProductDetailsBinding.inflate(layoutInflater)
        getProductDetails(intent.getStringExtra("id"))
        setContentView(binding.root)
    }

    private fun getProductDetails(proId: String?) {
        Firebase.firestore.collection("products").document(proId!!).get().addOnSuccessListener {
            val list=it.get("productImages") as ArrayList<String>
            val name=it.getString("productName")
            binding.name.text=name
            val productSp=it.getString("productSp")
            binding.sp.text="Price: ₹"+productSp
            binding.description.text=it.getString("productDescription")
            binding.mrp.paintFlags = binding.mrp.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            binding.mrp.text="₹"+it.getString("productMrp")
            val slideList= ArrayList<SlideModel>()
            for(data in list){
                slideList.add(SlideModel(data, ScaleTypes.CENTER_CROP))
            }
            binding.imageSlider.setImageList(slideList)

        }.addOnFailureListener {
            Toast.makeText(this,"Something went wrong", Toast.LENGTH_LONG).show()
        }
    }




}