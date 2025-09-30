package com.wyh.happyyousdk.dashboard

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.gson.Gson
import com.wyh.happyyousdk.APIEncryption.APILogs
import com.wyh.happyyousdk.ChallangesModule.helperClass.prepareFilePart
import com.wyh.happyyousdk.R
import com.wyh.happyyousdk.WebActivity
import com.wyh.happyyousdk.WellBeingActivity
import com.wyh.happyyousdk.absorb.HealthHacksActivity
import com.wyh.happyyousdk.absorb.QuickReadDashboard
import com.wyh.happyyousdk.contacts.ContactsActivityNew
import com.wyh.happyyousdk.dashboard.helper.NewDashboardHelper
import com.wyh.happyyousdk.databinding.WinningsDialogeLayoutBinding
import com.wyh.happyyousdk.diary.AddDiaryActivity
import com.wyh.happyyousdk.ehr.EhrActivity
import com.wyh.happyyousdk.hra.HRAAnalysisActivity
import com.wyh.happyyousdk.hra.HRAQuestionsActivity
import com.wyh.happyyousdk.ira.IRAAnalysisActivity
import com.wyh.happyyousdk.ira.IraActivity
import com.wyh.happyyousdk.model.CommonSuccessResponse
import com.wyh.happyyousdk.model.PopUpShowModel
import com.wyh.happyyousdk.model.request.ehr.FileData
import com.wyh.happyyousdk.model.request.rewards.AppFeedbackRequest
import com.wyh.happyyousdk.model.request.rewards.StartRewardsActivityRequest
import com.wyh.happyyousdk.model.response.RewardsData
import com.wyh.happyyousdk.model.response.ScratchAndWinResponse
import com.wyh.happyyousdk.model.response.TopUpData
import com.wyh.happyyousdk.model.response.getAnalysis.GetAnalysisResponse
import com.wyh.happyyousdk.model.response.ira.IRAHealthScoreResponse
import com.wyh.happyyousdk.network.ApiClientWyh
import com.wyh.happyyousdk.network.ApiInterfaceWyh
import com.wyh.happyyousdk.quizathon.QuizathonViewAllActivity
import com.wyh.happyyousdk.rewards.MoreTopUpsActivity
import com.wyh.happyyousdk.rewards.RewardsActivity
import com.wyh.happyyousdk.syncDevice.ConnectApp
import com.wyh.happyyousdk.syncDevice.SyncDeviceActivity
import com.wyh.happyyousdk.trends.TrendsActivity
import com.wyh.happyyousdk.unwind.UnwindActivity
import com.wyh.happyyousdk.utils.CommonUtils
import com.wyh.happyyousdk.utils.Constants
import com.wyh.happyyousdk.utils.Constants.TAG_REWARD_EVENT
import com.wyh.happyyousdk.utils.Constants.TAG_TOP_UP_EVENT
import com.wyh.happyyousdk.utils.SharedPref
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.Locale
import java.util.concurrent.TimeUnit

object DashboardHelper {

    var journalText = ""
    var imageName = ""
    var fileDataList = java.util.ArrayList<FileData>()
    var eventType = ""
    lateinit var topdata : TopUpData
    lateinit var rewardsData : RewardsData


