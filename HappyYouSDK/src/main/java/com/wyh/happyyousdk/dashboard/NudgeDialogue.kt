package com.wyh.happyyousdk.dashboard

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.esafirm.imagepicker.features.ImagePicker
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.wyh.happyyousdk.APIEncryption.APILogs
import com.wyh.happyyousdk.APIEncryption.RetrofitHandler
import com.wyh.happyyousdk.ChallangesModule.Activities.BadgesActivity
import com.wyh.happyyousdk.R
import com.wyh.happyyousdk.SpinWheel.Activities.DashboardWheelActivity
import com.wyh.happyyousdk.SpinWheel.rewardDialogCloseListener
import com.wyh.happyyousdk.dashboard.helper.NewDashboardHelper
import com.wyh.happyyousdk.databinding.CustomPopupStampsBinding
import com.wyh.happyyousdk.databinding.LayoutNewPointsPopUpBinding
import com.wyh.happyyousdk.databinding.PopUpScratchCardScratchableBinding
import com.wyh.happyyousdk.databinding.SpinPointsRewardsLayoutBinding
import com.wyh.happyyousdk.databinding.SpinnerCancelLayoutBinding
import com.wyh.happyyousdk.databinding.SpinnerVoucherLayoutBinding
import com.wyh.happyyousdk.databinding.WarerIntakeLayoutBinding
import com.wyh.happyyousdk.databinding.WinningsDialogeLayoutBinding
import com.wyh.happyyousdk.happyMarket.HappyMartCategory
import com.wyh.happyyousdk.happyMarket.HappyMartDisclaimerActivity
import com.wyh.happyyousdk.happyMarket.HappyMartOthersSection
import com.wyh.happyyousdk.model.RewardsModel
import com.wyh.happyyousdk.model.request.VoucherIdRequest
import com.wyh.happyyousdk.model.request.rewards.ActivityProgressRequest
import com.wyh.happyyousdk.model.response.AddOnBadges
import com.wyh.happyyousdk.model.response.AddOnPoints
import com.wyh.happyyousdk.model.response.AddOnScratchCard
import com.wyh.happyyousdk.model.response.RewardListResponse
import com.wyh.happyyousdk.model.response.RewardsData
import com.wyh.happyyousdk.model.response.TopUpData
import com.wyh.happyyousdk.model.response.rewards.ActivityProgressResponse
import com.wyh.happyyousdk.network.ApiClientWyh
import com.wyh.happyyousdk.network.ApiInterfaceWyh
import com.wyh.happyyousdk.rewards.RewardsActivity
import com.wyh.happyyousdk.utils.Analytics
import com.wyh.happyyousdk.utils.CommonUtils
import com.wyh.happyyousdk.utils.CommonUtils.formatDate
import com.wyh.happyyousdk.utils.Constants
import com.wyh.happyyousdk.utils.Constants.WATER
import com.wyh.happyyousdk.utils.CustomYesNoDialog
import com.wyh.happyyousdk.utils.SharedPref
import dev.skymansandy.scratchcardlayout.listener.ScratchListener
import dev.skymansandy.scratchcardlayout.ui.ScratchCardLayout
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Locale

object NudgeDialogue {


    lateinit var alertDialog: AlertDialog
    lateinit var builder: AlertDialog.Builder
    lateinit var scracthCardDialog: AlertDialog
    lateinit var alertDialogScratched: AlertDialog
    lateinit var alertDialogStamp: AlertDialog
    lateinit var alertDialogBonusStamp: AlertDialog
    var voucherIdRequest: VoucherIdRequest? = null
    var glasses: Int = 0
    var format: DecimalFormat = DecimalFormat("0.##")
    val parts: MutableList<MultipartBody.Part?> = ArrayList()


