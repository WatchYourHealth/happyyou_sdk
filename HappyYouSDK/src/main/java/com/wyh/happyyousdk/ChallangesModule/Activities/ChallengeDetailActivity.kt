package com.wyh.happyyousdk.ChallangesModule.Activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.esafirm.imagepicker.features.ImagePicker
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.wyh.happyyousdk.APIEncryption.APIInterface
import com.wyh.happyyousdk.APIEncryption.APILogs
import com.wyh.happyyousdk.APIEncryption.RetrofitHandler
import com.wyh.happyyousdk.ChallangesModule.Adapter.ChallengeDetailsAdapter
import com.wyh.happyyousdk.ChallangesModule.ClickInterface.ActivityClick
import com.wyh.happyyousdk.ChallangesModule.ClickInterface.InviteClick
import com.wyh.happyyousdk.ChallangesModule.Utils.OkHttpHelper
import com.wyh.happyyousdk.ChallangesModule.helperClass
import com.wyh.happyyousdk.R
import com.wyh.happyyousdk.SpinWheel.rewardDialogCloseListener
import com.wyh.happyyousdk.common.adapter.IndicatorsAdapter
import com.wyh.happyyousdk.dashboard.NewDashboardActivity
import com.wyh.happyyousdk.dashboard.NudgeDialogue.spinnerCancelDialog
import com.wyh.happyyousdk.dashboard.helper.NewDashboardHelper.Companion.featureName
import com.wyh.happyyousdk.dashboard.helper.NewDashboardHelper.Companion.trasactionId
import com.wyh.happyyousdk.databinding.ActivityChallengeDetailBinding
import com.wyh.happyyousdk.model.CommonSuccessResponse
import com.wyh.happyyousdk.model.request.CommonRequest
import com.wyh.happyyousdk.model.request.UpdateRequest
import com.wyh.happyyousdk.model.request.ehr.FileData
import com.wyh.happyyousdk.model.request.quizathon.ActivityRewardRequest
import com.wyh.happyyousdk.model.response.AssignRewardsResponse
import com.wyh.happyyousdk.model.response.AssignRewardsResponse.SpinRewardsData
import com.wyh.happyyousdk.model.response.BadgesData
import com.wyh.happyyousdk.model.response.ChallenegActivities
import com.wyh.happyyousdk.model.response.ChallengeActivityResponse
import com.wyh.happyyousdk.model.response.UpdateResponse
import com.wyh.happyyousdk.model.response.playwin.QuizathonRewardData
import com.wyh.happyyousdk.utils.CommonUtils
import com.wyh.happyyousdk.utils.Constants
import com.wyh.happyyousdk.utils.SharedPref
import com.wyh.happyyousdk.utils.SnapHelperOneByOne
import com.wyh.happyyousdk.utils.dialog.PostSpinDialog
import com.wyh.happyyousdk.utils.dialog.QuizRewardDialog
import com.wyhsdk.sharedPreferences.SharedPreference
import dev.skymansandy.scratchcardlayout.listener.ScratchListener
import dev.skymansandy.scratchcardlayout.ui.ScratchCardLayout
import okhttp3.MultipartBody
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.internal.http.RealResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException


