package com.wyh.happyyousdk.Goals.Utils

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.graphics.Rect
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.wyh.happyyousdk.APIEncryption.APILogs
import com.wyh.happyyousdk.Goals.ClickInterface.IndividualGoals
import com.wyh.happyyousdk.R
import com.wyh.happyyousdk.databinding.IndividualGoalPopUpBinding
import com.wyh.happyyousdk.utils.SharedPref

object Master {

    val WATER_GOAL = "Water"
    val SLEEP_GOAL = "Sleep"
    val STEPS_GOAL = "Steps"
    val CALORIES_BURN = "Calories Burn"
    val CALORIES_CONSUME = "Calories \n" + "Consume"
    val WEIGHT = "Weight"
    val MEDITATION = "Meditation"
    val OTHERS = "Other"

    lateinit var  alertBuilder : AlertDialog.Builder
    lateinit var alertDialog: AlertDialog
    fun addIndiviualGoal(context: Activity,comingFrom: String, click : IndividualGoals){
        try{
           alertBuilder  = AlertDialog.Builder(context)
            val binding : IndividualGoalPopUpBinding = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.individual_goal_pop_up,null,false)

            if(comingFrom.equals(WATER_GOAL,true)){
                binding.titleTv.text = context.resources.getText(R.string.water_title)
                binding.weightLayout.visibility = View.GONE
                binding.numOfDaysTv.text = context.resources.getText(R.string.sleep_tv)
                binding.goalTargetTv.text = context.resources.getText(R.string.water_target)
            }else if (comingFrom.equals(SLEEP_GOAL,true)){
                binding.titleTv.text = context.resources.getText(R.string.sleep_goal)
                binding.weightLayout.visibility = View.GONE
                binding.numOfDaysTv.text = context.resources.getText(R.string.sleep_tv)
                binding.goalTargetTv.text = context.resources.getText(R.string.sleep_target)
            }else if (comingFrom.equals(STEPS_GOAL,true)){
                binding.titleTv.text = context.resources.getText(R.string.steps_goal)
                binding.weightLayout.visibility = View.GONE
                binding.numOfDaysTv.text = context.resources.getText(R.string.sleep_tv)
                binding.goalTargetTv.text = context.resources.getText(R.string.step_target)
            }else if (comingFrom.equals(CALORIES_BURN,true)){
                binding.titleTv.text = context.resources.getText(R.string.calories_goal)
                binding.weightLayout.visibility = View.GONE
                binding.numOfDaysTv.text = context.resources.getText(R.string.sleep_tv)
                binding.goalTargetTv.text = context.resources.getText(R.string.calories_target)
            }else if (comingFrom.equals(CALORIES_CONSUME,true)){
                binding.titleTv.text = context.resources.getText(R.string.calories_consume_goal)
                binding.weightLayout.visibility = View.GONE
                binding.numOfDaysTv.text = context.resources.getText(R.string.sleep_tv)
                binding.goalTargetTv.text = context.resources.getText(R.string.calories_consume_target)
            }else if (comingFrom.equals(MEDITATION,true)){
                binding.titleTv.text = context.resources.getText(R.string.meditation_goal)
                binding.weightLayout.visibility = View.GONE
                binding.numOfDaysTv.text = context.resources.getText(R.string.sleep_tv)
                binding.goalTargetTv.text = context.resources.getText(R.string.meditation_target)
            }else if (comingFrom.equals(WEIGHT,true)){
                if(SharedPref.getWeight() != null && !SharedPref.getWeight().equals("")){
                    binding.numOfDays.setText(SharedPref.getWeight())
                }else{
                    binding.numOfDays.setText("0")

                }
                binding.numOfDays.isEnabled = false
                binding.titleTv.text = context.resources.getText(R.string.weight_goal)
                binding.weightLayout.visibility = View.VISIBLE
                binding.numOfDaysTv.text = context.resources.getText(R.string.current_weight_tv)
                binding.goalTargetTv.text = context.resources.getText(R.string.weight_target)
                binding.weightTotalDaysTv.text = context.resources.getText(R.string.weight_days)
            }

            binding.cancelGoalBtn.setOnClickListener {
                closeLogEvent(comingFrom,context)
                alertDialog.dismiss()
            }



            binding.setGoalBtn.setOnClickListener {
                Log.d("AuthToken", binding.numOfDays.text.toString())
                if (binding.numOfDays.text.toString().isEmpty() || binding.numOfDays.text.toString().equals("0",true)){
                    Toast.makeText(context,"Please enter correct days",Toast.LENGTH_LONG).show()
                    return@setOnClickListener
                }
                if(binding.goalTarget.text.isEmpty() || binding.goalTarget.text.toString().equals("0",true)){
                    Toast.makeText(context,"Please enter correct target goal",Toast.LENGTH_LONG).show()
                    return@setOnClickListener
                }
                if(comingFrom.equals(WEIGHT,true)){
                    if(SharedPref.getWeight().isNotEmpty()){
                        if(binding.goalTarget.text.toString().toDouble() == SharedPref.getWeight().toDouble()){
                            Toast.makeText(context,"Current weight and target weight should not be same.",Toast.LENGTH_LONG).show()
                            return@setOnClickListener
                        }
                    }
                    if(binding.weightTotalDays.text.toString().isEmpty() || binding.weightTotalDays.text.toString().equals("0",true)){
                        Toast.makeText(context,"Please enter correct total days",Toast.LENGTH_LONG).show()
                        return@setOnClickListener
                    }
                    if(binding.goalTarget.text.toString().toDouble() < 25 || binding.goalTarget.text.toString().toDouble() > 200){
                        Toast.makeText(context,"Please enter correct target goal",Toast.LENGTH_LONG).show()
                        return@setOnClickListener
                    }
                }
                alertDialog.dismiss()
                if(SharedPref.getWeight().isEmpty()){
                    SharedPref.putWeight(binding.numOfDays.text.toString())
                }
                if(binding.numOfDays.text.contains(".")){
                    startLogEvent(WEIGHT,context)
                    val weight = String.format("%.0f",binding.goalTarget.text.toString().toDouble())
                    click.goalClick(comingFrom,binding.weightTotalDays.text.toString().toInt(),weight.toInt(),context)
                }else{
                    startLogEvent(comingFrom,context)
                    click.goalClick(comingFrom,binding.numOfDays.text.toString().toInt(),binding.goalTarget.text.toString().toInt(),context)
                }
            }

            alertBuilder.setView(binding.root)
            alertDialog = alertBuilder.create()
            alertDialog.setCancelable(false)

            val displayRectangle = Rect()
            val window: Window = (context).window

            window.decorView.getWindowVisibleDisplayFrame(displayRectangle)

            alertDialog.window!!.setLayout(
                (displayRectangle.width() *
                        0.8f).toInt(), (displayRectangle.height() * 0.8f).toInt()
            )

            if (!alertDialog.isShowing){
                alertDialog.show()
            }

        }catch (e: Exception){
            e.toString()
        }
    }


    fun startLogEvent(cameFrom: String,context: Context){
        try{
            when (cameFrom) {
                WATER_GOAL -> APILogs.activityTracker("A_DB_BI_MZ_GO_AIG_W_SWG_Start",context)

                SLEEP_GOAL -> APILogs.activityTracker("A_DB_BI_MZ_GO_AIG_S_SWG_Start",context)

                STEPS_GOAL -> APILogs.activityTracker("A_DB_BI_MZ_GO_AIG_ST_STWG_Start",context)

                CALORIES_BURN -> APILogs.activityTracker("A_DB_BI_MZ_GO_AIG_CB_SCBG_Start",context)

                CALORIES_CONSUME -> APILogs.activityTracker("A_DB_BI_MZ_GO_AIG_CC_SCCG_Start",context)

                MEDITATION -> APILogs.activityTracker("A_DB_BI_MZ_GO_AIG_Med_SMG_Start",context)

                WEIGHT -> APILogs.activityTracker("A_DB_BI_MZ_GO_AIG_Wght_SWG_Start",context)

            }
        }catch (e: Exception){
            e.toString()
        }
    }

    fun closeLogEvent(cameFrom: String,context: Context){
        try{
            when (cameFrom) {
                WATER_GOAL -> APILogs.activityTracker("A_DB_BI_MZ_GO_AIG_W_SWG_Close",context)

                SLEEP_GOAL -> APILogs.activityTracker("A_DB_BI_MZ_GO_AIG_S_SWG_Close",context)

                STEPS_GOAL -> APILogs.activityTracker("A_DB_BI_MZ_GO_AIG_ST_STWG_Close",context)

                CALORIES_BURN -> APILogs.activityTracker("A_DB_BI_MZ_GO_AIG_CB_SCBG_Close",context)

                CALORIES_CONSUME -> APILogs.activityTracker("A_DB_BI_MZ_GO_AIG_CC_SCCG_Close",context)

                MEDITATION -> APILogs.activityTracker("A_DB_BI_MZ_GO_AIG_Med_SMG_Close",context)

                WEIGHT -> APILogs.activityTracker("A_DB_BI_MZ_GO_AIG_Wght_SWG_Close",context)

            }
        }catch (e: Exception){
            e.toString()
        }
    }



}