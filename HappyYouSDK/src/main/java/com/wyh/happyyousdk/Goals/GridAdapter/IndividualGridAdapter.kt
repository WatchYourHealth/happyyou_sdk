package com.wyh.happyyousdk.Goals.GridAdapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.wyh.happyyousdk.Goals.ClickInterface.DeleteGoal
import com.wyh.happyyousdk.Goals.ClickInterface.IndividualGoals
import com.wyh.happyyousdk.Goals.Utils.Master
import com.wyh.happyyousdk.R
import com.wyh.happyyousdk.model.response.RegualrGoalsData
import com.wyh.happyyousdk.utils.SharedPref

class IndividualGridAdapter(val context: Context, val list : List<RegualrGoalsData>,val onClick : IndividualGoals,val deleteCLick : DeleteGoal) : BaseAdapter() {

    var res: IntArray
    var bgCount = -1
    var backgrounSet = false;

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

    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(position: Int): Any {
        return ""
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    @SuppressLint("ViewHolder", "SetTextI18n")
    override fun getView(position: Int, p1: View?, p2: ViewGroup?): View {
        val view = LayoutInflater.from(context).inflate(R.layout.goals_individual_layout,null)
        val goalParentLayout = view.findViewById<RelativeLayout>(R.id.goal_individual_parent_layout)
        val deletGoal = view.findViewById<ImageView>(R.id.deleteGoal)
        val goalDesc = view.findViewById<TextView>(R.id.individual_goal_desc)
        val imageView = view.findViewById<ImageView>(R.id.individual_goal_img)

        if (bgCount == res.size - 1) {
            bgCount = 0
        } else {
            bgCount++
        }
        goalParentLayout.background = ContextCompat.getDrawable(context,res[bgCount])
        var daysOrDay = "day"
        if (list[position].noOfDays > 1) {
            daysOrDay = "days"
        }

        if (list[position].goalName.equals(Master.WATER_GOAL, ignoreCase = true)) {
            goalDesc.text = "Drink" + " " + list[position].goalTarget + " " + "glasses" + " in " + list[position].noOfDays + " " + daysOrDay
            imageView.setImageDrawable(context.getDrawable(R.drawable.ic_water))

        } else if (list[position].goalName.equals(Master.SLEEP_GOAL, ignoreCase = true)) {
            goalDesc.text = "Sleep" + " " + list[position].goalTarget + " " + "Hrs" + " in " + list[position].noOfDays + " " + daysOrDay
            imageView.setImageDrawable(context.getDrawable(R.drawable.ic_sleep_star))

        } else if (list[position].goalName.equals(Master.STEPS_GOAL, ignoreCase = true)) {
            goalDesc.text = "Walk" + " " + list[position].goalTarget + " " + list[position].goalName + " in " + list[position].noOfDays + " " + daysOrDay
            imageView.setImageDrawable(context.getDrawable(R.drawable.ic_steps))

        } else if (list[position].goalName.equals(Master.MEDITATION, ignoreCase = true)) {
            goalDesc.text = "Meditate" + " " + list[position].goalTarget + " " + "Hrs" + " in " + list[position].noOfDays + " " + daysOrDay
            imageView.setImageDrawable(context.getDrawable(R.drawable.ic_zen_zone))

        } else if (list[position].goalName.equals(Master.CALORIES_BURN, ignoreCase = true)) {
            goalDesc.text = "Burn" + " " + list[position].goalTarget + " " + "cal" + " in " + list[position].noOfDays + " " + daysOrDay
            imageView.setImageDrawable(context.getDrawable(R.drawable.ic_cal_burned))

        } else if (list[position].goalName.equals(Master.CALORIES_CONSUME, ignoreCase = true)) {
            goalDesc.text = "Consume" + " " + list[position].goalTarget + " Calories " + " in " + list[position].noOfDays + " " + daysOrDay
            imageView.setImageDrawable(context.getDrawable(R.drawable.ic_cal_burned))

        } else if (list[position].goalName.equals(Master.WEIGHT, ignoreCase = true)) {
            var currentWeight = 0.0
            currentWeight = if (SharedPref.getWeight().equals("", ignoreCase = true)) {
                0.0
            } else {
                java.lang.Double.valueOf(SharedPref.getWeight())
            }
            val weightToLoseOrGain = currentWeight - list[position].goalTarget.toInt()
            var desc = ""
            desc = if (weightToLoseOrGain < 0) {
                "Gain" + " " + -weightToLoseOrGain.toInt() + " " + "kg" + " in " + list[position].noOfDays + " " + daysOrDay
            } else if (weightToLoseOrGain > 0) {
                "Lose" + " " + weightToLoseOrGain + " " + "kg" + " in " + list[position].noOfDays + " " + daysOrDay
            } else {
                "Maintain" + " " + list[position].goalTarget + " " + "kg" + " for " + list[position].noOfDays + " " + daysOrDay
            }
            goalDesc.text = desc
            imageView.setImageDrawable(context.getDrawable(R.drawable.ic_ideal_weight))

        } else if (list[position].goalName.equals(Master.OTHERS, ignoreCase = true)) {
            goalDesc.text = list[position].goalDescription
            imageView.setImageDrawable(context.getDrawable(R.drawable.ic_other_document))
        }

        goalParentLayout.setOnClickListener {
           onClick.goalRedirection(list[position].goalName,list[position].goalDescription)
        }

        deletGoal.setOnClickListener {
            deleteCLick.deleteGoalClick(list[position].goalId)
        }

        if(list[position].goalName == Master.WATER_GOAL){
            imageView.setImageDrawable(context.getDrawable(R.drawable.ic_water))
        }else if (list[position].goalName == Master.SLEEP_GOAL){
            imageView.setImageDrawable(context.getDrawable(R.drawable.ic_sleep_star))
        }else if (list[position].goalName == Master.STEPS_GOAL){
            imageView.setImageDrawable(context.getDrawable(R.drawable.ic_steps))
        }else if (list[position].goalName == Master.MEDITATION){
            imageView.setImageDrawable(context.getDrawable(R.drawable.ic_zen_zone))
        }else if (list[position].goalName == Master.CALORIES_BURN){
            imageView.setImageDrawable(context.getDrawable(R.drawable.ic_cal_burned))
        }else if (list[position].goalName == Master.CALORIES_CONSUME){
            imageView.setImageDrawable(context.getDrawable(R.drawable.ic_cal_burned))
        }else if (list[position].goalName == Master.WEIGHT){
            imageView.setImageDrawable(context.getDrawable(R.drawable.ic_ideal_weight))
        }else if (list[position].goalName == Master.OTHERS){
            imageView.setImageDrawable(context.getDrawable(R.drawable.ic_other_document))
        }


        return view
    }

}