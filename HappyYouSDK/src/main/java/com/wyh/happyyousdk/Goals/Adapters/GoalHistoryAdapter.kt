package com.wyh.happyyousdk.Goals.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.GridView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.wyh.happyyousdk.Goals.ClickInterface.HistorySeeAll
import com.wyh.happyyousdk.Goals.GridAdapter.GoalHistoryGridAdapter
import com.wyh.happyyousdk.Goals.GridAdapter.IndividualGridAdapter
import com.wyh.happyyousdk.R
import com.wyh.happyyousdk.model.response.GoalHistoryResponse
import com.wyh.happyyousdk.model.response.HistoryData
import com.wyh.happyyousdk.model.response.RegualrGoalsData

class GoalHistoryAdapter(val context: Context, val list: ArrayList<HistoryData>, val click : HistorySeeAll,val comingFrom : String) : RecyclerView.Adapter<GoalHistoryAdapter.ViewHolder>(){


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)  {
        val grid: GridView = itemView.findViewById(R.id.grid)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.upcoming_badges_grid_layout,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return 1
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val gridAdapter = GoalHistoryGridAdapter(context,list,click,comingFrom)

        holder.grid.adapter = gridAdapter
        holder.grid.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            Toast.makeText(context, " selected$position", Toast.LENGTH_SHORT).show()
        }
    }

    fun getSublist(list: List<RegualrGoalsData>, index: Int): List<RegualrGoalsData> {
        val start = index * 3
        val end = Math.min(start + 3, list.size) // Ensure end index does not exceed list size

        // Ensure start index is within bounds
        return if (start < 0 || start >= list.size) {
            ArrayList()
        } else list.subList(start, end)
    }


}

