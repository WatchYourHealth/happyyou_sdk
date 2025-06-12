package com.wyh.happyyousdk.ChallangesModule.Adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.wyh.happyyousdk.ChallangesModule.ClickInterface.BadgesClick
import com.wyh.happyyousdk.ChallangesModule.ClickInterface.TribeChallengeClick
import com.wyh.happyyousdk.model.response.RewardsBadgesData
import com.wyh.happyyousdk.R
import com.wyh.happyyousdk.databinding.TribeChallengeMemberItemBinding
import com.wyh.happyyousdk.model.request.challengeTribe.GetCommunityRankDetailsTribeUserDetail
import com.wyh.happyyousdk.model.request.challengeTribe.ViewMoreTribeResponseDataModel

class TribeViewMoreAdapter(
    val context: Context,
    val listener: TribeChallengeClick
) : RecyclerView.Adapter<TribeViewMoreAdapter.ViewHolder>() {

    private val dataList: ArrayList<ViewMoreTribeResponseDataModel> = ArrayList()

    fun submitList(list: List<ViewMoreTribeResponseDataModel>){
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
        val data: ViewMoreTribeResponseDataModel = dataList[position]
        holder.binding.name.text = data.communityName
        holder.binding.rank.text = data.communityRank
        holder.binding.steps.text = data.communitySteps
        if(data.isMember){
            holder.binding.ivNext.visibility = View.INVISIBLE
        }else{
            holder.binding.ivNext.visibility = View.INVISIBLE
        }

        holder.binding.rvItem.setOnClickListener {
            Log.d("communityId", "${data.communityId}")
            if(data.isMember){
                listener.onClick(data)
            }
        }
    }


}