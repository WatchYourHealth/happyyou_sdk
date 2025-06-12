package com.wyh.happyyousdk.ChallangesModule


import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.esafirm.imagepicker.features.ImagePicker
import com.esafirm.imagepicker.model.Image
import com.google.gson.Gson
import com.wyh.happyyousdk.APIEncryption.APIInterface
import com.wyh.happyyousdk.APIEncryption.APILogs.sendLogs
import com.wyh.happyyousdk.APIEncryption.RetrofitHandler
import com.wyh.happyyousdk.ChallangesModule.Activities.BadgesActivity
import com.wyh.happyyousdk.ChallangesModule.Activities.ChallengeDetailActivity
import com.wyh.happyyousdk.ChallangesModule.Activities.NewTribeChallengeDashboard
import com.wyh.happyyousdk.ChallangesModule.ClickInterface.ActivityClick
import com.wyh.happyyousdk.ChallangesModule.ClickInterface.InviteClick
import com.wyh.happyyousdk.ChallangesModule.ClickInterface.RadioButtonClick
import com.wyh.happyyousdk.ChallangesModule.ClickInterface.SelectSingleTribe
import com.wyh.happyyousdk.ChallangesModule.Utils.TribeChallengeHelper
import com.wyh.happyyousdk.R
import com.wyh.happyyousdk.WebActivity
import com.wyh.happyyousdk.WellBeingActivity
import com.wyh.happyyousdk.WellBeingDisclaimerActivity
import com.wyh.happyyousdk.absorb.HealthHacksActivity
import com.wyh.happyyousdk.absorb.QuickReadActivity
import com.wyh.happyyousdk.common.adapter.IndicatorsWhiteAdapter
import com.wyh.happyyousdk.dass21.Dass21AnalysisActivity
import com.wyh.happyyousdk.dass21.Dass21QuestionsActivity
import com.wyh.happyyousdk.databinding.ActivityChallengeDetailBinding
import com.wyh.happyyousdk.model.CommonSuccessResponse
import com.wyh.happyyousdk.model.request.IntegrationIdRequest
import com.wyh.happyyousdk.model.request.ScratchRequest
import com.wyh.happyyousdk.model.request.StartRequest
import com.wyh.happyyousdk.model.request.quizathon.QuizScratchRequest
import com.wyh.happyyousdk.model.response.BadgesData
import com.wyh.happyyousdk.model.response.ChallenegActivities
import com.wyh.happyyousdk.model.response.HappyMartSubCategoryChildResponse
import com.wyh.happyyousdk.model.response.ScratchResponse
import com.wyh.happyyousdk.model.response.StartResponse
import com.wyh.happyyousdk.model.response.dass21.DassAnalysisResponse
import com.wyh.happyyousdk.model.response.faceScan.GetFaceKeysResponse
import com.wyh.happyyousdk.network.ApiClientWyh
import com.wyh.happyyousdk.network.ApiInterfaceWyh
import com.wyh.happyyousdk.quizathon.QuizathonViewAllActivity
import com.wyh.happyyousdk.trends.TrendsActivity
import com.wyh.happyyousdk.unwind.adapter.ThoughtAdapter
import com.wyh.happyyousdk.utils.Analytics
import com.wyh.happyyousdk.utils.CommonUtils
import com.wyh.happyyousdk.utils.Constants
import com.wyh.happyyousdk.utils.SharedPref
import com.wyh.happyyousdk.utils.SnapHelperOneByOne
import dev.skymansandy.scratchcardlayout.listener.ScratchListener
import dev.skymansandy.scratchcardlayout.ui.ScratchCardLayout
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.net.URL
import java.net.URLConnection
import java.util.concurrent.Executor
import java.util.concurrent.Executors


@SuppressLint("StaticFieldLeak")
object helperClass : InviteClick, ActivityClick, ScratchListener, SelectSingleTribe, RadioButtonClick {

    val apiInterface = RetrofitHandler.getRetrofitInstance().create(APIInterface::class.java)
    var journalText = ""
    var activityType = ""
    var activityID = ""
    lateinit var alertDialog: AlertDialog
    lateinit var scracthCardDialog: AlertDialog
    lateinit var alertDialogShare: AlertDialog
    var ChallengeID = ""
    lateinit var mContext: Context
    lateinit var list: RecyclerView
    lateinit var redirection: String
    lateinit var binding: ActivityChallengeDetailBinding
    var tribeListId: ArrayList<Int> = java.util.ArrayList()
    var listChallengeId: ArrayList<String> = ArrayList()
    lateinit var window: Window
    var othersCategory : ArrayList<HappyMartSubCategoryChildResponse> = arrayListOf()


