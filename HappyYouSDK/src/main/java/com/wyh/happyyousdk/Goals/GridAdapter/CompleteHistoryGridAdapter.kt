package com.wyh.happyyousdk.Goals.GridAdapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.wyh.happyyousdk.Goals.ClickInterface.HistorySeeAll
import com.wyh.happyyousdk.Goals.Utils.Master
import com.wyh.happyyousdk.R
import com.wyh.happyyousdk.diary.NewDiaryActivity
import com.wyh.happyyousdk.model.response.HistoryData
import com.wyh.happyyousdk.rewards.RewardsActivity
import com.wyh.happyyousdk.trends.TrendsActivity
import com.wyh.happyyousdk.utils.Constants
import com.wyh.happyyousdk.utils.SharedPref

class CompleteHistoryGridAdapter(val context: Context, val list : ArrayList<HistoryData>, val click : HistorySeeAll, val comingFrom : String) : BaseAdapter() {
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

    override fun getCount(): Int {
        return if(list.size <=2){
            list.size
        }else{
            3
        }
    }

    override fun getItem(position: Int): Any {
        TODO("Not yet implemented")
    }

    override fun getItemId(position: Int): Long {
        TODO("Not yet implemented")
    }

    override fun getView(position: Int, p1: View?, p2: ViewGroup?): View {
        val itemView = LayoutInflater.from(context).inflate(R.layout.goal_history_item,null)
        val parentLayout = itemView.findViewById<RelativeLayout>(R.id.history_parent_layout)
        val goalDesc = itemView.findViewById<TextView>(R.id.history_desc_tv);
        val goalMain = itemView.findViewById<RelativeLayout>(R.id.goalsMain)
        val moreGoals = itemView.findViewById<TextView>(R.id.moreGoals)
        val imageView = itemView.findViewById<ImageView>(R.id.goal_history_img);

        if (bgCount == res.size - 1) {
            bgCount = 0
        } else {
            bgCount++
        }

        if(list.size < 2){
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
            parentLayout.background = ContextCompat.getDrawable(context,res[bgCount])

        }else{
            if(position == 2){
                moreGoals.visibility = View.VISIBLE
                goalMain.visibility = View.GONE
                parentLayout.background = context.getDrawable(R.drawable.ic_hub_add_bg)
            }else{
                moreGoals.visibility = View.GONE
                goalMain.visibility = View.VISIBLE
                parentLayout.background = ContextCompat.getDrawable(context,res[bgCount])
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
            }
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

        parentLayout.setOnClickListener {
            if(position == 2){
                click.goalHistoryClick(list,comingFrom)
            }else{
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

                    "Calories Burn","Calories \\nConsume" -> context.startActivity(
                        Intent(context, TrendsActivity::class.java)
                            .putExtra("activityType", Constants.CALORIE))

                    "Weight" -> context.startActivity(
                        Intent(context, TrendsActivity::class.java)
                            .putExtra("activityType", Constants.WEIGHT))

                    "Meditation" -> context.startActivity(
                        Intent(context, TrendsActivity::class.java)
                            .putExtra("activityType", Constants.MEDITATION))

                    "Other" -> {
                        if(list[position].goalDescription.contains("winning",true)){
                            context.startActivity(Intent(context, RewardsActivity::class.java))
                        }else{
                            context.startActivity(Intent(context, NewDiaryActivity::class.java))

                        }
                    }

                }
            }
        }

        return itemView
    }
}