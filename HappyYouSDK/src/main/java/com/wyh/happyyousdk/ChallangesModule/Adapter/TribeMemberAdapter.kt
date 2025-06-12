package com.wyh.happyyousdk.ChallangesModule.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.wyh.happyyousdk.ChallangesModule.ClickInterface.BadgesClick
import com.wyh.happyyousdk.model.response.RewardsBadgesData
import com.wyh.happyyousdk.R
import com.wyh.happyyousdk.databinding.TribeChallengeMemberItemBinding
import com.wyh.happyyousdk.model.request.challengeTribe.GetCommunityRankDetailsTribeUserDetail

class TribeMemberAdapter(val context: Context, ) : RecyclerView.Adapter<TribeMemberAdapter.ViewHolder>() {

    private val dataList: ArrayList<GetCommunityRankDetailsTribeUserDetail> = ArrayList()

    fun submitList(list: List<GetCommunityRankDetailsTribeUserDetail>){
        dataList.clear()
        dataList.addAll(list)
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: TribeChallengeMemberItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = TribeChallengeMemberItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = dataList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data: GetCommunityRankDetailsTribeUserDetail = dataList[position]
        holder.binding.name.text = data.name
        holder.binding.rank.text = data.rank
        holder.binding.steps.text = data.totalSteps
    }


}