    fun startRewardActivity(activity: Activity,data : RewardsData, binding : WinningsDialogeLayoutBinding,alertDialog: AlertDialog){
        try{
            CommonUtils.showProgressDialige(activity)
            val apiInterfaceWyh = ApiClientWyh.getClient(CommonUtils.getBaseUrlForAPI(activity)).create(ApiInterfaceWyh::class.java)
            val rewardsRequest = StartRewardsActivityRequest(data.activityID, Constants.TAG_REWARD_EVENT)
            Log.d("AuthToken", "startRewardActivityReq: " + Gson().toJson(rewardsRequest) )

            apiInterfaceWyh.startRewardsActivity(SharedPref.getAuthToken(),rewardsRequest).enqueue(object : Callback<CommonSuccessResponse>{
                override fun onResponse(call: Call<CommonSuccessResponse>, response: Response<CommonSuccessResponse>) {
                    Log.d("AuthToken", "startRewardActivityRES: " + Gson().toJson(response.body()) + ", code: " + response.code())
                    CommonUtils.dismissDialoge()
                    if(response.isSuccessful && response.code() == 200){
                        binding.winningsDialogProgressbar.progress = data.progressPercentage
                        binding.challengeDialogStartBtn.text = activity.getText(R.string.complete)
                        if(data.eventType.equals("JournalUpload",true)){
                            binding.winningsJournalDespLayout.visibility = View.VISIBLE
                        } else if(data.redirectTo != null){
                            alertDialog.dismiss()
                            rewardRedirection(data,activity)
                        } else{
                            binding.winningsJournalDespLayout.visibility = View.GONE
                        }

                    }else{
                        APILogs.activityTracker("A_111_${response.code()}_${SharedPref.getEncryptedMobileNo()}",activity)
                    }
                }

                override fun onFailure(call: Call<CommonSuccessResponse>, t: Throwable) {
                    APILogs.activityTracker("A_111_Failed_${SharedPref.getEncryptedMobileNo()}",activity)
                    CommonUtils.dismissDialoge()

                }

            })

        }catch (e:Exception){
            CommonUtils.dismissDialoge()
            e.toString()
        }
    }

    fun startTopUpActivity(activity: Activity,data: TopUpData,binding : WinningsDialogeLayoutBinding){
        try{
            CommonUtils.showProgressDialige(activity)
            val apiInterfaceWyh = ApiClientWyh.getClient(CommonUtils.getBaseUrlForAPI(activity)).create(ApiInterfaceWyh::class.java)
            val rewardsRequest = StartRewardsActivityRequest(data.topUpID, Constants.TAG_TOP_UP_EVENT)
            Log.d("AuthToken", "startTopUpActivityReq: " + Gson().toJson(rewardsRequest) )
            apiInterfaceWyh.startRewardsActivity(SharedPref.getAuthToken(),rewardsRequest).enqueue(object : Callback<CommonSuccessResponse>{
                override fun onResponse(call: Call<CommonSuccessResponse>, response: Response<CommonSuccessResponse>) {
                    CommonUtils.dismissDialoge()
                    Log.d("AuthToken", "startTopUpActivityRES: " + Gson().toJson(response.body()) + ", code: " + response.code())
                    if(response.isSuccessful && response.code() == 200){
                        binding.winningsDialogProgressbar.progress = data.progressPercentage
                        binding.challengeDialogStartBtn.text = activity.getText(R.string.complete)
                        if(data.eventType.equals("JournalUpload",true) || data.eventType.equals("Feedback",true)){
                            binding.winningsJournalDespLayout.visibility = View.VISIBLE
                        }else if(data.redirectTo != null){
                            topUpRedirection(data,activity)
                        }
                        else {
                            binding.winningsJournalDespLayout.visibility = View.GONE
                        }

                    }else{

                    }
                }

                override fun onFailure(call: Call<CommonSuccessResponse>, t: Throwable) {
                    CommonUtils.dismissDialoge()

                }

            })

        }catch (e:Exception){
            CommonUtils.dismissDialoge()
            e.toString()
        }
    }

