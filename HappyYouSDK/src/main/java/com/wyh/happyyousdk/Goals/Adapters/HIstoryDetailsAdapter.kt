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
import com.wyh.happyyousdk.Goals.Utils.Master
import com.wyh.happyyousdk.R
import com.wyh.happyyousdk.model.response.HistoryData
import com.wyh.happyyousdk.trends.TrendsActivity
import com.wyh.happyyousdk.utils.Constants
import com.wyh.happyyousdk.utils.SharedPref

class HIstoryDetailsAdapter(val context: Context, val list : ArrayList<HistoryData>, val comingFrom: String) : RecyclerView.Adapter<MyViewHolder>() {

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.recommended_goals_layout,parent,false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if (bgCount == res.size - 1) {
            bgCount = 0
        } else {
            bgCount++
        }

        holder.parentLayout.background = ContextCompat.getDrawable(context,res[bgCount])
        var daysOrDay = "day"
        var glasseOrglasses = "glass"
        var hrOrHrs = "hr"
        var stepOrSteps = "step"
        var calOrCals = "cal"
        if (list[position].noOfDays > 1) {
            daysOrDay = "days"
        }else{
            daysOrDay = "day"
        }

        if(list[position].goalTarget > 1){
            glasseOrglasses = "glasses"
            hrOrHrs = "hrs"
            stepOrSteps = "steps"
            calOrCals = "cals"
        }else{
            glasseOrglasses = "glass"
            hrOrHrs = "hr"
            stepOrSteps = "step"
            calOrCals = "cal"
        }

        if(comingFrom.equals("completed",true)){
            holder.checkImg.visibility = View.VISIBLE
        }else{
            holder.checkImg.visibility = View.GONE
        }

        if (list[position].goalName.equals(Master.WATER_GOAL, ignoreCase = true)) {
            holder.goalDesc.text = "Drink" + " " + list[position].goalTarget + " " + glasseOrglasses + " in " + list[position].noOfDays + " " + daysOrDay
            holder.imageView.setImageDrawable(context.getDrawable(R.drawable.ic_water))

        } else if (list[position].goalName.equals(Master.SLEEP_GOAL, ignoreCase = true)) {
            holder.goalDesc.text = "Sleep" + " " + list[position].goalTarget + " " + hrOrHrs + " in " + list[position].noOfDays + " " + daysOrDay
            holder.imageView.setImageDrawable(context.getDrawable(R.drawable.ic_sleep_star))

        } else if (list[position].goalName.equals(Master.STEPS_GOAL, ignoreCase = true)) {
            holder.goalDesc.text = "Walk" + " " + list[position].goalTarget + " " + stepOrSteps + " in " + list[position].noOfDays + " " + daysOrDay
            holder.imageView.setImageDrawable(context.getDrawable(R.drawable.ic_steps))

        } else if (list[position].goalName.equals(Master.MEDITATION, ignoreCase = true)) {
            holder.goalDesc.text = "Meditate" + " " + list[position].goalTarget + " " + hrOrHrs + " in " + list[position].noOfDays + " " + daysOrDay
            holder.imageView.setImageDrawable(context.getDrawable(R.drawable.ic_zen_zone))

        } else if (list[position].goalName.equals(Master.CALORIES_BURN, ignoreCase = true)) {
            holder.goalDesc.text = "Burn" + " " + list[position].goalTarget + " " + calOrCals + " in " + list[position].noOfDays + " " + daysOrDay
            holder.imageView.setImageDrawable(context.getDrawable(R.drawable.ic_calories_burnt_white))

        } else if (list[position].goalName.replace("\n","").equals(Master.CALORIES_CONSUME.replace("\n",""), ignoreCase = true)) {
            holder.goalDesc.text = "Consume" + " " + list[position].goalTarget + calOrCals + " in " + list[position].noOfDays + " " + daysOrDay
            holder.imageView.setImageDrawable(context.getDrawable(R.drawable.ic_cal_burned))

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
            holder.goalDesc.text = desc
            holder.imageView.setImageDrawable(context.getDrawable(R.drawable.ic_ideal_weight))

        } else if (list[position].goalName.equals(Master.OTHERS, ignoreCase = true)) {
            if(list[position].goalDescription.contains("something",true)){
                holder.goalDesc.text = "Add something in diary"
                holder.imageView.setImageDrawable(context.getDrawable(R.drawable.ic_other_document))
            }else{
                holder.goalDesc.text = list[position].goalDescription
                holder.imageView.setImageDrawable(context.getDrawable(R.drawable.ic_other_document))
            }

        }

        holder.progressLayout.visibility = View.VISIBLE
        holder.progressBar.progress = list[position].progressPercentage.toInt()
        holder.progressTv.text = list[position].progressPercentage.toString()

        
        holder.parentLayout.setOnClickListener {
            if(comingFrom.equals("pending",true)){
                when (list[position].goalName.replace("\n","")) {
                    "Water" -> context.startActivity(
                        Intent(context, TrendsActivity::class.java)
                            .putExtra("activityType", Constants.WATER))
                    "Sleep" -> context.startActivity(
                        Intent(context, TrendsActivity::class.java)
                            .putExtra("activityType", Constants.SLEEP))
                    "Steps" -> context.startActivity(
                        Intent(context, TrendsActivity::class.java)
                            .putExtra("activityType", Constants.STEPS))

                    "Calories Consume" -> context.startActivity(
                        Intent(context, TrendsActivity::class.java)
                            .putExtra("activityType", Constants.CALORIE_CONSUME))

                    "Calories Burn" -> context.startActivity(
                        Intent(context, TrendsActivity::class.java)
                            .putExtra("activityType", Constants.CALORIE_BURNED))

                    "Weight" -> context.startActivity(
                        Intent(context, TrendsActivity::class.java)
                            .putExtra("activityType", Constants.WEIGHT))

                    "Meditation" -> context.startActivity(
                        Intent(context, TrendsActivity::class.java)
                            .putExtra("activityType", Constants.MEDITATION))

                }
            }

        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}

class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
    val parentLayout = itemView.findViewById<RelativeLayout>(R.id.recommended_parent_layout)
    val goalDesc = itemView.findViewById<TextView>(R.id.recomended_goal_desc_tv);
    val imageView = itemView.findViewById<ImageView>(R.id.recomended_goal_img)
    val progressLayout = itemView.findViewById<RelativeLayout>(R.id.progress_layout)
    val progressBar = itemView.findViewById<ProgressBar>(R.id.goal_progress_bar)
    val progressTv = itemView.findViewById<TextView>(R.id.goal_points_tv)
    val checkImg = itemView.findViewById<ImageView>(R.id.goal_complete_check_img)

}
