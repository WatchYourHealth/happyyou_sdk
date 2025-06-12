package com.wyh.happyyousdk.happyMarket.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.wyh.happyyousdk.R
import com.wyh.happyyousdk.databinding.HappyMartOtherSectionLayoutBinding
import com.wyh.happyyousdk.model.response.HappyMartSubCategoryChildResponse

class HappyMartOtherSectionAdapter(val context: Context, val itemList: ArrayList<HappyMartSubCategoryChildResponse>, val click: itemClickEvent) : RecyclerView.Adapter<HappyMartOtherSectionAdapter.MyViewHolder>() {

    lateinit var res: IntArray
    var bgCount = -1


    class MyViewHolder(itemView: HappyMartOtherSectionLayoutBinding) : RecyclerView.ViewHolder(itemView.root) {
        val binding : HappyMartOtherSectionLayoutBinding = itemView

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HappyMartOtherSectionAdapter.MyViewHolder {
        res = intArrayOf(
            R.drawable.ic_light_pink_button_bg,
            R.drawable.ic_light_blue_button_bg,
            R.drawable.ic_light_orange_button_bg,
            R.drawable.ic_light_blue_button_bg,
            R.drawable.ic_light_orange_button_bg,
            R.drawable.ic_light_pink_button_bg,
            R.drawable.ic_light_orange_button_bg,
            R.drawable.ic_light_pink_button_bg,
            R.drawable.ic_light_blue_button_bg
        )

        val view = DataBindingUtil.inflate<HappyMartOtherSectionLayoutBinding>(LayoutInflater.from(context), R.layout.happy_mart_other_section_layout,parent,false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: HappyMartOtherSectionAdapter.MyViewHolder, position: Int) {
        if (bgCount == res.size - 1) {
            bgCount = 0
        } else {
            bgCount++
        }


        holder.binding.happyMartOtherSectionItemParent.setOnClickListener {
            click.click(itemList[position])
        }

        holder.binding.happyMartOtherSectionItemParent.background = ContextCompat.getDrawable(context, res[bgCount])
        holder.binding.happyMartOtherSectionItemTv.text = itemList[position].vendorName
        Glide.with(context)
            .load(itemList[position].vendorLogo)
            .into(holder.binding.happyMartOtherSectionItemImg)

    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}

interface itemClickEvent{

    fun click(subCategoryData:HappyMartSubCategoryChildResponse)
}