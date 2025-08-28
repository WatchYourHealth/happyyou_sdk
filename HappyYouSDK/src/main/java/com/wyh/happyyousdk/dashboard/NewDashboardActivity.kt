package com.wyh.happyyousdk.dashboard

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.*
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.esafirm.imagepicker.features.ImagePicker
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.google.gson.Gson
import com.nekolaboratory.EmulatorDetector
import com.scottyab.rootbeer.RootBeer
import com.wyh.happyyousdk.*
import com.wyh.happyyousdk.APIEncryption.APIInterface
import com.wyh.happyyousdk.APIEncryption.APILogs
import com.wyh.happyyousdk.APIEncryption.APILogs.activityTracker
import com.wyh.happyyousdk.APIEncryption.BackgroundWork.CoroutineClass
import com.wyh.happyyousdk.APIEncryption.RetrofitHandler
import com.wyh.happyyousdk.ChallangesModule.Activities.ChallangesActivity
import com.wyh.happyyousdk.ChallangesModule.Activities.ChallengeDetailActivity
import com.wyh.happyyousdk.ChallangesModule.Activities.NewTribeChallengeDashboard
import com.wyh.happyyousdk.ChallangesModule.ClickInterface.ChallengeClick
import com.wyh.happyyousdk.ChallangesModule.Utils.TribeChallengeHelper
import com.wyh.happyyousdk.ChallangesModule.helperClass
import com.wyh.happyyousdk.ChallangesModule.helperClass.getFilePathFromImage
import com.wyh.happyyousdk.ChallangesModule.helperClass.getFileSizeFromPath
import com.wyh.happyyousdk.Goals.Activities.GoalsDashboard
import com.wyh.happyyousdk.Sonde.Activities.RespiratoryCheck
import com.wyh.happyyousdk.SpinWheel.Activities.DashboardWheelActivity
import com.wyh.happyyousdk.SpinWheel.Activities.spinwheelreward.SpinWheelRewardsActivity
import com.wyh.happyyousdk.SpinWheel.Utilities.DownloadImage
import com.wyh.happyyousdk.SpinWheel.Utilities.RewardInfoBottomSheet
import com.wyh.happyyousdk.SpinWheel.Utilities.onRewardDialogClick
import com.wyh.happyyousdk.SpinWheel.onRewardClick
import com.wyh.happyyousdk.SpinWheel.rewardDialogCloseListener
import com.wyh.happyyousdk.SpinWheel.spinRewardCallBack
import com.wyh.happyyousdk.absorb.HealthHacksActivity
import com.wyh.happyyousdk.addFamily.AddFamilyListActivity
import com.wyh.happyyousdk.common.adapter.IndicatorsAdapter
import com.wyh.happyyousdk.contacts.ContactsActivityNew
import com.wyh.happyyousdk.corporateAccount.ActivityCorporateAccountAddEdit
import com.wyh.happyyousdk.corporateAccount.ActivityCorporateAccountMain
import com.wyh.happyyousdk.crypto.RSAEncryption
import com.wyh.happyyousdk.crypto.RSAEncryption.callDecryptionMethod
import com.wyh.happyyousdk.dashboard.DashboardHelper.completeRewardsActivity
import com.wyh.happyyousdk.dashboard.DashboardHelper.completeTopUpActivity
import com.wyh.happyyousdk.dashboard.DashboardHelper.eventType
import com.wyh.happyyousdk.dashboard.DashboardHelper.saveImage
import com.wyh.happyyousdk.dashboard.Fragments.HappyMartFragment
import com.wyh.happyyousdk.dashboard.Fragments.ICEFragment
import com.wyh.happyyousdk.dashboard.Fragments.OtherFragment
import com.wyh.happyyousdk.dashboard.RedirectionMethod.getNudgeImage
import com.wyh.happyyousdk.dashboard.RedirectionMethod.redirectionScreen
import com.wyh.happyyousdk.dashboard.adapter.*
import com.wyh.happyyousdk.dashboard.helper.NewDashboardHelper
import com.wyh.happyyousdk.dashboard.helper.NewDashboardHelper.Companion.NOTIFICATION_PERMISSION_ID
import com.wyh.happyyousdk.dashboard.helper.NewDashboardHelper.Companion.PERMISSION_ID
import com.wyh.happyyousdk.dashboard.helper.NewDashboardHelper.Companion.addReferralCode
import com.wyh.happyyousdk.dashboard.helper.NewDashboardHelper.Companion.dashquizathonRewardList
import com.wyh.happyyousdk.dashboard.helper.NewDashboardHelper.Companion.featureName
import com.wyh.happyyousdk.dashboard.helper.NewDashboardHelper.Companion.rewardsData
import com.wyh.happyyousdk.dashboard.helper.NewDashboardHelper.Companion.searchFeature
import com.wyh.happyyousdk.dashboard.helper.NewDashboardHelper.Companion.showPopUpZenZone
import com.wyh.happyyousdk.dashboard.helper.NewDashboardHelper.Companion.showRewardsPopupDialogBox
import com.wyh.happyyousdk.dashboard.helper.NewDashboardHelper.Companion.topUpData
import com.wyh.happyyousdk.dashboard.helper.NewDashboardHelper.Companion.trasactionId
import com.wyh.happyyousdk.dashboard.helper.NewDashboardHelper.Companion.waterIntake
import com.wyh.happyyousdk.dashboard.helper.NewDashboardHelper.Companion.waterIntakeAllowed
import com.wyh.happyyousdk.dashboard.helper.NewDashboardHelper.Companion.waterIntakeGoal
import com.wyh.happyyousdk.dashboard.model.GetBannerResponse
import com.wyh.happyyousdk.databinding.*
import com.wyh.happyyousdk.diary.NewDiaryActivity
import com.wyh.happyyousdk.ehr.EhrActivity
import com.wyh.happyyousdk.fileshare.PhotoViewActivityFileShare
import com.wyh.happyyousdk.happyMarket.HappyMartCategory
import com.wyh.happyyousdk.happyMarket.HappyMartDisclaimerActivity
import com.wyh.happyyousdk.happyMarket.NewHappyMartActivity
import com.wyh.happyyousdk.hra.HRAAnalysisActivity
import com.wyh.happyyousdk.hra.HRAQuestionsActivity
import com.wyh.happyyousdk.ice.ICEDashboardActivity
import com.wyh.happyyousdk.ice.SOSActivity
import com.wyh.happyyousdk.model.*
import com.wyh.happyyousdk.model.request.*
import com.wyh.happyyousdk.model.request.absorb.AddBookmarkRequest
import com.wyh.happyyousdk.model.request.ehr.FileData
import com.wyh.happyyousdk.model.request.encrDecr.EncryptionRequest
import com.wyh.happyyousdk.model.request.hra.GetAnalysisRequest
import com.wyh.happyyousdk.model.request.kgi_policy.GetPolicyDetailsRequest
import com.wyh.happyyousdk.model.request.login.AddFCMTokenRequest
import com.wyh.happyyousdk.model.request.login.ApiSessionLogRequest
import com.wyh.happyyousdk.model.request.login.RefreshTokenRequest
import com.wyh.happyyousdk.model.request.postloginreward.StartSpinActivityRequest
import com.wyh.happyyousdk.model.request.quizathon.ActivityRewardRequest
import com.wyh.happyyousdk.model.request.quizathon.ClaimRewardRequest
import com.wyh.happyyousdk.model.request.rewards.ActivityProgressRequest
import com.wyh.happyyousdk.model.request.rewards.AppFeedbackRequest
import com.wyh.happyyousdk.model.request.rewards.StartRewardsActivityRequest
import com.wyh.happyyousdk.model.request.trends.AddReminderDataRequest
import com.wyh.happyyousdk.model.response.*
import com.wyh.happyyousdk.model.response.absorb.AddBookmarkResponse
import com.wyh.happyyousdk.model.response.dashboard.ActOMeterTribe
import com.wyh.happyyousdk.model.response.dashboard.FilePopupModel
import com.wyh.happyyousdk.model.response.dashboard.RewardsResponse
import com.wyh.happyyousdk.model.response.dashboard_new.ShowAdminRewardsEventsData
import com.wyh.happyyousdk.model.response.encrDecr.EncryptionResponse
import com.wyh.happyyousdk.model.response.getAnalysis.GetAnalysisResponse
import com.wyh.happyyousdk.model.response.kgi_policy.GetPolicyDetailsResponse
import com.wyh.happyyousdk.model.response.kgi_policy.GetPolicyDetailsUserPolicyDetail
import com.wyh.happyyousdk.model.response.login.RefreshTokenResponse
import com.wyh.happyyousdk.model.response.playwin.ClaimReClaimRewardModel
import com.wyh.happyyousdk.model.response.playwin.QuizathonModel
import com.wyh.happyyousdk.model.response.playwin.QuizathonResponseModel
import com.wyh.happyyousdk.model.response.playwin.QuizathonRewardData
import com.wyh.happyyousdk.model.response.playwin.SaveQuizRegistrationResponse
import com.wyh.happyyousdk.model.response.postloginreward.CommonResponse
import com.wyh.happyyousdk.model.response.postloginreward.RewardItem
import com.wyh.happyyousdk.model.response.rewards.ActivityProgressResponse
import com.wyh.happyyousdk.model.response.rewards.TopUp
import com.wyh.happyyousdk.network.ApiClientWyh
import com.wyh.happyyousdk.network.ApiInterfaceWyh
import com.wyh.happyyousdk.notification.NotificationDrawerActivity
import com.wyh.happyyousdk.play_and_win.PlayAndWinActivity
import com.wyh.happyyousdk.play_and_win.adapter.GridPlaywinAdapter
import com.wyh.happyyousdk.policyDetail.PolicyListActivity
import com.wyh.happyyousdk.policyDetail.PolicySearchActivity
import com.wyh.happyyousdk.policyDetail.PolicyViewMore
import com.wyh.happyyousdk.policyDetails.SearchPolicyActivity
import com.wyh.happyyousdk.profile.AddUserDetails
import com.wyh.happyyousdk.profile.ConnectionListModel
import com.wyh.happyyousdk.profile.DialogActivity
import com.wyh.happyyousdk.profile.ProfileActivity
import com.wyh.happyyousdk.quiz.adapter.QuizOptionAdapter
import com.wyh.happyyousdk.quizathon.QuizathonActivity
import com.wyh.happyyousdk.quizathon.QuizathonScoreActivity
import com.wyh.happyyousdk.quizathon.QuizathonViewAllActivity
import com.wyh.happyyousdk.quizathon.dialog.ClaimReClaimDialog
import com.wyh.happyyousdk.quizathon.dialog.QuizMessageDialog
import com.wyh.happyyousdk.quizathon.dialog.RetakeAlertDialog
import com.wyh.happyyousdk.reminders.RemindersActivity
import com.wyh.happyyousdk.rewards.FeedbackPopupDialogBox.Companion.getInstance
import com.wyh.happyyousdk.rewards.RewardsActivity
import com.wyh.happyyousdk.sendActivityData.SendDataToServerReceiver
import com.wyh.happyyousdk.syncDevice.SyncDeviceActivity
import com.wyh.happyyousdk.trends.TrendsActivity
import com.wyh.happyyousdk.unwind.UnwindActivity
import com.wyh.happyyousdk.utils.*
import com.wyh.happyyousdk.utils.CommonUtils
import com.wyh.happyyousdk.utils.CommonUtils.deleteImage
import com.wyh.happyyousdk.utils.CommonUtils.downloadImage
import com.wyh.happyyousdk.utils.CommonUtils.formatDateFromString
import com.wyh.happyyousdk.utils.CommonUtils.showDailyDialogPerSession
import com.wyh.happyyousdk.utils.Master.logOut
import com.wyh.happyyousdk.utils.dialog.QuizRewardDialog.showPostSpinnerBadgePopupCallBack
import com.wyh.happyyousdk.utils.dialog.QuizRewardDialog.showPostSpinnerOfferCallBack
import com.wyh.happyyousdk.utils.dialog.QuizRewardDialog.showPostSpinnerPointsPopupCallBack
import com.wyh.happyyousdk.utils.dialog.QuizRewardDialog.showPostSpinnerVoucherCallBack
import com.wyh.happyyousdk.utils.dialog.QuizRewardDialog.showStampsPopupCallBack
import com.wyh.happyyousdk.utils.dialog.RegistrationDialog
import com.wyh.happyyousdk.utils.viewtooltip.ViewTooltip
import com.wyh.happyyousdk.utils.wheelview.WheelItem
import com.wyhsdk.main.WatchYourHealth
import com.wyhsdk.sharedPreferences.SharedPreference
import com.wyhsdk.utils.Utilities
import dev.skymansandy.scratchcardlayout.listener.ScratchListener
import dev.skymansandy.scratchcardlayout.ui.ScratchCardLayout
import okhttp3.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.IOException
import java.lang.String.format
import java.net.URLConnection
import java.text.DecimalFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.ceil