class ChallengeDetailActivity : AppCompatActivity(), InviteClick, ActivityClick, ScratchListener,
    rewardDialogCloseListener {

    lateinit var apiInterface: APIInterface
    lateinit var linearLayoutManager: LinearLayoutManager
    var challengeID = ""
    var positionId = ""
    var imageName = ""
    lateinit var listChallengeId: ArrayList<String>
    var fileDataList = ArrayList<FileData>()
    var badgesList = ArrayList<BadgesData>()
    var challengeDetailsRecyler: RecyclerView? = null
    lateinit var transactionID: String
    val parts = ArrayList<MultipartBody.Part>()
    lateinit var context: Context
    lateinit var challengeDetailsResponse: ChallengeActivityResponse
    lateinit var shareImage: String
    lateinit var binding: ActivityChallengeDetailBinding
    lateinit var challengeDetailsAdapter: ChallengeDetailsAdapter
    private val CAMERA_PERMISSION_CODE = 104

    lateinit var reward: SpinRewardsData
    lateinit var quizreward: QuizathonRewardData


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_challenge_detail)
        setContentView(binding.root)
        context = this;
        init()
    }


    override fun onResume() {
        super.onResume()
        //getChallengeDetails(challengeID, context = this,this,this,challengeDetailsRecyler!!,"",positionId)
    }

    fun init() {
        try {
            SharedPref.init(context)
            SharedPreference.init(context)
            apiInterface = RetrofitHandler.getRetrofitInstance().create(APIInterface::class.java)
            challengeDetailsRecyler = findViewById(R.id.challenge_details_recylcer)
            challengeID = intent.getStringExtra("challengeID").toString()
            Log.d("Authtoken", "FaceScan ChallengeID $challengeID")
            positionId = intent.getStringExtra("position").toString()
            //listChallengeId = intent.getStringArrayListExtra( "listChallengeID") as ArrayList<String>
            challengeID.let {
                SharedPref.putChallengeID(challengeID)
                getChallengeDetails(
                    challengeID,
                    context = this,
                    this,
                    this,
                    challengeDetailsRecyler!!,
                    "",
                    positionId
                )
            }

            binding.commonToolBar.tvBack.text = getString(R.string.challenges_text)

            binding.commonToolBar.llBack.setOnClickListener {
                finish()
            }

            SharedPref.positionID(positionId)


            binding.ivHome.setOnClickListener {
                val intent = Intent(this, NewDashboardActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
                finish()
            }

            if (SharedPref.getChallengeCount() > 1) {
                binding.challengeIndicatorRecycler.visibility = View.VISIBLE
            } else {
                binding.challengeIndicatorRecycler.visibility = View.GONE
            }

            val indicatorsAdapter = IndicatorsAdapter(this, SharedPref.getChallengeCount(), 0)
            val snapHelperOneByOne = SnapHelperOneByOne()
            linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            var position = 0


            snapHelperOneByOne.attachToRecyclerView(binding.challengeDetailsRecylcer)


            binding.challengeIndicatorRecycler.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            binding.challengeIndicatorRecycler.hasFixedSize()
            binding.challengeIndicatorRecycler.adapter = indicatorsAdapter
            if (positionId != "") {
                indicatorsAdapter.updateSelectedIndex(positionId.toInt())
            }


            binding.challengeDetailsRecylcer.addOnScrollListener(object :
                RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)

                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        position =
                            if (linearLayoutManager.findFirstCompletelyVisibleItemPosition() != -1) {
                                linearLayoutManager.findFirstCompletelyVisibleItemPosition()
                            } else {
                                linearLayoutManager.findFirstVisibleItemPosition()
                            }
                        Log.d("position Tag", position.toString())
                        Log.d("position Tag", positionId)
                        if (positionId.toInt() != position && position != -1) {
                            positionId = position.toString()
                            challengeID = helperClass.listChallengeId[position]
                            getChallengeDetails(
                                helperClass.listChallengeId[position],
                                context = this@ChallengeDetailActivity,
                                this@ChallengeDetailActivity,
                                this@ChallengeDetailActivity,
                                challengeDetailsRecyler!!,
                                "",
                                positionId
                            )
                        }
                        indicatorsAdapter.updateSelectedIndex(position)
                    }
                }
            })

        } catch (e: Exception) {
            e.toString()
        }
    }


    fun getChallengeDetails(
        challengeID: String,
        context: Context,
        click: InviteClick,
        onclick: ActivityClick,
        recyclerView: RecyclerView,
        comingFrom: String,
        position: String
    ) {
        try {
            var challengeIDNew: String = ""
            CommonUtils.showProgressDialige(context)
            if (challengeID.equals("")) {
                challengeIDNew = SharedPref.getChallengeID()
            } else {
                challengeIDNew = challengeID
            }
            val commonRequest = CommonRequest(challengeIDNew)
            val api = RetrofitHandler.getRetrofitInstance().create(APIInterface::class.java)
            api.challengeActivity(SharedPref.getAuthToken(), commonRequest)
                .enqueue(object : Callback<ChallengeActivityResponse> {
                    @SuppressLint("NotifyDataSetChanged")
                    override fun onResponse(
                        call: Call<ChallengeActivityResponse>,
                        response: Response<ChallengeActivityResponse>
                    ) {
                        CommonUtils.dismissDialoge()
                        try {
                            if (response.code() == 200 && response.body()!!.success) {
                                response.body().let {
                                    challengeDetailsResponse = response.body()!!
                                    if (binding == null) {
                                        binding = helperClass.binding
                                    }
                                    challengeDetailsAdapter = ChallengeDetailsAdapter(
                                        context,
                                        click,
                                        onclick,
                                        response.body()!!,
                                        binding!!
                                    )
                                    linearLayoutManager = LinearLayoutManager(
                                        context,
                                        LinearLayoutManager.HORIZONTAL,
                                        false
                                    )
                                    recyclerView.layoutManager = linearLayoutManager
                                    recyclerView.hasFixedSize()
                                    recyclerView.adapter = challengeDetailsAdapter
                                    Log.d("AuthToken", "Position ID $positionId")
                                    if (position != "") {
                                        recyclerView.scrollToPosition(position.toInt())
                                    } else {
                                        recyclerView.scrollToPosition(0)
                                    }
                                    Log.d("AuthToken", "svdsdvsdv" + Gson().toJson(response.body()))
                                    if (response.body()!!.data != null)
                                        if (response.body()!!.data!!.badges != null) {
                                            if (response.body()!!.data.badges.size != 0) {
                                                badgesList = response.body()!!.data.badges
                                                helperClass.scracthDialoge(
                                                    context,
                                                    this@ChallengeDetailActivity,
                                                    response.body()!!.data.badges[0],
                                                    badgesList,
                                                    "normalChallenge"
                                                )

                                                transactionID =
                                                    response.body()!!.data.badges[0].transactionId
                                            }
                                        }
                                }
                            }
                        } catch (e: Exception) {
                            CommonUtils.dismissDialoge()
                            APILogs.sendLogs(
                                call.request().url().toString(),
                                e.message!!,
                                response.code().toString(),
                                this@ChallengeDetailActivity
                            )
                            e.toString()
                        }


                    }

                    override fun onFailure(call: Call<ChallengeActivityResponse>, t: Throwable) {
                        APILogs.sendLogs(
                            call.request().url().toString(),
                            t.message!!,
                            "onFailure",
                            this@ChallengeDetailActivity
                        )
                        CommonUtils.dismissDialoge()
                    }

                })

        } catch (e: Exception) {
            CommonUtils.dismissDialoge()
            APILogs.sendLogs(
                Constants.EXCEPTION,
                e.message!!,
                "Exception",
                this@ChallengeDetailActivity
            )
            e.toString()
        }
    }

    fun getChallengeForFeedBack(recyclerView: RecyclerView, position: String) {
        try {
            CommonUtils.showProgressDialige(context)
            val commonRequest = CommonRequest(challengeID)
            val api = RetrofitHandler.getRetrofitInstance().create(APIInterface::class.java)
            api.challengeActivity(SharedPref.getAuthToken(), commonRequest)
                .enqueue(object : Callback<ChallengeActivityResponse> {
                    override fun onResponse(
                        call: Call<ChallengeActivityResponse>,
                        response: Response<ChallengeActivityResponse>
                    ) {
                        CommonUtils.dismissDialoge()
                        try {
                            if (response.code() == 200 && response.body()!!.success) {
                                response.body().let {
                                    val challengeDetailsAdapter = binding?.let { it1 ->
                                        ChallengeDetailsAdapter(
                                            context,
                                            this@ChallengeDetailActivity,
                                            this@ChallengeDetailActivity,
                                            response.body()!!,
                                            it1
                                        )
                                    }
                                    linearLayoutManager = LinearLayoutManager(
                                        context,
                                        LinearLayoutManager.HORIZONTAL,
                                        false
                                    )
                                    recyclerView.layoutManager = linearLayoutManager
                                    recyclerView.hasFixedSize()
                                    recyclerView.adapter = challengeDetailsAdapter
                                    //recyclerView.scrollToPosition(positionId.toInt())
                                    Log.d("AuthToken", "Position ID $positionId")
                                    if (position != "") {
                                        recyclerView.scrollToPosition(position.toInt())
                                    } else {
                                        recyclerView.scrollToPosition(0)
                                    }
                                    Log.d("AuthToken", "svdsdvsdv" + Gson().toJson(response.body()))
                                    if (response.body()!!.data != null)
                                        if (response.body()!!.data!!.badges != null) {
                                            if (response.body()!!.data.badges.size != 0) {
                                                badgesList = response.body()!!.data.badges
                                                helperClass.scracthDialoge(
                                                    context,
                                                    this@ChallengeDetailActivity,
                                                    response.body()!!.data.badges[0],
                                                    badgesList,
                                                    "normalChallenge"
                                                )

                                                transactionID =
                                                    response.body()!!.data.badges[0].transactionId
                                            }
                                        }
                                }
                            }
                        } catch (e: Exception) {
                            APILogs.sendLogs(
                                call.request().url().toString(),
                                e.message!!,
                                response.code().toString(),
                                this@ChallengeDetailActivity
                            )
                            e.toString()
                        }
                    }

                    override fun onFailure(call: Call<ChallengeActivityResponse>, t: Throwable) {
                        CommonUtils.dismissDialoge()
                        APILogs.sendLogs(
                            call.request().url().toString(),
                            t.message!!,
                            "onFailure",
                            this@ChallengeDetailActivity
                        )

                    }

                })

        } catch (e: Exception) {
            APILogs.sendLogs(
                Constants.EXCEPTION,
                e.message!!,
                "Exception",
                this@ChallengeDetailActivity
            )
            e.toString()
        }
    }

    override fun clickToInvite() {

    }

    fun shareBitmap(uriForFile: Uri, context: Activity) {
        try {
            val intent = Intent(Intent.ACTION_SEND)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.putExtra(Intent.EXTRA_STREAM, uriForFile)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.type = "image/png"
            context.startActivityForResult(Intent.createChooser(intent, "Share"), 2121)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    fun updateChallenge(challengeID: String, activityID: String, context: Context) {
        try {
            //CommonUtils.showProgressDialige(this)
            val updateRequest = UpdateRequest(challengeID, activityID)
            apiInterface.updateChalleneg(SharedPref.getAuthToken(), updateRequest)
                .enqueue(object : Callback<UpdateResponse> {
                    override fun onResponse(
                        call: Call<UpdateResponse>,
                        response: Response<UpdateResponse>
                    ) {
                        CommonUtils.dismissDialoge()
                        response.let {
                            if (response.code() == 200 && response.body()!!.success) {
                                challengeDetailsRecyler?.let { it1 ->
                                    getChallengeDetails(
                                        challengeID,
                                        context,
                                        this@ChallengeDetailActivity,
                                        this@ChallengeDetailActivity,
                                        it1,
                                        "",
                                        positionId
                                    )
                                }
                            }
                        }
                    }

                    override fun onFailure(call: Call<UpdateResponse>, t: Throwable) {
                        CommonUtils.dismissDialoge()
                    }

                })


        } catch (e: Exception) {
            CommonUtils.dismissDialoge()
            e.toString()
        }
    }


    fun completeChallenge(
        parts: ArrayList<MultipartBody.Part>,
        challengeID: String,
        context: Context,
        recyclerView: RecyclerView,
        greetingID: String,
        communityID: String
    ) {
        try {

            CommonUtils.showProgressDialige(context)
            val client: okhttp3.OkHttpClient = OkHttpHelper.okHttpInit()
            var requestBody: RequestBody

            if (helperClass.activityType.equals("beforeafterupload")) {
                if (parts.size != 2) {
                    CommonUtils.dismissDialoge()
                    Toast.makeText(this, "please select 2 images", Toast.LENGTH_LONG).show()
                    return
                }
            }
            if (helperClass.activityType.equals("journal") || helperClass.activityType.equals(
                    "stepandnote",
                    ignoreCase = true
                ) || helperClass.activityType.equals("facescanandnote", ignoreCase = true)
            ) {
                requestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("Activityid", helperClass.activityID)
                    .addFormDataPart("ChallengeId", SharedPref.getChallengeID())
                    .addFormDataPart("journalContent", helperClass.journalText)
                    .addFormDataPart("EventType", helperClass.activityType).build()
            } else if (helperClass.activityType.equals("beforeafterupload")) {
                requestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addPart(parts[0])
                    .addPart(parts[1])
                    .addFormDataPart("Activityid", helperClass.activityID)
                    .addFormDataPart("ChallengeId", SharedPref.getChallengeID())
                    .addFormDataPart("journalContent", helperClass.journalText)
                    .addFormDataPart("EventType", helperClass.activityType).build()
            } else if (helperClass.activityType.equals("tribe") || helperClass.activityType.equals("tribeandwhatsapp")) {
                requestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("Activityid", helperClass.activityID)
                    .addFormDataPart("ChallengeId", SharedPref.getChallengeID())
                    .addFormDataPart("GreetingId", greetingID)
                    .addFormDataPart("communityId", communityID).build()
            } else {
                requestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addPart(parts[0])
                    .addFormDataPart("Activityid", helperClass.activityID)
                    .addFormDataPart("ChallengeId", SharedPref.getChallengeID())
                    .addFormDataPart("journalContent", helperClass.journalText)
                    .addFormDataPart("EventType", helperClass.activityType).build()
            }
            val request: Request = OkHttpHelper.requestBuilder(context, requestBody)
            client.newCall(request).enqueue(object : okhttp3.Callback {
                override fun onFailure(call: okhttp3.Call, e: IOException) {
                    helperClass.alertDialog.dismiss()
                    deleteImage()
                    CommonUtils.dismissDialoge()
                }

                override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                    CommonUtils.dismissDialoge()
                    try {
                        response.let {
                            deleteImage()
                            if (response.code() == 200 && response.body() != null) {
                                fileDataList.clear()
                                helperClass.alertDialog.dismiss()
                                try {
                                    var common: String? =
                                        (response.body() as? RealResponseBody)?.string()
                                    val type = object : TypeToken<CommonSuccessResponse?>() {}.type
                                    val commonResponse: CommonSuccessResponse =
                                        Gson().fromJson(common, type)
                                    if (commonResponse != null && commonResponse.spinTheWheelRewardsModel != null) {
                                        reward = commonResponse.spinTheWheelRewardsModel
                                        Log.d("ToastDebug", "Reached runOnUiThread block")
                                        if (!isFinishing && !isDestroyed) {
                                            runOnUiThread { getSpinRewardPopup(context, reward) }
                                        } else {
                                            Log.d(
                                                "ToastDebug",
                                                "Activity is finishing or destroyed"
                                            )
                                        }

                                    }

                                    if (!isFinishing && !isDestroyed) {
                                        runOnUiThread {
                                            if(checkIsFromQuizqathon()) {
                                                //QuizReward Api Call
                                                FetchQuizReward()

                                            }
                                        }
                                    } else {
                                        Log.d(
                                            "ToastDebug",
                                            "Activity is finishing or destroyed"
                                        )
                                    }
                                } catch (ex: Exception) {
                                    ex.printStackTrace()
                                }

                                getChallengeDetails(
                                    challengeID,
                                    context,
                                    this@ChallengeDetailActivity,
                                    this@ChallengeDetailActivity,
                                    recyclerView,
                                    helperClass.activityType,
                                    positionId
                                )
                            }
                        }
                    } catch (e: Exception) {
                        e.toString()
                    }
                }
            })
        } catch (e: Exception) {
            CommonUtils.dismissDialoge()
            e.toString()
        }
    }
    private fun FetchQuizReward() {
        val activityRewardRequest = ActivityRewardRequest(trasactionId, featureName)
        val call = apiInterface.FetchQuizReward(SharedPref.getAuthToken(), activityRewardRequest)

        call.enqueue(object : Callback<CommonSuccessResponse?> {
            override fun onResponse(
                call: Call<CommonSuccessResponse?>,
                response: Response<CommonSuccessResponse?>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    if (response.body()!!.quizathonRewardData != null) {
                        getQuizathonRewardPopup(context,response.body()!!.quizathonRewardData)
                    }
                }
            }

            override fun onFailure(call: Call<CommonSuccessResponse?>, t: Throwable) {
            }
        })
    }
    fun checkIsFromQuizqathon(): Boolean {
        if (trasactionId != null && !trasactionId!!.isEmpty()) {
            return true
        }
        return false
    }
    private fun getSpinRewardPopup(context: Context, data: AssignRewardsResponse.SpinRewardsData) {
        try {
            if (data.rewardType != null && !data.rewardType.isEmpty()) {
                val rewardtype = data.rewardType
                if (rewardtype.equals("Points", ignoreCase = true)) {
                    PostSpinDialog.showPostSpinnerPointsPopupCallBack(
                        data.rewardTitle,
                        context,
                        "Trends",
                        data.rewardDescription,
                        data.rewardHeader1,
                        data.rewardHeader2,
                        this@ChallengeDetailActivity,
                        this@ChallengeDetailActivity
                    )
                } else if (rewardtype.equals("Offers", ignoreCase = true)) {
                    PostSpinDialog.showPostSpinnerOfferCallBack(
                        context,
                        data.partnerLogo,
                        data.rewardDescription,
                        data.partnerName,
                        data.partnerUrl,
                        "Trends",
                        data.expiryInHours,
                        data.rewardTitle,
                        data.rewardHeader1,
                        data.rewardHeader2,
                        this@ChallengeDetailActivity,
                        this@ChallengeDetailActivity
                    )
                } else if (rewardtype.equals("Voucher", ignoreCase = true)) {
                    PostSpinDialog.showPostSpinnerVoucherCallBack(
                        context,
                        data.rewardLogo,
                        data.couponCode,
                        data.rewardDescription,
                        data.rewardTitle,
                        data.expiryInHours,
                        "Trends",
                        data.rewardHeader1,
                        data.rewardHeader2,
                        this@ChallengeDetailActivity,
                        this@ChallengeDetailActivity
                    )
                } else if (rewardtype.equals("Badge", ignoreCase = true)) {
                    PostSpinDialog.showPostSpinnerBadgePopupCallBack(
                        context,
                        "Trends",
                        data.rewardLogo,
                        data.rewardTitle,
                        data.rewardDescription,
                        data.rewardHeader1,
                        data.rewardHeader2,
                        this@ChallengeDetailActivity,
                        this@ChallengeDetailActivity
                    )
                } else if (rewardtype.equals("Stamps", ignoreCase = true)) {
                    PostSpinDialog.showStampsPopupCallBack(
                        data.rewardHeader1,
                        data.rewardHeader2,
                        data.rewardTitle,
                        data.rewardValue,
                        context,
                        this@ChallengeDetailActivity,
                        this@ChallengeDetailActivity
                    )
                }
            }
        } catch (ex: java.lang.Exception) {
            ex.printStackTrace()
        }
    }

    private fun getQuizathonRewardPopup(context: Context, data: QuizathonRewardData) {
        try {
            if (data.rewardType != null && !data.rewardType.isEmpty()) {
                val rewardtype = data.rewardType
                if (rewardtype.equals("Points", ignoreCase = true)) {
                    QuizRewardDialog.showPostSpinnerPointsPopupCallBack(
                        data.rewardTitle,
                        context,
                        "Trends",
                        data.rewardDescription,
                        data.rewardHeader1,
                        data.rewardHeader2,
                        this@ChallengeDetailActivity,
                        this@ChallengeDetailActivity
                    )
                } else if (rewardtype.equals("Offers", ignoreCase = true)) {
                    QuizRewardDialog.showPostSpinnerOfferCallBack(
                        context,
                        data.partnerLogo,
                        data.rewardDescription,
                        data.partnerName,
                        data.partnerUrl,
                        "Trends",
                        data.expiryInHours,
                        data.rewardTitle,
                        data.rewardHeader1,
                        data.rewardHeader2,
                        this@ChallengeDetailActivity,
                        this@ChallengeDetailActivity
                    )
                } else if (rewardtype.equals("Voucher", ignoreCase = true)) {
                    QuizRewardDialog.showPostSpinnerVoucherCallBack(
                        context,
                        data.rewardLogo,
                        data.couponCode,
                        data.rewardDescription,
                        data.rewardTitle,
                        data.expiryInHours,
                        "Trends",
                        data.rewardHeader1,
                        data.rewardHeader2,
                        this@ChallengeDetailActivity,
                        this@ChallengeDetailActivity
                    )
                } else if (rewardtype.equals("Badge", ignoreCase = true)) {
                    QuizRewardDialog.showPostSpinnerBadgePopupCallBack(
                        context,
                        "Trends",
                        data.rewardLogo,
                        data.rewardTitle,
                        data.rewardDescription,
                        data.rewardHeader1,
                        data.rewardHeader2,
                        this@ChallengeDetailActivity,
                        this@ChallengeDetailActivity
                    )
                } else if (rewardtype.equals("Stamps", ignoreCase = true)) {
                    QuizRewardDialog.showStampsPopupCallBack(
                        data.rewardHeader1,
                        data.rewardHeader2,
                        data.rewardTitle,
                        data.rewardValue,
                        context,
                        this@ChallengeDetailActivity,
                        this@ChallengeDetailActivity
                    )
                }else if(rewardtype.equals("future",ignoreCase = true)){
                    QuizRewardDialog.showFutureRewardDialog(context,data.dialogModel,data.claimDate)
                }
            }
        } catch (ex: java.lang.Exception) {
            ex.printStackTrace()
        }
    }

    fun saveImage(imageUri: Uri) {
        val fileData = FileData()
        fileData.path = helperClass.getRealPathFromURI(this, imageUri)
        val singleFileSize: Long = helperClass.getFileSizeFromPath(fileData.path)
        val fileDataNew = FileData()
        val file1 = CommonUtils.compressImageToJPEG(this, imageUri)
        fileDataNew.path = file1.path
        fileDataNew.mimeType = "application/png"
        imageName = file1.name
        if (file1.length() > 5000000) {
            Toast.makeText(this, "File(s) size should not be greater than 5MB", Toast.LENGTH_SHORT)
                .show()
        } else {
            fileDataList.add(fileDataNew)
        }
        helperClass.prepareFilePart("ActivityUploads", fileDataList[0].path)
            ?.let { parts.add(it) }
        challengeDetailsRecyler?.let { completeChallenge(parts, challengeID, this, it, "", "") }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 2121) {
            updateChallenge(challengeID, helperClass.activityID, this)
        }
        if (requestCode == 1) {
            try {
                imageName = ""
                if (data == null) {
                    Toast.makeText(this, "No Image Selected", Toast.LENGTH_SHORT).show()
                } else {
                    if (data!!.data != null) {
                        val imageUri = data.data
                        saveImage(imageUri!!)
                    } else {
                        val mClipData = data.clipData
                        if (mClipData!!.itemCount > 2) {
                            Toast.makeText(this, "Please select 1 or 2 images", Toast.LENGTH_LONG)
                                .show()
                        } else {
                            for (i in 0 until mClipData!!.itemCount) {
                                val item = mClipData.getItemAt(i)
                                val imageURI = item.uri
                                saveImage(imageURI)
                            }
                        }

                    }
                }


            } catch (e: Exception) {
                e.toString()
            }
        }
        try {
            if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
                imageName = ""
                val parts = ArrayList<MultipartBody.Part>()
                val image2 = ImagePicker.getImages(data)
                for (i in image2.indices) {
                    val fileData = FileData()
                    fileData.path = helperClass.getFilePathFromImage(this, image2[i])
                    val singleFileSize: Long = helperClass.getFileSizeFromPath(fileData.path)
                    Log.d("comparedata singleFileSize", singleFileSize.toString() + "")
                    val fileDataNew = FileData()
                    Log.d("comparedata singleFileSize", singleFileSize.toString() + "")
                    val file1 = CommonUtils.compressImageToJPEG(this, image2[i].uri)
                    fileDataNew.path = file1.path
                    fileDataNew.mimeType = "application/png"
                    imageName = file1.name
                    if (file1.length() > 5000000) {
                        Toast.makeText(
                            this,
                            "File(s) size should not be greater than 5MB",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        fileDataList.add(fileDataNew)
                    }
                    helperClass.prepareFilePart("ActivityUploads", fileDataList[0].path)
                        ?.let { parts.add(it) }

                }
                challengeDetailsRecyler?.let {
                    completeChallenge(
                        parts,
                        challengeID,
                        this,
                        it,
                        "",
                        ""
                    )
                }

            }
        } catch (e: Exception) {
            e.toString()
        }

    }


    fun deleteImage() {
        if (imageName !== "") {
            CommonUtils.deleteImage(imageName)
            imageName = ""
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityClick(
        challengeActivity: ChallenegActivities,
        activityBinding: ActivityChallengeDetailBinding
    ) {
        if (!challengeActivity.isCompleted) {
            try {
                if (challengeActivity.shareContent != null) {
                    shareImage = challengeActivity.shareContent[0] ?: ""
                } else {
                    shareImage = ""
                }
                binding = activityBinding
                helperClass.journalUploadDialog(
                    this@ChallengeDetailActivity,
                    challengeActivity,
                    challengeID,
                    shareImage,
                    activityBinding
                )
            } catch (e: Exception) {
                e.toString()
            }
        } else {

        }

    }

    override fun onScratchComplete() {
        try {
            CommonUtils.dismissDialoge()
            if (quizreward != null) {
                QuizRewardDialog.QuizScratchCard(this, quizreward.transId, true);
            }
        } catch (e: Exception) {
            e.toString()
        }
    }

    override fun onScratchProgress(
        scratchCardLayout: ScratchCardLayout,
        atLeastScratchedPercent: Int
    ) {
        if (atLeastScratchedPercent > 20) {
            Log.d("AuthToken", "Scracth Completed")
            scratchCardLayout.onFullReveal()

            helperClass.scratchComplete(this, SharedPref.getTransactionID())

        }
    }

    override fun onScratchStarted() {

    }


    fun requestCameraPermission(context: Context, permission: String, requestCode: Int) {
        if (ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_DENIED
        ) {
            ActivityCompat.requestPermissions(
                (context as Activity),
                arrayOf<String>(permission),
                requestCode
            )
        } else {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                helperClass.openImagePicker(context as Activity)
            } else {
                helperClass.openGalleryOnly(context as Activity)
            }
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                    helperClass.openImagePicker(context as Activity)
                } else {
                    helperClass.openGalleryOnly(context as Activity)
                }
                Toast.makeText(
                    this@ChallengeDetailActivity,
                    "Camera Permission Granted",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    this@ChallengeDetailActivity,
                    "Camera Permission Denied",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onDialogDismiss() {
        cancelDialog()
    }

    fun cancelDialog() {
        try {
            if (reward != null) {
                spinnerCancelDialog(context, reward.getRewardType(), this)
            } else if (quizreward != null) {
                spinnerCancelDialog(context, quizreward.getRewardType(), this)
            }
        } catch (e: java.lang.Exception) {
            // Log the exception for debugging
            e.printStackTrace()
        }
    }
}