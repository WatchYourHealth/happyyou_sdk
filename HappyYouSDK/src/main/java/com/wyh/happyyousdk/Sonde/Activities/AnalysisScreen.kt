package com.wyh.happyyousdk.Sonde.Activities

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson

import com.wyh.happyyousdk.APIEncryption.APIInterface
import com.wyh.happyyousdk.APIEncryption.APILogs
import com.wyh.happyyousdk.APIEncryption.RetrofitHandler
import com.wyh.happyyousdk.R
import com.wyh.happyyousdk.Sonde.Adapter.MentalWellnessScoreDetails
import com.wyh.happyyousdk.Sonde.Utilities.BackgroundService
import com.wyh.happyyousdk.Sonde.Utilities.Helper
import com.wyh.happyyousdk.SpinWheel.rewardDialogCloseListener

import com.wyh.happyyousdk.dashboard.NewDashboardActivity
import com.wyh.happyyousdk.dashboard.NudgeDialogue
import com.wyh.happyyousdk.dashboard.helper.NewDashboardHelper
import com.wyh.happyyousdk.dashboard.helper.NewDashboardHelper.Companion.featureName
import com.wyh.happyyousdk.dashboard.helper.NewDashboardHelper.Companion.trasactionId
import com.wyh.happyyousdk.databinding.ActivityAnalysisScreenBinding
import com.wyh.happyyousdk.databinding.CustomPopupStampsBinding
import com.wyh.happyyousdk.databinding.LayoutNewPointsPopUpBinding
import com.wyh.happyyousdk.databinding.RefferelCodeDialogeLayoutBinding
import com.wyh.happyyousdk.model.CommonSuccessResponse
import com.wyh.happyyousdk.model.request.PollTranscribeRequest
import com.wyh.happyyousdk.model.request.SondeReminderRequest
import com.wyh.happyyousdk.model.request.TranscribeJobRequest
import com.wyh.happyyousdk.model.request.quizathon.ActivityRewardRequest
import com.wyh.happyyousdk.model.request.rewards.RewardsPopupRequest
import com.wyh.happyyousdk.model.response.AssignRewardsResponse
import com.wyh.happyyousdk.model.response.AssignRewardsResponse.SpinRewardsData
import com.wyh.happyyousdk.model.response.FeedbackResponseData
import com.wyh.happyyousdk.model.response.PollTranscribeResponse
import com.wyh.happyyousdk.model.response.SondeReminderResponse
import com.wyh.happyyousdk.model.response.TranscribeJobResponse
import com.wyh.happyyousdk.model.response.playwin.QuizathonRewardData
import com.wyh.happyyousdk.network.ApiClientWyh
import com.wyh.happyyousdk.network.ApiInterfaceWyh
import com.wyh.happyyousdk.rewards.FeedbackPopupDialogBox
import com.wyh.happyyousdk.rewards.FeedbackPopupDialogBox.Companion.getInstance
import com.wyh.happyyousdk.rewards.PendingActivityDashboard
import com.wyh.happyyousdk.rewards.RewardsActivity
import com.wyh.happyyousdk.utils.Analytics
import com.wyh.happyyousdk.utils.CommonUtils
import com.wyh.happyyousdk.utils.SharedPref
import com.wyh.happyyousdk.utils.dialog.PostSpinDialog
import com.wyh.happyyousdk.utils.dialog.QuizRewardDialog
import dev.skymansandy.scratchcardlayout.listener.ScratchListener
import dev.skymansandy.scratchcardlayout.ui.ScratchCardLayout
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AnalysisScreen : AppCompatActivity(), ScratchListener, rewardDialogCloseListener {

    lateinit var binding: ActivityAnalysisScreenBinding
    var score = ""
    var comingFrom = ""
    var diaryConcern = false
    lateinit var context: Context
    var isFeedbackShown = false
    var isSpinRewardShown = false
    var isQuizRewardShown = false
    var spinRewardData: SpinRewardsData? = null
    var quizreward: QuizathonRewardData? = null
    var quizrewardBool: Boolean? = false
    var isPositiveBtn: Boolean = false
    var isStamp: Boolean = false
    var alertDialogStamp: AlertDialog? =
        null
    var alertDialogBonusStamp: AlertDialog? = null
    var alertDialogBonusRewards: AlertDialog? = null
    var stampId: Int = -1
    var apiInterfaceWyh: ApiInterfaceWyh? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_analysis_screen)
        setContentView(binding.root)
        SharedPref.init(this)
        context = this
        apiInterfaceWyh = ApiClientWyh.getClient(CommonUtils.getBaseUrlForAPI(context)).create(
            ApiInterfaceWyh::class.java
        )
        try {
            spinRewardData = intent.getSerializableExtra("spinRewardData") as SpinRewardsData
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        try {
            quizrewardBool = intent.getBooleanExtra("quizathonRewardmodel",false)
        } catch (ex: Exception) {
            quizrewardBool=false
            ex.printStackTrace()
        }
        score = intent.getStringExtra("score").toString()
        comingFrom = intent.getStringExtra("comingFrom").toString()
        binding.sondeAnalysisScoreTv.text = score



        binding.sondeAnalysisProgressBar.progress = score.toFloat()

        val customObj = JSONObject()
        try {
            customObj.put("PAGE_ID", "SondeAnalysis")
            customObj.put("type", comingFrom)
        } catch (e: JSONException) {
            throw RuntimeException(e)
        }
        

        binding.dateTv.text = CommonUtils.todayDate()
        binding.dateTv.text = CommonUtils.convertDateIntoMonthString(CommonUtils.todayDate())
        if (spinRewardData != null) {
            isSpinRewardShown = true
            spinRewardData?.let {
                getSpinRewardPopup(context, it)
            }
        } else {
            isSpinRewardShown = false
        }
        if (checkIsFromQuizqathon() && quizrewardBool == true) {
            //QuizReward Api Call
            FetchQuizReward()

        } else {
            isQuizRewardShown = false
        }

        if (comingFrom.equals("metalFitness", true)) {
            binding.mentalHealthScoreParent.visibility = View.VISIBLE
            binding.analysisBack.text = getString(R.string.mental_health_back_heading)
            binding.resultDetailsParent.visibility = View.VISIBLE
            binding.m3NoteTv.visibility = View.VISIBLE
            binding.respiratoryRetakeBtn.visibility = View.GONE
            binding.respiratoryScoreParent.visibility = View.GONE
            binding.setReminderLayout.visibility = View.VISIBLE
            binding.analysisButtonLayout.visibility = View.VISIBLE
            binding.healthScoreTv.text = getString(R.string.mental_wellness_score_heading)

            if (score.toInt() in (0..69)) {
                binding.tvHeartAgeRiskScore.text = getString(R.string.pay_attention)
                binding.m3NoteTv.text = getString(R.string.m3_attention_tv)
            } else if (score.toInt() in 70..79) {
                binding.tvHeartAgeRiskScore.text = getString(R.string.sonde_good)
                binding.m3NoteTv.text = getString(R.string.m3_good_tv)
            } else {
                binding.tvHeartAgeRiskScore.text = getString(R.string.sinde_excellent)
                binding.m3NoteTv.text = getString(R.string.m3_excellent_tv)
            }
            setMentalWellnessResult()
        } else {
            binding.respiratoryScoreParent.visibility = View.VISIBLE
            binding.analysisBack.text = getString(R.string.respiratory_health_back_heading)
            binding.m3NoteTv.visibility = View.GONE
            binding.respiratoryRetakeBtn.visibility = View.VISIBLE
            binding.setReminderLayout.visibility = View.GONE
            binding.analysisButtonLayout.visibility = View.GONE
            //binding.resultDetailsParent.visibility = View.GONE
            binding.healthScoreTv.text = getString(R.string.respiratory_score_heading)
            if (score.toInt() <= 64) {
                binding.tvHeartAgeRiskScore.text = getString(R.string.low_risk)
            } else if (score.toInt() in 65..79) {
                binding.tvHeartAgeRiskScore.text = getString(R.string.high_risk)
            } else {
                binding.tvHeartAgeRiskScore.text = getString(R.string.high_risk)

            }
        }

        binding.startM3Button.setOnClickListener {
            if (comingFrom.equals("metalFitness", true)) {
                if (SharedPref.getDiaryConcern()) {
                    finish()
                } else {
                    APILogs.activityTracker("A_DB_BI_AZ_KYW_MW_BR_Retake", this@AnalysisScreen)
                    addDiary("retake")
                }
            } else {
                APILogs.activityTracker("A_DB_BI_AZ_KYW_RH_Begin_Retake", this@AnalysisScreen)
                finish()
            }
        }

        binding.respiratoryRetakeBtn.setOnClickListener {
            finish()
        }

        binding.saveToJournal.setOnClickListener {
            if (SharedPref.getDiaryConcern()) {
                Toast.makeText(
                    this@AnalysisScreen,
                    "You have already saved this journal in the diary",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                CreateTranscribe("save")
            }
        }

        binding.analysisBackLayout.setOnClickListener {
            if (comingFrom.equals("metalFitness", true)) {
                if (SharedPref.getDiaryConcern()) {
                    finish()
                } else {
                    addDiary("back")
                }
            } else {
                finish()
            }
        }


        binding.sondeReminderSwitch.setOnCheckedChangeListener { _, p1 ->
            if (p1) {
                APILogs.activityTracker("A_DB_BI_AZ_KYW_MW_BR_SR", this@AnalysisScreen)
                setReminder(1)
            } else {
                setReminder(0)
            }

        }

        binding.historyImg.setOnClickListener {
            if (comingFrom.equals("metalFitness", true)) {
                if (SharedPref.getDiaryConcern()) {
                    APILogs.activityTracker("A_DB_BI_AZ_KYW_MW_BR_History", this@AnalysisScreen)
                    startActivity(
                        Intent(this, SondeHistory::class.java)
                            .putExtra("comingFrom", comingFrom)
                    )
                } else {
                    addDiary("history")
                }
            } else {
                startActivity(
                    Intent(this, SondeHistory::class.java)
                        .putExtra("comingFrom", comingFrom)
                )
            }

        }

        binding.analysisHome.setOnClickListener {
            if (comingFrom.equals("metalFitness", true)) {
                if (SharedPref.getDiaryConcern()) {
                    val intent = Intent(this, NewDashboardActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                    startActivity(intent)
                    finish()
                } else {
                    addDiary("home")
                }
            } else {
                val intent = Intent(this, NewDashboardActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
                finish()
            }

        }


    }

    private fun FetchQuizReward() {
        var apiInterface: APIInterface? = null
        apiInterface = RetrofitHandler.apiInterface()
        val activityRewardRequest = ActivityRewardRequest(trasactionId, featureName)
        val call = apiInterface.FetchQuizReward(SharedPref.getAuthToken(), activityRewardRequest)

        call.enqueue(object : Callback<CommonSuccessResponse?> {
            override fun onResponse(
                call: Call<CommonSuccessResponse?>,
                response: Response<CommonSuccessResponse?>
            ) {
                if (response.isSuccessful && response.body() != null && response.body()!!.quizathonRewardData != null) {
                    trasactionId = null
                    featureName = null
                    isQuizRewardShown = true
                    quizreward = response.body()!!.quizathonRewardData;
                    quizreward?.let {
                        getQuizathonRewardPopup(context, it)
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

    private fun setMentalWellnessResult() {
        try {
            binding.mentalScoreDetailList.layoutManager = LinearLayoutManager(this)
            binding.mentalScoreDetailList.hasFixedSize()
            val mentalWellnessScoreDetails =
                MentalWellnessScoreDetails(this, Helper.voiceFeatureList)
            binding.mentalScoreDetailList.adapter = mentalWellnessScoreDetails
        } catch (e: Exception) {
            e.toString()
        }
    }


    override fun onResume() {
        super.onResume()
        try {
            if (!isSpinRewardShown || !isQuizRewardShown) {
                if (!isFeedbackShown) {
                    Handler().postDelayed(Runnable {
                        if (CountdownActivity.feedbackModel != null && CountdownActivity.feedbackModel.starConfig != null) {
                            isFeedbackShown = true
                            val instance = FeedbackPopupDialogBox.getInstance()
                            instance.showPopUpFeedback(
                                this@AnalysisScreen,
                                CountdownActivity.feedbackModel!!
                            )
                            CountdownActivity.feedbackModel = FeedbackResponseData()
                        }
                    }, 1000)
                }
            }


        } catch (e: Exception) {
            e.toString()
        }


    }


    private fun setReminder(checkValue: Int) {
        try {
            CommonUtils.showProgressDialige(this)
            val apiInterface = RetrofitHandler.apiInterface()
            val SondeReminderRequest = SondeReminderRequest(checkValue)
            apiInterface.setSondeReminder(SharedPref.getAuthToken(), SondeReminderRequest)
                .enqueue(object : Callback<SondeReminderResponse> {
                    override fun onResponse(
                        call: Call<SondeReminderResponse>,
                        response: Response<SondeReminderResponse>
                    ) {
                        CommonUtils.dismissDialoge()
                        if (response.body() != null) {
                            if (response.code() == 200 && response.isSuccessful) {
                                if (checkValue == 1) {
                                    Toast.makeText(
                                        this@AnalysisScreen,
                                        "Reminder Set",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                } else {
                                    Toast.makeText(
                                        this@AnalysisScreen,
                                        "Reminder Disabled",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                }
                            }
                        }
                    }

                    override fun onFailure(call: Call<SondeReminderResponse>, t: Throwable) {
                        CommonUtils.dismissDialoge()
                    }

                })


        } catch (e: Exception) {
            CommonUtils.dismissDialoge()
            e.toString()
        }
    }

    fun CreateTranscribe(alertcomingFrom: String) {
        try {
            CommonUtils.showProgressDialige(this)
            diaryConcern = true
            SharedPref.putDiaryConcern(true)
            val transcribeRequest =
                TranscribeJobRequest(SharedPref.getSondeToken(), SharedPref.getMentalWellnessPath())
            val apiInterface = RetrofitHandler.apiInterface()
            Log.d("Authtoken", "CreateTranscribe Request ${Gson().toJson(transcribeRequest)}")
            apiInterface.createTranscribeJob(SharedPref.getAuthToken(), transcribeRequest)
                .enqueue(object : Callback<TranscribeJobResponse> {
                    override fun onResponse(
                        call: Call<TranscribeJobResponse>,
                        response: Response<TranscribeJobResponse>
                    ) {
                        CommonUtils.dismissDialoge()
                        if (response.body() != null && response.code() == 200) {
                            if (response.body()!!.data != null) {

                                Toast.makeText(
                                    this@AnalysisScreen,
                                    "Your Journal is being uploaded",
                                    Toast.LENGTH_LONG
                                ).show()
                                Log.d(
                                    "Authtoken",
                                    "CreateTranscribe Response ${Gson().toJson(response.body())}"
                                )

                                Handler().postDelayed({
                                    startService(
                                        Intent(applicationContext, BackgroundService::class.java)
                                            .putExtra("jobID", response.body()!!.data.jobId)
                                    )
                                }, 10000)

                                if (alertcomingFrom.equals(
                                        "back",
                                        true
                                    ) || alertcomingFrom.equals("retake", true)
                                ) {
                                    finish()
                                } else if (alertcomingFrom.equals("home", true)) {
                                    val intent =
                                        Intent(this@AnalysisScreen, NewDashboardActivity::class.java)
                                    intent.flags =
                                        Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                                    startActivity(intent)
                                    finish()
                                } else if (alertcomingFrom.equals("history", true)) {
                                    startActivity(
                                        Intent(this@AnalysisScreen, SondeHistory::class.java)
                                            .putExtra("comingFrom", comingFrom)
                                    )
                                }
                            }
                        }
                    }

                    override fun onFailure(call: Call<TranscribeJobResponse>, t: Throwable) {
                        CommonUtils.dismissDialoge()
                    }

                })

        } catch (e: Exception) {
            CommonUtils.dismissDialoge()
            e.toString()
        }
    }

    fun getTranscribeText(jobId: String, context: Context) {
        try {
            val request = PollTranscribeRequest(
                jobId,
                SharedPref.getSondeToken(),
                SharedPref.getWellnessTopic()
            )
            val apiInterface = RetrofitHandler.apiInterface()
            apiInterface.pollTranscribeJob(SharedPref.getAuthToken(), request)
                .enqueue(object : Callback<PollTranscribeResponse> {
                    override fun onResponse(
                        call: Call<PollTranscribeResponse>,
                        response: Response<PollTranscribeResponse>
                    ) {
                        try {
                            if (response.body() != null && response.code() == 200) {
                                //SharedPref.putDiaryConcern(false)
                                Log.d(
                                    "Authtoken",
                                    "getTranscribeText Response ${Gson().toJson(response.body()!!.data)}"
                                )
                                NewDashboardHelper.context?.stopService(
                                    Intent(
                                        NewDashboardHelper.context,
                                        BackgroundService::class.java
                                    )
                                )
                                Toast.makeText(
                                    NewDashboardHelper.context,
                                    "Your Journal has been successfully added to My diary",
                                    Toast.LENGTH_LONG
                                ).show()
                            } else {
                                diaryConcern = false
                                //SharedPref.putDiaryConcern(false)
                                Log.d(
                                    "Authtoken",
                                    "getTranscribeText Response ${Gson().toJson(response.code())}"
                                )
                                Toast.makeText(
                                    NewDashboardHelper.context,
                                    "Not able to update diary",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        } catch (ex: Exception) {
                            ex.message?.let { Log.d("Audio Error", it) }
                        }
                    }

                    override fun onFailure(call: Call<PollTranscribeResponse>, t: Throwable) {
                        try {
                            diaryConcern = false
                            //SharedPref.putDiaryConcern(false)
                            Log.d("Authtoken", "getTranscribeText Response ${t.toString()}")
                            Toast.makeText(
                                NewDashboardHelper.context,
                                "Not able to update diary",
                                Toast.LENGTH_LONG
                            ).show()
                        } catch (ex: Exception) {
                            ex.message?.let { Log.d("Audio Error", it) }
                        }
                    }

                })

        } catch (e: Exception) {
            e.toString()
        }
    }

    private fun addDiary(alertcomingFrom: String) {
        try {
            val alertBuilder = AlertDialog.Builder(this)
            val bindingNickName: RefferelCodeDialogeLayoutBinding = DataBindingUtil.inflate(
                LayoutInflater.from(this),
                R.layout.refferel_code_dialoge_layout,
                null,
                false
            )
            alertBuilder.setView(bindingNickName.root)
            val alertDialog = alertBuilder.create()
            alertDialog.setCancelable(false)
            if (!alertDialog.isShowing)
                alertDialog.show()

            bindingNickName.edtRefferal.visibility = View.GONE
            bindingNickName.wellnessDiaryLayout.visibility = View.VISIBLE
            bindingNickName.tvName.text = getString(R.string.mental_health_back_heading)
            bindingNickName.btnSubmit.text = "Yes"
            bindingNickName.btnCancel.text = "No"

            bindingNickName.btnCancel.setOnClickListener {
                alertDialog.dismiss()
                APILogs.activityTracker("A_DB_BI_AZ_KYW_MW_BR_Retake_No", this@AnalysisScreen)
                if (alertcomingFrom.equals("back", true) || alertcomingFrom.equals(
                        "retake",
                        true
                    )
                ) {
                    finish()
                } else if (alertcomingFrom.equals("home", true)) {
                    val intent = Intent(this, NewDashboardActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                    startActivity(intent)
                    finish()
                } else if (alertcomingFrom.equals("history", true)) {
                    startActivity(
                        Intent(this, SondeHistory::class.java)
                            .putExtra("comingFrom", comingFrom)
                    )
                }
            }


            bindingNickName.btnSubmit.setOnClickListener {
                alertDialog.dismiss()
                APILogs.activityTracker("A_DB_BI_AZ_KYW_MW_BR_STD", this@AnalysisScreen)
                CreateTranscribe(alertcomingFrom)
            }


            val displayRectangle = Rect()
            val window = window

            window.decorView.getWindowVisibleDisplayFrame(displayRectangle)
            alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            alertDialog.window!!.setLayout(
                (displayRectangle.width() *
                        0.88f).toInt(), RelativeLayout.LayoutParams.WRAP_CONTENT
            )


        } catch (e: Exception) {
            e.toString()
        }
    }

    override fun onScratchComplete() {
        TODO("Not yet implemented")
        if (quizreward != null) {
            QuizRewardDialog.QuizScratchCard(this@AnalysisScreen, quizreward!!.transId, true)
        }
    }

    override fun onScratchProgress(
        scratchCardLayout: ScratchCardLayout,
        i: Int
    ) {
        if (i > 20) {
            scratchCardLayout.onFullReveal()

            if (isStamp) {
                isStamp = false
                scratchTokenReward()
            }


            val handler = Handler(Looper.getMainLooper())
            handler.postDelayed({
                if (alertDialogBonusRewards != null && alertDialogBonusRewards!!.isShowing()) {
                    alertDialogBonusRewards!!.dismiss()
                }
                if ((alertDialogBonusStamp != null && alertDialogBonusStamp!!.isShowing())) {
                    alertDialogBonusStamp!!.dismiss()
                }
                if ((alertDialogStamp != null && alertDialogStamp!!.isShowing())) {
                    alertDialogStamp!!.dismiss()
                }
            }, 3000)
        }

    }

    override fun onScratchStarted() {
        TODO("Not yet implemented")
    }


    private fun scratchTokenReward() {
        val request = RewardsPopupRequest(stampId)
        val call = apiInterfaceWyh!!.scratchTokenReward(SharedPref.getAuthToken(), request)
        call.enqueue(object : Callback<CommonSuccessResponse?> {
            override fun onResponse(
                call: Call<CommonSuccessResponse?>,
                response: Response<CommonSuccessResponse?>
            ) {
                if (response.body() != null && response.code() == 200) {
                    Analytics.logEvent(
                        context,
                        context.javaClass.name,
                        getString(R.string.scratch_token_reward_success)
                    )
                } else {
                    Analytics.logEvent(
                        context,
                        context.javaClass.name,
                        getString(R.string.scratch_token_reward_failed)
                    )
                    Toast.makeText(
                        context,
                        resources.getString(R.string.error_string),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<CommonSuccessResponse?>, t: Throwable) {
                Analytics.logEvent(
                    context,
                    context.javaClass.name,
                    getString(R.string.scratch_token_reward_failed)
                )
                Toast.makeText(
                    context,
                    resources.getString(R.string.internet_connection),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun showBonusRewardsPopup(rewards: String, context: Context) {
        val alertBuilder = AlertDialog.Builder(context)

        val binding: LayoutNewPointsPopUpBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.layout_new_points_pop_up,
            null,
            false
        )
        alertBuilder.setView(binding.getRoot())
        alertDialogBonusRewards = alertBuilder.create()
        alertDialogBonusRewards!!.setCancelable(false)
        if (!alertDialogBonusRewards!!.isShowing()) alertDialogBonusRewards!!.show()

        val title = rewards.split(";".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
        val message = rewards.split(";".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]

        val points = message.replace("[^0-9]".toRegex(), "")
        alertDialogBonusRewards!!.setOnDismissListener(DialogInterface.OnDismissListener { dialogInterface: DialogInterface? ->
            if (isPositiveBtn) {
                isPositiveBtn = false
                val intent = Intent(this, RewardsActivity::class.java)
                intent.putExtra("currentIndex", 0)
                startActivity(intent)
                finish()
            } else {
                showRewardsPopupDialogBox()
            }
        })

        binding.tvPoints.setText(points)
        binding.tvEventName.setVisibility(View.VISIBLE)


        binding.btnPositive.setOnClickListener { view ->
            isPositiveBtn = true
            alertDialogBonusRewards!!.dismiss()
        }
        binding.ivClose.setOnClickListener { view ->
            alertDialogBonusRewards!!.dismiss()
            showRewardsPopupDialogBox()
        }
        binding.scratchView.setScratchListener(this@AnalysisScreen)
        binding.scratchView.setScratchDrawable(getDrawable(R.drawable.scratch_card_orange_new))

        binding.btnNegative.setOnClickListener { view -> alertDialogBonusRewards!!.dismiss() }

        val displayRectangle = Rect()
        val window = (context as AnalysisScreen).window

        window.decorView.getWindowVisibleDisplayFrame(displayRectangle)
        alertDialogBonusRewards!!.getWindow()!!
            .setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        alertDialogBonusRewards!!.getWindow()!!.setLayout(
            (displayRectangle.width() *
                    0.7f).toInt(), RelativeLayout.LayoutParams.WRAP_CONTENT
        )
    }

    private fun showRewardsPopupDialogBox() {
        if (NewDashboardHelper.popUpShowModels.size > 0) {
            var i = 0
            var firstData = NewDashboardHelper.popUpShowModels[i]
            if (NewDashboardHelper.popUpShowModels.size > 1 && firstData.key == "Rewards") {
                i = 1
                firstData = NewDashboardHelper.popUpShowModels[i]
            }
            when (firstData.key) {
                "Rewards" -> showRewardsPopupNew(firstData.value, context)
                "RewardsBounce" -> showBonusRewardsPopup(firstData.value, context)
                "TokenStamp" -> showStampsPopup(firstData.value, context)
                "TokenStampBounce" -> showBonusStampPopup(firstData.value, context)
                "FeedbackPOPUP" -> {
                    val instance = getInstance()
                    instance.showPopUpFeedback(
                        this@AnalysisScreen,
                        NewDashboardHelper.feedbackResponseData
                    )
                }

                else -> throw IllegalStateException("Unexpected value: " + firstData.key)
            }
            NewDashboardHelper.popUpShowModels.removeAt(i)
        }
    }

    private fun showStampsPopup(rewards: String, context: Context) {
        isStamp = true
        val alertBuilder = AlertDialog.Builder(context)

        val binding: CustomPopupStampsBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.custom_popup_stamps,
            null,
            false
        )
        alertBuilder.setView(binding.getRoot())
        alertDialogStamp = alertBuilder.create()
        alertDialogStamp!!.setCancelable(true)
        if (!alertDialogStamp!!.isShowing()) alertDialogStamp!!.show()


        alertDialogStamp!!.setOnDismissListener(DialogInterface.OnDismissListener { dialogInterface: DialogInterface? ->
            if (isPositiveBtn) {
                isPositiveBtn = false
                val intent = Intent(this, RewardsActivity::class.java)
                intent.putExtra("currentIndex", 1)
                startActivity(intent)
                finish()
            } else {
                showRewardsPopupDialogBox()
            }
        })
        val title = rewards.split(";".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
        val message = rewards.split(";".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
        val points = rewards.split(";".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[2]
        if (rewards.split(";".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray().size == 4) {
            val id = rewards.split(";".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[3]
            stampId = id.toInt()
        }

        /* if(isOrange){
            binding.scratchView.setScratchDrawable(context.getDrawable(R.drawable.scratch_card_orange_new));
        }else{
            binding.scratchView.setScratchDrawable(context.getDrawable(R.drawable.scratch_card_pink_new));
        }*/
//        String points = message.replaceAll("[^0-9]", "");
        binding.tvTitle.setText(title)
        binding.tvDescription.setText(message)
        binding.tvDescription2.setText("No. of Stamps: $points")


        /*if (rewards.contains("First Login")) {
            binding.btnNegative.setVisibility(View.GONE);
            binding.btnPositive.setText("OK");
            binding.btnPositive.setOnClickListener(view -> {
                alertDialog.dismiss();
                Intent intent = new Intent(context, SyncDeviceActivity.class);
                startActivity(intent);
            });
        } else {

        }*/

//        binding.scratchView.onFullReveal();
        binding.btnPositive.setOnClickListener { view ->
            isPositiveBtn = true
            alertDialogStamp!!.dismiss()
        }

        binding.ivClose.setOnClickListener { view ->
            alertDialogStamp!!.dismiss()
            showRewardsPopupDialogBox()
        }

        binding.scratchView.setScratchListener(this)
        binding.scratchView.setScratchDrawable(getDrawable(R.drawable.scratch_card_pink_new))

        binding.btnNegative.setOnClickListener { view -> alertDialogStamp!!.dismiss() }

        val displayRectangle = Rect()
        val window = (context as PendingActivityDashboard).window

        window.decorView.getWindowVisibleDisplayFrame(displayRectangle)
        alertDialogStamp!!.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        alertDialogStamp!!.getWindow()!!.setLayout(
            (displayRectangle.width() *
                    0.7f).toInt(), RelativeLayout.LayoutParams.WRAP_CONTENT
        )
    }

    private fun showBonusStampPopup(rewards: String, context: Context) {
        isStamp = true
        val alertBuilder = AlertDialog.Builder(context)

        val binding: CustomPopupStampsBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.custom_popup_stamps,
            null,
            false
        )
        alertBuilder.setView(binding.getRoot())
        alertBuilder.setView(binding.getRoot())
        alertDialogBonusStamp = alertBuilder.create()
        alertDialogBonusStamp!!.setCancelable(true)
        if (!alertDialogBonusStamp!!.isShowing()) alertDialogBonusStamp!!.show()

        alertDialogBonusStamp!!.setOnDismissListener(DialogInterface.OnDismissListener { dialogInterface: DialogInterface? ->
            if (isPositiveBtn) {
                isPositiveBtn = false
                val intent = Intent(this, RewardsActivity::class.java)
                intent.putExtra("currentIndex", 1)
                startActivity(intent)
                finish()
            } else {
                showRewardsPopupDialogBox()
            }
        })
        val title = rewards.split(";".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
        val message = rewards.split(";".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
        val points = rewards.split(";".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[2]
        if (rewards.split(";".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray().size == 4) {
            val id = rewards.split(";".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[3]
            stampId = id.toInt()
        }

        binding.tvTitle.setText("Milestone Points")
        binding.tvDescription.setText("")
        binding.tvDescription2.setText("No. of Stamps: $points")

        binding.btnPositive.setOnClickListener { view ->
            isPositiveBtn = true
            alertDialogBonusStamp!!.dismiss()
        }
        binding.ivClose.setOnClickListener { view ->
            alertDialogBonusStamp!!.dismiss()
            showRewardsPopupDialogBox()
        }
        binding.scratchView.setScratchListener(this@AnalysisScreen)
        binding.scratchView.setScratchDrawable(getDrawable(R.drawable.scratch_card_orange_new))

        binding.btnNegative.setOnClickListener { view -> alertDialogBonusStamp!!.dismiss() }

        val displayRectangle = Rect()
        val window = (context as AnalysisScreen).window

        window.decorView.getWindowVisibleDisplayFrame(displayRectangle)
        alertDialogBonusStamp!!.getWindow()!!
            .setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        alertDialogBonusStamp!!.getWindow()!!.setLayout(
            (displayRectangle.width() *
                    0.7f).toInt(), RelativeLayout.LayoutParams.WRAP_CONTENT
        )
    }

    private fun showRewardsPopupNew(rewards: String, context: Context) {
        val alertBuilder = AlertDialog.Builder(context)

        val binding: LayoutNewPointsPopUpBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.layout_new_points_pop_up,
            null,
            false
        )
        alertBuilder.setView(binding.getRoot())
        val alertDialog = alertBuilder.create()
        alertDialog.setCancelable(true)
        if (!alertDialog.isShowing) alertDialog.show()

        val title = rewards.split(";".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
        val message = rewards.split(";".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]

        val points = message.replace("[^0-9]".toRegex(), "")

        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({ alertDialog.dismiss() }, 3000)


        alertDialog.setOnDismissListener { dialogInterface: DialogInterface? ->
            if (isPositiveBtn) {
                isPositiveBtn = false
                val intent = Intent(this, RewardsActivity::class.java)
                intent.putExtra("currentIndex", 0)
                startActivity(intent)
                finish()
            } else {
                showRewardsPopupDialogBox()
            }
        }

        binding.tvPoints.setText(points)
        binding.tvEventName.setVisibility(View.VISIBLE)
        binding.tvEventName.setText(title)

        binding.ivClose.setOnClickListener { view ->
            alertDialog.dismiss()
        }

        binding.scratchView.onFullReveal()

        binding.btnPositive.setOnClickListener { view ->
            isPositiveBtn = true
            alertDialog.dismiss()
        }
        binding.scratchView.setScratchListener(this@AnalysisScreen)

        binding.btnNegative.setOnClickListener { view -> alertDialog.dismiss() }

        val displayRectangle = Rect()
        val window = window

        window.decorView.getWindowVisibleDisplayFrame(displayRectangle)
        alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))


        alertDialog.window!!.setLayout(
            (displayRectangle.width() *
                    0.67f).toInt(), RelativeLayout.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onDialogDismiss() {
        cancelDialog()
    }

    fun cancelDialog() {
        try {
            if (spinRewardData != null) {
                NudgeDialogue.spinnerCancelDialog(context, spinRewardData!!.getRewardType(), this)
            } else if (quizreward != null) {
                NudgeDialogue.spinnerCancelDialog(context, quizreward!!.getRewardType(), this)
            }

        } catch (e: java.lang.Exception) {
            // Log the exception for debugging
            e.printStackTrace()
        }
    }

    private fun getQuizathonRewardPopup(context: Context, data: QuizathonRewardData?) {
        try {
            quizreward=data;
            if (data != null && data.rewardType != null && !data.rewardType.isEmpty()) {
                val rewardtype = data.rewardType
                if (rewardtype.equals("Points", ignoreCase = true)) {
                    QuizRewardDialog.showPostSpinnerPointsPopupCallBack(
                        data.rewardTitle,
                        context,
                        "Trends",
                        data.rewardDescription,
                        data.rewardHeader1,
                        data.rewardHeader2,
                        this@AnalysisScreen,
                        this@AnalysisScreen
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
                        this@AnalysisScreen,
                        this@AnalysisScreen
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
                        this@AnalysisScreen,
                        this@AnalysisScreen
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
                        this@AnalysisScreen,
                        this@AnalysisScreen
                    )
                } else if (rewardtype.equals("Stamps", ignoreCase = true)) {
                    QuizRewardDialog.showStampsPopupCallBack(
                        data.rewardHeader1,
                        data.rewardHeader2,
                        data.rewardTitle,
                        data.rewardValue,
                        context,
                        this@AnalysisScreen,
                        this@AnalysisScreen
                    )
                }
            }
        } catch (ex: java.lang.Exception) {
            ex.printStackTrace()
        }
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
                        this@AnalysisScreen,
                        this@AnalysisScreen
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
                        this@AnalysisScreen,
                        this@AnalysisScreen
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
                        this@AnalysisScreen,
                        this@AnalysisScreen
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
                        this@AnalysisScreen,
                        this@AnalysisScreen
                    )
                } else if (rewardtype.equals("Stamps", ignoreCase = true)) {
                    PostSpinDialog.showStampsPopupCallBack(
                        data.rewardHeader1,
                        data.rewardHeader2,
                        data.rewardTitle,
                        data.rewardValue,
                        context,
                        this@AnalysisScreen,
                        this@AnalysisScreen
                    )
                }
            }
        } catch (ex: java.lang.Exception) {
            ex.printStackTrace()
        }
    }
}