    fun completFeedbackTopUp(feedback: String, activity: Activity){
        try{
            CommonUtils.showProgressDialige(activity)
            val apiInterfaceWyh = ApiClientWyh.getClient(CommonUtils.getBaseUrlForAPI(activity)).create(ApiInterfaceWyh::class.java)
            val feedbackRequest = AppFeedbackRequest(feedback)
            apiInterfaceWyh.topUpFeedback(SharedPref.getAuthToken(),feedbackRequest).enqueue(object : Callback<CommonSuccessResponse>{
                override fun onResponse(call: Call<CommonSuccessResponse>, response: Response<CommonSuccessResponse>) {
                    CommonUtils.dismissDialoge()
                    if(response.isSuccessful && response.code() == 200){
                        if(NudgeDialogue.alertDialog.isShowing)
                            NudgeDialogue.alertDialog.dismiss()
                        Toast.makeText(activity, "Feedback sent successfully", Toast.LENGTH_SHORT).show()
                        NewDashboardHelper.popUpShowModels.clear()
                        if (response.body()!!.rewards != null && response.body()!!.rewards.bonusRewards != null) {
                            NewDashboardHelper.popUpShowModels.add(PopUpShowModel(Constants.RewardsBounce, response.body()!!.rewards.bonusRewards))
                        }
                        if (response.body()!!.rewards != null && response.body()!!.rewards.reward != null) {
                            NewDashboardHelper.popUpShowModels.add(PopUpShowModel(Constants.Rewards, response.body()!!.rewards.reward))
                        }
                        if (response.body()!!.enGTokens != null && response.body()!!.enGTokens.bonusTokens != null) {
                            NewDashboardHelper.popUpShowModels.add(PopUpShowModel(Constants.TokenStampBounce, response.body()!!.enGTokens.bonusTokens))
                        }

                        NewDashboardHelper.showRewardsPopupDialogBox(activity,NewDashboardHelper.scratchListener,NewDashboardHelper.feedbackResponseData)


                    }
                }

                override fun onFailure(call: Call<CommonSuccessResponse>, t: Throwable) {
                    CommonUtils.dismissDialoge()

                }

            })
        }catch (e: Exception){
            CommonUtils.dismissDialoge()
            e.toString()
        }

    }

