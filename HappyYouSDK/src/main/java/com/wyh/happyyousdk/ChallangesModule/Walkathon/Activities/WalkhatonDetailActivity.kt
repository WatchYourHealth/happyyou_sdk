package com.wyh.happyyousdk.ChallangesModule.Walkathon.Activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.wyh.happyyousdk.utils.viewtooltip.ViewTooltip
import com.google.android.exoplayer2.util.Log
import com.google.gson.Gson
import com.wyh.happyyousdk.APIEncryption.APILogs
import com.wyh.happyyousdk.APIEncryption.RetrofitHandler
import com.wyh.happyyousdk.ChallangesModule.Walkathon.Adapters.BranchRankListAdapter
import com.wyh.happyyousdk.ChallangesModule.Walkathon.Adapters.GlobalRankListAdapter
import com.wyh.happyyousdk.ChallangesModule.Walkathon.Helper.Master
import com.wyh.happyyousdk.R
import com.wyh.happyyousdk.SDKConstants
import com.wyh.happyyousdk.databinding.ActivityWalkhatonDetailBinding
import com.wyh.happyyousdk.model.request.ChallengeRankRequest
import com.wyh.happyyousdk.model.response.BranchRanking
import com.wyh.happyyousdk.model.response.ChallengeRankData
import com.wyh.happyyousdk.model.response.ChallengeRankResponse
import com.wyh.happyyousdk.model.response.GlobalRanking
import com.wyh.happyyousdk.syncDevice.ConnectApp
import com.wyh.happyyousdk.syncDevice.SyncDeviceActivity
import com.wyh.happyyousdk.utils.CommonUtils
import com.wyh.happyyousdk.utils.SharedPref
import com.wyhsdk.sharedPreferences.SharedPreference
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WalkhatonDetailActivity : AppCompatActivity() {

    lateinit var binding: ActivityWalkhatonDetailBinding
    var globalCount = 10
    var branchCount = 10
    var globalRequestCount = 0
    var branchRequestCount = 0
    val userRankingList = arrayListOf<GlobalRanking>()
    var allUserRankingList = arrayListOf<GlobalRanking>()
    var selectedText = "Global"

    val branchRankingList = arrayListOf<BranchRanking>()
    var allBranchRankingList = arrayListOf<BranchRanking>()
    lateinit var globalRankListAdapter: GlobalRankListAdapter
    lateinit var branchRankListAdapter: BranchRankListAdapter
    var isWinnerAnnounced = false
    var isEventEnded = false
    var isGlobalLast = false
    var isBranchLast = false
    var isShown = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_walkhaton_detail)
        SharedPref.init(this)
        SharedPreference.init(this)
        getRanking("Global")


        isWinnerAnnounced = intent.getBooleanExtra("isWinnerAnnounced", false)
        isEventEnded = intent.getBooleanExtra("isEventEnded", false)

        binding.la404Bear.setAnimationFromUrl(CommonUtils.getBaseUrlForAPI(this@WalkhatonDetailActivity) + SDKConstants.endPointForImages + "anim_bear_404.json")

        /*isWinnerAnnounced = false
        isEventEnded = true*/

        binding.ivInfo.setOnClickListener {
            if (selectedText.equals("Global", true)) {
                /*if (allUserRankingList.isNotEmpty()) {
                    if (allUserRankingList[0].communityId != null && allUserRankingList[0].communityId == -1) {
                        val intent = Intent(this, CommunitiesActivity::class.java)
                        startActivity(intent)
                    } else if (allUserRankingList[0].communityId == null || allUserRankingList[0].communityId == 0) {
                        Toast.makeText(this, "No tribe available", Toast.LENGTH_SHORT).show()
                    } else {
                        val intent = Intent(this, ChatActivity::class.java)
                        intent.putExtra("communityId", allUserRankingList[0].communityId)
                        startActivity(intent)
                    }
                }*/
            } else {
                if (allBranchRankingList.isNotEmpty()) {
                    Master.viewToolTip(
                        this@WalkhatonDetailActivity, binding.ivInfo,
                        "Number of employees in branch: ${allBranchRankingList[0].totalHeadCount}",
                        ViewTooltip.Position.BOTTOM,
                        ViewTooltip.ALIGN.END
                    )
                }
            }

        }


        binding.individualLayout.setOnClickListener {
            APILogs.activityTracker("A_WALKATHON_LB_INDIVIDUAL_BTN", this@WalkhatonDetailActivity)
            binding.allRankDespTv.text = "My All India Rank"
            binding.rankDespTv.text = "My Rank in \nBranch"
            userRankingList.clear()
            allUserRankingList.clear()
            selectedText = "Global"
            globalRequestCount = 0
            binding.individualLayout.background =
                resources.getDrawable(R.drawable.carousel_black_bg)
            binding.branchLayout.background = resources.getDrawable(R.drawable.branch_rank_bg)

            binding.selectedTotalSteps.text = resources.getString(R.string.total_steps)
            binding.selectedAvgSteps.text = resources.getString(R.string.avg_steps)

            binding.walkhatonRankTv.text = resources.getString(R.string.rank)
            binding.walkhatonNameTv.text = resources.getString(R.string.name)
            binding.walkhatonBranchTv.text = resources.getString(R.string.kli_branch)
            binding.walkhatonTotalStepsTv.text = resources.getString(R.string.steps)
            binding.clickHereTv.visibility = View.VISIBLE
            getRanking("Global")
        }

        binding.rankLayout.setOnClickListener {
            APILogs.activityTracker("A_WALKATHON_LB_RANK", this@WalkhatonDetailActivity)
            if (selectedText.equals("Global", true)) {
                startActivity(Intent(this, BranchDetailActivity::class.java))
            }
        }

        binding.walkhathonBackLayout.setOnClickListener {
            finish()
        }

        binding.syncDevice.setOnClickListener {
            APILogs.activityTracker("A_WALKATHON_LB_SYNC_DEVICE", this@WalkhatonDetailActivity)
            startActivity(Intent(this, ConnectApp::class.java))
        }

        binding.branchLayout.setOnClickListener {
            APILogs.activityTracker("A_WALKATHON_LB_BRANCH_BTN", this@WalkhatonDetailActivity)
            branchRankingList.clear()
            allBranchRankingList.clear()
            selectedText = "Branch"
            branchRequestCount = 0
            binding.individualLayout.background = resources.getDrawable(R.drawable.branch_rank_bg)
            binding.branchLayout.background = resources.getDrawable(R.drawable.carousel_black_bg)


            binding.selectedTotalSteps.text = resources.getString(R.string.branch_total_steps_tv)
            binding.selectedAvgSteps.text = resources.getString(R.string.avg_steps_tv)

            binding.allRankDespTv.text = "My Branch\nEnrollment(%)"
            binding.rankDespTv.text = "My Branch \nRank"




            binding.walkhatonRankTv.text = "Branch Rank"
            binding.walkhatonNameTv.text = "Branch Name"
            binding.walkhatonBranchTv.text = resources.getString(R.string.avgStepsOfBranch)
            binding.walkhatonTotalStepsTv.text = resources.getString(R.string.enrollment_per_branch)
            binding.clickHereTv.visibility = View.GONE

            getRanking("Branch")
        }


        binding.loadMoreBtn.setOnClickListener {
            if (selectedText.equals("Global", true)) {
                APILogs.activityTracker("A_WALKATHON_LB_GLM_BTN", this@WalkhatonDetailActivity)
                if (!isGlobalLast && allUserRankingList.size > 10) {

                    Log.d("AuthToken", "Count $globalCount")


                    if (allUserRankingList.isNotEmpty() && allUserRankingList.size != userRankingList.size) {
                        if (globalCount == allUserRankingList.count()) {
                            globalRequestCount += 50
                            Log.d("AuthToken", "Global API Called")
                            getRanking("Global")
                        } else {
                            globalLoadMore()
                        }

                    } else {
                        if (allUserRankingList.size == userRankingList.size) {
                            globalRequestCount += 50
                            Log.d("branchRequestCount 2", "$globalRequestCount")
                            getRanking("Global")
                        }
                    }
                } else {
                    val msg: String =
                        if (userRankingList.size > 0) "No More Data Available" else "No Data Available"
                    Toast.makeText(this@WalkhatonDetailActivity, msg, Toast.LENGTH_SHORT).show()
                }

            } else if (selectedText.equals("Branch", true)) {
                APILogs.activityTracker("A_WALKATHON_LB_BLM_BTN", this@WalkhatonDetailActivity)
                Log.d("AuthToken", "Count1 $branchCount")
                Log.d("AuthToken", "Count2 ${allBranchRankingList.count()}")
                Log.d("AuthToken", "Count3 ${allBranchRankingList.size}")
                Log.d("AuthToken", "Count4 ${branchRankingList.size}")

                if (!isBranchLast && allBranchRankingList.size > 10) {
                    if (allBranchRankingList.isNotEmpty() && allBranchRankingList.size != branchRankingList.size) {
                        if (branchCount >= (allBranchRankingList.count())) {
                            branchRequestCount += 50
                            Log.d("AuthToken", "Branch API Called")
                            Log.d("branchRequestCount", "$branchRequestCount")
                            getRanking("Branch")
                        } else {
                            branchLoadMore()
                        }
                    } else {
                        if (allBranchRankingList.size == branchRankingList.size) {
                            branchRequestCount += 50
                            Log.d("branchRequestCount 2", "$branchRequestCount")
                            getRanking("Branch")
                        }
                    }
                } else {
                    val msg: String =
                        if (branchRankingList.size > 0) "No More Data Available" else "No Data Available"
                    Toast.makeText(this@WalkhatonDetailActivity, msg, Toast.LENGTH_SHORT).show()
                }

            }

        }


    }

    private fun showWinnerPopup() {
        Log.d("data isWinnerAnnounced", "$isWinnerAnnounced")
        Log.d("data isEventEnded", "$isEventEnded")
        if (!isShown) {
            isShown = true
            if (isWinnerAnnounced) {
                binding.eventEndTv.visibility = View.VISIBLE
                APILogs.activityTracker("A_WALKATHON_LB_WINNER_POPUP", this@WalkhatonDetailActivity)
                val intent = Intent(this@WalkhatonDetailActivity, WinnerDialogActivity::class.java)
                startActivity(intent)
            } else if (isEventEnded) {
                binding.eventEndTv.visibility = View.VISIBLE
                APILogs.activityTracker(
                    "A_WALKATHON_LB_END_EVENT_POPUP",
                    this@WalkhatonDetailActivity
                )
                Master.showMessagePopUp(
                    this@WalkhatonDetailActivity,
                    "",
                    "Event is ended, Winners will announce shortly."
                )
            } else {
                binding.eventEndTv.visibility = View.GONE
            }
        }
    }

    private fun globalLoadMore() {
        try {
            userRankingList.clear()
            globalCount += 10

            if (globalCount >= allUserRankingList.size) {
                globalCount = allUserRankingList.size
            }

            if (allUserRankingList.isNotEmpty()) {
                for (i in 0..allUserRankingList.size) {
                    if (i < globalCount) {
                        if (i > 0) {
                            userRankingList.add(allUserRankingList[i])
                        }
                    }
                }
            }

            binding.rankList.layoutManager = LinearLayoutManager(this@WalkhatonDetailActivity)
            binding.rankList.hasFixedSize()
            globalRankListAdapter =
                GlobalRankListAdapter(this@WalkhatonDetailActivity, userRankingList, "global")
            binding.rankList.adapter = globalRankListAdapter
            binding.rankList.scrollToPosition(userRankingList.size - 1)
        } catch (e: Exception) {
            e.toString()
        }

    }

    fun branchLoadMore() {
        try {
            branchRankingList.clear()
            branchCount += 10

            if (branchCount >= allBranchRankingList.size) {
                branchCount = allBranchRankingList.size
            }

            allBranchRankingList.forEachIndexed { index, branchRanking ->
                if (index < branchCount) {
                    if (index > 0) {
                        branchRankingList.add(branchRanking)
                    }
                }
            }

            binding.rankList.layoutManager = LinearLayoutManager(this@WalkhatonDetailActivity)
            binding.rankList.hasFixedSize()
            branchRankListAdapter =
                BranchRankListAdapter(
                    this@WalkhatonDetailActivity,
                    branchRankingList
                )
            binding.rankList.adapter = branchRankListAdapter
            binding.rankList.scrollToPosition(branchRankingList.size - 1)
        } catch (e: Exception) {
            e.toString()
        }
    }

    fun getRanking(rankType: String) {
        try {
            var challengeRankRequest: ChallengeRankRequest
            CommonUtils.showProgressDialige(this)
            if (selectedText.equals("Global", true)) {
                challengeRankRequest = ChallengeRankRequest(rankType, globalRequestCount)

            } else {
                challengeRankRequest = ChallengeRankRequest(rankType, branchRequestCount)
            }
            Log.d("request", Gson().toJson(challengeRankRequest))
            val apiInterface = RetrofitHandler.apiInterface()
            apiInterface.getChallengeRanking(SharedPref.getAuthToken(), challengeRankRequest)
                .enqueue(object : Callback<ChallengeRankResponse> {
                    override fun onResponse(
                        call: Call<ChallengeRankResponse>,
                        response: Response<ChallengeRankResponse>
                    ) {
                        Log.d("request", Gson().toJson(response.code()))
                        Log.d("request", Gson().toJson(response.body()))
                        showWinnerPopup()

                        CommonUtils.dismissDialoge()
                        if (response.code() == 200 && response.isSuccessful && response.body() != null) {
                            if (response.body()!!.data != null) {

                                val challengeData: ChallengeRankData = response.body()!!.data;
                                if (challengeData.showbranch) {
                                    binding.teddyImg.visibility = View.VISIBLE
                                    binding.rankLayout.visibility = View.VISIBLE
                                    binding.branchLayout.visibility = View.VISIBLE
                                    if (!challengeData.showindividual && !selectedText.equals("Branch", ignoreCase = true)) {
                                        binding.branchLayout.performClick()
                                    }
                                } else {
                                    binding.teddyImg.visibility = View.INVISIBLE
                                    binding.rankLayout.visibility = View.INVISIBLE
                                    binding.branchLayout.visibility = View.GONE
                                }

                                if (challengeData.showindividual) {
                                    binding.individualLayout.visibility = View.VISIBLE
                                } else {
                                    binding.individualLayout.visibility = View.GONE
                                }

                                if (response.body()!!.data.totalSteps != null) {
                                    binding.totalStepsTv.text = response.body()!!.data.totalSteps
                                } else {
                                    binding.totalStepsTv.text = "0"

                                }

                                if (response.body()!!.data.totalParticipation != null) {
                                    binding.avgStepsTv.text =
                                        response.body()!!.data.totalParticipation
                                } else {
                                    binding.avgStepsTv.text = "0"

                                }
                            }
                            if (rankType.equals("Global", true)) {
                                binding.ivInfo.visibility = View.VISIBLE
                                binding.ivInfo.setImageDrawable(resources.getDrawable(R.drawable.chat_01))
                                if (response.body()!!.data.globalRankings != null) {
                                    binding.rankListLayout.visibility = View.VISIBLE
                                    binding.llNoRecordFound.visibility = View.GONE

                                    if (response.body()!!.data.globalRankings!!.totalSteps != null) {
                                        binding.selectedTotalStepsValue.text =
                                            response.body()!!.data.globalRankings!!.totalSteps

                                    } else {
                                        binding.selectedTotalStepsValue.text = "0"

                                    }

                                    if (response.body()!!.data.globalRankings!!.avgSteps != null) {
                                        binding.selectedAvgStepsValue.text =
                                            response.body()!!.data.globalRankings!!.avgSteps
                                    } else {
                                        binding.selectedAvgStepsValue.text = "0"

                                    }


                                    if (response.body()!!.data.globalRankings!!.userAllIndiaRank != null) {
                                        val allRank =
                                            response.body()!!.data.globalRankings!!.userAllIndiaRank.split(
                                                "/"
                                            )
                                        binding.allRankTv.text = allRank[0]
                                        binding.totalAllRankTv.text = "out of ${allRank[1]}"
                                    }

                                    if (response.body()!!.data.globalRankings!!.rank != null) {
                                        val globalRank =
                                            response.body()!!.data.globalRankings!!.rank.split("/")
                                        binding.totalAllRankTv.visibility = View.VISIBLE
                                        binding.rankTv.text = globalRank[0]
                                        binding.totalRankTv.text = "out of ${globalRank[1]}"
                                    }

                                    if (response.body()!!.data.globalRankings!!.userRankings.isNotEmpty()) {
                                        isGlobalLast = false
                                        allUserRankingList.addAll(response.body()!!.data.globalRankings!!.userRankings)

                                        if (allUserRankingList.isNotEmpty()) {
                                            binding.myRankTv.text = allUserRankingList[0].rank
                                            binding.myNameTv.text = allUserRankingList[0].userName
                                            binding.myBranchTv.text =
                                                allUserRankingList[0].branchName
                                            binding.myTotalStepsTv.text =
                                                allUserRankingList[0].steps
                                            binding.branchName.text =
                                                allUserRankingList[0].branchName
                                        }

                                        //allUserRankingList.removeAt(0)

                                        if (allUserRankingList.isNotEmpty()) {
                                            var startIndex: Int = globalRequestCount
                                            var endIndex: Int = startIndex + 10
                                            if (globalRequestCount == 0) {
                                                startIndex = 1
                                            }

                                            if (endIndex <= allUserRankingList.size) {
                                                val list =
                                                    allUserRankingList.subList(startIndex, endIndex)
                                                userRankingList.addAll(list)
                                            } else {
                                                endIndex = allUserRankingList.size
                                                val list =
                                                    allUserRankingList.subList(startIndex, endIndex)
                                                userRankingList.addAll(list)
                                            }

                                            /*for (i in 0..allUserRankingList.size) {
                                                if (i < 10) {
                                                    userRankingList.add(allUserRankingList[i])
                                                }
                                            }*/
                                        }

                                        /*if(response.body()!!.data.globalRankings!!.userRankings.isNotEmpty()){
                                            response.body()!!.data.globalRankings!!.userRankings.forEachIndexed { index, userRanking ->
                                                if(index < 10){
                                                    userRankingList.add(userRanking)
                                                }
                                            }
                                        }*/


                                        Log.d("AuthToken", "Array Count " + allUserRankingList.size)

                                        binding.rankList.layoutManager =
                                            LinearLayoutManager(this@WalkhatonDetailActivity)
                                        binding.rankList.hasFixedSize()
                                        globalRankListAdapter = GlobalRankListAdapter(
                                            this@WalkhatonDetailActivity,
                                            userRankingList,
                                            "global"
                                        )
                                        binding.rankList.adapter = globalRankListAdapter

                                        binding.rankList.scrollToPosition(userRankingList.size - 1)


                                        Log.d("AuthToken", userRankingList.size.toString())
                                    } else {
                                        isGlobalLast = true
                                    }
                                } else {
                                    binding.selectedTotalStepsValue.text = "0"
                                    binding.selectedAvgStepsValue.text = "0"
                                    binding.rankTv.text = "0"
                                    binding.totalRankTv.text = "out of 0"
                                    binding.allRankTv.text = "0"
                                    binding.totalAllRankTv.text = "out of 0"
                                    binding.selectedAvgStepsValue.text = "0"
                                    binding.rankListLayout.visibility = View.GONE
                                    binding.llNoRecordFound.visibility = View.VISIBLE

                                }
                            }

                            if (rankType.equals("Branch", true)) {
                                binding.ivInfo.visibility = View.VISIBLE
                                binding.ivInfo.setImageDrawable(resources.getDrawable(R.drawable.ic_info))
                                if (response.body()!!.data.branchRankings != null) {
                                    Log.d(
                                        "request",
                                        Gson().toJson(response.body()!!.data.branchRankings?.branchRankings?.size)
                                    )
                                    binding.rankListLayout.visibility = View.VISIBLE
                                    binding.llNoRecordFound.visibility = View.GONE



                                    if (response.body()!!.data.totalSteps != null) {
                                        binding.totalStepsTv.text =
                                            response.body()!!.data.totalSteps
                                    }

                                    if (response.body()!!.data.totalParticipation != null) {
                                        binding.avgStepsTv.text =
                                            response.body()!!.data.totalParticipation
                                    }

                                    if (response.body()!!.data.branchRankings!!.totalSteps != null) {
                                        binding.selectedTotalStepsValue.text =
                                            response.body()!!.data.branchRankings!!.totalSteps
                                    }

                                    if (response.body()!!.data.branchRankings!!.avgSteps != null) {
                                        binding.selectedAvgStepsValue.text =
                                            response.body()!!.data.branchRankings!!.avgSteps
                                    }

                                    if (response.body()!!.data.branchRankings!!.rank != null) {
                                        val branchRank =
                                            response.body()!!.data.branchRankings!!.rank.split("/")
                                        binding.rankTv.text = branchRank[0]
                                        binding.totalRankTv.text = "out of ${branchRank[1]}"
                                    }


                                    if (response.body()!!.data.branchRankings!!.enrollmentPercentage != null) {
                                        binding.allRankTv.text =
                                            "${response.body()!!.data.branchRankings!!.enrollmentPercentage}%"
                                        binding.totalAllRankTv.visibility = View.VISIBLE
                                        binding.totalAllRankTv.text =
                                            "out of ${response.body()!!.data.branchRankings!!.enrollmentCount}"
                                    }

                                    if (response.body()!!.data.branchRankings!!.branchRankings.isNotEmpty()) {
                                        isBranchLast = false
                                        allBranchRankingList.addAll(response.body()!!.data.branchRankings!!.branchRankings)
                                        Log.d("Branch size", "${allBranchRankingList.size}")
                                        if (allBranchRankingList.isNotEmpty()) {
                                            binding.myRankTv.text = allBranchRankingList[0].rank
                                            binding.myNameTv.text =
                                                allBranchRankingList[0].branchName
                                            binding.myBranchTv.text =
                                                allBranchRankingList[0].avgSteps
                                            binding.myTotalStepsTv.text =
                                                "${allBranchRankingList[0].participation}%"
                                        }

                                        //allBranchRankingList.removeAt(0)


                                        Log.d("Branch size2", "${allBranchRankingList.size}")
                                        if (allBranchRankingList.isNotEmpty()) {
                                            var startIndex: Int = branchRequestCount
                                            var endIndex: Int = startIndex + 10
                                            if (branchRequestCount == 0) {
                                                startIndex = 1
                                            }
                                            if (endIndex <= allBranchRankingList.size) {
                                                val list =
                                                    allBranchRankingList.subList(
                                                        startIndex,
                                                        endIndex
                                                    )
                                                branchRankingList.addAll(list)
                                            } else {
                                                endIndex = allBranchRankingList.size
                                                val list = allBranchRankingList.subList(
                                                    startIndex,
                                                    endIndex
                                                )
                                                branchRankingList.addAll(list)
                                            }
                                            /* response.body()!!.data.branchRankings!!.branchRankings.forEachIndexed { index, branchRanking ->
                                                 if(index < 10){
                                                     branchRankingList.add(branchRanking)
                                                 }
                                             }*/
                                        }

                                        binding.rankList.layoutManager =
                                            LinearLayoutManager(this@WalkhatonDetailActivity)
                                        binding.rankList.hasFixedSize()
                                        branchRankListAdapter = BranchRankListAdapter(
                                            this@WalkhatonDetailActivity,
                                            branchRankingList
                                        )
                                        binding.rankList.adapter = branchRankListAdapter
                                        binding.rankList.scrollToPosition(branchRankingList.size - 1)
                                    } else {
                                        isBranchLast = true
                                    }

                                } else {
                                    binding.allRankTv.text = "0%"
                                    binding.totalStepsTv.text = "0"
                                    binding.totalAllRankTv.visibility = View.GONE
                                    binding.rankListLayout.visibility = View.GONE
                                    binding.llNoRecordFound.visibility = View.VISIBLE

                                }
                            }
                        }
                    }

                    override fun onFailure(call: Call<ChallengeRankResponse>, t: Throwable) {
                        CommonUtils.dismissDialoge()
                    }

                })

        } catch (e: Exception) {
            CommonUtils.dismissDialoge()
            e.toString()
        }
    }

    fun getNumberWithSuffix(number: Int): String {
        return if (number % 100 in 11..13) {
            number.toString() + "th"
        } else when (number % 10) {
            1 -> number.toString() + "st"
            2 -> number.toString() + "nd"
            3 -> number.toString() + "rd"
            else -> number.toString() + "th"
        }
    }

    override fun onResume() {
        super.onResume()
        if (!SharedPreference.getGoogleFitConnection()) {
            binding.syncDevice.visibility = View.VISIBLE
        } else {
            binding.syncDevice.visibility = View.GONE
        }
    }


}