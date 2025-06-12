package com.wyh.happyyousdk.ChallangesModule.Walkathon.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.wyh.happyyousdk.R
import com.wyh.happyyousdk.databinding.BranchItemBinding
import com.wyh.happyyousdk.model.response.WinnerBranchStep

class BranchItemAdapter(val data: ArrayList<WinnerBranchStep>): RecyclerView.Adapter<BranchItemAdapter.ViewHolder>() {

    class ViewHolder(val binding: BranchItemBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(): BranchItemBinding = binding
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: BranchItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.branch_item, parent, false
        )

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int  = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item: WinnerBranchStep = data[position]
        holder.binding.branchName.text = item.branchName
        holder.binding.branchP.text = item.branchRanking
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

