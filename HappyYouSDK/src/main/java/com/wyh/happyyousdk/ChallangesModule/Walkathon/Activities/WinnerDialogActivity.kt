package com.wyh.happyyousdk.ChallangesModule.Walkathon.Activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.ViewPager
import com.google.gson.Gson
import com.wyh.happyyousdk.APIEncryption.APILogs
import com.wyh.happyyousdk.APIEncryption.RetrofitHandler
import com.wyh.happyyousdk.ChallangesModule.Walkathon.Fragments.BranchFragment
import com.wyh.happyyousdk.ChallangesModule.Walkathon.Fragments.IndividualFragment
import com.wyh.happyyousdk.R
import com.wyh.happyyousdk.databinding.ActivityWinnerDialogBinding
import com.wyh.happyyousdk.model.response.GetWalkathonWinnerResponse
import com.wyh.happyyousdk.model.response.WinnerBranchStep
import com.wyh.happyyousdk.model.response.WinnerUserStep
import com.wyh.happyyousdk.rewards.SampleFragmentPagerAdapter
import com.wyh.happyyousdk.utils.CommonUtils
import com.wyh.happyyousdk.utils.SharedPref
import com.wyhsdk.sharedPreferences.SharedPreference
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WinnerDialogActivity : AppCompatActivity() {

    lateinit var binding : ActivityWinnerDialogBinding
    var userStepsList:ArrayList<WinnerUserStep> = ArrayList<WinnerUserStep>()
    var branchList:ArrayList<WinnerBranchStep> = ArrayList<WinnerBranchStep>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_winner_dialog)
        getWinner()
        SharedPref.init(this)
        SharedPreference.init(this)
        binding.cancelBtn.setOnClickListener {
            APILogs.activityTracker("A_WALKATHON_WINNER_CLOSE", this@WinnerDialogActivity)
            finish()
        }
    }

    private fun setupViewPager() {
        val adapter = SampleFragmentPagerAdapter(supportFragmentManager)
        adapter.addFragment(IndividualFragment(userStepsList),"Individual")
        adapter.addFragment(BranchFragment(branchList),"Branch")
        binding.viewpager.adapter = adapter
        binding.viewpager.currentItem = 0
        binding.winnerTabs.setupWithViewPager(binding.viewpager)
    }

    override fun onResume() {
        super.onResume()

    }

    private fun getWinner(){
        try{
            CommonUtils.showProgressDialige(this)
            val apiInterface = RetrofitHandler.apiInterface()
            apiInterface.getWalkathonWinner(SharedPref.getAuthToken()).enqueue(object :
                Callback<GetWalkathonWinnerResponse> {
                override fun onResponse(call: Call<GetWalkathonWinnerResponse>, response: Response<GetWalkathonWinnerResponse>) {
                    CommonUtils.dismissDialoge()

                    if(response.code() == 200 && response.isSuccessful && response.body() != null) {
                        Log.d("tag", Gson().toJson(response.body()!!.data.userSteps.size))
                        Log.d("tag", Gson().toJson(response.body()!!.data.branchSteps.size))
                        if(response.body()!!.data.userSteps.isNotEmpty()) {
                            userStepsList.clear()
                            userStepsList.addAll(response.body()!!.data.userSteps)
                        }
                        if(response.body()!!.data.branchSteps.isNotEmpty()) {
                            branchList.clear()
                            branchList.addAll(response.body()!!.data.branchSteps)
                        }

                        setupViewPager()
                    }
                }

                override fun onFailure(call: Call<GetWalkathonWinnerResponse>, t: Throwable) {
                    CommonUtils.dismissDialoge()
                }

            })

        }catch (e:Exception){
            CommonUtils.dismissDialoge()
            e.toString()
        }
    }

}