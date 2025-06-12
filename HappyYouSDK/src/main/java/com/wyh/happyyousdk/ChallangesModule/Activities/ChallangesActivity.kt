package com.wyh.happyyousdk.ChallangesModule.Activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.wyh.happyyousdk.APIEncryption.APIInterface
import com.wyh.happyyousdk.APIEncryption.APILogs
import com.wyh.happyyousdk.APIEncryption.RetrofitHandler
import com.wyh.happyyousdk.ChallangesModule.Adapter.ChallangesListAdapter
import com.wyh.happyyousdk.ChallangesModule.ClickInterface.ChallengeClick
import com.wyh.happyyousdk.ChallangesModule.Utils.TribeChallengeHelper
import com.wyh.happyyousdk.ChallangesModule.helperClass
import com.wyh.happyyousdk.R
import com.wyh.happyyousdk.dashboard.NewDashboardActivity
import com.wyh.happyyousdk.databinding.ActivityChallangesBinding
import com.wyh.happyyousdk.model.request.ChallengeCommunityRequest
import com.wyh.happyyousdk.model.request.CommonRequest
import com.wyh.happyyousdk.model.request.JoinChallengeRequest
import com.wyh.happyyousdk.model.response.*
import com.wyh.happyyousdk.utils.CommonUtils
import com.wyh.happyyousdk.utils.Constants
import com.wyh.happyyousdk.utils.SharedPref
import com.wyhsdk.sharedPreferences.SharedPreference
/*import kotlinx.android.synthetic.main.activity_challanges.**/
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChallangesActivity : AppCompatActivity(), ChallengeClick {

    lateinit var challengesList: ArrayList<challengesData>
    lateinit var apiInterface: APIInterface
    lateinit var context: Context;
    private lateinit var binding: ActivityChallangesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_challanges)
        context = this
        SharedPref.init(context)
        SharedPreference.init(context)
        init()
    }

    override fun onResume() {
        super.onResume()
        getChallenges(false, "", 0)

    }


    fun init() {
        try {
            apiInterface = RetrofitHandler.getRetrofitInstance().create(APIInterface::class.java)
            binding = DataBindingUtil.setContentView(this,R.layout.activity_challanges)
            binding.challengesBack.text = "Challenges"
            binding.ivHome.setOnClickListener {
                val intent = Intent(this, NewDashboardActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
                finish()
            }

            binding.challengesBackLayout.setOnClickListener {
                finish()
            }

            val customObj = JSONObject()
            try {
                customObj.put("PAGE_ID", "Challenges")
            } catch (e: JSONException) {
                throw RuntimeException(e)
            }
            

        } catch (e: Exception) {
            e.toString()
        }
    }

    override fun onClickPerform(challengeName: String, position: Int) {
        if(!challengesList.elementAt(position).ChallengeType.equals("Tribe", true)){
            if(challengesList.elementAt(position).IsUserEnrolled == "1"){
                val intent = Intent(this@ChallangesActivity, ChallengeDetailActivity::class.java)
                intent.putExtra("challengeID", challengeName)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                intent.putExtra("position", position.toString())
                startActivity(intent)
            }

        }
    }



    override fun joinNowClick(data:challengesData, challengeID: String, position: Int, challengeType: String, communityID: Int, recyclerView: RecyclerView) {
        Log.d("joinNowClick", challengeType)
        if (!challengeType.equals("tribe",true)) {
            joinChallenge(challengeID, position,communityID,challengeType,this,recyclerView)
        }

    }

    private fun getChallenges(cameFromJoin: Boolean, challenegID: String, position: Int) {
        try {
            CommonUtils.showProgressDialige(this)
            apiInterface.getChallenges(SharedPref.getAuthToken()).enqueue(object : Callback<GetChallengesResponse> {
                    override fun onResponse(call: Call<GetChallengesResponse>, response: Response<GetChallengesResponse>) {
                        CommonUtils.dismissDialoge()
                        response.let {
                            if (response.body() != null) {
                                if(response.body()!!.success || response.code() == 200 || response.body()!!.data.isEmpty()){
                                    response.body()?.let {
                                        challengesList = response.body()!!.data
                                        val joinList =  ArrayList<challengesData>()
                                        val unJoinList =  ArrayList<challengesData>()

                                        helperClass.listChallengeId.clear()
                                        challengesList.forEach { e ->

                                            if (e.IsUserEnrolled == "1") {
                                                if(!e.ChallengeType.equals("tribe",true) && !e.ChallengeType.equals("Steps",true)) {
                                                    helperClass.listChallengeId.add(e.ChallengeId)
                                                }
                                                joinList.add(e)
                                            }else{
                                                unJoinList.add(e)
                                            }
                                        }
                                        challengesList.clear();
                                        challengesList.addAll(joinList)
                                        challengesList.addAll(unJoinList)

                                        val challangesListAdapter = ChallangesListAdapter(this@ChallangesActivity,
                                            this@ChallangesActivity, challengesList,binding.challangesRecyclerView)
                                        binding.challangesRecyclerView.layoutManager = GridLayoutManager(this@ChallangesActivity, 3)
                                        binding.challangesRecyclerView.hasFixedSize()
                                        binding.challangesRecyclerView.adapter = challangesListAdapter
                                        SharedPref.putChallengeCount(helperClass.listChallengeId.size)
                                        if(cameFromJoin){
                                            onClickPerform(challenegID, position)
                                        }
                                    }
                                }

                            }
                        }

                    }

                    override fun onFailure(call: Call<GetChallengesResponse>, t: Throwable) {
                        APILogs.sendLogs(call.request().url().toString(),t.message!!,"onFailure",this@ChallangesActivity)
                        CommonUtils.dismissDialoge()

                    }

                })

        } catch (e: Exception) {
            APILogs.sendLogs(Constants.EXCEPTION,e.message!!,"Exception",this@ChallangesActivity)
            CommonUtils.dismissDialoge()
            e.toString()
        }
    }

    fun getChallengeDetails(context: Context,ChallengeID: String, data: challengesData, recyclerView: RecyclerView){
        if(data.IsUserEnrolled== "1" && data.IsStarted == 1) {
            val intent = Intent(context,NewTribeChallengeDashboard::class.java)
            intent.putExtra("challengeID", ChallengeID)
            context.startActivity(intent)
        }else{
            getChallengeApi(context, ChallengeID, false,data.IsUserEnrolled,recyclerView,data.IsStarted)
        }
    }

    fun getChallengeApi(context: Context,ChallengeID: String, cameFromInfo: Boolean, isUserEnrolled : String,recyclerView: RecyclerView, isStarted: Int){
        try{
            CommonUtils.showProgressDialige(context)
            val commonRequest = CommonRequest(ChallengeID)
            val apiInterface = RetrofitHandler.getRetrofitInstance().create(APIInterface::class.java)
            apiInterface.challengeActivity(SharedPref.getAuthToken(),commonRequest).enqueue(object : Callback<ChallengeActivityResponse>{
                override fun onResponse(call: Call<ChallengeActivityResponse>, response: Response<ChallengeActivityResponse>) {
                    CommonUtils.dismissDialoge()
                    try{
                        if(response.code() == 200 && response.isSuccessful){
                            if(response.body() != null){
                                if(cameFromInfo){
                                    TribeChallengeHelper.challengeDialog(context, ChallengeID, response.body()!!.data.challengeActivityDetails[0], cameFromInfo, isUserEnrolled,recyclerView,"challenges")
                                }else{
                                    if(SharedPref.getChallengeStarted() && isUserEnrolled.equals("1") && isStarted == 0){
                                        TribeChallengeHelper.showConcernInfoLayout(context,ChallengeID)
                                    }else{
                                        TribeChallengeHelper.challengeDialog(context, ChallengeID, response.body()!!.data.challengeActivityDetails[0], cameFromInfo, isUserEnrolled,recyclerView,"challenges")
                                    }
                                }


                            }
                        }
                    }catch (e: Exception){
                        e.toString()
                    }


                }

                override fun onFailure(call: Call<ChallengeActivityResponse>, t: Throwable) {
                    CommonUtils.dismissDialoge()
                }

            })

        }catch (e: Exception){
            CommonUtils.dismissDialoge()
            e.toString()
        }
    }


     fun joinChallenge(challenegID: String, position: Int, communityID: Int, challengeType: String,context: Context,recyclerView: RecyclerView) {
        try {
            CommonUtils.showProgressDialige(this)
            val commonRequest: JoinChallengeRequest = JoinChallengeRequest(challenegID,communityID)
            apiInterface = RetrofitHandler.getRetrofitInstance().create(APIInterface::class.java)
            apiInterface.joinChallenge(SharedPref.getAuthToken(), commonRequest).enqueue(object : Callback<JoinChallengeResponse> {
                    override fun onResponse(call: Call<JoinChallengeResponse>, response: Response<JoinChallengeResponse>) {
                        response.body()?.success.let {
                            if (response.body()!!.success) {
                                if(challengeType.equals("tribe",true)){
                                    if(SharedPref.getChallengeStarted()){
                                        CommonUtils.dismissDialoge()
                                        TribeChallengeHelper.showConcernInfoLayout(context,challenegID)
                                    }else{
                                        CommonUtils.dismissDialoge()
                                        getChallengesForTribe(context,recyclerView)

                                    }
                                }else{
                                    CommonUtils.dismissDialoge()
                                    getChallenges(true, challenegID, position)
                                }
                            }
                        }
                    }

                    override fun onFailure(call: Call<JoinChallengeResponse>, t: Throwable) {
                        APILogs.sendLogs(call.request().url().toString(),t.message!!,"onFailure",this@ChallangesActivity)
                        CommonUtils.dismissDialoge()
                    }
                })
        } catch (e: Exception) {
            APILogs.sendLogs(Constants.EXCEPTION,e.message!!,"Exception",this@ChallangesActivity)
            CommonUtils.dismissDialoge()
            e.toString()
        }
    }

    fun getChallengesForTribe(context: Context,recyclerView: RecyclerView){
        try{
            CommonUtils.showProgressDialige(context)
            apiInterface = RetrofitHandler.getRetrofitInstance().create(APIInterface::class.java)
            apiInterface.getChallenges(SharedPref.getAuthToken()).enqueue(object : Callback<GetChallengesResponse>{
                override fun onResponse(call: Call<GetChallengesResponse>, response: Response<GetChallengesResponse>) {
                    if(response.body() !=null){
                        CommonUtils.dismissDialoge()
                        if(response.code() == 200 && response.body()!!.success){
                            challengesList = response.body()!!.data
                            val challangesListAdapter = ChallangesListAdapter(context, object : ChallengeClick{
                                override fun onClickPerform(challengeName: String, position: Int) {

                                }

                                override fun joinNowClick(data:challengesData, challengeID: String, position: Int, challengeType: String, communityID: Int,recyclerView: RecyclerView) {
                                    Log.d("joinNowClick", challengeType)
                                    if (!challengeType.equals("tribe",true)) {
                                        joinChallenge(challengeID, position,communityID,challengeType,context,recyclerView)
                                    }
                                }

                            }, challengesList,recyclerView)
                            recyclerView.layoutManager = GridLayoutManager(context, 3)
                            recyclerView.hasFixedSize()
                            recyclerView.adapter = challangesListAdapter
                        }
                    }
                }

                override fun onFailure(call: Call<GetChallengesResponse>, t: Throwable) {
                    CommonUtils.showProgressDialige(context)

                }

            })


        }catch (e: Exception){
            CommonUtils.showProgressDialige(context)
            e.toString()
        }
    }


}