    @RequiresApi(Build.VERSION_CODES.O)
    private fun shareImages(
        context: Context,
        data: ArrayList<String>,
        challengeActivity: ChallenegActivities,
        challengeID: String,
        shareImg: String,
        recyclerView: RecyclerView?,
        binding: ActivityChallengeDetailBinding?
    ) {
        try {
            var positionShareImage: Int = 0
            challengeActivity.isStarted = true
            var dismissDialog: Boolean = false
            val alertBuilder = AlertDialog.Builder(context, R.style.CustomAlertDialogPink)
            val view = LayoutInflater.from(context).inflate(R.layout.thought_pop_up, null, false)
            alertBuilder.setView(view)

            alertDialogShare = alertBuilder.create()

            val logoImage = view.findViewById<ImageView>(R.id.iv_delete_record)
            val closePopup = view.findViewById<ImageView>(R.id.iv_close_record)
            val share = view.findViewById<ImageView>(R.id.ivShare)
            val btnOk = view.findViewById<Button>(R.id.btnOk)
            val rvThought = view.findViewById<RecyclerView>(R.id.rv_thought)
            val rvThoughtIndicator = view.findViewById<RecyclerView>(R.id.rv_thought_indicator)

            logoImage.visibility = View.INVISIBLE
            btnOk.text = "Close"
            btnOk.setBackgroundResource(R.drawable.pink_rc_bg_8dp)


            alertDialogShare.setOnDismissListener {
                if (!dismissDialog) {
                    journalUploadDialog(context, challengeActivity, challengeID, shareImg, binding)
                }
            }

            closePopup.setOnClickListener {
                alertDialogShare.dismiss()
            }


            share.setOnClickListener { v ->
                dismissDialog = true
                if (activityType.equals("tribe")) {

                } else if (activityType.equals("tribeandwhatsapp", true)) {
                    TribeChallengeHelper.showSharePopup(
                        positionShareImage,
                        context,
                        data,
                        challengeID,
                        challengeActivity,
                        btnOk
                    )
                } else {
                    getImageURi(data[positionShareImage], context)
                }
            }

            btnOk.setOnClickListener { v ->
                alertDialogShare.dismiss()
            }

            val thoughtAdapter = ThoughtAdapter(context, data)
            val linearLayoutManager = LinearLayoutManager(context)
            linearLayoutManager.orientation = RecyclerView.HORIZONTAL
            rvThought.layoutManager = linearLayoutManager
            rvThought.adapter = thoughtAdapter
            rvThought.onFlingListener = null

            val trendingLinearSnapHelper: LinearSnapHelper = SnapHelperOneByOne()
            trendingLinearSnapHelper.attachToRecyclerView(rvThought)


            val linearLayoutManager1 = LinearLayoutManager(context)
            linearLayoutManager1.orientation = RecyclerView.HORIZONTAL
            val indicatorsAdapter = IndicatorsWhiteAdapter(context, data.size, 0)
            rvThoughtIndicator.adapter = indicatorsAdapter
            rvThoughtIndicator.layoutManager = linearLayoutManager1

            rvThought.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        positionShareImage =
                            if (linearLayoutManager.findFirstCompletelyVisibleItemPosition() != -1) {
                                linearLayoutManager.findFirstCompletelyVisibleItemPosition()
                            } else linearLayoutManager.findFirstVisibleItemPosition()
                        indicatorsAdapter.updateSelectedIndex(positionShareImage)
                    }
                }
            })



            alertDialogShare.setCancelable(false)
            alertDialogShare.show()

            val displayRectangle = Rect()
            val window = (context as ChallengeDetailActivity).window

            window.decorView.getWindowVisibleDisplayFrame(displayRectangle)
            alertDialogShare.window!!.setLayout(
                (displayRectangle.width() * 0.7f).toInt(),
                (displayRectangle.height() * 0.6f).toInt()
            )


        } catch (e: Exception) {
            e.toString()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun journalUploadDialog(
        context: Context,
        challengeActivity: ChallenegActivities,
        challengeID: String,
        shareImg: String,
        recyclerView: ActivityChallengeDetailBinding?
    ) {
        try {
            val alertBuilder = AlertDialog.Builder(recyclerView!!.root.context, R.style.CustomAlertDialog)
            val view = LayoutInflater.from(recyclerView!!.root.context)
                .inflate(R.layout.challenge_journal_upload_dialog, null, false)
            alertBuilder.setView(view)
            binding = recyclerView!!
            val logoImage = view.findViewById<ImageView>(R.id.challenge_logo_img)
            val whatToDo = view.findViewById<TextView>(R.id.challenge_dialog_whattodo_tv)
            val howToDo = view.findViewById<TextView>(R.id.challenge_dialog_tvhowtodo_tv)
            val whyToDo = view.findViewById<TextView>(R.id.challenge_dialog_tvwhytodo)
            val journalDesc = view.findViewById<TextView>(R.id.journal_desc)
            val startButton = view.findViewById<Button>(R.id.challenge_dialog_start_btn)
            val closeButton = view.findViewById<Button>(R.id.challenge_dialoge_close_btn)
            val challengeJournalEt = view.findViewById<EditText>(R.id.challenge_journal_et)
            val challengeJournalDespLayout =
                view.findViewById<LinearLayout>(R.id.challenge_journal_desp_layout)
            val challengeDialogTitleTv = view.findViewById<TextView>(R.id.challenge_dialog_title_tv)
            val challengeCompleteDialoge =
                view.findViewById<LinearLayout>(R.id.challenge_complete_dialoge)
            val challengeBtnLayout = view.findViewById<RelativeLayout>(R.id.challenge_btn_layout)
            val buyBtn = view.findViewById<Button>(R.id.buy_btn)
            val closeBtn = view.findViewById<Button>(R.id.close_btn)
            val shareImgView = view.findViewById<ImageView>(R.id.share_img)
            val shareJokesList = view.findViewById<RecyclerView>(R.id.share_jokes_list)

            ChallengeID = challengeID
            mContext = recyclerView!!.root.context
            if (recyclerView!!.challengeDetailsRecylcer != null) {
                list = recyclerView!!.challengeDetailsRecylcer
            }

            alertDialog = alertBuilder.create()
            activityType = challengeActivity.activityType

            challengeDialogTitleTv.text = challengeActivity.activityName
            Glide.with(mContext).load(challengeActivity.activityImage).into(logoImage)

            if (challengeActivity.progressPercentage.toString() == "100" || challengeActivity.isCompleted) {
                if (activityType.equals("Share", true) || activityType.equals("tribe",
                        true
                    ) || activityType.equals("tribeandwhatsapp", true)) {
                    challengeCompleteDialoge.visibility = View.GONE
                    challengeJournalDespLayout.visibility = View.GONE
                    challengeBtnLayout.visibility = View.VISIBLE
                    startButton.text = "Share"

                } else {
                    challengeCompleteDialoge.visibility = View.VISIBLE
                    challengeJournalDespLayout.visibility = View.GONE
                    challengeBtnLayout.visibility = View.GONE
                }
            } else {
                challengeCompleteDialoge.visibility = View.GONE
                challengeBtnLayout.visibility = View.VISIBLE
                if (!challengeActivity.redirection.equals("") && !challengeActivity.isStarted) {
                    if (challengeActivity.activityType.equals("HappyMart", true)) {
                        buyBtn.visibility = View.VISIBLE
                        buyBtn.text = "Visit Link"
                        startButton.text = "Upload"
                        startButton.background =
                            context.resources.getDrawable(R.drawable.blue_rc_bg_8dp)
                    } else {
                        buyBtn.visibility = View.VISIBLE
                    }
                } else {
                    if (challengeActivity.activityType.equals("HappyMart", true)) {
                        buyBtn.visibility = View.VISIBLE
                        buyBtn.text = "Visit Link"
                        startButton.text = "Upload"
                        startButton.background =
                            context.resources.getDrawable(R.drawable.grey_rc_bg_8dp)
                    } else {
                        // buyBtn.visibility = View.VISIBLE
                        buyBtn.visibility = View.GONE
                    }
                }

                if (activityType.equals("upload", true)) {
                    challengeJournalDespLayout.visibility = View.GONE
                    if (challengeActivity.isStarted) {
                        startButton.text = "Submit"
                    } else {
                        startButton.text = "Start"
                    }
                } else if (activityType.equals("journal", true)) {
                    challengeJournalEt.hint =
                        "Please write more than 15 characters about the activity"
                    if (challengeActivity.isStarted) {
                        startButton.text = "Submit"
                        challengeJournalDespLayout.visibility = View.VISIBLE

                    } else {
                        startButton.text = "Start"
                        challengeJournalDespLayout.visibility = View.GONE
                    }
                } else if (activityType.equals("water", ignoreCase = true
                    ) || activityType.equals(
                        "das",
                        ignoreCase = true
                    ) || activityType.equals(
                        "wellbeing",
                        ignoreCase = true
                    ) || challengeActivity.activityType.equals("sonde", true)) {
                    if (challengeActivity.isStarted) {
                        startButton.text = "Submit"
                    } else {
                        startButton.text = "Start"
                    }
                    challengeJournalDespLayout.visibility = View.GONE
                } else if (activityType.equals("stepandnote", ignoreCase = true)) {
                    if (challengeActivity.isStarted) {
                        if(challengeActivity.progressPercentage >= 50){
                            startButton.text = "Submit"
                        }else{
                            startButton.text = "Complete"
                        }
                    } else {
                        startButton.text = "Start"
                    }

                    if (challengeActivity.progressPercentage >= 50) {
                        challengeJournalDespLayout.visibility = View.VISIBLE
                    } else {
                        challengeJournalDespLayout.visibility = View.GONE
                    }
                }else if(challengeActivity.activityType.equals("step", true) || challengeActivity.activityType.equals("steps", true)){
                    if (challengeActivity.isStarted) {
                        startButton.text = "Complete"
                    } else {
                        startButton.text = "Start"
                    }
                    challengeJournalDespLayout.visibility = View.GONE
                }else if(challengeActivity.activityType.equals("quiz",true)){
                    challengeJournalDespLayout.visibility = View.GONE
                    if(challengeActivity.isStarted){
                        startButton.text = "Complete"
                    }else{
                        startButton.text = "Start"
                    }
                }else if(challengeActivity.activityType.equals("facescanandnote",true)){
                    if(challengeActivity.isStarted){
                        if(challengeActivity.progressPercentage >= 50){
                            challengeJournalDespLayout.visibility = View.VISIBLE
                        }else{
                            challengeJournalDespLayout.visibility = View.GONE
                        }
                        startButton.text = "Complete"
                    }else{
                        challengeJournalDespLayout.visibility = View.GONE
                        startButton.text = "Start"
                    }
                }
                else if (activityType.equals("Share", true) || activityType.equals(
                        "tribe",
                        true
                    ) || activityType.equals("tribeandwhatsapp", true)
                ) {
                    if (challengeActivity.isStarted) {
                        startButton.text = "Share"
                        shareImgView.visibility = View.GONE
                        challengeJournalDespLayout.visibility = View.GONE
                        Glide.with(mContext)
                            .load(shareImg)
                            .into(shareImgView)

                    } else {
                        startButton.text = "Start"
                        shareJokesList.visibility = View.GONE
                        // shareImgView.visibility = View.GONE
                        challengeJournalDespLayout.visibility = View.GONE
                    }
                } else if (activityType.equals("beforeafterupload", true)) {
                    challengeJournalDespLayout.visibility = View.GONE
                    if (challengeActivity.isStarted) {
                        startButton.text = "Submit"

                    } else {
                        startButton.text = "Start"
                    }
                } else {
                    challengeJournalEt.hint =
                        "Please write more than 15 characters about the activity"
                    if (challengeActivity.isStarted) {
                        if (challengeActivity.activityType.equals("HappyMart", true)) {
                            buyBtn.visibility = View.VISIBLE
                            startButton.background =
                                context.resources.getDrawable(R.drawable.blue_rc_bg_8dp)
                            startButton.text = "Submit"
                        } else if (challengeActivity.activityType.equals("Article", true)) {
                            startButton.text = "Visit"
                            challengeJournalDespLayout.visibility = View.GONE
                        } else if (challengeActivity.activityType.contains("Article_", true)) {
                            startButton.text = "Visit"
                            challengeJournalDespLayout.visibility = View.GONE
                        } else {
                            startButton.background =
                                context.resources.getDrawable(R.drawable.blue_rc_bg_8dp)
                            startButton.text = "Submit"
                        }
                        //challengeJournalDespLayout.visibility = View.VISIBLE
                    } else {
                        if (challengeActivity.activityType.equals("HappyMart", true)) {
                            buyBtn.visibility = View.VISIBLE
                            buyBtn.text = "Visit Link"
                            startButton.text = "Upload"
                            startButton.background =
                                context.resources.getDrawable(R.drawable.grey_rc_bg_8dp)
                        } else {
                            startButton.background =
                                context.resources.getDrawable(R.drawable.blue_rc_bg_8dp)
                            startButton.text = "Start"
                            challengeJournalDespLayout.visibility = View.GONE
                        }
                    }
                }
            }

            closeBtn.setOnClickListener {
                alertDialog.dismiss()
            }

            buyBtn.setOnClickListener {
                startButton.background = context.resources.getDrawable(R.drawable.blue_rc_bg_8dp)
                if (!challengeActivity.isStarted) {
                    recyclerView?.let { it1 ->
                        startChallange(
                            challengeActivity.activityId.toString(),
                            challengeID,
                            mContext,
                            startButton,
                            challengeJournalDespLayout,
                            challengeJournalEt,
                            recyclerView!!.challengeDetailsRecylcer,
                            shareImgView,
                            shareImg,
                            challengeJournalDespLayout,
                            buyBtn,
                            challengeActivity
                        )
                    }
                } else {
                    var redirectionURL = ""
                    if (challengeActivity.redirection.contains("{userid}")) {
                        redirectionURL = challengeActivity.redirection.replace(
                            "{userid}",
                            SharedPref.getAesUuid()
                        )
                    } else {
                        redirectionURL = challengeActivity.redirection
                    }
                    val intent = Intent(mContext, WebActivity::class.java)
                    intent.putExtra("Url", redirectionURL)
                    intent.putExtra("loginUrl", "")
                    mContext.startActivity(intent)
                }
            }


            startButton.setOnClickListener {
                if (startButton.text.equals("Start")) {
                    recyclerView?.let { it1 ->
                        startChallange(
                            challengeActivity.activityId.toString(),
                            challengeID,
                            mContext,
                            startButton,
                            challengeJournalDespLayout,
                            challengeJournalEt,
                            recyclerView!!.challengeDetailsRecylcer,
                            shareImgView,
                            shareImg,
                            challengeJournalDespLayout,
                            buyBtn,
                            challengeActivity
                        )
                    }
                } else {
                    //alertDialog.dismiss()
                    if (activityType.equals("upload", true)) {
                        journalText = ""
                        checkVersionForImage(mContext as Activity)
                    } else if (activityType.equals("HappyMart", true)) {
                        if (challengeActivity.isStarted) {
                            journalText = challengeJournalEt.text.toString()
                            if (journalText.length < 15) {
                                Toast.makeText(
                                    mContext,
                                    mContext.getResources().getString(R.string.at_least_15_char),
                                    Toast.LENGTH_SHORT
                                ).show()
                                //checkVersionForImage(context as Activity)

                            } else {
                                checkVersionForImage(mContext as Activity)
                            }
                        }

                    } else if (activityType.equals("Article", true)) {
                        alertDialog.dismiss()
                        val activity = context as Activity
                        val i = Intent(context, HealthHacksActivity::class.java)
                        i.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                        context.startActivity(i)
                        activity.finish()
                    } else if (activityType.contains("Article_", true)) {
                        alertDialog.dismiss()
                        val activity = context as Activity
                        val i = Intent(context, QuickReadActivity::class.java)
                        i.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                        val code: String = activityType.split("_")[1]
                        i.putExtra("article_code", code)
                        context.startActivity(i)
                        activity.finish()
                    } else if (activityType.equals(
                            "wellbeing",
                            true
                        ) || activityType.equals("sonde", true)
                    ) {
                        alertDialog.dismiss()
                        val activity = context as Activity
                        val i = Intent(context, WellBeingActivity::class.java)
                        i.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                        context.startActivity(i)
                        activity.finish()
                    } else if (activityType.equals("das", true)) {
                        alertDialog.dismiss()
                        val activity = context as Activity
                        getDasAnalysis(context)
                    } else if (activityType.equals("Share", true) || activityType.equals(
                            "tribe",
                            true
                        ) || activityType.equals("tribeandwhatsapp", true)
                    ) {
                        alertDialog.dismiss()
                        activityID = challengeActivity.activityId.toString()
                        val imageList: ArrayList<String> = challengeActivity.shareContent
                        shareImages(
                            mContext,
                            imageList,
                            challengeActivity,
                            SharedPref.getChallengeID(),
                            shareImg,
                            recyclerView!!.challengeDetailsRecylcer,
                            recyclerView
                        )
//                        getImageURi(shareImg,context)
                    } else if (activityType.equals("journal", true)) {
                        activityID = challengeActivity.activityId.toString()
                        journalText = challengeJournalEt.text.toString()
                        if (journalText != "" && journalText.length > 15) {
                            val parts = ArrayList<MultipartBody.Part>()
                            recyclerView!!.challengeDetailsRecylcer?.let { it1 ->
                                ChallengeDetailActivity().completeChallenge(
                                    parts, challengeID,
                                    mContext, it1, "", ""
                                )
                            }
                        } else {
                            Toast.makeText(
                                mContext,
                                mContext.getResources().getString(R.string.at_least_15_char),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else if (activityType.equals("step", true) || activityType.equals("steps", true)) {
                        val activity = context as Activity
                        val intent = Intent(context, TrendsActivity::class.java)
                        intent.putExtra("activityType", Constants.STEPS)
                        context.startActivity(intent)
                        activity.finish()
                    }else if(activityType.equals("quiz",true)){
                        val activity = context as Activity
                        val intent = Intent(context, QuizathonViewAllActivity::class.java).apply {
                            putExtra("type", "quiz")
                            putExtra("quiz_cat", "All")
                            putExtra("name", "Play and Learn")
                        }
                        context.startActivity(intent)
                        activity.finish()
                    }else if(activityType.equals("facescanandnote",true)){
                        if(challengeActivity.progressPercentage >= 50){
                            challengeJournalDespLayout.visibility = View.VISIBLE
                            activityID = challengeActivity.activityId.toString()
                            journalText = challengeJournalEt.text.toString()
                            if (journalText != "" && journalText.length > 15) {
                                val parts = ArrayList<MultipartBody.Part>()
                                recyclerView!!.challengeDetailsRecylcer?.let { it1 ->
                                    ChallengeDetailActivity().completeChallenge(
                                        parts, challengeID,
                                        mContext, it1, "", ""
                                    )
                                }
                            } else {
                                Toast.makeText(
                                    mContext,
                                    mContext.getResources().getString(R.string.at_least_15_char),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }else{
                            alertDialog.dismiss()
                            SharedPref.putChallengeID(challengeID)
                        }

                    }
                    else if (activityType.equals("stepandnote", true)) {
                        if (challengeActivity.progressPercentage >= 50) {
                            activityID = challengeActivity.activityId.toString()
                            journalText = challengeJournalEt.text.toString()
                            if (journalText != "" && journalText.length > 15) {
                                val parts = ArrayList<MultipartBody.Part>()
                                recyclerView!!.challengeDetailsRecylcer?.let { it1 ->
                                    ChallengeDetailActivity().completeChallenge(
                                        parts, challengeID,
                                        mContext, it1, "", ""
                                    )
                                }
                            } else {
                                Toast.makeText(
                                    mContext,
                                    mContext.getResources().getString(R.string.at_least_15_char),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            val activity = context as Activity
                            val intent = Intent(context, TrendsActivity::class.java)
                            intent.putExtra("activityType", Constants.STEPS)
                            context.startActivity(intent)
                            activity.finish()
                        }

                    } else if (activityType.equals("beforeafterupload", true)) {
                        journalText = ""
                        checkVersionForImage(mContext as Activity)
                    } else if (activityType.equals("water", true)) {
                        alertDialog.dismiss()
                        val activity = context as Activity
                        val intent = Intent(context, TrendsActivity::class.java)
                        intent.putExtra("activityType", Constants.WATER)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                        context.startActivity(intent)
                        activity.finish()
                    } else {
                        journalText = challengeJournalEt.text.toString()
                        if (journalText.length <= 15) {
                            Toast.makeText(
                                mContext,
                                mContext.getResources().getString(R.string.at_least_15_char),
                                Toast.LENGTH_SHORT
                            ).show()
                            //checkVersionForImage(context as Activity)

                        } else {
                            checkVersionForImage(mContext as Activity)
                        }
                    }
                    activityID = challengeActivity.activityId.toString()
                }
            }

            closeButton.setOnClickListener {
                ChallengeDetailActivity().getChallengeDetails(
                    challengeID,
                    context,
                    this@helperClass,
                    this@helperClass,
                    recyclerView!!.challengeDetailsRecylcer,
                    "",
                    SharedPref.getPositionID()
                )
                alertDialog.dismiss()
            }


            whatToDo.text = challengeActivity.whatToDo
            howToDo.text = challengeActivity.howToDo
            whyToDo.text = challengeActivity.whyToDo
            journalDesc.text = challengeActivity.activityDesc



            alertDialog.setCancelable(false)
            alertDialog.show()


            val displayRectangle = Rect()
            val window = (mContext as ChallengeDetailActivity).window

            if (activityType.equals("journalupload") || activityType.equals("Share")) {
                window.decorView.getWindowVisibleDisplayFrame(displayRectangle)
                alertDialog.window!!.setLayout(
                    (displayRectangle.width() * 0.8f).toInt(),
                    (displayRectangle.height() * 0.8f).toInt()
                )
            } else {
                window.decorView.getWindowVisibleDisplayFrame(displayRectangle)
                alertDialog.window!!.setLayout(
                    (displayRectangle.width() * 0.8f).toInt(),
                    (displayRectangle.height() * 0.7f).toInt()
                )
            }

        } catch (e: Exception) {
            e.toString()
        }
    }

    override fun onCheckedChange(position: Int) {
        TODO("Not yet implemented")
    }

    override fun setSelectedTribe(communityID: Int) {
        //Toast.makeText(this,communityID,Toast.LENGTH_SHORT).show()

    }


    fun checkVersionForImage(context: Activity) {
        if(ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED){
            ChallengeDetailActivity().requestCameraPermission(context, Manifest.permission.CAMERA,104)
        }else{
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                openImagePicker(context as Activity)
            } else {
                openGalleryOnly(context as Activity)
            }
        }

    }





    fun openGalleryOnly(context: Activity) {
        val limit = if (activityType.equals("beforeafterupload")) 2 else 1
        ImagePicker.create(context)
            .includeVideo(false)
            .imageDirectory("Camera")
            .enableLog(true)
            .includeAnimation(true)
            .limit(limit)
            .showCamera(true)
            .start()
    }

    fun openImagePicker(activity: Activity) {
        try {
            val pickIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            pickIntent.type = "image/*"
            if (activityType.equals("beforeafterupload")) {
                pickIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            }
            pickIntent.action = Intent.ACTION_GET_CONTENT
            activity.startActivityForResult(pickIntent, 1)
        } catch (e: Exception) {
            e.toString()
        }
    }

    fun scracthDialoge(
        context: Context,
        scratchListener: ScratchListener,
        data: BadgesData,
        list: ArrayList<BadgesData>,
        comingFrom: String
    ) {
        try {
            val alertBuilder = AlertDialog.Builder(context)
            val view = LayoutInflater.from(context).inflate(R.layout.scratch_card_layout, null, false)
            alertBuilder.setView(view)
            val challengesScracth =
                view.findViewById<ScratchCardLayout>(R.id.challenges_scrach_view)
            val scratchTitleTv = view.findViewById<TextView>(R.id.scratch_title_tv)
            val scratchImg = view.findViewById<ImageView>(R.id.scratch_img)
            val scratchViewBtn = view.findViewById<Button>(R.id.scratch_view_btn)
            val scratchCloseBtn = view.findViewById<Button>(R.id.scratch_close_btn)

            SharedPref.putTransactionID(data.transactionId);


            scratchCloseBtn.setOnClickListener {
                scracthCardDialog.dismiss()
            }

            scratchViewBtn.setOnClickListener {
                context.startActivity(Intent(context, BadgesActivity::class.java))
            }

            scratchTitleTv.text = "Hurray!! You Won \n${data.badgeName} Badges"
            Glide.with(context).load(data.badgeLogo).into(scratchImg)
            scracthCardDialog = alertBuilder.create()

            challengesScracth.setScratchListener(scratchListener)
            challengesScracth.setScratchDrawable(context.getDrawable(R.drawable.scracth_orange_image))

            alertBuilder.setView(view)


            scracthCardDialog.show()

            scracthCardDialog.setOnDismissListener {
                try {
                    if (list.size != 0) {
                        if (list.size > 0) {
                            list.removeAt(0)
                            scracthDialoge(context, scratchListener, list[0], list, "")
                        } else {
                            scracthCardDialog.dismiss()
                        }
                    } else {
                        scracthCardDialog.dismiss()
                    }

                } catch (e: Exception) {
                    e.toString()
                }

            }

            val displayRectangle = Rect()
            if (comingFrom.equals("normalChallenge", true)) {
                window = (context as ChallengeDetailActivity).window

            } else {
                window = (context as NewTribeChallengeDashboard).window

            }
            window.decorView.getWindowVisibleDisplayFrame(displayRectangle)
            scracthCardDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            if (comingFrom.equals("normalChallenge", true)) {
                scracthCardDialog.getWindow()!!.setLayout(
                    (displayRectangle.width() * 0.7f).toInt(),
                    (displayRectangle.height() * 0.5f).toInt()
                )
            } else {
                scracthCardDialog.getWindow()!!.setLayout(
                    (displayRectangle.width() * 0.7f).toInt(),
                    (displayRectangle.height() * 0.5f).toInt()
                )
            }


        } catch (e: Exception) {
            e.toString()
        }
    }

    fun scratchComplete(context: Context, transactionID: String) {
        try {
            Log.d("AuthToken", "Scracth API Called")
            val scratchRequest = ScratchRequest(transactionID)
            apiInterface.scrathCard(SharedPref.getAuthToken(), scratchRequest)
                .enqueue(object : Callback<ScratchResponse> {
                    override fun onResponse(
                        call: Call<ScratchResponse>,
                        response: Response<ScratchResponse>
                    ) {
                        response.body().let {
                            Log.d("AuthToken", Gson().toJson(response.body()))
                            if (response.code() == 200 && response.body()!!.success) {

                            }
                        }
                    }

                    override fun onFailure(call: Call<ScratchResponse>, t: Throwable) {
                        CommonUtils.dismissDialoge()
                    }

                })
        } catch (e: Exception) {
            CommonUtils.dismissDialoge()
            e.toString()
        }
    }

    fun QuizScratchCard(context: Context, activityTransId: String, IsScratched: Boolean) {
        CommonUtils.showProgressDialige(context)
        val apiInterface = RetrofitHandler.apiInterface()
        var quizScratachRequest: QuizScratchRequest =
            QuizScratchRequest(activityTransId, IsScratched)
        apiInterface.QuizScratchCard(SharedPref.getAuthToken(), quizScratachRequest)
            .enqueue(object : Callback<CommonSuccessResponse> {
                override fun onResponse(
                    call: Call<CommonSuccessResponse>,
                    response: Response<CommonSuccessResponse>
                ) {
                    CommonUtils.dismissDialoge()
                }

                override fun onFailure(call: Call<CommonSuccessResponse>, t: Throwable) {
                    CommonUtils.dismissDialoge()
                }

            })
    }

    fun getImageURi(imageURL: String, context: Context) {
        var bitmapThought: Bitmap? = null
        var uriforthought: Uri? = null
        var fileName = ""

        val thread = Thread {
            try {
                val url = URL(imageURL)
                bitmapThought = BitmapFactory.decodeStream(url.openConnection().getInputStream())
                val cacheDir: File = File(context.getFilesDir(), "MyAppCache")
                if (!cacheDir.exists()) {
                    cacheDir.mkdirs()
                }
                val file = arrayOfNulls<File>(1)
                file[0] = File(cacheDir, "ShareImg" + ".png")
                val executor: Executor =
                    Executors.newSingleThreadExecutor()
                executor.execute {
                    try {
                        val bitmap: Bitmap = (bitmapThought as Bitmap?)!!
                        val fOut =
                            FileOutputStream(file[0])
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut)
                        fOut.flush()
                        file[0]!!.setReadable(true, false)
                        uriforthought = FileProvider.getUriForFile(
                            context, context.getPackageName() + ".provider",
                            file[0]!!
                        )
                        fileName = "ThoughOfTheDay"
                        Handler(Looper.getMainLooper()).postDelayed({
                            alertDialog.dismiss()
                            ChallengeDetailActivity().shareBitmap(
                                uriforthought!!,
                                context as Activity
                            )
                        }, 1000)
                    } catch (e: java.lang.Exception) {
                        Log.d("error", e.message!!)
                    }
                }
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
        thread.start()
    }


    fun startChallange(activityID: String, challengeID: String, context: Context, btn: Button, linearLayout: LinearLayout, editText: EditText,
        recyclerView: RecyclerView,
        img: ImageView,
        imgString: String,
        parent: LinearLayout,
        button: Button,
        challengeActivity: ChallenegActivities
    ): Boolean {
        var serviceResponse: Boolean = false
        try {
            CommonUtils.showProgressDialige(context)
            val startRequest = StartRequest(activityID, SharedPref.getChallengeID())
            apiInterface.startChallenge(SharedPref.getAuthToken(), startRequest)
                .enqueue(object : Callback<StartResponse> {
                    override fun onResponse(
                        call: Call<StartResponse>,
                        response: Response<StartResponse>
                    ) {
                        CommonUtils.dismissDialoge()
                        response.let {
                            serviceResponse = response.code() == 200 && response.body()!!.success
                            if (serviceResponse) {

                                if (activityType.equals("upload", true)) {
                                    btn.text = "Submit"
                                    linearLayout.visibility = View.GONE
                                } else if (activityType.equals("journal", true)) {
                                    btn.text = "Submit"
                                    editText.hint =
                                        "Please write more than 15 characters about the activity"
                                    linearLayout.visibility = View.VISIBLE
                                } else if (activityType.equals(
                                        "Share",
                                        true
                                    ) || activityType.equals(
                                        "tribe",
                                        true
                                    ) || challengeActivity.activityType.equals(
                                        "tribeandwhatsapp",
                                        true
                                    )
                                ) {
                                    btn.text = "Share"
                                    editText.hint = ""
                                    linearLayout.visibility = View.GONE
                                    img.visibility = View.GONE
                                    parent.visibility = View.GONE
                                    Glide.with(context)
                                        .load(imgString)
                                        .into(img)
                                } else if (challengeActivity.activityType.equals(
                                        "Article",
                                        ignoreCase = true
                                    )
                                ) {
                                    btn.text = "Visit"
                                    linearLayout.visibility = View.GONE
                                } else if (challengeActivity.activityType.equals(
                                        "water",
                                        true
                                    ) || challengeActivity.activityType.equals(
                                        "das",
                                        true
                                    ) || challengeActivity.activityType.equals(
                                        "wellbeing",
                                        true
                                    ) || challengeActivity.activityType.equals("sonde", true)
                                ) {
                                    btn.text = "Submit"
                                    linearLayout.visibility = View.GONE
                                } else if (activityType.equals("stepandnote", ignoreCase = true)) {
                                    if (challengeActivity.progressPercentage >= 50) {
                                        btn.text = "Submit"
                                        linearLayout.visibility = View.VISIBLE
                                    } else {
                                        btn.text = "Complete"
                                        linearLayout.visibility = View.GONE
                                    }
                                }  else if(challengeActivity.activityType.equals("quiz",true)){
                                    btn.text = "Complete"
                                    linearLayout.visibility = View.GONE

                                }
                                else if(challengeActivity.activityType.equals("facescanandnote",true)){
                                    btn.text = "Complete"
                                    if(challengeActivity.progressPercentage >= 50){
                                        linearLayout.visibility = View.VISIBLE

                                    }else{
                                        linearLayout.visibility = View.GONE
                                    }
                                }else if(challengeActivity.activityType.contains("Article_",true)){
                                    btn.text = "Visit"
                                    linearLayout.visibility = View.GONE
                                }
                                else if(challengeActivity.activityType.equals("step", true)
                                        || challengeActivity.activityType.equals("steps", true)){
                                    btn.text = "Complete"
                                    linearLayout.visibility = View.GONE
                                }
                                else {
                                    btn.text = "Submit"
                                    linearLayout.visibility = View.VISIBLE
                                }
                                if (!challengeActivity.redirection.equals("")) {
                                    button.visibility = View.VISIBLE
                                } else {
                                    button.visibility = View.GONE
                                }

                                if (activityType.equals("journal")) {
                                    ChallengeDetailActivity().getChallengeForFeedBack(
                                        recyclerView,
                                        SharedPref.getPositionID()
                                    )

                                } else {
                                    ChallengeDetailActivity().getChallengeDetails(
                                        challengeID,
                                        context,
                                        this@helperClass,
                                        this@helperClass,
                                        recyclerView,
                                        "",
                                        SharedPref.getPositionID()
                                    )

                                }
                                if (activityType.equals("HappyMart")) {
                                    var redirectionURL = ""
                                    if (challengeActivity.redirection.contains("{userid}")) {
                                        redirectionURL = challengeActivity.redirection.replace(
                                            "{userid}",
                                            SharedPref.getAesUuid()
                                        )
                                    } else {
                                        redirectionURL = challengeActivity.redirection

                                    }
                                    val intent = Intent(mContext, WebActivity::class.java)
                                    intent.putExtra("Url", redirectionURL)
                                    intent.putExtra("loginUrl", "")
                                    mContext.startActivity(intent)
                                }
                            }

                        }
                    }

                    override fun onFailure(call: Call<StartResponse>, t: Throwable) {
                        CommonUtils.dismissDialoge()
                        serviceResponse = false
                    }

                })

        } catch (e: Exception) {
            CommonUtils.dismissDialoge()
            e.toString()
        }

        return serviceResponse
    }


    fun getFilePathFromImage(context: Activity, image: Image?): String? {
        try {
            if (image == null) return null
            if (!TextUtils.isEmpty(image.path) && File(image.path).exists()) {
                return image.path
            } else if (image.uri != null) {
                val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                val cursor: Cursor = context.getContentResolver()
                    .query(image.uri, filePathColumn, null, null, null)!!
                cursor.moveToFirst()
                val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                var imgFilePath: String? = null
                if (columnIndex != -1) {
                    imgFilePath = cursor.getString(columnIndex)
                }
                cursor.close()
                return imgFilePath
            }
        } catch (e: Exception) {
            e.toString()
        }

        return null
    }

    fun getFileSizeFromPath(filePath: String?): Long {
        if (filePath == null) return 0
        val file = File(filePath)
        return file.length()
    }

    fun prepareFilePart(partName: String, path: String): MultipartBody.Part? {
        val file = File(path)
        val mimeType = URLConnection.guessContentTypeFromName(file.name)
        val requestFile = RequestBody.create(MediaType.parse(mimeType), file)
        return MultipartBody.Part.createFormData(partName, file.name, requestFile)
    }

    override fun onActivityClick(
        challengeActivity: ChallenegActivities,
        activityBinding: ActivityChallengeDetailBinding
    ) {
        if (!challengeActivity.isCompleted) {
            //journalUploadDialog(mContext,challengeActivity,ChallengeID,challengeActivity.shareContent[0],recyclerView!!.challengeDetailsRecylcer)

        }
    }

    override fun clickToInvite() {
        Log.d("AuthToken", "dvsdvsdv")

    }

    override fun onScratchComplete() {
        TODO("Not yet implemented")
    }

    override fun onScratchProgress(
        scratchCardLayout: ScratchCardLayout,
        atLeastScratchedPercent: Int
    ) {
        TODO("Not yet implemented")
    }

    override fun onScratchStarted() {
        TODO("Not yet implemented")
    }

    fun getRealPathFromURI(context: Context, contentUri: Uri?): String? {
        var cursor: Cursor? = null
        return try {
            val proj = arrayOf(MediaStore.Images.Media.DATA)
            cursor = context.contentResolver.query(contentUri!!, proj, null, null, null)
            val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            cursor.getString(column_index)
        } finally {
            cursor?.close()
        }
    }


    private fun getDasAnalysis(context: Context) {
        try {
            CommonUtils.showProgressDialige(context)
            val request = IntegrationIdRequest(context.getString(R.string.dass21_i_id))
            val apiInterfaceWyh =
                ApiClientWyh.getClient(CommonUtils.getBaseUrlForAPI(context))
                    .create<ApiInterfaceWyh>(
                        ApiInterfaceWyh::class.java
                    )
            apiInterfaceWyh.getDassAnalysis(SharedPref.getAuthToken(), request)
                .enqueue(object : Callback<DassAnalysisResponse> {
                    override fun onResponse(
                        call: Call<DassAnalysisResponse>,
                        response: Response<DassAnalysisResponse>
                    ) {
                        CommonUtils.dismissDialoge()
                        if (response.body() != null && response.code() == 200) {
                            val dassAnalysisResponse = Gson().fromJson<DassAnalysisResponse>(
                                SharedPref.getDASSAnalysis(),
                                DassAnalysisResponse::class.java
                            )
                            val isAnalysis =
                                dassAnalysisResponse != null && dassAnalysisResponse!!.getData() != null
                            val isShown = SharedPref.getWellBeingIntroShownDAS()
                            val activity = context as Activity
                            if (!isShown) {
                                SharedPref.putWellBeingIntroShownDAS(true)
                                val intent =
                                    Intent(context, WellBeingDisclaimerActivity::class.java)
                                intent.putExtra("came_from", "KnowYourDAS")
                                intent.putExtra("CategoryName", "DAS Score")
                                intent.putExtra("isAnalysis", isAnalysis)
                                intent.putExtra("shownScreen", true)
                                context.startActivity(intent)
                                activity.finish()
                            } else {
                                val intent: Intent = if (isAnalysis) {
                                    Intent(context, Dass21AnalysisActivity::class.java).putExtra("comingFrom","")
                                } else {
                                    Intent(context, Dass21QuestionsActivity::class.java)
                                }
                                context.startActivity(intent)
                                activity.finish()
                            }
                        }
                    }

                    override fun onFailure(call: Call<DassAnalysisResponse>, t: Throwable) {

                    }

                })

        } catch (e: Exception) {
            e.toString()
        }
    }


}