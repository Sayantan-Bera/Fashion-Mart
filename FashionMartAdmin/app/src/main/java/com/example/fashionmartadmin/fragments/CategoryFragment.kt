package com.example.fashionmartadmin.fragments

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.fashionmartadmin.R
import com.example.fashionmartadmin.adapter.CategoryAdapter
import com.example.fashionmartadmin.databinding.FragmentCategoryBinding
import com.example.fashionmartadmin.modal.CategoryModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class CategoryFragment : Fragment() {

    private lateinit var binding: FragmentCategoryBinding
    private var imageUrl: Uri?=null
    private lateinit var dialog: Dialog
    private var launchGalleryActivity=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if(it.resultCode== Activity.RESULT_OK){
            imageUrl=it.data!!.data
            binding.imageView.setImageURI(imageUrl)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentCategoryBinding.inflate(layoutInflater)
        dialog=Dialog(requireContext())
        dialog.setContentView(R.layout.progress_layout)
        dialog.setCancelable(false)
        getData()
        binding.imageView.setOnClickListener{
            val intent= Intent("android.intent.action.GET_CONTENT")
            intent.type="image/*"
            launchGalleryActivity.launch(intent)
        }
        binding.addCategory.setOnClickListener{
            validateData(binding.enterCategoryEditText.text.toString())
        }
        return binding.root
    }

    private fun getData() {
        val list=ArrayList<CategoryModel>()
        Firebase.firestore.collection("categories")
            .get().addOnSuccessListener {
                list.clear()
                for(doc in it.documents){
                    val data=doc.toObject(CategoryModel::class.java)
                    list.add(data!!)
                }
                binding.categoryRecycler.adapter=CategoryAdapter(requireContext(),list)
            }
    }

    private fun validateData(categoryName: String) {
        if(categoryName.isEmpty()){
            Toast.makeText(requireContext(),"Please add category", Toast.LENGTH_LONG).show()
        }else if(imageUrl==null){
            Toast.makeText(requireContext(),"Please select category image", Toast.LENGTH_LONG).show()
        }else{
            uploadImage(categoryName)
        }
    }

    private fun uploadImage(categoryName: String) {
        dialog.show()
        val fileName= UUID.randomUUID().toString()+".jpg"
        val refStorage= FirebaseStorage.getInstance().reference.child("category/$fileName")
        refStorage.putFile(imageUrl!!)
            .addOnSuccessListener {
                it.storage.downloadUrl.addOnSuccessListener{image ->
                    storageData(categoryName,image.toString())

                }
            }
            .addOnFailureListener{
                dialog.dismiss()
                Toast.makeText(requireContext(),"Something went wrong with storage",Toast.LENGTH_LONG).show()
            }
    }

    private fun storageData(categoryName: String, url: String) {
        val db= Firebase.firestore
        val data = hashMapOf<String,Any>(
            "cate" to categoryName ,
            "img" to url
        )
        db.collection("categories").add(data)
            .addOnSuccessListener {
                dialog.dismiss()
                binding.imageView.setImageDrawable(resources.getDrawable(R.drawable.image_preview))
                binding.enterCategoryEditText.text=null
                getData()
                Toast.makeText(requireContext(),"Category added",Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener {
                dialog.dismiss()
                Toast.makeText(requireContext(),"Something went wrong",Toast.LENGTH_LONG).show()
            }
    }


}