@Suppress("DEPRECATED_IDENTITY_EQUALS", "UNREACHABLE_CODE")
class NewDashboardActivity : AppCompatActivity(), ScratchListener, KYWClick, ChallengeClick,
    rewardDialogCloseListener,
    AddBookMark, onRewardClick, spinRewardCallBack {

    lateinit var binding: ActivityNewDashboardBinding
    lateinit var knowYourWellBeingAdapter: KnowYourWellBeingAdapter
    var feedbackResponseData: FeedbackResponseData = FeedbackResponseData()
    var rewardsModel = RewardsModel()
    lateinit var context: Context
    lateinit var tvName: TextView
    lateinit var levelLayout: LinearLayout
    lateinit var referralCopyImg: ImageView
    lateinit var referralShareImg: ImageView
    var redirectionURL = ""
    var vendorLogo = ""
    var notification_type = ""
    var communityId = ""
    var happyMartImageURL = ""
    var happyMartCategory = ""
    var happyMartRedirectionURL = ""
    var firstRedirection = ""
    var secondRedirection = ""
    var thirdRedirection = ""
    var fourthRedirection = ""
    var yesNoAnswer = ""
    var journalContent = ""
    var imageName = ""
    var nudgeBanner: NudgeBanner = NudgeBanner()


    var fileDataList = arrayListOf<FileData>()
    var showAdminRewardsEventsData = arrayListOf<ShowAdminRewardsEventsData>()
    var allPolicyDetails = arrayListOf<PolicyDetails>()
    var allBannerData = arrayListOf<GetBannerResponse.Data>()
    lateinit var userDetails: UserDetail
    var uploadFileTopUp: TopUp? = null
    var selectedPopupTribe = Constants.STEPS

    var userStepsGoal = 0
    var totalStepsDays = 0
    var glasses = 0
    var getAnalysisResponse: GetAnalysisResponse? = null
    lateinit var mainStartCalender: Calendar
    var gender = ""
    var otherGender = ""
    var user = "self"
    var comingFrom = ""
    var dobStr: String = ""
    var message = ""
    var iskgicomingfrom = false
    var isRefreshTokenGenerated = false
    var isUserPopupShown = false

    lateinit var apiInterface: APIInterface
    lateinit var apiInterfaceWyh: ApiInterfaceWyh

    lateinit var customPopUpRewardsJournalUploadBinding: CustomPopUpRewardsJournalUploadBinding
    lateinit var customPopupRewardsBinding: CustomPopupRewardsBinding
    var parts: List<MultipartBody.Part> = ArrayList()
    var fileUploadKey = "ActivityUploads"
    lateinit var progressDialog: ProgressDialog
    lateinit var alertDialog: AlertDialog
    lateinit var alertDialogScratchAndWin: AlertDialog
    lateinit var levelActivityJournalUploadAlertDialog: AlertDialog
    private val CAMERA_PERMISSION_CODE = 100
    var challengesList: kotlin.collections.ArrayList<challengesData> = arrayListOf()
    var actoMeterTribe: ActOMeterTribe = ActOMeterTribe()
    var progressGoals: Int = 0
    var currentLevel: Int = 0
    var totalPoints: Int = 0
    var isUserDetailAPIIsInProgress = false
    var isCameFromSpinWheel = false
    var isCorporateUser = 0
    var authDataresp: AuthDataResp = AuthDataResp()
    var clientsPolicyData: ArrayList<ClientResponseDashboard> = arrayListOf()
    lateinit var dialog: AlertDialog
    lateinit var reward: QuizathonRewardData
    lateinit var claimReclaimDialog: ClaimReClaimDialog
    var quizathonModelPointBurn: QuizathonModel? = null
    var quizathonModelRegister: QuizathonModel? = null
    var burnType: String = ""
    var click: String = ""
    var titleContent: String = ""
    var claimReward: RewardItem? = null
    var format: DecimalFormat = DecimalFormat("0.##")
    private var builder: AlertDialog.Builder? = null
    var index = 0
    var currentValueZenZone: String = "0"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(
            this@NewDashboardActivity,
            R.layout.activity_new_dashboard
        )
        context = this@NewDashboardActivity
        claimReclaimDialog = ClaimReClaimDialog(context)
        SharedPref.init(context)
        SharedPreference.init(context)



        NewDashboardHelper.scratchListener = this@NewDashboardActivity
        NewDashboardHelper.rewardDialogCloselistener = this@NewDashboardActivity
        NewDashboardHelper.binding = binding
        NewDashboardHelper.context = this
        progressDialog = ProgressDialog(context, R.style.ProgressBarTheme)
        progressDialog!!.setCancelable(false)
        progressDialog!!.setMessage("Please wait...")

        apiInterfaceWyh = ApiClientWyh.getClient(CommonUtils.getBaseUrlForAPI(context))
            .create(ApiInterfaceWyh::class.java)
        apiInterface = RetrofitHandler.apiInterface()

        Glide.with(context)
            .load(CommonUtils.getBaseUrlForAPI(context) + SDKConstants.endPointForImages + "ic_bear_mart.png")
            .into(binding.ivHappyMart)

        if (SharedPref.getCorporateAccountNotFound()) {
            Log.e(
                "getCorporateAccountNotFound",
                SharedPref.getCorporateAccountNotFound().toString()
            );
            showCorporateAlert();
        }
        if (SharedPref.getDecryptMobileNo()
                .isNotEmpty() && SharedPref.getDecryptMobileNo().length == 10
        ) {
            SharedPref.putDecryptMobileNo(SharedPref.getDecryptMobileNo())
        }
//        logAppEvent(context, "You are on Dashboard", null)//// Meta Removed 17/03/2025

        iskgicomingfrom = intent.getBooleanExtra("isKgiCommingFrom", false)
        isCameFromSpinWheel = intent.getBooleanExtra("isCameFromSpinWheel", false)
        NewDashboardHelper.watchYourHealth = WatchYourHealth(context, SharedPref.getUuid())
        NewDashboardHelper.watchYourHealth.initializeAPIClient(savedInstanceState)
        sideDrawer()

        binding.ivBack.setOnClickListener {
            finish()
        }

        if (intent.getStringExtra("comingFrom").toString() != null) {
            comingFrom = intent.getStringExtra("comingFrom").toString()
            if (comingFrom.equals("iceFragment", true)) {
                binding.dashboardDesign.visibility = View.GONE
                binding.frameContainer.visibility = View.VISIBLE
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.frame_container, ICEFragment())
                transaction.addToBackStack(null)
                transaction.commit()
            } else if (comingFrom.equals("happyMartFragment", true)) {
                binding.dashboardDesign.visibility = View.GONE
                binding.frameContainer.visibility = View.VISIBLE
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.frame_container, HappyMartFragment())
                transaction.addToBackStack(null)
                transaction.commit()
            } else if (comingFrom.equals("happyMartOther", true)) {
                binding.dashboardDesign.visibility = View.GONE
                binding.frameContainer.visibility = View.VISIBLE
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.frame_container, OtherFragment())
                transaction.addToBackStack(null)
                transaction.commit()
            }
        }

        if (intent.getSerializableExtra("loginRewardsModel") != null) {
            rewardsModel = (intent.getSerializableExtra("loginRewardsModel") as RewardsModel)
            NewDashboardHelper.rewardsModel = rewardsModel
        }



        /*if (SharedPref.getGFitScreenShowedOn() == CommonUtils.todayDateInFormat("dd/MM/yyyy")) {

        } else if (!SharedPreference.getGoogleFitConnection() && (rewardsModel == null || rewardsModel.reward == null)) {
            SharedPref.putGFitScreenShowedOn(CommonUtils.todayDateInFormat("dd/MM/yyyy"))
            val intent = Intent(context, SyncDeviceActivity::class.java)
            startActivity(intent)
            Toast.makeText(
                context,
                "Connect to Google Fit to stay in sync with your steps",
                Toast.LENGTH_SHORT
            ).show()
        }*/

        if (!SharedPref.getReferredBy().isEmpty()) {
            if (SharedPref.getReferredFor() == "ChallengeDetails") {
                startActivity(Intent(context, ChallangesActivity::class.java))
            }
            if (SharedPref.getReferredByID().trim { it <= ' ' } != SharedPref.getUuid()
                    .replace("\\+".toRegex(), " ").trim { it <= ' ' }) {
                NewDashboardHelper.addReferral(context)
            } else {
                if (SharedPref.getReferredComId().isEmpty()) {
                    SharedPref.putReferredByID("")
                    SharedPref.putReferredBy("")
                }
            }
        }

        if (!SharedPref.getReferredComId().isEmpty()) {
            if (SharedPref.getReferredByID().trim { it <= ' ' } != SharedPref.getUuid()
                    .replace("\\+".toRegex(), " ").trim { it <= ' ' }) {

            } else {
                SharedPref.putReferredByID("")
                SharedPref.putReferredBy("")
                SharedPref.putReferredComId("")
                SharedPref.putReferredComName("")
                SharedPref.putReferredFor("")
            }
        }

        if (SharedPref.getAesUuid().isEmpty()) {
            decryptRSA(SharedPref.getUuid())
        }

        try {
            notification_type = intent.getStringExtra("notification_type").toString()
            communityId = intent.getStringExtra("communityId").toString()
            happyMartImageURL = intent.getStringExtra("happyMartImageURL").toString()
            happyMartCategory = intent.getStringExtra("happyMartCategory").toString()
            happyMartRedirectionURL = intent.getStringExtra("happyMartRedirectionURL").toString()
            redirectionURL = intent.getStringExtra("redirectionURL").toString()
            vendorLogo = intent.getStringExtra("vendorLogo").toString()

            //notification is remaining
            if (notification_type != null) {
                processNotificationData(context)
            }

        } catch (e: Exception) {
            e.toString()
        }

        requestNotificationPermission()

        /* if (rewardsModel != null && rewardsModel.reward != null && rewardsModel.reward.contains("First Login")) {
            NewDashboardHelper.isShowUserPopup = false
            NewDashboardHelper.showIsFirstLogin(rewardsModel.reward, context, this)
        }*/
        /* if (rewardsModel != null && rewardsModel.reward != null && rewardsModel.reward.contains("First Login")) {
             NewDashboardHelper.isShowUserPopup = false
             NewDashboardHelper.showIsFirstLogin(rewardsModel.reward, context, this)
         }*/


        if (!SharedPref.getPushNotificationToken().isEmpty()) {
            sendFcmTokenToServer()
        } else {
            subscribeToTopics()
        }

        clickEvents()

        if (SharedPreference.getGoogleFitConnection()) {
            getGoogleFitData()
        }

        binding.llAddPolicy.setOnClickListener {
            activityTracker("Android_DASHBOARD_ADD_POLICY_BTN", context)
            val intent = Intent(context, PolicySearchActivity::class.java)
            startActivity(intent)
        }

        binding.btnEditProfile.setOnClickListener {
            activityTracker("A_DB_PC_Edit", context)
            val intent = Intent(context, ProfileActivity::class.java)
            intent.putExtra("points", NewDashboardHelper.totalpoints)
            intent.putExtra("currentLevel", NewDashboardHelper.level)
            startActivity(intent)
        }


        if (SharedPref.getSpinWheelStatus()) {
            if (!SharedPref.getWheelTimesUp()) {
                APILogs.activityTracker("SPIN_REWARD_API", context)
                Log.d("AuthToken", "SpinReward API")
                getSpinRewards(context)
            }
        }

    }


    private fun getGoogleFitData() {
        NewDashboardHelper.getFitPermission(context as Activity)
        NewDashboardHelper.getSteps()
        NewDashboardHelper.getSleep()
        NewDashboardHelper.getStand()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION)
                != PackageManager.PERMISSION_GRANTED
            ) {
                /*String[] permissions = {Manifest.permission.ACTIVITY_RECOGNITION};
                ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE_ACTIVITY_RECOGNITION);*/
            } else {
                fetchStepsData()
            }
        } else {
            fetchStepsData()
        }
        val intent1 = Intent(applicationContext, SendDataToServerReceiver::class.java)
        applicationContext.sendBroadcast(intent1)
    }

    private fun requestNotificationPermission() {
        try {
            if (Build.VERSION.SDK_INT > 32) {
                ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    NOTIFICATION_PERMISSION_ID
                )
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    private fun fetchStepsData() {
        val handler2 = Handler()
        handler2.postDelayed({
            SharedPreference.init(context)
            val googleFitConnection = SharedPreference.getGoogleFitConnection()
            //Log.v("Data", "Permission : " + googleFitConnection);
            /*if (!SharedPref.getGoogleFitBadge()) {
            assignBadge("3", "tech", "GoogleFit", "health");
        }*/if (googleFitConnection) {
            if (!SharedPreference.getAllStepDataSync()) {
                CoroutineClass().runBackGroundTask(WatchYourHealth.YEAR, context)
                //new ViewStepsCount(WatchYourHealth.YEAR).execute();
            } else {
                //new LocalStorageTask(context).getDaysSteps(context);
                getDaysSteps()
                //new LocalStorageTask(context).
                CoroutineClass().runBackGroundTask(
                    WatchYourHealth.LAST_DATA,
                    context
                )
                //new ViewStepsCount(WatchYourHealth.LAST_DATA).execute();
            }
        }
        }, 1000)
    }

    private fun decryptRSA(uuid: String) {
        val request = EncryptionRequest(arrayOf(uuid))
        val call = apiInterfaceWyh.decryptRSA(request)
        call.enqueue(object : Callback<EncryptionResponse?> {
            override fun onResponse(
                call: Call<EncryptionResponse?>,
                response: Response<EncryptionResponse?>
            ) {
                if (response.code() == 200 && response.body() != null && response.body()!!.data.size > 0) {
                    try {
                        /*SharedPref.putAesUuid(CryptoHelper.encrypt(context, response.body().getData().get(0).getDecryptedStr()));
                        SharedPref.putAesUuid(SharedPref.getAesUuid().split("\n")[0]);*/
                        SharedPref.putAesUuid(RSAEncryption.rsaEncrypt(response.body()!!.data[0].decryptedStr))
                        SharedPref.putAesUuid(
                            SharedPref.getAesUuid().split("\n".toRegex())
                                .dropLastWhile { it.isEmpty() }
                                .toTypedArray()[0])
                    } catch (e: java.lang.Exception) {
                        e.printStackTrace()
                    }
                }
            }

            override fun onFailure(call: Call<EncryptionResponse?>, t: Throwable) {}
        })
    }

    private fun getDaysSteps() {
        if (NewDashboardHelper.watchYourHealth.isStepExist) {
            try {
                val lastDate = NewDashboardHelper.watchYourHealth.lastStepsDate
                //String lastDate = "2019-11-20";
                val todayDateNew = Utilities.getTodayDateNew()
                val sdf = SimpleDateFormat("yyyy-MM-dd")
                val date = sdf.parse(lastDate)
                val date1 = sdf.parse(todayDateNew)
                val diff = date1.time - date.time
                val days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS).toInt()
                totalStepsDays = days
            } catch (e: ParseException) {
                e.printStackTrace()
            }
        } else {
            CoroutineClass().runBackGroundTask(WatchYourHealth.YEAR, context)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (NewDashboardHelper.feedBackCountDown != null) {
            NewDashboardHelper.feedBackCountDown!!.cancel()
        }
        if (binding.frameContainer.visibility != View.VISIBLE) {
            NewDashboardHelper.isShowUserPopup = true
            SharedPref.putFeedbackPopup(false)
            finish()
        } else {
            NewDashboardHelper.isShowUserPopup = false
            SharedPref.putFeedbackPopup(true)
            binding.frameContainer.visibility = View.GONE
            binding.dashboardDesign.visibility = View.VISIBLE
        }

    }

    private fun processNotificationData(context: Context) {
        if (intent.getStringExtra("notification_type") != null && intent.getStringExtra("notification_type") != "") {
            val reminderName = intent.getStringExtra("notification_type")
            Log.d("AuthToken", "NotificationType : $reminderName")
            when (reminderName!!.lowercase(Locale.getDefault())) {
                "water" -> {
                    val intent = Intent(context, TrendsActivity::class.java)
                    intent.putExtra("activityType", Constants.WATER)
                    context.startActivity(intent)
                }

                "musiclibrary", "meditation" -> {
                    val intent = Intent(context, TrendsActivity::class.java)
                    intent.putExtra("activityType", Constants.MEDITATION)
                    context.startActivity(intent)
                }

                "sleep" -> {
                    val intent = Intent(context, TrendsActivity::class.java)
                    intent.putExtra("activityType", Constants.SLEEP)
                    context.startActivity(intent)
                }

                "steps" -> {
                    val intent = Intent(context, TrendsActivity::class.java)
                    intent.putExtra("activityType", Constants.STEPS)
                    context.startActivity(intent)
                }

                "reward", "winnings" -> {
                    val intent = Intent(context, RewardsActivity::class.java)
                    context.startActivity(intent)
                }

                "syncdevice" -> {
                    val intent = Intent(context, SyncDeviceActivity::class.java)
                    context.startActivity(intent)
                }

                "hra" -> {
                    val intent: Intent = if (SharedPref.getAnalysisStatus()) {
                        Intent(context, HRAAnalysisActivity::class.java)
                    } else {
                        Intent(context, HRAQuestionsActivity::class.java)
                    }
                    context.startActivity(intent)
                }

                "refer" -> {
                    val intent = Intent(context, ContactsActivityNew::class.java)
                    intent.putExtra("comingFrom", "")
                    context.startActivity(intent)
                }

                "communityinvite" -> {
                    /*val intent = Intent(context, CommunitiesActivity::class.java)
                    context.startActivity(intent)*/
                }

                "tribemessage", "tribe message" -> {
                    /*val intent = Intent(context, CommunitiesActivity::class.java)
                    intent.putExtra("communityId", communityId)
                    intent.putExtra("cameFrom", "notification")
                    context.startActivity(intent)*/
                }

                "healthhacks" -> {
                    val intent = Intent(context, HealthHacksActivity::class.java)
                    context.startActivity(intent)
                }

                "challenge", "challenges" -> {
                    val intent = Intent(context, ChallangesActivity::class.java)
                    context.startActivity(intent)
                }

                "referalfriendreg", "referalfriendupdate" -> {
                    val intent = Intent(context, ContactsActivityNew::class.java)
                    context.startActivity(intent)
                }

                "unwind" -> {
                    val intent = Intent(context, UnwindActivity::class.java)
                    intent.putExtra("comingFrom", "dashboard")
                    context.startActivity(intent)
                }

                "ice" -> {
                    val intent = Intent(context, ICEDashboardActivity::class.java)
                    context.startActivity(intent)
                }

                "lifestyle", "Lifestyle section" -> {
                    val intent = Intent(context, LifestyleActivity::class.java)
                    context.startActivity(intent)
                }

                "quiz" -> {
                    val intent = Intent(this, QuizathonViewAllActivity::class.java).apply {
                        putExtra("type", "quiz")
                        putExtra("quiz_cat", "All")
                        putExtra("name", "Play and Learn")
                    }
                    context.startActivity(intent)
                }

                "diary" -> {
                    val intent = Intent(this, NewDiaryActivity::class.java)
                    context.startActivity(intent)
                }

                "teleconsultation", "healthassure", "connected", "getvisit", "visitor", "medpay", "pharmeasy", "mrf", "fitter", "hobby", "actofit", "amaha", "tripstacc", "advantage club", "shopsta" -> {
                    val intent = Intent(context, HappyMartCategory::class.java)
                    intent.putExtra("redirectionURL", happyMartRedirectionURL)
                    intent.putExtra("disclaimerURL", happyMartImageURL)
                    intent.putExtra("disclaimer", happyMartCategory)
                    intent.putExtra("redirectTo", reminderName.lowercase(Locale.getDefault()))
                    intent.putExtra("comingFrom", Constants.NOTIFICATION)
                    context.startActivity(intent)
                }

                "video" -> NewDashboardHelper.getAbsorbData(context, "video")
                "videos" -> NewDashboardHelper.getAbsorbData(context, "videos")
                "heartage" -> NewDashboardHelper.getHeartAnalysis(context, "notification")
                "jokes" -> {
                    SharedPref.putUnwindNotification("jokes")
                    val intent = Intent(context, UnwindActivity::class.java)
                    intent.putExtra("comingFrom", "dashboardJokes")
                    context.startActivity(intent)
                }

                "articles" -> NewDashboardHelper.getAbsorbData(context, "blogs")
                "reminders" -> {
                    val intent = Intent(context, RemindersActivity::class.java)
                    context.startActivity(intent)
                }

                "devices" -> {
                    val intent =
                        Intent(context, HappyMartDisclaimerActivity::class.java)
                    intent.putExtra("came_from", Constants.HappyMartDevice)
                    intent.putExtra("toolbarname", "Devices and Electronics")
                    context.startActivity(intent)
                }

                "calcount" -> {
                    val intent = Intent(context, TrendsActivity::class.java)
                    intent.putExtra("activityType", Constants.CALORIE)
                    context.startActivity(intent)
                }

                "affirmations" -> {
                    SharedPref.putUnwindNotification("affirmations")
                    val intent = Intent(context, UnwindActivity::class.java)
                    intent.putExtra("comingFrom", "dashboardAffirmation")
                    context.startActivity(intent)
                }

                "pharmacy" -> {
                    val intent = Intent(context, HappyMartDisclaimerActivity::class.java)
                    intent.putExtra("came_from", Constants.HappyMartPharmacy)
                    intent.putExtra("toolbarname", "Pharmacy")
                    context.startActivity(intent)
                }

                "fitness" -> {
                    val intent = Intent(context, HappyMartDisclaimerActivity::class.java)
                    intent.putExtra("came_from", Constants.HappyMartFitness)
                    intent.putExtra("toolbarname", "Fitness & Nutrition")
                    context.startActivity(intent)
                }

                "immunity" -> NewDashboardHelper.getIRAScore(context, "notification")
                "reads" -> NewDashboardHelper.getAbsorbData(context, "blogs")
                "hobby tribe" -> {
                    redirectionURL = SharedPref.getHobbyTribeUrl()
                    vendorLogo = SharedPref.getHobbyTribelogo()
                    SharedPref.putHobbyTribeUrl("")
                    SharedPref.putHobbyTribeLogo("")
                    context.startActivity(
                        Intent(context, HappyMartCategory::class.java)
                            .putExtra("comingFrom", Constants.DASHBOARD_BANNER)
                            .putExtra("selectedPartner", "Hobby Tribe")
                            .putExtra("toolbarname", "Hobby Tribe")
                            .putExtra("disclaimer", "Hobby Tribe")
                            .putExtra("redirectionURL", redirectionURL)
                            .putExtra("disclaimerURL", vendorLogo)
                    )
                }

                "webinar" -> {
                    context.startActivity(
                        Intent(context, WebActivity::class.java)
                            .putExtra("Url", CommonUtils.getBaseUrlForWebinar(context))
                            .putExtra("comingFrom", "redirection")
                            .putExtra("loginUrl", "")
                    )
                }

                "happyMart" -> {
                    context.startActivity(Intent(context, NewHappyMartActivity::class.java))
                }

                "wellbeing" -> {
                    context.startActivity(Intent(context, WellBeingActivity::class.java))
                }

                "lifeStyle" -> {
                    context.startActivity(Intent(context, LifestyleActivity::class.java))
                }

                "spinwheel" -> {
                    context.startActivity(Intent(context, SpinWheelRewardsActivity::class.java))
                }

            }
        }
    }


    override fun onResume() {
        super.onResume()
        val rootBeer = RootBeer(context)
        if (SDKConstants.environment !== "debug") {
            if (EmulatorDetector.isEmulator(applicationContext)) {
                val alertDialog = AlertDialog.Builder(this)
                alertDialog.setTitle("Emulator")
                alertDialog.setCancelable(false)
                alertDialog.setMessage("This application is not allowed to run on an emulator.")
                alertDialog.setPositiveButton("Exit") { dialog: DialogInterface, which: Int ->
                    dialog.dismiss()
                    val sharedPreferences = getSharedPreferences("mydata", MODE_PRIVATE)
                    sharedPreferences.edit().clear().apply()
                    SharedPref.clearSharedPref()
                    finishAffinity()
                }
                alertDialog.show()
            } else if (rootBeer.isRooted || rootBeer.isRootedWithBusyBoxCheck || rootBeer.checkSuExists()) {
                //if (new CheckRootedDevice(this).isRTWithoutBBCheck()) {
                // device is rooted
                val alertDialog = AlertDialog.Builder(this)
                alertDialog.setTitle("Root device")
                alertDialog.setCancelable(false)
                alertDialog.setMessage("This application is not allowed on root device.")
                alertDialog.setPositiveButton("Exit") { dialog: DialogInterface, which: Int ->
                    dialog.dismiss()
                    val sharedPreferences = getSharedPreferences("mydata", MODE_PRIVATE)
                    sharedPreferences.edit().clear().apply()
                    SharedPref.clearSharedPref()
                    finishAffinity()
                }
                alertDialog.show()
            } else if (RootCheck.isDeviceRooted()) {
                //if (new CheckRootedDevice(this).isRTWithoutBBCheck()) {
                // device is rooted
                val alertDialog = AlertDialog.Builder(context)
                alertDialog.setTitle("Root device")
                alertDialog.setCancelable(false)
                alertDialog.setMessage("This application is not allowed on root device.")
                alertDialog.setPositiveButton("Exit") { dialog: DialogInterface, which: Int ->
                    dialog.dismiss()
                    val sharedPreferences =
                        getSharedPreferences("mydata", MODE_PRIVATE)
                    sharedPreferences.edit().clear().apply()
                    SharedPref.clearSharedPref()
                    finishAffinity()
                }
                alertDialog.show()
            }
        }

        if (!SharedPref.getRedirectionKey().isEmpty()) {
            if (SharedPref.getRedirectionKey().contains("unwind")) {
                val intent = Intent(context, UnwindActivity::class.java)
                intent.putExtra("comingFrom", "dashboard")
                startActivity(intent)
            }
        }


        if (!SharedPref.getSpinWheelStatus() && NewDashboardHelper.isShowUserPopup) {
            if (rewardsModel != null && rewardsModel.reward != null && rewardsModel.reward.contains(
                    "First Login"
                )
            ) {
                NewDashboardHelper.showIsFirstLogin(rewardsModel.reward, context, this)
                NewDashboardHelper.isShowUserPopup = false
                rewardsModel = RewardsModel()
            }
        }

        if (SharedPref.getWheelTimesUp() && SharedPref.getSpinWheelStatus()) {
            APILogs.activityTracker("SPIN_DASHBOARD", context)
            getQuadrants()
        }

        if (SharedPref.getHobbyTribe()) {
            context.startActivity(
                Intent(context, HappyMartCategory::class.java)
                    .putExtra("comingFrom", Constants.DASHBOARD_BANNER)
                    .putExtra("selectedPartner", "Hobby Tribe")
                    .putExtra("toolbarname", "Hobby Tribe")
                    .putExtra("disclaimer", "Hobby Tribe")
                    .putExtra("redirectionURL", SharedPref.getHobbyTribeUrl())
                    .putExtra("disclaimerURL", SharedPref.getHobbyTribelogo())
            )

            SharedPref.putHobbyTribe(false)
            SharedPref.putHobbyTribeLogo("")
            SharedPref.putHobbyTribeUrl("")
        }

        if (SharedPref.getWebinarStatus()) {
            context.startActivity(
                Intent(context, WebActivity::class.java)
                    .putExtra("Url", CommonUtils.getBaseUrlForWebinar(context))
                    .putExtra("comingFrom", "redirection")
                    .putExtra("loginUrl", "")
            )
        }

        if (SharedPref.getIsUserNameUpdated()) {
            if (SharedPref.getFeedbackPopup()) {
                SharedPref.putFeedbackPopup(false)
            }
        }

        //GoogleFit
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACTIVITY_RECOGNITION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // no need to request permission popup bcz we have called it from onCreateView
            } else {
                fetchStepsData()
            }
        } else {
            fetchStepsData()
        }

        if (SharedPref.getUserDetailResponse().isNotEmpty()) {
            var userDetailResponse = Gson().fromJson(
                SharedPref.getUserDetailResponse(),
                GetUserDetailResponse::class.java
            )
            userDetailResponse.feedbackDetails = FeedbackResponseData()
            userDetailResponse.data.quizathonRewardList = dashquizathonRewardList
            setUserDetailUI(userDetailResponse, context, binding)
        }

        if (SharedPref.getDashboardResponse().isNotEmpty()) {
            setDashboardData(
                Gson().fromJson(
                    SharedPref.getDashboardResponse(),
                    NewDashboardResponse::class.java
                ), context, binding
            )
        }
        if (binding.frameContainer.visibility != View.VISIBLE) {
            getUserDetails(binding, context)
            getDashboardData(binding, context)
            //kgiPolicyDetails()
        }

    }

    override fun onPause() {
        super.onPause()
        Log.d("AuthToken", isUserPopupShown.toString())
        if (NewDashboardHelper.feedBackCountDown != null) {
            NewDashboardHelper.feedBackCountDown!!.cancel()
        }
    }


    fun getUserDetails(binding: ActivityNewDashboardBinding, context: Context) {
        try {
            Log.d("AuthToken", SharedPref.getAuthToken())
            isUserDetailAPIIsInProgress = true
            apiInterface.getUserDeatils(SharedPref.getAuthToken())
                .enqueue(object : Callback<GetUserDetailResponse> {
                    override fun onResponse(
                        call: Call<GetUserDetailResponse>,
                        response: Response<GetUserDetailResponse>
                    ) {
                        Log.d("AuthToken", "GetUserDetails" + Gson().toJson(response.code()))
                        if (response.body() != null && response.code() == 200 && response.isSuccessful) {
                            isUserDetailAPIIsInProgress = false
                            SharedPref.putUserDetailsResponse(Gson().toJson(response.body()))
                            SharedPref.setCorporateName(response.body()!!.data.userDetails.corporateName)
                            SharedPref.setCorporateImage(response.body()!!.data.userDetails.corporateLogo)
                            SharedPref.setCorporateId(response.body()!!.data.userDetails.corporateId)
                            authDataresp =
                                response.body()!!.data.authData;
                            clientsPolicyData =
                                response.body()!!.data.clientsPolicyData
                            if (authDataresp.mpin != null && authDataresp.mpin.isNotEmpty())
                                SharedPref.setPolicyMPin(callDecryptionMethod(authDataresp.mpin))
                            SharedPref.putHasBiometric(authDataresp.isHasBiometric)
                            if (clientsPolicyData != null && clientsPolicyData.isNotEmpty()) {
                                SharedPref.setClientId(clientsPolicyData[0].clientId)
                                SharedPref.setClientsPolicyData(Gson().toJson(clientsPolicyData))
                            }
                            if (!isDestroyed && !isFinishing) {
                                setUserDetailUI(response.body()!!, context, binding)
                            }

                        }
                        if (response.code() == 401) {
                            refreshAuthToken(binding, context)
                        } else {
                            Analytics.logEvent(
                                context,
                                "",
                                "A_101_${response.code()}_${SharedPref.getEncryptedMobileNo()}"
                            )
                        }
                    }

                    override fun onFailure(call: Call<GetUserDetailResponse>, t: Throwable) {
                        Analytics.logEvent(
                            context,
                            "",
                            "A_101_Failed_${SharedPref.getEncryptedMobileNo()}"
                        )
                        isUserDetailAPIIsInProgress = false

                    }

                })
        } catch (e: Exception) {
            Analytics.logEvent(context, "", "A_101_Exception_${SharedPref.getEncryptedMobileNo()}")
            isUserDetailAPIIsInProgress = false
            e.toString()
        }
    }


    fun setUserDetailUI(
        response: GetUserDetailResponse,
        context: Context,
        binding: ActivityNewDashboardBinding
    ) {
        if (response.data != null && response.data.userDetails != null) {
            allBannerData = response.data.banners
            userDetails = response.data.userDetails
            val referralCode: TextView =
                binding.navigationDrawer.getHeaderView(0).findViewById(R.id.referral_code_tv)
            if (response.data.userDetails != null) {
                referralCode.text = response.data.userDetails.referralCode
            }
            if (!isDestroyed && !isFinishing) {
                if (response.data.userDetails.corporateLogo != null && !response.data.userDetails.corporateLogo.equals(
                        ""
                    )
                ) {
                    Glide.with(context)
                        .load(response.data.userDetails.corporateLogo)
                        .into(binding.dashboardLogo)
                }
            }

            if (response.data.userDetails.isCorporateEmployee != null && !response.data.userDetails.isCorporateEmployee.equals(
                    ""
                )
            ) {
                isCorporateUser = response.data.userDetails.isCorporateEmployee
                binding.navigationDrawer.menu.findItem(R.id.menu_kotak_policy).isVisible =
                    isCorporateUser != 1
            }
            if (response.data.userDetails.isCorporateEmployee == 1) {
                binding.policyLayout.visibility = View.GONE
                binding.policyAbhaSeparator.visibility = View.GONE
                binding.rlLinkedPolicies.visibility = View.GONE
                binding.llPolicyNumber.visibility = View.GONE
                binding.llAddPolicy.visibility = View.GONE
            } else {
                binding.policyLayout.visibility = View.VISIBLE
                binding.policyAbhaSeparator.visibility = View.VISIBLE
                binding.rlLinkedPolicies.visibility = View.VISIBLE
                binding.llPolicyNumber.visibility = View.VISIBLE
            }

            if (response.data.userDetails.totalPoints != null) {
                SharedPref.putPointsHistory(response.data.userDetails.totalPoints.toInt());
                binding.pointsTv.text = response.data.userDetails.totalPoints
                binding.newPointsTv.text = response.data.userDetails.totalPoints
                NewDashboardHelper.totalpoints = response.data.userDetails.totalPoints.toInt()
            } else {
                binding.pointsTv.text = "0"
                binding.newPointsTv.text = "0"
            }

            if (response.data.wellbeingActivity != null) {
                setKnowYourWellBeing(response.data.wellbeingActivity, context, binding)
            }

            if (response.data.userDetails.connectionCount != null) {
                //ProfileActivity.tribeCount = response.body()!!.data.userDetails.connectionCount
                binding.connectionTv.text = response.data.userDetails.connectionCount
                binding.newConnectionTv.text = response.data.userDetails.connectionCount
            } else {
                //ProfileActivity.tribeCount = "0"
                binding.connectionTv.text = "0"
                binding.newConnectionTv.text = "0"
            }

            if (response.data.userDetails.otherCount != null) {
                binding.otherTv.text = response.data.userDetails.otherCount
                binding.newOtherTv.text = response.data.userDetails.otherCount
            } else {
                binding.otherTv.text = "0"
                binding.newOtherTv.text = "0"
            }

            if (response.data.userDetails.tribeCount != null && response.data.userDetails.tribeCount != "") {
                val count = response.data.userDetails.tribeCount.split(":")
                if (count.isNotEmpty()) {
                    ProfileActivity.tribeCount = count[1]
                } else {
                    ProfileActivity.tribeCount = "0"
                }
            } else {
                ProfileActivity.tribeCount = "0"
            }

            if (response.data.userDetails.referalCount != null && response.data.userDetails.referalCount != "") {
                val count = response.data.userDetails.referalCount.split(":")
                if (count.isNotEmpty()) {
                    ProfileActivity.referralCount = count[1]
                } else {
                    ProfileActivity.referralCount = "0"
                }
            } else {
                ProfileActivity.referralCount = "0"
            }

            if (response.data.userDetails.dareReferalCount != null && response.data.userDetails.dareReferalCount != "") {
                val count = response.data.userDetails.dareReferalCount.split(":")
                if (count.isNotEmpty()) {
                    ProfileActivity.dareReferralCount = count[1]
                } else {
                    ProfileActivity.dareReferralCount = "0"
                }
            } else {
                ProfileActivity.dareReferralCount = "0"
            }

            if (response.data.referrals != null) {
                if (response.data.referrals.showCard && response.data.referrals.referedBy == null) {
                    binding.newPointsParentLayout.visibility = View.GONE
                    binding.pointsParentLayout.visibility = View.VISIBLE
                    binding.referredByLayout.visibility = View.VISIBLE
                } else {
                    binding.referredByLayout.visibility = View.INVISIBLE
                    binding.newPointsParentLayout.visibility = View.VISIBLE
                    binding.pointsParentLayout.visibility = View.GONE
                }

            }

            if (response.data.quizathonRewardList != null && response.data.quizathonRewardList.size > 0 && showDailyDialogPerSession(
                    this@NewDashboardActivity
                )
            ) {
                dashquizathonRewardList.addAll(response.data.quizathonRewardList)
            }

            if (response.data.activitpopuplist != null && response.data.activitpopuplist.size > 0 && showDailyDialogPerSession(
                    this@NewDashboardActivity
                )
            ) {
                dashquizathonRewardList.addAll(response.data.activitpopuplist)
            }

            if (dashquizathonRewardList != null && dashquizathonRewardList.size > 0) {
                if (!claimReclaimDialog.isShowing) {
                    claimReclaimDialog = ClaimReClaimDialog(
                        context,
                        dashquizathonRewardList.get(0).transId,
                        "" + dashquizathonRewardList.get(0).reclaimBurnValue,
                        dashquizathonRewardList.get(0).rewardTitle,
                        dashquizathonRewardList.get(0).rewardTitleIcon,
                        dashquizathonRewardList.get(0).rewardDescription,
                        dashquizathonRewardList.get(0).isClaim,
                        dashquizathonRewardList.get(0), this
                    )
                    claimReclaimDialog.show()
                }
            }

            response.data.policyDetails.forEach {
                if (it.policyExpiryDate != null) {
                    it.policyExpiryDate = formatDateFromString(
                        "yyyy-MM-dd",
                        "dd/MM/yyyy",
                        it.policyExpiryDate.split("T")[0]
                    )
                }
                if (!allPolicyDetails.contains(it)) {
                    allPolicyDetails.add(it)
                }
            }

            setPolicyCardUI(binding, context)

            if (response.data.addondetails != null && response.data.addondetails.addOnBadges.isNotEmpty()) {
                NewDashboardHelper.isShowUserPopup = false
                NudgeDialogue.scracthDialoge(
                    context,
                    this@NewDashboardActivity,
                    response.data.addondetails.addOnBadges[0],
                    response.data.addondetails.addOnBadges
                )
            }

            if (response.data.addondetails != null && response.data.addondetails.addOnPoints.isNotEmpty()) {
                NewDashboardHelper.isShowUserPopup = false
                NudgeDialogue.showRewardsPopupNew(
                    response.data.addondetails.addOnPoints[0],
                    context
                )
            }

            if (response.data.addondetails != null && response.data.addondetails.addOnScratchCard.isNotEmpty()) {
                NewDashboardHelper.isShowUserPopup = false
                NudgeDialogue.showScratchCard(
                    response.data.addondetails.addOnScratchCard[0],
                    0,
                    context
                )
            }

            if (response.data.addondetails != null && response.data.addondetails.addOnStamps.isNotEmpty()) {
                NewDashboardHelper.isShowUserPopup = false
                NudgeDialogue.showStampsPopup(
                    response.data.addondetails.addOnStamps[0].activityName,
                    response.data.addondetails.addOnStamps[0].stampsValue,
                    context
                )
            }

            if (response.data.showAdminRewardsEvents.isNotEmpty()) {
                showAdminRewardsEventsData =
                    response.data.showAdminRewardsEvents
                if (rewardsModel != null && rewardsModel.reward != null && rewardsModel.reward.contains(
                        "First Login"
                    )
                ) {

                } else {
                    NewDashboardHelper.isShowUserPopup = false
                    showAdminRewardEventsPopUp()
                }
            }

            if (response.feedbackDetails != null) {
                feedbackResponseData = response.feedbackDetails
                NewDashboardHelper.feedbackResponseData = feedbackResponseData
            }

            if (response.data.quizathonRewardList != null) {

            }

            if (response.data.rewards != null && response.data.rewards.reward != null) {
                if (response.data.rewards.reward.contains("First Login")) {
                    NewDashboardHelper.showIsFirstLogin(
                        response.data.rewards.reward,
                        context,
                        this@NewDashboardActivity
                    )
                } else {
                    showRewardsPopupNew(
                        response.data.rewards.reward,
                        context,
                        this@NewDashboardActivity,
                        feedbackResponseData
                    )
                }

            }

            if (response.data.rewards != null && response.data.rewards.bonusRewards != null) {
                NewDashboardHelper.showBonusRewardsPopup(
                    response.data.rewards.reward,
                    context,
                    this@NewDashboardActivity,
                    feedbackResponseData
                )
            }

            NewDashboardHelper.popUpShowModels.clear()
            if (response.data.popRewards.isNotEmpty()) {
                NewDashboardHelper.tempPopupRewards =
                    response.data.popRewards
                if (NewDashboardHelper.tempPopupRewards.size > 0) {
                    NewDashboardHelper.popUpShowModels.add(
                        PopUpShowModel(
                            "Rewards",
                            NewDashboardHelper.tempPopupRewards[0].popupMessage
                        )
                    )
                }
            }

            if (response.data.levelPopup != null && response.data.levelPopup.isNotEmpty()) {
                NewDashboardHelper.congratsText =
                    response.data.levelPopup
            }

            if (!SharedPref.getSpinWheelStatus() && !SharedPref.getWheelTimesUp()) {
                if (!SharedPref.getIsUserNameUpdated()) {
                    if (NewDashboardHelper.isShowUserPopup) {
                        if (!isUserPopupShown) {
                            isUserPopupShown = true
                            searchFeature(context as Activity, "userdetails")
                        }
                    }
                } else {
                    if (feedbackResponseData != null && feedbackResponseData.customFeedbackRespModel != null && feedbackResponseData.customFeedbackRespModel.questionJSON != null) {
                        if (!SharedPref.getFeedbackPopup() && NewDashboardHelper.isShowUserPopup) {
                            SharedPref.putFeedbackPopup(true)
                            if (!NewDashboardHelper.isFeedbackStarted) {
                                NewDashboardHelper.showFeedBackPopup(
                                    response.feedbackDetails,
                                    context
                                )
                            }
                        }
                    } else {
                        if (feedbackResponseData.feedbackModel != null && feedbackResponseData.starConfig != null) {
                            if (!SharedPref.getFeedbackPopup() && NewDashboardHelper.isShowUserPopup) {
                                if (!NewDashboardHelper.isFeedbackStarted) {
                                    SharedPref.putFeedbackPopup(true)
                                    NewDashboardHelper.showFeedBackPopup(
                                        response.feedbackDetails,
                                        context
                                    )
                                }

                            }
                        }
                    }
                }
            }
            if ((response.data.userDetails.marshUrl == null || response.data.userDetails.marshUrl.isEmpty())
                && (response.data.userDetails.opdId == null || response.data.userDetails.opdId.toString()
                    .equals("0"))
            ) {
                binding.forYouLl.visibility = View.GONE
            } else {
                if (response.data.userDetails.marshUrl != null && response.data.userDetails.marshUrl.isNotEmpty()) {
                    binding.benefitsYouLl.visibility = View.VISIBLE
                    SharedPref.setMarshURL(response.data.userDetails.marshUrl)
                } else {
                    binding.benefitsYouLl.visibility = View.GONE
                }
                if (response.data.userDetails.opdId != null && !response.data.userDetails.opdId.toString()
                        .equals("0")
                ) {
                    binding.opdLl.visibility = View.VISIBLE
                } else {
                    binding.opdLl.visibility = View.GONE
                }
            }
        }
    }

    fun setPolicyCardUI(binding: ActivityNewDashboardBinding, context: Context) {
        Log.d("All policies", Gson().toJson(allPolicyDetails))
        Log.d("All banners", Gson().toJson(allBannerData))
        if (allPolicyDetails.size > 1 || allBannerData.size > 0) {
            binding.userDetailsRv.visibility = View.VISIBLE
            binding.singlePolicyLayout.visibility = View.GONE
            setUpBanner(allPolicyDetails, allBannerData, userDetails, context, binding)
        } else {
            binding.userDetailsRv.visibility = View.GONE
            binding.singlePolicyLayout.visibility = View.VISIBLE

            if (userDetails.gender != null) {
                binding.separatedView.visibility = View.VISIBLE
                binding.newUserGender.text =
                    userDetails.gender
            } else {
                binding.separatedView.visibility = View.GONE
                binding.newUserGender.text = ""
            }

            if (userDetails.age != null && userDetails.age.isNotEmpty()) {
                binding.newUserAge.text =
                    "${userDetails.age} Yrs"
            } else {
                binding.newUserAge.text = ""
            }

            if (userDetails.abhaNumber != null && userDetails.abhaNumber.isNotEmpty()) {
                binding.abhaId.text = userDetails.abhaNumber

            } else {
                binding.addAbhaId.visibility = View.VISIBLE
                binding.abhaId.visibility = View.GONE

            }

            if (userDetails.profileImage != null && userDetails.profileImage.isNotEmpty()) {
                Glide.with(context)
                    .load(userDetails.profileImage)
                    .into(binding.ivProfile)
            }

            binding.newUserName.text = userDetails.name
            Log.d("AllPolicies", Gson().toJson(allPolicyDetails))
            if (clientsPolicyData != null && clientsPolicyData.isNotEmpty()) {
                // binding.llPolicyNumber.visibility = View.VISIBLE
                //binding.rlLinkedPolicies.visibility = View.VISIBLE
                binding.llAddPolicy.visibility = View.GONE
                binding.userPolicyNum.text = clientsPolicyData[0].policyNo
                var formattedEndDate: String? = clientsPolicyData[0].endDate
                if (formattedEndDate != null) {
                    try {
                        formattedEndDate = formatDateFromString(
                            "yyyy-MM-dd'T'HH:mm",
                            "dd/MM/yyyy",
                            formattedEndDate
                        )
                    } catch (e: Exception) {

                    }
                    binding.userPolicyExpiryDate.text = formattedEndDate
                } else
                    binding.userPolicyExpiryDate.text = "NA"

                binding.btnLinkedPolicy.setOnClickListener {
                    val intent = Intent(context, PolicyListActivity::class.java)
                    intent.putExtra("intent", "Dashboard")
                    context.startActivity(intent)
                    activityTracker("Android_DASHBOARD_VIEW_ALL_POLICIES", context)
                }
                binding.userPolicyNum.setOnClickListener {
                    val intent = Intent(context, PolicyViewMore::class.java)
                    intent.putExtra("clientID", clientsPolicyData.get(0).clientId)
                    intent.putExtra("policyNo", clientsPolicyData.get(0).policyNo)
                    intent.putExtra("intent", "Dashboard")
                    context.startActivity(intent)
                }
            } else {
                if (isCorporateUser == 0) {
                    binding.llPolicyNumber.visibility = View.GONE
                    binding.llAddPolicy.visibility = View.VISIBLE
                    binding.userPolicyExpiryDate.text = "NA"
                    binding.rlLinkedPolicies.visibility = View.GONE
                }

            }
        }
    }

    private fun kgiPolicyDetails() {
        val kgiInterface = RetrofitHandler.kjiInterfaceJava()
        var mobileNumber = SharedPref.getKgiMobileNo()
        if (SharedPref.getKgiMobileNo().isEmpty()) {
            mobileNumber = SharedPref.getMobileNo()
        }
        val request = GetPolicyDetailsRequest(mobileNumber!!)
        val token = SharedPref.getKgiAuthToken()
        val call = kgiInterface.kgiPolicyDetails(request)
        Log.d("policy url: kgi", Gson().toJson(call.request().url()))
        Log.d("policy req: kgi", Gson().toJson(request))
        call.enqueue(object : Callback<GetPolicyDetailsResponse?> {
            override fun onResponse(
                call: Call<GetPolicyDetailsResponse?>,
                response: Response<GetPolicyDetailsResponse?>
            ) {
                Log.d("policy code: kgi", Gson().toJson(response.code()))
                Log.d("policy body: kgi", Gson().toJson(response.body()))
                if (response.code() == 200 && response.body() != null && response.body()!!.success) {
                    try {
                        if (response.body()!!.data.userPolicyDetails!!.isNotEmpty()) {
                            val kgiPoliciesList: List<GetPolicyDetailsUserPolicyDetail>? =
                                response.body()!!.data.userPolicyDetails
                            SharedPref.putKGIPolicyDetails(Gson().toJson(response.body()!!.data.userPolicyDetails))
                            SharedPref.putKGIPolicyVASType(Gson().toJson(response.body()!!.data.userPolicyDetails!![0].vaSCategory))
                            val kgiPolicyDetails = SharedPref.getKGIPolicyDetails()
                            if (kgiPolicyDetails.isNotEmpty() || iskgicomingfrom) {
                                if (!kgiPoliciesList.isNullOrEmpty()) {
                                    kgiPoliciesList.forEach {
                                        val policyNumber = it.policyNumber ?: ""
                                        val policyEndDate = it.policyEndDate ?: ""
                                        val policyDetails =
                                            PolicyDetails(policyNumber, policyEndDate)
                                        if (!allPolicyDetails.contains(policyDetails)) {
                                            allPolicyDetails.add(policyDetails)
                                        }
                                    }
                                    setPolicyCardUI(NewDashboardHelper.binding, context)
                                }
                                binding.viewPolicyBefinit.visibility = View.VISIBLE
                            } else {
                                binding.viewPolicyBefinit.visibility = View.GONE
                            }
                        } else {
                            binding.viewPolicyBefinit.visibility = View.GONE
                        }
                    } catch (e: Exception) {
                        binding.viewPolicyBefinit.visibility = View.GONE
                    }
                }
            }

            override fun onFailure(call: Call<GetPolicyDetailsResponse?>, t: Throwable) {
                Log.d("login re Exception: kgi", Gson().toJson(t.message))
            }
        })
    }

    fun setKnowYourWellBeing(
        list: ArrayList<String>,
        context: Context,
        binding: ActivityNewDashboardBinding
    ) {
        //profileDetailsAdapter = ProfileDetailsAdapter(context)
        list.add("View All")
        knowYourWellBeingAdapter = KnowYourWellBeingAdapter(context, list, this)
        binding.knowYourWellbeingRv.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.knowYourWellbeingRv.setItemViewCacheSize(10)
        //binding.userDetailsRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.knowYourWellbeingRv.adapter = knowYourWellBeingAdapter
        //binding.userDetailsRv.adapter = profileDetailsAdapter
        binding.knowYourWellbeingRv.onFlingListener = null
        val snapHelper: SnapHelper = SnapHelperOneByOne()
        snapHelper.attachToRecyclerView(binding.knowYourWellbeingRv)

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        NewDashboardHelper.watchYourHealth.getGoogleFitPermission(requestCode, resultCode)
        val parts: MutableList<MultipartBody.Part?> = ArrayList()
        Log.d(
            "AuthToken",
            "Inside dashboard result req: $requestCode result: $resultCode"
        )

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                SharedPreference.init(this)
                SharedPreference.putGoogleFitConnection(true)
                NewDashboardHelper.getSteps()
                Toast.makeText(this, "GoogleFit Connected Successfully!! ", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(this, "GoogleFit Connection Failed..", Toast.LENGTH_SHORT).show()
            }
        } else if (requestCode == 2) {
            val intent = Intent(context, PostLoginActivity::class.java)
            startActivity(intent)
        } else if (requestCode == 12) {
            try {
                if (data!!.data != null) {
                    val imageURI = data!!.data
                    saveImage(imageURI, this)
                } else {
                    val mClipData = data!!.clipData
                    for (i in 0 until mClipData!!.itemCount) {
                        val item = mClipData!!.getItemAt(i)
                        val imageURI = item.uri
                        saveImage(imageURI, this)
                    }
                }
            } catch (e: Exception) {
            }
        }
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            val image2 = ImagePicker.getImages(data)
            for (i in image2.indices) {
                val fileData = FileData()
                fileData.path = getFilePathFromImage(this, image2[i])
                val singleFileSize = getFileSizeFromPath(fileData.path)
                Log.d("comparedata singleFileSize", singleFileSize.toString() + "")
                val fileDataNew = FileData()
                Log.d("comparedata singleFileSize", singleFileSize.toString() + "")
                val file1 =
                    CommonUtils.compressImageToJPEG(context, image2[i].uri)
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
            }
            if (fileDataList.size > 0) {
                if (uploadFileTopUp != null) {
                    parts.add(prepareFilePart("ActivityUploads", fileDataList[0].path))
                    uploadFileWithContentTopUP(parts, uploadFileTopUp!!)
                } else {
                    if (eventType.equals("Rewards", ignoreCase = true)) {
                        if (rewardsData!!.eventType.equals("upload", ignoreCase = true)) {
                            parts.add(prepareFilePart("ActivityUploads", fileDataList[0].path))
                        } else {
                            parts.add(prepareFilePart("JournalUpload", fileDataList[0].path))
                        }
                    } else {
                        if (topUpData!!.eventType.equals("JournalUpload", ignoreCase = true)) {
                            parts.add(prepareFilePart("JournalUpload", fileDataList[0].path))
                        } else if (topUpData!!.eventType.equals("upload", ignoreCase = true)) {
                            parts.add(prepareFilePart("ActivityUploads", fileDataList[0].path))
                        }
                    }
                    if (eventType == "Rewards") {
                        completeRewardsActivity(context as Activity, parts, rewardsData!!)
                    } else {
                        completeTopUpActivity(context as Activity, parts, topUpData!!)
                    }
                }
            }
        }
    }


    private fun uploadFileWithContentTopUP(parts: MutableList<MultipartBody.Part?>, topUp: TopUp) {
        CommonUtils.showProgressDialige(this)
        val client = OkHttpClient().newBuilder()
            .readTimeout(120, TimeUnit.SECONDS)
            .connectTimeout(120, TimeUnit.SECONDS)
            .writeTimeout(120, TimeUnit.SECONDS)
            .certificatePinner(ApiClientWyh.getCertificatePinner())
            .build()
        val body: RequestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addPart(parts[0]) //                .addFormDataPart(fileUploadKey, filename,
            //                        RequestBody.create(MediaType.parse("application/octet-stream"), new File(fileDataList.get(0).getPath())))
            .addFormDataPart("eventId", topUp.topUpID.toString())
            .addFormDataPart("eventType", topUp.eventType)
            .addFormDataPart("eventName", topUp.topUpTag)
            .addFormDataPart("yesNo", yesNoAnswer)
            .addFormDataPart("journalContent", journalContent)
            .addFormDataPart("eventCategory", Constants.TAG_ADD_ON)
            .build()
        val request = Request.Builder()
            .url(CommonUtils.getBaseUrlForAPI(context) + "Rewards/EarnRewards")
            .method("POST", body)
            .addHeader("Authorization", SharedPref.getAuthToken())
            .build()
        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                CommonUtils.dismissDialoge()
                //levelActivityJournalUploadAlertDialog.dismiss()
                deleteImage(imageName)
                fileDataList.clear()
                Analytics.logEvent(
                    context,
                    context.javaClass.name,
                    context.getString(R.string.upload_top_up_activity_failed)
                )
                runOnUiThread {
                    /* Toast.makeText(
                        context,
                        context.resources.getString(R.string.error_string),
                        Toast.LENGTH_SHORT
                    ).show()*/
                }
            }

            @Throws(IOException::class)
            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                CommonUtils.dismissDialoge()
                //if (levelActivityJournalUploadAlertDialog != null && levelActivityJournalUploadAlertDialog.isShowing()) levelActivityJournalUploadAlertDialog.dismiss()
                deleteImage(imageName)
                fileDataList.clear()
                if (response.code() == 200 && response.body() != null) {
                    Analytics.logEvent(
                        context,
                        context.javaClass.name,
                        context.getString(R.string.upload_top_up_activity_success)
                    )
                    val commonSuccessResponse = Gson().fromJson(
                        response.body()!!.string(),
                        CommonSuccessResponse::class.java
                    )
                    if (commonSuccessResponse.isSuccess) {
                        uploadFileTopUp = null
                        runOnUiThread {
                            //levelActivityJournalUploadAlertDialog.dismiss()
                            if (commonSuccessResponse.rewards != null && commonSuccessResponse.rewards.reward != null) {
                                Toast.makeText(
                                    context,
                                    context.resources.getString(R.string.points_earned_msg),
                                    Toast.LENGTH_SHORT
                                ).show()
                                NewDashboardHelper.popUpShowModels.clear()
                                NewDashboardHelper.popUpShowModels.add(
                                    PopUpShowModel(
                                        Constants.Rewards,
                                        commonSuccessResponse.rewards.reward
                                    )
                                )
                            }
                            if (commonSuccessResponse.rewards != null && commonSuccessResponse.rewards.bonusRewards != null) {
                                Toast.makeText(
                                    context,
                                    context.resources.getString(R.string.points_earned_msg),
                                    Toast.LENGTH_SHORT
                                ).show()
                                NewDashboardHelper.popUpShowModels.clear()
                                NewDashboardHelper.popUpShowModels.add(
                                    PopUpShowModel(
                                        Constants.RewardsBounce,
                                        commonSuccessResponse.rewards.bonusRewards
                                    )
                                )
                            }
                            showRewardsPopupDialogBox(
                                context,
                                this@NewDashboardActivity,
                                feedbackResponseData
                            )
                        }
                    }
                } else {
                    Analytics.logEvent(
                        context,
                        context.javaClass.name,
                        context.getString(R.string.upload_top_up_activity_failed)
                    )
                }
            }
        })
    }

    fun getAnalysis() {
        val request = GetAnalysisRequest(getString(R.string.hra_i_id), "")
        val call = apiInterfaceWyh.getAnalysis(SharedPref.getAuthToken(), request)
        if (progressDialog != null && !progressDialog.isShowing) {
            progressDialog.show()
        }
        call.enqueue(object : Callback<GetAnalysisResponse?> {
            override fun onResponse(
                call: Call<GetAnalysisResponse?>,
                response: Response<GetAnalysisResponse?>
            ) {
                if (progressDialog != null && progressDialog.isShowing) progressDialog.dismiss()
                if (response.body() != null && response.code() == 200) {
                    Analytics.logEvent(
                        context,
                        context.javaClass.name,
                        getString(R.string.hra_get_analysis_success)
                    )
                    Log.d("Analysis", Gson().toJson(response.body()))
                    SharedPref.putHRAAnalysis(Gson().toJson(response.body()))
                    getAnalysisResponse = Gson().fromJson<GetAnalysisResponse>(
                        SharedPref.getHRAAnalysis(),
                        GetAnalysisResponse::class.java
                    )
                    val isAnalysis =
                        getAnalysisResponse != null && getAnalysisResponse!!.analysisData != null && getAnalysisResponse!!.analysisData.score > 0
                    SharedPref.putAnalysisStatus(isAnalysis)
                    if (isAnalysis) {
                        if (SharedPref.getHRAAnalysis().isEmpty()) {
                            getAnalysis()
                        } else {
                            intent =
                                Intent(context, HRAAnalysisActivity::class.java)
                            startActivity(intent)
                        }
                    } else {
                        intent = Intent(context, HRAQuestionsActivity::class.java)
                        startActivity(intent)
                    }
                } else {
                    Analytics.logEvent(
                        context,
                        context.javaClass.name,
                        getString(R.string.hra_get_analysis_failed)
                    )
                }
            }

            override fun onFailure(call: Call<GetAnalysisResponse?>, t: Throwable) {
                if (progressDialog != null && progressDialog.isShowing) progressDialog.dismiss()
                Analytics.logEvent(
                    context,
                    context.javaClass.name,
                    getString(R.string.hra_get_analysis_failed)
                )
            }
        })
    }

    fun sideDrawer() {
        binding.sideDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        binding.navigationDrawer.bringToFront()

        //Changed RL BG
        Glide.with(context)
            .asBitmap()
            .load(CommonUtils.getBaseUrlForAPI(context) + SDKConstants.endPointForImages + "ic_hamburger_bg.png")
            .into(object : CustomTarget<Bitmap?>() {
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap?>?
                ) {
                    val drawable: Drawable = BitmapDrawable(getResources(), resource)
                    binding.navigationDrawer.background = drawable
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                }
            })
        tvName = binding.navigationDrawer.getHeaderView(0).findViewById(R.id.tvName)
        levelLayout = binding.navigationDrawer.getHeaderView(0).findViewById(R.id.llLevel)
        referralCopyImg =
            binding.navigationDrawer.getHeaderView(0).findViewById(R.id.referral_copy_img)
        referralShareImg =
            binding.navigationDrawer.getHeaderView(0).findViewById(R.id.referral_share_img)
        val tvLevel: TextView =
            binding.navigationDrawer.getHeaderView(0).findViewById(R.id.tv_level)
        tvName.text = "Hi ${SharedPref.getUserName()}"
        tvLevel.text = "Level 2"


        levelLayout.setOnClickListener {
            binding.sideDrawer.closeDrawer(Gravity.RIGHT)
            val intent = Intent(context, RewardsActivity::class.java)
            startActivity(intent)
        }

        referralCopyImg.setOnClickListener {

        }

        referralShareImg.setOnClickListener {
            binding.sideDrawer.closeDrawer(Gravity.RIGHT)
            val intent = Intent(context, ContactsActivityNew::class.java)
            intent.putExtra("comingFrom", "share")
            startActivity(intent)
        }

        binding.navigationDrawer.setNavigationItemSelectedListener {
            if (it.itemId === R.id.menu_contacts) {
                binding.sideDrawer.closeDrawer(Gravity.RIGHT)
                val intent = Intent(context, ContactsActivityNew::class.java)
                intent.putExtra("comingFrom", "")
                startActivity(intent)
            } else if (it.itemId === R.id.menu_sync_device) {
                binding.sideDrawer.closeDrawer(Gravity.RIGHT)
                val intent = Intent(context, SyncDeviceActivity::class.java)
                startActivity(intent)
            } else if (it.itemId === R.id.menu_corporate_account) {
                binding.sideDrawer.closeDrawer(Gravity.RIGHT)
                if (SharedPref.getCorporateName().equals("")) {
                    val intent = Intent(context, ActivityCorporateAccountAddEdit::class.java)
                    startActivity(intent)
                } else {
                    val intent = Intent(context, ActivityCorporateAccountMain::class.java)
                    startActivity(intent)
                }

            } else if (it.itemId === R.id.menu_eye_checkup) {
                binding.sideDrawer.closeDrawer(Gravity.RIGHT)
                ShowEyeCheckPopu()

            } else if (it.itemId === R.id.menuReminders) {
                binding.sideDrawer.closeDrawer(Gravity.RIGHT)
                val intent = Intent(context, RemindersActivity::class.java)
                intent.putExtra("from", "Dashboard")
                startActivity(intent)
            } else if (it.itemId === R.id.menu_kotak_policy) {
                binding.sideDrawer.closeDrawer(Gravity.RIGHT)
                if (SharedPref.getClientsPolicyData().isNotEmpty() && SharedPref.getClientId()
                        .isNotEmpty()
                ) {
                    /* if (SharedPref.getPolicyMPin().isNotEmpty()) {
                         val intent = Intent(context, PolicyLoginMPIN::class.java)
                         startActivity(intent)
                     } else {
                         val intent = Intent(context, PolicyCreateMPIN::class.java)
                         startActivity(intent)
                     }*/
                    val intent = Intent(context, PolicyListActivity::class.java)
                    activityTracker("Android_POLICY_MENU_CLICK", context)
                    intent.putExtra("intent", "Dashboard")
                    startActivity(intent)
                } else {
                    activityTracker("Android_DASHBOARD_ADD_POLICY_BTN", context)
                    val intent = Intent(context, PolicySearchActivity::class.java)
                    startActivity(intent)
                }
            } else if (it.itemId === R.id.menu_profile) {
                activityTracker("A_DB_HM_Profile_Connections", context)
                binding.sideDrawer.closeDrawer(Gravity.RIGHT)
                val intent = Intent(context, ProfileActivity::class.java)
                intent.putExtra("points", NewDashboardHelper.totalpoints)
                intent.putExtra("currentLevel", NewDashboardHelper.level)
                startActivity(intent)
            } else if (it.itemId === R.id.menu_addUserDetails) {
                activityTracker("A_DB_HM_UD", context)
                binding.sideDrawer.closeDrawer(Gravity.RIGHT)
                val intent = Intent(context, AddUserDetails::class.java)
                startActivity(intent)
            } else if (it.itemId === R.id.notification) {
                binding.sideDrawer.closeDrawer(Gravity.RIGHT)
                val intent =
                    Intent(context, NotificationDrawerActivity::class.java)
                startActivity(intent)
            } else if (it.itemId === R.id.scanqr) {
                binding.sideDrawer.closeDrawer(Gravity.RIGHT)
                val intent = Intent(context, ScanQRActivity::class.java)
                startActivity(intent)
            } else if (it.getItemId() === R.id.aboutUs) {
                binding.sideDrawer.closeDrawer(Gravity.RIGHT)
                val intent = Intent(context, AboutUsActivity::class.java)
                startActivity(intent)
            } else if (it.itemId === R.id.menuSupport) {
                binding.sideDrawer.closeDrawer(Gravity.RIGHT)
                val intent = Intent(context, NeedSupportActivity::class.java)
                startActivity(intent)
            } else if (it.itemId === R.id.menu_addFamily) {
                binding.sideDrawer.closeDrawer(Gravity.RIGHT)
                val intent = Intent(context, AddFamilyListActivity::class.java)
                startActivity(intent)
            } else if (it.getItemId() === R.id.menu_add_referral_code) {
                binding.sideDrawer.closeDrawer(Gravity.RIGHT)
                addReferralCode("referralCode", context, binding)
            }

            true
        }

        for (i in 0..12) {
            binding.navigationDrawer.menu.getItem(i).setActionView(R.layout.menu_layout_next_image)

        }


    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == NewDashboardHelper.REQUEST_CODE_ACTIVITY_RECOGNITION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                NewDashboardHelper.watchYourHealth.connectAPIClient()
            } else {
                Toast.makeText(
                    context,
                    "Physical activity permission required to use step counts.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        if (requestCode == PERMISSION_ID) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                getLastLocation()
            } else {
                // getLastLocation();
            }
        }

        if (requestCode == CAMERA_PERMISSION_CODE) {

            // Checking whether user granted the permission or not.
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                checkImagePicker()
                // Showing the toast message
                Toast.makeText(
                    context,
                    "Camera Permission Granted",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    context,
                    "Camera Permission Denied",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        if (requestCode == NewDashboardHelper.CALL_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("AuthToken", "Permission granted")
            } else {
                Toast.makeText(context, "Permission denied", Toast.LENGTH_LONG).show()
            }
        }
    }


    fun setUpBanner(
        policyDetails: ArrayList<PolicyDetails>,
        banner: ArrayList<GetBannerResponse.Data>,
        userDetail: UserDetail,
        context: Context,
        binding: ActivityNewDashboardBinding
    ) {
        try {
            var positionReads = 0
            val dashBoardBannerAdaper =
                DashboardBannerAdapter(
                    context,
                    policyDetails,
                    clientsPolicyData,
                    banner,
                    userDetail
                )
            val linearLayoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            binding.userDetailsRv.layoutManager = linearLayoutManager
            binding.userDetailsRv.adapter = dashBoardBannerAdaper
            binding.userDetailsRv.onFlingListener = null
            binding.userDetailsRv.setItemViewCacheSize(30)
            val snapHelper: SnapHelper = SnapHelperOneByOne()
            snapHelper.attachToRecyclerView(binding.userDetailsRv)

            var indicatorCount = policyDetails.size + banner.size

            if (policyDetails.size == 0) indicatorCount++

            val indicatorsLayoutManager = LinearLayoutManager(context)
            indicatorsLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
            val indicatorsAdapter = IndicatorsAdapter(context, indicatorCount, 0)
            binding.bannerIndicator.layoutManager = indicatorsLayoutManager
            binding.bannerIndicator.hasFixedSize()
            binding.bannerIndicator.adapter = indicatorsAdapter

            binding.userDetailsRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        if (linearLayoutManager.findFirstCompletelyVisibleItemPosition() != -1) {
                            positionReads =
                                linearLayoutManager.findFirstCompletelyVisibleItemPosition()
                        } else positionReads = linearLayoutManager.findFirstVisibleItemPosition()
                        Log.d("AuthToken", "$positionReads")
                        indicatorsAdapter.updateSelectedIndex(positionReads)
                    }
                }
            })

        } catch (e: Exception) {
            e.toString()
        }
    }


    fun clickEvents() {

        binding.dashboardMenu.llActivities.setOnClickListener {
            NewDashboardHelper.hideEmptyWheel(binding)
            NewDashboardHelper.hideServicesWheel(binding)
            NewDashboardHelper.hideMyZoneWheel(binding)
            NewDashboardHelper.showActivitiesWheel(binding)
        }

        binding.dashboardMenu.llServices.setOnClickListener {
            NewDashboardHelper.hideEmptyWheel(binding)
            NewDashboardHelper.hideMyZoneWheel(binding)
            NewDashboardHelper.hideActivitiesWheel(binding)
            NewDashboardHelper.showServicesWheel(binding)
        }

        binding.dashboardMenu.llMyZone.setOnClickListener {
            NewDashboardHelper.hideEmptyWheel(binding)
            NewDashboardHelper.hideActivitiesWheel(binding)
            NewDashboardHelper.hideServicesWheel(binding)
            NewDashboardHelper.showMyZoneWheel(binding)

        }

        binding.ivBlackOpacity.setOnClickListener {
            if (binding.dashboardMenu.rlMain.visibility == View.VISIBLE) {
                binding.dashboardMenu.ivHomeMenuClicked.performClick()
            }
        }

        binding.dashboardMenu.ivHomeMenuClicked.setOnClickListener {
            binding.dashboardMenu.rlMain.visibility = View.GONE
            binding.ivBlackOpacity.visibility = View.GONE
            NewDashboardHelper.hideActivitiesWheel(binding)
            NewDashboardHelper.showEmptyWheel(binding)
            NewDashboardHelper.hideServicesWheel(binding)
            NewDashboardHelper.hideMyZoneWheel(binding)
        }

        binding.sideMenu.setOnClickListener {
            activityTracker("A_DB_Hamburger Menu", context)
            binding.sideDrawer.openDrawer(binding.navigationDrawer, true)
        }

        binding.teleConsultationLayout.setOnClickListener {
            activityTracker("A_DB_Con_TC", context)
            val intent = Intent(context, HappyMartCategory::class.java)
            intent.putExtra("id", "1")
            intent.putExtra("category", "Tele-Consultation")
            intent.putExtra("comingFrom", "dashboard")
            startActivity(intent)

        }

        binding.physicalConsultationLayout.setOnClickListener {
            activityTracker("A_DB_Con_PC", context)
            val intent = Intent(context, HappyMartCategory::class.java)
            intent.putExtra("id", "4")
            intent.putExtra("category", "Physical Consultation")
            intent.putExtra("comingFrom", "dashboard")
            startActivity(intent)
        }

        binding.dashboardMenu.tvLifestyle.setOnClickListener {
            val intent = Intent(context, LifestyleActivity::class.java)
            startActivity(intent)
            binding.dashboardMenu.ivHomeMenuClicked.performClick()
        }

        binding.dashboardMenu.tvZenZone.setOnClickListener {
            val intent = Intent(context, TrendsActivity::class.java)
            intent.putExtra("activityType", Constants.MEDITATION)
            startActivity(intent)
            binding.dashboardMenu.ivHomeMenuClicked.performClick()
        }

        binding.dashboardMenu.tvKnowYourWellBeing.setOnClickListener {
            val intent = Intent(context, WellBeingActivity::class.java)
            startActivity(intent)
            binding.dashboardMenu.ivHomeMenuClicked.performClick()
        }

        binding.dashboardMenu.tvHealthHacks.setOnClickListener {
            val i = Intent(context, HealthHacksActivity::class.java)
            startActivity(i)
            binding.dashboardMenu.ivHomeMenuClicked.performClick()
        }

        binding.dashboardMenu.tvHealthLocker.setOnClickListener {
            val intent = Intent(context, EhrActivity::class.java)
            startActivity(intent)
            binding.dashboardMenu.ivHomeMenuClicked.performClick()
        }

        binding.dashboardMenu.tvHappyMart.setOnClickListener {
            val intent = Intent(context, NewHappyMartActivity::class.java)
            startActivity(intent)
            binding.dashboardMenu.ivHomeMenuClicked.performClick()
        }

        binding.dashboardMenu.tvDigiCoach.setOnClickListener {
            Toast.makeText(context, "Coming Soon", Toast.LENGTH_SHORT).show()
            binding.dashboardMenu.ivHomeMenuClicked.performClick()
        }

        binding.dashboardMenu.tvChallanges.setOnClickListener {
            startActivity(Intent(context, ChallangesActivity::class.java))
            binding.dashboardMenu.ivHomeMenuClicked.performClick()
        }

        binding.dashboardMenu.tvMyTribe.setOnClickListener {
            /*val intent = Intent(context, CommunitiesActivity::class.java)
            startActivity(intent)
            binding.dashboardMenu.ivHomeMenuClicked.performClick()*/
        }

        binding.dashboardMenu.tvGoals.setOnClickListener {
            val intent = Intent(context, GoalsDashboard::class.java)
            startActivity(intent)
            binding.dashboardMenu.ivHomeMenuClicked.performClick()
        }

        binding.dashboardMenu.tvMyWinnings.setOnClickListener {
            val intent = Intent(context, RewardsActivity::class.java)
            startActivity(intent)
            binding.dashboardMenu.ivHomeMenuClicked.performClick()
        }

        binding.dashboardMenu.tvUnwind.setOnClickListener {
            val i = Intent(context, UnwindActivity::class.java)
            i.putExtra("comingFrom", "dashboard")
            startActivity(i)
            binding.dashboardMenu.ivHomeMenuClicked.performClick()
        }

        binding.diagnosticsLayout.setOnClickListener {
            activityTracker("A_DB_FAP_D&LT", context)
            val intent = Intent(context, HappyMartCategory::class.java)
            intent.putExtra("id", "2")
            intent.putExtra("category", "Diagnostics and LAB Tests")
            intent.putExtra("comingFrom", "dashboard")
            startActivity(intent)
        }

        binding.pharmacyLayout.setOnClickListener {
            activityTracker("A_DB_FAP_Pharmacy", context)
            val intent = Intent(context, HappyMartCategory::class.java)
            intent.putExtra("id", "3")
            intent.putExtra("category", "Pharmacy")
            intent.putExtra("comingFrom", "dashboard")
            startActivity(intent)
        }

        binding.ambulanceLayout.setOnClickListener {
            activityTracker("A_DB_FAP_Ambulance", context)
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:9111891118")
            startActivity(intent)
        }

        binding.mentalWellnessLayout.setOnClickListener {
            activityTracker("A_DB_FAP_MW", context)
            val intent = Intent(context, HappyMartCategory::class.java)
            intent.putExtra("id", "6")
            intent.putExtra("category", "Mental Wellbeing")
            intent.putExtra("comingFrom", "dashboard")
            startActivity(intent)
        }

        binding.fitnessNutritionistLayout.setOnClickListener {
            activityTracker("A_DB_FAP_F&N", context)
            val intent = Intent(context, HappyMartCategory::class.java)
            intent.putExtra("id", "12")
            intent.putExtra("category", "Devices & Merchandise")
            intent.putExtra("comingFrom", "dashboard")
            startActivity(intent)
        }

        binding.viewHappyMartLayout.setOnClickListener {
            activityTracker("A_DB_FAP_VHM", context)
            val intent = Intent(context, NewHappyMartActivity::class.java)
            startActivity(intent)
            binding.dashboardMenu.ivHomeMenuClicked.performClick()
        }

        binding.searchImg.setOnClickListener {
            searchFeature(context as Activity, "search")
        }

        binding.sosBtn.setOnClickListener {
            activityTracker("A_DB_SOS", context)
            startActivity(Intent(context, SOSActivity::class.java))

        }

        binding.firstNudgeLayout.setOnClickListener {
            activityTracker("A_DB_NudgeButton1", context)
            nudgeRedirection(firstRedirection, "1")
        }

        binding.secondNudgeLayout.setOnClickListener {
            activityTracker("A_DB_NudgeButton2", context)
            nudgeRedirection(secondRedirection, "2")
        }

        binding.thirdNudgeLayout.setOnClickListener {
            activityTracker("A_DB_NudgeButton3", context)
            nudgeRedirection(thirdRedirection, "3")
        }

        binding.fourthNudgeRedirection.setOnClickListener {
            activityTracker("A_DB_NudgeButton4", context)
            nudgeRedirection(fourthRedirection, "4")
        }

        binding.referralMobileNum.setOnClickListener {
            APILogs.activityTracker("A_DB_RB_Enter", context)
            addReferralCode("Contest", context, binding)
        }

        binding.goalsLayout.setOnClickListener {
            activityTracker("A_DB_MW_DG", context)
            startActivity(Intent(context, GoalsDashboard::class.java))
        }
        binding.viewPolicyBefinit.setOnClickListener {
            /*val intent = Intent(this@NewDashboardActivity, KotakLiveWellActivity::class.java)
            startActivity(intent)*/
        }

        binding.viewAllTracker.setOnClickListener {
            activityTracker("A_DB_MW_View All Trackers", context)
            startActivity(Intent(context, LifestyleActivity::class.java))
        }

        binding.viewAllHealthHacks.setOnClickListener {
            activityTracker("A_DB_HH_View All H", context)
            startActivity(Intent(context, HealthHacksActivity::class.java))
        }

        binding.seeAllActivities.setOnClickListener {
            activityTracker("A_DB_MW_MP_SAA", context)
            startActivity(
                Intent(context, RewardsActivity::class.java)
                    .putExtra("comingFrom", "activities")
            )
        }

        binding.dashboardSteps.setOnClickListener {
            activityTracker("A_DB_MW_Steps", context)
            startActivity(
                Intent(context, TrendsActivity::class.java).putExtra(
                    "activityType",
                    Constants.STEPS
                )
            )
        }

        binding.dashboardZenzone.setOnClickListener {
            activityTracker("A_DB_MW_ZenZone_Add", context)
            startActivity(
                Intent(context, TrendsActivity::class.java).putExtra(
                    "activityType",
                    Constants.MEDITATION
                )
            )
        }

        binding.ivDashboardIce.setOnClickListener {
            activityTracker("A_DB_Ice", context)
            NewDashboardHelper.isShowUserPopup = false
            binding.dashboardDesign.visibility = View.GONE
            binding.frameContainer.visibility = View.VISIBLE
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.frame_container, ICEFragment())
            transaction.addToBackStack(null)
            transaction.commit()

        }

        binding.ivDashHappyMart.setOnClickListener {
            activityTracker("A_DB_Happy Mart", context)
            NewDashboardHelper.isShowUserPopup = false
            binding.dashboardDesign.visibility = View.GONE
            binding.frameContainer.visibility = View.VISIBLE
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.frame_container, HappyMartFragment())
            transaction.addToBackStack(null)
            transaction.commit()

        }

        binding.ivDashWinnings.setOnClickListener {
            activityTracker("A_DB_Winnings", context)
            NewDashboardHelper.isShowUserPopup = false
            startActivity(
                Intent(this, RewardsActivity::class.java)
                    .putExtra("comingFrom", "NewDashboard")
            )
            finish()

        }

        binding.ivDashboardHome.setOnClickListener {
            activityTracker("A_DB_Home icon", context)
            binding.dashboardDesign.visibility = View.VISIBLE
            binding.frameContainer.visibility = View.GONE
            Log.d("AuthToken", "UserDetail Called 2")
            getUserDetails(binding, context)
            getDashboardData(binding, context)

            //kgiPolicyDetails()
        }

        binding.ivDashOthers.setOnClickListener {
            activityTracker("A_DB_Others", context)
            isUserPopupShown = false
            binding.dashboardDesign.visibility = View.GONE
            binding.frameContainer.visibility = View.VISIBLE
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.frame_container, OtherFragment())
            transaction.addToBackStack(null)
            transaction.commit()

        }

        binding.addAbhaId.setOnClickListener {
            /*APILogs.activityTracker("A_DB_PC_Add Abha_ID", context)
            startActivity(Intent(this, ABHALinkActivity::class.java))*/
        }

        binding.connectionLayout.setOnClickListener {
            activityTracker("A_DB_Connection", context)
            getProfileDetails()
        }

        binding.newConnectionLayout.setOnClickListener {
            activityTracker("A_DB_Connection", context)
            getProfileDetails()
        }


        binding.addPolicyLayout.setOnClickListener {
            activityTracker("A_DB_PC_Add Policy", context)
            val intent = Intent(context, SearchPolicyActivity::class.java)
            startActivity(intent)
        }

        binding.pointsValueLayout.setOnClickListener {
            activityTracker("A_DB_Reward Points", context)
            val intent = Intent(this, RewardsActivity::class.java)
            intent.putExtra("currentIndex", 0)
            startActivity(intent)
        }

        binding.newPointsLayout.setOnClickListener {
            activityTracker("A_DB_Reward Points", context)
            val intent = Intent(this, RewardsActivity::class.java)
            intent.putExtra("currentIndex", 0)
            startActivity(intent)
        }


        binding.otherLayout.setOnClickListener {
            activityTracker("A_DB_Other", context)
            getRewardList(this)

        }

        binding.newOtherLayout.setOnClickListener {
            activityTracker("A_DB_Other", context)
            getRewardList(this)
        }

        binding.viewAllChallenges.setOnClickListener {
            activityTracker("A_DB_Challenges_View", context)
            startActivity(Intent(context, ChallangesActivity::class.java))
        }

        binding.healthLockerImg.setOnClickListener {
            startActivity(Intent(context, EhrActivity::class.java))
        }

        binding.syncYourDeviceTv.setOnClickListener {
            startActivity(Intent(context, SyncDeviceActivity::class.java))
        }

        binding.addZenzoneLayout.setOnClickListener {
            showPopUpZenZone(context, NewDashboardHelper.currentValueZenZone.toDouble())
        }

        binding.benefitsYouLl.setOnClickListener {
            activityTracker("A_OnDashboard_ButtonMarshURL", context)
            if (SharedPref.getMarshURL().isNotEmpty()) {
                val intent = Intent(context, WebActivity::class.java)
                intent.putExtra("Url", SharedPref.getMarshURL())
                startActivity(intent)
            }
        }

        binding.opdLl.setOnClickListener {
            activityTracker("A_OnDashboard_OPD", context)
            startActivity(
                Intent(context, HappyMartCategory::class.java)
                    .putExtra("category", "OPD")
                    .putExtra("id", "14")
                    .putExtra("comingFrom", "dashboard")
            )
        }

    }

    private fun getProfileDetails() {
        if (progressDialog != null && !progressDialog.isShowing) progressDialog.show()
        val call = apiInterfaceWyh.getUserDetails(SharedPref.getAuthToken())
        call.enqueue(object : Callback<ProfileDetailsResponse?> {
            override fun onResponse(
                call: Call<ProfileDetailsResponse?>,
                response: Response<ProfileDetailsResponse?>
            ) {
                try {
                    if (progressDialog != null && progressDialog.isShowing) progressDialog.dismiss()
                    if (response.code() == 200 && response.body() != null) {
                        Analytics.logEvent(
                            context,
                            context.javaClass.name,
                            getString(R.string.get_profile_details_success)
                        )

                        if (response.body()!!.success) {
                            val profileDetailsResponse = response.body()!!
                                .data

                            if (profileDetailsResponse.connectionLists.size > 0) {
                                ProfileActivity.tribeListModel.clear()
                                ProfileActivity.referenceListModel.clear()
                                ProfileActivity.dareListModel.clear()
                                for (i in profileDetailsResponse.connectionLists.indices) {
                                    if (profileDetailsResponse.connectionLists[i].type.equals(
                                            "tribe",
                                            ignoreCase = true
                                        )
                                    ) {
                                        val model = ConnectionListModel(
                                            profileDetailsResponse.connectionLists[i].name,
                                            profileDetailsResponse.connectionLists[i].type,
                                            profileDetailsResponse.connectionLists[i].tribeId
                                        )
                                        ProfileActivity.tribeListModel.add(model)
                                    } else if ((profileDetailsResponse.connectionLists[i].type.equals(
                                            "Dare referral",
                                            ignoreCase = true
                                        ))
                                    ) {
                                        val model = ConnectionListModel(
                                            profileDetailsResponse.connectionLists[i].name,
                                            profileDetailsResponse.connectionLists[i].type,
                                            profileDetailsResponse.connectionLists[i].tribeId
                                        )
                                        ProfileActivity.dareListModel.add(model)
                                    } else {
                                        val model = ConnectionListModel(
                                            profileDetailsResponse.connectionLists[i].name,
                                            profileDetailsResponse.connectionLists[i].type,
                                            profileDetailsResponse.connectionLists[i].tribeId
                                        )
                                        ProfileActivity.referenceListModel.add(model)
                                    }
                                }
                                ProfileActivity.totalCount =
                                    ProfileActivity.tribeListModel.size.toString()
                            }

                            if (profileDetailsResponse.categoryCount != null) {
                                if (profileDetailsResponse.categoryCount.tribeCount != null &&
                                    profileDetailsResponse.categoryCount.tribeCount != ""
                                ) {
                                    val count =
                                        profileDetailsResponse.categoryCount.tribeCount.split(":".toRegex())
                                            .dropLastWhile { it.isEmpty() }
                                            .toTypedArray()
                                    if (count.size != 0) {
                                        ProfileActivity.tribeCount = count[0]
                                    }
                                }
                            }

                            if (profileDetailsResponse.categoryCount != null) {
                                if (profileDetailsResponse.categoryCount.tribeCount != null &&
                                    profileDetailsResponse.categoryCount.tribeCount != ""
                                ) {
                                    val count =
                                        profileDetailsResponse.categoryCount.tribeCount.split(":".toRegex())
                                            .dropLastWhile { it.isEmpty() }
                                            .toTypedArray()
                                    if (count.size != 0) {
                                        ProfileActivity.tribeCount = count[1]
                                    }
                                }
                            }

                            if (profileDetailsResponse.categoryCount != null) {
                                if (profileDetailsResponse.categoryCount.referalCount != null &&
                                    profileDetailsResponse.categoryCount.referalCount != ""
                                ) {
                                    val count =
                                        profileDetailsResponse.categoryCount.referalCount.split(
                                            ":".toRegex()
                                        )
                                            .dropLastWhile { it.isEmpty() }
                                            .toTypedArray()
                                    if (count.size != 0) {
                                        ProfileActivity.referralCount = count[1]
                                    }
                                }
                            }

                            if (profileDetailsResponse.categoryCount != null) {
                                if (profileDetailsResponse.categoryCount.dareReferalCount != null &&
                                    profileDetailsResponse.categoryCount.dareReferalCount != ""
                                ) {
                                    val count =
                                        profileDetailsResponse.categoryCount.dareReferalCount.split(
                                            ":".toRegex()
                                        ).dropLastWhile { it.isEmpty() }
                                            .toTypedArray()
                                    if (count.size != 0) {
                                        ProfileActivity.dareReferralCount = count[1]
                                    }
                                }
                            }

                            ProfileActivity.categoryCountText =
                                """
                            ${profileDetailsResponse.categoryCount.tribeCount}
                            ${profileDetailsResponse.categoryCount.referalCount}
                            ${profileDetailsResponse.categoryCount.dareReferalCount}
                            """.trimIndent()


                            SharedPref.putReferralCode(profileDetailsResponse.profile.referralCode)

                            val intent = Intent(context, DialogActivity::class.java)
                            intent.putExtra("categoryCountText", ProfileActivity.tribeCount)
                            startActivity(intent)
                        }
                    } else if (response.code() == 401) {
//                    refreshAuthToken();
                    } else {
                        Analytics.logEvent(
                            context,
                            context.javaClass.name,
                            getString(R.string.get_profile_details_failed)
                        )
                        /* Toast.makeText(
                            context,
                            resources.getString(R.string.error_string),
                            Toast.LENGTH_SHORT
                        ).show()*/
                    }
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<ProfileDetailsResponse?>, t: Throwable) {
                if (progressDialog != null && progressDialog.isShowing) progressDialog.dismiss()
                Analytics.logEvent(
                    context,
                    context.javaClass.name,
                    getString(R.string.get_profile_details_failed)
                )
                /* Toast.makeText(
                    context,
                    resources.getString(R.string.error_string),
                    Toast.LENGTH_SHORT
                ).show()*/
            }
        })
    }

    /*Top Up Starts*/
    private fun getActivityProgress(
        progressBar: LinearProgressIndicator,
        topUp: TopUp,
        tvSteps: TextView?
    ) {
        val request: ActivityProgressRequest = ActivityProgressRequest(topUp.topUpID, "Topup")
        val call: Call<ActivityProgressResponse> =
            apiInterfaceWyh.getActivityProgress(SharedPref.getAuthToken(), request)
        call.enqueue(object : Callback<ActivityProgressResponse?> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(
                call: Call<ActivityProgressResponse?>,
                response: Response<ActivityProgressResponse?>
            ) {
                if (response.code() == 200 && response.body() != null) {
                    if (response.body()!!.data != null) {
                        if (tvSteps != null && response.body()!!.data.totalSteps != 0) {
                            tvSteps.visibility = View.VISIBLE
                            tvSteps.text =
                                "Your steps: " + response.body()!!.data.userSteps + " / " + response.body()!!.data.totalSteps
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

            override fun onFailure(call: Call<ActivityProgressResponse?>, t: Throwable) {

            }
        })
    }

    private fun showAdminRewardEventsPopUp() {
        val topUpDataList: List<ShowAdminRewardsEventsData> = showAdminRewardsEventsData
        if (topUpDataList != null && !topUpDataList.isEmpty()) {
            val eventsData = topUpDataList[0]
            val topUp = TopUp(
                eventsData.name,
                eventsData.eventName,
                eventsData.description,
                eventsData.rewardPoints,
                eventsData.redirectTo,
                eventsData.whatToDoInActivity,
                eventsData.howToDoTheActivity,
                eventsData.whyToDoTheActivity,
                eventsData.imagePath,
                eventsData.rewardPoints,
                "Journal",
                eventsData.isStarted,
                eventsData.isCompleted,
                eventsData.imagePath
            )
            if (topUp.eventType != null) {
                if (topUp.eventType.equals("scratch & win", ignoreCase = true)) {
                    if (!topUp.isIsCompleted) {
                        startScratchAndWin(topUp)
                    } else {
                        showCustomPopUpTopUp(topUp, R.drawable.orange_circle)
                    }
                } else if (topUp.eventType.equals(
                        "JournalUpload",
                        ignoreCase = true
                    )
                ) showCustomPopUpJournalUploadTopUp(
                    topUp,
                    R.drawable.orange_circle
                ) else if (topUp.eventType.equals(
                        "Journal",
                        ignoreCase = true
                    )
                ) showCustomPopUpJournalUploadTopUp(
                    topUp,
                    R.drawable.orange_circle
                ) else if (topUp.eventType.equals(
                        "Feedback",
                        ignoreCase = true
                    )
                ) showCustomPopUpFeedback(
                    topUp,
                    R.drawable.orange_circle
                ) else showCustomPopUpTopUp(topUp, R.drawable.orange_circle)
                showAdminRewardsEventsData.clear()
            }
        }
    }

    private fun startScratchAndWin(topUp: TopUp) {
        if (progressDialog != null && !progressDialog.isShowing()) progressDialog.show()
        val request = StartRewardsActivityRequest(topUp.topUpID, Constants.TAG_TOP_UP_EVENT)
        val call: Call<ScratchAndWinResponse> =
            apiInterfaceWyh.startScratchAndWin(SharedPref.getAuthToken(), request)
        call.enqueue(object : Callback<ScratchAndWinResponse?> {
            override fun onResponse(
                call: Call<ScratchAndWinResponse?>,
                response: Response<ScratchAndWinResponse?>
            ) {
                if (progressDialog != null && progressDialog.isShowing) progressDialog.dismiss()
                if (response.body() != null && response.code() == 200) {
                    Analytics.logEvent(
                        context,
                        context.javaClass.name,
                        getString(R.string.rewards_start_activity_success)
                    )
                    if (response.body()!!.isSuccess) {
                        if (alertDialog != null) alertDialog.dismiss()
                        showScratchAndWinPopup(response.body()!!.data.toString(), context)
                        Toast.makeText(context, "Top up has been started", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        Toast.makeText(context, response.body()!!.msg, Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Analytics.logEvent(
                        context,
                        context.javaClass.name,
                        getString(R.string.rewards_start_activity_failed)
                    )
                    //Toast.makeText(context, resources.getString(R.string.error_string), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ScratchAndWinResponse?>, t: Throwable) {
                if (progressDialog != null && progressDialog.isShowing) progressDialog.dismiss()
                Analytics.logEvent(
                    context,
                    context.javaClass.name,
                    getString(R.string.rewards_start_activity_failed)
                )
                Toast.makeText(
                    context,
                    resources.getString(R.string.internet_connection),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun showCustomPopUpTopUp(topUp: TopUp, bgDrawable: Int) {
        val alertBuilder = AlertDialog.Builder(context, R.style.CustomAlertDialog)
        customPopupRewardsBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.custom_popup_rewards,
            null,
            false
        )
        alertBuilder.setView(customPopupRewardsBinding.root)
        alertDialog = alertBuilder.create()
        alertDialog.setCancelable(false)
        if (!alertDialog.isShowing()) alertDialog.show()
        Glide.with(context)
            .load(topUp.topupIcon)
            .error(R.drawable.dummy_image)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .into(customPopupRewardsBinding.ivLogo)
        customPopupRewardsBinding.rlLogo.background = ContextCompat.getDrawable(
            context,
            bgDrawable
        )
        customPopupRewardsBinding.tvTitle.text = topUp.topUpName
        customPopupRewardsBinding.tvDescription.text = topUp.topUpDesc
        if (topUp.isIsStarted || topUp.isIsCompleted) {
            customPopupRewardsBinding.btnPositive.visibility = View.GONE
        }
        if (topUp.whatTo != null) {
            customPopupRewardsBinding.llWhatHowAndWhy.visibility = View.VISIBLE
            customPopupRewardsBinding.tvWhatToDo.text = topUp.whatTo
            customPopupRewardsBinding.tvHowToDo.text = topUp.howTo
            customPopupRewardsBinding.tvWhyToDo.text = topUp.whyTo
        }
        if (topUp.topUpName.lowercase(Locale.getDefault()) == "scratch & win") {
            customPopupRewardsBinding.progressBar.visibility = View.GONE
        } else {
            if (topUp.isStarted && !topUp.isIsCompleted) {
                customPopupRewardsBinding.progressBar.visibility = View.VISIBLE
                getActivityProgress(
                    customPopupRewardsBinding.progressBar,
                    topUp,
                    customPopupRewardsBinding.tvSteps
                )
            } else if (topUp.isIsCompleted) {
                customPopupRewardsBinding.progressBar.setVisibility(View.VISIBLE)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    customPopupRewardsBinding.progressBar.setProgress(100, true)
                } else customPopupRewardsBinding.progressBar.progress = 100
            } else {
                customPopupRewardsBinding.progressBar.visibility = View.GONE
            }
        }
        if (topUp.isIsCompleted) {
            customPopupRewardsBinding.btnPositive.visibility = View.GONE
        }
        checkTopUpCondition(topUp)
        val displayRectangle = Rect()
        val window = window
        window.decorView.getWindowVisibleDisplayFrame(displayRectangle)
        alertDialog.getWindow()!!.setLayout(
            (displayRectangle.width() *
                    0.8f).toInt(), (displayRectangle.height() * 0.6f).toInt()
        )
    }

    private fun showCustomPopUpFeedback(topUp: TopUp, bgDrawable: Int) {
        val alertBuilder = AlertDialog.Builder(context, R.style.CustomAlertDialog)
        val binding: CustomPopUpFeedbackBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.custom_pop_up_feedback,
            null,
            false
        )
        alertBuilder.setView(binding.root)
        alertDialog = alertBuilder.create()
        alertDialog.setCancelable(false)
        if (!alertDialog.isShowing) alertDialog.show()
        Glide.with(context)
            .load(topUp.topupIcon)
            .error(R.drawable.dummy_image)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .into(binding.ivLogo)
        binding.rlLogo.background = ContextCompat.getDrawable(
            context,
            bgDrawable
        )
        binding.tvTitle.text = topUp.topUpName
        binding.tvDescription.text = topUp.topUpDesc
        if (topUp.isIsCompleted) {
            binding.btnPositive.visibility = View.GONE
        }
        if (topUp.whatTo != null) {
            binding.llWhatHowAndWhy.visibility = View.VISIBLE
            binding.tvWhatToDo.text = topUp.whatTo
            binding.tvHowToDo.text = topUp.howTo
            binding.tvWhyToDo.text = topUp.whyTo
        }
        binding.btnNegative.setOnClickListener { view -> alertDialog.dismiss() }
        if (topUp.isStarted && !topUp.isIsCompleted) {
            binding.progressBar.visibility = View.VISIBLE
        } else if (topUp.isIsCompleted) {
            binding.progressBar.visibility = View.VISIBLE
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                binding.progressBar.setProgress(100, true)
            } else binding.progressBar.progress = 100
        }
        binding.btnPositive.setOnClickListener { view1 ->
            binding.btnPositive.visibility = View.VISIBLE
            binding.btnPositive.text = "Submit"
            binding.tvLabel.text = "Your feedback"
            binding.llJournal.visibility = View.VISIBLE
            binding.btnPositive.setOnClickListener {
                val msg: String = binding.edtFeedback.text.toString().replace(" ", "")
                if (!TextUtils.isEmpty(binding.edtFeedback.text) && msg.length >= 35) {
                    alertDialog.dismiss()
                    topUpFeedback(binding.edtFeedback.text.toString())
                } else {
                    binding.edtFeedback.requestFocus()
                    Toast.makeText(
                        context,
                        resources.getString(R.string.at_least_35_char),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
        if (topUp.isIsStarted && !topUp.isIsCompleted) {
            binding.btnPositive.setVisibility(View.VISIBLE)
            binding.btnPositive.setText("Submit")
            binding.tvLabel.setText("Your feedback")
            binding.llJournal.setVisibility(View.VISIBLE)
            binding.btnPositive.setOnClickListener { view ->
                if (!TextUtils.isEmpty(binding.edtFeedback.getText())) {
                    alertDialog.dismiss()
                    topUpFeedback(binding.edtFeedback.getText().toString())
                } else {
                    binding.edtFeedback.requestFocus()
                }
            }
        }
        if (topUp.isIsCompleted) {
            binding.llJournal.setVisibility(View.GONE)
            binding.btnPositive.setVisibility(View.GONE)
        }
        val displayRectangle = Rect()
        val window = window
        window.decorView.getWindowVisibleDisplayFrame(displayRectangle)
        alertDialog.getWindow()!!.setLayout(
            (displayRectangle.width() *
                    0.8f).toInt(), (displayRectangle.height() * 0.7f).toInt()
        )
    }

    private fun showCustomPopUpJournalUploadTopUp(topUp: TopUp, bgDrawable: Int) {
        val alertBuilder = AlertDialog.Builder(context, R.style.CustomAlertDialog)
        customPopUpRewardsJournalUploadBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.custom_pop_up_rewards_journal_upload,
            null,
            false
        )
        alertBuilder.setView(customPopUpRewardsJournalUploadBinding.getRoot())
        levelActivityJournalUploadAlertDialog = alertBuilder.create()
        levelActivityJournalUploadAlertDialog.setCancelable(false)
        if (!levelActivityJournalUploadAlertDialog.isShowing()) levelActivityJournalUploadAlertDialog.show()
        Glide.with(context)
            .load(topUp.topupIcon)
            .error(R.drawable.dummy_image)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .into(customPopUpRewardsJournalUploadBinding.ivLogo)
        customPopUpRewardsJournalUploadBinding.rlLogo.setBackground(
            ContextCompat.getDrawable(
                context,
                bgDrawable
            )
        )
        customPopUpRewardsJournalUploadBinding.tvTitle.setText(topUp.topUpName)
        customPopUpRewardsJournalUploadBinding.tvDescription.setText(topUp.topUpDesc)
        if (topUp.isStarted || topUp.isIsCompleted) {
            customPopUpRewardsJournalUploadBinding.btnPositive.setVisibility(View.GONE)
        }
        checkActivityPopUpConditionsJournalUploadTopUp(topUp)
        if (topUp.whatTo != null) {
            customPopUpRewardsJournalUploadBinding.llWhatHowAndWhy.setVisibility(View.VISIBLE)
            customPopUpRewardsJournalUploadBinding.tvWhatToDo.setText(topUp.whatTo)
            customPopUpRewardsJournalUploadBinding.tvHowToDo.setText(topUp.howTo)
            customPopUpRewardsJournalUploadBinding.tvWhyToDo.setText(topUp.whyTo)
        }
    }


    private fun checkActivityPopUpConditionsJournalUploadTopUp(topUp: TopUp) {
        customPopUpRewardsJournalUploadBinding.btnNegative.setOnClickListener { view -> levelActivityJournalUploadAlertDialog.dismiss() }
        customPopUpRewardsJournalUploadBinding.btnPositive.setOnClickListener { view1 -> }
        if (topUp.isStarted && !topUp.isIsCompleted) {
            customPopUpRewardsJournalUploadBinding.progressBar.setVisibility(View.VISIBLE)
        } else if (topUp.isIsCompleted) {
            customPopUpRewardsJournalUploadBinding.progressBar.setVisibility(View.VISIBLE)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                customPopUpRewardsJournalUploadBinding.progressBar.setProgress(100, true)
            } else customPopUpRewardsJournalUploadBinding.progressBar.setProgress(100)
        }
        if (!topUp.isIsCompleted && topUp.isStarted && topUp.eventType.equals(
                "JournalUpload",
                ignoreCase = true
            )
        ) {
            customPopUpRewardsJournalUploadBinding.btnPositive.setVisibility(View.VISIBLE)
            customPopUpRewardsJournalUploadBinding.btnPositive.setText("Submit")
            customPopUpRewardsJournalUploadBinding.llJournal.setVisibility(View.VISIBLE)
            customPopUpRewardsJournalUploadBinding.btnPositive.setOnClickListener { view ->
                if (!TextUtils.isEmpty(customPopUpRewardsJournalUploadBinding.edtJournal.getText()) && customPopUpRewardsJournalUploadBinding.edtJournal.getText().length >= 15
                ) {
                    journalContent =
                        customPopUpRewardsJournalUploadBinding.edtJournal.getText().toString()
                    fileUploadKey = "JournalUpload"
                    uploadFileTopUp = topUp
                    if (ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.CAMERA
                        ) == PackageManager.PERMISSION_DENIED
                    ) {
                        //levelActivityJournalUploadAlertDialog.dismiss();
                        checkPermission(
                            Manifest.permission.CAMERA,
                            CAMERA_PERMISSION_CODE
                        )
                    } else {
                        checkImagePicker()
                    }
                } else {
                    customPopUpRewardsJournalUploadBinding.edtJournal.requestFocus()
                    Toast.makeText(
                        context,
                        resources.getString(R.string.at_least_15_char),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
        if (!topUp.isCompleted && topUp.isStarted && topUp.eventType.equals(
                "journal",
                ignoreCase = true
            )
        ) {
            customPopUpRewardsJournalUploadBinding.btnPositive.setVisibility(View.VISIBLE)
            customPopUpRewardsJournalUploadBinding.btnPositive.setText("Submit")
            customPopUpRewardsJournalUploadBinding.llJournal.setVisibility(View.VISIBLE)
            customPopUpRewardsJournalUploadBinding.edtJournal.setHint(resources.getString(R.string.at_least_15_char))
            customPopUpRewardsJournalUploadBinding.btnPositive.setOnClickListener { view ->
                if (!TextUtils.isEmpty(customPopUpRewardsJournalUploadBinding.edtJournal.getText()) &&
                    customPopUpRewardsJournalUploadBinding.edtJournal.getText().toString()
                        .length > 15
                ) {
                    levelActivityJournalUploadAlertDialog.dismiss()
                    journalContent =
                        customPopUpRewardsJournalUploadBinding.edtJournal.getText().toString()
                    uploadContentOnlyTopUP(topUp)
                } else {
                    Toast.makeText(
                        context,
                        resources.getString(R.string.at_least_15_char),
                        Toast.LENGTH_SHORT
                    ).show()
                    customPopUpRewardsJournalUploadBinding.edtJournal.requestFocus()
                }
            }
        }
        if (topUp.isIsCompleted) {
            customPopUpRewardsJournalUploadBinding.llJournal.setVisibility(View.GONE)
            customPopUpRewardsJournalUploadBinding.btnPositive.setVisibility(View.GONE)
        }
        val displayRectangle = Rect()
        val window = window
        window.decorView.getWindowVisibleDisplayFrame(displayRectangle)
        if (topUp.isStarted && !topUp.isIsCompleted) {
            levelActivityJournalUploadAlertDialog.getWindow()!!.setLayout(
                (displayRectangle.width() *
                        0.8f).toInt(), (displayRectangle.height() * 0.8f).toInt()
            )
        } else {
            levelActivityJournalUploadAlertDialog.getWindow()!!.setLayout(
                (displayRectangle.width() *
                        0.8f).toInt(), (displayRectangle.height() * 0.6f).toInt()
            )
        }
    }

    fun checkPermission(permission: String, requestCode: Int) {
        // Checking if permission is not granted
        if (context == null) {
            context = this
        }
        if (ContextCompat.checkSelfPermission(
                context,
                permission
            ) == PackageManager.PERMISSION_DENIED
        ) {
            ActivityCompat.requestPermissions(
                (context as Activity),
                arrayOf(permission),
                requestCode
            )
        } else {
            checkImagePicker()
        }
    }

    fun deleteImage() {
        if (imageName != "") {
            deleteImage(imageName)
            imageName = ""
        }
    }

    fun checkImagePicker() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            openImagePicker()
        } else {
            openGalleryOnly()
        }
    }

    fun openImagePicker() {
        try {
            val intent = Intent()
            intent.putExtra(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(intent, 1)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    fun openGalleryOnly() {
        ImagePicker.create(this)
            .includeVideo(false)
            .imageDirectory("Camera")
            .enableLog(true)
            .includeAnimation(true)
            .limit(1)
            .showCamera(true)
            .start()
    }

    private fun prepareFilePart(partName: String, path: String): MultipartBody.Part {
        val file = File(path)
        val mimeType = URLConnection.guessContentTypeFromName(file.name)
        val requestFile = RequestBody.create(MediaType.parse(mimeType), file)
        return MultipartBody.Part.createFormData(partName, file.name, requestFile)
    }

    private fun topUpRedirection(topUp: TopUp) {
        if (topUp.redirectTo != null) {
            when (topUp.redirectTo.lowercase(Locale.getDefault())) {
                "invite" -> {
                    val intent = Intent(context, ContactsActivityNew::class.java)
                    intent.putExtra("comingFrom", "share")
                    intent.putExtra("isFromHRA", true)
                    startActivity(intent)
                }

                "tribe" -> {
                    /*intent = Intent(context, CommunitiesActivity::class.java)
                    intent.putExtra("cameFromLevel", false)
                    startActivity(intent)*/
                }

                "quiz" -> {
                    intent = Intent(context, QuizathonViewAllActivity::class.java).apply {
                        putExtra("type", "quiz")
                        putExtra("quiz_cat", "All")
                        putExtra("name", "Play and Learn")
                    }
                    startActivityForResult(intent, 2121)
                }

                "happy footprint" -> {
                    intent = Intent(context, TrendsActivity::class.java)
                    intent.putExtra("activityType", Constants.STEPS)
                    startActivity(intent)
                }
            }
        }
    }

    private fun checkTopUpCondition(topUp: TopUp) {
        customPopupRewardsBinding.btnNegative.setOnClickListener { view1 -> if (alertDialog != null) alertDialog.dismiss() }
        customPopupRewardsBinding.btnPositive.setOnClickListener { view1 ->
//            alertDialog.dismiss();
            if (topUp.isIsStarted) {
                if (topUp.redirectTo != null) {
                    alertDialog.dismiss()
                    topUpRedirection(topUp)
                }
            } else {
                if (topUp.topUpName.equals("Scratch & Win", ignoreCase = true)) {
                    startScratchAndWin(topUp)
                } /*else
                    startTopUps(topUp);*/
            }
        }
        if (topUp.isIsStarted && topUp.eventType != "upload" && !topUp.isIsCompleted) {
            if (topUp.redirectTo != null) {
                customPopupRewardsBinding.btnPositive.setVisibility(View.VISIBLE)
                customPopupRewardsBinding.btnPositive.setText("Complete")
            } else {
                customPopupRewardsBinding.btnPositive.setVisibility(View.GONE)
            }
        }
        if (topUp.isIsStarted && topUp.eventType.equals(
                "upload",
                ignoreCase = true
            ) && !topUp.isIsCompleted
        ) {
            customPopupRewardsBinding.btnPositive.setVisibility(View.VISIBLE)
            customPopupRewardsBinding.btnPositive.setText("Upload")
            customPopupRewardsBinding.btnPositive.setOnClickListener { view ->
                fileUploadKey = "ActivityUploads"
                alertDialog.dismiss()
                uploadFileTopUp = topUp
                checkPermission(
                    Manifest.permission.CAMERA,
                    CAMERA_PERMISSION_CODE
                )
            }
        }
        if (topUp.isIsStarted && topUp.eventType.equals(
                "yesno",
                ignoreCase = true
            ) && !topUp.isIsCompleted
        ) {
            customPopupRewardsBinding.llYesNo.setVisibility(View.VISIBLE)
            customPopupRewardsBinding.btnYes.setOnClickListener { view ->
                alertDialog.dismiss()
                yesNoAnswer = "Yes"
                uploadContentOnlyTopUP(topUp)
            }
            customPopupRewardsBinding.btnNo.setOnClickListener { view ->
                alertDialog.dismiss()
                yesNoAnswer = "No"
                uploadContentOnlyTopUP(topUp)
            }
        }
        if (topUp.isIsStarted && topUp.eventType.equals(
                "journal",
                ignoreCase = true
            ) && !topUp.isIsCompleted
        ) {
            customPopupRewardsBinding.btnPositive.setVisibility(View.VISIBLE)
            customPopupRewardsBinding.btnPositive.setText("Submit")
            customPopupRewardsBinding.llJournal.setVisibility(View.VISIBLE)
            customPopupRewardsBinding.btnPositive.setOnClickListener { view ->
                if (!TextUtils.isEmpty(customPopupRewardsBinding.edtJournal.getText()) && CommonUtils.validateLetters(
                        customPopupRewardsBinding.edtJournal.getText().toString()
                    )
                ) {
                    alertDialog.dismiss()
                    journalContent = customPopupRewardsBinding.edtJournal.getText().toString()
                    uploadContentOnlyTopUP(topUp)
                } else {
                    customPopupRewardsBinding.edtJournal.requestFocus()
                }
            }
        }
        if (topUp.topUpName.lowercase(Locale.getDefault())
                .contains("challenge") && !topUp.topUpName.equals(
                "Safety challenge",
                ignoreCase = true
            )
        ) {
            customPopupRewardsBinding.btnPositive.setVisibility(View.GONE)
        }
    }

    private fun uploadContentOnlyTopUP(topUp: TopUp) {
        val progressDialog = ProgressDialog(context, R.style.ProgressBarTheme)
        progressDialog.setCancelable(false)
        progressDialog.setMessage("Please wait...")
        if (progressDialog != null && !progressDialog.isShowing) progressDialog.show()
        val client = OkHttpClient().newBuilder()
            .certificatePinner(ApiClientWyh.getCertificatePinner())
            .build()
        val body: RequestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("ActivityUploads", "")
            .addFormDataPart("eventId", topUp.topUpID.toString())
            .addFormDataPart("eventType", topUp.eventType)
            .addFormDataPart("eventName", topUp.topUpTag)
            .addFormDataPart("yesNo", if (yesNoAnswer == null) "" else yesNoAnswer)
            .addFormDataPart(
                "journalContent",
                if (journalContent == null) "" else journalContent
            )
            .addFormDataPart("eventCategory", Constants.TAG_ADD_ON)
            .build()
        val request = Request.Builder()
            .url(CommonUtils.getBaseUrlForAPI(context) + "Rewards/EarnRewards")
            .method("POST", body)
            .addHeader("Authorization", SharedPref.getAuthToken())
            .build()
        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                if (progressDialog != null && progressDialog.isShowing) progressDialog.dismiss()
                deleteImage()
                fileDataList.clear()
                Analytics.logEvent(
                    context,
                    context.javaClass.name,
                    context.getString(R.string.upload_top_up_activity_failed)
                )
            }

            @Throws(IOException::class)
            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                if (response.code() == 200 && response.body() != null) {
                    Analytics.logEvent(
                        context,
                        context.javaClass.name,
                        context.getString(R.string.upload_top_up_activity_success)
                    )
                    if (progressDialog != null && progressDialog.isShowing) progressDialog.dismiss()
                    if (levelActivityJournalUploadAlertDialog != null && levelActivityJournalUploadAlertDialog.isShowing()) levelActivityJournalUploadAlertDialog.dismiss()
                    deleteImage()
                    fileDataList.clear()
                    val commonSuccessResponse = Gson().fromJson(
                        response.body()!!.string(),
                        CommonSuccessResponse::class.java
                    )
                    if (commonSuccessResponse.rewards != null) {
                        runOnUiThread {
                            Toast.makeText(
                                context,
                                context.resources.getString(R.string.points_earned_msg),
                                Toast.LENGTH_SHORT
                            ).show()
                            NewDashboardHelper.popUpShowModels.clear()
                            if (commonSuccessResponse.rewards != null && commonSuccessResponse.rewards.reward != null) {
                                NewDashboardHelper.popUpShowModels.add(
                                    PopUpShowModel(
                                        Constants.Rewards,
                                        commonSuccessResponse.rewards.reward
                                    )
                                )
                            }
                            if (commonSuccessResponse.rewards != null && commonSuccessResponse.rewards.bonusRewards != null) {
                                NewDashboardHelper.popUpShowModels.add(
                                    PopUpShowModel(
                                        Constants.RewardsBounce,
                                        commonSuccessResponse.rewards.bonusRewards
                                    )
                                )
                            }
                            showRewardsPopupDialogBox(
                                context,
                                this@NewDashboardActivity,
                                feedbackResponseData
                            )
                        }
                    }
                } else {
                    Analytics.logEvent(
                        context,
                        context.javaClass.name,
                        context.getString(R.string.upload_top_up_activity_failed)
                    )
                }
            }
        })
    }

    private fun topUpFeedback(feedback: String) {
        if (progressDialog != null && !progressDialog.isShowing()) progressDialog.show()
        val request = AppFeedbackRequest(feedback)
        val call: Call<CommonSuccessResponse> =
            apiInterfaceWyh.topUpFeedback(SharedPref.getAuthToken(), request)
        call.enqueue(object : Callback<CommonSuccessResponse?> {
            override fun onResponse(
                call: Call<CommonSuccessResponse?>, response: Response<CommonSuccessResponse?>
            ) {
                if (progressDialog != null && progressDialog.isShowing()) progressDialog.dismiss()
                if (response.body() != null && response.code() == 200) {
                    Analytics.logEvent(
                        context,
                        context.javaClass.name,
                        getString(R.string.app_feedback_success)
                    )
                    Toast.makeText(context, "Feedback sent successfully", Toast.LENGTH_SHORT)
                        .show()
                    NewDashboardHelper.popUpShowModels.clear()
                    if (response.body()!!.rewards != null && response.body()!!.rewards.bonusRewards != null) {
                        NewDashboardHelper.popUpShowModels.add(
                            PopUpShowModel(
                                Constants.RewardsBounce, response.body()!!
                                    .rewards.bonusRewards
                            )
                        )
                    }
                    if (response.body()!!.rewards != null && response.body()!!.rewards.reward != null) {
                        NewDashboardHelper.popUpShowModels.add(
                            PopUpShowModel(
                                Constants.Rewards, response.body()!!
                                    .rewards.reward
                            )
                        )
                    }
                    if (response.body()!!.enGTokens != null && response.body()!!.enGTokens.bonusTokens != null) {
                        NewDashboardHelper.popUpShowModels.add(
                            PopUpShowModel(
                                Constants.TokenStampBounce, response.body()!!
                                    .enGTokens.bonusTokens
                            )
                        )
                    }

                    showRewardsPopupDialogBox(
                        context,
                        this@NewDashboardActivity,
                        feedbackResponseData
                    )
                } else {
                    Analytics.logEvent(
                        context,
                        context.javaClass.name,
                        getString(R.string.app_feedback_failed)
                    )

                }
            }

            override fun onFailure(call: Call<CommonSuccessResponse?>, t: Throwable) {
                if (progressDialog != null && progressDialog.isShowing()) progressDialog.dismiss()
                Analytics.logEvent(
                    context,
                    context.javaClass.name,
                    getString(R.string.app_feedback_failed)
                )
                Toast.makeText(
                    context,
                    resources.getString(R.string.internet_connection),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    fun showScratchAndWinPopup(rewards: String?, context: Context?) {
        val alertBuilder = AlertDialog.Builder(context)
        val binding: LayoutNewPointsPopUpBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.layout_new_points_pop_up,
            null,
            false
        )
        alertBuilder.setView(binding.getRoot())
        alertDialogScratchAndWin = alertBuilder.create()
        alertDialogScratchAndWin.setCancelable(false)
        if (!alertDialogScratchAndWin.isShowing()) alertDialogScratchAndWin.show()
        val title = "Scratch & Win"
        binding.scratchView.setScratchDrawable(resources.getDrawable(R.drawable.scratch_card_blue_new))
        binding.tvPoints.setText(rewards)
        binding.tvEventName.setVisibility(View.VISIBLE)
        binding.tvEventName.setText(title)
        alertDialogScratchAndWin.setOnDismissListener(DialogInterface.OnDismissListener { })
        binding.ivClose.setOnClickListener { view -> alertDialogScratchAndWin.dismiss() }

//      binding.btnPositive.setText("Collect");
        binding.btnPositive.setOnClickListener { view -> alertDialogScratchAndWin.dismiss() }
        binding.scratchView.setScratchListener(this)
        binding.btnNegative.setOnClickListener { view -> alertDialogScratchAndWin.dismiss() }
        val displayRectangle = Rect()
        val window = window
        window.decorView.getWindowVisibleDisplayFrame(displayRectangle)
        alertDialogScratchAndWin.window!!
            .setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialogScratchAndWin.window!!.setLayout(
            (displayRectangle.width() *
                    0.7f).toInt(), RelativeLayout.LayoutParams.WRAP_CONTENT
        )
    }

    fun getDashboardData(binding: ActivityNewDashboardBinding, context: Context) {
        try {
            val apiInterface = RetrofitHandler.apiInterface()
            apiInterface.getDashBoardDataV3(SharedPref.getAuthToken())
                .enqueue(object : Callback<NewDashboardResponse> {
                    override fun onResponse(
                        call: Call<NewDashboardResponse>,
                        response: Response<NewDashboardResponse>
                    ) {
                        try {
                            if (response.body() != null && response.code() == 200 && response.isSuccessful) {

                                SharedPref.putDashboardResponse(Gson().toJson(response.body()))
                                setDashboardData(response.body()!!, context, binding)
                                if (response.body()!!.data.fetchdashboarddata.filePopupModellist != null && response.body()!!.data.fetchdashboarddata.filePopupModellist.size > 0) {

                                    NewDashboardHelper.Companion.popupModelList =
                                        response.body()!!.data.fetchdashboarddata.filePopupModellist;
                                    ShowFileShareDialogNew()


                                }
                            } else {
                                Analytics.logEvent(
                                    context,
                                    "",
                                    "A_102_${response.code()}_${SharedPref.getEncryptedMobileNo()}"
                                )
                            }

                        } catch (e: Exception) {
                            e.toString()
                        }
                    }

                    override fun onFailure(call: Call<NewDashboardResponse>, t: Throwable) {
                        Analytics.logEvent(
                            context,
                            "",
                            "A_101_Failed_${SharedPref.getEncryptedMobileNo()}"
                        )
                    }

                })

        } catch (e: Exception) {
            e.toString()
        }
    }


    fun setDashboardData(
        response: NewDashboardResponse,
        context: Context,
        binding: ActivityNewDashboardBinding
    ) {
        try {
            if (response != null && response.data != null) {
                if (response.data.nudgeRedirection != null && response.data.nudgeRedirection.nudgeFeature != null) {
                    firstRedirection =
                        response.data.nudgeRedirection.nudgeFeature.firstRedirection
                    secondRedirection =
                        response.data.nudgeRedirection.nudgeFeature.secondRedirection
                    thirdRedirection = response.data.nudgeRedirection.nudgeFeature.featureName
                    fourthRedirection = response.data.nudgeRedirection.nudgeFeature.activityName
                }

                if (response.data.nudgeRedirection.nudgeBanner != null) {
                    nudgeBanner = response.data.nudgeRedirection.nudgeBanner
                }

                if (response.data.fetchdashboarddata.actoMeterTribe != null) {
                    actoMeterTribe = response.data.fetchdashboarddata.actoMeterTribe
                }

                if (response.data.fetchdashboarddata.showSpin) {
                    SharedPref.setShowSpin(response.data.fetchdashboarddata.showSpin)
                    GetActiveQuizathon();
                    //binding.Spinthewheel.visibility = View.VISIBLE
                } else {
                    SharedPref.setShowSpin(response.data.fetchdashboarddata.showSpin)
                    GetActiveQuizathon();
                    //binding.Spinthewheel.visibility = View.GONE
                }

                binding.tvQuizVewAll.setOnClickListener {
                    APILogs.activityTracker("A_Dashboard_PlayAndWin_ViewAll_Clicked", context);
                    val intent = Intent(context, PlayAndWinActivity::class.java)
                    startActivity(intent)
                }

                binding.rlSpinthewheel.setOnClickListener {
                    APILogs.activityTracker("A_HOMEDASHBOARD_OTHER_SPINWINTILECLICKED", context);
                    val intent = Intent(context, SpinWheelRewardsActivity::class.java)
                    startActivity(intent)
                }

                binding.rlPlayandWin.setOnClickListener {

                    val intent = Intent(context, PlayAndWinActivity::class.java)
                    startActivity(intent)
                }


                if (SharedPreference.getGoogleFitConnection()) {
                    binding.stepsLayout.visibility = View.VISIBLE
                    binding.syncYourDeviceTv.visibility = View.GONE
                } else {
                    binding.stepsLayout.visibility = View.GONE
                    binding.syncYourDeviceTv.visibility = View.VISIBLE
                }


                setNudgeImages(response.data.nudgeRedirection.nudgeFeature, context, binding)
                setUpBlogs(response.data.fetchdashboarddata.blogs)

                if (response.data.challengesDetails.isNotEmpty()) {
                    binding.challengesLayout.visibility = View.VISIBLE
                    challengesList = response.data.challengesDetails
                    val joinList = ArrayList<challengesData>()
                    val unJoinList = ArrayList<challengesData>()
                    helperClass.listChallengeId.clear()
                    challengesList.forEach { e ->
                        if (e.IsUserEnrolled == "1") {
                            if (!e.ChallengeType.equals(
                                    "tribe",
                                    true
                                ) && !e.ChallengeType.equals("Steps", true)
                            ) {
                                helperClass.listChallengeId.add(e.ChallengeId)
                            }
                            joinList.add(e)
                        } else {
                            unJoinList.add(e)
                        }
                    }
                    SharedPref.putChallengeCount(helperClass.listChallengeId.size)
                    setUpChallenges(challengesList, context, binding)
                } else {
                    binding.challengesLayout.visibility = View.GONE
                }

                //Rewards
                val rewards: RewardsResponse = response.data.fetchdashboarddata.rewards
                val tvLevel: TextView =
                    binding.navigationDrawer.getHeaderView(0).findViewById(R.id.tv_level)
                val tvPoints: TextView =
                    binding.navigationDrawer.getHeaderView(0).findViewById(R.id.tv_points)
                val llPoints: LinearLayout =
                    binding.navigationDrawer.getHeaderView(0).findViewById(R.id.llPoints)
                val llLevel: LinearLayout =
                    binding.navigationDrawer.getHeaderView(0).findViewById(R.id.llLevel)
                val progressIndicator: LinearProgressIndicator =
                    binding.navigationDrawer.getHeaderView(0).findViewById(R.id.level_progress)
                progressIndicator.max = 16
                progressIndicator.progress = rewards.currentLevel


                progressGoals = rewards.currentLevel
                currentLevel = rewards.currentLevel
                totalPoints = rewards.totalPoints
                SharedPref.putCurrentLevel(currentLevel)
                tvLevel.text = "Level: " + currentLevel
                tvPoints.text = "" + totalPoints


                NewDashboardHelper.waterIntakeGoal =
                    response.data.fetchdashboarddata.actoMeter.userWaterGoal
                NewDashboardHelper.waterIntake =
                    response.data.fetchdashboarddata.actoMeter.userTodayWaterIntakeGoal.toDouble()
                NewDashboardHelper.currentValueZenZone =
                    response.data.fetchdashboarddata.actoMeter.zenzoneMinutes.toString()
                binding.userLevel.text =
                    "Level ${response.data.fetchdashboarddata.rewards.currentLevel.toString()}"
                NewDashboardHelper.level = response.data.fetchdashboarddata.rewards.currentLevel
                binding.userPoints.text =
                    "Available points: ${response.data.fetchdashboarddata.rewards.totalPoints}"
                SharedPref.putTotalPoints(response.data.fetchdashboarddata.rewards.totalPoints.toString())
                binding.stepsTvValue.text =
                    response.data.fetchdashboarddata.actoMeter.userTodaySteps.toString()
                binding.stepsOutOfValueTv.text =
                    "/${response.data.fetchdashboarddata.actoMeter.userStepsGoal.toString()}"

                binding.zenZoneValueTv.text =
                    response.data.fetchdashboarddata.actoMeter.zenzoneMinutes.toString()
                currentValueZenZone =
                    response.data.fetchdashboarddata.actoMeter.zenzoneMinutes.toString()
                userStepsGoal = response.data.fetchdashboarddata.actoMeter.userStepsGoal

            }
        } catch (e: Exception) {
            e.toString()
        }
    }

    fun setUpChallenges(
        list: ArrayList<challengesData>,
        context: Context,
        binding: ActivityNewDashboardBinding
    ) {
        val challengeAdapter = ChallengesAdapter(context, list, this@NewDashboardActivity)
        binding.challengesRv.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.challengesRv.hasFixedSize()
        binding.challengesRv.adapter = challengeAdapter
    }

    fun setNudgeImages(
        nudgeFeature: NudgeFeature?,
        context: Context,
        binding: ActivityNewDashboardBinding
    ) {
        try {
            if (nudgeFeature != null) {
                if (nudgeFeature.firstRedirectionURL == null) {
                    binding.firstNudgeImg.setBackgroundResource(R.drawable.nudge_bear)

                } else {
                    if (!nudgeFeature.firstRedirectionURL.equals("", ignoreCase = true)) {
                        Glide.with(context)
                            .load(nudgeFeature.firstRedirectionURL)
                            .into(binding.firstNudgeImg)
                    } else {
                        binding.firstNudgeImg.setImageResource(getNudgeImage(firstRedirection))
                    }
                }
            }
            if (nudgeFeature != null) {
                if (nudgeFeature.secondRedirectionURL == null) {
                    binding.secondNudgeImg.setBackgroundResource(R.drawable.nudge_bear)
                } else {
                    if (!nudgeFeature.secondRedirectionURL.equals("", ignoreCase = true)) {
                        Glide.with(context)
                            .load(nudgeFeature.secondRedirectionURL)
                            .into(binding.secondNudgeImg)
                    } else {
                        binding.secondNudgeImg.setImageResource(getNudgeImage(secondRedirection))
                    }
                }

            }
            if (nudgeFeature != null) {
                if (nudgeFeature.thirdRedirectionURL == null) {
                    binding.thirdNudgeImg.setBackgroundResource(R.drawable.nudge_bear)
                } else {
                    if (!nudgeFeature.thirdRedirectionURL.equals("", ignoreCase = true)) {
                        Glide.with(context)
                            .load(nudgeFeature.thirdRedirectionURL)
                            .into(binding.thirdNudgeImg)
                    } else {
                        binding.thirdNudgeImg.setBackgroundResource(
                            getNudgeImage(
                                thirdRedirection
                            )
                        )
                    }
                }

            }
            if (nudgeFeature != null) {
                if (nudgeFeature.fourthRedirectionURL == null) {
                    binding.fourNudgeImg.setBackgroundResource(R.drawable.nudge_bear)
                } else {
                    if (!nudgeFeature.fourthRedirectionURL.equals("", ignoreCase = true)) {
                        Glide.with(context)
                            .load(nudgeFeature.fourthRedirectionURL)
                            .into(binding.fourNudgeImg)
                    } else {
                        binding.fourNudgeImg.setImageResource(getNudgeImage(fourthRedirection))
                    }
                }
            }
        } catch (e: Exception) {
            e.toString()
        }
    }


    fun updateUserDetails(
        name: String,
        email: String,
        dob: String,
        gende: String,
        comingFrom: String,
        context: Context
    ) {
        try {
            CommonUtils.showProgressDialige(context)
            val userDetailsRequest = UserDetailsRequest(
                name,
                RSAEncryption.rsaEncrypt(email),
                CommonUtils.formatDateFromString("dd/MM/yyyy", "yyyy/MM/dd", dob),
                gende
            )
            val apiInterface = RetrofitHandler.apiInterface()
            apiInterface.updateUserDetails(SharedPref.getAuthToken(), userDetailsRequest)
                .enqueue(object : Callback<UserDetailResponse?> {
                    override fun onResponse(
                        call: Call<UserDetailResponse?>,
                        response: Response<UserDetailResponse?>
                    ) {
                        try {
                            CommonUtils.dismissDialoge()
                            if (response.code() == 200 && response.body() != null && response.isSuccessful) {
                                if (comingFrom.equals("updateUsername", ignoreCase = true)) {
                                    SharedPref.putIsUserNameUpdated(true)
                                    SharedPref.putUserName(name)
                                    tvName =
                                        NewDashboardHelper.binding.navigationDrawer.getHeaderView(
                                            0
                                        ).findViewById(R.id.tvName)
                                    tvName.text = "Hi,\n$name"
                                    getUserDetails(
                                        NewDashboardHelper.binding,
                                        NewDashboardHelper.context
                                    )
                                    /* if (feedbackResponseData != null && feedbackResponseData.starConfig != null) {
                                        NewDashboardHelper.isShowUserPopup = false
                                        SharedPref.putFeedbackPopup(true)
                                        NewDashboardHelper.showFeedBackPopup(feedbackResponseData, context)
                                    }*/
                                    Toast.makeText(
                                        context,
                                        "Your name is updated",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                }
                                if (!comingFrom.equals("updateUsername", ignoreCase = true)) {
                                    faceScanRegistration(context, comingFrom, dob, gende, user)
                                }
                            }
                        } catch (e: Exception) {
                            e.toString()
                        }

                    }

                    override fun onFailure(call: Call<UserDetailResponse?>, t: Throwable) {
                        CommonUtils.dismissDialoge()
                    }
                })
        } catch (e: java.lang.Exception) {
            CommonUtils.dismissDialoge()
            e.printStackTrace()
        }
    }

    private fun nudgeRedirection(key: String, nudgeButton: String) {
        try {
            if (key.equals("Rewards", ignoreCase = true) || key.equals(
                    "EnG/Topup",
                    ignoreCase = true
                )
            ) {
                NewDashboardHelper.getNudgeDetails(key, nudgeButton, context)
            } else if (key.equals("water", ignoreCase = true)) {
                waterIntakeDialoge(
                    this@NewDashboardActivity,
                    NewDashboardHelper.waterIntakeGoal,
                    NewDashboardHelper.waterIntake,
                    NewDashboardHelper.waterIntakeAllowed
                )
            } else if (key.equals("zenzone", ignoreCase = true)) {
                showPopUpZenZone(NewDashboardHelper.currentValueZenZone.toDouble(), context)
            } else if (key.equals("Banner", ignoreCase = true) || key.equals(
                    "redirection",
                    ignoreCase = true
                )
            ) {
                if (nudgeBanner != null) {
                    val redirectionModel = RedirectionModel(
                        nudgeBanner.getRedirectionKey(),
                        nudgeBanner.openInChrome,
                        "Nudges",
                        nudgeBanner.getRedirectionUrl(),
                        nudgeBanner.getTitle(),
                        nudgeBanner.getDisclaimer(),
                        nudgeBanner.getDisclaimerImageUrl(),
                        "",
                        0,
                        nudgeBanner.getBannerId(),
                        nudgeBanner.getHappyMartKey(),
                        nudgeBanner.getHappyMartValue(),
                        "",
                        ""
                    )
                    redirectionScreen(redirectionModel, context)
                } else {
                    Toast.makeText(context, "No upcoming activity", Toast.LENGTH_SHORT).show()
                }
            } else if (key.contains("HappyMartKey")) {
                val split =
                    key.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                if (split.isNotEmpty()) {
                    if (split[2].equals("Tele", ignoreCase = true)) {
                        val redirectionModel = RedirectionModel(
                            "", false, "Nudges", "", "",
                            "", "", "", 0, "", "Tele-Consultation", split[1], "", ""
                        )
                        redirectionScreen(redirectionModel, context)
                    } else {
                        val redirectionModel = RedirectionModel(
                            "", false, "Nudges", "", "", "",
                            "", "", 0, "", split[2], split[1], "", ""
                        )
                        redirectionScreen(redirectionModel, context)
                    }
                }
            } else {
                val redirectionModel = RedirectionModel(
                    key,
                    false,
                    "Nudges",
                    "",
                    "",
                    "",
                    "",
                    "",
                    0,
                    "",
                    "",
                    "",
                    "",
                    ""
                )
                redirectionScreen(redirectionModel, context)
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    fun setUpBlogs(blogs: ArrayList<Blogs>) {
        try {
            var positionReads = 0
            val dashboardBlogAdapter = DashboardBlogAdapter(context, blogs, this)


            val linearLayoutManager = LinearLayoutManager(
                this@NewDashboardActivity,
                LinearLayoutManager.HORIZONTAL,
                false
            )

            binding.blogsList.layoutManager = linearLayoutManager
            binding.blogsList.hasFixedSize()
            binding.blogsList.adapter = dashboardBlogAdapter
            binding.blogsList.onFlingListener = null
            val snapHelper: SnapHelper = SnapHelperOneByOne()
            snapHelper.attachToRecyclerView(binding.blogsList);

            val indicatorsLayoutManager = LinearLayoutManager(context)
            indicatorsLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
            val indicatorsAdapter = IndicatorsAdapter(this@NewDashboardActivity, 3, 0)
            binding.blogsIndicators.layoutManager = indicatorsLayoutManager
            binding.blogsIndicators.hasFixedSize()
            binding.blogsIndicators.adapter = indicatorsAdapter

            binding.blogsList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        if (linearLayoutManager.findFirstCompletelyVisibleItemPosition() != -1) {
                            positionReads =
                                linearLayoutManager.findFirstCompletelyVisibleItemPosition()
                        } else positionReads =
                            linearLayoutManager.findFirstVisibleItemPosition()
                        Log.d("AuthToken", "$positionReads")
                        indicatorsAdapter.updateSelectedIndex(positionReads)
                    }
                }
            })

        } catch (e: Exception) {
            e.toString()
        }
    }

    override fun onScratchComplete() {

    }

    override fun onScratchProgress(scratchCardLayout: ScratchCardLayout, i: Int) {
        if (i >= 20 && NudgeDialogue.voucherIdRequest != null && !NewDashboardHelper.isRewards) {
            NewDashboardHelper.updateScratchStatus(context)
            scratchCardLayout.onFullReveal()
        } else if (i >= 20) {
            scratchCardLayout.onFullReveal()
        }
        if (i > 20) {
            val handler = Handler(Looper.getMainLooper())
            handler.postDelayed({
                if (NewDashboardHelper.isDialogBonusRewardsInitialized()) {
                    if (NewDashboardHelper.alertDialogBonusRewards != null && NewDashboardHelper.alertDialogBonusRewards.isShowing) {
                        NewDashboardHelper.alertDialogBonusRewards.dismiss()
                    }
                }

                if (NewDashboardHelper.isDialogDialogFirstLogin()) {
                    if (NewDashboardHelper.alertDialogFirstLogin != null && NewDashboardHelper.alertDialogFirstLogin.isShowing()) {
                        NewDashboardHelper.alertDialogFirstLogin.dismiss()
                    }
                }


            }, 3000)
        }
    }


    override fun onScratchStarted() {

    }

    fun checkVisibility(image: ImageView) {
        if (image.visibility == View.VISIBLE) {
            image.visibility = View.VISIBLE
        } else {
            image.visibility = View.INVISIBLE
        }
    }

    private fun viewToolTip(
        view: ImageView,
        text: String,
        position: ViewTooltip.Position,
        align: ViewTooltip.ALIGN
    ) {
        val viewTooltip = ViewTooltip.on(context as Activity, view)
            .autoHide(true, 500)
            .clickToHide(true)
            .align(align)
            .position(position)
            .text(text)
            .textColor(Color.BLACK)
            .color(context.resources.getColor(R.color.light_pink))
            .corner(10)
            .arrowWidth(15)
            .arrowHeight(15)
            .distanceWithView(0)
            .setTextGravity(Gravity.CENTER) //change the opening animation
            .animation(ViewTooltip.FadeTooltipAnimation(500)) //listeners
            .onDisplay { }
            .onHide { }
        viewTooltip.show()
    }

    fun userDetailsPopup(comingFrom: String, context: Context) {
        try {
            val builder = AlertDialog.Builder(context)
            val binding: UpdateSerDetailsLayoutBinding = DataBindingUtil.inflate(
                LayoutInflater.from(context), R.layout.update_ser_details_layout,
                null, false
            )
            builder.setView(binding.root)
            val alertDialog = builder.create()
            alertDialog.setCancelable(true)
            val prevYear = Calendar.getInstance()
            prevYear.add(Calendar.YEAR, -15)
            mainStartCalender = Calendar.getInstance()
            if (SharedPref.getDOB() != null && SharedPref.getDOB() !== "") {
                dobStr = SharedPref.getDOB()
                binding.updateUserDobEt.isEnabled = false
                binding.updateUserDobEt.setText(SharedPref.getDOB())
            }
            if (SharedPref.getRegistrationGender() != null && SharedPref.getRegistrationGender() !== "") {
                if (SharedPref.getRegistrationGender().equals("male", ignoreCase = true)) {
                    gender = "male"
                    binding.updateUserMaleLayout.isEnabled = false
                    binding.updateUserFemaleLayout.isEnabled = false
                    binding.updateUserMaleImg.setImageResource(R.drawable.male_selected)
                    binding.updateUserFemaleImg.setImageResource(R.drawable.female_unselected)
                } else {
                    gender = "female"
                    binding.updateUserFemaleLayout.isEnabled = false
                    binding.updateUserMaleLayout.isEnabled = false
                    binding.updateUserFemaleImg.setImageResource(R.drawable.female_selected)
                    binding.updateUserMaleImg.setImageResource(R.drawable.male_unselected)
                }
            }
            binding.updateUserRadioGroup.setOnCheckedChangeListener { radioGroup, i ->
                when (i) {
                    R.id.rb_self -> {
                        user = "self"
                        if (SharedPref.getRegistrationGender() != null && SharedPref.getRegistrationGender() !== "" || SharedPref.getDOB() != null && SharedPref.getDOB() !== "") {
                            binding.updateUserMaleLayout.isEnabled = false
                            binding.updateUserFemaleLayout.isEnabled = false
                            binding.updateUserDobEt.isEnabled = false
                        } else {
                            binding.updateUserMaleLayout.isEnabled = true
                            binding.updateUserFemaleLayout.isEnabled = true
                            binding.updateUserDobEt.isEnabled = true
                        }
                        if (SharedPref.getDOB() != null && SharedPref.getDOB() !== "") {
                            binding.updateUserDobEt.setText(SharedPref.getDOB())
                        }
                        if (SharedPref.getRegistrationGender() != null && SharedPref.getRegistrationGender() !== "") {
                            if (SharedPref.getUserGender().equals("male", ignoreCase = true)) {
                                gender = "male"
                                binding.updateUserMaleImg.setImageResource(R.drawable.male_selected)
                                binding.updateUserFemaleImg.setImageResource(R.drawable.female_unselected)
                            } else {
                                gender = "female"
                                binding.updateUserFemaleImg.setImageResource(R.drawable.female_selected)
                                binding.updateUserMaleImg.setImageResource(R.drawable.male_unselected)
                            }
                        } else {
                            binding.updateUserMaleLayout.isEnabled = true
                            binding.updateUserFemaleLayout.isEnabled = true
                            binding.updateUserMaleImg.setImageResource(R.drawable.male_unselected)
                            binding.updateUserFemaleImg.setImageResource(R.drawable.female_unselected)
                        }
                        if (comingFrom != null && !comingFrom.isEmpty()) {
                            if (comingFrom.equals("facescan", ignoreCase = true)) {
                                activityTracker("A_DB_Tile_FS_Self", context!!)
                            } else if (comingFrom.equals("hra", ignoreCase = true)) {
                                activityTracker("A_DB_Tile_FS_Self", context!!)
                            } else if (comingFrom.equals("search", ignoreCase = true)) {
                                activityTracker("A_DB_Tile_FS_Self", context!!)
                            }
                        }
                    }

                    R.id.rb_others -> {
                        user = "others"
                        binding.updateUserMaleLayout.isEnabled = true
                        binding.updateUserFemaleLayout.isEnabled = true
                        binding.updateUserDobEt.isEnabled = true
                        binding.updateUserMaleImg.setImageResource(R.drawable.male_unselected)
                        binding.updateUserFemaleImg.setImageResource(R.drawable.female_unselected)
                        binding.updateUserDobEt.setText("")
                        if (comingFrom != null && !comingFrom.isEmpty()) {
                            if (comingFrom.equals("facescan", ignoreCase = true)) {
                                activityTracker("A_DB_Tile_FS_Others", context!!)
                            } else if (comingFrom.equals("hra", ignoreCase = true)) {
                                activityTracker("A_DB_Tile_FS_Self", context!!)
                            } else if (comingFrom.equals("search", ignoreCase = true)) {
                                activityTracker("A_DB_Tile_FS_Self", context!!)
                            }
                        }
                    }
                }
            }
            binding.updateUserDobEt.setOnClickListener {
                val year: Int = mainStartCalender.get(Calendar.YEAR)
                val month: Int = mainStartCalender.get(Calendar.MONTH)
                val day: Int = mainStartCalender.get(Calendar.DAY_OF_MONTH)
                val pickerDialog = DatePickerDialog(
                    context!!,
                    { view1: DatePicker?, year1: Int, monthOfYear: Int, dayOfMonth: Int ->
                        val newDay: String
                        newDay = if (dayOfMonth.toString().length == 1) {
                            "0$dayOfMonth"
                        } else {
                            dayOfMonth.toString()
                        }
                        val newMonth: String
                        newMonth = if ((monthOfYear + 1).toString().length == 1) {
                            "0" + (monthOfYear + 1)
                        } else {
                            (monthOfYear + 1).toString()
                        }
                        dobStr = "$year1-$newMonth-$newDay"
                        binding.updateUserDobEt.setText(dobStr)

                        //Toast.makeText(getApplicationContext(), dobStr, Toast.LENGTH_LONG).show();
                        mainStartCalender = Calendar.getInstance()
                        mainStartCalender.set(Calendar.YEAR, year1)
                        mainStartCalender.set(Calendar.MONTH, monthOfYear)
                        mainStartCalender.set(Calendar.DATE, dayOfMonth)
                        if (comingFrom != null && !comingFrom.isEmpty() && user.equals(
                                "self",
                                ignoreCase = true
                            )
                        ) {
                            if (comingFrom.equals("facescan", ignoreCase = true)) {
                                activityTracker("A_DB_Tile_FS_Self_DOB", context)
                            } else if (comingFrom.equals("hra", ignoreCase = true)) {
                                activityTracker("A_DB_Tile_FS_Self", context)
                            } else if (comingFrom.equals("search", ignoreCase = true)) {
                                activityTracker("A_DB_Tile_FS_Self", context)
                            }
                        } else if (comingFrom != null && !comingFrom.isEmpty() && user.equals(
                                "others",
                                ignoreCase = true
                            )
                        ) {
                            if (comingFrom.equals("facescan", ignoreCase = true)) {
                                activityTracker("A_DB_Tile_FS_Others_DOB", context)
                            } else if (comingFrom.equals("hra", ignoreCase = true)) {
                                activityTracker("A_DB_Tile_FS_Self", context)
                            } else if (comingFrom.equals("search", ignoreCase = true)) {
                                activityTracker("A_DB_Tile_FS_Self", context)
                            }
                        }
                    }, year, month, day
                )
                //pickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
                pickerDialog.datePicker.maxDate = prevYear.timeInMillis
                pickerDialog.show()
            }
            binding.updateUserMaleLayout.setOnClickListener {
                if (user.equals("self", ignoreCase = true)) {
                    gender = "male"
                    if (comingFrom != null && !comingFrom.isEmpty()) {
                        if (comingFrom.equals("facescan", ignoreCase = true)) {
                            activityTracker("A_DB_Tile_FS_Self_Male", context!!)
                        } else if (comingFrom.equals("hra", ignoreCase = true)) {
                            activityTracker("A_DB_Tile_FS_Self", context!!)
                        } else if (comingFrom.equals("search", ignoreCase = true)) {
                            activityTracker("A_DB_Tile_FS_Self", context!!)
                        }
                    }
                } else {
                    otherGender = "male"
                    if (comingFrom != null && !comingFrom.isEmpty()) {
                        if (comingFrom.equals("facescan", ignoreCase = true)) {
                            activityTracker("A_DB_Tile_FS_Others_Male", context!!)
                        } else if (comingFrom.equals("hra", ignoreCase = true)) {
                            activityTracker("A_DB_Tile_FS_Self", context!!)
                        } else if (comingFrom.equals("search", ignoreCase = true)) {
                            activityTracker("A_DB_Tile_FS_Self", context!!)
                        }
                    }
                }
                binding.updateUserMaleImg.setImageResource(R.drawable.male_selected)
                binding.updateUserFemaleImg.setImageResource(R.drawable.female_unselected)
            }
            binding.updateUserFemaleLayout.setOnClickListener {
                if (user.equals("self", ignoreCase = true)) {
                    gender = "female"
                    if (comingFrom != null && !comingFrom.isEmpty()) {
                        if (comingFrom.equals("facescan", ignoreCase = true)) {
                            activityTracker("A_DB_Tile_FS_Self_Female", context!!)
                        } else if (comingFrom.equals("hra", ignoreCase = true)) {
                            activityTracker("A_DB_Tile_FS_Self", context!!)
                        } else if (comingFrom.equals("search", ignoreCase = true)) {
                            activityTracker("A_DB_Tile_FS_Self", context!!)
                        }
                    }
                } else {
                    otherGender = "female"
                    if (comingFrom != null && !comingFrom.isEmpty()) {
                        if (comingFrom.equals("facescan", ignoreCase = true)) {
                            activityTracker("A_DB_Tile_FS_Others_Female", context!!)
                        } else if (comingFrom.equals("hra", ignoreCase = true)) {
                            activityTracker("A_DB_Tile_FS_Self", context!!)
                        } else if (comingFrom.equals("search", ignoreCase = true)) {
                            activityTracker("A_DB_Tile_FS_Self", context!!)
                        }
                    }
                }
                binding.updateUserFemaleImg.setImageResource(R.drawable.female_selected)
                binding.updateUserMaleImg.setImageResource(R.drawable.male_unselected)
            }
            binding.updateUserSubmit.setOnClickListener(View.OnClickListener {
                if (user.equals("self", ignoreCase = true)) {
                    if (gender == "") {
                        Toast.makeText(context, "Please select gender", Toast.LENGTH_LONG)
                            .show()
                        return@OnClickListener
                    }
                } else {
                    if (otherGender == "") {
                        Toast.makeText(context, "Please select gender", Toast.LENGTH_LONG)
                            .show()
                        return@OnClickListener
                    }
                }
                if (binding.updateUserDobEt.text.toString().equals("", true)) {
                    Toast.makeText(
                        context,
                        "Please select your date of birth",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    return@OnClickListener
                }
                if (user == "") {
                    Toast.makeText(context, "Please select user", Toast.LENGTH_LONG).show()
                    return@OnClickListener
                }
                alertDialog.dismiss()
                if (!user.equals("others", ignoreCase = true)) {
                    updateUserDetails("", "", dobStr, gender, comingFrom!!, context!!)
                    if (comingFrom != null && !comingFrom.isEmpty()) {
                        if (comingFrom.equals("facescan", ignoreCase = true)) {
                            activityTracker("A_DB_Tile_FS_Others_Submit", context)
                        } else if (comingFrom.equals("hra", ignoreCase = true)) {
                            activityTracker("A_DB_Tile_FS_Self", context)
                        } else if (comingFrom.equals("search", ignoreCase = true)) {
                            activityTracker("A_DB_Tile_FS_Self", context)
                        }
                    }
                } else {
                    if (comingFrom != null && !comingFrom.isEmpty()) {
                        if (comingFrom.equals("facescan", ignoreCase = true)) {
                            activityTracker("A_DB_Tile_FS_Self_Submit", context!!)
                        } else if (comingFrom.equals("hra", ignoreCase = true)) {
                            activityTracker("A_DB_Tile_FS_Self", context!!)
                        } else if (comingFrom.equals("search", ignoreCase = true)) {
                            activityTracker("A_DB_Tile_FS_Self", context!!)
                        }
                    }
                    faceScanRegistration(context, comingFrom, dobStr, otherGender, user)
                }
            })
            if (!alertDialog.isShowing) alertDialog.show()
            val displayRectangle = Rect()
            val window = window
            window.decorView.getWindowVisibleDisplayFrame(displayRectangle)
            alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            alertDialog.window!!.setLayout(
                (displayRectangle.width() *
                        0.88f).toInt(), RelativeLayout.LayoutParams.WRAP_CONTENT
            )
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    fun faceScanRegistration(
        context: Context,
        comingFrom: String,
        dob: String?,
        gende: String?,
        userType: String
    ) {
        try {
            CommonUtils.showProgressDialige(context)
            if (userType.equals("self", ignoreCase = true)) {
                SharedPref.putDOB(dob)
                SharedPref.putRegistrationGender(gende)
            }
            val faceScanRegistrationRequest = FaceScanRegistrationRequest(
                dob!!, gende!!, userType
            )
            val apiInterface = RetrofitHandler.apiInterface()
            apiInterface.faceScanRegistration(
                SharedPref.getAuthToken(), faceScanRegistrationRequest
            ).enqueue(object : Callback<FaceScanRegistrationResponse?> {
                override fun onResponse(
                    call: Call<FaceScanRegistrationResponse?>,
                    response: Response<FaceScanRegistrationResponse?>
                ) {
                    Log.d("AuthToken", "FaceScan " + response.code().toString())
                    if (response.code() == 200 && response.isSuccessful) {
                        CommonUtils.dismissDialoge()
                        SharedPref.putFaceScanID(response.body()!!.data.scanId)
                        gender = ""
                        if (userType.equals("others", ignoreCase = true)) {
                            otherGender = ""
                            user = "self"
                        }
                        if (comingFrom.equals("mentalWellness", ignoreCase = true)) {
                            context.startActivity(
                                Intent(context, RespiratoryCheck::class.java)
                                    .putExtra("comingFrom", "metalFitness")
                            )
                        } else if (comingFrom.equals("respiratory", ignoreCase = true)) {
                            SharedPref.putRespiratoryCount(1)
                            context.startActivity(
                                Intent(context, RespiratoryCheck::class.java)
                                    .putExtra("comingFrom", "respiratory")
                            )
                        }
                    }
                }

                override fun onFailure(
                    call: Call<FaceScanRegistrationResponse?>,
                    t: Throwable
                ) {
                    CommonUtils.dismissDialoge()
                }
            })
        } catch (e: java.lang.Exception) {
            CommonUtils.dismissDialoge()
        }
    }


    override fun kywonClick(name: String) {
        if (name.equals("health score", true)) {
            activityTracker("A_DB_KYW_HS", context)
            getAnalysis()
        } else if (name.equals("Mental Wellness", true)) {
            activityTracker("A_DB_KYW_Mental Wellness", context)
            startActivity(
                Intent(context, RespiratoryCheck::class.java)
                    .putExtra("comingFrom", "metalFitness")
            )
        } else if (name.equals("Respiratory Health", true)) {
            activityTracker("A_DB_KYW_RH", context)
            startActivity(
                Intent(context, RespiratoryCheck::class.java).putExtra(
                    "comingFrom",
                    "respiratory"
                )
            )
        } else if (name.equals("DASS Score", true)) {
            NewDashboardHelper.activityName = null
            RedirectionMethod.getDassAnalysis(context)
        } else if (name.equals("Heart Age", true)) {
            NewDashboardHelper.activityName = null
            RedirectionMethod.getHeartAnalysis(context, "")
        } else if (name.equals("View All", true)) {
            activityTracker("A_DB_KYW_VAA ", context)
            startActivity(Intent(context, WellBeingActivity::class.java))
        } else if (name.equals("Immunity Risk Assessment", true)) {
            activityTracker("A_DB_KYW_IRA", context)
            NewDashboardHelper.activityName = null
            NewDashboardHelper.getIRAScore(context, "")
        }

    }

    fun showRewardsPopupForNudge(rewards: String, context: Context) {
        NewDashboardHelper.isRewards = true
        val alertBuilder = AlertDialog.Builder(context)
        val binding: LayoutNewPointsPopUpBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.layout_new_points_pop_up,
            null,
            false
        )
        alertBuilder.setView(binding.root)
        val alertDialog = alertBuilder.create()
        if (rewards.contains("First Login")) alertDialog.setCancelable(false) else {
            alertDialog.setCancelable(true)
            binding.scratchView.onFullReveal()
        }
        if (!alertDialog.isShowing) {
            alertDialog.show()
        }
        val title =
            rewards.split(";".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
        val message =
            rewards.split(";".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
        val points = message.replace("[^0-9]".toRegex(), "")
        alertDialog.setOnDismissListener {
            Log.d("call", "showRewardsPopupForNudge")
            //getDashboardData()
            if (NewDashboardHelper.tempPopupRewards != null && NewDashboardHelper.tempPopupRewards.size > 0) {
                NewDashboardHelper.removeRewardsPopup(
                    NewDashboardHelper.tempPopupRewards[0].activityId,
                    context,
                    this,
                    feedbackResponseData
                )
            } else if (NewDashboardHelper.isPositiveBtn) {
                NewDashboardHelper.isPositiveBtn = false
                val intent = Intent(context, RewardsActivity::class.java)
                intent.putExtra("currentIndex", 0)
                context.startActivity(intent)
            } else {
                showRewardsPopupDialogBox(context, this, feedbackResponseData)
            }
        }
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({ alertDialog.dismiss() }, 3000)
        binding.tvPoints.text = points
        binding.tvEventName.visibility = View.VISIBLE
        binding.tvEventName.text = title
        binding.ivClose.setOnClickListener { view -> alertDialog.dismiss() }
        binding.scratchView.onFullReveal()
        binding.btnNegative.setOnClickListener { view -> alertDialog.dismiss() }
        binding.btnPositive.setOnClickListener { view ->
            NewDashboardHelper.isPositiveBtn = true
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

    fun UploadData(activityName: String, countOrTime: Double, context: Context) {
        try {
            CommonUtils.showProgressDialige(context)
            NewDashboardHelper.waterIntake = countOrTime
            val request = AddReminderDataRequest(
                activityName,
                Math.round(countOrTime).toInt(),
                CommonUtils.todayDateInFormat("yyyy-MM-dd")
            )
            val apiInterfaceWyh = ApiClientWyh.getClient(CommonUtils.getBaseUrlForAPI(context))
                .create(ApiInterfaceWyh::class.java)
            val call = apiInterfaceWyh.addActivityData(SharedPref.getAuthToken(), request)
            Log.d("AuthToken", Gson().toJson(call.request()))
            call.enqueue(object : Callback<CommonSuccessResponse?> {
                override fun onResponse(
                    call: Call<CommonSuccessResponse?>,
                    response: Response<CommonSuccessResponse?>
                ) {
                    CommonUtils.dismissDialoge()
                    Log.d("response", Gson().toJson(response.body()))
                    Log.d("response", Gson().toJson(response.code()))
                    if (response.code() == 200 && response.body() != null && response.body()!!.isSuccess) {
                        SharedPref.putWaterIntake(false)
                        Analytics.logEvent(
                            context,
                            context.javaClass.name,
                            context.getString(R.string.log_reminder_success)
                        )
                        if (checkIsFromQuizqathon()) {
                            FetchQuizReward()
                        } else {
                            if (response.body()!!.rewards != null && response.body()!!.rewards.reward != null) {
                                NewDashboardHelper.popUpShowModels.add(
                                    PopUpShowModel(
                                        Constants.Rewards,
                                        response.body()!!.rewards.reward
                                    )
                                )
                            }
                            if (response.body()!!.rewards != null && response.body()!!.rewards.bonusRewards != null) {
                                NewDashboardHelper.popUpShowModels.add(
                                    PopUpShowModel(
                                        Constants.RewardsBounce, response.body()!!
                                            .rewards.bonusRewards
                                    )
                                )
                            }
                            if (response.body()!!.enGTokens != null && response.body()!!.enGTokens.tokens != null) {
                                NewDashboardHelper.popUpShowModels.add(
                                    PopUpShowModel(
                                        Constants.TokenStamp, response.body()!!
                                            .enGTokens.tokens
                                    )
                                )
                            }
                            if (response.body()!!.enGTokens != null && response.body()!!.enGTokens.bonusTokens != null) {
                                NewDashboardHelper.popUpShowModels.add(
                                    PopUpShowModel(
                                        Constants.TokenStampBounce, response.body()!!
                                            .enGTokens.bonusTokens
                                    )
                                )
                            }
                            if (response.body()!!.feedbackDetails != null && response.body()!!.feedbackDetails.customFeedbackRespModel != null && response.body()!!
                                    .feedbackDetails.feedbackModel != null && response.body()!!.feedbackDetails.starConfig != null
                            ) {
                                NewDashboardHelper.popUpShowModels.add(
                                    PopUpShowModel(
                                        Constants.FeedbackPOPUP,
                                        ""
                                    )
                                )
                                feedbackResponseData = response.body()!!.feedbackDetails
                            }
                            Toast.makeText(
                                context,
                                "Data Added Successfully!!!",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            if (NewDashboardHelper.popUpShowModels.size > 0) {
                                showRewardsPopupDialogBox(
                                    context,
                                    this@NewDashboardActivity,
                                    feedbackResponseData
                                )
                            } else {
                                if (activityName == Constants.WATER || activityName == Constants.MEDITATION) {
                                    fetchRewards(context)
                                }
                            }
                        }
                        Log.d("call", "upload data")
                        getDashboardData(NewDashboardHelper.binding, NewDashboardHelper.context)
                    } else {
                        Analytics.logEvent(
                            context,
                            context.javaClass.name,
                            getString(R.string.log_reminder_failed)
                        )
                    }
                }

                override fun onFailure(call: Call<CommonSuccessResponse?>, t: Throwable) {
                    CommonUtils.dismissDialoge()
                    Analytics.logEvent(
                        context,
                        context.javaClass.name,
                        getString(R.string.log_reminder_failed)
                    )
                }
            })
        } catch (e: java.lang.Exception) {
            CommonUtils.dismissDialoge()
            e.printStackTrace()
        }
    }

    private fun fetchRewards(context: Context) {
        //CommonUtils.showProgressDialige(context);
        val request = FetchRewardsRequest("waterintake")
        val apiInterfaceWyh =
            ApiClientWyh.getClient(CommonUtils.getBaseUrlForAPI(context)).create(
                ApiInterfaceWyh::class.java
            )
        val call = apiInterfaceWyh.fetchRewards(SharedPref.getAuthToken(), request)
        call.enqueue(object : Callback<FetchRewardsResponse?> {
            override fun onResponse(
                call: Call<FetchRewardsResponse?>,
                response: Response<FetchRewardsResponse?>
            ) {
                //CommonUtils.dismissDialoge();
                if (response.body() != null && response.code() == 200) {
                    if (response.body()!!.data != null) {
                        if (response.body()!!.data.size > 0) {
                            showRewardsPopupNew(
                                response.body()!!.data[0].popupMessage,
                                context,
                                this@NewDashboardActivity,
                                feedbackResponseData
                            )
                        }
                    }
                }
            }

            override fun onFailure(call: Call<FetchRewardsResponse?>, t: Throwable) {
                if (progressDialog != null && progressDialog.isShowing) progressDialog.dismiss()
            }
        })
    }

    fun UploadActivityData(activityName: String, countOrTime: Double) {
        if (progressDialog != null && !progressDialog.isShowing) progressDialog.show()
        val request = AddReminderDataRequest(
            activityName,
            Math.round(countOrTime).toInt(),
            CommonUtils.todayDateInFormat("yyyy-MM-dd")
        )
        val call = apiInterfaceWyh.addActivityData(SharedPref.getAuthToken(), request)
        call.enqueue(object : Callback<CommonSuccessResponse?> {
            override fun onResponse(
                call: Call<CommonSuccessResponse?>,
                response: Response<CommonSuccessResponse?>
            ) {
                if (progressDialog != null && progressDialog.isShowing) progressDialog.dismiss()
                if (response.code() == 200 && response.body() != null && response.body()!!.isSuccess) {
                    Analytics.logEvent(
                        context,
                        context.javaClass.name,
                        getString(R.string.zen_zone_update_success)
                    )

                    if (checkIsFromQuizqathon()) {
                        FetchQuizReward()
                    } else {
                        if (response.body()!!.rewards != null && response.body()!!.rewards.reward != null) {
                            NewDashboardHelper.popUpShowModels.add(
                                PopUpShowModel(
                                    Constants.Rewards,
                                    response.body()!!.rewards.reward
                                )
                            )
                        }
                        if (response.body()!!.rewards != null && response.body()!!.rewards.bonusRewards != null) {
                            NewDashboardHelper.popUpShowModels.add(
                                PopUpShowModel(
                                    Constants.RewardsBounce,
                                    response.body()!!.rewards.bonusRewards
                                )
                            )
                        }
                        if (response.body()!!.enGTokens != null && response.body()!!.enGTokens.tokens != null) {
                            NewDashboardHelper.popUpShowModels.add(
                                PopUpShowModel(
                                    Constants.TokenStamp,
                                    response.body()!!.enGTokens.tokens
                                )
                            )
                        }
                        if (response.body()!!.enGTokens != null && response.body()!!.enGTokens.bonusTokens != null) {
                            NewDashboardHelper.popUpShowModels.add(
                                PopUpShowModel(
                                    Constants.TokenStampBounce,
                                    response.body()!!.enGTokens.bonusTokens
                                )
                            )
                        }
                        Toast.makeText(context, "Data Added Successfully!!!", Toast.LENGTH_SHORT)
                            .show()
                        if (NewDashboardHelper.popUpShowModels.size > 0) {
                            showRewardsPopupDialogBox(
                                context,
                                this@NewDashboardActivity,
                                feedbackResponseData
                            )
                        }
                    }
                    Log.d("call", "UploadActivityData")
                    getDashboardData(NewDashboardHelper.binding, NewDashboardHelper.context)
                    //getDashboardDetails("", true);
                } else {
                    Analytics.logEvent(
                        context,
                        context.javaClass.name,
                        getString(R.string.zen_zone_update_failed)
                    )
                }
            }

            override fun onFailure(call: Call<CommonSuccessResponse?>, t: Throwable) {
                if (progressDialog != null && progressDialog.isShowing) progressDialog.dismiss()
                Analytics.logEvent(
                    context,
                    context.javaClass.name,
                    getString(R.string.zen_zone_update_failed)
                )
            }
        })
    }

    fun showRewardsPopupNew(
        rewards: String,
        context: Context,
        scratchListener: ScratchListener,
        feedbackResponseData: FeedbackResponseData
    ) {
        NewDashboardHelper.isRewards = true
        val alertBuilder = AlertDialog.Builder(context)
        val binding: LayoutNewPointsPopUpBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.layout_new_points_pop_up,
            null,
            false
        )
        alertBuilder.setView(binding.root)
        val alertDialog = alertBuilder.create()
        if (rewards.contains("First Login")) alertDialog.setCancelable(false) else {
            alertDialog.setCancelable(true)
            binding.scratchView.onFullReveal()
        }
        if (!alertDialog.isShowing) {
            alertDialog.show()
        }
        val title =
            rewards.split(";".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
        val message =
            rewards.split(";".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
        val points = message.replace("[^0-9]".toRegex(), "")

        alertDialog.setOnDismissListener {
            Log.d("AuthToken", "PopDismiss")
            getDashboardData(NewDashboardHelper.binding, NewDashboardHelper.context)
            if (NewDashboardHelper.tempPopupRewards != null && NewDashboardHelper.tempPopupRewards.size > 0) {
                NewDashboardHelper.removeRewardsPopup(
                    NewDashboardHelper.tempPopupRewards[0].activityId,
                    context,
                    scratchListener,
                    feedbackResponseData
                )
            } else if (NewDashboardHelper.isPositiveBtn) {
                NewDashboardHelper.isPositiveBtn = false
                val intent = Intent(context, RewardsActivity::class.java)
                intent.putExtra("currentIndex", 0)
                context.startActivity(intent)
            } else {
                showRewardsPopupDialogBox(context, scratchListener, feedbackResponseData)
            }
        }


        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({ alertDialog.dismiss() }, 3000)
        binding.tvPoints.text = points
        binding.tvEventName.visibility = View.VISIBLE
        binding.tvEventName.text = title
        binding.ivClose.setOnClickListener { view -> alertDialog.dismiss() }
        binding.scratchView.onFullReveal()
        binding.btnNegative.setOnClickListener { view -> alertDialog.dismiss() }
        binding.btnPositive.setOnClickListener { view ->
            NewDashboardHelper.isPositiveBtn = true
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

    private fun refreshAuthToken(binding: ActivityNewDashboardBinding, context: Context) {
        if (progressDialog != null && !progressDialog.isShowing) progressDialog.show()
        val deviceModel = Build.BRAND + " " + Build.MODEL
        val osVersion = Build.VERSION.RELEASE + "(" + Build.VERSION.SDK_INT + ")"
        val appVersion = SDKConstants.appVersionName
        isRefreshTokenGenerated = true
        val request = RefreshTokenRequest(deviceModel, osVersion, appVersion)
        val call = apiInterfaceWyh.refreshToken(SharedPref.getAuthToken(), request)
        Log.d("AuthToken", SharedPref.getAuthToken() + "URL: " + call.request().url())
        call.enqueue(object : Callback<RefreshTokenResponse?> {
            override fun onResponse(
                call: Call<RefreshTokenResponse?>,
                response: Response<RefreshTokenResponse?>
            ) {
                if (progressDialog != null && progressDialog.isShowing) progressDialog.dismiss()
                if (response.code() == 200 && response.body() != null && response.body()!!.isSuccess && response.body()!!.data.authToken != null && response.body()!!.data.authToken != "") {
                    isRefreshTokenGenerated = false
                    Analytics.logEvent(
                        context,
                        this@NewDashboardActivity.context.javaClass.name,
                        getString(R.string.refresh_token_success)
                    )
                    SharedPref.putAuthToken("Bearer " + response.body()!!.data.authToken)
                    SharedPreference.putAuthToken("Bearer " + response.body()!!.data.authToken)

                    Log.d("call", "refreshAuthToken")
                    getUserDetails(binding, context)
                    getDashboardData(binding, context)
                } else {
                    Analytics.logEvent(
                        this@NewDashboardActivity.context,
                        this@NewDashboardActivity.context.javaClass.name,
                        getString(R.string.refresh_token_failed)
                    )
                    /*Toast.makeText(this@NewDashboardActivity.context, resources.getString(R.string.session_time_out), Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@NewDashboardActivity.context, MobileNumberActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    SharedPref.clearSharedPref()
                    finishAffinity()*/
                    logOut(context)
                }
            }

            override fun onFailure(call: Call<RefreshTokenResponse?>, t: Throwable) {
                if (progressDialog != null && progressDialog.isShowing) progressDialog.dismiss()
                Analytics.logEvent(
                    this@NewDashboardActivity.context,
                    this@NewDashboardActivity.context.javaClass.name,
                    getString(R.string.refresh_token_failed)
                )
                /*Toast.makeText(this@NewDashboardActivity.context, resources.getString(R.string.session_time_out), Toast.LENGTH_SHORT).show()
                val intent = Intent(this@NewDashboardActivity.context, MobileNumberActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                SharedPref.clearSharedPref()
                finishAffinity()*/
                logOut(context)
            }
        })
    }

    fun downloadFaceScanReport(type: String, context: Context) {
        try {
            CommonUtils.showProgressDialige(context)
            val apiInterface = RetrofitHandler.apiInterface()
            apiInterface.faceScanReport(SharedPref.getAuthToken())
                .enqueue(object : Callback<DownloadFaceScanResponse?> {
                    override fun onResponse(
                        call: Call<DownloadFaceScanResponse?>,
                        response: Response<DownloadFaceScanResponse?>
                    ) {
                        CommonUtils.dismissDialoge()
                        if (response.code() == 200 && response.isSuccessful && response.body() != null && response.body()!!.data != null) {
                            RedirectionMethod.showSharePopup(
                                context,
                                response.body()!!.data.fileDownloadName
                            )
                        }
                    }

                    override fun onFailure(
                        call: Call<DownloadFaceScanResponse?>,
                        t: Throwable
                    ) {
                        CommonUtils.dismissDialoge()
                    }
                })
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    override fun onClickPerform(challengeName: String, position: Int) {
        try {
            activityTracker("A_DB_Challenges_${challengesList[position].ChallengeId}", context)
            if (!challengesList.elementAt(position).ChallengeType.equals("Tribe", true)) {
                if (challengesList.elementAt(position).IsUserEnrolled == "1") {
                    val intent =
                        Intent(this@NewDashboardActivity, ChallengeDetailActivity::class.java)
                    intent.putExtra("challengeID", challengeName)
                    intent.flags =
                        Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                    intent.putExtra("position", position.toString())
                    startActivity(intent)
                }
            }
        } catch (e: Exception) {
            e.toString()
        }

    }

    override fun joinNowClick(
        data: challengesData, challengeID: String, position: Int, challengeType: String,
        communityID: Int,
        recyclerView: RecyclerView
    ) {
        if (challengeType.equals("tribe", true)) {
        } else {
            joinChallenge(challengeID, communityID, challengeType, this)
        }

    }

    fun joinChallenge(
        challenegID: String,
        communityID: Int,
        challengeType: String,
        context: Context
    ) {
        try {
            CommonUtils.showProgressDialige(this)
            val commonRequest: JoinChallengeRequest = JoinChallengeRequest(challenegID, communityID)
            val apiInterface =
                RetrofitHandler.getRetrofitInstance().create(APIInterface::class.java)
            apiInterface.joinChallenge(SharedPref.getAuthToken(), commonRequest)
                .enqueue(object : Callback<JoinChallengeResponse> {
                    override fun onResponse(
                        call: Call<JoinChallengeResponse>,
                        response: Response<JoinChallengeResponse>
                    ) {
                        response.body()?.success.let {
                            if (response.body()!!.success) {
                                if (challengeType.equals("tribe", true)) {
                                    if (SharedPref.getChallengeStarted()) {
                                        CommonUtils.dismissDialoge()
                                        TribeChallengeHelper.showConcernInfoLayout(
                                            context,
                                            challenegID
                                        )
                                    } else {
                                        CommonUtils.dismissDialoge()
                                        getChallengesForTribe(context)

                                    }
                                } else {
                                    CommonUtils.dismissDialoge()
                                    getDashboardData(NewDashboardHelper.binding, context)

                                }
                            }
                        }
                    }

                    override fun onFailure(call: Call<JoinChallengeResponse>, t: Throwable) {
                        APILogs.sendLogs(
                            call.request().url().toString(),
                            t.message!!,
                            "onFailure",
                            context
                        )
                        CommonUtils.dismissDialoge()
                    }
                })
        } catch (e: Exception) {
            APILogs.sendLogs(Constants.EXCEPTION, e.message!!, "Exception", context)
            CommonUtils.dismissDialoge()
            e.toString()
        }
    }


    fun getChallengeDetails(context: Context, ChallengeID: String, data: challengesData) {
        if (data.IsUserEnrolled == "1" && data.IsStarted == 1) {
            val intent = Intent(context, NewTribeChallengeDashboard::class.java)
            intent.putExtra("challengeID", ChallengeID)
            context.startActivity(intent)
        } else {
            getChallengeApi(context, ChallengeID, false, data.IsUserEnrolled, data.IsStarted)
        }
    }

    fun getChallengeApi(
        context: Context,
        ChallengeID: String,
        cameFromInfo: Boolean,
        isUserEnrolled: String,
        isStarted: Int
    ) {
        try {
            CommonUtils.showProgressDialige(context)
            val commonRequest = CommonRequest(ChallengeID)
            val apiInterface = RetrofitHandler.apiInterface()
            apiInterface.challengeActivity(SharedPref.getAuthToken(), commonRequest)
                .enqueue(object : Callback<ChallengeActivityResponse> {
                    override fun onResponse(
                        call: Call<ChallengeActivityResponse>,
                        response: Response<ChallengeActivityResponse>
                    ) {
                        CommonUtils.dismissDialoge()
                        try {
                            if (response.code() == 200 && response.isSuccessful) {
                                if (response.body() != null) {
                                    if (cameFromInfo) {
                                        TribeChallengeHelper.challengeDialog(
                                            context,
                                            ChallengeID,
                                            response.body()!!.data.challengeActivityDetails[0],
                                            cameFromInfo,
                                            isUserEnrolled,
                                            NewDashboardHelper.binding.challengesRv,
                                            "dashboard"
                                        )
                                    } else {
                                        if (SharedPref.getChallengeStarted() && isUserEnrolled.equals(
                                                "1"
                                            ) && isStarted == 0
                                        ) {
                                            TribeChallengeHelper.showConcernInfoLayout(
                                                context,
                                                ChallengeID
                                            )
                                        } else {
                                            TribeChallengeHelper.challengeDialog(
                                                context,
                                                ChallengeID,
                                                response.body()!!.data.challengeActivityDetails[0],
                                                cameFromInfo,
                                                isUserEnrolled,
                                                NewDashboardHelper.binding.challengesRv,
                                                "dashboard"
                                            )
                                        }
                                    }


                                }
                            }
                        } catch (e: Exception) {
                            e.toString()
                        }
                    }

                    override fun onFailure(
                        call: Call<ChallengeActivityResponse>,
                        t: Throwable
                    ) {
                        CommonUtils.dismissDialoge()
                    }

                })
        } catch (e: Exception) {
            CommonUtils.dismissDialoge()
            e.toString()
        }
    }


    fun getChallengesForTribe(context: Context) {
        try {
            CommonUtils.showProgressDialige(context)
            val apiInterface =
                RetrofitHandler.getRetrofitInstance().create(APIInterface::class.java)
            apiInterface.getChallenges(SharedPref.getAuthToken())
                .enqueue(object : Callback<GetChallengesResponse> {
                    override fun onResponse(
                        call: Call<GetChallengesResponse>,
                        response: Response<GetChallengesResponse>
                    ) {
                        if (response.body() != null) {
                            CommonUtils.dismissDialoge()
                            if (response.code() == 200 && response.body()!!.success) {
                                getDashboardData(NewDashboardHelper.binding, context)
                            }
                        }
                    }

                    override fun onFailure(call: Call<GetChallengesResponse>, t: Throwable) {
                        CommonUtils.showProgressDialige(context)

                    }

                })


        } catch (e: Exception) {
            CommonUtils.showProgressDialige(context)
            e.toString()
        }
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
                                NewDashboardHelper.voucherList =
                                    response.body()!!.data.voucherList
                            }

                            if (response.body()!!.data.rewardsSummary.availableStamps != null) {
                                NewDashboardHelper.availableStamps =
                                    response.body()!!.data.rewardsSummary.availableStamps
                            }

                            if (response.body()!!.data.rewardsSummary.stampCount != null) {
                                NewDashboardHelper.stampCount =
                                    response.body()!!.data.rewardsSummary.stampCount
                            }

                            val intent =
                                Intent(this@NewDashboardActivity, OtherActivity::class.java)
                                    .putExtra(
                                        "stamp",
                                        response.body()!!.data.rewardsSummary.stampCount
                                            ?: ""
                                    )
                                    .putExtra(
                                        "voucher",
                                        response.body()!!.data.rewardsSummary.voucherCount
                                            ?: ""
                                    )
                                    .putExtra(
                                        "badges",
                                        response.body()!!.data.rewardsSummary.badgeCount
                                            ?: ""
                                    )
                            startActivity(intent)

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


    fun addBookmark(articleCode: String, isBookmark: Boolean, context: Context) {
        val progressDialog = ProgressDialog(context)
        val apiInterfaceWyh = ApiClientWyh.getClient(CommonUtils.getBaseUrlForAPI(context))
            .create(ApiInterfaceWyh::class.java)
        if (progressDialog != null && !progressDialog.isShowing) progressDialog.show()
        val request = AddBookmarkRequest(articleCode, isBookmark)
        val call = apiInterfaceWyh.addBookMark(SharedPref.getAuthToken(), request)
        call.enqueue(object : Callback<AddBookmarkResponse?> {
            override fun onResponse(
                call: Call<AddBookmarkResponse?>,
                response: Response<AddBookmarkResponse?>
            ) {
                if (progressDialog != null && progressDialog.isShowing) progressDialog.dismiss()
                if (response.code() == 200 && response.body() != null && response.body()!!.success) {
                    Analytics.logEvent(
                        context,
                        context.javaClass.name,
                        context.resources.getString(R.string.health_hacks_add_bookmark_success)
                    )
                    Log.d("BookMark", Gson().toJson(response.body()))
                    if (isBookmark) {
                        Toast.makeText(
                            context,
                            "Successfully added to Bookmark",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(context, "Remove from Bookmark", Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {
                    Analytics.logEvent(
                        context,
                        context.javaClass.name,
                        context.resources.getString(R.string.health_hacks_add_bookmark_failed)
                    )
                    //Toast.makeText(context, context.resources.getString(R.string.error_string), Toast.LENGTH_SHORT).show()
                }
                getDashboardData(NewDashboardHelper.binding, NewDashboardHelper.context)
            }

            override fun onFailure(call: Call<AddBookmarkResponse?>, t: Throwable) {
                if (progressDialog != null && progressDialog.isShowing) progressDialog.dismiss()
                Analytics.logEvent(
                    context,
                    context.javaClass.name,
                    context.resources.getString(R.string.health_hacks_add_bookmark_failed)
                )
                //Toast.makeText(context, context.resources.getString(R.string.error_string), Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun clickToBookMark(articleCode: String, isBookmark: Boolean) {
        activityTracker("A_DB_HH_Book Mark", context)
        addBookmark(articleCode, isBookmark, NewDashboardHelper.context)
    }


    public fun showPopUpZenZone(context1: Context, currentTime: Double) {
        val alertBuilder = AlertDialog.Builder(context1)
        val zenZoneBinding: ZenZoneLayoutBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context1),
            R.layout.zen_zone_layout, null, false
        )
        alertBuilder.setView(zenZoneBinding.root)
        val alertDialog = alertBuilder.create()
        alertDialog.setCancelable(true)
        alertDialog.setOnDismissListener { dialogInterface: DialogInterface? -> alertDialog.dismiss() }
        zenZoneBinding.btnTen.setOnClickListener { v -> zenZoneBinding.etMins.setText("10") }
        zenZoneBinding.btnTwenty.setOnClickListener { v -> zenZoneBinding.etMins.setText("20") }
        zenZoneBinding.btnForty.setOnClickListener { v -> zenZoneBinding.etMins.setText("40") }
        zenZoneBinding.btnClosed.setOnClickListener { view -> alertDialog.dismiss() }
        zenZoneBinding.btnAdd.setOnClickListener { view ->
            if (!zenZoneBinding.etMins.text.toString().isEmpty()) {
                val meditationMinutes = zenZoneBinding.etMins.text.toString().toDouble().toInt()
                if (meditationMinutes > 0) {
                    alertDialog.dismiss()
                    UploadActivityData(Constants.MEDITATION, meditationMinutes + currentTime)
                } else {
                    Toast.makeText(context, "Please add time", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context1, "Please add time", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        zenZoneBinding.btnViewMore.setOnClickListener {
            alertDialog.dismiss()
            val intent = Intent(context1, TrendsActivity::class.java)
            intent.putExtra("activityType", Constants.MEDITATION)
            context1.startActivity(intent)
        }
        if (!alertDialog.isShowing) alertDialog.show()
        val displayRectangle = Rect()
        val window = window
        window.decorView.getWindowVisibleDisplayFrame(displayRectangle)
        alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.window!!.setLayout(
            (displayRectangle.width() *
                    0.8f).toInt(), RelativeLayout.LayoutParams.WRAP_CONTENT
        )
    }

    private fun sendFcmTokenToServer() {
        val addFCMTokenRequest = AddFCMTokenRequest(
            SharedPref.getPushNotificationToken(),
            RSAEncryption.rsaEncrypt(SharedPref.getDecryptMobileNo())
        )
        val apiInterface = RetrofitHandler.apiInterface()
        apiInterface.sendFCMToken(SharedPref.getAuthToken(), addFCMTokenRequest)
            .enqueue(object : Callback<CommonSuccessResponse?> {
                override fun onResponse(
                    call: Call<CommonSuccessResponse?>,
                    response: Response<CommonSuccessResponse?>
                ) {
                    if (progressDialog != null && progressDialog.isShowing) progressDialog.dismiss()
                    if (response.body() != null && response.code() == 200) {
                        Analytics.logEvent(
                            context,
                            context.javaClass.name,
                            getString(R.string.add_fcm_token_success)
                        )
                        if (response.body()!!.isSuccess) {
                            subscribeToTopics()
                            //Toast.makeText(DashboardActivity.this, "Success", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Analytics.logEvent(
                            context,
                            context.javaClass.name,
                            getString(R.string.add_fcm_token_failed)
                        )
                    }
                }

                override fun onFailure(call: Call<CommonSuccessResponse?>, t: Throwable) {
                    if (progressDialog != null && progressDialog.isShowing) progressDialog.dismiss()
                    Analytics.logEvent(
                        context, context.javaClass.name, getString(R.string.add_fcm_token_failed)
                    )
                }
            })
    }

    private fun subscribeToTopics() {

    }

    private fun getSpinRewards(context: Context) {
        try {
            val assignRewardsRequest =
                AssignRewardsRequest(SharedPref.getTempUUID(), SharedPref.getUuid())
            apiInterface.assignRewards(SharedPref.getAuthToken(), assignRewardsRequest)
                .enqueue(object : Callback<AssignRewardsResponse> {
                    override fun onResponse(
                        call: Call<AssignRewardsResponse>,
                        response: Response<AssignRewardsResponse>
                    ) {
                        Log.d("AuthToken", "getSpinRewards code ${response.code()}")
                        if (response.body() != null && response.code() == 200 && response.isSuccessful) {
                            NewDashboardHelper.isShowUserPopup = false
                            if (rewardsModel != null && rewardsModel.reward != null && rewardsModel.reward.contains(
                                    "First Login"
                                )
                            ) {
                                NewDashboardHelper.showFirstLoginPopup = true
                            }
                            Log.d("AuthToken", "getSpinRewards ${Gson().toJson(response.body())}")
                            if (response.body()!!.data != null) {
                                Log.d(
                                    "AuthToken",
                                    "rewardType ${response.body()!!.data.rewardType}"
                                )
                                if (response.body()!!.data.rewardType != null) {
                                    if (response.body()!!.data.isrewardawarded == 1) {
                                        if (response.body()!!.data.rewardType.equals(
                                                "Points",
                                                true
                                            )
                                        ) {
                                            APILogs.activityTracker("SPIN_POINT_REWARD", context)
                                            Log.d(
                                                "AuthToken",
                                                "Happy You Points " + response.body()!!.data.rewardTitle
                                            )
                                            NudgeDialogue.showSpinnerPointsPopup(
                                                response.body()!!.data.rewardValue,
                                                response.body()!!.data.rewardTitle,
                                                context,
                                                rewardsModel,
                                                "Dashboard",
                                                response.body()!!.data.rewardDescription,
                                                response.body()!!.data.rewardHeader1,
                                                response.body()!!.data.rewardHeader2
                                            )
                                        } else if (response.body()!!.data.rewardType.equals(
                                                "Offers",
                                                true
                                            )
                                        ) {
                                            APILogs.activityTracker("SPIN_OFFER_REWARD", context)
                                            NudgeDialogue.showSpinnerOffer(
                                                context,
                                                response.body()!!.data.partnerLogo,
                                                response.body()!!.data.rewardDescription,
                                                response.body()!!.data.partnerName,
                                                response.body()!!.data.partnerUrl,
                                                rewardsModel,
                                                "Dashboard",
                                                response.body()!!.data.expiryInHours,
                                                response.body()!!.data.rewardTitle,
                                                response.body()!!.data.rewardHeader1,
                                                response.body()!!.data.rewardHeader2
                                            )
                                        } else if (response.body()!!.data.rewardType.equals(
                                                "Voucher",
                                                true
                                            )
                                        ) {
                                            APILogs.activityTracker("SPIN_VOUCHER_REWARD", context)
                                            NudgeDialogue.showSpinnerVoucher(
                                                context,
                                                response.body()!!.data.rewardLogo,
                                                response.body()!!.data.couponCode,
                                                response.body()!!.data.rewardDescription,
                                                response.body()!!.data.rewardType,
                                                response.body()!!.data.rewardTitle,
                                                response.body()!!.data.rewardValue,
                                                rewardsModel,
                                                response.body()!!.data.expiryInHours,
                                                "Dashboard",
                                                response.body()!!.data.rewardHeader1,
                                                response.body()!!.data.rewardHeader2
                                            )
                                        }
                                    } else if (response.body()!!.data.isrewardawarded == -1) {
                                        APILogs.activityTracker("SPIN_REWARD_NOT_ELIGIBLE", context)
                                        NudgeDialogue.spinnerCancelDialog(
                                            context,
                                            rewardsModel,
                                            context.resources.getString(R.string.rewards_not_eligible),
                                            "Dashboard_Not_Eligible",
                                            response.body()!!.data.rewardType
                                        )
                                    }
                                } else if (response.body()!!.data.isrewardawarded == 0) {
                                    APILogs.activityTracker("SPIN_REWARD_EXPIRED", context)
                                    NudgeDialogue.spinnerCancelDialog(
                                        context,
                                        rewardsModel,
                                        context.resources.getString(R.string.rewards_expired),
                                        "Dashboard_Expired",
                                        ""
                                    )
                                }
                            }

                        }
                    }

                    override fun onFailure(call: Call<AssignRewardsResponse>, t: Throwable) {

                    }

                })
        } catch (e: Exception) {
            e.toString()
        }
    }


    private fun logApiSaveResponse() {
        try {
            val apiSessionLogRequest =
                ApiSessionLogRequest(
                    SharedPref.getSessionStartTime(),
                    SharedPref.getSessionEndTime()
                )
            apiInterface.SaveAppSessionLog(SharedPref.getAuthToken(), apiSessionLogRequest)
                .enqueue(object : Callback<CommonResponse> {
                    override fun onResponse(
                        call: Call<CommonResponse>,
                        response: Response<CommonResponse>
                    ) {
                        Log.d("AuthToken", "logSessionApi code ${response.code()}")
                        if (response.body() != null && response.code() == 200 && response.isSuccessful) {

                        }

                    }

                    override fun onFailure(call: Call<CommonResponse>, t: Throwable) {

                    }

                })
        } catch (e: Exception) {
            e.toString()
        }
    }


    private fun getQuadrants() {
        try {
            CommonUtils.showProgressDialige(this)
            apiInterface.quadrant.enqueue(object : Callback<GetQuadrantsResponse?> {
                override fun onResponse(
                    call: Call<GetQuadrantsResponse?>,
                    response: Response<GetQuadrantsResponse?>
                ) {
                    if (response.body() != null && response.code() == 200) {
                        if (response.body()!!.getData() != null && !response.body()!!.getData()
                                .getQuadrantData().isEmpty()
                        ) {
                            CommonUtils.wheelItems.clear()
                            for (i in response.body()!!.getData().getQuadrantData().indices) {
                                val imageBitmap = arrayOf<Bitmap?>(null)
                                DownloadImage { bitmap: Bitmap? ->
                                    if (bitmap != null) {
                                        imageBitmap[0] = bitmap
                                        val scaledBitmap = Bitmap.createScaledBitmap(
                                            imageBitmap[0]!!,
                                            80, 80,
                                            true
                                        )
                                        val wheelItem = WheelItem(
                                            Color.parseColor(
                                                response.body()!!.getData()
                                                    .getQuadrantData()[i].getQuadrantColor()
                                            ),
                                            scaledBitmap,
                                            response.body()!!.getData()
                                                .getQuadrantData()[i].getRewardName()
                                        )
                                        CommonUtils.wheelItems.add(wheelItem)
                                        Log.d("AuthToken", CommonUtils.wheelItems.size.toString())
                                        if (CommonUtils.wheelItems.size == response.body()!!
                                                .getData().getQuadrantData().size
                                        ) {
                                            CommonUtils.dismissDialoge()
                                            val intent =
                                                Intent(context, DashboardWheelActivity::class.java)
                                            startActivity(intent)
                                        }
                                    } else {
                                        CommonUtils.dismissDialoge()
                                        //Log.e("DownloadError", "Failed to download image.");
                                    }
                                }.execute(
                                    response.body()!!.getData().getQuadrantData()[i].getRewardIcon()
                                )
                            }
                        } else {

                        }
                    } else {

                    }
                }

                override fun onFailure(call: Call<GetQuadrantsResponse?>, t: Throwable) {
                    CommonUtils.dismissDialoge()

                }
            })
        } catch (e: java.lang.Exception) {
            CommonUtils.dismissDialoge()
            e.printStackTrace()
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        SharedPref.putSessionEndTime(getCurrentTimestamp())
        logApiSaveResponse()
    }

    private fun getCurrentTimestamp(): String {
        val isoFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        return isoFormat.format(Date())
    }

    override fun onDialogDismiss() {
        cancelDialog()
        //getUserDetails(binding, context)
    }

    fun SaveBannerRegistration(
        RegistrationID: String,
        AnswerJson: String,
        redirectionModel: RedirectionModel,
        isRegistered: Boolean = false,
    ) {
        CommonUtils.dismissDialoge()
        CommonUtils.showProgressDialige(context)
        val request: SaveRegistrationAnswer = SaveRegistrationAnswer(RegistrationID, AnswerJson)
        val call: Call<CommonSuccessResponse> =
            apiInterfaceWyh.SaveBannerRegistration(SharedPref.getAuthToken(), request)
        call.enqueue(object : Callback<CommonSuccessResponse?> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(
                call: Call<CommonSuccessResponse?>,
                response: Response<CommonSuccessResponse?>
            ) {
                if (response.code() == 200 && response.body() != null) {
                    Toast.makeText(
                        context,
                        response.body()!!.msg,
                        Toast.LENGTH_LONG
                    ).show()
                    CommonUtils.dismissDialoge()
                    if (redirectionModel != null && redirectionModel.redirectionKey != null && redirectionModel.redirectionKey.isNotEmpty()) {
                        if (redirectionModel.redirectionKey.equals(
                                "fileshare",
                                ignoreCase = true
                            ) && !isRegistered
                        ) {
                            getUserDetails(binding, context)
                            getDashboardData(binding, context)
                        } else if (redirectionModel.redirectionKey.equals(
                                "fileshare",
                                ignoreCase = true
                            ) && isRegistered
                        ) {
                            redirectionScreen(redirectionModel, context)
                        } else {
                            redirectionScreen(redirectionModel, context)
                        }
                    } else {
                        getUserDetails(binding, context)
                        getDashboardData(binding, context)
                    }
                }
            }

            override fun onFailure(call: Call<CommonSuccessResponse?>, t: Throwable) {
                CommonUtils.dismissDialoge()
                Toast.makeText(
                    context,
                    "Something went wrong! Please try again later.",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        })
    }

    fun setSpinWheelData() {


    }

    private fun GetActiveQuizathon() {
        try {
            CommonUtils.dismissDialoge()
            CommonUtils.showProgressDialige(context)
            apiInterface.GetActiveQuizathon(SharedPref.getAuthToken())
                .enqueue(object : Callback<QuizathonResponseModel?> {
                    override fun onResponse(
                        call: Call<QuizathonResponseModel?>,
                        response: Response<QuizathonResponseModel?>
                    ) {
                        CommonUtils.dismissDialoge()
                        Log.d("Assign Response", Gson().toJson(response.body()))
                        val getQuizQuestionsList: MutableList<QuizathonModel> = ArrayList()


                        /* quizathonModelPointBurn?.let { callRegisterAfterPointBurn(it) }
                         quizathonModelRegister?.let { callRegisterAfterPointBurn(it) }*/
                        if (SharedPref.getShowSpin()) {
                            val quizathonModel = QuizathonModel()
                            quizathonModel.quizTitle = "Spin and Win"
                            getQuizQuestionsList.add(quizathonModel)
                        }
                        if (response.body() != null && response.code() == 200 && response.body()!!.quizathonData != null && response.body()!!.quizathonData.quizathonModels != null) {
                            getQuizQuestionsList.addAll(response.body()!!.quizathonData!!.quizathonModels)
                        }
                        var maxLength = getQuizQuestionsList.size
                        if (getQuizQuestionsList.size > 6) {
                            maxLength = 6
                        }
                        val gridPlaywinAdapter = GridPlaywinAdapter(
                            null,
                            getQuizQuestionsList.subList(0, maxLength),
                            null,
                            "Quizathon", { item, type ->
                                if (item is QuizathonModel) {
                                    var quizathonModel: QuizathonModel = item
                                    if (quizathonModel.quizTitle.equals(
                                            "Spin and Win",
                                            ignoreCase = true
                                        )
                                    ) {
                                        startActivity(
                                            Intent(
                                                this@NewDashboardActivity,
                                                SpinWheelRewardsActivity::class.java
                                            )
                                        )
                                    } else {
                                        if (quizathonModel.getAnswerJson() != null && !quizathonModel.getAnswerJson()
                                                .isEmpty() && quizathonModel.isQuizCompleted
                                        ) {
                                            val intent = Intent(
                                                context,
                                                QuizathonScoreActivity::class.java
                                            )
                                            intent.putExtra(
                                                "data",
                                                quizathonModel.getAnswerJson()
                                            )
                                            intent.putExtra(
                                                "your_score",
                                                quizathonModel.getUserScore()
                                            )
                                            intent.putExtra(
                                                "total_score",
                                                quizathonModel.getTotalScore()
                                            )
                                            intent.putExtra(
                                                "CategoryName",
                                                quizathonModel.getQuizTitle()
                                            )
                                            intent.putExtra(
                                                "Category", quizathonModel.getQuizDesc()
                                            )
                                            intent.putExtra("qid", quizathonModel.getQuizId())
                                            intent.putExtra("comingFrom", "quiz")
                                            intent.putExtra("rewardText", "")
                                            intent.putExtra(
                                                "rewardDate",
                                                quizathonModel.getRewardDate()
                                            )
                                            intent.putExtra(
                                                "isRetakeAvailable",
                                                quizathonModel.getRetakeAvailable()
                                            )
                                            intent.putExtra(
                                                "RetakeId",
                                                quizathonModel.retakeId
                                            )
                                            intent.putExtra(
                                                "RetakeId",
                                                quizathonModel.retakeId
                                            )
                                            intent.putExtra(
                                                "hasSufficientBalance",
                                                quizathonModel.isHasSufficientBalance()
                                            )
                                            intent.putExtra(
                                                "retakePointstoBurn",
                                                quizathonModel.getRetakePointstoBurn()
                                            )
                                            intent.putExtra(
                                                "showNextGameButton",
                                                quizathonModel.showNextGameButton
                                            )
                                            intent.putExtra(
                                                "quizdata",
                                                quizathonModel.model
                                            )
                                            intent.putExtra(
                                                "nextGameRedirectTo",
                                                quizathonModel.redirectTo
                                            )
                                            intent.putExtra("quizModel", quizathonModel)
                                            intent.putExtra(
                                                "withActivity",
                                                quizathonModel.getShowActivity()
                                            )
                                            context.startActivity(intent)
                                        } else if (!quizathonModel.getIsUserRegistered() && quizathonModel.getRegistrationQuestions() != null && !quizathonModel.getRegistrationQuestions()
                                                .isEmpty() && quizathonModel.isRegistrationRequired && !quizathonModel.isRegistrationAllowed && quizathonModel.registrationPointBurn >= 0
                                        ) {
                                            burnType = "QuizRegistration"
                                            quizathonModelPointBurn = quizathonModel
                                            showBurnQuizDialog(
                                                quizathonModel.registrationPointBurn.toInt(),
                                                quizathonModel.burnRegTitle,
                                                quizathonModel.burnRegIcon,
                                                quizathonModel.burnRegMessage,
                                                quizathonModel.quizId
                                            )
                                        } else if (!quizathonModel.getIsUserRegistered() && quizathonModel.getRegistrationQuestions() != null && !quizathonModel.getRegistrationQuestions()
                                                .isEmpty() && quizathonModel.isRegistrationRequired
                                        ) {
                                            val intent =
                                                Intent(context, QuizathonActivity::class.java)
                                            intent.putExtra(
                                                "data",
                                                if (quizathonModel.answerJson != null && !quizathonModel.isQuizCompleted) quizathonModel.answerJson else quizathonModel.questionJson
                                            )
                                            intent.putExtra(
                                                "CategoryName", quizathonModel.getQuizTitle()
                                            )
                                            intent.putExtra(
                                                "Category", quizathonModel.getQuizDesc()
                                            )
                                            intent.putExtra(
                                                "rewardDate", quizathonModel.getRewardDate()
                                            )
                                            intent.putExtra("comingFrom", "")
                                            intent.putExtra("IsRetake", quizathonModel.retake)
                                            intent.putExtra("RetakeId", quizathonModel.retakeId)
                                            intent.putExtra("qId", quizathonModel.getQuizId())
                                            intent.putExtra("type", type)
                                            intent.putExtra("quizModel", quizathonModel)

                                            val dialog = RegistrationDialog(
                                                this@NewDashboardActivity,
                                                quizathonModel.getRegistrationQuestions(),
                                                quizathonModel.regTitle,
                                                quizathonModel.getQuizId(),
                                                intent,
                                                quizathonModel.getQuizStarted(),
                                                quizathonModel.quizDesc
                                            )
                                            if (quizathonModel.regIcon != null && quizathonModel.regIcon.isNotEmpty()) {
                                                dialog.iconUrl = quizathonModel.regIcon
                                            }

                                            dialog.show()
                                        } else if (!quizathonModel.isRegistrationRequired) {
                                            if (quizathonModel.quizBurnPoints > 0.toString()) {
                                                burnType = "QuizEntry"
                                                quizathonModelRegister = quizathonModel
                                                showBurnQuizDialog(
                                                    quizathonModel.quizBurnPoints.toInt(),
                                                    "Quiz",
                                                    quizathonModel.burnRegIcon,
                                                    "You can participate in the quiz using " + quizathonModel.quizBurnPoints + " HappyYou points",
                                                    quizathonModel.quizId
                                                )
                                            } else {
                                                val intent =
                                                    Intent(context, QuizathonActivity::class.java)
                                                intent.putExtra(
                                                    "data",
                                                    if (quizathonModel.answerJson != null && !quizathonModel.isQuizCompleted) quizathonModel.answerJson else quizathonModel.questionJson
                                                )
                                                intent.putExtra(
                                                    "CategoryName", quizathonModel.getQuizTitle()
                                                )
                                                intent.putExtra(
                                                    "Category", quizathonModel.getQuizDesc()
                                                )
                                                intent.putExtra(
                                                    "rewardDate", quizathonModel.getRewardDate()
                                                )
                                                intent.putExtra("comingFrom", "")
                                                intent.putExtra("IsRetake", quizathonModel.retake)
                                                intent.putExtra("RetakeId", quizathonModel.retakeId)
                                                intent.putExtra("qId", quizathonModel.getQuizId())
                                                intent.putExtra("type", type)
                                                intent.putExtra("quizModel", quizathonModel)
                                                context.startActivity(intent)
                                            }

                                        } else if (quizathonModel.getDialog_model() != null && quizathonModel.getDialog_model()
                                                .getType() != null
                                        ) {
                                            val dialog = QuizMessageDialog(
                                                this@NewDashboardActivity,
                                                quizathonModel.getDialog_model(),
                                                apiInterfaceWyh
                                            )
                                            dialog.show()
                                        } else {
                                            val intent =
                                                Intent(context, QuizathonActivity::class.java)
                                            intent.putExtra(
                                                "data",
                                                if (quizathonModel.answerJson != null && !quizathonModel.isQuizCompleted) quizathonModel.answerJson else quizathonModel.questionJson
                                            )
                                            intent.putExtra(
                                                "CategoryName", quizathonModel.getQuizTitle()
                                            )
                                            intent.putExtra(
                                                "Category", quizathonModel.getQuizDesc()
                                            )
                                            intent.putExtra(
                                                "rewardDate", quizathonModel.getRewardDate()
                                            )
                                            intent.putExtra("comingFrom", "")
                                            intent.putExtra("IsRetake", quizathonModel.retake)
                                            intent.putExtra("RetakeId", quizathonModel.retakeId)
                                            intent.putExtra("qId", quizathonModel.getQuizId())
                                            intent.putExtra("type", type)
                                            intent.putExtra("quizModel", quizathonModel)
                                            startActivity(intent)
                                        }
                                    }
                                }
                            }

                        )


                        val linearLayoutManager = LinearLayoutManager(
                            this@NewDashboardActivity,
                            LinearLayoutManager.HORIZONTAL,
                            false
                        )
                        binding.rvGridQuiz.setLayoutManager(linearLayoutManager)
                        binding.rvGridQuiz.setAdapter(gridPlaywinAdapter)
                        val quickReadLinearSnapHelper: LinearSnapHelper = SnapHelperOneByOne()
                        binding.rvGridQuiz.setOnFlingListener(null)
                        quickReadLinearSnapHelper.attachToRecyclerView(binding.rvGridQuiz)

                        val absorbHealthTvLinearLayoutManager1 = LinearLayoutManager(
                            this@NewDashboardActivity
                        )
                        absorbHealthTvLinearLayoutManager1.orientation = RecyclerView.HORIZONTAL
                        var indicatorSize = 1

                        if (getQuizQuestionsList != null && getQuizQuestionsList.size > 2) {
                            indicatorSize =
                                ceil(
                                    maxLength.toString()
                                        .toDouble() / 2
                                )
                                    .toInt()
                        }

                        if (indicatorSize > 1) {
                            binding.rvGridQuizIndicator.setVisibility(View.VISIBLE)
                        } else {
                            binding.rvGridQuizIndicator.setVisibility(View.GONE)
                        }


                        val absorbHealthTvIndicatorsAdapter =
                            IndicatorsAdapter(this@NewDashboardActivity, indicatorSize, 0)
                        binding.rvGridQuizIndicator.setAdapter(absorbHealthTvIndicatorsAdapter)
                        binding.rvGridQuizIndicator.setLayoutManager(
                            absorbHealthTvLinearLayoutManager1
                        )
                        binding.rvGridQuizIndicator.setHasFixedSize(true)

                        binding.rvGridQuiz.addOnScrollListener(object :
                            RecyclerView.OnScrollListener() {
                            override fun onScrollStateChanged(
                                recyclerView: RecyclerView,
                                newState: Int
                            ) {
                                super.onScrollStateChanged(recyclerView, newState)
                                var position = 0
                                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                                    position =
                                        if (linearLayoutManager.findFirstCompletelyVisibleItemPosition() != -1) {
                                            linearLayoutManager.findFirstCompletelyVisibleItemPosition()
                                        } else linearLayoutManager.findFirstVisibleItemPosition()
                                    absorbHealthTvIndicatorsAdapter.updateSelectedIndex(position)
                                }
                            }
                        })

                    }

                    override fun onFailure(call: Call<QuizathonResponseModel?>, t: Throwable) {
                        CommonUtils.dismissDialoge()
                    }
                })
        } catch (e: java.lang.Exception) {
            CommonUtils.dismissDialoge()
        }
    }

    fun ApiSaveQuizRegistration(
        RegistrationID: String?,
        AnswerJson: String?,
        inte: Intent,
        isQuizStarted: Boolean,
    ) {
        CommonUtils.dismissDialoge()
        CommonUtils.showProgressDialige(context)
        val request: SaveQuizRegistration = SaveQuizRegistration(RegistrationID, AnswerJson)
        val call = apiInterfaceWyh.SaveQuizRegistration(SharedPref.getAuthToken(), request)

        call.enqueue(object : Callback<SaveQuizRegistrationResponse?> {
            override fun onResponse(
                call: Call<SaveQuizRegistrationResponse?>,
                response: Response<SaveQuizRegistrationResponse?>
            ) {
                if (response.code() == 200 && response.body() != null) {
                    CommonUtils.dismissDialoge()
                    try {
                        if (isQuizStarted) {
                            startActivity(inte)
                        } else {
                            val dialog = QuizMessageDialog(
                                this@NewDashboardActivity,
                                response.body()!!.quizRegistrationData.dialogModel, apiInterfaceWyh
                            )
                            dialog.show()
                        }
                    } catch (e: Exception) {
                        CommonUtils.dismissDialoge()
                        e.printStackTrace()
                    }
                    GetActiveQuizathon()
                }
            }

            override fun onFailure(call: Call<SaveQuizRegistrationResponse?>, t: Throwable) {
                CommonUtils.dismissDialoge()
                Toast.makeText(
                    context,
                    "Something went wrong! Please try again later.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    fun ClaimRewardById(
        TransId: String?,
        IsClaim: Boolean,
        IsReclaim: Boolean,
        BurnValue: String?,
        claimReClaimRewardModel: ClaimReClaimRewardModel
    ) {
        val request: ClaimRewardRequest = ClaimRewardRequest(TransId, IsClaim, IsReclaim, BurnValue)
        val call = apiInterface.ClaimRewardById(SharedPref.getAuthToken(), request)

        call.enqueue(object : Callback<CommonSuccessResponse?> {
            override fun onResponse(
                call: Call<CommonSuccessResponse?>,
                response: Response<CommonSuccessResponse?>
            ) {
                if (response.code() == 200 && response.body() != null) {
                    try {
                        if (response.body()!!.quizathonRewardData != null) {
                            //QuizReward Api Call
                            getQuizathonRewardPopup(response.body()!!.quizathonRewardData)
                            if (dashquizathonRewardList.size > 0) {
                                dashquizathonRewardList.removeAt(0)
                            }

                        } else {
                            if (response.body() != null) {
                                var message: String = response.body()!!.msg
                                Toast.makeText(
                                    context,
                                    message,
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {

                                Toast.makeText(
                                    context,
                                    "Unable to claim the rewards",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

            override fun onFailure(call: Call<CommonSuccessResponse?>, t: Throwable) {
                Toast.makeText(
                    context,
                    "Something went wrong! Please try again later.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    fun checkIsFromQuizqathon(): Boolean {
        if (trasactionId != null && !trasactionId!!.isEmpty()) {
            return true
        }
        return false
    }

    private fun getQuizathonRewardPopup(data: QuizathonRewardData) {
        reward = data
        try {
            if (data.rewardType != null && !data.rewardType.isEmpty()) {
                val rewardtype = data.rewardType
                if (rewardtype.equals("Points", ignoreCase = true)) {
                    showPostSpinnerPointsPopupCallBack(
                        data.rewardTitle, context, "Trends", data.rewardDescription,
                        data.rewardHeader1, data.rewardHeader2,
                        this,
                        this
                    )
                } else if (rewardtype.equals("Offers", ignoreCase = true)) {
                    showPostSpinnerOfferCallBack(
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
                        this,
                        this
                    )
                } else if (rewardtype.equals("Voucher", ignoreCase = true)) {
                    showPostSpinnerVoucherCallBack(
                        context,
                        data.rewardLogo,
                        data.couponCode,
                        data.rewardDescription,
                        data.rewardTitle,
                        data.expiryInHours,
                        "Trends",
                        data.rewardHeader1,
                        data.rewardHeader2,
                        this,
                        this
                    )
                } else if (rewardtype.equals("Badge", ignoreCase = true)) {
                    showPostSpinnerBadgePopupCallBack(
                        context,
                        "Trends",
                        data.rewardLogo,
                        data.rewardTitle,
                        data.rewardDescription,
                        data.rewardHeader1,
                        data.rewardHeader2,
                        this,
                        this
                    )
                } else if (rewardtype.equals("Stamps", ignoreCase = true)) {
                    showStampsPopupCallBack(
                        data.rewardHeader1, data.rewardHeader2, data.rewardTitle, data.rewardValue,
                        this,
                        this,
                        this
                    )
                }
            }
        } catch (ex: java.lang.Exception) {
        }
    }

    fun removeClaimDialog(model: ClaimReClaimRewardModel) {
        try {
            dashquizathonRewardList.remove(model);
            if (dashquizathonRewardList.size > 0) {
                if (!claimReclaimDialog.isShowing) {
                    claimReclaimDialog = ClaimReClaimDialog(
                        context,
                        dashquizathonRewardList.get(0).transId,
                        "" + dashquizathonRewardList.get(0).reclaimBurnValue,
                        dashquizathonRewardList.get(0).rewardTitle,
                        dashquizathonRewardList.get(0).rewardTitleIcon,
                        dashquizathonRewardList.get(0).rewardDescription,
                        dashquizathonRewardList.get(0).isClaim,
                        dashquizathonRewardList.get(0), this
                    )
                    claimReclaimDialog.show()
                }

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun cancelDialog() {
        try {
            if (dashquizathonRewardList.size > 0) {
                if (!claimReclaimDialog.isShowing) {
                    claimReclaimDialog = ClaimReClaimDialog(
                        context,
                        dashquizathonRewardList.get(0).transId,
                        "" + dashquizathonRewardList.get(0).reclaimBurnValue,
                        dashquizathonRewardList.get(0).rewardTitle,
                        dashquizathonRewardList.get(0).rewardTitleIcon,
                        dashquizathonRewardList.get(0).rewardDescription,
                        dashquizathonRewardList.get(0).isClaim,
                        dashquizathonRewardList.get(0), this
                    )
                    claimReclaimDialog.show()
                }

            } else {
                NudgeDialogue.spinnerCancelDialog(context, reward.getRewardType(), this)
            }
        } catch (e: java.lang.Exception) {
            // Log the exception for debugging
            e.printStackTrace()
        }
    }

    private fun showCorporateAlert() {
        Log.e("showCorporateAlert", "");
        val inflater = LayoutInflater.from(context)
        val dialogView = inflater.inflate(R.layout.dialog_corporate_account, null, false)

        // Build the dialog
        dialog = AlertDialog.Builder(context)
            .setView(dialogView)
            .create()
        dialog.setCancelable(false)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val tvTitle = dialogView.findViewById<TextView>(R.id.tvTitle)
        val btnYes = dialogView.findViewById<Button>(R.id.btnYes)
        val btnNo = dialogView.findViewById<Button>(R.id.btnNo)
        btnYes.text = "Retry"
        btnNo.text = "Close"
        tvTitle.text = "Your Corporate Not Found"
        btnYes.setOnClickListener { view: View? ->
            SharedPref.putCorporateAccountNotFound(false)
            val intent = Intent(context, ActivityCorporateAccountAddEdit::class.java)
            startActivity(intent)
        }
        btnNo.setOnClickListener { view: View? ->
            SharedPref.putCorporateAccountNotFound(false)
            dismissDialog()
        }

        /*  dialog.setOnKeyListener(DialogInterface.OnKeyListener { dialog1: DialogInterface?, keyCode: Int, event: KeyEvent ->
              if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP) {
                  //dialog.setCancelable(true);
                  //dialog.dismiss();  // Dismiss the dialog
                  SharedPref.putCorporateAccountNotFound(false)
                  dismissDialog() // Optionally close the activity
                   true
              }
              false
          })*/
        dialog.show()
    }

    fun dismissDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss()
        }
    }

    @SuppressLint("SetTextI18n")
    fun ShowFileShareDialogNew() {
        if (NewDashboardHelper.Companion.popupModelList.size > 0) {
            var model: FilePopupModel = NewDashboardHelper.Companion.popupModelList.get(0)
            val inflater = LayoutInflater.from(context)
            val dialogView = inflater.inflate(R.layout.dialog_file_share_new, null, false)
            lateinit var dialog: AlertDialog
            // Build the dialog
            dialog = AlertDialog.Builder(context)
                .setView(dialogView)
                .create()
            dialog.setCancelable(false);
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val window = dialog.window
            window?.setDimAmount(0.9f)
            val tvTitle = dialogView.findViewById<TextView>(R.id.tvTitle)
            //        TextView tvTitle2=dialogView.findViewById(R.id.tvTitle2);
            val tvSubTitle1 = dialogView.findViewById<TextView>(R.id.tvSubTitle1)
            val tvSubTitle2 = dialogView.findViewById<TextView>(R.id.tvSubTitle2)
            val tvSubTitle3 = dialogView.findViewById<TextView>(R.id.tvSubTitle3)
            val tvSubTitle4 = dialogView.findViewById<TextView>(R.id.tvSubTitle4)
            val tvSubTitle5 = dialogView.findViewById<TextView>(R.id.tvSubTitle5)
            val tvSubTitle6 = dialogView.findViewById<TextView>(R.id.tvSubTitle6)
            val tvSubTitle1Value = dialogView.findViewById<TextView>(R.id.tvSubTitle1Value)
            val tvSubTitle2Value = dialogView.findViewById<TextView>(R.id.tvSubTitle2Value)
            val tvSubTitle3Value = dialogView.findViewById<TextView>(R.id.tvSubTitle3Value)
            val tvSubTitle4Value = dialogView.findViewById<TextView>(R.id.tvSubTitle4Value)
            val tvSubTitle5Value = dialogView.findViewById<TextView>(R.id.tvSubTitle5Value)
            val tvSubTitle6Value = dialogView.findViewById<TextView>(R.id.tvSubTitle6Value)
            val tvDesc1 = dialogView.findViewById<TextView>(R.id.tvDesc1)
            val tvDesc2 = dialogView.findViewById<TextView>(R.id.tvDesc2)
            val tvDesc3 = dialogView.findViewById<TextView>(R.id.tvDesc3)
            val tvExpireOn = dialogView.findViewById<TextView>(R.id.tvExpireOn)
            val tvFileName = dialogView.findViewById<TextView>(R.id.tvFileName)
            val btnDownload = dialogView.findViewById<RelativeLayout>(R.id.btnDownload)
            val btnView = dialogView.findViewById<RelativeLayout>(R.id.btnView)
            val imFileShareImage = dialogView.findViewById<ImageView>(R.id.imFileShareImage)
            val btnClose = dialogView.findViewById<ImageView>(R.id.imClose)
            val tvNote = dialogView.findViewById<TextView>(R.id.tvNote)
            btnDownload.setOnClickListener { v: View? ->
                activityTracker("Android_FILE_DOWNLOAD", context)
                saveFileShare(model.categoryName, "Download")
                if ((model.filePath != null) and !model.filePath.isEmpty()) {
                    downloadImage(context, model.filePath, model.fileName)
                }
                //dialog.dismiss()
            }
            btnClose.setOnClickListener { v: View? ->
                dialog.dismiss()
                activityTracker("Android_FILE_CANCEL", context)
            }
            btnView.setOnClickListener { v: View? ->
                activityTracker("Android_FILE_VIEW", context)
                saveFileShare(model.categoryName, "View")
                if ((model.filePath != null) and !model.filePath.isEmpty()) {
                    if (model.filePath.endsWith(".pdf") || model.filePath.endsWith(".doc") || model.filePath.endsWith(
                            ".docx"
                        )
                    ) {
                        CommonUtils.downloadAndViewFile(context, model.filePath)
                    } else {
                        viewImage(model.filePath)
                    }
                }
            }
            Glide.with(context)
                .load(model.tileIcon)
                .error(R.drawable.ic_file_share)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(imFileShareImage)

            tvTitle.text = model.categoryName
            tvSubTitle1.text =
                model.eventId.split(";".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
            tvSubTitle2.text = model.dateString.split(";".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()[0]
            tvSubTitle3.text = model.timeString.split(";".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()[0]
            tvSubTitle4.text =
                model.place.split(";".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
            tvSubTitle5.text = "End Date"
            tvSubTitle6.text = "End Time"
            tvSubTitle1Value.text =
                model.eventId.split(";".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
            tvSubTitle2Value.text =
                model.dateString.split(";".toRegex()).dropLastWhile { it.isEmpty() }
                    .toTypedArray()[1]
            tvSubTitle3Value.text =
                model.timeString.split(";".toRegex()).dropLastWhile { it.isEmpty() }
                    .toTypedArray()[1]
            tvSubTitle4Value.text =
                model.place.split(";".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
            tvSubTitle5Value.text = formatDateFromString(
                "yyyy-MM-dd'T'HH:mm",
                "dd/MM/yyyy",
                model.expiryDate
            )
            tvSubTitle6Value.text = formatDateFromString(
                "yyyy-MM-dd'T'HH:mm",
                "hh:mm a",
                model.expiryDate
            )
            tvDesc1.text = model.description1.replace("\\n", "\n")
            tvDesc2.text = model.description2.replace("\\n", "\n")
            tvDesc3.text = model.description3.replace("\\n", "\n")
            tvExpireOn.visibility = View.GONE
            tvExpireOn.text = "Expire On " + formatDateFromString(
                "yyyy-MM-dd'T'HH:mm",
                "dd/MM/yyyy",
                model.expiryDate
            )
            tvFileName.text = model.fileName
            tvNote.visibility = View.GONE
            tvNote.text = model.note
            dialog.show()

            dialog.setOnDismissListener {
                NewDashboardHelper.Companion.popupModelList.remove(model)
                ShowFileShareDialogNew()
            }
        }
    }

    fun ShowEyeCheckPopu() {
        val inflater = LayoutInflater.from(context)
        val dialogView = inflater.inflate(R.layout.dailog_eye_checkup, null, false)
        lateinit var dialog: AlertDialog
        // Build the dialog
        dialog = AlertDialog.Builder(context)
            .setView(dialogView)
            .create()
        dialog.setCancelable(false);
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val window = dialog.window
        window?.setDimAmount(0.9f)
        val imClose = dialogView.findViewById<ImageView>(R.id.imClose)
        dialog.show()

        imClose.setOnClickListener(View.OnClickListener { v: View? ->

            dialog.dismiss()
        })
    }

    fun ShowFileShareDialog() {
        //Log.e("ShowFileshare",response.data.fetchdashboarddata.filePopupModel.fileName);
        if (NewDashboardHelper.Companion.popupModelList.size > 0) {
            var model: FilePopupModel = NewDashboardHelper.Companion.popupModelList.get(0)
            val inflater = LayoutInflater.from(context)
            val dialogView = inflater.inflate(R.layout.dialog_file_share, null, false)
            lateinit var dialog: AlertDialog
            // Build the dialog
            dialog = AlertDialog.Builder(context)
                .setView(dialogView)
                .create()
            dialog.setCancelable(false);
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val window = dialog.window
            window?.setDimAmount(0.9f)
            val tvTitle = dialogView.findViewById<TextView>(R.id.tvTitle)
            val tvTitle2 = dialogView.findViewById<TextView>(R.id.tvTitle2)
            val tvExpireOn = dialogView.findViewById<TextView>(R.id.tvExpireOn)
            val tvFileName = dialogView.findViewById<TextView>(R.id.tvFileName)
            val tvNote = dialogView.findViewById<TextView>(R.id.tvNote)
            val btnDownload = dialogView.findViewById<RelativeLayout>(R.id.btnDownload)
            val btnView = dialogView.findViewById<RelativeLayout>(R.id.btnView)
            val imClose = dialogView.findViewById<ImageView>(R.id.imClose)
            val imFileShareImage = dialogView.findViewById<ImageView>(R.id.imFileShareImage)
            // Close button with validation
            imClose.setOnClickListener(View.OnClickListener { v: View? ->
                activityTracker("Android_FILE_CANCEL", context)
                dialog.dismiss()
            })
            btnDownload.setOnClickListener(View.OnClickListener { v: View? ->
                activityTracker("Android_FILE_DOWNLOAD", context)
                saveFileShare(model.categoryName, "Download")
                downloadImage(this, model.filePath, model.fileName)
                //dialog.dismiss()
            })
            btnView.setOnClickListener { v: View? ->
                activityTracker("Android_FILE_VIEW", context)
                //viewImage(model.filePath)
                saveFileShare(model.categoryName, "View")
                if ((model.filePath != null) and model.filePath.isNotEmpty()) {
                    if (model.filePath.endsWith(".pdf") || model.filePath.endsWith(".doc") || model.filePath.endsWith(
                            ".docx"
                        )
                    ) {
                        CommonUtils.downloadAndViewFile(context, model.filePath)
                    } else {
                        viewImage(model.filePath)
                    }
                }
            }
            Glide.with(context)
                .load(model.tileIcon)
                .error(R.drawable.ic_file_share)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(imFileShareImage)

            tvTitle.text = model.categoryName
            tvTitle2.text = model.subCategoryName
            tvNote.text = model.note
            tvExpireOn.text = "Expire On " + formatDateFromString(
                "yyyy-MM-dd'T'HH:mm",
                "dd/MM/yyyy",
                model.expiryDate
            )
            tvFileName.text = model.fileName
            dialog.show()

            dialog.setOnDismissListener {
                NewDashboardHelper.Companion.popupModelList.remove(model)
                ShowFileShareDialogNew()
            }
        }
    }

    private fun viewImage(url: String) {
        if (url.endsWith(".pdf")) {
            // Handle PDF files
            val path = Uri.parse(url)
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(path, "application/pdf")
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            try {
                context.startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(context, "No Application Available to View PDF", Toast.LENGTH_SHORT)
                    .show()
            }
        } else if (url.endsWith(".doc") || url.endsWith(".docx")) {
            // Handle DOC and DOCX files
            val path = Uri.parse(url)
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(path, "application/msword")
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            try {
                context.startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(
                    context,
                    "No Application Available to View Word Document",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            // Handle image files
            val i = Intent(context, PhotoViewActivityFileShare::class.java)
            i.putExtra("url", url)
            context.startActivity(i)
        }
    }

    private fun saveFileShare(corporateCat: String, UserAction: String) {
        val request = UpdateFileStatusReq(corporateCat, UserAction)
        val call: Call<UpdateFileStatusResp> =
            apiInterface.UpdateFileStatus(SharedPref.getAuthToken(), request)

        call.enqueue(object : Callback<UpdateFileStatusResp?> {
            override fun onResponse(
                call: Call<UpdateFileStatusResp?>,
                response: Response<UpdateFileStatusResp?>
            ) {
            }

            override fun onFailure(call: Call<UpdateFileStatusResp?>, t: Throwable) {
            }
        })
    }

    public fun showBurnQuizDialog(
        pointsToBurn: Int,
        title: String,
        icon: String,
        message: String,
        qid: String
    ) {


        // Use StringBuilder for better performance
//        var messageBuilder = java.lang.StringBuilder("Still want to participate!\n")
//            .append("You can participate in quiz by burning points")


        val retakeAlertDialog = RetakeAlertDialog(
            this@NewDashboardActivity,
            pointsToBurn,
            icon,
            title,
            message,
            getRetakeDialogListener(qid, pointsToBurn) // Use extracted listener
        )

        retakeAlertDialog.show()
    }

    // Separate method to handle dialog button clicks
    private fun getRetakeDialogListener(
        qid: String,
        pointsToBurn: Int
    ): QuizOptionAdapter.OnItemClickListener {
        return QuizOptionAdapter.OnItemClickListener { position: Int, name: String ->
            if (name.equals("Close", ignoreCase = true)) {
                // Optional: Add logging or tracking here
            } else if (name.equals("Retake", ignoreCase = true)) {

                BurnSpinData(qid.toInt(), pointsToBurn)
            }
        }
    }

    private fun BurnSpinData(quizId: Int, pointsToBurn: Int) {
        val request = BurnRegPointsReq(quizId, pointsToBurn, burnType)
        val call: Call<BurnRegPointsResp?> = if (burnType == "QuizEntry") {
            apiInterface.BurnQuizPoints(SharedPref.getAuthToken(), request)
        } else {
            apiInterface.BurnQuizPoints(SharedPref.getAuthToken(), request)
        }

        call.enqueue(object : Callback<BurnRegPointsResp?> {
            override fun onResponse(
                call: Call<BurnRegPointsResp?>,
                response: Response<BurnRegPointsResp?>
            ) {
                if (response.isSuccessful) {
                    if (response.body()?.success == true) {
                        Toast.makeText(
                            context,
                            "Points burned. You can now register the Quiz",
                            Toast.LENGTH_SHORT
                        ).show()
                        burnType = ""
                        //GetActiveQuizathon()
                        if (click.isNotEmpty()) {
                            if (click.equals("QuizRegistration")) {
                                callRegisterAfterPointBurnBanner()
                            } else {
                                callQuizAfterPointBurnBanner()
                            }

                        } else {
                            if (quizathonModelPointBurn != null && burnType == "") {
                                callRegisterAfterPointBurn(quizathonModelPointBurn!!)
                            }
                        }

                        if (click.isNotEmpty()) {
                            if (click.equals("QuizEntry")) {
                                callRegisterAfterPointBurnBanner()
                            } else {
                                callQuizAfterPointBurnBanner()
                            }

                        } else {
                            if (quizathonModelRegister != null && burnType == "") {

                                callQuizAfterPointBurn(quizathonModelRegister!!)
                            }
                        }
                        getUserDetails(binding, context)
                    } else {
                        Toast.makeText(
                            context,
                            response.body()?.msg ?: "",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }
            }

            override fun onFailure(call: Call<BurnRegPointsResp?>, t: Throwable) {
                // You may want to log or handle the error here
            }
        })
    }

    fun callRegisterAfterPointBurn(quizathonModel: QuizathonModel) {
        if (!quizathonModel.isUserRegistered && quizathonModel.registrationQuestions != null && !quizathonModel.registrationQuestions.isEmpty() && quizathonModel.isRegistrationRequired) {
            val intent = Intent(context, QuizathonActivity::class.java)
            intent.putExtra(
                "data",
                if ((quizathonModel.answerJson != null && !quizathonModel.isQuizCompleted)
                ) quizathonModel.answerJson
                else quizathonModel.questionJson
            )
            intent.putExtra("CategoryName", quizathonModel.quizTitle)
            intent.putExtra("Category", quizathonModel.quizDesc)
            intent.putExtra("rewardDate", quizathonModel.rewardDate)
            intent.putExtra("comingFrom", "")
            intent.putExtra("IsRetake", quizathonModel.retake)
            intent.putExtra("RetakeId", quizathonModel.retakeId)
            intent.putExtra("qId", quizathonModel.quizId)
            intent.putExtra("type", "Quizathon")
            intent.putExtra("quizModel", quizathonModel)
            val dialog = RegistrationDialog(
                this@NewDashboardActivity,
                quizathonModel.registrationQuestions,
                quizathonModel.regTitle,
                quizathonModel.quizId,
                intent,
                quizathonModel.quizStarted,
                quizathonModel.quizDesc
            )
            if (quizathonModel.regIcon != null && !quizathonModel.regIcon.isEmpty()) {
                dialog.iconUrl = quizathonModel.regIcon
            }
            dialog.show()
            quizathonModelPointBurn = null
        }
    }

    fun callRegisterAfterPointBurnBanner() {
        click = ""
        val dialog = RegistrationDialog(
            context,
            allBannerData[index].quizathonRegistrationQuestion,
            allBannerData[index].regTitle,
            allBannerData[index].quizathonId,
            createIntent(allBannerData[index]),
            allBannerData[index].quizathonStarted,
            allBannerData[index].quizathonTitle
        )

        dialog.iconUrl = allBannerData[index].regIcon

        dialog.show()
    }

    fun createIntent(banner: GetBannerResponse.Data): Intent {

        var intent = Intent(context, QuizathonActivity::class.java)
        if (banner.isQuizCompleted) {
            intent = Intent(context, QuizathonScoreActivity::class.java)
        }
        intent.putExtra("data", banner.quizathonQuestion)
        intent.putExtra("CategoryName", banner.quizathonTitle ?: "")
        intent.putExtra("Category", banner.quizathonTitle ?: "")
        intent.putExtra("comingFrom", "")
        intent.putExtra("IsRetake", false)
        intent.putExtra("RetakeId", 0)
        intent.putExtra("rewardDate", banner.quizathonRewardDate)
        intent.putExtra("qId", banner.quizathonId)
        intent.putExtra("quizdata", banner.model)
        intent.putExtra("withActivity", banner.showActivity)
        intent.putExtra("showNextGameButton", banner.isShowNextGameButton)
        intent.putExtra("nextGameRedirectTo", banner.redirectTo)
        return intent
    }

    fun callQuizAfterPointBurnBanner() {
        click = "";
        if (allBannerData[index].quizathonStarted) {
            val intent = Intent(context, QuizathonActivity::class.java)
            intent.putExtra("data", allBannerData[index].quizathonQuestion)
            intent.putExtra(
                "CategoryName",
                allBannerData[index].quizathonTitle ?: ""
            )
            intent.putExtra(
                "Category",
                allBannerData[index].quizathonTitle ?: ""
            )
            intent.putExtra("comingFrom", "")
            intent.putExtra("IsRetake", false)
            intent.putExtra("RetakeId", 0)
            intent.putExtra(
                "rewardDate",
                allBannerData[index].quizathonRewardDate
            )
            intent.putExtra("qId", allBannerData[index].quizathonId)
            context.startActivity(createIntent(allBannerData[index]))
        }
    }

    override fun onRewardClick(item: Any?, type: String?) {
        if (item is ClaimReClaimRewardModel) {
            claimReward = item.rewardItem

            val dialog =
                RewardInfoBottomSheet(context, claimReward, "Active", object : onRewardDialogClick {
                    override fun onStartClick() {
                        if (claimReward != null) {
                            StartSpinActivity(claimReward!!.getActivityTransId())
                            if (claimReward!!.getRedirectionKey().contains("feedback")) {
                                val instance = getInstance()
                                instance.showPopUpFeedbackCallback(
                                    this@NewDashboardActivity,
                                    NewDashboardHelper.feedbackResponseData,
                                    this@NewDashboardActivity
                                )
                            }
                        }
                    }

                    override fun onCompleteClick() {
                        activityTracker(
                            "A_PlayAndWinQuizathon_ActivityPopUpRedirection_" + claimReward!!.getRedirectionKey(),
                            context
                        )
                        if (claimReward!!.getRedirectionKey().contains("feedback")) {
                            val instance = getInstance()
                            instance.showPopUpFeedbackCallback(
                                this@NewDashboardActivity, NewDashboardHelper.feedbackResponseData,
                                this@NewDashboardActivity
                            )
                        } else if (claimReward!!.getRedirectionKey()
                                .equals("journalUpload", ignoreCase = true)
                        ) {
                            fileUploadKey = "JournalUpload"
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                openImagePicker()
                            } else {
                                openGalleryOnly()
                            }
                        } else if (claimReward!!.getRedirectionKey()
                                .equals("activityimageupload", ignoreCase = true)
                        ) {
                            fileUploadKey = "ActivityUpload"
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                openImagePicker()
                            } else {
                                openGalleryOnly()
                            }
                        } else if (claimReward!!.getRedirectionKey()
                                .equals("water", ignoreCase = true)
                        ) {
                            waterIntakeDialoge(
                                this@NewDashboardActivity,
                                waterIntakeGoal,
                                waterIntake,
                                waterIntakeAllowed
                            )
                        } else if (claimReward!!.getRedirectionKey()
                                .equals("meditation", ignoreCase = true)
                        ) {
                            showPopUpZenZone(
                                this@NewDashboardActivity,
                                currentValueZenZone.toDouble()
                            )
                        } else {
                            val redirectionModel = RedirectionModel(
                                claimReward!!.getRedirectionKey(),
                                false,
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                0,
                                "",
                                "",
                                "",
                                "",
                                ""
                            )
                            redirectionScreen(redirectionModel, context)
                        }
                    }

                    override fun onCloseClick() {
                        //cancelDialog();
                    }

                    override fun onSubmit(title: String) {
                        titleContent = title
                        if (claimReward!!.getRedirectionKey()
                                .equals("journalUpload", ignoreCase = true)
                        ) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                openImagePicker()
                            } else {
                                checkPermission(
                                    Manifest.permission.CAMERA,
                                    CAMERA_PERMISSION_CODE
                                )
                            }
                        }
                    }
                })
            dialog.comingFrom = "Quiz"
            dialog.isFromRewardList = true
            dialog.show(supportFragmentManager, "expandableBottomSheet")
        }

    }

    private fun StartSpinActivity(ActivityTransId: Int) {
        try {
            CommonUtils.dismissDialoge()
            CommonUtils.showProgressDialige(this@NewDashboardActivity)
            apiInterface.StartQuizActivity(
                SharedPref.getAuthToken(),
                StartSpinActivityRequest(ActivityTransId)
            ).enqueue(object : Callback<CommonResponse?> {
                override fun onResponse(
                    call: Call<CommonResponse?>,
                    response: Response<CommonResponse?>
                ) {
                    CommonUtils.dismissDialoge()
                    if (response.body() != null && response.code() == 200) {
                        if (dashquizathonRewardList.size > 0) {
                            dashquizathonRewardList.removeAt(0)
                        }
                        if (claimReward!!.getRedirectionKey().equals("water", ignoreCase = true)) {
                            waterIntakeDialoge(
                                this@NewDashboardActivity,
                                waterIntakeGoal,
                                waterIntake,
                                waterIntakeAllowed
                            )
                        } else if (claimReward!!.getRedirectionKey()
                                .equals("meditation", ignoreCase = true)
                        ) {
                            showPopUpZenZone(
                                this@NewDashboardActivity,
                                currentValueZenZone.toDouble()
                            )
                        } else {
                            val redirectionModel = RedirectionModel(
                                claimReward!!.getRedirectionKey(),
                                false,
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                0,
                                "",
                                "",
                                "",
                                "",
                                ""
                            )
                            redirectionScreen(redirectionModel, this@NewDashboardActivity)
                        }
                    }
                }

                override fun onFailure(call: Call<CommonResponse?>, t: Throwable) {
                    CommonUtils.dismissDialoge()
                }
            })
        } catch (e: java.lang.Exception) {
            CommonUtils.dismissDialoge()
        }
    }

    fun waterIntakeDialoge(context: Context) {
        try {
            val customYesNoDialog = CustomYesNoDialog(context, R.style.Theme_Dialog)
            customYesNoDialog.show()
            customYesNoDialog.setCancelable(false)
            customYesNoDialog.binding.llActionRequired.visibility = View.GONE
            customYesNoDialog.binding.txtInfoPopUpDesc.text =
                "You have exceeded the water intake limit, still, you have wanted to add water ?"
            customYesNoDialog.binding.btnYes.text = "Yes"
            customYesNoDialog.binding.btnCancel.text = "No"
            customYesNoDialog.binding.btnYes.background =
                ContextCompat.getDrawable(context, R.drawable.blue_rc_bg_8dp)
            customYesNoDialog.binding.btnCancel.background =
                ContextCompat.getDrawable(context, R.drawable.pink_rc_bg_8dp)
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

            builder!!.setView(binding.root)
            alertDialog = builder!!.create()
            builder!!.setCancelable(true)

            binding.ivAdd.setOnClickListener { view ->
                increaseNumber(
                    binding.tvGlassAmt.text.toString(),
                    binding,
                    waterIntakeGoal,
                    waterIntake,
                    context
                )
            }

            binding.ivMinus.setOnClickListener { view ->
                decreaseNumber(binding, context)
            }


            binding.btnClosed.setOnClickListener { view ->
                SharedPref.putWaterIntake(false)
                alertDialog.dismiss()
            }

            binding.btnAddWater.setOnClickListener { view ->
                val glasses = binding.tvGlassAmt.text.toString().toInt() + waterIntake.toInt()
                if (binding.tvGlassAmt.text.toString().toInt() > 0) {
                    alertDialog.dismiss()
                    UploadData(Constants.WATER, glasses.toDouble(), context)
                } else {
                    Toast.makeText(context, "Please add water intake", Toast.LENGTH_SHORT).show()
                }
            }

            if (!alertDialog.isShowing) {
                alertDialog.show()
            }

            val displayRectangle = Rect()
            val window = window
            window.decorView.getWindowVisibleDisplayFrame(displayRectangle)
            alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            alertDialog.window!!.setLayout(
                (displayRectangle.width() * 0.8f).toInt(),
                RelativeLayout.LayoutParams.WRAP_CONTENT
            )
            alertDialog.setOnDismissListener {
                //Todo
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
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
        val actualGlasses = glasses + 1
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


    private fun FetchQuizReward() {
        if (progressDialog != null && progressDialog.isShowing) progressDialog.show()
        val activityRewardRequest = ActivityRewardRequest(trasactionId, featureName)
        val call = apiInterface.FetchQuizReward(SharedPref.getAuthToken(), activityRewardRequest)

        call.enqueue(object : Callback<CommonSuccessResponse?> {
            override fun onResponse(
                call: Call<CommonSuccessResponse?>,
                response: Response<CommonSuccessResponse?>
            ) {
                if (progressDialog != null && progressDialog.isShowing) progressDialog.dismiss()
                if (response.isSuccessful && response.body() != null) {
                    trasactionId = null
                    featureName = null
                    if (response.body()!!.quizathonRewardData != null) {
                        getQuizathonRewardPopup(response.body()!!.quizathonRewardData)
                    }
                }
            }

            override fun onFailure(call: Call<CommonSuccessResponse?>, t: Throwable) {
                if (progressDialog != null && progressDialog.isShowing) progressDialog.dismiss()
                t.printStackTrace()
            }
        })
    }

    override fun onRewardRecieved(spinRewardsData: AssignRewardsResponse.SpinRewardsData?) {
        TODO("Not yet implemented")
    }

    override fun onQuizRewardRecieved(quizathonRewardData: QuizathonRewardData?) {
        TODO("Not yet implemented")
    }

    fun callQuizAfterPointBurn(quizathonModel: QuizathonModel) {
        val intent = Intent(context, QuizathonActivity::class.java)
        intent.putExtra(
            "data",
            if ((quizathonModel.answerJson != null && !quizathonModel.isQuizCompleted)
            ) quizathonModel.answerJson
            else quizathonModel.questionJson
        )
        intent.putExtra("CategoryName", quizathonModel.quizTitle)
        intent.putExtra("Category", quizathonModel.quizDesc)
        intent.putExtra("rewardDate", quizathonModel.rewardDate)
        intent.putExtra("comingFrom", "")
        intent.putExtra("IsRetake", quizathonModel.retake)
        intent.putExtra("RetakeId", quizathonModel.retakeId)
        intent.putExtra("qId", quizathonModel.quizId)
        intent.putExtra("type", "Quizathon")
        intent.putExtra("quizModel", quizathonModel)
        context.startActivity(intent)
        quizathonModelRegister = null
        burnType = ""
    }
}