package com.wyh.happyyousdk.happyMarket.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.wyh.happyyousdk.R
import com.wyh.happyyousdk.model.response.CategoryData
import com.wyh.happyyousdk.model.response.HappyMartSubCategoryResponse

class CategoryAdapter(val context: Context, val categoryList : ArrayList<CategoryData>, val clikcEvent : onClickCategory) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {


    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val categoryImage = itemView.findViewById<ImageView>(R.id.iv_partner)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.happy_mart_partner_item,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryAdapter.ViewHolder, position: Int) {
        Glide.with(context)
            .load(categoryList[position].happyMartSubCategoryResponse.vendorLogo)
            .into(holder.categoryImage)

        holder.categoryImage.setOnClickListener {
            clikcEvent.onClick(categoryList[position].happyMartSubCategoryResponse)
        }
    }

    override fun getItemCount(): Int {
        return  categoryList.size
    }
}

interface onClickCategory {
    fun onClick(response: HappyMartSubCategoryResponse)
}