package com.example.fashionmart.fragment

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.fashionmart.R
import com.example.fashionmart.adapter.CategoryAdapter
import com.example.fashionmart.adapter.ProductAdapter
import com.example.fashionmart.databinding.FragmentHomeBinding
import com.example.fashionmart.modal.AddProductModel
import com.example.fashionmart.modal.CategoryModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class HomeFragment : Fragment() {

    private lateinit var binding:FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentHomeBinding.inflate(layoutInflater)
        val preference=requireContext().getSharedPreferences("info", AppCompatActivity.MODE_PRIVATE)
         if(preference.getBoolean("isCart",false))
             findNavController().navigate(R.id.action_homeFragment_to_cartFragment)
        getCategories()
        getSliderImage()
        getProducts()
        return binding.root
    }

    private fun getSliderImage() {
        val list= ArrayList<String>()
        Firebase.firestore.collection("slider").get().addOnSuccessListener {
            list.clear()
            for(doc in it.documents){
                val data=doc.getString("img")
                list.add(data!!)
            }
            val slideList=ArrayList<SlideModel>()
            for(data in list){
                slideList.add(SlideModel(data, ScaleTypes.CENTER_CROP))
            }
            binding.imageSlider.setImageList(slideList)

//            Glide.with(requireContext()).load(it.get("img")).into(binding.sliderImage)
        }
    }

    private fun getProducts() {
        val list= ArrayList<AddProductModel>()
        Firebase.firestore.collection("products")
            .get().addOnSuccessListener {
                list.clear()
                for(doc in it.documents){
                    val data=doc.toObject(AddProductModel::class.java)
                    list.add(data!!)
                }
                val spanCount = 2

                val spacing = 30

                val includeEdge = true

                binding.recyclerProduct.layoutManager=GridLayoutManager(requireContext(),2)
                binding.recyclerProduct.addItemDecoration(
                    GridSpacingItemDecoration(
                        spanCount,
                        spacing,
                        includeEdge
                    )
                )
                binding.recyclerProduct.adapter= ProductAdapter(requireContext(),list)
            }
    }

    private fun getCategories() {
        val list= ArrayList<CategoryModel>()
        Firebase.firestore.collection("categories")
            .get().addOnSuccessListener {
                list.clear()
                for(doc in it.documents){
                    val data=doc.toObject(CategoryModel::class.java)
                    list.add(data!!)
                }
                binding.recyclerCategory.layoutManager=GridLayoutManager(requireContext(),4)
                binding.recyclerCategory.adapter= CategoryAdapter(requireContext(),list)
            }
    }

}
class GridSpacingItemDecoration(
    private val spanCount: Int,
    private val spacing: Int,
    private val includeEdge: Boolean
) :
    ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view) // item position
        val column = position % spanCount // item column
        if (includeEdge) {
            outRect.left =
                spacing - column * spacing / spanCount // spacing - column * ((1f / spanCount) * spacing)
            outRect.right =
                (column + 1) * spacing / spanCount // (column + 1) * ((1f / spanCount) * spacing)
            if (position < spanCount) { // top edge
                outRect.top = spacing
            }
            outRect.bottom = spacing // item bottom
        } else {
            outRect.left = column * spacing / spanCount // column * ((1f / spanCount) * spacing)
            outRect.right =
                spacing - (column + 1) * spacing / spanCount // spacing - (column + 1) * ((1f /    spanCount) * spacing)
            if (position >= spanCount) {
                outRect.top = spacing // item top
            }
        }
    }
}