package com.wyh.happyyousdk.ChallangesModule.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.wyh.happyyousdk.ChallangesModule.Activities.TribeChallengeDashboard
import com.wyh.happyyousdk.ChallangesModule.ClickInterface.ActivityClick
import com.wyh.happyyousdk.ChallangesModule.ClickInterface.InviteClick
import com.wyh.happyyousdk.model.response.ChallengeActivityResponse
import com.wyh.happyyousdk.R
import com.wyh.happyyousdk.SDKConstants
import com.wyh.happyyousdk.databinding.ActivityChallengeDetailBinding
import com.wyh.happyyousdk.utils.CommonUtils
import com.wyh.happyyousdk.utils.SharedPref

class ChallengeDetailsAdapter(val context: Context, val click: InviteClick, val activityClick: ActivityClick, val challengeDetailsResponse: ChallengeActivityResponse, val binding : ActivityChallengeDetailBinding ?=null) : RecyclerView.Adapter<ChallengeDetailsAdapter.ViewHolder>() {

    lateinit var challengeActivtyAdapter: ChallengeActivtyAdapter
    lateinit var bannerAdapter: BannerAdapter

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val challengee_activities_recycler = itemView.findViewById<RecyclerView>(R.id.challengee_activities_recycler)!!
        val banner_recycler = itemView.findViewById<RecyclerView>(R.id.banner_recycler)
        val inviteButton = itemView.findViewById<Button>(R.id.challenge_invite_btn)
        val challengeNameTv = itemView.findViewById<TextView>(R.id.challenge_name_tv)
        val challengeRankTv = itemView.findViewById<TextView>(R.id.challenge_rank_tv)
        val completeActivityTv = itemView.findViewById<TextView>(R.id.complete_activity_tv)
        val challengeParticipantTv = itemView.findViewById<TextView>(R.id.challenge_participant_tv)
        val bannerPrev = itemView.findViewById<ImageView>(R.id.banner_prev)
        val bannerNext = itemView.findViewById<ImageView>(R.id.banner_next)
        val badgeCount = itemView.findViewById<TextView>(R.id.badge_count_tv)
        val rlHeader = itemView.findViewById<RelativeLayout>(R.id.rlHeader)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChallengeDetailsAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.challenge_details_layout,parent,false)
        return ViewHolder(view)

    }

    @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: ChallengeDetailsAdapter.ViewHolder, position: Int) {
        setRecyclerView(holder)
        holder.challengeNameTv.text = challengeDetailsResponse.data.userStatus.challengeName
        Glide.with(context)
            .load(CommonUtils.getBaseUrlForAPI(context) + SDKConstants.endPointForImages + "ic_tree_cloud_bg.png")
            .into(object : CustomTarget<Drawable?>() {
                override fun onResourceReady(
                    resource: Drawable,
                    transition: Transition<in Drawable?>?
                ) {
                    holder.rlHeader.background = resource
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                }
            })
        holder.challengeParticipantTv.text = "Total Participants: ${challengeDetailsResponse.data.userStatus.totalJoined}"
        holder.challengeRankTv.text = "Rank: ${challengeDetailsResponse.data.userStatus.userRank}"
        holder.completeActivityTv.text = "Activity Completed: ${challengeDetailsResponse.data.userStatus.activityCompleted.toString()}/${challengeDetailsResponse.data.userStatus.totalChallengeActivity.toString()}"
        holder.badgeCount.text = "Badges: ${challengeDetailsResponse.data.userStatus.badgeCount}"
        holder.inviteButton.setOnClickListener {
            click.clickToInvite()
        }

        holder.bannerNext.setOnClickListener {
            holder.banner_recycler.smoothScrollToPosition(bannerAdapter.itemCount + 1)
        }

        holder.bannerPrev.setOnClickListener {
            holder.banner_recycler.smoothScrollToPosition(bannerAdapter.itemCount - 1)
        }
    }

    override fun getItemCount(): Int {
        return SharedPref.getChallengeCount()

    }

    fun setRecyclerView(holder: ChallengeDetailsAdapter.ViewHolder){

        holder.challengee_activities_recycler.layoutManager = GridLayoutManager(context,3)
        holder.challengee_activities_recycler.hasFixedSize()
        challengeActivtyAdapter = ChallengeActivtyAdapter(context,activityClick,challengeDetailsResponse.data.challengeActivityDetails,
            binding!!
        )
        holder.challengee_activities_recycler.adapter = challengeActivtyAdapter

        holder.banner_recycler.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
        holder.banner_recycler.hasFixedSize()
        bannerAdapter = BannerAdapter(context,challengeDetailsResponse.data.challengebanners)
        holder.banner_recycler.adapter = bannerAdapter
    }



}