package com.wyh.happyyousdk.ChallangesModule.Walkathon.Adapters

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.wyh.happyyousdk.utils.viewtooltip.ViewTooltip
import com.wyh.happyyousdk.utils.viewtooltip.ViewTooltip.*
import com.wyh.happyyousdk.ChallangesModule.Walkathon.Helper.Master
import com.wyh.happyyousdk.R
import com.wyh.happyyousdk.model.response.BranchRanking

class BranchRankListAdapter(val context: Context, val list: ArrayList<BranchRanking>) :
    RecyclerView.Adapter<BranchRankListAdapter.ViewHolder>() {


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val rank = itemView.findViewById<TextView>(R.id.rank_tv)
        val avgSteps = itemView.findViewById<TextView>(R.id.rank_branch)
        val name = itemView.findViewById<TextView>(R.id.rank_name)
        val participation = itemView.findViewById<TextView>(R.id.rank_total_steps)
        val info = itemView.findViewById<ImageView>(R.id.ivInfo)
        val infoLayout = itemView.findViewById<RelativeLayout>(R.id.infoLayout)
    }

    interface OnBranchClick {
        fun onClickInfo(data: BranchRanking)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.rank_list_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.rank.text = list[position].rank
        holder.name.text = list[position].branchName
        holder.avgSteps.text = list[position].avgSteps
        holder.participation.text = "${list[position].participation}%"

        holder.info.visibility = View.VISIBLE
        holder.infoLayout.setOnClickListener {
            Master.viewToolTip(
                context as Activity,
                holder.info,
                "Number of employees in branch: ${list[position].totalHeadCount}",
                Position.BOTTOM,
                ALIGN.END
            )
        }
    }

}