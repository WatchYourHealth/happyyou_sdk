package com.wyh.happyyousdk.Goals.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.GridView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.wyh.happyyousdk.Goals.ClickInterface.RecommendedGoals
import com.wyh.happyyousdk.Goals.GridAdapter.RecommendedGoalsGridAdapter
import com.wyh.happyyousdk.R
import com.wyh.happyyousdk.dashboard.model.DashboardBottomCardDataResponse.tilesData
import com.wyh.happyyousdk.model.response.RecomendedGoalsData
import kotlin.math.ceil
import kotlin.math.min


class RecommendedGoalsAdapter(val context: Context, val recomendedGoalsList: ArrayList<RecomendedGoalsData>,
val clickInterface : RecommendedGoals) : RecyclerView.Adapter<RecommendedGoalsAdapter.ViewHolder>() {


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val grid: GridView = itemView.findViewById(R.id.grid)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendedGoalsAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.upcoming_badges_grid_layout,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecommendedGoalsAdapter.ViewHolder, position: Int) {
        val gridAdapter = RecommendedGoalsGridAdapter(context,getSublist(recomendedGoalsList,position),clickInterface)
        holder.grid.adapter = gridAdapter
        holder.grid.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            Toast.makeText(context, " selected$position", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int {
        return if (recomendedGoalsList.size > 3) {
            ceil(recomendedGoalsList.size.toString().toDouble() / 3.0).toInt()
        } else {
            1
        }
    }

    fun getSublist(list: List<RecomendedGoalsData?>, index: Int): List<RecomendedGoalsData> {
        val start = index * 3
        val end = min(start + 3, list.size) // Ensure end index does not exceed list size

        // Ensure start index is within bounds
        return if (start < 0 || start >= list.size) {
            ArrayList()
        } else list.subList(start, end) as List<RecomendedGoalsData>
    }
}