    fun completeRewardsActivity(activity: Activity, parts: MutableList<MultipartBody.Part?>, data : RewardsData){
        try{
            CommonUtils.showProgressDialige(activity)
            var body : RequestBody
            val client = OkHttpClient().newBuilder()
                .readTimeout(120, TimeUnit.SECONDS)
                .connectTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .certificatePinner(ApiClientWyh.getCertificatePinner())
                .build()

            if(data.eventType.equals("journal",true)){
                body = MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("ActivityUploads","")
                    .addFormDataPart("eventId", data.activityID.toString())
                    .addFormDataPart("eventType", data.eventType)
                    .addFormDataPart("eventName", data.activityTag)
                    .addFormDataPart("yesNo", "")
                    .addFormDataPart("journalContent", journalText)
                    .addFormDataPart("eventCategory", TAG_REWARD_EVENT)
                    .build();
            }else{
                body = MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addPart(parts[0])
                    .addFormDataPart("eventId", data.activityID.toString())
                    .addFormDataPart("eventType", data.eventType)
                    .addFormDataPart("eventName", data.activityTag)
                    .addFormDataPart("yesNo", "")
                    .addFormDataPart("journalContent", journalText)
                    .addFormDataPart("eventCategory", TAG_REWARD_EVENT)
                    .build();
            }



            val request = Request.Builder()
                .url(CommonUtils.getBaseUrlForAPI(activity) + "Rewards/EarnRewards")
                .method("POST", body)
                .addHeader("Authorization", SharedPref.getAuthToken())
                .build()

            client.newCall(request).enqueue(object : okhttp3.Callback{
                override fun onFailure(call: okhttp3.Call, e: IOException) {
                    APILogs.activityTracker("A_112_Failed_REWARD_${SharedPref.getEncryptedMobileNo()}",activity)
                    CommonUtils.dismissDialoge()
                }

                override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                    CommonUtils.dismissDialoge()
                    fileDataList.clear()
                    if(response.isSuccessful && response.code() == 200){
                        val commonSuccessResponse = Gson().fromJson(response.body()!!.string(), CommonSuccessResponse::class.java)
                        if(NudgeDialogue.alertDialog.isShowing)
                            NudgeDialogue.alertDialog.dismiss()
                        activity.runOnUiThread(Runnable {
                            if (commonSuccessResponse.rewards != null && commonSuccessResponse.rewards.reward != null) {
                                Toast.makeText(activity, activity.resources.getString(R.string.points_earned_msg), Toast.LENGTH_SHORT).show()
                                //getRewardsDashboardData()
                                NewDashboardHelper.popUpShowModels.add(PopUpShowModel(Constants.Rewards, commonSuccessResponse.rewards.reward))
                            }
                            if (commonSuccessResponse.rewards != null && commonSuccessResponse.rewards.bonusRewards != null) {
                                Toast.makeText(
                                    activity,
                                    activity.resources.getString(R.string.points_earned_msg),
                                    Toast.LENGTH_SHORT
                                ).show()
                                //getRewardsDashboardData()
                                NewDashboardHelper.popUpShowModels.add(
                                    PopUpShowModel(
                                        Constants.RewardsBounce,
                                        commonSuccessResponse.rewards.bonusRewards
                                    )
                                )
                            }
                            Log.d("data", "" + commonSuccessResponse.rewards.reward)
                            NewDashboardHelper.showRewardsPopupDialogBox(activity,NewDashboardHelper.scratchListener,NewDashboardHelper.feedbackResponseData)
                        })
                    }else{
                        APILogs.activityTracker("A_112_${response.code()}_REWARD_${SharedPref.getEncryptedMobileNo()}",activity)
                    }
                }

            })
        }catch (e: Exception){
            e.toString()
        }
    }

    fun completeTopUpActivity(activity: Activity, parts: MutableList<MultipartBody.Part?>, data : TopUpData){
        try{
            CommonUtils.showProgressDialige(activity)
            val client = OkHttpClient().newBuilder()
                .readTimeout(120, TimeUnit.SECONDS)
                .connectTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .certificatePinner(ApiClientWyh.getCertificatePinner())
                .build()

            val body : RequestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
                .addPart(parts[0])
                .addFormDataPart("eventId", data.topUpID.toString())
                .addFormDataPart("eventType", data.eventType)
                .addFormDataPart("eventName", data.topUpTag)
                .addFormDataPart("yesNo", "")
                .addFormDataPart("journalContent", journalText)
                .addFormDataPart("eventCategory", TAG_TOP_UP_EVENT)
                .build();

            val request = Request.Builder()
                .url(CommonUtils.getBaseUrlForAPI(activity) + "Rewards/EarnRewards")
                .method("POST", body)
                .addHeader("Authorization", SharedPref.getAuthToken())
                .build()

            client.newCall(request).enqueue(object : okhttp3.Callback{
                override fun onFailure(call: okhttp3.Call, e: IOException) {
                    APILogs.activityTracker("A_113_Failed_TOPUP_${SharedPref.getEncryptedMobileNo()}",activity)

                    CommonUtils.dismissDialoge()
                }

                override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                    CommonUtils.dismissDialoge()
                     if(response.isSuccessful && response.code() == 200){

                        CommonUtils.dismissDialoge()
                        fileDataList.clear()
                        if(response.isSuccessful && response.code() == 200){
                            val commonSuccessResponse = Gson().fromJson(response.body()!!.string(), CommonSuccessResponse::class.java)
                            if(NudgeDialogue.alertDialog.isShowing)
                                NudgeDialogue.alertDialog.dismiss()
                            activity.runOnUiThread(Runnable {
                                if (commonSuccessResponse.rewards != null && commonSuccessResponse.rewards.reward != null) {
                                    Toast.makeText(activity, activity.resources.getString(R.string.points_earned_msg), Toast.LENGTH_SHORT).show()
                                    NewDashboardHelper.popUpShowModels.clear()
                                    NewDashboardHelper.popUpShowModels.add(PopUpShowModel(Constants.Rewards, commonSuccessResponse.rewards.reward))
                                }
                                if (commonSuccessResponse.rewards != null && commonSuccessResponse.rewards.bonusRewards != null) {
                                    Toast.makeText(activity, activity.resources.getString(R.string.points_earned_msg), Toast.LENGTH_SHORT).show()
                                    NewDashboardHelper.popUpShowModels.clear()
                                    NewDashboardHelper.popUpShowModels.add(PopUpShowModel(Constants.RewardsBounce,commonSuccessResponse.rewards.bonusRewards))
                                }
                                Log.d("data", "" + commonSuccessResponse.rewards.reward)
                                NewDashboardHelper.showRewardsPopupDialogBox(activity,NewDashboardHelper.scratchListener,NewDashboardHelper.feedbackResponseData)
                            })
                        }else{
                            APILogs.activityTracker("A_113_${response.code()}_TOPUP_${SharedPref.getEncryptedMobileNo()}",activity)

                        }
                    }
                }

            })
        }catch (e: Exception){
            e.toString()
        }
    }




    fun saveImage(imageURI: Uri?, context : Activity) {
        val parts: MutableList<MultipartBody.Part?> = ArrayList()
        val fileData = FileData()
        fileData.path = CommonUtils.getRealPathFromURI(imageURI, context)
        val fileDataNew = FileData()
        val file1 = CommonUtils.compressImageToJPEG(context, imageURI)
        fileDataNew.path = file1.path
        fileDataNew.mimeType = "application/png"
        imageName = file1.name
        if (file1.length() > 5000000) {
            Toast.makeText(
                context,
                "File(s) size should not be greater than 5MB",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            fileDataList.add(fileDataNew)
        }
        if (fileDataList.size > 0) {
            if(eventType.equals("Rewards",true)){
                rewardsData = NewDashboardHelper.rewardsData!!
                if(rewardsData.eventType.equals("upload",true)){
                    parts.add(prepareFilePart("ActivityUploads", fileDataList[0].path)!!)
                }else if(rewardsData.eventType.equals("JournalUpload",true)){
                    parts.add(prepareFilePart("JournalUpload", fileDataList[0].path)!!)
                }
                completeRewardsActivity(context,parts,rewardsData)
            }else{
                topdata = NewDashboardHelper.topUpData!!
                if(topdata.eventType.equals("JournalUpload",true)){
                    parts.add(prepareFilePart("JournalUpload",fileDataList[0].path)!!)
                }else if(topdata.eventType.equals("upload",true)){
                    parts.add(prepareFilePart("ActivityUploads",fileDataList[0].path)!!)
                }
                completeTopUpActivity(context,parts,topdata)

            }
        }
        Log.d("FileData", Gson().toJson(fileDataList))
    }

    fun topUpRedirection(topUp: TopUpData, acivity : Activity) {
        if (topUp.redirectTo != null) {
            when (topUp.redirectTo.lowercase(Locale.getDefault())) {
                "invite" -> {
                    val intent = Intent(acivity, ContactsActivityNew::class.java)
                    intent.putExtra("comingFrom", "share")
                    intent.putExtra("isFromHRA", true)
                    acivity.startActivity(intent)
                }
                "quiz" -> {
                    val intent = Intent(acivity, QuizathonViewAllActivity::class.java).apply {
                        putExtra("type", "quiz")
                        putExtra("quiz_cat", "All")
                        putExtra("name", "Play and Learn")
                    }

                    acivity.startActivityForResult(intent, 2121)
                }
                "happy footprint" -> {
                    val intent = Intent(acivity, TrendsActivity::class.java)
                    intent.putExtra("activityType", Constants.STEPS)
                    acivity.startActivity(intent)
                }
            }
        }
    }

    fun startScractWin(activity: Activity,data: TopUpData){
        try{
            CommonUtils.showProgressDialige(activity)
            val apiInterfaceWyh = ApiClientWyh.getClient(CommonUtils.getBaseUrlForAPI(activity)).create(ApiInterfaceWyh::class.java)
            val request = StartRewardsActivityRequest(data.topUpID,TAG_TOP_UP_EVENT)
            apiInterfaceWyh.startScratchAndWin(SharedPref.getAuthToken(),request).enqueue(object : Callback<ScratchAndWinResponse>{
                override fun onResponse(call: Call<ScratchAndWinResponse>, response: Response<ScratchAndWinResponse>) {
                    CommonUtils.dismissDialoge()
                    if(response.isSuccessful && response.code() == 200){
                        MoreTopUpsActivity().showScratchAndWinPopup(response.body()!!.data.toString(),activity)
                        Toast.makeText(activity, "Top up has been started", Toast.LENGTH_SHORT).show()

                    }
                }

                override fun onFailure(call: Call<ScratchAndWinResponse>, t: Throwable) {
                    CommonUtils.dismissDialoge()

                }

            })


        }catch (e: Exception){
            e.toString()
        }
    }

     fun rewardRedirection(data: RewardsData, activity: Activity) {
        if (data.redirectTo != null) {
            when (data.redirectTo.lowercase(Locale.getDefault())) {
                "hra" -> {
                    val intent: Intent
                    val getAnalysisResponse = Gson().fromJson(SharedPref.getHRAAnalysis(), GetAnalysisResponse::class.java)
                    intent =
                        if (getAnalysisResponse != null && getAnalysisResponse.analysisData != null && getAnalysisResponse.analysisData.score > 0) {
                            Intent(activity, HRAAnalysisActivity::class.java)
                        } else {
                            Intent(activity, HRAQuestionsActivity::class.java)
                        }
                    activity.startActivity(intent)
                }
                "ira" -> {
                    val iraHealthScoreResponse = Gson().fromJson(SharedPref.getIRAHealthData(), IRAHealthScoreResponse::class.java)
                    var healthScore = ""
                    if (iraHealthScoreResponse != null && iraHealthScoreResponse.iraHealthScoreData != null) healthScore =
                        iraHealthScoreResponse.iraHealthScoreData.playSports
                    if (healthScore != "0" && healthScore != "") {
                        val intent = Intent(activity, IRAAnalysisActivity::class.java)
                        activity.startActivity(intent)

                    } else {
                        val intent = Intent(activity, IraActivity::class.java)
                        intent.putExtra("from", Constants.IRA_STATUS_COMPLETED)
                        activity.startActivity(intent)

                    }
                }
                "blogread" -> {
                    val intent1 = Intent(activity, QuickReadDashboard::class.java)
                    activity.startActivity(intent1)
                }
                "wellbeing" -> {
                    val intent1 = Intent(activity, WellBeingActivity::class.java)
                    activity.startActivity(intent1)
                }
                "waterintake" -> {
                    val intent2 = Intent(activity, TrendsActivity::class.java)
                    intent2.putExtra("activityType", Constants.WATER)
                    activity.startActivity(intent2)
                }
                "eng" -> {
                    val intent2 = Intent(activity, RewardsActivity::class.java)
                    intent2.putExtra("currentIndex", 1)
                    activity.startActivity(intent2)
                }
                "meditate" -> {
                    val intent3 = Intent(activity, TrendsActivity::class.java)
                    intent3.putExtra("activityType", Constants.MEDITATION)
                    activity.startActivity(intent3)
                }
                "quiz" -> {
                    val intent4 = Intent(activity, QuizathonViewAllActivity::class.java).apply {
                        putExtra("type", "quiz")
                        putExtra("quiz_cat", "All")
                        putExtra("name", "Play and Learn")
                    }
                    activity.startActivity(intent4)
                }
                "webinars", "healthtv" -> {
                    val intent5 = Intent(activity, HealthHacksActivity::class.java)
                    intent5.putExtra("cameFrom", "rewards")
                    activity.startActivity(intent5)
                }
                "ehr" -> {
                    val intent6 = Intent(activity, EhrActivity::class.java)
                    activity.startActivity(intent6)
                }
                "syncdevice" -> {
                    val intent7 = Intent(activity, ConnectApp::class.java)
                    activity.startActivity(intent7)
                }
                "invite" -> {
                    val intent7 = Intent(activity, ContactsActivityNew::class.java)
                    intent7.putExtra("isFromHRA", true)
                    intent7.putExtra("comingFrom", "share")
                    activity.startActivity(intent7)
                }
                "journal" -> {
                    val intent7 = Intent(activity, AddDiaryActivity::class.java)
                    intent7.putExtra("title", "")
                    intent7.putExtra("content", "")
                    intent7.putExtra("date", "")
                    intent7.putExtra("came_from", "add")
                    intent7.putExtra("id", 0)
                    intent7.putExtra("imagePath", "")
                    activity.startActivity(intent7)
                }
                "unwind" -> {
                    val intent7 = Intent(activity, UnwindActivity::class.java)
                    intent7.putExtra("comingFrom", "dashboard")
                    activity.startActivity(intent7)
                }
                "calorieintake" -> {
                    val i = Intent(activity, WebActivity::class.java)
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    i.putExtra("comingFrom","nudgeRedirection")
                    i.putExtra("Url", CommonUtils.getBaseUrlForAddFood(activity.applicationContext))
                    activity.startActivity(i)
                }
                "calorieburn" -> {
                    val i = Intent(activity, WebActivity::class.java)
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    i.putExtra("comingFrom","nudgeRedirection")
                    i.putExtra("Url", CommonUtils.getBaseUrlForAddExercise(activity.applicationContext))
                    activity.startActivity(i)
                }
            }
        }
    }

}