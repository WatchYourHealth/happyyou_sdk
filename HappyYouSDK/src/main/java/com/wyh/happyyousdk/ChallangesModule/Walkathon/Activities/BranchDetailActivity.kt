package com.wyh.happyyousdk.ChallangesModule.Walkathon.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.exoplayer2.util.Log
import com.wyh.happyyousdk.APIEncryption.APILogs
import com.wyh.happyyousdk.APIEncryption.RetrofitHandler
import com.wyh.happyyousdk.ChallangesModule.Walkathon.Adapters.UserRankListAdapter
import com.wyh.happyyousdk.R
import com.wyh.happyyousdk.databinding.ActivityBranchDetailBinding
import com.wyh.happyyousdk.model.request.ChallengeRankRequest
import com.wyh.happyyousdk.model.response.ChallengeRankResponse
import com.wyh.happyyousdk.model.response.UserRanking
import com.wyh.happyyousdk.utils.CommonUtils
import com.wyh.happyyousdk.utils.SharedPref
import com.wyhsdk.sharedPreferences.SharedPreference
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BranchDetailActivity : AppCompatActivity() {



    val usersList = arrayListOf<UserRanking>()
    var allUserList = arrayListOf<UserRanking>()
    var userCount = 10
    var userCountForCondition = 10
    var userRequestCount = 0
    lateinit var userRankListAdapter: UserRankListAdapter
    lateinit var binding : ActivityBranchDetailBinding
    var isGlobalLast = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_branch_detail)
        SharedPref.init(this)
        SharedPreference.init(this)

        binding.branchCancel.setOnClickListener {
            APILogs.activityTracker("A_WALKATHON_IND_RANK_CLOSED_BTN", this@BranchDetailActivity)
            finish()
        }

        binding.closeBtn.setOnClickListener {
            APILogs.activityTracker("A_WALKATHON_IND_RANK_CLOSED_BTN", this@BranchDetailActivity)
            finish()
        }

        binding.branchDetailLoadMoreBtn.setOnClickListener {
            if(!isGlobalLast && allUserList.size > 10){
               loadMoreWorking()
            } else {
                val msg: String =
                    if (usersList.size > 0) "No More Data Available" else "No Data Available"
                Toast.makeText(this@BranchDetailActivity, msg, Toast.LENGTH_SHORT).show()
            }

        }

        getUserChallengeRanking()
    }


    fun loadMoreWorking(){
        try{
            Log.d("AuthToken", "Count $userCount")
            APILogs.activityTracker("A_WALKATHON_IND_RANK_LM_BTN", this@BranchDetailActivity)

            if(allUserList.isNotEmpty()){
                if(userCount == allUserList.size){
                    userRequestCount = allUserList.size
                    userCountForCondition = 10
                    getUserChallengeRanking()
                }else{
                    usersList.clear()
                    userCount += 10
                    userCountForCondition += 10
                    Log.d("AuthToken","All User size ${allUserList.size} and $userCount")

                    for(i in 1..allUserList.size - 1){
                        if(i < userCount){
                            usersList.add(allUserList[i])
                        }
                    }
                    Log.d("AuthToken","User size ${usersList.size} and $userCount")

                    userRankListAdapter = UserRankListAdapter(this@BranchDetailActivity,usersList)
                    binding.branchRankList.layoutManager = LinearLayoutManager(this@BranchDetailActivity)
                    binding.branchRankList.hasFixedSize()
                    binding.branchRankList.adapter = userRankListAdapter
                    binding.branchRankList.scrollToPosition(usersList.size - 1)

                }
            }
        }catch (e: Exception){
            e.toString()
        }
    }


    fun getUserChallengeRanking(){
        try{
            CommonUtils.showProgressDialige(this)
            val request = ChallengeRankRequest("User",userRequestCount)
            val apiInterface = RetrofitHandler.apiInterface()
            apiInterface.getChallengeRanking(SharedPref.getAuthToken(),request).enqueue(object : Callback<ChallengeRankResponse>{
                override fun onResponse(call: Call<ChallengeRankResponse>, response: Response<ChallengeRankResponse>) {
                    CommonUtils.dismissDialoge()
                    try {
                        if(response.code() == 200 && response.isSuccessful && response.body() != null){
                            if(response.body()!!.data.userRankings != null){
                                binding.branchDetailListLayout.visibility = View.VISIBLE
                                binding.llNoRecordFound.visibility = View.GONE
                                if(response.body()!!.data.userRankings!!.userBranchRank != null){
                                    val userRank = response.body()!!.data.userRankings!!.userBranchRank.split("/")
                                    binding.branchDetailRankTv.text = userRank[0]
                                    binding.branchDetailTotalRankTv.text = "out of ${userRank[1]}"
                                }

                                if(response.body()!!.data.userRankings!!.branchRank != null){
                                    val rank = response.body()!!.data.userRankings!!.branchRank.split("/")
                                    binding.branchDetailBranchRankTv.text = rank[0]
                                    binding.branchDetailTotalBranchRankTv.text = "out of ${rank[1]}"
                                }

                                if(response.body()!!.data.userRankings!!.allIndiaUserRank != null){
                                    val allIndiaRank = response.body()!!.data.userRankings!!.allIndiaUserRank.split("/")
                                    binding.branchDetailAllRankTv.text = allIndiaRank[0]
                                    binding.branchDetailTotalAllRankTv.text = "out of ${allIndiaRank[1]}"
                                }

                                if(response.body()!!.data.userRankings!!.userRankings.isNotEmpty()){
                                    allUserList.addAll(response.body()!!.data.userRankings!!.userRankings)
                                    binding.ownRankTv.text = allUserList[0].rank
                                    binding.ownNameTv.text = allUserList[0].userName
                                    binding.ownBranchTv.text = allUserList[0].branchName
                                    binding.ownTotalStepsTv.text = allUserList[0].steps


                                    /*for(i in 1..allUserList.size - 1){
                                        if(i < userCountForCondition){
                                            usersList.add(allUserList[i])
                                        }
                                    }*/
                                    isGlobalLast = false
                                    var startIndex: Int = userRequestCount
                                    var endIndex: Int = startIndex + 10
                                    if (userRequestCount == 0) {
                                        startIndex = 1
                                    }

                                    if (endIndex <= allUserList.size) {
                                        val list = allUserList.subList(startIndex, endIndex)
                                        usersList.addAll(list)
                                    } else {
                                        endIndex = allUserList.size
                                        val list =
                                            allUserList.subList(startIndex, endIndex)
                                        usersList.addAll(list)
                                    }

                                    userRankListAdapter = UserRankListAdapter(this@BranchDetailActivity,usersList)
                                    binding.branchRankList.layoutManager = LinearLayoutManager(this@BranchDetailActivity)
                                    binding.branchRankList.hasFixedSize()
                                    binding.branchRankList.adapter = userRankListAdapter

                                } else {
                                    isGlobalLast = true
                                }
                            }else{
                                binding.branchDetailListLayout.visibility = View.GONE
                                binding.llNoRecordFound.visibility = View.VISIBLE
                            }
                        }

                    }catch (e: Exception){
                        e.toString()
                    }
                }

                override fun onFailure(call: Call<ChallengeRankResponse>, t: Throwable) {
                    CommonUtils.dismissDialoge()

                }

            })

        }catch (e:Exception){
            CommonUtils.dismissDialoge()
            e.toString()
        }
    }
}