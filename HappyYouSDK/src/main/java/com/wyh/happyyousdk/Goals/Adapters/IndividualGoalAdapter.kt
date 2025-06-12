package com.wyh.happyyousdk.Goals.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.GridView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.wyh.happyyousdk.Goals.ClickInterface.DeleteGoal
import com.wyh.happyyousdk.Goals.ClickInterface.IndividualGoals
import com.wyh.happyyousdk.Goals.GridAdapter.IndividualGridAdapter
import com.wyh.happyyousdk.R
import com.wyh.happyyousdk.model.response.RegualrGoalsData

class IndividualGoalAdapter(val context: Context, val list : List<RegualrGoalsData>,val onClick : IndividualGoals, val deleteGoalCLick : DeleteGoal) : RecyclerView.Adapter<IndividualGoalAdapter.ViewHolder>() {


    var res: IntArray
    var bgCount = -1

    init {
        res = intArrayOf(
            R.drawable.ic_light_pink_button_bg,
            R.drawable.ic_light_blue_button_bg,
            R.drawable.ic_light_orange_button_bg,
            R.drawable.ic_light_blue_button_bg,
            R.drawable.ic_light_orange_button_bg,
            R.drawable.ic_light_pink_button_bg,
            R.drawable.ic_light_orange_button_bg,
            R.drawable.ic_light_pink_button_bg,
            R.drawable.ic_light_blue_button_bg
        )

    }
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val grid: GridView = itemView.findViewById(R.id.grid)

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IndividualGoalAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.upcoming_badges_grid_layout,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: IndividualGoalAdapter.ViewHolder, position: Int) {
        if (bgCount == res.size - 1) {
            bgCount = 0
        } else {
            bgCount++
        }
        val gridAdapter = IndividualGridAdapter(context,getSublist(list,position),onClick,deleteGoalCLick)
        holder.grid.adapter = gridAdapter
        holder.grid.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            Toast.makeText(context, " selected$position", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int {
        return 3
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