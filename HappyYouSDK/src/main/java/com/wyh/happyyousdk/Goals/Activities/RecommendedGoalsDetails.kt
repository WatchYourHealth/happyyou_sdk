package com.wyh.happyyousdk.Goals.Activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.wyh.happyyousdk.Goals.Adapters.RecommendedGoalsDetailsAdapter
import com.wyh.happyyousdk.Goals.ClickInterface.RecommendedGoals
import com.wyh.happyyousdk.R

import com.wyh.happyyousdk.dashboard.NewDashboardActivity
import com.wyh.happyyousdk.databinding.ActivityRecommendedGoalsDetailsBinding
import com.wyh.happyyousdk.model.response.RecomendedGoalsData
import com.wyh.happyyousdk.trends.TrendsActivity
import com.wyh.happyyousdk.utils.Constants
import com.wyh.happyyousdk.utils.CustomYesNoDialog

class RecommendedGoalsDetails : AppCompatActivity(),RecommendedGoals {

    lateinit var binding : ActivityRecommendedGoalsDetailsBinding
    lateinit var detailAdapter : RecommendedGoalsDetailsAdapter
    var addedGoals : ArrayList<RecomendedGoalsData> = arrayListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this,R.layout.activity_recommended_goals_details)


        GoalsDashboard.recommendedGoalsList.forEach {
            if(it.goaladded == 0){
                addedGoals.add(it)
            }
        }

        binding.recommendedGoalsDetailsRV.layoutManager = GridLayoutManager(this,3)
        detailAdapter = RecommendedGoalsDetailsAdapter(this,addedGoals,this)
        binding.recommendedGoalsDetailsRV.hasFixedSize()
        binding.recommendedGoalsDetailsRV.adapter = detailAdapter
        binding.commonToolBar.tvBack.text = "Recommended Goals"

        binding.commonToolBar.llBack.setOnClickListener {
            finish()
        }

        binding.ivHome.setOnClickListener {
            val intent = Intent(this, NewDashboardActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }


    }



    override fun onClick(goalData: RecomendedGoalsData) {
        when (goalData.goalName) {
            "Water" -> startActivity(
                Intent(this, TrendsActivity::class.java)
                    .putExtra("activityType", Constants.WATER))
            "Sleep" -> startActivity(
                Intent(this, TrendsActivity::class.java)
                    .putExtra("activityType", Constants.SLEEP))
            "Steps" -> startActivity(
                Intent(this, TrendsActivity::class.java)
                    .putExtra("activityType", Constants.STEPS))

            "Calories Burn","Calories \\nConsume" -> startActivity(
                Intent(this, TrendsActivity::class.java)
                    .putExtra("activityType", Constants.CALORIE))

            "Weight" -> startActivity(
                Intent(this, TrendsActivity::class.java)
                    .putExtra("activityType", Constants.WEIGHT))

            "Meditation" -> startActivity(
                Intent(this, TrendsActivity::class.java)
                    .putExtra("activityType", Constants.MEDITATION))

        }
    }


}