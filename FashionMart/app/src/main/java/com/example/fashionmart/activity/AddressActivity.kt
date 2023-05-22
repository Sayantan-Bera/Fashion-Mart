package com.example.fashionmart.activity

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.fashionmart.R
import com.example.fashionmart.databinding.ActivityAddressBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AddressActivity : AppCompatActivity() {
    private lateinit var binding:ActivityAddressBinding
    private lateinit var builder: AlertDialog
    private lateinit var preferences:SharedPreferences
    private lateinit var totalCost:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)
        preferences=this.getSharedPreferences("user", MODE_PRIVATE)
        builder= AlertDialog.Builder(this)
            .setTitle("Loading...")
            .setMessage("Please wait")
            .setCancelable(false)
            .create()
        totalCost=intent.getStringExtra("totalCost")!!
        loadUserInfo()
        binding.proceedCheckout.setOnClickListener {
            validateData(binding.enterNmae.text.toString(),binding.enterMobileNumber.text.toString(),binding.enterRoad.text.toString(),binding.enterLandmark.text.toString()
            ,binding.enterState.text.toString(),binding.enterCity.text.toString(),binding.enterPinCode.text.toString())
            
        }
    }

    private fun validateData(name: String, mobile: String,road:String, landmark: String, state: String, city: String, pinCode: String) {
      if(name.isEmpty()||mobile.isEmpty()||road.isEmpty()||landmark.isEmpty()||state.isEmpty()||pinCode.isEmpty()||city.isEmpty()){
          Toast.makeText(this,"Please enter all fields", Toast.LENGTH_LONG).show()
      }else{
          storeData(road,landmark,state,city,pinCode)
      }
    }

    private fun storeData( road: String, landmark: String, state: String, city: String,pinCode: String) {
       val map= hashMapOf<String,Any>()
        map["road"]=road
        map["landmark"]=landmark
        map["state"]=state
        map["city"]=city
        map["pinCode"]=pinCode
        builder.show()
        Firebase.firestore.collection("users")
            .document(preferences.getString("number","")!!)
            .update(map).addOnSuccessListener {
                builder.dismiss()
                val b=Bundle()
                b.putStringArrayList("productIds",intent.getStringArrayListExtra("productIds"))
                b.putString("totalCost",totalCost)
                val intent= Intent(this, CheckoutActivity::class.java)
                intent.putExtras(b)
                startActivity(intent)
            }
            .addOnFailureListener {
                builder.dismiss()
                Toast.makeText(this,"Something went wrong", Toast.LENGTH_LONG).show()
            }
    }

    private fun loadUserInfo() {

        builder.show()
        Firebase.firestore.collection("users")
            .document(preferences.getString("number","")!!)
            .get().addOnSuccessListener {
                builder.dismiss()
                binding.enterNmae.setText(it.getString("userName"))
                binding.enterMobileNumber.setText(it.getString("userPhoneNumber"))
                binding.enterRoad.setText(it.getString("road"))
                binding.enterLandmark.setText(it.getString("landmark"))
                binding.enterState.setText(it.getString("state"))
                binding.enterCity.setText(it.getString("city"))
                binding.enterPinCode.setText(it.getString("pinCode"))

            }.addOnFailureListener {
                builder.dismiss()
                Toast.makeText(this,"Something went wrong", Toast.LENGTH_LONG).show()
            }
    }
}