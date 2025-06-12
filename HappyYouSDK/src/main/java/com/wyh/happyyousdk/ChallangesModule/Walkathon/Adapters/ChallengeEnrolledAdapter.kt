package com.wyh.happyyousdk.ChallangesModule.Walkathon.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wyh.happyyousdk.R
import com.wyh.happyyousdk.model.request.BranchData
import com.wyh.happyyousdk.model.request.EnrolledHistoryResponse

class ChallengeEnrolledAdapter (val context: Context, val list : ArrayList<BranchData>) : RecyclerView.Adapter<ChallengeEnrolledAdapter.MyViewHolder>() {


    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

        val branchID = itemView.findViewById<TextView>(R.id.list_branch_id)
        val branchName = itemView.findViewById<TextView>(R.id.list_branch_name)
        val date = itemView.findViewById<TextView>(R.id.list_enroll_date)
        val address = itemView.findViewById<TextView>(R.id.list_enroll_address)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChallengeEnrolledAdapter.MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.challenge_enrolled_layout,parent,false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChallengeEnrolledAdapter.MyViewHolder, position: Int) {
        holder.branchID.text = list[position].branchCode
        holder.branchName.text = list[position].branchName
        holder.date.text = list[position].scannedDate
        holder.address.text = list[position].participationCode
    }

    override fun getItemCount(): Int {
        return list.size
    }
}