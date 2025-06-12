package com.wyh.happyyousdk.Goals.Activities

/*
import kotlinx.android.synthetic.main.activity_goals_dashboard.*
*/
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.google.gson.Gson

import com.wyh.happyyousdk.APIEncryption.APIInterface
import com.wyh.happyyousdk.APIEncryption.RetrofitHandler
import com.wyh.happyyousdk.Goals.Adapters.GoalCardsAdapter
import com.wyh.happyyousdk.Goals.Adapters.IndividualGoalItemAdapter
import com.wyh.happyyousdk.Goals.Adapters.RecommendedGoalAdapter
import com.wyh.happyyousdk.Goals.ClickInterface.DeleteGoal
import com.wyh.happyyousdk.Goals.ClickInterface.IndividualGoals
import com.wyh.happyyousdk.Goals.ClickInterface.RecommendedGoals
import com.wyh.happyyousdk.Goals.Utils.Master
import com.wyh.happyyousdk.R
import com.wyh.happyyousdk.common.adapter.IndicatorsAdapter
import com.wyh.happyyousdk.dashboard.NewDashboardActivity
import com.wyh.happyyousdk.databinding.ActivityGoalsDashboardBinding
import com.wyh.happyyousdk.diary.NewDiaryActivity
import com.wyh.happyyousdk.hra.HRAQuestionsActivity
import com.wyh.happyyousdk.model.request.DeleteGoalRequest
import com.wyh.happyyousdk.model.response.DeleteGoalResponse
import com.wyh.happyyousdk.model.response.GetGoalDayResponse
import com.wyh.happyyousdk.model.response.GetIndividualGoalResponse
import com.wyh.happyyousdk.model.response.GoalHistoryResponse
import com.wyh.happyyousdk.model.response.OtherGoalsData
import com.wyh.happyyousdk.model.response.RecomendedGoalsData
import com.wyh.happyyousdk.model.response.RecomendedGoalsResponse
import com.wyh.happyyousdk.model.response.RegualrGoalsData
import com.wyh.happyyousdk.rewards.RewardsActivity
import com.wyh.happyyousdk.trends.TrendsActivity
import com.wyh.happyyousdk.utils.CommonUtils
import com.wyh.happyyousdk.utils.Constants
import com.wyh.happyyousdk.utils.Constants.WATER
import com.wyh.happyyousdk.utils.CustomYesNoDialog
import com.wyh.happyyousdk.utils.SharedPref
import com.wyh.happyyousdk.utils.SnapHelperOneByOne
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class GoalsDashboard : AppCompatActivity(), RecommendedGoals, IndividualGoals, DeleteGoal {

     lateinit var indicatorsAdapter : IndicatorsAdapter
     lateinit var apiInterface: APIInterface
     lateinit var context: Context
    var positionReads = 0
    private lateinit var binding: ActivityGoalsDashboardBinding
     companion object {
         var recommendedGoalsList : ArrayList<RecomendedGoalsData> = arrayListOf()
         var individualGoalList : ArrayList<RegualrGoalsData> = arrayListOf()
         var otherGoals : ArrayList<OtherGoalsData> = arrayListOf()
     }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //

        binding = DataBindingUtil.setContentView(this, R.layout.activity_goals_dashboard);
        //setContentView(binding.root)

        context = this
        init()

        val customObj = JSONObject()
        try {
            customObj.put("PAGE_ID", "GoalsDashboard")
        } catch (e: JSONException) {
            throw RuntimeException(e)
        }
        
    }


    fun init(){
        try{
            apiInterface = RetrofitHandler.apiInterface()

            binding.ivHome.setOnClickListener {
                val intent = Intent(this, NewDashboardActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
                finish()
            }
            binding.goalHeaderLayout.setOnClickListener {
                finish()
            }

            binding.recommendedGoals.setOnClickListener {
                startActivity(Intent(this@GoalsDashboard,RecommendedGoalsDetails::class.java))

            }

            binding.individualGoals.setOnClickListener {
                startActivity(Intent(this@GoalsDashboard,IndividualGoalDetails::class.java))
            }

            /*individual_goal_card.setOnClickListener {
                APILogs.activityTracker("A_DB_BI_MZ_GO_AIG",this@GoalsDashboard)
                val intent = Intent(this, IndividualGoalsDashboard::class.java)
                startActivity(intent)
            }*/

            binding.historyBtn.setOnClickListener {
                getGoalHistory()
            }

            binding.takeHRA.setOnClickListener {
                context.startActivity(Intent(context, HRAQuestionsActivity::class.java))
            }

        }catch (e: Exception){
            e.toString()
        }
    }

    override fun onResume() {
        super.onResume()
        getDayGoal()
        fetchRecommendedGoals()
        setUpIndividualGoals()
    }

    fun getDayGoal(){
        try{
            apiInterface.getDayGoal(SharedPref.getAuthToken()).enqueue(object : Callback<GetGoalDayResponse>{
                override fun onResponse(call: Call<GetGoalDayResponse>, response: Response<GetGoalDayResponse>) {
                    CommonUtils.dismissDialoge()
                    if(response.body() != null && response.isSuccessful){
                        binding.goalsCardList.layoutManager = LinearLayoutManager(this@GoalsDashboard,LinearLayoutManager.HORIZONTAL,false)
                        binding.goalsCardList.hasFixedSize()
                        val goalsCardsAdapter = GoalCardsAdapter(this@GoalsDashboard, response.body()!!.data)
                        binding.goalsCardList.adapter = goalsCardsAdapter
                        binding.goalsCardList.onFlingListener = null
                        val snapHelper: SnapHelper = PagerSnapHelper()
                        snapHelper.attachToRecyclerView(binding.goalsCardList);
                    }
                }

                override fun onFailure(call: Call<GetGoalDayResponse>, t: Throwable) {
                    CommonUtils.dismissDialoge()

                }

            })
        }catch (e: Exception){
            CommonUtils.dismissDialoge()
            e.toString()
        }
    }

    fun setUpIndividualGoals(){
        try{
            apiInterface.getIndividualGoal(SharedPref.getAuthToken()).enqueue(object : Callback<GetIndividualGoalResponse>{
                override fun onResponse(call: Call<GetIndividualGoalResponse>, response: Response<GetIndividualGoalResponse>) {
                    try{
                        if(response.isSuccessful && response.code() == 200){
                            individualGoalList.clear()
                            if(response.body()!!.data.otherGoals.isNotEmpty()){
                                otherGoals = response.body()!!.data.otherGoals
                            }
                            if(response.body()!!.data.otherGoals.isNotEmpty()){
                                response.body()!!.data.otherGoals.forEach {
                                    if(it.onDashboard){
                                        val dataList = RegualrGoalsData(it.goalId,it.goalName,it.goalDescription,0,0,"",it.onDashboard,"other",it.progressPercentage)
                                        individualGoalList.add(dataList)
                                    }
                                }
                            }
                            if(response.body()!!.data.regularGoals.isNotEmpty()){
                                binding.individualGoalParent.visibility = View.VISIBLE
                                response.body()!!.data.regularGoals.forEach {
                                    if(it.onDashboard){
                                        individualGoalList.add(it)
                                    }
                                }
                            }

                            if(individualGoalList.isNotEmpty()){
                                binding.individualList.visibility = View.VISIBLE
                                val individualLayoutManager = LinearLayoutManager(this@GoalsDashboard,LinearLayoutManager.HORIZONTAL,false)
                                binding.individualList.layoutManager = individualLayoutManager
                                binding.individualList.hasFixedSize()
                                val individualGoalItemAdapter = IndividualGoalItemAdapter(this@GoalsDashboard, individualGoalList,this@GoalsDashboard,this@GoalsDashboard);
                                binding.individualList.adapter = individualGoalItemAdapter
                                binding.individualList.onFlingListener = null
                                val snapHelper: SnapHelper = SnapHelperOneByOne()
                                snapHelper.attachToRecyclerView(binding.individualList);



                                val indicatorSize = Math.ceil((individualGoalList.size.toString().toDouble() + 1.0) / 3.0).toInt()
                                Log.d("AuthToken", "Size $indicatorSize")
                                val indicatorsLayoutManager = LinearLayoutManager(context)
                                indicatorsLayoutManager.orientation = LinearLayoutManager.HORIZONTAL

                                if(indicatorSize > 1){
                                    binding.individualIndicator.visibility = View.VISIBLE
                                }else{
                                    binding.individualIndicator.visibility = View.INVISIBLE
                                }

                                val indicatorsAdapter = IndicatorsAdapter(this@GoalsDashboard, indicatorSize,0)
                                binding.individualIndicator.layoutManager = indicatorsLayoutManager
                                binding.individualIndicator.hasFixedSize()
                                binding.individualIndicator.adapter = indicatorsAdapter

                                binding.individualList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                                    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                                        super.onScrollStateChanged(recyclerView, newState)
                                        Log.d("AuthToken", ""+newState)
                                        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                                            Log.d("AuthToken", "Inside")
                                            if (individualLayoutManager.findFirstCompletelyVisibleItemPosition() != -1) {
                                                positionReads = individualLayoutManager.findFirstCompletelyVisibleItemPosition()
                                            } else positionReads = individualLayoutManager.findFirstVisibleItemPosition()
                                            Log.d("AuthToken", "$positionReads")
                                            indicatorsAdapter.updateSelectedIndex(positionReads)
                                        }
                                    }
                                })
                            }else{
                                val individualLayoutManager = LinearLayoutManager(this@GoalsDashboard,LinearLayoutManager.HORIZONTAL,false)
                                binding.individualList.layoutManager = individualLayoutManager
                                binding.individualList.hasFixedSize()
                                val individualGoalItemAdapter = IndividualGoalItemAdapter(this@GoalsDashboard, individualGoalList,this@GoalsDashboard,this@GoalsDashboard);
                                binding.individualList.adapter = individualGoalItemAdapter

                            }

                        }
                    }catch (e: Exception){
                        e.toString()
                    }

                }

                override fun onFailure(call: Call<GetIndividualGoalResponse>, t: Throwable) {
                    CommonUtils.dismissDialoge()
                }

            })



        }catch (e: Exception){
            CommonUtils.dismissDialoge()
            e.toString()
        }
    }

    fun fetchRecommendedGoals(){
        try{
            CommonUtils.showProgressDialige(this)
            apiInterface.getRecomendedGoals(SharedPref.getAuthToken()).enqueue(object : Callback<RecomendedGoalsResponse>{
                override fun onResponse(call: Call<RecomendedGoalsResponse>, response: Response<RecomendedGoalsResponse>) {
                    try{
                        CommonUtils.dismissDialoge()
                        if(response.code() == 200 && response.isSuccessful && response.body() != null){
                            Log.d("AuthToken","Goals Response ${Gson().toJson(response.body())}")
                            if(response.body()!!.data != null && response.body()!!.data.isEmpty()){
                                binding.mainScroll.visibility = View.GONE
                                binding.historyBtn.visibility = View.GONE
                                binding.llHistoryButton.visibility = View.GONE
                                binding.noGoalsLayout.visibility = View.VISIBLE
                            }else{
                                binding.mainScroll.visibility = View.VISIBLE
                                binding.historyBtn.visibility = View.VISIBLE
                                binding.llHistoryButton.visibility = View.VISIBLE
                                binding.noGoalsLayout.visibility = View.GONE

                                val linearLayoutManager = LinearLayoutManager(this@GoalsDashboard,LinearLayoutManager.HORIZONTAL,false)
                                binding.recommendedGoalsList.layoutManager = linearLayoutManager
                                recommendedGoalsList = response.body()!!.data
                                val recommendedGoalsAdapter = RecommendedGoalAdapter(this@GoalsDashboard,
                                    recommendedGoalsList)
                                binding.recommendedGoalsList.adapter = recommendedGoalsAdapter
                                binding.recommendedGoalsList.onFlingListener = null
                                val snapHelper: SnapHelper = SnapHelperOneByOne()
                                snapHelper.attachToRecyclerView(binding.recommendedGoalsList);


                                val recommendedLayoutmanager = LinearLayoutManager(this@GoalsDashboard,LinearLayoutManager.HORIZONTAL,false)



                                //recommended_goals_indicator

                                val indicatorSize = Math.ceil(recommendedGoalsList.size.toString().toDouble() / 3.0).toInt()
                                val indicatorsLayoutManager = LinearLayoutManager(context)
                                indicatorsLayoutManager.orientation = LinearLayoutManager.HORIZONTAL

                                if(indicatorSize > 1){
                                    binding.recommendedGoalsIndicator.visibility = View.VISIBLE
                                }else{
                                    binding.recommendedGoalsIndicator.visibility = View.INVISIBLE
                                }

                                val indicatorsAdapter = IndicatorsAdapter(this@GoalsDashboard, indicatorSize,0)
                                binding.recommendedGoalsIndicator.layoutManager = indicatorsLayoutManager
                                binding.recommendedGoalsIndicator.hasFixedSize()
                                binding.recommendedGoalsIndicator.adapter = indicatorsAdapter

                                binding.recommendedGoalsList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                                    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                                        super.onScrollStateChanged(recyclerView, newState)
                                        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                                            if (linearLayoutManager.findFirstCompletelyVisibleItemPosition() != -1) {
                                                positionReads = linearLayoutManager.findFirstCompletelyVisibleItemPosition()
                                            } else positionReads = linearLayoutManager.findFirstVisibleItemPosition()
                                            Log.d("AuthToken", "$positionReads")
                                            indicatorsAdapter.updateSelectedIndex(positionReads)
                                        }
                                    }
                                })
                            }
                        }
                    }catch (e: Exception){
                        e.toString()
                    }

                }

                override fun onFailure(call: Call<RecomendedGoalsResponse>, t: Throwable) {
                    CommonUtils.dismissDialoge()
                }

            })
        }catch (e: Exception){
            CommonUtils.dismissDialoge()
            e.toString()
        }
    }

    fun deleteGoal(goalID: String){
        try{
            val deleteIdRequest = DeleteGoalRequest(goalID)
            apiInterface.deleteGoals(SharedPref.getAuthToken(),deleteIdRequest).enqueue(object : Callback<DeleteGoalResponse>{
                override fun onResponse(call: Call<DeleteGoalResponse>, response: Response<DeleteGoalResponse>) {
                    if(response.code() == 200 && response.isSuccessful){
                        Toast.makeText(this@GoalsDashboard,"Your goal is deleted",Toast.LENGTH_SHORT).show()
                        setUpIndividualGoals()
                    }
                }

                override fun onFailure(call: Call<DeleteGoalResponse>, t: Throwable) {
                    CommonUtils.dismissDialoge()

                }

            })
        }catch (e: Exception){
            CommonUtils.dismissDialoge()
            e.toString()
        }
    }

    override fun onClick(goalData: RecomendedGoalsData) {
        startActivity(Intent(this@GoalsDashboard,RecommendedGoalsDetails::class.java))
    }

    override fun menuClick(name: String) {
        TODO("Not yet implemented")
    }

    override fun goalClick(name: String, noOfDays: Int, target: Int, context: Context) {
        TODO("Not yet implemented")
    }

    override fun goalRedirection(goalName: String,goalDesc: String) {
        when (goalName) {
            Master.WATER_GOAL -> startActivity(Intent(this@GoalsDashboard,TrendsActivity::class.java)
                .putExtra("activityType",WATER))
            Master.SLEEP_GOAL -> startActivity(Intent(this@GoalsDashboard,TrendsActivity::class.java)
                .putExtra("activityType", Constants.SLEEP))
            Master.STEPS_GOAL -> startActivity(
                Intent(this@GoalsDashboard,TrendsActivity::class.java)
                    .putExtra("activityType", Constants.STEPS))

            Master.CALORIES_BURN -> startActivity(
                Intent(this@GoalsDashboard,TrendsActivity::class.java)
                    .putExtra("activityType", Constants.CALORIE_BURNED))

            Master.CALORIES_CONSUME -> startActivity(
                Intent(this@GoalsDashboard,TrendsActivity::class.java)
                    .putExtra("activityType", Constants.CALORIE_CONSUME))

            Master.WEIGHT -> startActivity(
                Intent(this@GoalsDashboard,TrendsActivity::class.java)
                    .putExtra("activityType", Constants.WEIGHT))

            Master.MEDITATION -> startActivity(
                Intent(this@GoalsDashboard,TrendsActivity::class.java)
                    .putExtra("activityType", Constants.MEDITATION))

            Master.OTHERS -> {
                if(goalDesc.contains("winning",true)){
                    startActivity(Intent(this@GoalsDashboard,RewardsActivity::class.java))
                }else{
                    startActivity(Intent(this@GoalsDashboard,NewDiaryActivity::class.java))

                }
            }

        }
    }

    fun deleteGoalPopup(goalID: String) {
        try {
            val customYesNoDialog = CustomYesNoDialog(context!!, R.style.Theme_Dialog)
            customYesNoDialog.show()
            customYesNoDialog.setCancelable(false)
            customYesNoDialog.binding.llActionRequired.visibility = View.GONE
            customYesNoDialog.binding.txtInfoPopUpDesc.text = "Are you sure, You want to delete this goal ?"
            customYesNoDialog.binding.btnYes.text = "Yes"
            customYesNoDialog.binding.btnCancel.text = "No"
            customYesNoDialog.binding.btnYes.background =
                ContextCompat.getDrawable(context, R.drawable.blue_rc_bg_8dp)
            customYesNoDialog.binding.btnCancel.background =
                ContextCompat.getDrawable(context, R.drawable.pink_rc_bg_8dp)
            customYesNoDialog.binding.btnYes.setOnClickListener { view: View? ->
                customYesNoDialog.dismiss()
                deleteGoal(goalID)
            }
            customYesNoDialog.binding.btnCancel.setOnClickListener { view: View? ->
                customYesNoDialog.dismiss()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun deleteGoalClick(goalID: String) {
        deleteGoalPopup(goalID)
    }

    fun getGoalHistory(){
        try{
            CommonUtils.showProgressDialige(this)
            val apiInterface = RetrofitHandler.apiInterface()
            apiInterface.goalsHistory(SharedPref.getAuthToken()).enqueue(object : Callback<GoalHistoryResponse>{
                override fun onResponse(call: Call<GoalHistoryResponse>, response: Response<GoalHistoryResponse>) {
                    CommonUtils.dismissDialoge()
                    if(response.body() != null && response.code() == 200 && response.body()!!.success){
                        if (response.body()!!.data != null){
                            if(response.body()!!.data.deletedGoals.isEmpty() && response.body()!!.data.completedGoals.isEmpty() && response.body()!!.data.pendingGoals.isEmpty() ){
                                Toast.makeText(context,"There are no goals in history",Toast.LENGTH_SHORT).show()
                            }else{
                                startActivity(Intent(this@GoalsDashboard,GoalHistory::class.java))
                            }
                        }
                    }

                }

                override fun onFailure(call: Call<GoalHistoryResponse>, t: Throwable) {
                    CommonUtils.dismissDialoge()

                }

            })
        }catch (e: Exception){
            CommonUtils.dismissDialoge()
            e.toString()
        }
    }


}