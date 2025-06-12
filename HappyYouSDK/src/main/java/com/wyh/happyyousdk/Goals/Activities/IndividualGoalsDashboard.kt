package com.wyh.happyyousdk.Goals.Activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SharedMemory
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.google.gson.Gson
import com.wyh.happyyousdk.APIEncryption.APIInterface
import com.wyh.happyyousdk.APIEncryption.APILogs
import com.wyh.happyyousdk.APIEncryption.RetrofitHandler
import com.wyh.happyyousdk.Goals.Adapters.IndividualGoalsAdapter
import com.wyh.happyyousdk.Goals.ClickInterface.IndividualGoals
import com.wyh.happyyousdk.Goals.Model.IndividualGoalsModel
import com.wyh.happyyousdk.Goals.Utils.Master
import com.wyh.happyyousdk.Goals.Utils.Master.OTHERS
import com.wyh.happyyousdk.R
import com.wyh.happyyousdk.dashboard.NewDashboardActivity
import com.wyh.happyyousdk.databinding.ActivityBranchDetailBinding
import com.wyh.happyyousdk.databinding.ActivityIndividualGoalsDashboardBinding
import com.wyh.happyyousdk.model.request.AddIndividualGoalRequest
import com.wyh.happyyousdk.model.response.AddIndividualGoalResponse
import com.wyh.happyyousdk.utils.CommonUtils
import com.wyh.happyyousdk.utils.SharedPref
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class IndividualGoalsDashboard : AppCompatActivity(), IndividualGoals {

    private lateinit var individualGoalsAdapter : IndividualGoalsAdapter
    var goalsList : ArrayList<IndividualGoalsModel> = ArrayList()
    lateinit var apiInterface : APIInterface
    lateinit var binding : ActivityIndividualGoalsDashboardBinding


    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this,R.layout.activity_individual_goals_dashboard)


        var goalsModel = IndividualGoalsModel(R.drawable.ic_h2o_intake,Master.WATER_GOAL)
        goalsList.add(goalsModel)

        goalsModel = IndividualGoalsModel(R.drawable.ic_sleep_star,Master.SLEEP_GOAL)
        goalsList.add(goalsModel)

        goalsModel = IndividualGoalsModel(R.drawable.ic_foot_prints,Master.STEPS_GOAL)
        goalsList.add(goalsModel)

        goalsModel = IndividualGoalsModel(R.drawable.ic_calories_burnt_white,Master.CALORIES_BURN)
        goalsList.add(goalsModel)

        goalsModel = IndividualGoalsModel(R.drawable.ic_cal_burned,Master.CALORIES_CONSUME)
        goalsList.add(goalsModel)

        goalsModel = IndividualGoalsModel(R.drawable.ic_ideal_weight,Master.WEIGHT)
        goalsList.add(goalsModel)

        goalsModel = IndividualGoalsModel(R.drawable.ic_zen_zone,Master.MEDITATION)
        goalsList.add(goalsModel)

        goalsModel = IndividualGoalsModel(R.drawable.ic_other_document,Master.OTHERS)
        goalsList.add(goalsModel)

        init()
    }

    private fun init(){
        try{
            binding.individualGoalsList.layoutManager = GridLayoutManager(this@IndividualGoalsDashboard,3)
            binding.individualGoalsList.hasFixedSize()
             individualGoalsAdapter = IndividualGoalsAdapter(this@IndividualGoalsDashboard,goalsList,this)
            binding.individualGoalsList.adapter = individualGoalsAdapter

            binding.individualGoalsBackLayout.setOnClickListener {
                finish()
            }

            binding.gaolBack.text = "Individual Goal"

            binding.ivHome.setOnClickListener {
                val intent = Intent(this, NewDashboardActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
                finish()
            }

        }catch (e: Exception){
            e.toString()
        }
    }

    override fun menuClick(name: String) {
        if(GoalsDashboard.individualGoalList.size < 5){
            if(name.equals(OTHERS,true)){
                APILogs.activityTracker("A_DB_BI_MZ_GO_AIG_Others",this@IndividualGoalsDashboard)
                startActivity(Intent(this,OtherGoals::class.java))
            }else{
                Master.addIndiviualGoal(this@IndividualGoalsDashboard,name,this)
            }
        }else{
            Toast.makeText(this@IndividualGoalsDashboard,"Please complete or delete existing goal from goal dashboard.",Toast.LENGTH_SHORT).show()
        }


    }

    override fun goalClick(name: String, noOfDays: Int, target: Int,context: Context) {
        try{
            CommonUtils.showProgressDialige(context)
            val request = AddIndividualGoalRequest(name,"",noOfDays,target)
            apiInterface = RetrofitHandler.apiInterface()
            Log.d("AuthToken","Goal Request ${Gson().toJson(request)}")
            apiInterface.addIndividualGoal(SharedPref.getAuthToken(),request).enqueue(object : Callback<AddIndividualGoalResponse>{
                override fun onResponse(call: Call<AddIndividualGoalResponse>, response: Response<AddIndividualGoalResponse>) {
                    CommonUtils.dismissDialoge()
                    if (response.code() == 200 && response.isSuccessful){
                        Toast.makeText(context,name + " "+ context.getString(R.string.goal_add_success),Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }

                override fun onFailure(call: Call<AddIndividualGoalResponse>, t: Throwable) {
                    CommonUtils.dismissDialoge()
                }

            })
        }catch (e: Exception){
            CommonUtils.dismissDialoge()
            e.toString()
        }

    }

    override fun goalRedirection(goalName: String, goalDesc: String) {
        TODO("Not yet implemented")
    }


}