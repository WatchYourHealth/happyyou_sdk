package com.wyh.happyyousdk.Sonde.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.wyh.happyyousdk.R
import com.wyh.happyyousdk.Sonde.Activities.SondeDashboard
import com.wyh.happyyousdk.Sonde.Interface.ItemClick

class SondeDashboardAdapter(val context: Context, val list : Array<String>, val click : ItemClick) : RecyclerView.Adapter<SondeDashboardAdapter.ViewHolder>() {

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val textView = itemView.findViewById<TextView>(R.id.sonde_dashboard_item_tv)
        val imageView = itemView.findViewById<ImageView>(R.id.sonde_dashboard_item_img)
        val parentLayout = itemView.findViewById<RelativeLayout>(R.id.sonde_dashboard_item_header)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SondeDashboardAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.sonde_dashboard_item_layout,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: SondeDashboardAdapter.ViewHolder, position: Int) {
        holder.textView.text = list[position]
        holder.parentLayout.setOnClickListener {
            click.onDashBoardItemClick(list[position])
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}