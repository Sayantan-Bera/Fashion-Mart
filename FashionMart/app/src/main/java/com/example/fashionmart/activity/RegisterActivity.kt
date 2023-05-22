package com.example.fashionmart.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.fashionmart.R
import com.example.fashionmart.databinding.ActivityRegisterBinding
import com.example.fashionmart.model.UserModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding:ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.signIn.setOnClickListener {
            openLogin()
        }
        binding.signUp.setOnClickListener {
            validateUser()
        }
    }

    private fun validateUser() {
        if(binding.nameEditText.text!!.isEmpty() || binding.phoneEditText.text!!.isEmpty()){
            Toast.makeText(this,"Please enter all fields",Toast.LENGTH_LONG).show()
        }else{
            storeData()
        }
    }

    private fun storeData() {
        val builder=AlertDialog.Builder(this)
            .setTitle("Loading...")
            .setMessage("Please wait")
            .setCancelable(false)
            .create()
        builder.show()
        val preferences=this.getSharedPreferences("user", MODE_PRIVATE)
        val editor=preferences.edit()
        editor.putString("number",binding.phoneEditText.text.toString())
        editor.putString("name",binding.nameEditText.text.toString())
        editor.apply()
        val data=UserModel(binding.nameEditText.text.toString(),binding.phoneEditText.text.toString())
        Firebase.firestore.collection("users").document(binding.phoneEditText.text.toString())
            .set(data).addOnSuccessListener {
                Toast.makeText(this,"Successfully registered",Toast.LENGTH_SHORT).show()
                builder.dismiss()
                openLogin()
            }.addOnFailureListener{
                builder.dismiss()
                Toast.makeText(this,"Something went wrong",Toast.LENGTH_SHORT).show()
            }
    }

    private fun openLogin() {
        startActivity(Intent(this,LoginActivity::class.java))
        finish()
    }
}