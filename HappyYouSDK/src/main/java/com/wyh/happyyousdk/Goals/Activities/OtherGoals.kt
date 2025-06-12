package com.wyh.happyyousdk.Goals.Activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.wyh.happyyousdk.APIEncryption.APIInterface
import com.wyh.happyyousdk.APIEncryption.APILogs
import com.wyh.happyyousdk.APIEncryption.RetrofitHandler
import com.wyh.happyyousdk.Goals.Adapters.OtherGoalAdapter
import com.wyh.happyyousdk.Goals.ClickInterface.OtherGoalClick
import com.wyh.happyyousdk.R

import com.wyh.happyyousdk.dashboard.NewDashboardActivity
import com.wyh.happyyousdk.databinding.ActivityOtherGoalsBinding
import com.wyh.happyyousdk.model.request.AddGoalOnDashboardRequest
import com.wyh.happyyousdk.model.response.AddGoalOnDashboardResponse
import com.wyh.happyyousdk.model.response.OtherGoalsData
import com.wyh.happyyousdk.utils.CommonUtils
import com.wyh.happyyousdk.utils.CustomYesNoDialog
import com.wyh.happyyousdk.utils.SharedPref
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OtherGoals : AppCompatActivity(), OtherGoalClick {

    lateinit var binding : ActivityOtherGoalsBinding
    lateinit var otherGoalsAdapter : OtherGoalAdapter
    var otherGoalsList : ArrayList<OtherGoalsData> = arrayListOf()
    lateinit var apiInterface: APIInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this,R.layout.activity_other_goals)
        apiInterface = RetrofitHandler.apiInterface()


        GoalsDashboard.otherGoals.forEach {
            if(!it.onDashboard){
                otherGoalsList.add(it)
            }
        }
        if (otherGoalsList.isNotEmpty()) {
            binding.noGoalsText.visibility = View.GONE

        }else{
            binding.noGoalsText.visibility = View.VISIBLE
            binding.otherGoalsDetailsRV.visibility = View.GONE
        }

        binding.otherGoalsDetailsRV.layoutManager = GridLayoutManager(this,3)
        binding.otherGoalsDetailsRV.hasFixedSize()
        otherGoalsAdapter = OtherGoalAdapter(this,otherGoalsList,this)
        binding.otherGoalsDetailsRV.adapter = otherGoalsAdapter


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

    override fun otherGoalOnClick(otherGoalData : OtherGoalsData) {
        if(GoalsDashboard.individualGoalList.size < 5){
            addGoalDailoge(this,otherGoalData)
        }else{
            Toast.makeText(this@OtherGoals,"Please complete the goals", Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addGoalDailoge(context: Context?, otherGoalData: OtherGoalsData) {
        try {
            val customYesNoDialog = CustomYesNoDialog(context!!, R.style.Theme_Dialog)
            customYesNoDialog.show()
            customYesNoDialog.setCancelable(false)
            customYesNoDialog.binding.llActionRequired.visibility = View.GONE
            customYesNoDialog.binding.txtInfoPopUpDesc.text = "Are you sure, You want to add this goal ?"
            customYesNoDialog.binding.btnYes.text = "Yes"
            customYesNoDialog.binding.btnCancel.text = "No"
            customYesNoDialog.binding.btnYes.background =
                ContextCompat.getDrawable(context, R.drawable.blue_rc_bg_8dp)
            customYesNoDialog.binding.btnCancel.background =
                ContextCompat.getDrawable(context, R.drawable.pink_rc_bg_8dp)
            customYesNoDialog.binding.btnYes.setOnClickListener { view: View? ->
                if(otherGoalData.goalDescription.contains("Winning")){
                    APILogs.activityTracker("A_DB_BI_MZ_GO_AIG_Others_CATWT_Yes",this@OtherGoals)
                }else if(otherGoalData.goalDescription.contains("Dairy")){
                    APILogs.activityTracker("A_DB_BI_MZ_GO_AIG_Others_ASID_Yes",this@OtherGoals)
                }
                addGoalsToDashboard(otherGoalData.goalId,true)
                customYesNoDialog.dismiss()
            }
            customYesNoDialog.binding.btnCancel.setOnClickListener { view: View? ->
                if(otherGoalData.goalDescription.contains("Winning")){
                    APILogs.activityTracker("A_DB_BI_MZ_GO_AIG_Others_CATWT_No",this@OtherGoals)
                }else if(otherGoalData.goalDescription.contains("Dairy")){
                    APILogs.activityTracker("A_DB_BI_MZ_GO_AIG_Others_ASID_No",this@OtherGoals)
                }
                customYesNoDialog.dismiss()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun addGoalsToDashboard(goalID: String, flag: Boolean){
        try{
            CommonUtils.showProgressDialige(this)
            val request : AddGoalOnDashboardRequest = AddGoalOnDashboardRequest(goalID,flag)
            apiInterface.addGoalOnDashboard(SharedPref.getAuthToken(),request).enqueue(object : Callback<AddGoalOnDashboardResponse>{
                override fun onResponse(call: Call<AddGoalOnDashboardResponse>, response: Response<AddGoalOnDashboardResponse>) {
                    CommonUtils.dismissDialoge()
                    if(response.code() == 200 && response.isSuccessful){
                       Toast.makeText(this@OtherGoals,"Goal Added Succesfully",Toast.LENGTH_SHORT).show()
                       startActivity(Intent(this@OtherGoals,GoalsDashboard::class.java))
                       finish()
                   }
                }

                override fun onFailure(call: Call<AddGoalOnDashboardResponse>, t: Throwable) {
                    CommonUtils.dismissDialoge()

                }

            })
        }catch (e: Exception){
            CommonUtils.dismissDialoge()
            e.toString()
        }
    }
}