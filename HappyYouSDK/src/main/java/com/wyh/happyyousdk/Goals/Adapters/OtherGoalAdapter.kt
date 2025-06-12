package com.wyh.happyyousdk.Goals.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.wyh.happyyousdk.Goals.Activities.OtherGoals
import com.wyh.happyyousdk.Goals.ClickInterface.OtherGoalClick
import com.wyh.happyyousdk.R
import com.wyh.happyyousdk.databinding.RecommendedGoalsLayoutBinding
import com.wyh.happyyousdk.model.response.OtherGoalsData

class OtherGoalAdapter(val context : Context,val list : ArrayList<OtherGoalsData>,val onclick : OtherGoalClick) : RecyclerView.Adapter<OtherGoalAdapter.ViewHolder>() {

    lateinit var binding : RecommendedGoalsLayoutBinding
    var res: IntArray = intArrayOf(
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
    var bgCount = -1
    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OtherGoalAdapter.ViewHolder {
        val inflater = LayoutInflater.from(context)
        binding = DataBindingUtil.inflate(inflater, R.layout.recommended_goals_layout,parent,false)
        return ViewHolder(binding.root)

    }

    override fun onBindViewHolder(holder: OtherGoalAdapter.ViewHolder, position: Int) {
        if (bgCount == res.size - 1) {
            bgCount = 0
        } else {
            bgCount++
        }

        binding.recommendedParentLayout.background = ContextCompat.getDrawable(context,res[bgCount])
        binding.recomendedGoalDescTv.text = list[position].goalDescription


        binding.recommendedParentLayout.setOnClickListener {
            onclick.otherGoalOnClick(list[position])
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}