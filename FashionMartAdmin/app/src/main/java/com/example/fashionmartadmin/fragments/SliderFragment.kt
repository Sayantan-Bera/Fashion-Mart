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
import com.example.fashionmartadmin.databinding.FragmentSliderBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.util.*


class SliderFragment : Fragment() {

    private lateinit var binding:FragmentSliderBinding
    private var imageUrl: Uri?=null
    private lateinit var dialog: Dialog
    private var launchGalleryActivity=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
           if(it.resultCode== Activity.RESULT_OK){
               imageUrl=it.data!!.data
               binding.imageSlider.setImageURI(imageUrl)
           }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentSliderBinding.inflate(layoutInflater)
        dialog=Dialog(requireContext())
        dialog.setContentView(R.layout.progress_layout)
        dialog.setCancelable(false)
        binding.uploadImage.setOnClickListener{
            val intent= Intent("android.intent.action.GET_CONTENT")
            intent.type="image/*"
            launchGalleryActivity.launch(intent)
        }
        binding.uploadSlider.setOnClickListener{
          if(imageUrl!=null){
              uploadImage(imageUrl!!)
          }else{
              Toast.makeText(requireContext(),"Please select image",Toast.LENGTH_LONG).show()
          }
        }
        return binding.root
    }

    private fun uploadImage(uri: Uri) {
       dialog.show()
        val fileName=UUID.randomUUID().toString()+".jpg"
        val refStorage=FirebaseStorage.getInstance().reference.child("slider/$fileName")
        refStorage.putFile(uri)
            .addOnSuccessListener {
                it.storage.downloadUrl.addOnSuccessListener{image ->
                    storageData(image.toString())

                }
            }
            .addOnFailureListener{
                dialog.dismiss()
                Toast.makeText(requireContext(),"Something went wrong with storage",Toast.LENGTH_LONG).show()
            }
    }

    private fun storageData(image: String) {
        val db=Firebase.firestore
        val data = hashMapOf<String,Any>(
            "img" to image
                )
        db.collection("slider").add(data)
            .addOnSuccessListener {
             dialog.dismiss()
                binding.imageSlider.setImageDrawable(resources.getDrawable(R.drawable.image_preview))
                imageUrl=null
                Toast.makeText(requireContext(),"Slider updated",Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener {
                dialog.dismiss()
                Toast.makeText(requireContext(),"Something went wrong",Toast.LENGTH_LONG).show()
            }
    }
}