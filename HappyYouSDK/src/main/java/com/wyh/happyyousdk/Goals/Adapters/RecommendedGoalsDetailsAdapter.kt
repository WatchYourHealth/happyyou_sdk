package com.wyh.happyyousdk.Goals.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.wyh.happyyousdk.Goals.ClickInterface.RecommendedGoals
import com.wyh.happyyousdk.Goals.Utils.Master
import com.wyh.happyyousdk.R
import com.wyh.happyyousdk.databinding.RecommendedGoalsLayoutBinding
import com.wyh.happyyousdk.model.response.RecomendedGoalsData

class RecommendedGoalsDetailsAdapter(val context : Context, val goalsList : ArrayList<RecomendedGoalsData>, val clickInterface : RecommendedGoals) : RecyclerView.Adapter<RecommendedGoalsDetailsAdapter.ViewHolder>(){

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


    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendedGoalsDetailsAdapter.ViewHolder {
        val inflater = LayoutInflater.from(context)
        binding = DataBindingUtil.inflate(inflater, R.layout.recommended_goals_layout,parent,false)


        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: RecommendedGoalsDetailsAdapter.ViewHolder, position: Int) {
        if (bgCount == res.size - 1) {
            bgCount = 0
        } else {
            bgCount++
        }
        binding.recomendedGoalDescTv.text = goalsList[position].goalText
        binding.recommendedParentLayout.background = ContextCompat.getDrawable(context,res[bgCount])

        binding.recommendedParentLayout.setOnClickListener {
            clickInterface.onClick(goalsList[position])
        }

        if(goalsList[position].goalName == Master.WATER_GOAL){
            binding.recomendedGoalImg
            binding.recomendedGoalImg.setImageDrawable(context.getDrawable(R.drawable.ic_water))
        }else if (goalsList[position].goalName == Master.SLEEP_GOAL){
            binding.recomendedGoalImg.setImageDrawable(context.getDrawable(R.drawable.ic_sleep_star))
        }else if (goalsList[position].goalName == Master.STEPS_GOAL){
            binding.recomendedGoalImg.setImageDrawable(context.getDrawable(R.drawable.ic_steps))
        }else if (goalsList[position].goalName == Master.MEDITATION){
            binding.recomendedGoalImg.setImageDrawable(context.getDrawable(R.drawable.ic_zen_zone))
        }else if (goalsList[position].goalName == Master.CALORIES_BURN){
            binding.recomendedGoalImg.setImageDrawable(context.getDrawable(R.drawable.ic_cal_burned))
        }else if (goalsList[position].goalName == Master.CALORIES_CONSUME){
            binding.recomendedGoalImg.setImageDrawable(context.getDrawable(R.drawable.ic_cal_burned))
        }else if (goalsList[position].goalName == Master.WEIGHT){
            binding.recomendedGoalImg.setImageDrawable(context.getDrawable(R.drawable.ic_ideal_weight))
        }else if (goalsList[position].goalName == Master.OTHERS){
            binding.recomendedGoalImg.setImageDrawable(context.getDrawable(R.drawable.ic_other_document))
        }

    }

    override fun getItemCount(): Int {
        return goalsList.size
    }
}