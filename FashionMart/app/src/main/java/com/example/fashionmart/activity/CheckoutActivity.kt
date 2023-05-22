package com.example.fashionmart.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.fashionmart.MainActivity
import com.example.fashionmart.R
import com.example.fashionmart.databinding.ActivityCheckoutBinding
import com.example.fashionmart.roomdb.AppDatabase
import com.example.fashionmart.roomdb.ProductModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

class CheckoutActivity : AppCompatActivity() , PaymentResultListener {
    private lateinit var binding:ActivityCheckoutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val price=intent.getStringExtra("totalCost")
        Checkout.preload(applicationContext)
        val co = Checkout()
        co.setKeyID("rzp_test_0xUnETDa8goGaN")
        try {
            val options = JSONObject()
            options.put("name","Fashion Mart")
            options.put("description","Get best deals")
            //You can omit the image option to fetch the image from the dashboard
//            options.put("image","https://s3.amazonaws.com/rzp-mobile/images/rzp.jpg")
            options.put("theme.color", "#ff595e");
            options.put("currency","INR");
//            options.put("order_id", "order_DBJOWzybf0sJbb");
            options.put("amount",(price!!.toInt()*100).toString())//pass amount in currency subunits

            val prefill = JSONObject()
            prefill.put("email","sayantanbera133@gmail.com")
            prefill.put("contact","7439469261")

            options.put("prefill",prefill)
            co.open(this,options)
        }catch (e: Exception){
            Toast.makeText(this,"Error in payment",Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

    override fun onPaymentSuccess(p0: String?) {
       uploadData()
    }

    private fun uploadData() {
        val id=intent.getStringArrayListExtra("productIds")
        for (currentId in id!!){
            fetchData(currentId)
        }
    }

    private fun fetchData(productId: String?) {

        val dao=AppDatabase.getInstance(this).productDao()

        Firebase.firestore.collection("products")
               .document(productId!!).get().addOnSuccessListener {
                   lifecycleScope.launch(Dispatchers.IO){
                   dao.deleteProduct(ProductModel(productId))
                   }
                       saveData(it.getString("productName"),
                       it.getString("productSp"),
                   productId)
               }
    }

    private fun saveData(name: String?, price: String?, productId: String) {
       val preferences=this.getSharedPreferences("user", MODE_PRIVATE)
        val data = hashMapOf<String ,Any>()
        data["name"]=name!!
        data["price"]=price!!
        data["productId"]=productId
        data["status"]="Ordered"
        data["userId"]=preferences.getString("number","")!!
        val firestore=Firebase.firestore.collection("allOrders")
        val key=firestore.document().id
        data["orderId"]=key

        firestore.document(key).set(data).addOnSuccessListener {
            Toast.makeText(this,"Order placed",Toast.LENGTH_LONG).show()

                startActivity(Intent(this,MainActivity::class.java))
                finish()


        }
            .addOnFailureListener {
                Toast.makeText(this,"Something went wrong",Toast.LENGTH_LONG).show()
            }
    }

    override fun onPaymentError(p0: Int, p1: String?) {
        Toast.makeText(this,"Error in payment",Toast.LENGTH_LONG).show()
    }

}