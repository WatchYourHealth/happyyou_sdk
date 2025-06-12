package com.wyh.happyyousdk.ChallangesModule.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.wyh.happyyousdk.ChallangesModule.Activities.BadgesActivity
import com.wyh.happyyousdk.ChallangesModule.ClickInterface.ActivityClick
import com.wyh.happyyousdk.model.response.ChallenegActivities
import com.wyh.happyyousdk.ChallangesModule.helperClass
import com.wyh.happyyousdk.R
import com.wyh.happyyousdk.databinding.ActivityChallengeDetailBinding
import com.wyh.happyyousdk.utils.SharedPref

class ChallengeActivtyAdapter(val context: Context, val click: ActivityClick, val activityList : ArrayList<ChallenegActivities>, val binding : ActivityChallengeDetailBinding) : RecyclerView.Adapter<ChallengeActivtyAdapter.ViewHolder>() {


    var res: IntArray
    var bgCount = -1
    lateinit var shareImage : String



    init {
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

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChallengeActivtyAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.challenge_activity_layout,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return activityList.size
    }

    override fun onBindViewHolder(holder: ChallengeActivtyAdapter.ViewHolder, position: Int) {
        if (bgCount == res.size - 1) {
            bgCount = 0
        } else {
            bgCount++
        }


        holder.challangeActivityPointsTv.text = activityList[position].progressPercentage.toString() + "%"
        holder.challange_activity_progress_bar.progress = activityList[position].progressPercentage.toInt()

        if(activityList[position].isCompleted){
            holder.challangeCompleteTv.visibility = View.VISIBLE
            holder.completeCheckImg.visibility = View.VISIBLE
            holder.challangeActivityPointsTv.text =  "100%"
            holder.challange_activity_progress_bar.progress = 100
        }else{
            holder.challangeCompleteTv.visibility = View.GONE
            holder.completeCheckImg.visibility = View.GONE
        }
        Glide.with(context).load(activityList[position].activityImage).into(holder.challangeActivityImage)
        holder.challange_activity_parent_layout.background = ContextCompat.getDrawable(context,res[bgCount])


        holder.challange_activity_name_tv.text = activityList[position].activityName
        holder.challange_activity_parent_layout.setOnClickListener {
            //click.onActivityClick(activityList[position],binding)
            if(!activityList[position].isCompleted){
                try{
                    if(activityList[position].shareContent != null){
                        shareImage = activityList[position].shareContent[0] ?: ""
                    }else{
                        shareImage = ""
                    }
                    helperClass.journalUploadDialog(context, activityList[position],SharedPref.getChallengeID(),shareImage,binding)
                }catch (e: Exception){
                    e.toString()
                }
            }else{
                if(activityList[position].activityType.equals("Share") || activityList[position].activityType.equals("tribe")){
                    try{
                        if(activityList[position].shareContent != null){
                            shareImage = activityList[position].shareContent[0] ?: ""
                        }else{
                            shareImage = ""
                        }
                        helperClass.journalUploadDialog(context, activityList[position],SharedPref.getChallengeID(),shareImage,binding)
                    }catch (e: Exception){
                        e.toString()
                    }
                }
            }
        }

        holder.completeCheckImg.setOnClickListener {
            context.startActivity(Intent(context,BadgesActivity::class.java))
        }


        holder.challengeActivityInfo.setOnClickListener {
            helperClass.journalUploadDialog(context,activityList[position],"","",binding)
        }

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val challange_activity_parent_layout : RelativeLayout = itemView.findViewById(R.id.challange_activity_parent_layout)
        val challange_activity_name_tv: TextView = itemView.findViewById(R.id.challange_activity_name_tv)
        val challange_activity_progress_bar: ProgressBar = itemView.findViewById(R.id.challange_activity_progress_bar)
        val challangeActivityPointsTv : TextView = itemView.findViewById(R.id.challange_activity_points_tv)
        val challangeCompleteTv : TextView = itemView.findViewById(R.id.challange_complete_tv)
        val completeCheckImg : ImageView = itemView.findViewById(R.id.complete_check_img)
        val challangeActivityImage : ImageView = itemView.findViewById(R.id.challange_activity_image)
        val challengeActivityInfo : ImageView = itemView.findViewById(R.id.challenge_activity_info)
    }
}