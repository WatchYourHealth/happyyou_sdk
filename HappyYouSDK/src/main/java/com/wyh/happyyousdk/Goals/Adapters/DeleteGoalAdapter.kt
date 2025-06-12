package com.wyh.happyyousdk.Goals.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.wyh.happyyousdk.Goals.ClickInterface.HistorySeeAll
import com.wyh.happyyousdk.Goals.GridAdapter.DeleteHIstoryGridAdapter
import com.wyh.happyyousdk.Goals.GridAdapter.GoalHistoryGridAdapter
import com.wyh.happyyousdk.R
import com.wyh.happyyousdk.model.response.HistoryData

class DeleteGoalAdapter(val context: Context, val list : ArrayList<HistoryData>,val onClick : HistorySeeAll,val comingFrom: String) : RecyclerView.Adapter<DeleteGoalAdapter.ViewHolder>() {


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val grid: GridView = itemView.findViewById(R.id.grid)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeleteGoalAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.upcoming_badges_grid_layout,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: DeleteGoalAdapter.ViewHolder, position: Int) {
        val gridAdapter = DeleteHIstoryGridAdapter(context,list,onClick,comingFrom)

        holder.grid.adapter = gridAdapter
        holder.grid.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            Toast.makeText(context, " selected$position", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int {
        return 1
    }
}