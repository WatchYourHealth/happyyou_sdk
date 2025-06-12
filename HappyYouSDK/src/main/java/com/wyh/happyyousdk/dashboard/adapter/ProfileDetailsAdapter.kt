package com.wyh.happyyousdk.dashboard.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wyh.happyyousdk.R

class ProfileDetailsAdapter(val context: Context) : RecyclerView.Adapter<ProfileDetailsAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileDetailsAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.user_details_layout,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProfileDetailsAdapter.ViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return 1
    }
}