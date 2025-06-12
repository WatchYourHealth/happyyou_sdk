package com.wyh.happyyousdk.ChallangesModule.Walkathon.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.wyh.happyyousdk.R
import com.wyh.happyyousdk.databinding.BranchItemBinding
import com.wyh.happyyousdk.databinding.IndividualItemBinding
import com.wyh.happyyousdk.model.response.WinnerBranchStep
import com.wyh.happyyousdk.model.response.WinnerUserStep

class IndividualItemAdapter(val data: ArrayList<WinnerUserStep>): RecyclerView.Adapter<IndividualItemAdapter.ViewHolder>() {

    class ViewHolder(val binding: IndividualItemBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(): IndividualItemBinding = binding
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: IndividualItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.individual_item, parent, false
        )

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int  = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item: WinnerUserStep = data[position]
        holder.binding.name.text = item.username
        holder.binding.branchName.text = item.branchName
        holder.binding.avg.text = item.avgSteps.toString()
        holder.binding.total.text = item.totalSteps.toString()

        if(position == 0) {
            holder.binding.llMain.setBackgroundResource(R.drawable.rank_gold_bg)
        }

        if(position == 1){
            holder.binding.llMain.setBackgroundResource(R.drawable.rank_silver_bg)
        }

        if(position == 2){
            holder.binding.llMain.setBackgroundResource(R.drawable.rank_brown_bg)
        }

    }

}

