package com.wyh.happyyousdk.dashboard.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.wyh.happyyousdk.R
import com.wyh.happyyousdk.databinding.DashboardChallengesListBinding
import com.wyh.happyyousdk.databinding.StampItemLayoutBinding
import com.wyh.happyyousdk.model.response.StampListData
import com.wyh.happyyousdk.utils.CommonUtils

class StampsAdapter(val context: Context,val list: ArrayList<StampListData>) : RecyclerView.Adapter<StampsAdapter.ViewHolder>() {

    lateinit var binding : StampItemLayoutBinding

    class ViewHolder(binding: StampItemLayoutBinding) : RecyclerView.ViewHolder(binding.root){
        var binding : StampItemLayoutBinding
        init {
            this.binding = binding
        }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StampsAdapter.ViewHolder {
        binding  = DataBindingUtil.inflate(LayoutInflater.from(context),
            R.layout.stamp_item_layout,parent,false)

        return ViewHolder(binding)


    }

    override fun onBindViewHolder(holder: StampsAdapter.ViewHolder, position: Int) {
        holder.binding.stampActivityName.text = list[position].activityName
        holder.binding.stampEarnedStamps.text = list[position].earnedStamps
        if(list[position].transactionDate != null){
            holder.binding.stampDate.text = CommonUtils.formatDateFromString("yyyy-MM-dd","dd/MM/yyyy",list[position].transactionDate.split("T")[0])
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}