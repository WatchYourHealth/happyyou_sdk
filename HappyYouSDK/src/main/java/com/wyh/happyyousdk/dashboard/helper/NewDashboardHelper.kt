package com.wyh.happyyousdk.dashboard.helper

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.location.LocationManager
import android.os.Build
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.wyh.happyyousdk.APIEncryption.BackgroundWork.CoroutineClass
import com.wyh.happyyousdk.APIEncryption.RetrofitHandler
import com.wyh.happyyousdk.APIEncryption.RetrofitHandler.apiInterface
import com.wyh.happyyousdk.R
import com.wyh.happyyousdk.SpinWheel.Activities.DashboardWheelActivity
import com.wyh.happyyousdk.SpinWheel.rewardDialogCloseListener
import com.wyh.happyyousdk.WellBeingDisclaimerActivity
import com.wyh.happyyousdk.absorb.HealthTvListingActivity
import com.wyh.happyyousdk.absorb.QuickReadDashboard
import com.wyh.happyyousdk.crypto.CryptoHelper
import com.wyh.happyyousdk.dashboard.NewDashboardActivity
import com.wyh.happyyousdk.dashboard.NudgeDialogue
import com.wyh.happyyousdk.dashboard.NudgeDialogue.rewardsDialoge
import com.wyh.happyyousdk.dashboard.NudgeDialogue.topUPDialoge
import com.wyh.happyyousdk.dashboard.RedirectionMethod
import com.wyh.happyyousdk.dashboard.SearchActivity
import com.wyh.happyyousdk.databinding.ActivityNewDashboardBinding
import com.wyh.happyyousdk.databinding.CongratsPopUpBinding
import com.wyh.happyyousdk.databinding.CustomPopupRewardsBinding
import com.wyh.happyyousdk.databinding.LayoutNewPointsPopUpBinding
import com.wyh.happyyousdk.databinding.RefferelCodeDialogeLayoutBinding
import com.wyh.happyyousdk.databinding.ZenZoneLayoutBinding
import com.wyh.happyyousdk.heartAge.HeartAgeAnalysisActivity
import com.wyh.happyyousdk.heartAge.HeartAgeQuestionsActivity
import com.wyh.happyyousdk.ira.IRAAnalysisActivity
import com.wyh.happyyousdk.ira.IraActivity
import com.wyh.happyyousdk.model.CommonSuccessResponse
import com.wyh.happyyousdk.model.PopUpShowModel
import com.wyh.happyyousdk.model.RewardsModel
import com.wyh.happyyousdk.model.request.AddReferralRequest
import com.wyh.happyyousdk.model.request.GetNudgeDetailsRequest
import com.wyh.happyyousdk.model.request.ReferalContestRequest
import com.wyh.happyyousdk.model.request.UpdateCoopCodeRequest
import com.wyh.happyyousdk.model.request.absorb.GetDashboardDataRequest
import com.wyh.happyyousdk.model.request.faceScan.AddFaceScanVitalsRequest
import com.wyh.happyyousdk.model.request.heartAge.GetHeartAgeAnalysisRequest
import com.wyh.happyyousdk.model.request.ira.ConversationRequest
import com.wyh.happyyousdk.model.request.quizathon.ActivityRewardRequest
import com.wyh.happyyousdk.model.request.rewards.RewardsPopupRequest
import com.wyh.happyyousdk.model.response.AssignRewardsResponse.SpinRewardsData
import com.wyh.happyyousdk.model.response.FeedbackResponseData
import com.wyh.happyyousdk.model.response.NudgeRewardsResponse
import com.wyh.happyyousdk.model.response.NudgeTopupResponse
import com.wyh.happyyousdk.model.response.ReferalContestResponse
import com.wyh.happyyousdk.model.response.RewardsData
import com.wyh.happyyousdk.model.response.StampListData
import com.wyh.happyyousdk.model.response.TopUpData
import com.wyh.happyyousdk.model.response.UpdateCoopResponse
import com.wyh.happyyousdk.model.response.absorb.GetDashboardDataResponse
import com.wyh.happyyousdk.model.response.absorb.GetDashboardDataResponse.Data.TagName
import com.wyh.happyyousdk.model.response.badgeListData
import com.wyh.happyyousdk.model.response.dashboard.FilePopupModel
import com.wyh.happyyousdk.model.response.dashboard_new.ShowAdminRewardsEventsData
import com.wyh.happyyousdk.model.response.faceScan.FetchFaceScanVitalsResponse
import com.wyh.happyyousdk.model.response.heartAge.HeartAgeAnalysisResponse
import com.wyh.happyyousdk.model.response.ira.IRAHealthScoreResponse
import com.wyh.happyyousdk.model.response.playwin.ClaimReClaimRewardModel
import com.wyh.happyyousdk.model.response.playwin.QuizathonRewardData
import com.wyh.happyyousdk.model.response.rewards.PopRewardModel
import com.wyh.happyyousdk.model.response.voucherListData
import com.wyh.happyyousdk.network.ApiClientWyh
import com.wyh.happyyousdk.network.ApiInterfaceWyh
import com.wyh.happyyousdk.rewards.FeedbackPopupDialogBox.Companion.getInstance
import com.wyh.happyyousdk.rewards.RewardsActivity
import com.wyh.happyyousdk.syncDevice.SyncDeviceActivity
import com.wyh.happyyousdk.trends.TrendsActivity
import com.wyh.happyyousdk.utils.Analytics
import com.wyh.happyyousdk.utils.CommonUtils
import com.wyh.happyyousdk.utils.Constants
import com.wyh.happyyousdk.utils.CustomYesNoDialog
import com.wyh.happyyousdk.utils.SharedPref
import com.wyh.happyyousdk.utils.dialog.PostSpinDialog
import com.wyh.happyyousdk.utils.dialog.QuizRewardDialog
import com.wyhsdk.main.WatchYourHealth
import com.wyhsdk.sharedPreferences.SharedPreference
import com.wyhsdk.utils.Utilities
import dev.skymansandy.scratchcardlayout.listener.ScratchListener
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class NewDashboardHelper {

    companion object {

        var versionCode = 0

        @SuppressLint("StaticFieldLeak")
        lateinit var alertDialogFirstLogin: AlertDialog
        lateinit var alertDialogBonusRewards: AlertDialog
        var congratsText = ""
        var popUpShowModels: ArrayList<PopUpShowModel> = arrayListOf()
        var tempPopupRewards: ArrayList<PopRewardModel> = arrayListOf()
        var showAdminRewardsEventsData: ArrayList<ShowAdminRewardsEventsData> = arrayListOf()
        var feedbackResponseData: FeedbackResponseData = FeedbackResponseData()
        lateinit var scratchListener: ScratchListener
        lateinit var rewardDialogCloselistener: rewardDialogCloseListener
        var backgroundHandler: Handler? = null

        @SuppressLint("StaticFieldLeak")
        lateinit var watchYourHealth: WatchYourHealth
        const val REQUEST_CODE_ACTIVITY_RECOGNITION = 1
        const val FACE_SCAN_REQUEST_CODE = 65
        const val CALL_PERMISSION = 12
        var PERMISSION_ID = 44
        var NOTIFICATION_PERMISSION_ID = 40
        var rewardsData: RewardsData? = null
        var topUpData: TopUpData? = null
        lateinit var binding: ActivityNewDashboardBinding
        lateinit var context: Context
        var totalCalorieBurned = 0
        private var hourlyStep = false
        var isShowUserPopup = true
        var showFirstLoginPopup = false
        lateinit var mFusedLocationClient: FusedLocationProviderClient
        var feedBackCountDown: CountDownTimer? = null
        var feedbackCount = 30
        lateinit var alertDialog: AlertDialog
        var tribeListId: ArrayList<Int> = java.util.ArrayList()
        var stamList: ArrayList<StampListData> = arrayListOf()
        var voucherList: ArrayList<voucherListData> = arrayListOf()
        var badgeList: ArrayList<badgeListData> = arrayListOf()
        var availableStamps = ""
        var stampCount = ""
        var rewardsModel = RewardsModel()
        var isRewards: Boolean = false
        var isPositiveBtn = false
        var equivalentAmount = 0.0
        var totalpoints = 0
        var level = 0
        lateinit var customPopupRewardsBinding: CustomPopupRewardsBinding
        var tagNameList: List<TagName>? = null
        var heartAgeAnalysisResponse: HeartAgeAnalysisResponse? = null
        var iraHealthScoreResponse: IRAHealthScoreResponse? = null
        var isFeedbackStarted = false
        var waterIntakeGoal = 0
        var waterIntake = 0.0
        var waterIntakeAllowed = false
        var currentValueZenZone = "0"
        var dashquizathonRewardList: ArrayList<ClaimReClaimRewardModel> = java.util.ArrayList()
        var popupModelList: ArrayList<FilePopupModel> = java.util.ArrayList()
        var trasactionId: String? = null
        var featureName: String? = null;
        var activityName: String? = null;
        var mainStartCalender: Calendar = Calendar.getInstance()
        var gender: String = ""
        var otherGender: String = ""
        var user: String = "self"
        var isPaymentSuccess: Boolean = false
        var dobStr: String = ""


        fun isDialogBonusRewardsInitialized(): Boolean {
            return try {
                ::alertDialogBonusRewards.isInitialized
            } catch (e: UninitializedPropertyAccessException) {
                false
            }
        }

        fun isDialogDialogFirstLogin(): Boolean {
            return try {
                ::alertDialogFirstLogin.isInitialized
            } catch (e: UninitializedPropertyAccessException) {
                false
            }
        }

        fun showEmptyWheel(binding: ActivityNewDashboardBinding) {
            binding.dashboardMenu.ivEmptyCircle.visibility = View.VISIBLE
        }

        fun hideEmptyWheel(binding: ActivityNewDashboardBinding) {
            binding.dashboardMenu.ivEmptyCircle.visibility = View.GONE
        }


        fun showActivitiesWheel(binding: ActivityNewDashboardBinding) {
            binding.dashboardMenu.ivActivitiesCircle.setVisibility(View.VISIBLE)
            binding.dashboardMenu.tvLifestyle.setVisibility(View.VISIBLE)
            binding.dashboardMenu.tvZenZone.setVisibility(View.VISIBLE)
            binding.dashboardMenu.tvKnowYourWellBeing.setVisibility(View.VISIBLE)
        }

        fun hideActivitiesWheel(binding: ActivityNewDashboardBinding) {
            binding.dashboardMenu.ivActivitiesCircle.setVisibility(View.GONE)
            binding.dashboardMenu.tvLifestyle.setVisibility(View.GONE)
            binding.dashboardMenu.tvZenZone.setVisibility(View.GONE)
            binding.dashboardMenu.tvKnowYourWellBeing.setVisibility(View.GONE)
        }

        fun showServicesWheel(binding: ActivityNewDashboardBinding) {
            binding.dashboardMenu.ivServicesCircle.setVisibility(View.VISIBLE)
            binding.dashboardMenu.tvHealthHacks.setVisibility(View.VISIBLE)
            binding.dashboardMenu.tvDigiCoach.setVisibility(View.VISIBLE)
            binding.dashboardMenu.tvHappyMart.setVisibility(View.VISIBLE)
            binding.dashboardMenu.tvHealthLocker.setVisibility(View.VISIBLE)
        }

        fun hideServicesWheel(binding: ActivityNewDashboardBinding) {
            binding.dashboardMenu.ivServicesCircle.setVisibility(View.GONE)
            binding.dashboardMenu.tvHealthHacks.setVisibility(View.GONE)
            binding.dashboardMenu.tvDigiCoach.setVisibility(View.GONE)
            binding.dashboardMenu.tvHappyMart.setVisibility(View.GONE)
            binding.dashboardMenu.tvHealthLocker.setVisibility(View.GONE)
        }

        fun showMyZoneWheel(binding: ActivityNewDashboardBinding) {
            binding.dashboardMenu.ivMyZoneCircle.setVisibility(View.VISIBLE)
            binding.dashboardMenu.tvMyTribe.setVisibility(View.VISIBLE)
            binding.dashboardMenu.tvChallanges.setVisibility(View.VISIBLE)
            binding.dashboardMenu.tvGoals.setVisibility(View.VISIBLE)
            binding.dashboardMenu.tvMyWinnings.setVisibility(View.VISIBLE)
            binding.dashboardMenu.tvUnwind.setVisibility(View.VISIBLE)
        }

        fun hideMyZoneWheel(binding: ActivityNewDashboardBinding) {
            binding.dashboardMenu.ivMyZoneCircle.setVisibility(View.GONE)
            binding.dashboardMenu.tvChallanges.setVisibility(View.GONE)
            binding.dashboardMenu.tvMyTribe.setVisibility(View.GONE)
            binding.dashboardMenu.tvGoals.setVisibility(View.GONE)
            binding.dashboardMenu.tvMyWinnings.setVisibility(View.GONE)
            binding.dashboardMenu.tvUnwind.setVisibility(View.GONE)
        }

        fun getFitPermission(context: Activity) {
            backgroundHandler = Handler()
            if (!SharedPref.getFitBitConnection()) {
                if (ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACTIVITY_RECOGNITION
                    )
                    != PackageManager.PERMISSION_GRANTED
                ) {
                    // Permission is not granted
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        ActivityCompat.requestPermissions(
                            context, arrayOf<String>(Manifest.permission.ACTIVITY_RECOGNITION),
                            REQUEST_CODE_ACTIVITY_RECOGNITION
                        )
                    } else {
                        //Log.v("Data", "Inside Loop MONTH");
                        watchYourHealth.connectAPIClient()
                        val googleFitConnection = SharedPreference.getGoogleFitConnection()
                        if (googleFitConnection) {
                            //new ViewStepsCount(WatchYourHealth.MONTH).execute();
                            backgroundHandler!!.postDelayed(object : Runnable {
                                override fun run() {
                                    //Do something after 45 seconds
                                    Log.d("AuthToken", "Handler running")
                                    CoroutineClass().runBackGroundTask(
                                        WatchYourHealth.MONTH,
                                        context
                                    )
                                    //new ViewStepsCount(WatchYourHealth.MONTH).execute();
                                    backgroundHandler!!.postDelayed(this, (1000 * 30).toLong())
                                }
                            }, 1000)
                        }
                    }
                } else {
                    watchYourHealth.connectAPIClient()
                    val googleFitConnection = SharedPreference.getGoogleFitConnection()
                    if (googleFitConnection) {
                        /*if (!SharedPref.getGoogleFitBadge()) {
                        assignBadge("3", "tech", "GoogleFit", "Health");
                    }*/
                        //new ViewStepsCount(WatchYourHealth.MONTH).execute();
                        backgroundHandler!!.postDelayed(object : Runnable {
                            override fun run() {
                                //Do something after 45 seconds
                                Log.d("AuthToken", "Handler running")
                                CoroutineClass().runBackGroundTask(
                                    WatchYourHealth.MONTH,
                                    context
                                )
                                //new ViewStepsCount(WatchYourHealth.MONTH).execute();
                                backgroundHandler!!.postDelayed(this, (1000 * 30).toLong())
                            }
                        }, 1000)
                    }
                }
            }
        }


        fun getSteps() {
            //watchYourHealth = new WatchYourHealth(this);
            if (SharedPreference.getGoogleFitConnection()) {
                val stepCount =
                    watchYourHealth.getTotalSteps(com.wyhsdk.utils.Constants.SOURCE_GOOGLEFIT)
                SharedPref.putTodaySteps(stepCount)
                if (SharedPref.getUserCalorieBurned() == 0) {
                    val totalWalkedSteps = stepCount.toInt() * Constants.perStepInKm
                    val walkedValue = String.format("%.2f", totalWalkedSteps)
                    totalCalorieBurned =
                        Math.round(stepCount.toInt() * Constants.perCalorieInStep).toInt()
                    SharedPref.putUserCalorieBurned(totalCalorieBurned)
                    Log.d("Calories", totalCalorieBurned.toString())
                }

            }
        }

        fun getSleep() {
            var sleep = 0
            if (SharedPreference.getGoogleFitConnection()) {
                sleep = watchYourHealth.getSleep(
                    com.wyhsdk.utils.Constants.SOURCE_GOOGLEFIT,
                    CommonUtils.todayDate()
                )
                Log.d("AuthToken", "Sleep Data$sleep")
            }
            if (sleep != 0) {
                val sleepHourCount = CommonUtils.convertMinutesIntoHour(sleep)
                Log.d("AuthToken", "Sleep Data Count$sleepHourCount")
            } else {
            }
        }

        fun getStand() {
            if (!hourlyStep) {
                val minHourlyStepsDate = watchYourHealth.minHourlyStepsDate
                try {
                    val formatter = SimpleDateFormat("yyyy-MM-dd")
                    val startDate = formatter.parse(minHourlyStepsDate)
                    val endDate = formatter.parse(Utilities.getTodayDateNew())
                    val start = Calendar.getInstance()
                    start.time = startDate
                    val end = Calendar.getInstance()
                    end.time = endDate
                    //end.add(Calendar.DATE, 1);
                    //Date newEndDate = end.getTime();
                    //Log.v("DAte",""+end);
                    var date = start.time
                    while (start.before(end)) {

                        // Do your job here with `date`.
                        val spf = SimpleDateFormat("yyyy-MM-dd")
                        val convertDate = spf.format(date)
                        println("Stand Date : $convertDate")
                        //String todayDateNew = Utilities.getTodayDateNew();
                        val standCount = watchYourHealth.getTodayHourlyStandCount(convertDate)
                        //System.out.println("Stand Count : " + standCount);
                        watchYourHealth.insertStandCount(standCount, convertDate, true)
                        val activeStandCount = watchYourHealth.getTodayHourlyStandCount(
                            convertDate,
                            Constants.ACTIVE_STEPS_COUNT
                        )
                        //System.out.println("Active Stand Count : " +
                        // activeStandCount);
                        Log.d("Active Stand Count : ", "$convertDate --> $activeStandCount")
                        watchYourHealth.insertActiveHourCount(activeStandCount, convertDate)
                        start.add(
                            Calendar.DATE,
                            1
                        )
                        date = start.time
                    }
                    hourlyStep = true
                } catch (e: java.lang.Exception) {
                    //Log.e("Stand Exception", e.getMessage());
                }
            }
            val standCount = watchYourHealth.getTodayHourlyStandCount(Utilities.getTodayDateNew())
            watchYourHealth.insertStandCount(standCount, Utilities.getTodayDateNew(), true)
            val stand = watchYourHealth.stand
            val activeStandCount = watchYourHealth.getTodayHourlyStandCount(
                Utilities.getTodayDateNew(),
                Constants.ACTIVE_STEPS_COUNT
            )
            Log.d("Active Stand Count : ", Utilities.getTodayDateNew() + " --> " + activeStandCount)
            watchYourHealth.insertActiveHourCount(
                activeStandCount,
                Utilities.getTodayDateNew()
            )
            val activeHour = watchYourHealth.activeHour
        }

        fun checkPermissions(context: Context): Boolean {
            return ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

            // If we want background location
            // on Android 10.0 and higher,
            // use:
            // ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED
        }

        fun isLocationEnabled(context: Activity): Boolean {
            val locationManager =
                context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
            )
        }

        fun requestPermissions(context: Activity) {
            ActivityCompat.requestPermissions(
                context,
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ),
                PERMISSION_ID
            )
        }

        fun requestNewLocationData(context: Activity) {

            // Initializing LocationRequest
            // object with appropriate methods
            val mLocationRequest = LocationRequest()
            mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            mLocationRequest.interval = 5
            mLocationRequest.fastestInterval = 0
            mLocationRequest.numUpdates = 1

            // setting LocationRequest
            // on FusedLocationClient
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            mFusedLocationClient.requestLocationUpdates(
                mLocationRequest,
                mLocationCallback,
                Looper.myLooper()
            )
        }

        private val mLocationCallback: LocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                val mLastLocation = locationResult.lastLocation
            }
        }


        fun showIsFirstLogin(rewards: String, context: Context, scratchListener: ScratchListener) {
            isRewards = true
            val alertBuilder = AlertDialog.Builder(context)
            val binding: LayoutNewPointsPopUpBinding = DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.layout_new_points_pop_up,
                null,
                false
            )
            alertBuilder.setView(binding.getRoot())
            alertDialogFirstLogin = alertBuilder.create()
            alertDialogFirstLogin.setCancelable(false)
            if (!alertDialogFirstLogin.isShowing()) alertDialogFirstLogin.show()
            val title =
                rewards.split(";".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
            val message =
                rewards.split(";".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
            val points = message.replace("[^0-9]".toRegex(), "")
            binding.tvPoints.text = points
            binding.tvEventName.visibility = View.VISIBLE
            binding.tvEventName.text = title
            binding.ivClose.setOnClickListener {

                alertDialogFirstLogin.dismiss()
            }
            if (rewards.contains("First Login")) {
                alertDialogFirstLogin.setOnDismissListener(DialogInterface.OnDismissListener {
                    val intent = Intent(context, SyncDeviceActivity::class.java)
                    intent.putExtra("isFirst", true)
                    context.startActivity(intent)
                    NewDashboardActivity().rewardsModel = RewardsModel()
                    rewardsModel = RewardsModel()
                    showFirstLoginPopup = false
                    if (context is DashboardWheelActivity) {
                        context.finish()
                    }
                    //rewardsModel = null
                })

                binding.btnPositive.setOnClickListener {
                    alertDialogFirstLogin.dismiss()
                }
            } else {
                binding.scratchView.onFullReveal()
                binding.btnPositive.setOnClickListener { view ->
                    alertDialogFirstLogin.dismiss()
                    val intent = Intent(context, RewardsActivity::class.java)
                    intent.putExtra("currentIndex", 0)
                    context.startActivity(intent)
                }
            }
            binding.scratchView.setScratchListener(scratchListener)
            binding.btnNegative.setOnClickListener { view -> alertDialogFirstLogin.dismiss() }
            val displayRectangle = Rect()
            val window: Window = (context as Activity).window
            window.decorView.getWindowVisibleDisplayFrame(displayRectangle)
            alertDialogFirstLogin.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            alertDialogFirstLogin.window?.setLayout(
                (displayRectangle.width() *
                        0.7f).toInt(), RelativeLayout.LayoutParams.WRAP_CONTENT
            )
        }

        fun showBonusRewardsPopup(
            rewards: String,
            context: Context,
            scratchListener: ScratchListener,
            feedbackResponseData: FeedbackResponseData
        ) {
            isRewards = true
            val alertBuilder = AlertDialog.Builder(context)
            val binding: LayoutNewPointsPopUpBinding = DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.layout_new_points_pop_up,
                null,
                false
            )
            alertBuilder.setView(binding.root)
            alertDialogBonusRewards = alertBuilder.create()
            alertDialogBonusRewards.setCancelable(false)
            if (!alertDialogBonusRewards.isShowing()) alertDialogBonusRewards.show()
            val title =
                rewards.split(";".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
            val message =
                rewards.split(";".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
            val points = message.replace("[^0-9]".toRegex(), "")
            binding.tvPoints.text = points
            binding.tvEventName.visibility = View.VISIBLE
            alertDialogBonusRewards.setOnDismissListener(DialogInterface.OnDismissListener { dialogInterface: DialogInterface? ->
                if (isPositiveBtn) {
                    isPositiveBtn = false
                    val intent = Intent(context, RewardsActivity::class.java)
                    intent.putExtra("currentIndex", 0)
                    context.startActivity(intent)
                } else {
                    showRewardsPopupDialogBox(context, scratchListener, feedbackResponseData)
                }
            })
            binding.btnPositive.setOnClickListener { view ->
                isPositiveBtn = true
                alertDialogBonusRewards.dismiss()
            }
            binding.ivClose.setOnClickListener { view -> alertDialogBonusRewards.dismiss() }
            binding.scratchView.setScratchListener(scratchListener)
            binding.btnNegative.setOnClickListener { view -> alertDialogBonusRewards.dismiss() }
            val displayRectangle = Rect()
            val window = (context as NewDashboardActivity).window
            window.decorView.getWindowVisibleDisplayFrame(displayRectangle)
            alertDialogBonusRewards.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            alertDialogBonusRewards.window!!.setLayout(
                (displayRectangle.width() *
                        0.7f).toInt(), RelativeLayout.LayoutParams.WRAP_CONTENT
            )
        }


        fun showRewardsPopupDialogBox(
            context: Context,
            scratchListener: ScratchListener,
            feedbackResponseData: FeedbackResponseData
        ) {
            if (popUpShowModels.size > 0) {
                var i = 0
                var firstData = popUpShowModels[i]
                if (popUpShowModels.size > 1 && firstData.key == "Rewards") {
                    i = 1
                    firstData = popUpShowModels[i]
                }
                when (firstData.key) {
                    "Rewards" -> if (firstData.value != null && firstData.value != "") {
                        NewDashboardActivity().showRewardsPopupNew(
                            firstData.value,
                            context,
                            scratchListener,
                            feedbackResponseData
                        )
                    }

                    "RewardsBounce" -> if (firstData.value != null && firstData.value != "") {
                        showBonusRewardsPopup(
                            firstData.value,
                            context,
                            scratchListener,
                            feedbackResponseData
                        )
                    }

                    "FeedbackPOPUP" -> {
                        val instance = getInstance()
                        instance.showPopUpFeedback((context as Activity), feedbackResponseData)
                    }

                    else -> {}
                }
                popUpShowModels.removeAt(i)
            } else {
                if (congratsText != "") {
                    showCongratsPopup(congratsText, context)
                    congratsText = ""
                }
            }
        }


        private fun showCongratsPopup(level: String, context: Context) {
            val alertBuilder =
                AlertDialog.Builder(context, R.style.CustomAlertDialog)
            val binding: CongratsPopUpBinding = DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.congrats_pop_up,
                null,
                false
            )
            alertBuilder.setView(binding.getRoot())
            val alertDialog = alertBuilder.create()
            alertDialog.setCancelable(true)
            if (!alertDialog.isShowing) alertDialog.show()
            val handler = Handler(Looper.getMainLooper())
            handler.postDelayed({ alertDialog.dismiss() }, 3000)
            SharedPref.putCurrentLevel(level.toInt())
            binding.tvLevel.text = "Level $level"
            binding.btnPositive.setOnClickListener { v -> alertDialog.dismiss() }
            val displayRectangle = Rect()
            val window = (context as Activity).window
            window.decorView.getWindowVisibleDisplayFrame(displayRectangle)
            alertDialog.window!!.setLayout(
                (displayRectangle.width() *
                        0.7f).toInt(), RelativeLayout.LayoutParams.WRAP_CONTENT
            )
        }

        fun removeRewardsPopup(
            activityId: Int,
            context: Context,
            scratchListener: ScratchListener,
            feedbackResponseData: FeedbackResponseData
        ) {
            CommonUtils.showProgressDialige(context)
            val request = RewardsPopupRequest(activityId)
            var apiInterfaceWyh: ApiInterfaceWyh =
                ApiClientWyh.getClient(CommonUtils.getBaseUrlForAPI(context))
                    .create(ApiInterfaceWyh::class.java)
            val call: Call<CommonSuccessResponse> =
                apiInterfaceWyh.rewardsPopup(SharedPref.getAuthToken(), request)
            call.enqueue(object : Callback<CommonSuccessResponse?> {
                override fun onResponse(
                    call: Call<CommonSuccessResponse?>,
                    response: Response<CommonSuccessResponse?>
                ) {
                    try {
                        CommonUtils.dismissDialoge()
                        if (response.body() != null && response.code() == 200) {
                            Analytics.logEvent(
                                context,
                                context.javaClass.name,
                                context.getString(R.string.rewards_popup_success)
                            )
                            tempPopupRewards.removeAt(0)
                            tempPopupRewards
                            if (isPositiveBtn) {
                                isPositiveBtn = false
                                val intent =
                                    Intent(context, RewardsActivity::class.java)
                                intent.putExtra("currentIndex", 0)
                                context.startActivity(intent)
                            } else {
                                if (tempPopupRewards != null && tempPopupRewards.size > 0) {
                                    popUpShowModels.add(
                                        PopUpShowModel(
                                            Constants.Rewards,
                                            tempPopupRewards[0].popupMessage
                                        )
                                    )
                                }
                                showRewardsPopupDialogBox(
                                    context,
                                    scratchListener,
                                    feedbackResponseData
                                )
                            }
                        } else {
                            Analytics.logEvent(
                                context,
                                context.javaClass.name,
                                context.getString(R.string.rewards_popup_failed)
                            )
                            Toast.makeText(
                                context,
                                context.resources.getString(R.string.error_string),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onFailure(call: Call<CommonSuccessResponse?>, t: Throwable) {
                    CommonUtils.dismissDialoge()
                    Analytics.logEvent(
                        context,
                        context.javaClass.name,
                        context.getString(R.string.rewards_popup_failed)
                    )
                    Toast.makeText(
                        context,
                        context.resources.getString(R.string.internet_connection),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }


        fun updateScratchStatus(context: Context) {
            var apiInterfaceWyh: ApiInterfaceWyh =
                ApiClientWyh.getClient(CommonUtils.getBaseUrlForAPI(context))
                    .create(ApiInterfaceWyh::class.java)

            val call: Call<CommonSuccessResponse> = apiInterfaceWyh.updateScratchStatus(
                SharedPref.getAuthToken(),
                NudgeDialogue.voucherIdRequest
            )
            call.enqueue(object : Callback<CommonSuccessResponse?> {
                override fun onResponse(
                    call: Call<CommonSuccessResponse?>,
                    response: Response<CommonSuccessResponse?>
                ) {
                    if (response.code() == 200 && response.body() != null) {
                        Analytics.logEvent(
                            context,
                            context.javaClass.name,
                            context.getString(R.string.scratch_coupon_success)
                        )
                        NudgeDialogue.voucherIdRequest = null
                    } else {
                        Analytics.logEvent(
                            context,
                            context.javaClass.name,
                            context.getString(R.string.scratch_coupon_failed)
                        )
                    }
                }

                override fun onFailure(call: Call<CommonSuccessResponse?>, t: Throwable) {
                    Analytics.logEvent(
                        context,
                        context.javaClass.name,
                        context.getString(R.string.scratch_coupon_failed)
                    )
                }
            })
        }

        fun addReferralCode(
            comingFrom: String,
            context: Context,
            binding: ActivityNewDashboardBinding
        ) {
            val alertBuilder = AlertDialog.Builder(context)
            val bindingNickName: RefferelCodeDialogeLayoutBinding = DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.refferel_code_dialoge_layout,
                null,
                false
            )
            alertBuilder.setView(bindingNickName.root)
            val alertDialog = alertBuilder.create()
            alertDialog.setCancelable(true)
            if (!alertDialog.isShowing) alertDialog.show()
            if (comingFrom.equals("referralCode", ignoreCase = true)) {
                bindingNickName.edtRefferal.visibility = View.GONE
                bindingNickName.edtVoucher.visibility = View.VISIBLE
                bindingNickName.tvName.text = context.getString(R.string.add_referral_code)
                bindingNickName.edtVoucher.hint = "Enter referral code"
                if (SharedPref.getCopReffralCode() != null) {
                    bindingNickName.edtVoucher.setText(SharedPref.getCopReffralCode())
                }
            } else {
                bindingNickName.edtRefferal.visibility = View.VISIBLE
                bindingNickName.edtVoucher.visibility = View.GONE
                bindingNickName.tvName.text = context.getString(R.string.enter_mobile_num)
                bindingNickName.edtRefferal.hint = "Enter mobile number"
                bindingNickName.edtRefferal.inputType = InputType.TYPE_CLASS_PHONE
                bindingNickName.edtRefferal.filters = arrayOf<InputFilter>(LengthFilter(10))
            }
            bindingNickName.btnCancel.setOnClickListener { v -> alertDialog.dismiss() }
            bindingNickName.btnSubmit.setOnClickListener { v ->
                if (comingFrom.equals("referralCode", ignoreCase = true)) {
                    if (bindingNickName.edtVoucher.text.toString() != "") {
                        updateCorporateCode(
                            bindingNickName.edtVoucher.text.toString(),
                            context,
                            alertDialog,
                            bindingNickName.edtRefferal
                        )
                    } else {
                        Toast.makeText(
                            context,
                            "Please enter referral code",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    if (bindingNickName.edtRefferal.text.toString() != "" && bindingNickName.edtRefferal.text.toString().length == 10) {
                        referalContest(
                            bindingNickName.edtRefferal.text.toString(),
                            alertDialog,
                            context,
                            binding
                        )
                    } else {
                        Toast.makeText(
                            context,
                            "Please enter valid mobile number",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
            val displayRectangle = Rect()
            val window = (context as Activity).window
            window.decorView.getWindowVisibleDisplayFrame(displayRectangle)
            alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            alertDialog.window!!.setLayout(
                (displayRectangle.width() *
                        0.88f).toInt(), RelativeLayout.LayoutParams.WRAP_CONTENT
            )
        }

        fun updateCorporateCode(
            refferalCode: String,
            context: Context,
            alertDialog: AlertDialog,
            edtName: EditText
        ) {
            try {
                CommonUtils.showProgressDialige(context)
                val updateCoopCodeRequest = UpdateCoopCodeRequest(refferalCode)
                val apiInterface = RetrofitHandler.apiInterface()
                apiInterface.updateCoopCode(SharedPref.getAuthToken(), updateCoopCodeRequest)
                    .enqueue(object : Callback<UpdateCoopResponse?> {
                        override fun onResponse(
                            call: Call<UpdateCoopResponse?>,
                            response: Response<UpdateCoopResponse?>
                        ) {
                            CommonUtils.dismissDialoge()
                            if (response.body() != null && response.code() == 200) {
                                if (response.body()!!.data.size != 0) {
                                    if (response.body()!!.data[0].msg != "" && response.body()!!.data != null) {
                                        if (!response.body()!!.data[0].status.equals(
                                                "false",
                                                ignoreCase = true
                                            )
                                        ) {
                                            SharedPref.putCopReferralCode(refferalCode)
                                            alertDialog.dismiss()
                                        }
                                        Toast.makeText(
                                            context,
                                            response.body()!!.data[0].msg,
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            }
                        }

                        override fun onFailure(call: Call<UpdateCoopResponse?>, t: Throwable) {
                            CommonUtils.dismissDialoge()
                        }
                    })
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }


        fun referalContest(
            referalMobileNumber: String,
            alertDialog: AlertDialog,
            context: Context,
            binding: ActivityNewDashboardBinding
        ) {
            try {
                CommonUtils.showProgressDialige(context)
                val referalContestRequest = ReferalContestRequest(
                    CryptoHelper.encrypt(
                        context,
                        SharedPref.getDecryptMobileNo()
                    ),
                    CryptoHelper.encrypt(context, referalMobileNumber.trim { it <= ' ' })
                )
                val apiInterface = RetrofitHandler.apiInterface()
                apiInterface.referalContest(SharedPref.getAuthToken(), referalContestRequest)
                    .enqueue(object : Callback<ReferalContestResponse?> {
                        override fun onResponse(
                            call: Call<ReferalContestResponse?>,
                            response: Response<ReferalContestResponse?>
                        ) {
                            CommonUtils.dismissDialoge()
                            if (response.body() != null && response.code() == 200) {
                                if (response.isSuccessful) {
                                    try {
                                        if (alertDialog.isShowing) {
                                            if (response.body()!!.data == 1) {
                                                alertDialog.dismiss()
                                                binding.referredByLayout.visibility = View.INVISIBLE
                                                binding.pointsParentLayout.visibility = View.GONE
                                                binding.newPointsParentLayout.visibility =
                                                    View.VISIBLE

                                                Toast.makeText(
                                                    context,
                                                    "Your referral number is successful added",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            } else {
                                                Toast.makeText(
                                                    context,
                                                    "Please enter happy you register number",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        }
                                    } catch (e: java.lang.Exception) {
                                        e.printStackTrace()
                                    }
                                }
                            }
                        }

                        override fun onFailure(call: Call<ReferalContestResponse?>, t: Throwable) {
                            CommonUtils.dismissDialoge()
                        }
                    })
            } catch (e: java.lang.Exception) {
                CommonUtils.dismissDialoge()
                e.printStackTrace()
            }
        }

        fun searchFeature(context: Activity, comingFrom: String) {
            val alertBuilder = AlertDialog.Builder(context)
            val bindingNickName: RefferelCodeDialogeLayoutBinding = DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.refferel_code_dialoge_layout,
                null,
                false
            )
            alertBuilder.setView(bindingNickName.root)

            val alertDialog = alertBuilder.create()
            alertDialog.setCancelable(true)


            if (!alertDialog.isShowing) alertDialog.show()

            if (comingFrom.equals("search", ignoreCase = true)) {
                bindingNickName.tvName.text = context.getString(R.string.search)
                bindingNickName.edtRefferal.hint = context.getString(R.string.search_bar_txt)
                bindingNickName.btnSubmit.text = context.getString(R.string.search)
            } else {
                bindingNickName.tvName.text = context.getString(R.string.update_username)
                bindingNickName.edtRefferal.hint = context.getString(R.string.enter_username)
                bindingNickName.btnSubmit.text = context.getString(R.string.submit)
            }
            bindingNickName.btnCancel.setOnClickListener { v ->
                if (feedbackResponseData != null && feedbackResponseData.starConfig != null) {
                    SharedPref.putFeedbackPopup(true)
                    showFeedBackPopup(feedbackResponseData, context)
                }
                isShowUserPopup = false
                alertDialog.dismiss()
            }

            bindingNickName.btnSubmit.setOnClickListener { v ->
                if (comingFrom.equals("search", ignoreCase = true)) {
                    if (!bindingNickName.edtRefferal.text.toString()
                            .isEmpty() && bindingNickName.edtRefferal.text.toString().length >= 1
                    ) {
                        alertDialog.dismiss()
                        bindingNickName.edtRefferal.error = ""
                        val intent = Intent(context, SearchActivity::class.java)
                        intent.putExtra(
                            Constants.SearchKey,
                            bindingNickName.edtRefferal.text.toString()
                        )
                        context.startActivity(intent)
                    } else {
                        bindingNickName.edtRefferal.error = "Please enter at least one character"
                    }
                } else {
                    if (!bindingNickName.edtRefferal.text.toString()
                            .isEmpty() && bindingNickName.edtRefferal.text.toString().length >= 1
                    ) {
                        alertDialog.dismiss()
                        NewDashboardActivity().updateUserDetails(
                            bindingNickName.edtRefferal.text.toString(),
                            "",
                            "",
                            "",
                            "updateUsername",
                            context
                        )
                    } else {
                        Toast.makeText(context, "Please enter name", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            val displayRectangle = Rect()
            val window = context.window
            window.decorView.getWindowVisibleDisplayFrame(displayRectangle)
            alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            alertDialog.window!!.setLayout(
                (displayRectangle.width() *
                        0.88f).toInt(), RelativeLayout.LayoutParams.WRAP_CONTENT
            )
        }

        fun showFeedBackPopup(feedbackResponseData: FeedbackResponseData, context: Context) {
            try {
                feedBackCountDown = object : CountDownTimer(15000, 1000) {
                    override fun onTick(l: Long) {
                        isFeedbackStarted = true
                        feedbackCount = (l / 1000).toInt()
                        Log.d("AuthToken", feedbackCount.toString())
                    }

                    override fun onFinish() {
                        isFeedbackStarted = false
                        if (feedbackCount == 0) {
                            SharedPref.putFeedbackPopup(true)
                            val instance = getInstance()
                            instance.showPopUpFeedback((context as Activity), feedbackResponseData)
                        }
                    }
                }.start()
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }


        fun addReferral(context: Context) {
            val request = AddReferralRequest(SharedPref.getReferredByID().replace(" ", "+"))
            var apiInterfaceWyh: ApiInterfaceWyh =
                ApiClientWyh.getClient(CommonUtils.getBaseUrlForAPI(context))
                    .create(ApiInterfaceWyh::class.java)

            val call: Call<CommonSuccessResponse> =
                apiInterfaceWyh.addReferral(SharedPref.getAuthToken(), request)
            call.enqueue(object : Callback<CommonSuccessResponse?> {
                override fun onResponse(
                    call: Call<CommonSuccessResponse?>,
                    response: Response<CommonSuccessResponse?>
                ) {
                    if (response.code() == 200 && response.body() != null && response.body()!!.isSuccess) {
                        Analytics.logEvent(
                            context,
                            context.javaClass.name,
                            context.getString(R.string.add_referral_success)
                        )
                        if (SharedPref.getReferredFor() != "Tribe") {
                            showSingleActionPopUp(
                                "Hey " + SharedPref.getUserName() + ", you have been invited to download the app by "
                                        + SharedPref.getReferredBy(),
                                "OK",
                                SharedPref.getReferredFor(),
                                false, context
                            )
                        }
                        SharedPref.putReferredByID("")
                        SharedPref.putReferredBy("")
                    } else if (response.code() == 200 && response.body() != null) {
                        SharedPref.putReferredByID("")
                        SharedPref.putReferredBy("")
                    } else {
                        Analytics.logEvent(
                            context,
                            context.javaClass.name,
                            context.getString(R.string.get_questions_failed)
                        )
                    }
                }

                override fun onFailure(call: Call<CommonSuccessResponse?>, t: Throwable) {
                    Analytics.logEvent(
                        context,
                        context.javaClass.name,
                        context.getString(R.string.get_questions_failed)
                    )
                }
            })
        }


        private fun showSingleActionPopUp(
            message: String,
            buttonText: String,
            cameFrom: String,
            isCancelable: Boolean,
            context: Context
        ) {
            val customYesNoDialog = CustomYesNoDialog(context, R.style.Theme_Dialog)
            customYesNoDialog.show()
            customYesNoDialog.setCancelable(isCancelable)
            customYesNoDialog.binding.btnCancel.visibility = View.GONE
            customYesNoDialog.binding.llActionRequired.visibility = View.GONE
            customYesNoDialog.binding.txtInfoPopUpDesc.text = message
            customYesNoDialog.binding.btnYes.text = buttonText
            customYesNoDialog.binding.btnCancel.background =
                ContextCompat.getDrawable(context, R.drawable.pink_rc_bg_8dp)
            customYesNoDialog.binding.btnYes.background =
                ContextCompat.getDrawable(context, R.drawable.blue_rc_bg_8dp)
            customYesNoDialog.binding.btnYes.setOnClickListener { view: View? -> customYesNoDialog.dismiss() }
            customYesNoDialog.binding.btnCancel.setOnClickListener { view: View? -> customYesNoDialog.dismiss() }
        }

        /*Top Up Ends*/
        fun getNudgeDetails(methodID: String, nudgeButton: String, context: Context) {
            try {
                CommonUtils.showProgressDialige(context)
                val getNudgeDetailsRequest = GetNudgeDetailsRequest(methodID, nudgeButton)
                val apiInterface = RetrofitHandler.apiInterface()
                apiInterface.getNudgeDetails(SharedPref.getAuthToken(), getNudgeDetailsRequest)
                    .enqueue(object : Callback<ResponseBody?> {
                        override fun onResponse(
                            call: Call<ResponseBody?>,
                            response: Response<ResponseBody?>
                        ) {
                            CommonUtils.dismissDialoge()
                            if (response.code() == 200 && response.isSuccessful && response.body() != null) {
                                try {
                                    val parser = JsonParser()
                                    val json =
                                        response.body()!!.string().replace("\\/".toRegex(), "/")
                                    Log.d(
                                        "AuthToken",
                                        "NudgeResponse " + response.body()!!.string()
                                    )
                                    val mJson = parser.parse(json)
                                    val json2 = parser.parse(json)
                                    val jsonObject = JSONObject(
                                        json.lowercase(Locale.getDefault())
                                            .replace("\\s".toRegex(), "")
                                    )
                                    val gson = GsonBuilder().serializeNulls().create()
                                    Log.d("AuthToken", "Nudge Parse Response  $mJson")
                                    if ((mJson as JsonObject)["data"] != null && !mJson["data"].asJsonArray.isEmpty) {
                                        if (methodID.equals("Rewards", ignoreCase = true)) {
                                            val nudgeRewardsResponse = gson.fromJson(
                                                mJson,
                                                NudgeRewardsResponse::class.java
                                            )
                                            rewardsData = nudgeRewardsResponse.data[0]
                                            rewardsDialoge(
                                                context as Activity,
                                                nudgeRewardsResponse.data[0],
                                                nudgeButton
                                            )
                                        } else if (methodID.equals(
                                                "EnG/Topup",
                                                ignoreCase = true
                                            )
                                        ) {
                                            val nudgeTopupResponse =
                                                gson.fromJson(mJson, NudgeTopupResponse::class.java)
                                            topUpData = nudgeTopupResponse.data[0]
                                            topUPDialoge(
                                                context as Activity,
                                                nudgeTopupResponse.data[0],
                                                nudgeButton
                                            )
                                        }
                                    } else {
                                        Toast.makeText(
                                            context,
                                            "No upcoming activity",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                } catch (e: java.lang.Exception) {
                                    e.printStackTrace()
                                }
                            } else {
                                Toast.makeText(context, "No upcoming activity", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }

                        override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                            Log.d("AuthToken", "NudgeResponse " + "onFailure")
                        }
                    })
            } catch (e: java.lang.Exception) {
                Log.d("AuthToken", "NudgeResponseException" + e.message)
                e.printStackTrace()
            }
        }


        fun showPopUpZenZone(currentTime: Double, context: Context) {
            val alertBuilder = AlertDialog.Builder(context)
            val zenZoneBinding: ZenZoneLayoutBinding = DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.zen_zone_layout,
                null,
                false
            )
            alertBuilder.setView(zenZoneBinding.getRoot())
            val alertDialog = alertBuilder.create()
            alertDialog.setCancelable(true)
            alertDialog.setOnDismissListener { dialogInterface: DialogInterface? -> alertDialog.dismiss() }
            zenZoneBinding.btnTen.setOnClickListener { v -> zenZoneBinding.etMins.setText("10") }
            zenZoneBinding.btnTwenty.setOnClickListener { v -> zenZoneBinding.etMins.setText("20") }
            zenZoneBinding.btnForty.setOnClickListener { v -> zenZoneBinding.etMins.setText("40") }
            zenZoneBinding.btnClosed.setOnClickListener { view -> alertDialog.dismiss() }
            zenZoneBinding.btnAdd.setOnClickListener { view ->
                if (!zenZoneBinding.etMins.getText().toString().isEmpty()) {
                    alertDialog.dismiss()
                    val meditationMinutes =
                        zenZoneBinding.etMins.getText().toString().toDouble() as Int
                    NewDashboardActivity().UploadActivityData(
                        Constants.MEDITATION,
                        meditationMinutes + currentTime
                    )
                } else {
                    Toast.makeText(context, "Please add time", Toast.LENGTH_SHORT).show()
                }
            }

            zenZoneBinding.btnViewMore.setOnClickListener { view ->
                alertDialog.dismiss()
                val intent = Intent(context, TrendsActivity::class.java)
                intent.putExtra("activityType", Constants.MEDITATION)
                context.startActivity(intent)
            }

            if (!alertDialog.isShowing) alertDialog.show()
            val displayRectangle = Rect()
            val window: Window = (context as Activity).window
            window.decorView.getWindowVisibleDisplayFrame(displayRectangle)
            alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            alertDialog.window!!.setLayout(
                (displayRectangle.width() *
                        0.8f).toInt(), RelativeLayout.LayoutParams.WRAP_CONTENT
            )
        }

        private fun FetchQuizReward() {
            val activityRewardRequest = ActivityRewardRequest(trasactionId, featureName)

            val call =
                apiInterface().FetchQuizReward(SharedPref.getAuthToken(), activityRewardRequest)

            call.enqueue(object : Callback<CommonSuccessResponse?> {
                override fun onResponse(
                    call: Call<CommonSuccessResponse?>,
                    response: Response<CommonSuccessResponse?>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        trasactionId = null
                        featureName = null
                        if (response.body()!!.quizathonRewardData != null) {
                            getQuizathonRewardPopup(response.body()!!.quizathonRewardData)
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

        private fun getSpinRewardPopup(data: SpinRewardsData) {
            try {
                if (data.rewardType != null && !data.rewardType.isEmpty()) {
                    val rewardtype = data.rewardType
                    if (rewardtype.equals("Points", ignoreCase = true)) {
                        PostSpinDialog.showPostSpinnerPointsPopupCallBack(
                            data.rewardTitle, context, "Trends", data.rewardDescription,
                            data.rewardHeader1, data.rewardHeader2, scratchListener,
                            rewardDialogCloselistener
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
                            scratchListener,
                            rewardDialogCloselistener
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
                            scratchListener,
                            rewardDialogCloselistener
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
                            scratchListener,
                            rewardDialogCloselistener
                        )
                    } else if (rewardtype.equals("Stamps", ignoreCase = true)) {
                        PostSpinDialog.showStampsPopupCallBack(
                            data.rewardHeader1,
                            data.rewardHeader2,
                            data.rewardTitle,
                            data.rewardValue,
                            context,
                            scratchListener,
                            rewardDialogCloselistener
                        )
                    }
                }
            } catch (ex: java.lang.Exception) {
            }
        }

        private fun getQuizathonRewardPopup(data: QuizathonRewardData) {
            try {
                if (data.rewardType != null && !data.rewardType.isEmpty()) {
                    val rewardtype = data.rewardType
                    if (rewardtype.equals("Points", ignoreCase = true)) {
                        QuizRewardDialog.showPostSpinnerPointsPopupCallBack(
                            data.rewardTitle, context, "Trends", data.rewardDescription,
                            data.rewardHeader1, data.rewardHeader2,
                            scratchListener,
                            rewardDialogCloselistener
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
                            scratchListener,
                            rewardDialogCloselistener
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
                            scratchListener,
                            rewardDialogCloselistener
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
                            scratchListener,
                            rewardDialogCloselistener
                        )
                    } else if (rewardtype.equals("Stamps", ignoreCase = true)) {
                        QuizRewardDialog.showStampsPopupCallBack(
                            data.rewardHeader1,
                            data.rewardHeader2,
                            data.rewardTitle,
                            data.rewardValue,
                            context,
                            scratchListener,
                            rewardDialogCloselistener
                        )
                    }
                }
            } catch (ex: java.lang.Exception) {
            }
        }


        fun getIRAScore(context: Context, comingFrom: String) {
            try {
                val iraRequest = ConversationRequest(Constants.IRA_INTEGRATION_ID, "")
                val apiInterface = ApiClientWyh.getClient(CommonUtils.getBaseUrlForAPI(context))
                    .create(ApiInterfaceWyh::class.java)
                apiInterface.getIRAHealthScore(SharedPref.getAuthToken(), iraRequest)
                    .enqueue(object : Callback<IRAHealthScoreResponse?> {
                        override fun onResponse(
                            call: Call<IRAHealthScoreResponse?>,
                            response: Response<IRAHealthScoreResponse?>
                        ) {
                            if (response != null) {
                                if (response.body() != null && response.code() == 200) {
                                    iraHealthScoreResponse = response.body()
                                    val isAnalysis =
                                        iraHealthScoreResponse != null && iraHealthScoreResponse!!.iraHealthScoreData != null && iraHealthScoreResponse!!.iraHealthScoreData
                                            .playSports != null && !iraHealthScoreResponse!!.iraHealthScoreData
                                            .playSports
                                            .isEmpty() && iraHealthScoreResponse!!.iraHealthScoreData
                                            .playSports.toInt() > 0
                                    val isShown = SharedPref.getWellBeingIntroShownImmunity()
                                    if (comingFrom.equals("redirection", ignoreCase = true)) {
                                        val intent =
                                            Intent(context, WellBeingDisclaimerActivity::class.java)
                                        intent.putExtra("came_from", "KnowYourImmunity")
                                        intent.putExtra("CategoryName", "Immunity Score")
                                        intent.putExtra("isAnalysis", isAnalysis)
                                        intent.putExtra("shownScreen", true)
                                        context.startActivity(intent)
                                    } else {
                                        if (!isShown) {
                                            SharedPref.putWellBeingIntroShownImmunity(true)
                                            val intent = Intent(
                                                context,
                                                WellBeingDisclaimerActivity::class.java
                                            )
                                            intent.putExtra("came_from", "KnowYourImmunity")
                                            intent.putExtra("CategoryName", "Immunity Score")
                                            intent.putExtra("isAnalysis", isAnalysis)
                                            intent.putExtra("shownScreen", true)
                                            context.startActivity(intent)
                                        } else {
                                            val intent: Intent = if (isAnalysis) {
                                                Intent(context, IRAAnalysisActivity::class.java)
                                            } else {
                                                Intent(context, IraActivity::class.java)
                                            }
                                            intent.putExtra("from", Constants.IRA_STATUS_COMPLETED)
                                            context.startActivity(intent)
                                        }
                                    }
                                }
                            }
                        }

                        override fun onFailure(call: Call<IRAHealthScoreResponse?>, t: Throwable) {}
                    })
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }


        fun waterAlertDialoge(context: Context?) {
            try {
                val customYesNoDialog = CustomYesNoDialog(context!!, R.style.Theme_Dialog)
                customYesNoDialog.show()
                customYesNoDialog.setCancelable(false)
                customYesNoDialog.binding.llActionRequired.visibility = View.GONE
                customYesNoDialog.binding.txtInfoPopUpDesc.text =
                    "You have exceeded the water intake limit, still, you have wanted to add water ?"
                customYesNoDialog.binding.btnYes.text = "Yes"
                customYesNoDialog.binding.btnCancel.text = "No"
                customYesNoDialog.binding.btnYes.background = ContextCompat.getDrawable(
                    context, R.drawable.blue_rc_bg_8dp
                )
                customYesNoDialog.binding.btnCancel.background = ContextCompat.getDrawable(
                    context, R.drawable.pink_rc_bg_8dp
                )
                customYesNoDialog.binding.btnYes.setOnClickListener { view: View? ->
                    customYesNoDialog.dismiss()
                    SharedPref.putWaterIntake(true)
                }
                customYesNoDialog.binding.btnCancel.setOnClickListener { view: View? ->
                    customYesNoDialog.dismiss()
                    SharedPref.putWaterIntake(false)
                }
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }


        fun getAbsorbData(context: Context, comingFrom: String) {
            try {
                CommonUtils.showProgressDialige(context)
                val dashboardDataRequest = GetDashboardDataRequest("", "")
                val apiInterface = ApiClientWyh.getClient(CommonUtils.getBaseUrlForAPI(context))
                    .create(ApiInterfaceWyh::class.java)
                apiInterface.getAbsorbDashboard(SharedPref.getAuthToken(), dashboardDataRequest)
                    .enqueue(object : Callback<GetDashboardDataResponse?> {
                        override fun onResponse(
                            call: Call<GetDashboardDataResponse?>,
                            response: Response<GetDashboardDataResponse?>
                        ) {
                            CommonUtils.dismissDialoge()
                            if (response.body() != null && response.code() == 200) {
                                if (response.body()!!.data != null && response.body()!!.data.tagName != null) {
                                    tagNameList = response.body()!!.data.tagName
                                    if (comingFrom.equals("blogs", ignoreCase = true)) {
                                        val i = Intent(context, QuickReadDashboard::class.java)
                                        i.putExtra("taglist", tagNameList as Serializable?)
                                        context.startActivity(i)
                                    } else {
                                        val i = Intent(context, HealthTvListingActivity::class.java)
                                        i.putExtra("taglist", tagNameList as Serializable?)
                                        context.startActivity(i)
                                    }
                                }
                            }
                        }

                        override fun onFailure(
                            call: Call<GetDashboardDataResponse?>,
                            t: Throwable
                        ) {
                            CommonUtils.dismissDialoge()
                        }
                    })
            } catch (e: java.lang.Exception) {
                CommonUtils.dismissDialoge()
                e.printStackTrace()
            }
        }


        fun getHeartAnalysis(context: Context, comingFrom: String) {
            try {
                CommonUtils.showProgressDialige(context)
                val request = GetHeartAgeAnalysisRequest(
                    context.getString(R.string.heartage_i_id),
                    SharedPref.getHeartAgeConversationID()
                )
                val apiInterface = ApiClientWyh.getClient(CommonUtils.getBaseUrlForAPI(context))
                    .create(ApiInterfaceWyh::class.java)
                apiInterface.getHeartAgeAnalysis(SharedPref.getAuthToken(), request)
                    .enqueue(object : Callback<HeartAgeAnalysisResponse?> {
                        override fun onResponse(
                            call: Call<HeartAgeAnalysisResponse?>,
                            response: Response<HeartAgeAnalysisResponse?>
                        ) {
                            try {
                                CommonUtils.dismissDialoge()
                                if (response.body() != null && response.code() == 200) {
                                    SharedPref.putHeartAgeAnalysis(Gson().toJson(response.body()))
                                    heartAgeAnalysisResponse =
                                        Gson().fromJson<HeartAgeAnalysisResponse>(
                                            Gson().toJson(response.body()),
                                            HeartAgeAnalysisResponse::class.java
                                        )
                                    val isAnalysis =
                                        heartAgeAnalysisResponse != null && heartAgeAnalysisResponse!!.getData() != null
                                    val isShown = SharedPref.getWellBeingIntroShownHeartAge()
                                    if (comingFrom.equals("redirection", ignoreCase = true)) {
                                        val intent =
                                            Intent(context, WellBeingDisclaimerActivity::class.java)
                                        intent.putExtra("came_from", "HeartAge")
                                        intent.putExtra("CategoryName", "Heart Age")
                                        intent.putExtra("isAnalysis", isAnalysis)
                                        intent.putExtra("shownScreen", true)
                                        context.startActivity(intent)
                                    } else {
                                        if (!isShown) {
                                            SharedPref.putWellBeingIntroShownHeartAge(true)
                                            val intent =
                                                Intent(
                                                    context,
                                                    WellBeingDisclaimerActivity::class.java
                                                )
                                            intent.putExtra("came_from", "HeartAge")
                                            intent.putExtra("CategoryName", "Heart Age")
                                            intent.putExtra("isAnalysis", isAnalysis)
                                            intent.putExtra("shownScreen", true)
                                            context.startActivity(intent)
                                        } else {
                                            val intent: Intent = if (isAnalysis) {
                                                Intent(
                                                    context,
                                                    HeartAgeAnalysisActivity::class.java
                                                )
                                            } else {
                                                Intent(
                                                    context,
                                                    HeartAgeQuestionsActivity::class.java
                                                )
                                            }
                                            context.startActivity(intent)
                                        }
                                    }
                                }
                            } catch (e: Exception) {
                                e.toString()
                            }

                        }

                        override fun onFailure(
                            call: Call<HeartAgeAnalysisResponse?>,
                            t: Throwable
                        ) {
                            CommonUtils.dismissDialoge()
                        }
                    })
            } catch (e: java.lang.Exception) {
                CommonUtils.dismissDialoge()
                e.printStackTrace()
            }
        }

    }


}