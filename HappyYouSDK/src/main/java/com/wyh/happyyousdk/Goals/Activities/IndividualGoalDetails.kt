package com.wyh.happyyousdk.Goals.Activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.wyh.happyyousdk.APIEncryption.RetrofitHandler
import com.wyh.happyyousdk.Goals.Adapters.IndividualGoalDetailsAdapter
import com.wyh.happyyousdk.Goals.ClickInterface.DeleteGoal
import com.wyh.happyyousdk.R
import com.wyh.happyyousdk.dashboard.NewDashboardActivity
import com.wyh.happyyousdk.databinding.ActivityIndividualGoalDetailsBinding
import com.wyh.happyyousdk.model.request.DeleteGoalRequest
import com.wyh.happyyousdk.model.response.DeleteGoalResponse
import com.wyh.happyyousdk.model.response.GetIndividualGoalResponse
import com.wyh.happyyousdk.model.response.RegualrGoalsData
import com.wyh.happyyousdk.utils.CommonUtils
import com.wyh.happyyousdk.utils.CustomYesNoDialog
import com.wyh.happyyousdk.utils.SharedPref
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class IndividualGoalDetails : AppCompatActivity(), DeleteGoal {

    lateinit var binding : ActivityIndividualGoalDetailsBinding
    var addedGoals : ArrayList<RegualrGoalsData> = arrayListOf()
    var individualGoalList : ArrayList<RegualrGoalsData> = arrayListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this,R.layout.activity_individual_goal_details)

        GoalsDashboard.individualGoalList.forEach {
            addedGoals.add(it)
        }

        binding.commonToolBar.llBack.setOnClickListener {
            finish()
        }

        binding.commonToolBar.tvBack.text = "Individual Goals"

        binding.ivHome.setOnClickListener {
            val intent = Intent(this, NewDashboardActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }

        if(addedGoals.isEmpty()){
            binding.llNoRecordFound.visibility = View.VISIBLE
            binding.listLayout.visibility = View.GONE
        }else{
            binding.llNoRecordFound.visibility = View.GONE
            binding.listLayout.visibility = View.VISIBLE
            binding.individualGoalsDetailsRV.layoutManager =  GridLayoutManager(this,3)
            binding.individualGoalsDetailsRV.hasFixedSize()
            val adapter = IndividualGoalDetailsAdapter(this@IndividualGoalDetails,addedGoals,this)
            binding.individualGoalsDetailsRV.adapter = adapter
        }

    }

    override fun deleteGoalClick(goalID: String) {
        deleteGoalPopup(goalID,this)
    }

    fun deleteGoalPopup(goalID: String,context: Context) {
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

    fun deleteGoal(goalID: String){
        try{
            CommonUtils.showProgressDialige(this)
            val deleteIdRequest = DeleteGoalRequest(goalID)
            val apiInterface = RetrofitHandler.apiInterface()
            apiInterface.deleteGoals(SharedPref.getAuthToken(),deleteIdRequest).enqueue(object :
                Callback<DeleteGoalResponse> {
                override fun onResponse(call: Call<DeleteGoalResponse>, response: Response<DeleteGoalResponse>) {
                    CommonUtils.dismissDialoge()
                    if(response.code() == 200 && response.isSuccessful){
                        Toast.makeText(this@IndividualGoalDetails,"Your goal is deleted", Toast.LENGTH_SHORT).show()
                        fetchAddedGoals()
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

    fun fetchAddedGoals(){
        try{
            CommonUtils.showProgressDialige(this)
            val apiInterface = RetrofitHandler.apiInterface()
            apiInterface.getIndividualGoal(SharedPref.getAuthToken()).enqueue(object : Callback<GetIndividualGoalResponse>{
                override fun onResponse(call: Call<GetIndividualGoalResponse>, response: Response<GetIndividualGoalResponse>) {
                    CommonUtils.dismissDialoge()
                    if(response.code() == 200 && response.body() != null){
                        individualGoalList.clear()
                        if(response.body()!!.data.otherGoals.isNotEmpty()){
                            response.body()!!.data.otherGoals.forEach {
                                if(it.onDashboard){
                                    val dataList = RegualrGoalsData(it.goalId,it.goalName,it.goalDescription,0,0,"",it.onDashboard,"other",it.progressPercentage)
                                    individualGoalList.add(dataList)
                                }
                            }
                        }
                        if(response.body()!!.data.regularGoals.isNotEmpty()){
                            response.body()!!.data.regularGoals.forEach {
                                if(it.onDashboard){
                                    individualGoalList.add(it)
                                }
                            }
                        }

                        if(individualGoalList.isNotEmpty()){
                            binding.llNoRecordFound.visibility = View.GONE
                            binding.listLayout.visibility = View.VISIBLE
                            binding.individualGoalsDetailsRV.layoutManager =  GridLayoutManager(this@IndividualGoalDetails,3)
                            binding.individualGoalsDetailsRV.hasFixedSize()
                            val adapter = IndividualGoalDetailsAdapter(this@IndividualGoalDetails,individualGoalList,this@IndividualGoalDetails)
                            binding.individualGoalsDetailsRV.adapter = adapter
                        }else{
                            binding.llNoRecordFound.visibility = View.VISIBLE
                            binding.listLayout.visibility = View.GONE
                        }
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
}