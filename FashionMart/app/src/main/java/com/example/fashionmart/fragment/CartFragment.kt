package com.example.fashionmart.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.fashionmart.R
import com.example.fashionmart.activity.AddressActivity
import com.example.fashionmart.activity.ProductDetailsActivity
import com.example.fashionmart.adapter.CartAdapter
import com.example.fashionmart.adapter.CategoryAdapter
import com.example.fashionmart.databinding.FragmentCartBinding
import com.example.fashionmart.roomdb.AppDatabase
import com.example.fashionmart.roomdb.ProductModel


class CartFragment : Fragment() {
   private lateinit var binding: FragmentCartBinding
   private lateinit var list:ArrayList<String>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentCartBinding.inflate(layoutInflater)
        val preference=requireContext().getSharedPreferences("info", AppCompatActivity.MODE_PRIVATE)
        val editor=preference.edit()
        editor.putBoolean("isCart",false)
        editor.apply()
        val dao=AppDatabase.getInstance(requireContext()).productDao()
        list=ArrayList()
        dao.getAllProducts().observe(requireActivity()){
            binding.cartRecycler.adapter= CartAdapter(requireContext(),it)

            list.clear()
            for (data in it){
                list.add(data.productId)
            }
            totalCost(it)
        }
        return binding.root
    }

    private fun totalCost(data: List<ProductModel>?) {
      var total=70
        for(item in data!!){
            total+=item.productSp!!.toInt()
        }
        binding.itemNo.text=data.size.toString()
        binding.cost.text="₹"+total
        binding.checkOut.isEnabled = data.isNotEmpty()
        if(data.isEmpty()){
            binding.emptyText.visibility=View.VISIBLE
        }else{
            binding.emptyText.visibility=View.GONE
        }
        binding.checkOut.setOnClickListener{
            val intent= Intent(context, AddressActivity::class.java)
            val b=Bundle()
            b.putStringArrayList("productIds",list)
            b.putString("totalCost",total.toString())
            intent.putExtras(b)
            startActivity(intent)
        }
    }


}