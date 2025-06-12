package com.wyh.happyyousdk.Goals.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.wyh.happyyousdk.Goals.ClickInterface.IndividualGoals
import com.wyh.happyyousdk.Goals.Model.IndividualGoalsModel
import com.wyh.happyyousdk.R

class IndividualGoalsAdapter(val context : Context, val goalsList : ArrayList<IndividualGoalsModel>
, val clickEvent : IndividualGoals) : RecyclerView.Adapter<IndividualGoalsAdapter.ViewHolder>() {

    var res: IntArray = intArrayOf(
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
    var bgCount = -1


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val individualGoalsImage = itemView.findViewById<ImageView>(R.id.individual_goals_image)
        val individualGoalsNameTv = itemView.findViewById<TextView>(R.id.individual_goals_name_tv)
        val parentLayout = itemView.findViewById<RelativeLayout>(R.id.individual_goals_parent_layout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.individual_goal_layout,parent,false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return goalsList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (bgCount == res.size - 1) {
            bgCount = 0
        } else {
            bgCount++
        }
        holder.individualGoalsNameTv.text = goalsList[position].goalsName
        holder.parentLayout.background = ContextCompat.getDrawable(context,res[bgCount])

        holder.parentLayout.setOnClickListener {
            clickEvent.menuClick(goalsList[position].goalsName)
        }
        Glide.with(context).load(goalsList[position].image).into(holder.individualGoalsImage)
    }
}