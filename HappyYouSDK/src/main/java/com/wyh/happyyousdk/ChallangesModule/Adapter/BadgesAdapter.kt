package com.wyh.happyyousdk.ChallangesModule.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.wyh.happyyousdk.ChallangesModule.ClickInterface.BadgesClick
import com.wyh.happyyousdk.model.response.RewardsBadgesData
import com.wyh.happyyousdk.R

class BadgesAdapter(val context: Context, val badgesList: ArrayList<RewardsBadgesData>, val click : BadgesClick) : RecyclerView.Adapter<BadgesAdapter.MyViewHolder>() {


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val challangenametv : TextView = itemView.findViewById(R.id.challange_name_tv)
        val challangeImage : ImageView = itemView.findViewById(R.id.challange_image)
        val challangeParentLayout : RelativeLayout = itemView.findViewById(R.id.challange_parent_layout)
        val joinNowLayout: LinearLayout = itemView.findViewById(R.id.join_now_layout)
        val infoIcon: ImageView = itemView.findViewById(R.id.info_icon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.challange_layout,parent,false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return badgesList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.challangenametv.text = badgesList[position].BadgeName
        Glide.with(context).load(badgesList[position].BadgeLogo).into(holder.challangeImage)
        holder.joinNowLayout.visibility = View.GONE
        holder.infoIcon.visibility = View.GONE
        if(badgesList[position].IsScratched.equals("0")){
            holder.challangeParentLayout.background = context.getDrawable(R.drawable.scratch_card_orange_new)
            holder.challangenametv.visibility = View.GONE
            holder.challangeImage.visibility = View.GONE
        }else{
            holder.challangeParentLayout.background = context.getDrawable(R.drawable.ic_light_blue_button_bg)
            holder.challangenametv.visibility = View.VISIBLE
            holder.challangeImage.visibility = View.VISIBLE

        }

        holder.challangeParentLayout.setOnClickListener {
            if(badgesList[position].IsScratched.equals("0")){
                click.clickToScracth(badgesList[position])
            }
        }
    }
}