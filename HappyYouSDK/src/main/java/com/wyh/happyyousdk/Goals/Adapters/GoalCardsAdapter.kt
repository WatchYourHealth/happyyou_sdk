package com.wyh.happyyousdk.Goals.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.wyh.happyyousdk.R
import com.wyh.happyyousdk.model.response.GetGoalDayResponse
import com.wyh.happyyousdk.model.response.GoalDayData

class GoalCardsAdapter(val context: Context,val goalList : ArrayList<GoalDayData>) : RecyclerView.Adapter<GoalCardsAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val golaName = itemView.findViewById<TextView>(R.id.goalName)
        val goalDesc = itemView.findViewById<TextView>(R.id.goalDesc)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoalCardsAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.goal_cards_layout,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: GoalCardsAdapter.ViewHolder, position: Int) {
        holder.golaName.text = goalList[position].goalName
        holder.goalDesc.text = goalList[position].goalText
    }

    override fun getItemCount(): Int {
        return goalList.size
    }
}