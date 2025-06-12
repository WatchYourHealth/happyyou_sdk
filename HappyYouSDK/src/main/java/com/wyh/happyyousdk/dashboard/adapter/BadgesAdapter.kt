package com.wyh.happyyousdk.dashboard.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.wyh.happyyousdk.R
import com.wyh.happyyousdk.databinding.BadgesItemLayoutBinding
import com.wyh.happyyousdk.model.response.badgeListData
import com.wyh.happyyousdk.utils.CommonUtils

class BadgesAdapter(val context: Context,val list: ArrayList<badgeListData>) : RecyclerView.Adapter<BadgesAdapter.ViewHolder>() {

    lateinit var binding: BadgesItemLayoutBinding

    class ViewHolder (binding: BadgesItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        var binding : BadgesItemLayoutBinding
        init{
            this.binding = binding
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BadgesAdapter.ViewHolder {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.badges_item_layout,parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BadgesAdapter.ViewHolder, position: Int) {
        holder.binding.badgeName.text = list[position].badgeName
        holder.binding.badgeDate.text = CommonUtils.formatDateFromString("yyyy-MM-dd","dd/MM/yyyy",list[position].transactionDate.split("T")[0])
        Glide.with(context)
            .load(list[position].badgeLogo)
            .into(holder.binding.badgeIcon)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}