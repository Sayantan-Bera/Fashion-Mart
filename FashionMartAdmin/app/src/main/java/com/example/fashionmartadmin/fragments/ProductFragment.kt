package com.example.fashionmartadmin.fragments

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.example.fashionmartadmin.R
import com.example.fashionmartadmin.adapter.ProductAdapter
import com.example.fashionmartadmin.databinding.FragmentProductBinding
import com.example.fashionmartadmin.modal.AddProductModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.ArrayList


class ProductFragment : Fragment() {

    private lateinit var binding:FragmentProductBinding
    private lateinit var dialog:Dialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentProductBinding.inflate(layoutInflater)

        dialog= Dialog(requireContext())
        dialog.setContentView(R.layout.progress_layout)
        dialog.setCancelable(false)
        getProducts()
        binding.addButton.setOnClickListener{
            Navigation.findNavController(it).navigate(R.id.action_productFragment_to_addProductFragment)
            getProducts()
        }

        return binding.root
    }
    fun getProducts() {
        dialog.show()
        val list= ArrayList<AddProductModel>()
        Firebase.firestore.collection("products")
            .get().addOnSuccessListener {
                dialog.dismiss()
                list.clear()
                for(doc in it.documents){
                    val data=doc.toObject(AddProductModel::class.java)
                    list.add(data!!)
                }
                binding.recyclerViewProduct.layoutManager= GridLayoutManager(requireContext(),2)
                binding.recyclerViewProduct.adapter= ProductAdapter(requireContext(),list)
            }.addOnFailureListener {
                dialog.dismiss()

                Toast.makeText(requireContext(),"Something went wrong", Toast.LENGTH_LONG).show()

            }
    }



}