package com.wyh.happyyousdk.Goals.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.wyh.happyyousdk.Goals.ClickInterface.DeleteGoal
import com.wyh.happyyousdk.Goals.Utils.Master.CALORIES_BURN
import com.wyh.happyyousdk.Goals.Utils.Master.CALORIES_CONSUME
import com.wyh.happyyousdk.Goals.Utils.Master.MEDITATION
import com.wyh.happyyousdk.Goals.Utils.Master.OTHERS
import com.wyh.happyyousdk.Goals.Utils.Master.SLEEP_GOAL
import com.wyh.happyyousdk.Goals.Utils.Master.STEPS_GOAL
import com.wyh.happyyousdk.Goals.Utils.Master.WATER_GOAL
import com.wyh.happyyousdk.Goals.Utils.Master.WEIGHT
import com.wyh.happyyousdk.R
import com.wyh.happyyousdk.diary.NewDiaryActivity
import com.wyh.happyyousdk.model.response.RegualrGoalsData
import com.wyh.happyyousdk.rewards.RewardsActivity
import com.wyh.happyyousdk.trends.TrendsActivity
import com.wyh.happyyousdk.utils.Constants
import com.wyh.happyyousdk.utils.SharedPref

class IndividualGoalDetailsAdapter(val context: Context,val list : ArrayList<RegualrGoalsData>, val deleteGoal: DeleteGoal) : RecyclerView.Adapter<IndividualGoalDetailsAdapter.ViewHolder>() {

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

