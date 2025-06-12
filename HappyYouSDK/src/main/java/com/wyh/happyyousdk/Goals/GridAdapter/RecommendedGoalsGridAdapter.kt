package com.wyh.happyyousdk.Goals.GridAdapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.wyh.happyyousdk.Goals.ClickInterface.RecommendedGoals
import com.wyh.happyyousdk.Goals.Utils.Master
import com.wyh.happyyousdk.R
import com.wyh.happyyousdk.databinding.RecommendedGoalsLayoutBinding
import com.wyh.happyyousdk.model.response.RecomendedGoalsData
import com.wyh.happyyousdk.trends.TrendsActivity
import com.wyh.happyyousdk.utils.Constants

class RecommendedGoalsGridAdapter(val context: Context,val recomendedGoalsList: List<RecomendedGoalsData>
,val clickInterface : RecommendedGoals) : BaseAdapter() {


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
    lateinit var binding : RecommendedGoalsLayoutBinding

    override fun getCount(): Int {
        return recomendedGoalsList.size
    }

    override fun getItem(p0: Int): Any {
        return ""
    }

    override fun getItemId(p0: Int): Long {
        return 0
    }

    @SuppressLint("ViewHolder", "UseCompatLoadingForDrawables")
    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {

        val inflater = LayoutInflater.from(context)
        binding = DataBindingUtil.inflate(inflater,R.layout.recommended_goals_layout,p2,false)


        if (bgCount == res.size - 1) {
            bgCount = 0
        } else {
            bgCount++
        }

        binding.recommendedParentLayout.setOnClickListener {
            when (recomendedGoalsList[p0].goalName) {
                "Water" -> context.startActivity(
                    Intent(context, TrendsActivity::class.java)
                        .putExtra("activityType", Constants.WATER))
                "Sleep" -> context.startActivity(
                    Intent(context, TrendsActivity::class.java)
                        .putExtra("activityType", Constants.SLEEP))
                "Steps" -> context.startActivity(
                    Intent(context, TrendsActivity::class.java)
                        .putExtra("activityType", Constants.STEPS))

                "Calories Burn","Calories \\nConsume" -> context.startActivity(
                    Intent(context, TrendsActivity::class.java)
                        .putExtra("activityType", Constants.CALORIE))

                "Weight" -> context.startActivity(
                    Intent(context, TrendsActivity::class.java)
                        .putExtra("activityType", Constants.WEIGHT))

                "Meditation" -> context.startActivity(
                    Intent(context, TrendsActivity::class.java)
                        .putExtra("activityType", Constants.MEDITATION))

            }

        }

        binding.recommendedParentLayout.background = context.getDrawable(R.drawable.ic_light_pink_button_bg)
        binding.moreGoals.visibility = View.GONE
        binding.goalsMain.visibility = View.VISIBLE
        binding.recommendedParentLayout.background = ContextCompat.getDrawable(context,res[bgCount])
        binding.recomendedGoalDescTv.text = recomendedGoalsList[p0].goalText


        if(recomendedGoalsList[p0].goalName == Master.WATER_GOAL){
            binding.recomendedGoalImg
            binding.recomendedGoalImg.setImageDrawable(context.getDrawable(R.drawable.ic_water))
        }else if (recomendedGoalsList[p0].goalName == Master.SLEEP_GOAL){
            binding.recomendedGoalImg.setImageDrawable(context.getDrawable(R.drawable.ic_sleep_star))
        }else if (recomendedGoalsList[p0].goalName == Master.STEPS_GOAL){
            binding.recomendedGoalImg.setImageDrawable(context.getDrawable(R.drawable.ic_steps))
        }else if (recomendedGoalsList[p0].goalName == Master.MEDITATION){
            binding.recomendedGoalImg.setImageDrawable(context.getDrawable(R.drawable.ic_zen_zone))
        }else if (recomendedGoalsList[p0].goalName == Master.CALORIES_BURN){
            binding.recomendedGoalImg.setImageDrawable(context.getDrawable(R.drawable.ic_cal_burned))
        }else if (recomendedGoalsList[p0].goalName == Master.CALORIES_CONSUME){
            binding.recomendedGoalImg.setImageDrawable(context.getDrawable(R.drawable.ic_cal_burned))
        }else if (recomendedGoalsList[p0].goalName == Master.WEIGHT){
            binding.recomendedGoalImg.setImageDrawable(context.getDrawable(R.drawable.ic_ideal_weight))
        }else if (recomendedGoalsList[p0].goalName == Master.OTHERS){
            binding.recomendedGoalImg.setImageDrawable(context.getDrawable(R.drawable.ic_other_document))
        }


        return binding.root

    }
}