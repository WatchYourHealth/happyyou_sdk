package com.wyh.happyyousdk.dashboard.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.wyh.happyyousdk.R
import com.wyh.happyyousdk.dashboard.KYWClick

class KnowYourWellBeingAdapter(val context: Context, val list : ArrayList<String>,val onClick : KYWClick) : RecyclerView.Adapter<KnowYourWellBeingAdapter.ViewHolder>() {


    @SuppressLint("UseCompatLoadingForDrawables")
    val drawable = arrayListOf<Drawable>(context.resources.getDrawable(R.drawable.kwn_list_bg),
        context.resources.getDrawable(R.drawable.kwn_pink_bg),
        context.resources.getDrawable(R.drawable.kwn_blue_bg),
        context.resources.getDrawable(R.drawable.kwn_pink_bg),
        context.resources.getDrawable(R.drawable.kwn_blue_bg),
        context.resources.getDrawable(R.drawable.kwn_list_bg))

    var count = 1
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tiltle = itemView.findViewById<TextView>(R.id.wellbeing_title_tv)
        val tvAssessments = itemView.findViewById<TextView>(R.id.tvAssessments)
        val animation = itemView.findViewById<LottieAnimationView>(R.id.lottie_file)
        val parentLayout = itemView.findViewById<RelativeLayout>(R.id.kyw_parent_layout)
        val bearImg = itemView.findViewById<ImageView>(R.id.bear_img)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KnowYourWellBeingAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.know_your_wellbeing_layout,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: KnowYourWellBeingAdapter.ViewHolder, position: Int) {

            holder.bearImg.visibility = View.GONE
            holder.parentLayout.visibility = View.VISIBLE
            holder.tiltle.text = list[position]

            if(drawable.size > position){
                holder.parentLayout.background = drawable[position]
            }

            if(list[position].equals("Health Score",true)){
                holder.animation.setAnimation(R.raw.heartbeat)
                holder.tvAssessments.visibility = View.GONE
            }else if(list[position].equals("Face Scan",true)){
                holder.animation.setAnimation(R.raw.face_scan)
                holder.tvAssessments.visibility = View.GONE
            }else if(list[position].equals("View All",true)){
                holder.animation.setAnimation(R.raw.virtual_assistant)
                holder.tvAssessments.visibility = View.VISIBLE
            } else if(list[position].equals("DASS Score",true)){
                holder.animation.setAnimation(R.raw.dass)
                holder.tvAssessments.visibility = View.GONE
            }else if (list[position].equals("Mental Wellness",true)){
                holder.animation.setAnimation(R.raw.mental_wellness)
                holder.tvAssessments.visibility = View.GONE
            }else if(list[position].equals("Heart Age",true)){
                holder.tvAssessments.visibility = View.GONE
                holder.animation.setAnimation(R.raw.heartage)
            }else if(list[position].equals("Respiratory Health",true)){
                holder.tvAssessments.visibility = View.GONE
                holder.animation.setAnimation(R.raw.respiratory)
            }else if(list[position].equals("Immunity Risk Assessment",true)){
                holder.tvAssessments.visibility = View.GONE
                holder.animation.setAnimation(R.raw.immunity)
            }

            holder.animation.playAnimation()
            holder.parentLayout.setOnClickListener {
                onClick.kywonClick(list[position])

        }

    }

    override fun getItemCount(): Int {
        return list.size
    }
}