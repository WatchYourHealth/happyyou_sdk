 package com.wyh.happyyousdk.Goals.Activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.wyh.happyyousdk.APIEncryption.APIInterface
import com.wyh.happyyousdk.APIEncryption.RetrofitHandler
import com.wyh.happyyousdk.Goals.Adapters.DeleteGaolItemAdapter
import com.wyh.happyyousdk.Goals.ClickInterface.HistorySeeAll
import com.wyh.happyyousdk.R
import com.wyh.happyyousdk.common.adapter.IndicatorsAdapter
import com.wyh.happyyousdk.dashboard.NewDashboardActivity
import com.wyh.happyyousdk.databinding.ActivityGoalHistoryBinding
import com.wyh.happyyousdk.model.response.GoalHistoryResponse
import com.wyh.happyyousdk.model.response.HistoryData
import com.wyh.happyyousdk.utils.CommonUtils
import com.wyh.happyyousdk.utils.SharedPref
import com.wyh.happyyousdk.utils.SnapHelperOneByOne
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.ceil

 class GoalHistory : AppCompatActivity(), HistorySeeAll {

     lateinit var apiInterface : APIInterface
     lateinit var pendingGoalsAdapter : DeleteGaolItemAdapter
     lateinit var completedGoalsAdapter : DeleteGaolItemAdapter
     lateinit var deletedGoals : DeleteGaolItemAdapter
     lateinit var binding : ActivityGoalHistoryBinding
     var comingFrom = ""
     var positionReads = 0


     companion object{
         var pendingList : ArrayList<HistoryData> = arrayListOf()
         var completedList : ArrayList<HistoryData> = arrayListOf()
         var deleteList : ArrayList<HistoryData> = arrayListOf()
     }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_goal_history)

        apiInterface = RetrofitHandler.apiInterface()
        getGoalsHistory()

        binding.commonToolBar.llBack.setOnClickListener {
            finish()
        }

        binding.completedGoals.setOnClickListener {
            startActivity(Intent(this,HistoryDetailActivity::class.java)
                .putExtra("comingFrom","completed"))
        }

        binding.deletedGoals.setOnClickListener {
            startActivity(Intent(this,HistoryDetailActivity::class.java)
                .putExtra("comingFrom","delete"))
        }

        binding.pendingGoals.setOnClickListener {
            startActivity(Intent(this,HistoryDetailActivity::class.java)
                .putExtra("comingFrom","pending"))
        }

        binding.commonToolBar.tvBack.text = "History"

        binding.ivHome.setOnClickListener {
            val intent = Intent(this, NewDashboardActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }

    }

     private fun getGoalsHistory(){
         try{
             CommonUtils.showProgressDialige(this)
             apiInterface.goalsHistory(SharedPref.getAuthToken()).enqueue(object : Callback<GoalHistoryResponse>{
                 override fun onResponse(call: Call<GoalHistoryResponse>, response: Response<GoalHistoryResponse>) {
                     CommonUtils.dismissDialoge()
                     if(response.code() == 200 && response.body()!!.success){
                         if(response.body()!!.data.pendingGoals.isNotEmpty()){
                             pendingList.clear()
                             pendingList = response.body()!!.data.pendingGoals
                             val arrayList : ArrayList<HistoryData> = arrayListOf()


                             if (pendingList.size > 9){
                                 for (i in 0..8) {
                                     arrayList.add(pendingList[i])
                                 }
                             }else{
                                 arrayList.addAll(pendingList)
                             }


                             binding.pendingHistoryParent.visibility = View.VISIBLE
                             val pendingLayoutManager = LinearLayoutManager(this@GoalHistory,LinearLayoutManager.HORIZONTAL,false)
                             binding.pendingGoalsRV.layoutManager = pendingLayoutManager
                             binding.pendingGoalsRV.hasFixedSize()
                             pendingGoalsAdapter = DeleteGaolItemAdapter(this@GoalHistory, arrayList,"pending")
                             binding.pendingGoalsRV.adapter = pendingGoalsAdapter

                             binding.pendingGoalsRV.onFlingListener = null
                             val snapHelper: SnapHelper = SnapHelperOneByOne()
                             snapHelper.attachToRecyclerView(binding.pendingGoalsRV);

                             val indicatorSize = ceil((arrayList.size.toString().toDouble()) / 3.0).toInt()
                             Log.d("AuthToken", "Size $indicatorSize")
                             val indicatorsLayoutManager = LinearLayoutManager(this@GoalHistory)
                             indicatorsLayoutManager.orientation = LinearLayoutManager.HORIZONTAL

                             if(indicatorSize > 1){
                                 binding.pendingGoalsIndicator.visibility = View.VISIBLE
                             }else{
                                 binding.pendingGoalsIndicator.visibility = View.INVISIBLE
                             }

                             val indicatorsAdapter = IndicatorsAdapter(this@GoalHistory, indicatorSize,0)
                             binding.pendingGoalsIndicator.layoutManager = indicatorsLayoutManager
                             binding.pendingGoalsIndicator.hasFixedSize()
                             binding.pendingGoalsIndicator.adapter = indicatorsAdapter

                             binding.pendingGoalsRV.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                                 override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                                     super.onScrollStateChanged(recyclerView, newState)
                                     Log.d("AuthToken", ""+newState)
                                     if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                                         Log.d("AuthToken", "Inside")
                                         if (pendingLayoutManager.findFirstCompletelyVisibleItemPosition() != -1) {
                                             positionReads = pendingLayoutManager.findFirstCompletelyVisibleItemPosition()
                                         } else positionReads = pendingLayoutManager.findFirstVisibleItemPosition()
                                         Log.d("AuthToken", "$positionReads")
                                         indicatorsAdapter.updateSelectedIndex(positionReads)
                                     }
                                 }
                             })


                         }else{
                             binding.pendingHistoryParent.visibility = View.GONE
                             binding.pendingGoalsIndicator.visibility = View.GONE
                         }

                         if(response.body()!!.data.deletedGoals.isNotEmpty()){

                             val arrayList : ArrayList<HistoryData> = arrayListOf()

                             deleteList.clear()
                             deleteList = response.body()!!.data.deletedGoals

                             if (deleteList.size > 9){
                                 for (i in 0..8) {
                                     arrayList.add(deleteList[i])
                                 }
                             }else{
                                 arrayList.addAll(deleteList)
                             }


                             binding.deleteGoalParent.visibility = View.VISIBLE
                             val deleteGoalManager = LinearLayoutManager(this@GoalHistory,LinearLayoutManager.HORIZONTAL,false)
                             binding.deletedGoalsRV.layoutManager = deleteGoalManager
                             binding.deletedGoalsRV.hasFixedSize()
                             deletedGoals = DeleteGaolItemAdapter(this@GoalHistory, arrayList,"delete")
                             binding.deletedGoalsRV.adapter = deletedGoals

                             binding.deletedGoalsRV.onFlingListener = null
                             val snapHelper: SnapHelper = SnapHelperOneByOne()
                             snapHelper.attachToRecyclerView(binding.deletedGoalsRV);

                             val indicatorSize = Math.ceil((arrayList.size.toString().toDouble()) / 3.0).toInt()
                             Log.d("AuthToken", "Size $indicatorSize")
                             val indicatorsLayoutManager = LinearLayoutManager(this@GoalHistory)
                             indicatorsLayoutManager.orientation = LinearLayoutManager.HORIZONTAL

                             if(indicatorSize > 1){
                                 binding.deletedGoalsIndicator.visibility = View.VISIBLE
                             }else{
                                 binding.deletedGoalsIndicator.visibility = View.INVISIBLE
                             }

                             val indicatorsAdapter = IndicatorsAdapter(this@GoalHistory, indicatorSize,0)
                                 binding.deletedGoalsIndicator.layoutManager = indicatorsLayoutManager
                                 binding.deletedGoalsIndicator.hasFixedSize()
                                 binding.deletedGoalsIndicator.adapter = indicatorsAdapter

                             binding.deletedGoalsRV.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                                 override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                                     super.onScrollStateChanged(recyclerView, newState)
                                     Log.d("AuthToken", ""+newState)
                                     if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                                         Log.d("AuthToken", "Inside")
                                         if (deleteGoalManager.findFirstCompletelyVisibleItemPosition() != -1) {
                                             positionReads = deleteGoalManager.findFirstCompletelyVisibleItemPosition()
                                         } else positionReads = deleteGoalManager.findFirstVisibleItemPosition()
                                         Log.d("AuthToken", "$positionReads")
                                         indicatorsAdapter.updateSelectedIndex(positionReads)
                                     }
                                 }
                             })
                         }else{
                             binding.deleteGoalParent.visibility = View.GONE
                             binding.deletedGoalsIndicator.visibility = View.GONE
                         }

                         if(response.body()!!.data.completedGoals.isNotEmpty()){
                             completedList.clear()
                             completedList = response.body()!!.data.completedGoals

                             val arrayList : ArrayList<HistoryData> = arrayListOf()

                             if (completedList.size > 9){
                                 for (i in 0..8) {
                                     arrayList.add(completedList[i])
                                 }
                             }else{
                                 arrayList.addAll(completedList)
                             }


                             binding.completeGoalParent.visibility = View.VISIBLE
                             val completeLayoutManager = LinearLayoutManager(this@GoalHistory,LinearLayoutManager.HORIZONTAL,false)
                             binding.completedGoalsRV.layoutManager = completeLayoutManager
                             binding.completedGoalsRV.hasFixedSize()
                             completedGoalsAdapter = DeleteGaolItemAdapter(this@GoalHistory, arrayList,"completed")
                             binding.completedGoalsRV.adapter = completedGoalsAdapter

                             binding.completedGoalsRV.onFlingListener = null
                             val snapHelper: SnapHelper = SnapHelperOneByOne()
                             snapHelper.attachToRecyclerView(binding.completedGoalsRV);

                             val indicatorSize = Math.ceil((arrayList.size.toString().toDouble()) / 3.0).toInt()
                             val indicatorsLayoutManager = LinearLayoutManager(this@GoalHistory)
                             indicatorsLayoutManager.orientation = LinearLayoutManager.HORIZONTAL

                             if(indicatorSize > 1){
                                 binding.completedGoalsIndicator.visibility = View.VISIBLE
                             }else{
                                 binding.completedGoalsIndicator.visibility = View.INVISIBLE
                             }

                             val indicatorsAdapter = IndicatorsAdapter(this@GoalHistory, indicatorSize,0)
                                 binding.completedGoalsIndicator.layoutManager = indicatorsLayoutManager
                                 binding.completedGoalsIndicator.hasFixedSize()
                                 binding.completedGoalsIndicator.adapter = indicatorsAdapter

                             binding.completedGoalsRV.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                                 override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                                     super.onScrollStateChanged(recyclerView, newState)
                                     Log.d("AuthToken", ""+newState)
                                     if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                                         Log.d("AuthToken", "Inside")
                                         if (completeLayoutManager.findFirstCompletelyVisibleItemPosition() != -1) {
                                             positionReads = completeLayoutManager.findFirstCompletelyVisibleItemPosition()
                                         } else positionReads = completeLayoutManager.findFirstVisibleItemPosition()
                                         Log.d("AuthToken", "$positionReads")
                                         indicatorsAdapter.updateSelectedIndex(positionReads)
                                     }
                                 }
                             })
                         }else{
                             binding.completeGoalParent.visibility = View.GONE
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

     override fun goalHistoryClick(data: ArrayList<HistoryData>,comingFrom : String) {
         deleteList = data
         startActivity(Intent(this,HistoryDetailActivity::class.java)
             .putExtra("comingFrom",comingFrom))
     }
 }