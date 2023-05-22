package com.example.fashionmartadmin.fragments

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.fashionmartadmin.R
import com.example.fashionmartadmin.adapter.AddProductImageAdapter
import com.example.fashionmartadmin.databinding.FragmentAddProductBinding
import com.example.fashionmartadmin.modal.AddProductModel
import com.example.fashionmartadmin.modal.CategoryModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.util.*


class AddProductFragment : Fragment() {


    private lateinit var binding:FragmentAddProductBinding
    private lateinit var list: ArrayList<Uri>
    private lateinit var listImages:ArrayList<String>
    private lateinit var adapter: AddProductImageAdapter
    private var coverImage:Uri?=null
    private lateinit var dialog: Dialog
    private var coverImageUrl:String?=""
    private lateinit var categoryList:ArrayList<String>
    private var launchGalleryActivity=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if(it.resultCode== Activity.RESULT_OK){
            coverImage=it.data!!.data
            binding.imageProduct.setImageURI(coverImage)
            binding.imageProduct.visibility=View.VISIBLE
        }
    }
    private var launchProductActivity=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if(it.resultCode== Activity.RESULT_OK){
            val imageUrl=it.data!!.data
            list.add(imageUrl!!)
            adapter.notifyDataSetChanged()

        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentAddProductBinding.inflate(layoutInflater)
        list= ArrayList()
        listImages= ArrayList()
        dialog=Dialog(requireContext())
        dialog.setContentView(R.layout.progress_layout)
        dialog.setCancelable(false)
        binding.selectCoverImage.setOnClickListener{
            val intent= Intent("android.intent.action.GET_CONTENT")
            intent.type="image/*"
            launchGalleryActivity.launch(intent)
        }
        binding.selectProductImage.setOnClickListener{
            val intent= Intent("android.intent.action.GET_CONTENT")
            intent.type="image/*"
            launchProductActivity.launch(intent)
        }
        setProductCategory()
        adapter= AddProductImageAdapter(list)
        binding.recyclerProductImage.adapter=adapter
        binding.addProduct.setOnClickListener{
            validateData()
        }
        return binding.root
    }

    private fun validateData() {
        if(binding.enterProductEditText.text.toString().isEmpty()){
            binding.enterProductEditText.requestFocus()
            binding.enterProductEditText.error="Please add product name"
        }else if(binding.enterDescriptionEditText.text.toString().isEmpty()){
            binding.enterDescriptionEditText.requestFocus()
            binding.enterDescriptionEditText.error="Please add product description"
        }else if(binding.enterMRPEditText.text.toString().isEmpty()){
            binding.enterMRPEditText.requestFocus()
            binding.enterMRPEditText.error="Please add product description"
        }
        else if(binding.enterSPEditText.text.toString().isEmpty()){
            binding.enterSPEditText.requestFocus()
            binding.enterSPEditText.error="Please add product description"
        }else if(coverImage==null){
            Toast.makeText(requireContext(),"Please add cover image", Toast.LENGTH_LONG).show()
        }else if(list.size<1){
            Toast.makeText(requireContext(),"Please add product images", Toast.LENGTH_LONG).show()
        }else{
            uploadImage()
        }
    }

    private fun uploadImage() {
        dialog.show()
        val fileName= UUID.randomUUID().toString()+".jpg"
        val refStorage= FirebaseStorage.getInstance().reference.child("products/$fileName")
        refStorage.putFile(coverImage!!)
            .addOnSuccessListener {
                it.storage.downloadUrl.addOnSuccessListener{image ->
                    coverImageUrl=image.toString()
                    uploadProductImage()

                }
            }
            .addOnFailureListener{
                dialog.dismiss()
                Toast.makeText(requireContext(),"Something went wrong with storage",Toast.LENGTH_LONG).show()
            }
    }

    private var i=0
    private fun uploadProductImage() {
        dialog.show()
        val fileName= UUID.randomUUID().toString()+".jpg"
        val refStorage= FirebaseStorage.getInstance().reference.child("products/$fileName")
        refStorage.putFile(list[i])
            .addOnSuccessListener {
                it.storage.downloadUrl.addOnSuccessListener{image ->
                    listImages.add(image!!.toString())
                    if(list.size==listImages.size){
                        storeData()
                    }else{
                        i+=1
                        uploadProductImage()
                    }

                }
            }
            .addOnFailureListener{
                dialog.dismiss()
                Toast.makeText(requireContext(),"Something went wrong with storage",Toast.LENGTH_LONG).show()
            }
    }

    private fun storeData() {
        val db=Firebase.firestore.collection("products")
        val key=db.document().id
        val date=AddProductModel(
            binding.enterProductEditText.text.toString(),
            binding.enterDescriptionEditText.text.toString(),
            coverImageUrl.toString(),
            categoryList[binding.productCategorySpinner.selectedItemPosition],
            key,
            binding.enterMRPEditText.text.toString(),
            binding.enterSPEditText.text.toString(),
            listImages
        )
        db.document(key).set(date).addOnSuccessListener {
            dialog.dismiss()
            Toast.makeText(requireContext(),"Product added", Toast.LENGTH_LONG).show()
            binding.enterProductEditText.text=null
            binding.enterDescriptionEditText.text=null
            binding.enterMRPEditText.text=null
            binding.enterSPEditText.text=null
            list.clear()
            listImages.clear()
            binding.imageProduct.visibility= GONE
        }
            .addOnFailureListener {
                dialog.dismiss()
                Toast.makeText(requireContext(),"Something went wrong", Toast.LENGTH_LONG).show()
            }
    }

    private fun setProductCategory(){
        categoryList= ArrayList()
        Firebase.firestore.collection("categories").get().addOnSuccessListener {
            categoryList.clear()
            for (doc in it.documents){
                val data=doc.toObject(CategoryModel::class.java)
                categoryList.add(data!!.cate!!)
            }
            categoryList.add(0,"Select Category")
            val arrayAdapter=ArrayAdapter(requireContext(),R.layout.dropdown_item_layout,categoryList)
            binding.productCategorySpinner.adapter=arrayAdapter
        }
    }

}