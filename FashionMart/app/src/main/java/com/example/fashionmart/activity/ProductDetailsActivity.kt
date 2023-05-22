package com.example.fashionmart.activity

import android.content.Intent
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.fashionmart.MainActivity
import com.example.fashionmart.R
import com.example.fashionmart.databinding.ActivityProductDetailsBinding
import com.example.fashionmart.roomdb.AppDatabase
import com.example.fashionmart.roomdb.ProductDao
import com.example.fashionmart.roomdb.ProductModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class ProductDetailsActivity : AppCompatActivity() {
    private lateinit var binding:ActivityProductDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityProductDetailsBinding.inflate(layoutInflater)
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
          val slideList=ArrayList<SlideModel>()
          for(data in list){
              slideList.add(SlideModel(data, ScaleTypes.CENTER_CROP))
          }
          binding.imageSlider.setImageList(slideList)
          cartAction(proId,name,productSp,it.getString("productCoverImg"))
      }.addOnFailureListener {
          Toast.makeText(this,"Something went wrong", Toast.LENGTH_LONG).show()
      }
    }

    private fun cartAction(proId: String, name: String?, productSp: String?, coverImg: String?) {
        val productDao=AppDatabase.getInstance(this).productDao()
        if(productDao.isExist(proId)!=null){
            binding.addToCart.text="Go to Cart"
        }else{
            binding.addToCart.text="Add to Cart"
        }
        binding.addToCart.setOnClickListener{
            if(productDao.isExist(proId)!=null){
                openCart()
            }else{
                addToCart(productDao,proId,name,productSp,coverImg)
            }
        }

    }

    private fun addToCart(productDao: ProductDao, proId: String, name: String?, productSp: String?, coverImg: String?) {
       val data=ProductModel(proId,name,coverImg,productSp)
        lifecycleScope.launch(Dispatchers.IO) {
            productDao.insertProduct(data)
            binding.addToCart.text="Go to cart"
        }
    }

    private fun openCart() {
        val preference=this.getSharedPreferences("info", MODE_PRIVATE)
        val editor=preference.edit()
        editor.putBoolean("isCart",true)
        editor.apply()
        startActivity(Intent(this,MainActivity::class.java))
        finish()
    }
}