    var daysOrDay = "day"
    var glasseOrglasses = "glass"
    var hrOrHrs = "hr"
    var stepOrSteps = "step"
    var calOrCals = "cal"


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val individualParentLayout = itemView.findViewById<RelativeLayout>(R.id.recommended_parent_layout)
        val goalDesc = itemView.findViewById<TextView>(R.id.recomended_goal_desc_tv)
        val goalImg = itemView.findViewById<ImageView>(R.id.recomended_goal_img)
        val delete = itemView.findViewById<ImageView>(R.id.deleteGoal1)
        val progressLayout = itemView.findViewById<RelativeLayout>(R.id.progress_layout)
        val progressBar = itemView.findViewById<ProgressBar>(R.id.goal_progress_bar)
        val progressTv = itemView.findViewById<TextView>(R.id.goal_points_tv)



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IndividualGoalDetailsAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.recommended_goals_layout,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: IndividualGoalDetailsAdapter.ViewHolder, position: Int) {
        if (bgCount == res.size - 1) {
            bgCount = 0
        } else {
            bgCount++
        }
        holder.goalDesc.text = list[position].goalDescription
        holder.delete.visibility = View.VISIBLE
        holder.individualParentLayout.background = ContextCompat.getDrawable(context,res[bgCount])


        daysOrDay = if (list[position].noOfDays > 1) {
            "days"
        } else {
            "day"
        }
        if (list[position].goalTarget > 1) {
            glasseOrglasses = "glasses"
            hrOrHrs = "hrs"
            stepOrSteps = "steps"
            calOrCals = "cals"
        } else {
            glasseOrglasses = "glass"
            hrOrHrs = "hr"
            stepOrSteps = "step"
            calOrCals = "cal"
        }


        holder.progressLayout.visibility = View.VISIBLE
        holder.progressBar.progress = list[position].progressPercentage.toInt()
        holder.progressTv.text = list[position].progressPercentage.toString()

        if (list[position].goalName.equals(WATER_GOAL, ignoreCase = true)) {
            holder.goalDesc.setText("Drink" + " " + list[position].goalTarget + " " + glasseOrglasses + " in " + list[position].noOfDays + " " + daysOrDay)
            holder.goalImg.setImageDrawable(context.getDrawable(R.drawable.ic_water))
        } else if (list[position].goalName.equals(SLEEP_GOAL, ignoreCase = true)) {
            holder.goalDesc.setText("Sleep" + " " + list[position].goalTarget + " " + hrOrHrs + " in " + list[position].noOfDays + " " + daysOrDay)
            holder.goalImg.setImageDrawable(context.getDrawable(R.drawable.ic_sleep_star))
        } else if (list[position].goalName.equals(STEPS_GOAL, ignoreCase = true)) {
            holder.goalDesc.setText("Walk" + " " + list[position].goalTarget + " " + stepOrSteps + " in " + list[position].noOfDays + " " + daysOrDay)
            holder.goalImg.setImageDrawable(context.getDrawable(R.drawable.ic_steps))
        } else if (list[position].goalName.equals(MEDITATION, ignoreCase = true)) {
            holder.goalDesc.setText("Meditate" + " " + list[position].goalTarget + " " + hrOrHrs + " in " + list[position].noOfDays + " " + daysOrDay)
            holder.goalImg.setImageDrawable(context.getDrawable(R.drawable.ic_zen_zone))
        } else if (list[position].goalName.equals(CALORIES_BURN, ignoreCase = true)) {
            holder.goalDesc.setText("Burn" + " " + list[position].goalTarget + " " + calOrCals + " in " + list[position].noOfDays + " " + daysOrDay)
            holder.goalImg.setImageDrawable(context.getDrawable(R.drawable.ic_calories_burnt_white))
        } else if (list[position].goalName.equals(CALORIES_CONSUME, ignoreCase = true)) {
            holder.goalDesc.setText("Consume" + " " + list[position].goalTarget +" " +  calOrCals + " in " + list[position].noOfDays + " " + daysOrDay)
            holder.goalImg.setImageDrawable(context.getDrawable(R.drawable.ic_cal_burned))
        } else if (list[position].goalName.equals(WEIGHT, ignoreCase = true)) {
            val currentWeight = java.lang.Double.valueOf(SharedPref.getWeight())
            val weightToLoseOrGain = currentWeight - list[position].goalTarget
            var desc = ""
            desc = if (weightToLoseOrGain < 0) {
                "Gain" + " " + -weightToLoseOrGain.toInt() + " " + "kg" + " in " + list[position].noOfDays + " " + daysOrDay
            } else if (weightToLoseOrGain > 0) {
                "Lose" + " " + weightToLoseOrGain + " " + "kg" + " in " + list[position].noOfDays + " " + daysOrDay
            } else {
                "Maintain" + " " + list[position].goalTarget + " " + "kg" + " for " + list[position].noOfDays + " " + daysOrDay
            }
            holder.goalDesc.setText(desc)
            holder.goalImg.setImageDrawable(context.getDrawable(R.drawable.ic_ideal_weight))
        } else if (list[position].goalName.equals(OTHERS, ignoreCase = true)) {
            holder.goalDesc.setText(list[position].goalDescription)
            holder.goalImg.setImageDrawable(context.getDrawable(R.drawable.ic_other_document))
        }

        holder.individualParentLayout.setOnClickListener {
            when (list[position].goalName) {
                "Water" -> context.startActivity(
                    Intent(context, TrendsActivity::class.java)
                        .putExtra("activityType", Constants.WATER))
                "Sleep" -> context.startActivity(
                    Intent(context, TrendsActivity::class.java)
                        .putExtra("activityType", Constants.SLEEP))
                "Steps" -> context.startActivity(
                    Intent(context, TrendsActivity::class.java)
                        .putExtra("activityType", Constants.STEPS))

                "Calories Burn","Calories \nConsume" -> context.startActivity(
                    Intent(context, TrendsActivity::class.java)
                        .putExtra("activityType", Constants.CALORIE))

                "Weight" -> context.startActivity(
                    Intent(context, TrendsActivity::class.java)
                        .putExtra("activityType", Constants.WEIGHT))

                "Meditation" -> context.startActivity(
                    Intent(context , TrendsActivity::class.java)
                        .putExtra("activityType", Constants.MEDITATION))

                "Other" -> if(list[position].goalDescription.contains("Winning")){
                    context.startActivity(Intent(context, RewardsActivity::class.java))

                }else{
                    context.startActivity(Intent(context, NewDiaryActivity::class.java))

                }

            }
        }

        holder.delete.setOnClickListener {
            deleteGoal.deleteGoalClick(list[position].goalId)
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

}