    fun waterIntakeDialoge(
        context: Context,
        waterIntakeGoal: Int,
        waterIntake: Double,
        waterIntakeAllowed: Boolean
    ) {
        try {
            builder = AlertDialog.Builder(context)
            val binding: WarerIntakeLayoutBinding = DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.warer_intake_layout, null, false
            )

            builder.setView(binding.root)
            alertDialog = builder.create()
            builder.setCancelable(true)

            binding.ivAdd.setOnClickListener {
                increaseNumber(
                    binding.tvGlassAmt.text.toString(),
                    binding,
                    waterIntakeGoal,
                    waterIntake,
                    context
                )
            }



            binding.ivMinus.setOnClickListener {
                decreaseNumber(binding, context)
            }
            /* binding.btnViewMore.setOnClickListener {
                 val intent = Intent(context, TrendsActivity::class.java)
                 intent.putExtra("activityType", Constants.WATER)
                 context.startActivity(intent)
             }*/

            binding.btnClosed.setOnClickListener {
                SharedPref.putWaterIntake(false)
                alertDialog.dismiss()
            }

            binding.btnAddWater.setOnClickListener {
                val glasses = binding.tvGlassAmt.text.toString().toInt() + waterIntake
                if (binding.tvGlassAmt.text.toString().toInt() > 0) {
                    alertDialog.dismiss()
                    NewDashboardActivity().UploadData(WATER, glasses, context)

                } else {
                    Toast.makeText(context, "Please add water intake", Toast.LENGTH_SHORT).show()
                }
            }

            if (!alertDialog.isShowing) alertDialog.show()

            val displayRectangle = Rect()
            val window: Window = (context as NewDashboardActivity).window

            window.decorView.getWindowVisibleDisplayFrame(displayRectangle)
            alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))



            alertDialog.window!!.setLayout(
                (displayRectangle.width() *
                        0.8f).toInt(), RelativeLayout.LayoutParams.WRAP_CONTENT
            )
        } catch (e: Exception) {
            e.toString()
        }
    }

    fun increaseNumber(
        waterValue: String,
        binding: WarerIntakeLayoutBinding,
        waterIntakeGoal: Int,
        waterIntake: Double,
        context: Context
    ) {
        glasses = waterValue.toDouble().toInt()
        val actualGlasses: Int = glasses + 1
        val currentDayWaterCount = (actualGlasses + waterIntake).toInt()
        if (actualGlasses > waterIntakeGoal || waterIntake > waterIntakeGoal || currentDayWaterCount > waterIntakeGoal) {
            if (!SharedPref.getWaterIntake()) {
                waterIntakeDialoge(context)
                return
            } else {
                if (glasses < 30) {
                    display(glasses + 1, binding, context)
                }
            }
        }
        if (glasses < 30) {
            display(glasses + 1, binding, context)
        }
    }

    fun decreaseNumber(binding: WarerIntakeLayoutBinding, context: Context) {
        glasses = binding.tvGlassAmt.text.toString().toDouble().toInt()
        if (glasses > 0) {
            display(glasses - 1, binding, context)
        }
    }

    private fun display(i: Int, binding: WarerIntakeLayoutBinding, context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            binding.progressBar.setProgress(i, true)
        } else {
            binding.progressBar.progress = i
        }
        binding.tvGlassAmt.setText(format.format(i.toLong()))
        if (i >= 16) {
            binding.tvGlassAmt.setTextColor(context.resources.getColor(R.color.white))
        } else {
            binding.tvGlassAmt.setTextColor(context.resources.getColor(R.color.dark_pink))
        }
    }

    fun waterIntakeDialoge(context: Context) {
        try {
            val customYesNoDialog =
                CustomYesNoDialog(context, R.style.Theme_Dialog)
            customYesNoDialog.show()
            customYesNoDialog.setCancelable(false)
            customYesNoDialog.binding.llActionRequired.visibility = View.GONE
            customYesNoDialog.binding.txtInfoPopUpDesc.text = "You have exceeded the water intake limit, still, you have wanted to add water ?"
            customYesNoDialog.binding.btnYes.text = "Yes"
            customYesNoDialog.binding.btnCancel.text = "No"
            customYesNoDialog.binding.btnYes.background = ContextCompat.getDrawable(
                context,
                R.drawable.blue_rc_bg_8dp
            )
            customYesNoDialog.binding.btnCancel.background = ContextCompat.getDrawable(
                context,
                R.drawable.pink_rc_bg_8dp
            )
            customYesNoDialog.binding.btnYes.setOnClickListener {
                customYesNoDialog.dismiss()
                SharedPref.putWaterIntake(true)
            }

            customYesNoDialog.binding.btnCancel.setOnClickListener { view ->
                customYesNoDialog.dismiss()
                SharedPref.putWaterIntake(false)
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    fun rewardsDialoge(context: Activity, data: RewardsData, nudgeButton: String) {
        try {
            Log.d("AuthToken", "Dialoge")
            builder = AlertDialog.Builder(context, R.style.CustomAlertDialog)
            val binding: WinningsDialogeLayoutBinding = DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.winnings_dialoge_layout, null, false
            )

            binding.winningsDialogTvhowtodoTv.text = data.howTo
            binding.winningsDialogWhattodoTv.text = data.whatTo
            binding.winningsDialogTvwhytodo.text = data.whyTo
            binding.winningsDialogTitleTv.text = data.activityName
            binding.journalDesc.text = data.activityDesc
            Glide.with(context).load(data.activityImagePath).into(binding.winningsLogoImg)


            if (data.isStarted) {
                if (data.eventType.equals("JournalUpload", true) || data.eventType.equals(
                        "Journal",
                        true
                    )
                ) {
                    binding.winningsDialogProgressbar.visibility = View.VISIBLE
                    getActivityProgress(
                        context,
                        "rewards",
                        data.activityID,
                        null,
                        binding.winningsDialogProgressbar
                    )
                    binding.winningsJournalDespLayout.visibility = View.VISIBLE
                } else {
                    binding.winningsJournalDespLayout.visibility = View.GONE
                }


                if (data.eventType.equals("Upload", true)) {
                    binding.challengeDialogStartBtn.text = context.getString(R.string.upload)
                    binding.winningsJournalDespLayout.visibility = View.GONE
                } else {
                    binding.winningsDialogProgressbar.visibility = View.VISIBLE
                    getActivityProgress(
                        context,
                        "rewards",
                        data.activityID,
                        binding.tvSteps,
                        binding.winningsDialogProgressbar
                    )
                    if (data.eventType.equals("auto", true) && data.redirectTo == null) {
                        binding.dialogCloseBtn.visibility = View.VISIBLE
                        binding.challengeDialogStartBtn.visibility = View.GONE
                        binding.challengeDialogeCloseBtn.visibility = View.GONE
                    } else {
                        binding.dialogCloseBtn.visibility = View.GONE
                        binding.challengeDialogStartBtn.visibility = View.VISIBLE
                        binding.challengeDialogeCloseBtn.visibility = View.VISIBLE
                        //binding.winningsDialogProgressbar.progress = data.progressPercentage
                        binding.challengeDialogStartBtn.text = context.getString(R.string.complete)
                    }
                }

            } else {
                binding.winningsJournalDespLayout.visibility = View.GONE
                binding.challengeDialogStartBtn.text = context.getString(R.string.start)
                binding.winningsDialogProgressbar.visibility = View.GONE

            }

            binding.dialogCloseBtn.setOnClickListener {
                alertDialog.dismiss()
            }

            binding.challengeDialogStartBtn.setOnClickListener {
                if (binding.challengeDialogStartBtn.text.equals(context.getString(R.string.start))) {
                    if (nudgeButton.equals("first", true)) {
                        APILogs.activityTracker("A_DB_NudgeButton1_Start", context)
                    } else if (nudgeButton.equals("second", true)) {
                        APILogs.activityTracker("A_DB_NudgeButton2_Start", context)
                    } else if (nudgeButton.equals("third", true)) {
                        APILogs.activityTracker("A_DB_NudgeButton3_Start", context)
                    } else {
                        APILogs.activityTracker("A_DB_NudgeButton4_Start", context)
                    }

                    DashboardHelper.startRewardActivity(
                        context, data, binding,
                        alertDialog
                    )
                } else {
                    if (data.eventType.equals("journalupload", true)) {
                        DashboardHelper.journalText = binding.winningsJournalEt.text.toString()
                        if (!binding.winningsJournalEt.text.isEmpty() || binding.winningsJournalEt.length() > 15) {
                            DashboardHelper.eventType = "Rewards"
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                val pickIntent = Intent(
                                    Intent.ACTION_PICK,
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                                )
                                pickIntent.type = "image/*"
                                pickIntent.action = Intent.ACTION_GET_CONTENT
                                context.startActivityForResult(pickIntent, 12)
                            } else {
                                ImagePicker.create(context)
                                    .includeVideo(false)
                                    .imageDirectory("Camera")
                                    .enableLog(true)
                                    .includeAnimation(true)
                                    .limit(1)
                                    .showCamera(true)
                                    .start()
                            }

                        } else {
                            Toast.makeText(
                                context,
                                context.resources.getString(R.string.at_least_15_char),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    } else if (data.eventType.equals("upload", true)) {
                        DashboardHelper.journalText = ""
                        DashboardHelper.eventType = "Rewards"
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            val pickIntent = Intent(
                                Intent.ACTION_PICK,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                            )
                            pickIntent.type = "image/*"
                            pickIntent.action = Intent.ACTION_GET_CONTENT
                            context.startActivityForResult(pickIntent, 12)
                        } else {
                            ImagePicker.create(context)
                                .includeVideo(false)
                                .imageDirectory("Camera")
                                .enableLog(true)
                                .includeAnimation(true)
                                .limit(1)
                                .showCamera(true)
                                .start()
                        }
                    } else if (data.eventType.equals("Journal", true)) {
                        if (!binding.winningsJournalEt.text.isEmpty() || binding.winningsJournalEt.length() > 15) {
                            DashboardHelper.journalText = binding.winningsJournalEt.text.toString()
                            DashboardHelper.completeRewardsActivity(context, parts, data)
                        } else {
                            Toast.makeText(
                                context,
                                context.resources.getString(R.string.at_least_15_char),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    } else if (data.redirectTo != null) {
                        alertDialog.dismiss()
                        DashboardHelper.rewardRedirection(data, context)
                    } else {

                    }
                }
            }

            binding.challengeDialogeCloseBtn.setOnClickListener {
                if (nudgeButton.equals("first", true)) {
                    APILogs.activityTracker("A_DB_NudgeButton1_Close", context)
                } else if (nudgeButton.equals("second", true)) {
                    APILogs.activityTracker("A_DB_NudgeButton2_Close", context)
                } else if (nudgeButton.equals("third", true)) {
                    APILogs.activityTracker("A_DB_NudgeButton3_Close", context)
                } else {
                    APILogs.activityTracker("A_DB_NudgeButton4_Close", context)
                }
                alertDialog.dismiss()
            }

            builder.setView(binding.root)
            alertDialog = builder.create()
            alertDialog.setCancelable(false)


            if (!alertDialog.isShowing) alertDialog.show()

            val displayRectangle = Rect()
            val window: Window = (context as NewDashboardActivity).window

            window.decorView.getWindowVisibleDisplayFrame(displayRectangle)

            alertDialog.window!!.setLayout(
                (displayRectangle.width() *
                        0.8f).toInt(), (displayRectangle.height() * 0.8f).toInt()
            )

        } catch (e: Exception) {
            Log.d("AuthToken", "Dialoge Exception $e")
            e.toString()
        }
    }


    fun getActivityProgress(
        context: Context,
        comingFrom: String,
        eventid: Int,
        tvSteps: TextView?,
        progressBar: LinearProgressIndicator
    ) {
        try {
            val apiInterface = ApiClientWyh.getClient(CommonUtils.getBaseUrlForAPI(context))
                .create(ApiInterfaceWyh::class.java)
            var request: ActivityProgressRequest = if (comingFrom.equals("rewards", true)) {
                ActivityProgressRequest(eventid, "Activity")
            } else {
                ActivityProgressRequest(eventid, "Topup")
            }

            apiInterface.getActivityProgress(SharedPref.getAuthToken(), request)
                .enqueue(object : Callback<ActivityProgressResponse> {
                    @SuppressLint("SetTextI18n")
                    override fun onResponse(
                        call: Call<ActivityProgressResponse>,
                        response: Response<ActivityProgressResponse>
                    ) {
                        if (response.code() == 200 && response.body() != null) {
                            if (response.body()!!.data != null) {
                                if (tvSteps != null && response.body()!!.data.totalSteps != 0) {
                                    tvSteps.visibility = View.VISIBLE
                                    tvSteps.text =
                                        "Your steps: ${response.body()!!.data.userSteps} / ${response.body()!!.data.totalSteps}"
                                } else if (tvSteps != null && response.body()!!.data.totalCount != 0) {
                                    tvSteps.visibility = View.VISIBLE
                                    tvSteps.text =
                                        "Your progress: " + response.body()!!.data.userCount + " / " + response.body()!!.data.totalCount
                                }
                                val progress = response.body()!!.data.progressPercentage
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    progressBar.setProgress(progress, true)
                                } else progressBar.progress = progress
                            }
                        }
                    }

                    override fun onFailure(call: Call<ActivityProgressResponse>, t: Throwable) {

                    }

                })

        } catch (e: Exception) {
            e.toString()
        }
    }

    fun topUPDialoge(context: Activity, data: TopUpData, nudgeButton: String) {
        try {
            Log.d("AuthToken", "Dialoge")
            builder = AlertDialog.Builder(context, R.style.CustomAlertDialog)
            val binding: WinningsDialogeLayoutBinding = DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.winnings_dialoge_layout, null, false
            )

            binding.winningsDialogTvhowtodoTv.text = data.howTo
            binding.winningsDialogWhattodoTv.text = data.whatTo
            binding.winningsDialogTvwhytodo.text = data.whyTo
            binding.winningsDialogTitleTv.text = data.topUpName
            binding.journalDesc.text = data.topUpDesc
            Glide.with(context)
                .load(data.topupIcon)
                .dontAnimate()
                .into(binding.winningsLogoImg)


            if (data.isStarted) {
                if (data.eventType.equals("auto") && data.redirectTo == null) {
                    binding.winningsDialogProgressbar.visibility = View.VISIBLE
                    getActivityProgress(
                        context,
                        "topup",
                        data.topUpID,
                        binding.tvSteps,
                        binding.winningsDialogProgressbar
                    )
                    binding.dialogCloseBtn.visibility = View.VISIBLE
                    binding.challengeDialogStartBtn.visibility = View.GONE
                    binding.challengeDialogeCloseBtn.visibility = View.GONE
                } else {
                    binding.winningsDialogProgressbar.visibility = View.VISIBLE
                    binding.dialogCloseBtn.visibility = View.GONE
                    binding.challengeDialogStartBtn.visibility = View.VISIBLE
                    binding.challengeDialogeCloseBtn.visibility = View.VISIBLE
                }
                if (data.eventType.equals("JournalUpload", true)) {
                    binding.winningsDialogProgressbar.visibility = View.VISIBLE
                    getActivityProgress(
                        context,
                        "topup",
                        data.topUpID,
                        null,
                        binding.winningsDialogProgressbar
                    )
                    binding.winningsJournalDespLayout.visibility = View.VISIBLE
                } else if (data.eventType.equals("Journal", true)) {
                    binding.winningsDialogProgressbar.visibility = View.VISIBLE
                    binding.winningsJournalDespLayout.visibility = View.VISIBLE
                    getActivityProgress(
                        context,
                        "topup",
                        data.topUpID,
                        null,
                        binding.winningsDialogProgressbar
                    )
                } else if (data.eventType.equals("Feedback", true)) {
                    binding.tvLabel.text = "Feedback"
                    binding.winningsJournalDespLayout.visibility = View.VISIBLE
                    binding.winningsJournalEt.hint =
                        context.resources.getString(R.string.at_least_35_char)
                } else if (data.topUpName.equals("Scratch & Win", true)) {
                    binding.winningsDialogProgressbar.progress = data.progressPercentage
                    binding.challengeDialogStartBtn.text = context.getString(R.string.complete)
                    binding.winningsJournalDespLayout.visibility = View.GONE
                } else {
                    binding.winningsDialogProgressbar.visibility = View.VISIBLE
                    getActivityProgress(
                        context,
                        "topup",
                        data.topUpID,
                        binding.tvSteps,
                        binding.winningsDialogProgressbar
                    )
                    binding.winningsJournalDespLayout.visibility = View.GONE
                }

                binding.challengeDialogStartBtn.text = context.getString(R.string.complete)

            } else {
                binding.winningsJournalDespLayout.visibility = View.GONE
                binding.winningsDialogProgressbar.visibility = View.GONE
                binding.challengeDialogStartBtn.text = context.getString(R.string.start)
                binding.winningsDialogProgressbar.progress = 0
            }


            binding.challengeDialogStartBtn.setOnClickListener {
                if (binding.challengeDialogStartBtn.text.equals(context.getString(R.string.start))) {
                    DashboardHelper.startTopUpActivity(context, data, binding)
                } else {
                    DashboardHelper.journalText = binding.winningsJournalEt.text.toString()
                    if (data.eventType.equals("Feedback", true)) {
                        if (!binding.winningsJournalEt.text.isEmpty() || binding.winningsJournalEt.length() >= 35) {
                            DashboardHelper.completFeedbackTopUp(
                                binding.winningsJournalEt.text.toString(),
                                context
                            )
                        } else {
                            binding.winningsJournalEt.requestFocus()
                            Toast.makeText(
                                context,
                                context.resources.getString(R.string.at_least_35_char),
                                Toast.LENGTH_SHORT
                            ).show()

                        }
                    } else if (data.redirectTo != null) {
                        DashboardHelper.topUpRedirection(data, context)
                    } else if (data.topUpName.equals("Scratch & Win", true)) {
                        DashboardHelper.startScractWin(context, data)
                    } else {
                        if (!binding.winningsJournalEt.text.isEmpty() || binding.winningsJournalEt.length() > 15) {
                            DashboardHelper.eventType = "TopUp"
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                val pickIntent = Intent(
                                    Intent.ACTION_PICK,
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                                )
                                pickIntent.type = "image/*"
                                pickIntent.action = Intent.ACTION_GET_CONTENT
                                context.startActivityForResult(pickIntent, 12)
                            } else {
                                ImagePicker.create(context)
                                    .includeVideo(false)
                                    .imageDirectory("Camera")
                                    .enableLog(true)
                                    .includeAnimation(true)
                                    .limit(1)
                                    .showCamera(true)
                                    .start()
                            }

                        } else {
                            Toast.makeText(
                                context,
                                context.resources.getString(R.string.at_least_15_char),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }

                }
            }

            binding.challengeDialogeCloseBtn.setOnClickListener {
                alertDialog.dismiss()
            }

            binding.dialogCloseBtn.setOnClickListener {
                alertDialog.dismiss()
            }

            builder.setView(binding.root)
            alertDialog = builder.create()
            alertDialog.setCancelable(false)


            if (!alertDialog.isShowing) alertDialog.show()

            val displayRectangle = Rect()
            val window: Window = (context as NewDashboardActivity).window

            window.decorView.getWindowVisibleDisplayFrame(displayRectangle)

            alertDialog.window!!.setLayout(
                (displayRectangle.width() *
                        0.8f).toInt(), (displayRectangle.height() * 0.8f).toInt()
            )

        } catch (e: Exception) {
            Log.d("AuthToken", "Dialoge Exception $e")
            e.toString()
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    fun scracthDialoge(
        context: Context,
        scratchListener: ScratchListener,
        data: AddOnBadges,
        list: ArrayList<AddOnBadges>
    ) {
        try {
            val alertBuilder = AlertDialog.Builder(context)
            val view =
                LayoutInflater.from(context).inflate(R.layout.scratch_card_layout, null, false)
            alertBuilder.setView(view)
            val challengesScracth =
                view.findViewById<ScratchCardLayout>(R.id.challenges_scrach_view)
            val scratchTitleTv = view.findViewById<TextView>(R.id.scratch_title_tv)
            val scratchImg = view.findViewById<ImageView>(R.id.scratch_img)
            val scratchViewBtn = view.findViewById<Button>(R.id.scratch_view_btn)
            val scratchCloseBtn = view.findViewById<Button>(R.id.scratch_close_btn)



            scratchCloseBtn.setOnClickListener {
                NewDashboardHelper.isShowUserPopup = true
                scracthCardDialog.dismiss()
            }

            scratchViewBtn.setOnClickListener {
                NewDashboardHelper.isShowUserPopup = true
                scracthCardDialog.dismiss()
                context.startActivity(Intent(context, BadgesActivity::class.java))
            }

            scratchTitleTv.text = "Hurray!! You Won \n${data.badgeName} Badges"
            scratchImg.background = context.getDrawable(R.color.white)
            Glide.with(context).load(data.getBadgeLogo()).into(scratchImg)
            scracthCardDialog = alertBuilder.create()

            challengesScracth.setScratchListener(scratchListener)
            challengesScracth.setScratchDrawable(context.getDrawable(R.drawable.scracth_orange_image))

            alertBuilder.setView(view)


            scracthCardDialog.show()

            scracthCardDialog.setOnDismissListener {
                try {
                    NewDashboardHelper.isShowUserPopup = true
                    if (list.size != 0) {
                        if (list.size > 0) {
                            list.removeAt(0)
                            scracthDialoge(context, scratchListener, list[0], list)
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
            val window: Window = (context as NewDashboardActivity).window
            window.decorView.getWindowVisibleDisplayFrame(displayRectangle)
            scracthCardDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            scracthCardDialog.window!!.setLayout(
                (displayRectangle.width() * 0.7f).toInt(),
                (displayRectangle.height() * 0.5f).toInt()
            )


        } catch (e: Exception) {
            e.toString()
        }
    }

    fun showRewardsPopupNew(rewards: AddOnPoints, context: Context) {
        val alertBuilder = AlertDialog.Builder(context)
        val binding: LayoutNewPointsPopUpBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.layout_new_points_pop_up,
            null,
            false
        )

        alertBuilder.setView(binding.root)
        val alertDialog = alertBuilder.create()

        alertDialog.setCancelable(false)

        if (!alertDialog.isShowing) alertDialog.show()


        binding.tvPoints.text = rewards.getPointsValue().toString()
        binding.tvEventName.visibility = View.VISIBLE
        binding.tvEventName.text = rewards.getActivityName()


        binding.ivClose.setOnClickListener { view -> alertDialog.dismiss() }
        val handler = Handler(Looper.getMainLooper())
        /*  handler.postDelayed({ alertDialog.dismiss() }, 3000)
          alertDialog.setOnDismissListener {

          }*/

        binding.btnPositive.setOnClickListener {
            NewDashboardHelper.isShowUserPopup = true
            alertDialog.dismiss()
            val intent = Intent(context, RewardsActivity::class.java)
            intent.putExtra("currentIndex", 0)
            context.startActivity(intent)
        }

        binding.scratchView.setScratchListener(context as NewDashboardActivity)
        binding.btnNegative.setOnClickListener {
            NewDashboardHelper.isShowUserPopup = true
            alertDialog.dismiss()

        }

        val displayRectangle = Rect()
        val window = context.window

        window.decorView.getWindowVisibleDisplayFrame(displayRectangle)
        alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        alertDialog.window!!.setLayout(
            (displayRectangle.width() *
                    0.67f).toInt(), RelativeLayout.LayoutParams.WRAP_CONTENT
        )
    }


    fun showSpinnerPointsPopup(
        rewards: String,
        rewardsTitle: String,
        context: Context,
        rewardsModel: RewardsModel,
        comingFrom: String,
        desc: String,
        headerOne: String,
        headerTwo: String
    ) {
        val alertBuilder = AlertDialog.Builder(context)
        val binding: SpinPointsRewardsLayoutBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.spin_points_rewards_layout,
            null,
            false
        )
        alertBuilder.setView(binding.root)
        val alertDialog = alertBuilder.create()

        alertDialog.setCancelable(false)

        if (comingFrom.equals("Dashboard", true)) {
            binding.scratchView.setScratchListener(context as NewDashboardActivity)
        } else {
            binding.scratchView.setScratchListener(context as DashboardWheelActivity)
        }
        if (!alertDialog.isShowing) alertDialog.show()

        binding.mainLayout.background =
            context.resources.getDrawable(R.drawable.one_sided_curved_border_info)
        //binding.scratchView.visibility = View.INVISIBLE
        binding.scratchView.onFullReveal()
        binding.tvPoints.text = rewardsTitle
        binding.pointsDesc.text = desc
        binding.headerOne.text = headerOne
        binding.headerTwo.text = headerTwo


        binding.btnPositive.setOnClickListener {
            NewDashboardHelper.isShowUserPopup = true
            SharedPref.setSpinWheelStatus(false)
            val intent = Intent(context, RewardsActivity::class.java)
            intent.putExtra("currentIndex", 0)
            context.startActivity(intent)
            alertDialog.dismiss()
            if (context is DashboardWheelActivity) {
                (context as Activity).finish()
            }
        }

        binding.ivClose.setOnClickListener {
            SharedPref.setSpinWheelStatus(false)
            Handler().postDelayed({
                if (rewardsModel != null && rewardsModel.reward != null && rewardsModel.reward.contains(
                        "First Login"
                    )
                ) {
                    if (comingFrom.equals("Dashboard", true)) {
                        NewDashboardHelper.isShowUserPopup = false
                        NewDashboardHelper.showIsFirstLogin(
                            rewardsModel.reward,
                            context,
                            (context as NewDashboardActivity)
                        )
                    } else {
                        NewDashboardHelper.isShowUserPopup = true
                        (context as Activity).finish()
                    }
                }
            }, 1000)

            alertDialog.dismiss()

        }

        val displayRectangle = Rect()
        val window = (context as Activity).window

        window.decorView.getWindowVisibleDisplayFrame(displayRectangle)
        alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        alertDialog.window!!.setLayout(
            (displayRectangle.width() *
                    0.67f).toInt(), RelativeLayout.LayoutParams.WRAP_CONTENT
        )
    }

    fun showSpinnerVoucher(
        context: Context,
        rewardLogo: String,
        couponCode: String,
        rewardDesc: String,
        rewardType: String,
        rewardtitle: String,
        rewardValue: String,
        rewardsModel: RewardsModel,
        expiry: String,
        comingFrom: String,
        headerOne: String,
        headerTwo: String
    ) {
        try {
            var IsMaskedCodeShown = true
            val alertBuilder = AlertDialog.Builder(context)
            val binding: SpinnerVoucherLayoutBinding = DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.spinner_voucher_layout, null, false
            )

            alertBuilder.setView(binding.root)
            alertDialogScratched = alertBuilder.create()
            alertDialogScratched.setCancelable(true)

            Log.d("AuthToken", "Logo $rewardLogo")
            binding.mainLayout.background =
                context.resources.getDrawable(R.drawable.one_sided_curved_border_info)
            binding.scratchView.onFullReveal()

            if (comingFrom.equals("Dashboard", true)) {
                binding.scratchView.setScratchListener(context as NewDashboardActivity)
            } else {
                binding.scratchView.setScratchListener(context as DashboardWheelActivity)
            }
            if (!alertDialogScratched.isShowing) alertDialogScratched.show()

            val originalCode = couponCode
            val maskedCode = maskVoucherCode(couponCode)


            if (expiry != null && expiry != "") {
                val date = expiry.split(" ")
                if (date.isNotEmpty()) {
                    binding.voucherExpiryTv.text =
                        "Expiry Date : \n${formatDate(date[0],"MM/dd/yyyy",
                            "dd/MM/yyyy")} ${convertTo12HourFormat(date[1])}"
                } else {
                    binding.voucherExpiryTv.text = ""
                }
            }

            binding.llCopy.visibility = View.VISIBLE
            binding.offerTitle.visibility = View.GONE
            binding.tvPoints.visibility = View.VISIBLE
            binding.voucherExpiryTv.visibility = View.VISIBLE
            binding.headerOne.text = headerOne
            binding.headerTwo.text = headerTwo

            if (!SharedPref.getIsScratchedFirstCard()) SharedPref.putIsScratchedFirstCard(true)
            binding.descriptionTv.text = rewardDesc
            binding.tvCouponCode.text = maskedCode
            binding.tvPoints.text = rewardtitle

            binding.viewRewards.setOnClickListener { view ->
                NewDashboardHelper.isShowUserPopup = true
                SharedPref.setSpinWheelStatus(false)
                alertDialogScratched.dismiss()
                getRewardList(context)
            }

            binding.viewVoucher.setOnClickListener {
                if (IsMaskedCodeShown) {
                    IsMaskedCodeShown = false
                    binding.tvCouponCode.text = maskedCode
                    binding.viewVoucher.setImageDrawable(context.getDrawable(R.drawable.invisible_eye))
                } else {
                    IsMaskedCodeShown = true
                    binding.tvCouponCode.text = originalCode
                    binding.viewVoucher.setImageDrawable(context.getDrawable(R.drawable.baseline_remove_red_eye_24))
                }
            }



            binding.claimRewards.setOnClickListener {
                NewDashboardHelper.isShowUserPopup = true
                SharedPref.setSpinWheelStatus(false)
                alertDialogScratched.dismiss()
                context.startActivity(
                    Intent(context, HappyMartOthersSection::class.java)
                        .putExtra("points", "")
                        .putExtra("comingFrom", "spinner")
                        .putExtra("couponCode", couponCode)
                )
                if (context is DashboardWheelActivity) {
                    context.finish()
                }

            }
            binding.ivClose.setOnClickListener {
                SharedPref.setSpinWheelStatus(false)
                spinnerCancelDialog(context, rewardsModel, "", "${comingFrom}_voucher", "")
                alertDialogScratched.dismiss()
            }


            binding.tvCopy.setOnClickListener { view ->
                val clipboard =
                    context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("label", couponCode)
                clipboard.setPrimaryClip(clip)
                Toast.makeText(context, "Copied", Toast.LENGTH_SHORT).show()
            }


            Glide.with(context)
                .load(rewardLogo)
                .error(R.drawable.dummy_image)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(binding.rewardLogo)
            val displayRectangle = Rect()
            val window = (context as Activity).window
            window.decorView.getWindowVisibleDisplayFrame(displayRectangle)
            alertDialogScratched.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            alertDialogScratched.window!!.setLayout(
                (displayRectangle.width() *
                        0.7f).toInt(), (displayRectangle.height() * 0.7f).toInt()
            )


        } catch (e: Exception) {
            e.toString()
        }
    }


    fun maskVoucherCode(voucherCode: String): String {
        val visibleLength = voucherCode.length - 4
        return if (visibleLength > 0) {
            voucherCode.substring(0, visibleLength) + "****"
        } else {
            "****" // Handle cases where the code is too short
        }
    }

    fun convertTo12HourFormat(time24: String): String {
        // Define input (24-hour) and output (12-hour) date formats
        val inputFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("hh:mm:ss  a", Locale.getDefault())

        return try {
            //Parse the 24-hour time and format it into 12-hour time
            val date = inputFormat.parse(time24)
            if (outputFormat.format(date).contains("am")) {
                outputFormat.format(date).replace("am", "AM")
            } else {
                outputFormat.format(date).replace("pm", "PM")

            }
        } catch (e: Exception) {
            "Invalid time format" //Handle invalid input
        }
    }


    fun showSpinnerOffer(
        context: Context,
        partnerLogo: String,
        partnerDesc: String,
        partnerName: String,
        partnerurl: String,
        rewardsModel: RewardsModel,
        comingFrom: String,
        expiry: String,
        rewardTitle: String,
        headerOne: String,
        headerTwo: String
    ) {
        try {
            val alertBuilder = AlertDialog.Builder(context)
            val binding: SpinnerVoucherLayoutBinding = DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.spinner_voucher_layout, null, false
            )

            alertBuilder.setView(binding.root)
            val alertDialog = alertBuilder.create()
            alertDialog.setCancelable(true)

            Log.d("AuthToken", "Logo $partnerLogo")
            if (!alertDialog.isShowing) alertDialog.show()
            binding.llCopy.visibility = View.GONE
            binding.tvPoints.visibility = View.GONE
            binding.voucherExpiryTv.visibility = View.VISIBLE
            binding.headerOne.text = headerOne
            binding.headerTwo.text = headerTwo

            binding.mainLayout.background =
                context.resources.getDrawable(R.drawable.one_sided_curved_border_info)
            binding.scratchView.onFullReveal()
            if (comingFrom.equals("Dashboard", true)) {
                binding.scratchView.setScratchListener(context as NewDashboardActivity)
            } else {
                binding.scratchView.setScratchListener(context as DashboardWheelActivity)
            }

            if (!SharedPref.getIsScratchedFirstCard()) SharedPref.putIsScratchedFirstCard(true)
            binding.offerTitle.text = rewardTitle
            binding.descriptionTv.text = partnerDesc


            if (expiry != null && expiry != "") {
                val date = expiry.split(" ")
                if (date.isNotEmpty()) {
                    binding.voucherExpiryTv.text =
                        "Expiry Date : \n${formatDate(date[0],"MM/dd/yyyy",
                            "dd/MM/yyyy")} ${convertTo12HourFormat(date[1])}"
                } else {
                    binding.voucherExpiryTv.text = ""
                }

            }


            binding.viewRewards.setOnClickListener { view ->
                SharedPref.setSpinWheelStatus(false)
                NewDashboardHelper.isShowUserPopup = true
                alertDialog.dismiss()
                getRewardList(context)
            }

            binding.claimRewards.setOnClickListener {
                NewDashboardHelper.isShowUserPopup = true
                SharedPref.setSpinWheelStatus(false)
                alertDialog.dismiss()
                context.startActivity(
                    Intent(context, HappyMartCategory::class.java)
                        .putExtra("comingFrom", Constants.DASHBOARD_BANNER)
                        .putExtra("selectedPartner", partnerName)
                        .putExtra("toolbarname", partnerName)
                        .putExtra("disclaimer", partnerName)
                        .putExtra("redirectionURL", partnerurl)
                        .putExtra("disclaimerURL", partnerLogo)
                )
                if (context is DashboardWheelActivity) {
                    context.finish()
                }
            }


            binding.ivClose.setOnClickListener {
                NewDashboardHelper.isShowUserPopup = true
                SharedPref.setSpinWheelStatus(false)
                spinnerCancelDialog(context, rewardsModel, "", "${comingFrom}_offer", "")
                alertDialog.dismiss()
            }

            Glide.with(context)
                .load(partnerLogo)
                .error(R.drawable.dummy_image)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(binding.rewardLogo)

            val displayRectangle = Rect()
            val window = (context as Activity).window
            window.decorView.getWindowVisibleDisplayFrame(displayRectangle)
            alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            alertDialog.window!!.setLayout(
                (displayRectangle.width() *
                        0.7f).toInt(), (displayRectangle.height() * 0.7f).toInt()
            )

        } catch (e: Exception) {
            e.toString()
        }
    }

    fun spinnerCancelDialog(
        context: Context,
        rewardsModel: RewardsModel,
        messgae: String,
        comingFrom: String,
        rewardType: String
    ) {
        try {
            val alertBuilder = AlertDialog.Builder(context)
            val binding: SpinnerCancelLayoutBinding = DataBindingUtil.inflate(
                LayoutInflater.from(context), R.layout.spinner_cancel_layout, null, false
            )

            alertBuilder.setView(binding.root)
            binding.scratchView.onFullReveal()
            val comingDialog = comingFrom.split("_")
            if (comingDialog.isNotEmpty()) {
                if (comingDialog[0].equals("Dashboard", true)) {
                    binding.scratchView.setScratchListener(context as NewDashboardActivity)
                } else {
                    binding.scratchView.setScratchListener(context as DashboardWheelActivity)
                }
            }
            if (comingFrom.contains("voucher", true)) {
                Glide.with(context)
                    .load(context.resources.getDrawable(R.drawable.reward_expire_img))
                    .into(binding.ivVendorLogo)
                binding.navigationDesc.text = "You can view your voucher detail by navigating to"
            } else if (comingFrom.contains("offer", true)) {
                Glide.with(context)
                    .load(context.resources.getDrawable(R.drawable.reward_expire_img))
                    .into(binding.ivVendorLogo)
                binding.navigationDesc.text =
                    "You can view your complimentary services by navigating to HappyMart"
                binding.navigationTv.visibility = View.GONE
                binding.viewRewards.visibility = View.GONE
            } else if (comingFrom.contains("Not_Eligible", true)) {
                SharedPref.setSpinWheelStatus(false)
                Glide.with(context)
                    .load(context.resources.getDrawable(R.drawable.no_reward_img))
                    .into(binding.ivVendorLogo)
                binding.navigationTv.visibility = View.GONE
                binding.viewRewards.visibility = View.VISIBLE
                binding.rewardStatusTv.text = "Oops!"
                binding.navigationDesc.text =
                    "You are not eligible to claim rewards as you have already claimed once before."
            } else if (comingFrom.contains("Expired", true)) {
                SharedPref.setSpinWheelStatus(false)
                Glide.with(context)
                    .load(context.resources.getDrawable(R.drawable.no_reward_img))
                    .into(binding.ivVendorLogo)
                binding.rewardStatusTv.text = "Oops!"
                binding.viewRewards.visibility = View.GONE
                binding.navigationTv.visibility = View.GONE
                binding.navigationDesc.text =
                    "You are not eligible to claim rewards since it is expired."
            } else if (comingFrom.contains("Old_User")) {
                Glide.with(context)
                    .load(context.resources.getDrawable(R.drawable.no_reward_img))
                    .into(binding.ivVendorLogo)
                binding.rewardStatusTv.text = "Oops!"
                binding.navigationTv.visibility = View.GONE
                binding.viewRewards.visibility = View.GONE
                SharedPref.setSpinWheelStatus(false)
                binding.navigationDesc.text = "The Reward is applicable only for the new user"
            }
            val alertDialogScratched = alertBuilder.create()
            alertDialogScratched.setCancelable(true)


            binding.viewRewards.setOnClickListener {
                alertDialogScratched.dismiss()
                if (rewardType.equals("Points", true)) {
                    val intent = Intent(context, RewardsActivity::class.java)
                    intent.putExtra("currentIndex", 0)
                    context.startActivity(intent)
                } else if (rewardType.equals("Offers", true) || rewardType.equals(
                        "Voucher",
                        true
                    )
                ) {
                    getRewardList(context)
                }
            }

            binding.ivClose.setOnClickListener {
                if (comingFrom.contains("Old_User")) {
                    SharedPref.setOldUserPopup(true)
                }
                if (context is DashboardWheelActivity) {
                    SharedPref.setOldUserPopup(true)
                    (context as Activity).finish()
                } else {
                    Handler().postDelayed({
                        if (rewardsModel != null && rewardsModel.reward != null && rewardsModel.reward.contains(
                                "First Login"
                            )
                        ) {
                            NewDashboardHelper.isShowUserPopup = false
                            NewDashboardHelper.showIsFirstLogin(
                                rewardsModel.reward,
                                context,
                                (context as NewDashboardActivity)
                            )
                        }
                    }, 1000)
                }
                alertDialogScratched.dismiss()
            }

            if (!alertDialogScratched.isShowing) alertDialogScratched.show()
            val displayRectangle = Rect()
            val window = (context as Activity).window
            window.decorView.getWindowVisibleDisplayFrame(displayRectangle)
            alertDialogScratched.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            alertDialogScratched.window!!.setLayout(
                (displayRectangle.width() *
                        0.7f).toInt(), (displayRectangle.height() * 0.5f).toInt()
            )
        } catch
            (e: Exception) {
            e.toString()
        }
    }

    fun spinnerCancelDialog(
        context: Context,
        rewardType: String,
        scratchListener: ScratchListener
    ) {
        try {
            val alertBuilder = AlertDialog.Builder(context)
            val binding: SpinnerCancelLayoutBinding = DataBindingUtil.inflate(
                LayoutInflater.from(context), R.layout.spinner_cancel_layout, null, false
            )

            alertBuilder.setView(binding.root)
            binding.scratchView.onFullReveal()

            binding.scratchView.setScratchListener(scratchListener)

            if (rewardType.contains("voucher", true)) {
                Glide.with(context)
                    .load(context.resources.getDrawable(R.drawable.reward_expire_img))
                    .into(binding.ivVendorLogo)
                binding.navigationDesc.text = "You can view your voucher detail by navigating to"
            } else if (rewardType.contains("offer", true)) {
                Glide.with(context)
                    .load(context.resources.getDrawable(R.drawable.reward_expire_img))
                    .into(binding.ivVendorLogo)
                binding.navigationDesc.text =
                    "You can view your complimentary services by navigating to "
                binding.navigationTv.visibility = View.VISIBLE
                binding.viewRewards.visibility = View.GONE
            } else if (rewardType.contains("Badge", true)) {
                Glide.with(context)
                    .load(context.resources.getDrawable(R.drawable.reward_expire_img))
                    .into(binding.ivVendorLogo)
                binding.navigationDesc.text = "You can view your HappyYou badges by navigating to"
                binding.navigationTv.text =
                    ContextCompat.getString(context, R.string.badge_navigation)
                binding.viewRewards.visibility = View.GONE
            } else if (rewardType.contains("Points", true)) {
                Glide.with(context)
                    .load(context.resources.getDrawable(R.drawable.reward_expire_img))
                    .into(binding.ivVendorLogo)
                binding.navigationDesc.text = "You can view your HappyYou points by navigating to"
                binding.navigationTv.text =
                    ContextCompat.getString(context, R.string.points_navigation)
                binding.viewRewards.visibility = View.GONE
            } else if (rewardType.contains("Stamps", true)) {
                Glide.with(context)
                    .load(context.resources.getDrawable(R.drawable.reward_expire_img))
                    .into(binding.ivVendorLogo)
                binding.navigationDesc.text = "You can view your HappyYou stamps by navigating to"
                binding.navigationTv.text =
                    ContextCompat.getString(context, R.string.stamp_navigation)
                binding.viewRewards.visibility = View.GONE
            }
            val alertDialogScratched = alertBuilder.create()
            alertDialogScratched.setCancelable(true)


            binding.viewRewards.setOnClickListener {
                alertDialogScratched.dismiss()
                if (rewardType.equals("Points", true)) {
                    val intent = Intent(context, RewardsActivity::class.java)
                    intent.putExtra("currentIndex", 0)
                    context.startActivity(intent)
                } else if (rewardType.equals("Offers", true) || rewardType.equals(
                        "Voucher",
                        true
                    )
                ) {
                    getRewardList(context)
                }
            }

            binding.ivClose.setOnClickListener {
                alertDialogScratched.dismiss()
            }

            if (!alertDialogScratched.isShowing) alertDialogScratched.show()
            val displayRectangle = Rect()
            val window = (context as Activity).window
            window.decorView.getWindowVisibleDisplayFrame(displayRectangle)
            alertDialogScratched.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            alertDialogScratched.window!!.setLayout(
                (displayRectangle.width() *
                        0.7f).toInt(), (displayRectangle.height() * 0.5f).toInt()
            )
        } catch
            (e: Exception) {
            e.toString()
        }
    }

    fun spinnerCancelDialogWeb(
        context: Context,
        rewardType: String,
        scratchListener: ScratchListener,
        rewardCloseDialogLitener: rewardDialogCloseListener
    ) {
        try {
            val alertBuilder = AlertDialog.Builder(context)
            val binding: SpinnerCancelLayoutBinding = DataBindingUtil.inflate(
                LayoutInflater.from(context), R.layout.spinner_cancel_layout, null, false
            )

            alertBuilder.setView(binding.root)
            binding.scratchView.onFullReveal()

            binding.scratchView.setScratchListener(scratchListener)

            if (rewardType.contains("voucher", true)) {
                Glide.with(context)
                    .load(context.resources.getDrawable(R.drawable.reward_expire_img))
                    .into(binding.ivVendorLogo)
                binding.navigationDesc.text = "You can view your voucher detail by navigating to"
            } else if (rewardType.contains("offer", true)) {
                Glide.with(context)
                    .load(context.resources.getDrawable(R.drawable.reward_expire_img))
                    .into(binding.ivVendorLogo)
                binding.navigationDesc.text =
                    "You can view your complimentary services by navigating to HappyMart"
                binding.navigationTv.visibility = View.VISIBLE
                binding.viewRewards.visibility = View.GONE
            } else if (rewardType.contains("Badge", true)) {
                Glide.with(context)
                    .load(context.resources.getDrawable(R.drawable.reward_expire_img))
                    .into(binding.ivVendorLogo)
                binding.navigationDesc.text = "You can view your HappyYou badges by navigating to"
                binding.navigationTv.text =
                    ContextCompat.getString(context, R.string.badge_navigation)
                binding.viewRewards.visibility = View.GONE
            } else if (rewardType.contains("Points", true)) {
                Glide.with(context)
                    .load(context.resources.getDrawable(R.drawable.reward_expire_img))
                    .into(binding.ivVendorLogo)
                binding.navigationDesc.text = "You can view your HappyYou points by navigating to"
                binding.navigationTv.text =
                    ContextCompat.getString(context, R.string.points_navigation)
                binding.viewRewards.visibility = View.GONE
            } else if (rewardType.contains("Stamps", true)) {
                Glide.with(context)
                    .load(context.resources.getDrawable(R.drawable.reward_expire_img))
                    .into(binding.ivVendorLogo)
                binding.navigationDesc.text = "You can view your HappyYou stamps by navigating to"
                binding.navigationTv.text =
                    ContextCompat.getString(context, R.string.stamp_navigation)
                binding.viewRewards.visibility = View.GONE
            }
            val alertDialogScratched = alertBuilder.create()
            alertDialogScratched.setCancelable(true)


            binding.viewRewards.setOnClickListener {
                alertDialogScratched.dismiss()
                if (rewardType.equals("Points", true)) {
                    val intent = Intent(context, RewardsActivity::class.java)
                    intent.putExtra("currentIndex", 0)
                    context.startActivity(intent)
                } else if (rewardType.equals("Offers", true) || rewardType.equals(
                        "Voucher",
                        true
                    )
                ) {
                    getRewardList(context)
                }
            }

            binding.ivClose.setOnClickListener {
                rewardCloseDialogLitener.onDialogDismiss()
                alertDialogScratched.dismiss()
            }

            if (!alertDialogScratched.isShowing) alertDialogScratched.show()
            val displayRectangle = Rect()
            val window = (context as Activity).window
            window.decorView.getWindowVisibleDisplayFrame(displayRectangle)
            alertDialogScratched.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            alertDialogScratched.window!!.setLayout(
                (displayRectangle.width() *
                        0.7f).toInt(), (displayRectangle.height() * 0.5f).toInt()
            )
        } catch
            (e: Exception) {
            e.toString()
        }
    }


    fun showScratchCard(addOnScratch: AddOnScratchCard, drawable: Int, context: Context) {
        val alertBuilder = AlertDialog.Builder(context)
        val binding: PopUpScratchCardScratchableBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.pop_up_scratch_card_scratchable,
            null,
            false
        )
        alertBuilder.setView(binding.root)
        val alertDialogScratched = alertBuilder.create()
        alertDialogScratched.setCancelable(true)

        if (!alertDialogScratched.isShowing) alertDialogScratched.show()

        if (!addOnScratch.isScratched())
            voucherIdRequest = VoucherIdRequest(addOnScratch.freebieID)

        if (addOnScratch.voucherCode != null) {
            binding.llCopy.visibility = View.VISIBLE
            binding.btnRedeem.visibility = View.GONE
            binding.llScratchview.visibility = View.GONE
        } else {
            binding.llCopy.visibility = View.GONE
            binding.btnRedeem.visibility = View.VISIBLE
            binding.llScratchview.visibility = View.VISIBLE
        }
        if (!SharedPref.getIsScratchedFirstCard()) SharedPref.putIsScratchedFirstCard(true)
        binding.scratchView.setScratchListener(context as NewDashboardActivity)
        //binding.scratchView.setScratchDrawable(ContextCompat.getDrawable(context, drawable))
        binding.tvTitle.text = addOnScratch.voucherTitle
        binding.tvValue.text = addOnScratch.voucherValue + " off"
        binding.tvDescription.text = addOnScratch.voucherDescription
        binding.tvCouponCode.text = addOnScratch.voucherCode
        binding.tvAmount.text = "₹ ${addOnScratch.voucherValue}"

        binding.btnRedeem.setOnClickListener { view ->
            NewDashboardHelper.isShowUserPopup = true
            alertDialogScratched.dismiss()
            val intent = Intent(context, HappyMartDisclaimerActivity::class.java)
            if (addOnScratch.vendorName.lowercase(Locale.getDefault())
                    .contains("hobby tribe")
            ) {
                intent.putExtra("came_from", Constants.HappyMartHobby)
            } else if (addOnScratch.vendorName.lowercase(Locale.getDefault())
                    .contains("mind")
            ) {
                intent.putExtra("came_from", Constants.HappyMentalWellbeing)
            } else if (addOnScratch.vendorName.lowercase(Locale.getDefault())
                    .contains("pharmeasy")
            ) {
                intent.putExtra("came_from", Constants.HappyMartPharmacy)
            } else if (addOnScratch.vendorName.lowercase(Locale.getDefault())
                    .contains("medpay")
            ) {
                intent.putExtra("came_from", Constants.HappyMartOPD)
            } else if (addOnScratch.vendorName.lowercase(Locale.getDefault())
                    .contains("coach")
            ) {
                intent.putExtra("came_from", Constants.HappyMartFitness)
            } else if (addOnScratch.vendorName.lowercase(Locale.getDefault())
                    .contains("acto")
            ) {
                intent.putExtra("came_from", Constants.HappyMartDevice)
            } else if (addOnScratch.vendorName.lowercase(Locale.getDefault())
                    .contains("connect") || addOnScratch.vendorName
                    .lowercase(Locale.getDefault()).contains("health")
            ) {
                intent.putExtra("came_from", Constants.HappyMartDiagnostics)
            } else {
                intent.putExtra("came_from", Constants.HappyMartOther)
            }
            intent.putExtra("toolbarname", addOnScratch.vendorName)
            context.startActivity(intent)
        }
        binding.btnNegative.setOnClickListener {
            NewDashboardHelper.isShowUserPopup = true
            alertDialogScratched.dismiss()
        }


        binding.tvCopy.setOnClickListener { view ->
            val clipboard =
                context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("label", addOnScratch.voucherCode)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(context, "Copied", Toast.LENGTH_SHORT).show()
        }


        binding.ivClose.setOnClickListener { view -> alertDialogScratched.dismiss() }
        Glide.with(context)
            .load(addOnScratch.vendorLogo)
            .error(R.drawable.dummy_image)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .into(binding.ivVendorLogo)
        val displayRectangle = Rect()
        val window = (context).window
        window.decorView.getWindowVisibleDisplayFrame(displayRectangle)
        alertDialogScratched.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialogScratched.window!!.setLayout(
            (displayRectangle.width() *
                    0.7f).toInt(), (displayRectangle.height() * 0.5f).toInt()
        )
    }

    fun showStampsPopup(name: String, points: Int, context: Context) {
        val alertBuilder = AlertDialog.Builder(context)
        val binding: CustomPopupStampsBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.custom_popup_stamps,
            null,
            false
        )
        alertBuilder.setView(binding.root)
        alertDialogStamp = alertBuilder.create()
        alertDialogStamp.setCancelable(true)
        if (!alertDialogStamp.isShowing) alertDialogStamp.show()

        binding.tvTitle.text = name
        binding.tvDescription2.text = "No. of Stamps: $points"


        binding.btnPositive.setOnClickListener { view ->
            NewDashboardHelper.isShowUserPopup = true
            alertDialogStamp.dismiss()
            val intent = Intent(context, RewardsActivity::class.java)
            intent.putExtra("currentIndex", 1)
            context.startActivity(intent)
        }
        binding.ivClose.setOnClickListener { view ->
            NewDashboardHelper.isShowUserPopup = true
            alertDialogStamp.dismiss()
        }


        binding.scratchView.setScratchListener(context as NewDashboardActivity)
        binding.scratchView.setScratchDrawable(context.getDrawable(R.drawable.scratch_card_pink_new))
        binding.btnNegative.setOnClickListener { view -> alertDialogStamp.dismiss() }
        val displayRectangle = Rect()
        val window = context.window
        window.decorView.getWindowVisibleDisplayFrame(displayRectangle)
        alertDialogStamp.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialogStamp.window!!.setLayout(
            (displayRectangle.width() *
                    0.7f).toInt(), (displayRectangle.height() * 0.5f).toInt()
        )
    }

    private fun showRewardsPopupDialogBox(points: Int, context: Context) {
        if (NewDashboardHelper.popUpShowModels.size > 0) {
            var i = 0
            var firstData = NewDashboardHelper.popUpShowModels[i]
            if (NewDashboardHelper.popUpShowModels.size > 1 && firstData.key == "Rewards") {
                i = 1
                firstData = NewDashboardHelper.popUpShowModels[i]
            }
            when (firstData.key) {
                "TokenStamp" -> showStampsPopup(firstData.value, points, context)
                "TokenStampBounce" -> showBonusStampPopup(points, firstData.value, context)
                else -> throw IllegalStateException("Unexpected value: " + firstData.key)
            }
            NewDashboardHelper.popUpShowModels.removeAt(i)
        }
    }

    private fun showBonusStampPopup(points: Int, rewards: String, context: Context) {
        val alertBuilder = AlertDialog.Builder(context)
        val binding: CustomPopupStampsBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.custom_popup_stamps,
            null,
            false
        )
        alertBuilder.setView(binding.root)
        alertBuilder.setView(binding.root)
        alertDialogBonusStamp = alertBuilder.create()
        alertDialogBonusStamp.setCancelable(true)
        if (!alertDialogBonusStamp.isShowing) alertDialogBonusStamp.show()

        binding.tvTitle.text = "Milestone Points"
        binding.tvDescription.text = ""
        binding.tvDescription2.text = "No. of Stamps: $points"

        binding.btnPositive.setOnClickListener { view ->
            alertDialogBonusStamp.dismiss()
            showRewardsPopupDialogBox(points, context)
        }

        binding.ivClose.setOnClickListener { view ->
            alertDialogBonusStamp.dismiss()
            //showRewardsPopupDialogBox(points, context)
        }

        binding.scratchView.setScratchListener(context as NewDashboardActivity)
        binding.scratchView.setScratchDrawable(context.getDrawable(R.drawable.scratch_card_orange_new))
        binding.btnNegative.setOnClickListener { view -> alertDialogBonusStamp.dismiss() }
        val displayRectangle = Rect()
        val window = context.window
        window.decorView.getWindowVisibleDisplayFrame(displayRectangle)
        alertDialogBonusStamp.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialogBonusStamp.window!!.setLayout(
            (displayRectangle.width() *
                    0.7f).toInt(), (displayRectangle.height() * 0.5f).toInt()
        )
    }

    fun getRewardList(context: Context) {
        try {
            CommonUtils.showProgressDialige(context)
            val apiInterface = RetrofitHandler.apiInterface()
            apiInterface.getRewardList(SharedPref.getAuthToken())
                .enqueue(object : Callback<RewardListResponse> {
                    override fun onResponse(
                        call: Call<RewardListResponse>,
                        response: Response<RewardListResponse>
                    ) {
                        CommonUtils.dismissDialoge()
                        if (response.body() != null && response.code() == 200 && response.isSuccessful) {
                            if (response.body()!!.data.badgeList.isNotEmpty()) {
                                NewDashboardHelper.badgeList = response.body()!!.data.badgeList
                            }
                            if (response.body()!!.data.stampList.isNotEmpty()) {
                                NewDashboardHelper.stamList = response.body()!!.data.stampList
                            }
                            if (response.body()!!.data.voucherList.isNotEmpty()) {
                                NewDashboardHelper.voucherList = response.body()!!.data.voucherList
                            }

                            if (response.body()!!.data.rewardsSummary.availableStamps != null) {
                                NewDashboardHelper.availableStamps =
                                    response.body()!!.data.rewardsSummary.availableStamps
                            }

                            if (response.body()!!.data.rewardsSummary.stampCount != null) {
                                NewDashboardHelper.stampCount =
                                    response.body()!!.data.rewardsSummary.stampCount
                            }

                            val intent = Intent(context, OtherActivity::class.java)
                                .putExtra(
                                    "stamp",
                                    response.body()!!.data.rewardsSummary.stampCount
                                )
                                .putExtra(
                                    "voucher",
                                    response.body()!!.data.rewardsSummary.voucherCount ?: ""
                                )
                                .putExtra(
                                    "badges",
                                    response.body()!!.data.rewardsSummary.badgeCount ?: ""
                                )
                                .putExtra("comingFrom", "spinner")
                            context.startActivity(intent)
                            if (context is DashboardWheelActivity) {
                                context.finish()
                            }

                        } else {
                            Analytics.logEvent(
                                context,
                                "",
                                "A_103_${response.code()}_${SharedPref.getEncryptedMobileNo()}"
                            )

                        }
                    }

                    override fun onFailure(call: Call<RewardListResponse>, t: Throwable) {
                        Analytics.logEvent(
                            context,
                            "",
                            "A_101_Failed_${SharedPref.getEncryptedMobileNo()}"
                        )
                        CommonUtils.dismissDialoge()
                    }

                })
        } catch (e: Exception) {
            CommonUtils.dismissDialoge()
            e.toString()
        }
    }

}