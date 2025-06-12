package com.wyh.happyyousdk.ChallangesModule.Walkathon.Adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.wyh.happyyousdk.R
import com.wyh.happyyousdk.model.response.BranchUserData
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class BranchUserAdapter(val context: Context, val list : ArrayList<BranchUserData>) : RecyclerView.Adapter<BranchUserAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val userBranchName = itemView.findViewById<TextView>(R.id.user_branch_name)
        val userEnrollDate = itemView.findViewById<TextView>(R.id.user_enroll_date)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.branch_user_layout,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.userBranchName.text = list[position].name
        holder.userEnrollDate.text = list[position].enrollDate.replace("T", " ")
    }

    @SuppressLint("SimpleDateFormat")
    fun convertToNewFormat(dateStr: String?): String? {
        val utc: TimeZone = TimeZone.getTimeZone("UTC")
        val sourceFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        val destFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        sourceFormat.timeZone = utc
        val convertedDate: Date = dateStr?.let { sourceFormat.parse(it) } as Date
        return destFormat.format(convertedDate)
    }
}