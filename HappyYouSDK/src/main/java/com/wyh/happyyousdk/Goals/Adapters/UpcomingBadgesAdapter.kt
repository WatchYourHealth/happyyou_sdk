package com.wyh.happyyousdk.Goals.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.GridView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.wyh.happyyousdk.Goals.GridAdapter.UpcomingBadgesGridAdapter
import com.wyh.happyyousdk.R

class UpcomingBadgesAdapter(val context: Context) : RecyclerView.Adapter<UpcomingBadgesAdapter.ViewHolder>() {


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val grid: GridView = itemView.findViewById(R.id.grid)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UpcomingBadgesAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.upcoming_badges_grid_layout,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: UpcomingBadgesAdapter.ViewHolder, position: Int) {
        val gridAdapter = UpcomingBadgesGridAdapter(context)
        holder.grid.adapter = gridAdapter
        holder.grid.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            Toast.makeText(context, " selected$position", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int {
        return 3
    }
}