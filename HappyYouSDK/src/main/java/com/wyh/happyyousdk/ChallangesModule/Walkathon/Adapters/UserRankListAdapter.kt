package com.wyh.happyyousdk.ChallangesModule.Walkathon.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.wyh.happyyousdk.R
import com.wyh.happyyousdk.model.response.UserRanking

class UserRankListAdapter(val context: Context, val list: ArrayList<UserRanking>) :
    RecyclerView.Adapter<UserRankListAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val rank = itemView.findViewById<TextView>(R.id.rank_tv)
        val rankName = itemView.findViewById<TextView>(R.id.rank_name)
        val branch = itemView.findViewById<TextView>(R.id.rank_branch)
        val totalSteps = itemView.findViewById<TextView>(R.id.rank_total_steps)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.rank_list_layout,parent,false)
        return ViewHolder(view)

    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.rank.text = list[position].rank
        holder.rankName.text = list[position].userName
        holder.branch.text = list[position].branchName
        holder.totalSteps.text = list[position].steps
    }
}