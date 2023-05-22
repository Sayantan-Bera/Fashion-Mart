package com.example.fashionmart.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.fashionmart.R
import com.example.fashionmart.activity.EditProfileActivity
import com.example.fashionmart.activity.LoginActivity
import com.example.fashionmart.activity.MyOrdersActivity
import com.example.fashionmart.databinding.FragmentMoreBinding
import com.google.firebase.auth.FirebaseAuth


class MoreFragment : Fragment() {

     private lateinit var binding:FragmentMoreBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentMoreBinding.inflate(layoutInflater)
        binding.editProfile.setOnClickListener {
            val intent=Intent(context,EditProfileActivity::class.java)
            startActivity(intent)
        }
        binding.myOrders.setOnClickListener {
            val intent=Intent(context,MyOrdersActivity::class.java)
            startActivity(intent)
        }
        binding.logOut.setOnClickListener {
            logout()
        }
        return binding.root
    }

    private fun logout(){
        FirebaseAuth.getInstance().signOut()
        val intent=Intent(context,LoginActivity::class.java)
        startActivity(intent)
        activity?.onBackPressed()
    }

}