package com.wyh.happyyousdk.dashboard

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.view.LayoutInflater
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.gson.Gson
import com.wyh.happyyousdk.APIEncryption.APILogs.activityTracker
import com.wyh.happyyousdk.APIEncryption.RetrofitHandler
import com.wyh.happyyousdk.AboutUsActivity
import com.wyh.happyyousdk.ChallangesModule.Activities.BadgesActivity
import com.wyh.happyyousdk.ChallangesModule.Activities.ChallangesActivity
import com.wyh.happyyousdk.Goals.Activities.GoalHistory
import com.wyh.happyyousdk.Goals.Activities.GoalsDashboard
import com.wyh.happyyousdk.Goals.Activities.HistoryDetailActivity
import com.wyh.happyyousdk.Goals.Activities.IndividualGoalsDashboard
import com.wyh.happyyousdk.Goals.Activities.OtherGoals
import com.wyh.happyyousdk.Goals.Activities.RecommendedGoalsDetails
import com.wyh.happyyousdk.NeedSupportActivity
import com.wyh.happyyousdk.R
import com.wyh.happyyousdk.Sonde.Activities.RespiratoryCheck
import com.wyh.happyyousdk.SpinWheel.Activities.spinwheelreward.SpinWheelRewardsActivity
import com.wyh.happyyousdk.WebActivity
import com.wyh.happyyousdk.WellBeingActivity
import com.wyh.happyyousdk.WellBeingDisclaimerActivity
import com.wyh.happyyousdk.absorb.HealthHacksActivity
import com.wyh.happyyousdk.addFamily.AddFamilyActivity
import com.wyh.happyyousdk.addFamily.AddFamilyListActivity
import com.wyh.happyyousdk.contacts.ContactsActivityNew
import com.wyh.happyyousdk.dashboard.helper.NewDashboardHelper
import com.wyh.happyyousdk.dass21.Dass21AnalysisActivity
import com.wyh.happyyousdk.dass21.Dass21QuestionsActivity
import com.wyh.happyyousdk.databinding.ShareOptionPopUpBinding
import com.wyh.happyyousdk.diary.AddDiaryActivity
import com.wyh.happyyousdk.diary.NewDiaryActivity
import com.wyh.happyyousdk.diary.model.DiaryEventListResponse
import com.wyh.happyyousdk.ehr.AddNewEhrRecord
import com.wyh.happyyousdk.ehr.EhrActivity
import com.wyh.happyyousdk.ehr.HealthLockerHistoryActivity
import com.wyh.happyyousdk.fileshare.ActivityFileShareList
import com.wyh.happyyousdk.happyMarket.HappyMartCategory
import com.wyh.happyyousdk.happyMarket.HappyMartDisclaimerActivity
import com.wyh.happyyousdk.happyMarket.MoreMyPurchasesActivity
import com.wyh.happyyousdk.happyMarket.MyOrderActivity
import com.wyh.happyyousdk.happyMarket.NewHappyMartActivity
import com.wyh.happyyousdk.happyMarket.WebinarWebview
import com.wyh.happyyousdk.heartAge.HeartAgeAnalysisActivity
import com.wyh.happyyousdk.heartAge.HeartAgeQuestionsActivity
import com.wyh.happyyousdk.hra.HRAAnalysisActivity
import com.wyh.happyyousdk.hra.HRAQuestionsActivity
import com.wyh.happyyousdk.ice.AddEmergencyContactActivity
import com.wyh.happyyousdk.ice.CPRAndFirstAddActivity
import com.wyh.happyyousdk.ice.ICEDashboardActivity
import com.wyh.happyyousdk.ice.SOSActivity
import com.wyh.happyyousdk.model.RedirectionModel
import com.wyh.happyyousdk.model.request.IntegrationIdRequest
import com.wyh.happyyousdk.model.request.heartAge.GetHeartAgeAnalysisRequest
import com.wyh.happyyousdk.model.response.GetIndividualGoalResponse
import com.wyh.happyyousdk.model.response.GoalHistoryResponse
import com.wyh.happyyousdk.model.response.RecomendedGoalsResponse
import com.wyh.happyyousdk.model.response.dass21.DassAnalysisResponse
import com.wyh.happyyousdk.model.response.getAnalysis.GetAnalysisResponse
import com.wyh.happyyousdk.model.response.heartAge.HeartAgeAnalysisResponse
import com.wyh.happyyousdk.network.ApiClientWyh
import com.wyh.happyyousdk.network.ApiInterfaceWyh
import com.wyh.happyyousdk.notification.NotificationDrawerActivity
import com.wyh.happyyousdk.policyDetail.PolicySearchActivity
import com.wyh.happyyousdk.policyDetails.PolicyDetailsActivity
import com.wyh.happyyousdk.profile.AddUserDetails
import com.wyh.happyyousdk.profile.ProfileActivity
import com.wyh.happyyousdk.profile.dialog.AddUserDetailsDialog
import com.wyh.happyyousdk.quiz.QuizActivity
import com.wyh.happyyousdk.quiz.QuizScoreActivity
import com.wyh.happyyousdk.quizathon.QuizathonViewAllActivity
import com.wyh.happyyousdk.reminders.RemindersActivity
import com.wyh.happyyousdk.rewards.MoreCurrentActivities
import com.wyh.happyyousdk.rewards.MoreTopUpsActivity
import com.wyh.happyyousdk.rewards.MoreUpcomingActivities
import com.wyh.happyyousdk.rewards.PendingActivityDashboard
import com.wyh.happyyousdk.rewards.RewardsActivity
import com.wyh.happyyousdk.rewards.RewardsCollectiblesActivity
import com.wyh.happyyousdk.rewards.RewardsHistoryActivity
import com.wyh.happyyousdk.rewards.UnscratchedTokensActivity
import com.wyh.happyyousdk.syncDevice.SyncDeviceActivity
import com.wyh.happyyousdk.trends.TrendsActivity
import com.wyh.happyyousdk.unwind.UnwindActivity
import com.wyh.happyyousdk.utils.Analytics
import com.wyh.happyyousdk.utils.CommonUtils
import com.wyh.happyyousdk.utils.Constants
import com.wyh.happyyousdk.utils.Constants.ACTIVEHOURS
import com.wyh.happyyousdk.utils.Constants.CALORIE
import com.wyh.happyyousdk.utils.Constants.COMMUNITY
import com.wyh.happyyousdk.utils.Constants.DASHBOARD_BANNER
import com.wyh.happyyousdk.utils.Constants.HappyMartDevice
import com.wyh.happyyousdk.utils.Constants.INDIVIDUAL
import com.wyh.happyyousdk.utils.Constants.SLEEP
import com.wyh.happyyousdk.utils.Constants.STEPS
import com.wyh.happyyousdk.utils.Constants.SearchKey
import com.wyh.happyyousdk.utils.Constants.WATER
import com.wyh.happyyousdk.utils.Constants.WEIGHT
import com.wyh.happyyousdk.utils.SharedPref
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Locale

object RedirectionMethod {

    var thoughOfTheDayList: List<String>? = listOf()
    var getEventListData: List<DiaryEventListResponse.Datum> = ArrayList()
    var levelList: ArrayList<String> = arrayListOf()
    var engList: ArrayList<String> = arrayListOf()
    var topUpList: ArrayList<String> = arrayListOf()
    var levelListData: ArrayList<DiaryEventListResponse.Datum> = arrayListOf()
    var engListData: ArrayList<DiaryEventListResponse.Datum> = arrayListOf()
    var topUpListData: ArrayList<DiaryEventListResponse.Datum> = arrayListOf()
    lateinit var heartAgeAnalysisResponse: HeartAgeAnalysisResponse

    fun getNudgeImage(redirectionKey: String): Int {
        when (redirectionKey.trim()) {
            "water" -> {
                return R.drawable.ic_water
            }

            "Banner" -> return R.drawable.banner_icon
            "musiclibrary", "meditation" -> return R.drawable.ic_zen_zone
            "sleep" -> return R.drawable.ic_sleep_star
            "steps" -> return R.drawable.ic_steps
            "riddle" -> return R.drawable.ic_joke
            "reward", "winnings", "Rewards" -> return R.drawable.ic_rewards_badge
            "syncdevice", "Sync your device" -> return R.drawable.ic_sync_device
            "hra" ->
                return R.drawable.ic_know_your_health

            "refer" -> return R.drawable.ic_share
            "communityinvite" -> return R.drawable.ic_tribe
            "tribemessage" -> return R.drawable.ic_tribe
            "healthhacks" -> return R.drawable.ic_health_tv
            "challenge", "challenges", "Challanges" -> return R.drawable.ic_challenge
            "referalfriendreg", "referalfriendupdate", "Profile " -> return R.drawable.ic_profile
            "unwind" -> return R.drawable.ic_joke
            "ice" -> return R.drawable.ice
            "lifestyle" -> return R.drawable.ic_profile
            "quiz" -> return R.drawable.ic_quiz
            "diary" -> return R.drawable.ic_diary_icon
            "video" -> return R.drawable.ic_health_tv
            "videos" -> return R.drawable.ic_health_tv
            "heartage" -> return R.drawable.ic_heart_age
            "jokes" -> return R.drawable.ic_joke
            "articles" -> return R.drawable.ic_quick_read
            "reminders" -> return R.drawable.ic_reminders
            "devices" -> return R.drawable.ic_sync_device //Happy mart
            "calcount" -> return R.drawable.ic_cal_burned
            "facescan" -> return R.drawable.facescan_new
            "affirmations" -> return R.drawable.ic_affirmation
            "immunity" -> return R.drawable.ic_immunity
            "reads" -> return R.drawable.ic_quick_read
            "HealthAssure" -> return R.drawable.ic_diary_icon //Happy mart
            "connected" -> return R.drawable.ic_sync_device //Happy mart
            "visitor" -> return R.drawable.ic_diary_icon //Happy mart
            "GetVisit" -> return R.drawable.ic_diary_icon //Happy mart
            "medpay" -> return R.drawable.ic_diary_icon //Happy mart
            "pharmeasy" -> return R.drawable.ic_diary_icon //Happy mart
            "mrf" -> return R.drawable.ic_diary_icon //Happy mart
            "Amaha" -> return R.drawable.ic_diary_icon //Happy mart
            "fitter", "hobby" -> return R.drawable.ic_diary_icon //Happy mart
            "actofit" -> return R.drawable.ic_diary_icon //Happy mart
            "Tripstacc" -> return R.drawable.ic_diary_icon //Happy mart
            "Advantage Club" -> return R.drawable.ic_diary_icon //Happy mart
            "Shopsta" -> return R.drawable.ic_diary_icon //Happy mart
            "Active hours" -> return R.drawable.ic_stand_time
            "Weight watcher" -> return R.drawable.ic_ideal_weight
            "Know your wellbeing" -> return R.drawable.ic_know_your_wellbeing
            "DAS" -> return R.drawable.ic_dass_21
            "Health Locker" -> return R.drawable.ic_my_record // no Icon but using document icon
            "Happy Mart" -> return R.drawable.ic_happy_services // no Icon but using other icon
            "Winning" -> return R.drawable.ic_winnings
            "Diagnostics" -> return R.drawable.ic_diagnostic
            "Hospitals" -> return R.drawable.ic_hospital
            "Doctors prescription" -> return R.drawable.ic_doctors_prescription
            "Diet plans" -> return R.drawable.ic_diet_plan
            "Fitness plus" -> return R.drawable.ic_fitness
            "Vaccination certificates" -> return R.drawable.ic_vaccination_certificate
            "My photos" -> return R.drawable.ic_my_photos
            "Other documents" -> return R.drawable.ic_other_document
            "Goals" -> return R.drawable.ic_diary_icon // no icon
            "Activities" -> return R.drawable.ic_activities
            "Top up", "EnG/Topup" -> return R.drawable.ic_top_ups
            "Tribe meter" -> return R.drawable.ic_diary_icon // no icon
            "Update details" -> return R.drawable.ic_diary_icon // no icon
            "Add family" -> return R.drawable.ic_family_hub
            "Kotak policy" -> return R.drawable.ic_kotak_policy
            "Notification" -> return R.drawable.ic_notification
            "Scan QR" -> return R.drawable.ic_qrwhite
            "Act-o-meter" -> return R.drawable.ic_diary_icon // no  icon
            "Mental Wellness" -> return R.drawable.mental_health
            "Respiratory Health" -> return R.drawable.respiratory_
            "Chrome" -> return R.drawable.chrome_icon
            "webinar" -> return R.drawable.webinar_icon
            else -> return R.drawable.nudge_bear

        }
    }

    fun redirectionScreen(redirectionModel: RedirectionModel, context: Context) {
        try {
            if (redirectionModel.redirectionKey != null && !redirectionModel.redirectionKey.equals(
                    "",
                    true
                )
            ) {
                if (redirectionModel.redirectionUrl != null && redirectionModel.redirectionUrl != "") {
                    context.startActivity(
                        Intent(context, HappyMartCategory::class.java)
                            .putExtra("comingFrom", DASHBOARD_BANNER)
                            .putExtra("selectedPartner", redirectionModel.toolbarname)
                            .putExtra("toolbarname", redirectionModel.toolbarname)
                            .putExtra("disclaimer", redirectionModel.disclaimer)
                            .putExtra("redirectionURL", redirectionModel.redirectionUrl)
                            .putExtra("disclaimerURL", redirectionModel.disclaimerURL)
                    )
                } else {
                    when (redirectionModel.redirectionKey.trim()) {
                        "water" ->{
                            activityTracker("A_PlayAndWinQuizathon_ActivityPopUpRedirection_Water",context);
                            context.startActivity(
                                    Intent(context, TrendsActivity::class.java)
                                            .putExtra("activityType", WATER)
                            )
                        }


                        "musiclibrary", "meditation" ->{
                            activityTracker("A_PlayAndWinQuizathon_ActivityPopUpRedirection_Meditation",context);
                            context.startActivity(
                                    Intent(context, TrendsActivity::class.java)
                                            .putExtra("activityType", Constants.MEDITATION)
                            )
                        }

                        "sleep" -> context.startActivity(
                            Intent(context, TrendsActivity::class.java)
                                .putExtra("activityType", SLEEP)
                        )

                        "steps" -> context.startActivity(
                            Intent(context, TrendsActivity::class.java)
                                .putExtra("activityType", STEPS)
                        )

                        "reward", "winnings" -> context.startActivity(
                            Intent(
                                context,
                                RewardsActivity::class.java
                            )
                        )

                        "syncdevice", "Sync your device" -> context.startActivity(
                            Intent(
                                context,
                                SyncDeviceActivity::class.java
                            )
                        )

                        "hra" -> {
                            if (NewDashboardHelper.activityName != null && NewDashboardHelper.activityName.equals("hra")) {
                                context.startActivity(
                                    Intent(
                                        context,
                                        HRAQuestionsActivity::class.java
                                    )
                                )
                            } else {
                                if (SharedPref.getHRAAnalysis() != "") {
                                    val getAnalysisResponse: GetAnalysisResponse =
                                        Gson().fromJson<GetAnalysisResponse>(
                                            SharedPref.getHRAAnalysis(),
                                            GetAnalysisResponse::class.java
                                        )
                                    val isAnalysis =
                                        getAnalysisResponse != null && getAnalysisResponse.analysisData != null && getAnalysisResponse.analysisData.score > 0
                                    if (isAnalysis) {
                                        context.startActivity(
                                            Intent(
                                                context,
                                                HRAAnalysisActivity::class.java
                                            )
                                        )
                                    } else {
                                        context.startActivity(
                                            Intent(
                                                context,
                                                HRAQuestionsActivity::class.java
                                            )
                                        )
                                    }
                                } else {
                                    var isShown: Boolean = SharedPref.getWellBeingIntroShownHealth()
                                    if (!isShown) {
                                        SharedPref.putWellBeingIntroShownHealth(true)
                                        val intent =
                                            Intent(context, WellBeingDisclaimerActivity::class.java)
                                        intent.putExtra("came_from", "KnowYourHealth")
                                        intent.putExtra("CategoryName", "Health Score")
                                        intent.putExtra("isAnalysis", false)
                                        intent.putExtra("shownScreen", true)
                                        context.startActivity(intent)
                                    } else {
                                        context.startActivity(
                                            Intent(
                                                context,
                                                HRAQuestionsActivity::class.java
                                            )
                                        )

                                    }
                                }
                            }

                        }

                        "refer", "referalfriendreg", "referalfriendupdate" ->
                            context.startActivity(
                                Intent(context, ContactsActivityNew::class.java)
                                    .putExtra("comingFrom", "")
                            )

                        "healthhacks" -> context.startActivity(
                            Intent(
                                context,
                                HealthHacksActivity::class.java
                            )
                        )

                        "challenge", "challenges", "Challanges" ->{
                            activityTracker("A_PlayAndWinQuizathon_ActivityPopUpRedirection_Challenges",context)
                            context.startActivity(
                                    Intent(
                                            context,
                                            ChallangesActivity::class.java
                                    )
                            )

                        }
                        "Profile" -> context.startActivity(
                            Intent(
                                context,
                                ProfileActivity::class.java
                            )
                        )

                        "unwind" -> {context.startActivity(
                            Intent(context, UnwindActivity::class.java)
                                .putExtra("comingFrom", "redirectionUnwind")
                        )
                        activityTracker("A_PlayAndWinQuizathon_ActivityPopUpRedirection_unwind",context);
                        }

                        "ice" -> context.startActivity(
                            Intent(
                                context,
                                ICEDashboardActivity::class.java
                            )
                        )

                        "lifestyle" -> context.startActivity(
                            Intent(
                                context,
                                LifestyleActivity::class.java
                            )
                        )

                        "quiz" ->{
                            activityTracker("A_PlayAndWinQuizathon_ActivityPopUpRedirection_Quiz",context)
                            val intent = Intent(context,QuizathonViewAllActivity::class.java).apply {
                                putExtra("type", "quiz")
                                putExtra("quiz_cat", "normal")
                                putExtra("name", "Play and Learn")
                            }
                            context.startActivity(intent)
                        }
                        "trivia" ->{
                            activityTracker("A_PlayAndWinQuizathon_ActivityPopUpRedirection_trivia",context);
                            val intent = Intent(context,QuizathonViewAllActivity::class.java).apply {
                                putExtra("type", "quiz")
                                putExtra("quiz_cat", "trivia")
                                putExtra("name", "Play and Learn")
                            }
                            context.startActivity(intent)
                        }

                        "bannerquiz" -> {
                            if (redirectionModel.bannerQuizModel != null) {
                                if (redirectionModel.isQuizCompleted == true) {
                                    val intent = Intent(context, QuizScoreActivity::class.java)
                                    intent.putExtra(
                                        "data",
                                        Gson().toJson(redirectionModel.bannerQuizModel?.answerJson)
                                    )
                                    intent.putExtra(
                                        "your_score",
                                        redirectionModel.bannerQuizModel?.userScore
                                    )
                                    intent.putExtra(
                                        "total_score",
                                        redirectionModel.bannerQuizModel?.totalQuestions
                                    )
                                    intent.putExtra(
                                        "CategoryName",
                                        redirectionModel.bannerQuizModel?.category
                                    )
                                    intent.putExtra("comingFrom", "bannerQuiz")
                                    intent.putExtra("rewardDate", redirectionModel.rewardDate)
                                    context.startActivity(intent)
                                } else {
                                    val intent = Intent(context, QuizActivity::class.java)
                                    intent.putExtra("comingFrom", "bannerQuiz")
                                    intent.putExtra(
                                        "data",
                                        Gson().toJson(redirectionModel.bannerQuizModel?.answerJson)
                                    )
                                    intent.putExtra(
                                        "CategoryName",
                                        redirectionModel.bannerQuizModel?.category
                                    )
                                    intent.putExtra("qId", redirectionModel.bannerQuizModel?.quizId)
                                    intent.putExtra("rewardDate", redirectionModel.rewardDate)
                                    context.startActivity(intent)
                                }
                            }
                        }

                        "diary" -> context.startActivity(
                            Intent(
                                context,
                                NewDiaryActivity::class.java
                            )
                        )

                        "video" -> {NewDashboardHelper.getAbsorbData(context, "video")
                            activityTracker("A_PlayAndWinQuizathon_ActivityPopUpRedirection_Video",context);
                        }

                        "videos" -> {
                            activityTracker("A_PlayAndWinQuizathon_ActivityPopUpRedirection_Video",context);
                            NewDashboardHelper.getAbsorbData(context, "videos")
                        }

                        "heartage" -> {
                            activityTracker("A_PlayAndWinQuizathon_ActivityPopUpRedirection_heartage",context);
                            NewDashboardHelper.getHeartAnalysis(context, "")
                        }

                        "jokes" -> {
                            SharedPref.putUnwindNotification("jokes")
                            context.startActivity(
                                Intent(context, UnwindActivity::class.java)
                                    .putExtra("comingFrom", "redirectionJoke")
                            )
                        }

                        "articles" ->{
                            activityTracker("A_PlayAndWinQuizathon_ActivityPopUpRedirection_article",context);
                            NewDashboardHelper.getAbsorbData(context, "blogs")
                        }

                        "reminders" -> context.startActivity(
                            Intent(
                                context,
                                RemindersActivity::class.java
                            )
                        )

                        "devices" -> context.startActivity(
                            Intent(context, HappyMartDisclaimerActivity::class.java)
                                .putExtra("came_from", HappyMartDevice)
                                .putExtra("toolbarname", redirectionModel.toolbarname)
                        )

                        "calcount" -> context.startActivity(
                            Intent(context, TrendsActivity::class.java)
                                .putExtra("activityType", CALORIE)
                        )


                        "affirmations" -> {
                            SharedPref.putUnwindNotification("affirmations")
                            context.startActivity(
                                Intent(context, UnwindActivity::class.java)
                                    .putExtra("comingFrom", "redirectionAffirmation")
                            )

                        }

                        "immunity" -> NewDashboardHelper.getIRAScore(context, "")

                        "reads" -> NewDashboardHelper.getAbsorbData(context, "blogs")

                        "Need support" -> context.startActivity(
                            Intent(
                                context,
                                NeedSupportActivity::class.java
                            )
                        )

                        "About us" -> context.startActivity(
                            Intent(
                                context, AboutUsActivity::class.java
                            )
                        )

                        "HealthAssure" -> context.startActivity(
                            Intent(context, HappyMartDisclaimerActivity::class.java)
                                .putExtra("came_from", DASHBOARD_BANNER)
                                .putExtra("selectedPartner", "health_assure")
                                .putExtra("toolbarname", redirectionModel.toolbarname)
                                .putExtra("disclaimer", redirectionModel.disclaimer)
                                .putExtra("disclaimerURL", redirectionModel.disclaimerURL)
                        )

                        "connected" -> context.startActivity(
                            Intent(context, HappyMartDisclaimerActivity::class.java)
                                .putExtra("came_from", DASHBOARD_BANNER)
                                .putExtra("selectedPartner", "connected")
                                .putExtra("toolbarname", redirectionModel.toolbarname)
                                .putExtra("disclaimer", redirectionModel.disclaimer)
                                .putExtra("disclaimerURL", redirectionModel.disclaimerURL)
                        )

                        "visitor" -> context.startActivity(
                            Intent(context, HappyMartDisclaimerActivity::class.java)
                                .putExtra("came_from", DASHBOARD_BANNER)
                                .putExtra("selectedPartner", "visitor")
                                .putExtra("toolbarname", redirectionModel.toolbarname)
                                .putExtra("disclaimer", redirectionModel.disclaimer)
                                .putExtra("disclaimerURL", redirectionModel.disclaimerURL)
                        )

                        "GetVisit" -> context.startActivity(
                            Intent(context, HappyMartDisclaimerActivity::class.java)
                                .putExtra("came_from", DASHBOARD_BANNER)
                                .putExtra(
                                    "selectedPartner",

                                    "GetVisit"
                                )
                                .putExtra("toolbarname", redirectionModel.toolbarname)
                                .putExtra("disclaimer", redirectionModel.disclaimer)
                                .putExtra("disclaimerURL", redirectionModel.disclaimerURL)
                        )

                        "medpay" -> context.startActivity(
                            Intent(context, HappyMartDisclaimerActivity::class.java)
                                .putExtra("came_from", DASHBOARD_BANNER)
                                .putExtra("selectedPartner", "medpay")
                                .putExtra("toolbarname", redirectionModel.toolbarname)
                                .putExtra("disclaimer", redirectionModel.disclaimer)
                                .putExtra("disclaimerURL", redirectionModel.disclaimerURL)
                        )

                        "pharmeasy" -> context.startActivity(
                            Intent(context, HappyMartDisclaimerActivity::class.java)
                                .putExtra("came_from", DASHBOARD_BANNER)
                                .putExtra("selectedPartner", "pharmeasy")
                                .putExtra("toolbarname", redirectionModel.toolbarname)
                                .putExtra("disclaimer", redirectionModel.disclaimer)
                                .putExtra("disclaimerURL", redirectionModel.disclaimerURL)
                        )

                        "mrf" -> context.startActivity(
                            Intent(context, HappyMartDisclaimerActivity::class.java)
                                .putExtra("came_from", DASHBOARD_BANNER)
                                .putExtra("selectedPartner", "mrf")
                                .putExtra("toolbarname", redirectionModel.toolbarname)
                                .putExtra("disclaimer", redirectionModel.disclaimer)
                                .putExtra("disclaimerURL", redirectionModel.disclaimerURL)
                        )

                        "Amaha" -> context.startActivity(
                            Intent(context, HappyMartDisclaimerActivity::class.java)
                                .putExtra("came_from", DASHBOARD_BANNER)
                                .putExtra("selectedPartner", "Amaha")
                                .putExtra("toolbarname", redirectionModel.toolbarname)
                                .putExtra("disclaimer", redirectionModel.disclaimer)
                                .putExtra("disclaimerURL", redirectionModel.disclaimerURL)
                        )

                        "fitter", "hobby" -> context.startActivity(
                            Intent(context, HappyMartDisclaimerActivity::class.java)
                                .putExtra("came_from", DASHBOARD_BANNER)
                                .putExtra("selectedPartner", "fitter")
                                .putExtra("toolbarname", redirectionModel.toolbarname)
                                .putExtra("disclaimer", redirectionModel.disclaimer)
                                .putExtra("disclaimerURL", redirectionModel.disclaimerURL)
                        )

                        "actofit" -> context.startActivity(
                            Intent(context, HappyMartDisclaimerActivity::class.java)
                                .putExtra("came_from", DASHBOARD_BANNER)
                                .putExtra("selectedPartner", "actofit")
                                .putExtra("toolbarname", redirectionModel.toolbarname)
                                .putExtra("disclaimer", redirectionModel.disclaimer)
                                .putExtra("disclaimerURL", redirectionModel.disclaimerURL)
                        )

                        "Tripstacc" -> context.startActivity(
                            Intent(context, HappyMartDisclaimerActivity::class.java)
                                .putExtra("came_from", DASHBOARD_BANNER)
                                .putExtra("selectedPartner", "Tripstacc")
                                .putExtra("toolbarname", redirectionModel.toolbarname)
                                .putExtra("disclaimer", redirectionModel.disclaimer)
                                .putExtra("disclaimerURL", redirectionModel.disclaimerURL)
                        )

                        "Advantage Club", "Advantage club" -> context.startActivity(
                            Intent(context, HappyMartDisclaimerActivity::class.java)
                                .putExtra("came_from", DASHBOARD_BANNER)
                                .putExtra("selectedPartner", "Advantage Club")
                                .putExtra("toolbarname", redirectionModel.toolbarname)
                                .putExtra("disclaimer", redirectionModel.disclaimer)
                                .putExtra("disclaimerURL", redirectionModel.disclaimerURL)
                        )

                        "Shopsta" -> context.startActivity(
                            Intent(context, HappyMartDisclaimerActivity::class.java)
                                .putExtra("came_from", DASHBOARD_BANNER)
                                .putExtra("selectedPartner", "Shopsta")
                                .putExtra("toolbarname", redirectionModel.toolbarname)
                                .putExtra("disclaimer", redirectionModel.disclaimer)
                                .putExtra("disclaimerURL", redirectionModel.disclaimerURL)
                        )

                        "Active hours" -> context.startActivity(
                            Intent(context, TrendsActivity::class.java)
                                .putExtra("activityType", ACTIVEHOURS)
                        )

                        "Weight watcher" -> context.startActivity(
                            Intent(context, TrendsActivity::class.java)
                                .putExtra("activityType", WEIGHT)
                        )

                        "Know your wellbeing" -> context.startActivity(
                            Intent(
                                context,
                                WellBeingActivity::class.java
                            )
                        )

                        "addfamilymember" -> {
                            activityTracker("A_PlayAndWinQuizathon_ActivityPopUpRedirection_addfamilymember",context);
                            context.startActivity(
                                    Intent(
                                            context,
                                            AddFamilyActivity::class.java
                                    )
                            )
                        }

                        "updateusername" -> {
                            activityTracker("A_PlayAndWinQuizathon_ActivityPopUpRedirection_updateusername",context);
                            AddUserDetailsDialog(context).show();

                        }

                        "uploadehr" -> {
                            activityTracker("A_PlayAndWinQuizathon_ActivityPopUpRedirection_uploadehr",context);
                            context.startActivity(
                                    Intent(
                                            context,
                                            AddNewEhrRecord::class.java
                                    )
                            )
                        }

                        "DAS" -> getDassAnalysis(context)

                        "das" -> getDassAnalysis(context)


                        "Diagnostics" -> SearchActivity().getHealthRecord("Diagnostics", context)

                        "Hospitals" -> SearchActivity().getHealthRecord("Hospitals", context)

                        "Doctors prescription" -> SearchActivity().getHealthRecord(
                            "Doctors prescription",
                            context
                        )

                        "Diet plans" -> SearchActivity().getHealthRecord("Diet plans", context)

                        "Fitness plus" -> SearchActivity().getHealthRecord("Fitness plus", context)

                        "Vaccination certificates" -> SearchActivity().getHealthRecord(
                            "Vaccination certificates",
                            context
                        )

                        "My photos" -> SearchActivity().getHealthRecord("My photos", context)

                        "Other documents" -> SearchActivity().getHealthRecord(
                            "Other documents",
                            context
                        )


                        "Face scan reports" -> SearchActivity().getHealthRecord(
                            "Face scan reports",
                            context
                        )


                        "Health Locker" -> context.startActivity(
                            Intent(
                                context,
                                EhrActivity::class.java
                            )
                        )

                        "Happy Mart" -> context.startActivity(
                            Intent(
                                context,
                                NewHappyMartActivity::class.java
                            )
                        )

                        "Winning" -> context.startActivity(
                            Intent(
                                context,
                                RewardsActivity::class.java
                            )
                        )

                        "spinwheel" -> context.startActivity(
                            Intent(
                                context,
                                SpinWheelRewardsActivity::class.java
                            )
                        )

                        "Goals" -> context.startActivity(
                            Intent(
                                context,
                                GoalsDashboard::class.java
                            )
                        )

                        "Activities" -> {
                            val i = Intent(context, MoreCurrentActivities::class.java)
                            context.startActivity(i)
                        }

                        "Top up" -> {
                            val i = Intent(context, MoreTopUpsActivity::class.java)
                            context.startActivity(i)
                        }


                        "Update details" -> context.startActivity(
                            Intent(
                                context,
                                AddUserDetails::class.java
                            )
                        )
                        //"updateprofile" -> context.startActivity(Intent(context,AddUserDetails::class.java))
                        "updateprofile" -> {
                            activityTracker("A_PlayAndWinQuizathon_ActivityPopUpRedirection_updateprofile",context)
                            AddUserDetailsDialog(context).show()
                        }

                        "Add family member" -> context.startActivity(
                            Intent(
                                context,
                                AddFamilyActivity::class.java
                            )
                        )

                        "Add family" -> context.startActivity(
                            Intent(
                                context,
                                AddFamilyListActivity::class.java
                            )
                        )

                        "Kotak policy" -> context.startActivity(
                            Intent(
                                context,
                                PolicyDetailsActivity::class.java
                            )
                        )

                        "Notification" -> {
                            context.startActivity(
                                Intent(
                                    context,
                                    NotificationDrawerActivity::class.java
                                )
                            )
                        }

                        "Scan QR" -> context.startActivity(
                            Intent(
                                context,
                                ScanQRActivity::class.java
                            )
                        )

                        "Mental Wellness" -> {
                            context.startActivity(
                                Intent(context, RespiratoryCheck::class.java)
                                    .putExtra("comingFrom", "metalFitness")
                            )
                        }

                        "Respiratory Health" -> {
                            context.startActivity(
                                Intent(context, RespiratoryCheck::class.java)
                                    .putExtra("comingFrom", "respiratory")
                            )
                        }

                        "Chrome" ->
                            if (redirectionModel.redirectionUrl!!.contains("zoom")) {
                                val uri = Uri.parse(
                                    redirectionModel.redirectionUrl.replace(
                                        "zoomus://",
                                        "https://"
                                    )
                                )
                                val sendIntent = Intent(Intent.ACTION_VIEW, uri)
                                context.startActivity(sendIntent)
                            } else {
                                context.startActivity(
                                    Intent(context, HappyMartCategory::class.java)
                                        .putExtra("comingFrom", DASHBOARD_BANNER)
                                        .putExtra("selectedPartner", redirectionModel.toolbarname)
                                        .putExtra("toolbarname", redirectionModel.toolbarname)
                                        .putExtra("disclaimer", redirectionModel.disclaimer)
                                        .putExtra("redirectionURL", redirectionModel.redirectionUrl)
                                        .putExtra("disclaimerURL", redirectionModel.disclaimerURL)
                                )
                            }

                        "winnings scratch and win", "Collectibles" -> {
                            context.startActivity(
                                Intent(
                                    context,
                                    RewardsCollectiblesActivity::class.java
                                )
                            )
                        }

                        "Upcoming activities" -> context.startActivity(
                            Intent(
                                context,
                                MoreUpcomingActivities::class.java
                            )
                        )

                        "winnings earn and grab" -> context.startActivity(
                            Intent(context, RewardsActivity::class.java)
                                .putExtra("currentIndex", 1)
                        )

                        "Grab opportunities" -> context.startActivity(
                            Intent(
                                context,
                                UnscratchedTokensActivity::class.java
                            )
                        )

                        "Grab opportunities activities" -> context.startActivity(
                            Intent(
                                context,
                                PendingActivityDashboard::class.java
                            )
                        )

                        "History" -> context.startActivity(
                            Intent(context, RewardsHistoryActivity::class.java)
                                .putExtra("comingFrom", "redirection")
                        )

                        "Badges" -> context.startActivity(
                            Intent(
                                context,
                                BadgesActivity::class.java
                            )
                        )

                        "search" -> NewDashboardHelper.searchFeature(
                            context as Activity,
                            "search"
                        );

                        "sos" -> context.startActivity(Intent(context, SOSActivity::class.java));

                        "emergency contact" -> context.startActivity(
                            Intent(
                                context,
                                AddEmergencyContactActivity::class.java
                            )
                        )

                        "tutorials" -> context.startActivity(
                            Intent(
                                context,
                                CPRAndFirstAddActivity::class.java
                            )
                        )

                        "health locker history" -> context.startActivity(
                            Intent(context, HealthLockerHistoryActivity::class.java)
                                .putExtra(SearchKey, "")
                        )

                        "happymart orders" -> context.startActivity(
                            Intent(context, MyOrderActivity::class.java)
                                .putExtra("vendor", "")
                        )

                        "happymart my purchase" -> context.startActivity(
                            Intent(
                                context,
                                MoreMyPurchasesActivity::class.java
                            )
                        )

                        "add individual goals" -> context.startActivity(
                            Intent(
                                context,
                                IndividualGoalsDashboard::class.java
                            )
                        )

                        "add individual goals other" -> getIndividualGoal(context)

                        "recommended goals" -> getRecommendedGoal(context)

                        "goals history" -> context.startActivity(
                            Intent(
                                context,
                                GoalHistory::class.java
                            )
                        )

                        "completed goals" -> getGoalHistory(context, "completed")

                        "deleted goals" -> getGoalHistory(context, "delete")

                        "heart age" -> getHeartAnalysis(context, "")

                        "thought of the day" -> {
                            context.startActivity(
                                Intent(context, UnwindActivity::class.java)
                                    .putExtra("comingFrom", "redirectionThought")
                            )
                        }

                        "my diary activities" -> context.startActivity(
                            Intent(context, NewDiaryActivity::class.java)
                                .putExtra("actvities", 1)
                        )

                        "my diary create new journal" -> context.startActivity(
                            Intent(context, AddDiaryActivity::class.java)
                                .putExtra("came_from", "redirection")
                        )

                        "my diary share and earn" -> getEvent(context)

                        "calories consumed" -> context.startActivity(
                            Intent(context, WebActivity::class.java)
                                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                .putExtra("comingFrom", "redirection")
                                .putExtra("Url", context.resources.getString(R.string.addFoodUrl))
                        )

                        "calories burned" -> context.startActivity(
                            Intent(context, WebActivity::class.java)
                                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                .putExtra("comingFrom", "redirection")

                                .putExtra(
                                    "Url",
                                    context.resources.getString(R.string.addExerciseUrl)
                                )
                        )

                        "webinar" -> {
                            activityTracker("A_PlayAndWinQuizathon_ActivityPopUpRedirection_Webinar",context)
                            context.startActivity(
                                    Intent(context, WebinarWebview::class.java)
                                            .putExtra("Url", context.resources.getString(R.string.webinarURL))
                                            .putExtra("comingFrom", "redirection")
                                            .putExtra("loginUrl", "")
                            )
                        }

                        "feedback" -> {
                            activityTracker("A__PlayAndWinQuizathon_ActivityPopUpRedirection_Feedback",context);
                            context.startActivity(
                            Intent(context, QuizathonViewAllActivity::class.java)
                                .putExtra("type", "feedback")
                                .putExtra("name", "Feedback")
                        )}

                        "addpolicy" -> {
                            activityTracker("A_PlayAndWinQuizathon_ActivityPopUpRedirection_addpolicy",context);
                            context.startActivity(
                                    Intent(
                                            context,
                                            PolicySearchActivity::class.java
                                    )
                            )
                        }
                        "fileshare" -> context.startActivity(
                                Intent(
                                        context,
                                        ActivityFileShareList::class.java
                                )
                        )
                    }

                }
            } else if (redirectionModel.redirectionUrl != null && !redirectionModel.redirectionUrl.equals(
                    "",
                    true
                )
            ) {
                if (redirectionModel.isRedirectedToWeb!!) {
                    if (redirectionModel.redirectionUrl.contains("zoom")) {
                        val uri = Uri.parse(
                            redirectionModel.redirectionUrl.replace(
                                "zoomus://",
                                "https://"
                            )
                        )
                        val sendIntent = Intent(Intent.ACTION_VIEW, uri)
                        context.startActivity(sendIntent)
                    } else {
                        val uri = Uri.parse(redirectionModel.redirectionUrl)
                        val sendIntent = Intent(Intent.ACTION_VIEW, uri)
                        context.startActivity(sendIntent)
                    }

                } else {
                    if (redirectionModel.redirectionUrl.contains("{userid}")) {
                        val i = Intent(context, WebActivity::class.java)
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        i.putExtra("comingFrom", "redirection")
                        i.putExtra(
                            "Url",
                            redirectionModel.redirectionUrl.replace(
                                "{userid}",
                                SharedPref.getAesUuid()
                            )
                        )
                        context.startActivity(i)
                    } else if (redirectionModel.redirectionUrl.contains("{webinar-list")) {
                        val i = Intent(context, WebActivity::class.java)
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        i.putExtra("comingFrom", "redirection")
                        i.putExtra("Url", redirectionModel.redirectionUrl)
                        context.startActivity(i)
                    } else {
                        context.startActivity(
                            Intent(context, HappyMartCategory::class.java)
                                .putExtra("disclaimer", redirectionModel.disclaimer)
                                .putExtra("redirectionURL", redirectionModel.redirectionUrl)
                                .putExtra("disclaimerURL", redirectionModel.disclaimerURL)
                                .putExtra("comingFrom", Constants.DASHBOARD_BANNER)
                        )
                    }

                }
            } else if (redirectionModel.happyMartKey != null && !redirectionModel.happyMartKey.equals(
                    "",
                    true
                )
            ) {
                context.startActivity(
                    Intent(context, HappyMartCategory::class.java)
                        .putExtra("id", redirectionModel.happyMartValue)
                        .putExtra("category", redirectionModel.happyMartKey)
                        .putExtra("comingFrom", "redirection")
                )
            }


        } catch (e: Exception) {
            e.toString()
        }
    }

    fun getIndividualGoal(context: Context) {
        try {
            CommonUtils.showProgressDialige(context)
            var apiInterface = RetrofitHandler.apiInterface()
            apiInterface.getIndividualGoal(SharedPref.getAuthToken())
                .enqueue(object : Callback<GetIndividualGoalResponse> {
                    override fun onResponse(
                        call: Call<GetIndividualGoalResponse>,
                        response: Response<GetIndividualGoalResponse>
                    ) {
                        CommonUtils.showProgressDialige(context)
                        if (response.isSuccessful && response.code() == 200) {
                            GoalsDashboard.otherGoals.clear()
                            if (response.body()!!.data.otherGoals.isNotEmpty()) {
                                GoalsDashboard.otherGoals = response.body()!!.data.otherGoals
                                context.startActivity(Intent(context, OtherGoals::class.java))
                            }
                        } else {
                            Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }

                    override fun onFailure(call: Call<GetIndividualGoalResponse>, t: Throwable) {
                        CommonUtils.showProgressDialige(context)

                    }

                })

        } catch (e: Exception) {
            CommonUtils.showProgressDialige(context)
            e.toString()
        }
    }

    fun getRecommendedGoal(context: Context) {
        try {
            CommonUtils.showProgressDialige(context)
            val apiInterface = RetrofitHandler.apiInterface()
            apiInterface.getRecomendedGoals(SharedPref.getAuthToken())
                .enqueue(object : Callback<RecomendedGoalsResponse> {
                    override fun onResponse(
                        call: Call<RecomendedGoalsResponse>,
                        response: Response<RecomendedGoalsResponse>
                    ) {
                        CommonUtils.dismissDialoge()
                        if (response.isSuccessful && response.code() == 200) {
                            GoalsDashboard.recommendedGoalsList.clear()
                            if (response.body()!!.data.isNotEmpty()) {
                                GoalsDashboard.recommendedGoalsList = response.body()!!.data
                                context.startActivity(
                                    Intent(
                                        context,
                                        RecommendedGoalsDetails::class.java
                                    )
                                )
                            } else {
                                context.startActivity(Intent(context, GoalsDashboard::class.java))
                            }
                        } else {
                            Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }

                    override fun onFailure(call: Call<RecomendedGoalsResponse>, t: Throwable) {
                        CommonUtils.dismissDialoge()

                    }

                })

        } catch (e: Exception) {
            CommonUtils.dismissDialoge()
            e.toString()
        }
    }

    fun getGoalHistory(context: Context, comingFrom: String) {
        try {
            CommonUtils.showProgressDialige(context)
            var apiInterface = RetrofitHandler.apiInterface()
            apiInterface.goalsHistory(SharedPref.getAuthToken())
                .enqueue(object : Callback<GoalHistoryResponse> {
                    override fun onResponse(
                        call: Call<GoalHistoryResponse>,
                        response: Response<GoalHistoryResponse>
                    ) {
                        CommonUtils.dismissDialoge()
                        if (response.isSuccessful && response.code() == 200) {
                            if (comingFrom.equals("pending", true)) {
                                if (response.body()!!.data.pendingGoals.isNotEmpty()) {
                                    GoalHistory.pendingList.clear()
                                    GoalHistory.pendingList = response.body()!!.data.pendingGoals
                                    context.startActivity(
                                        Intent(context, HistoryDetailActivity::class.java)
                                            .putExtra("comingFrom", "pending")
                                    )
                                } else {
                                    Toast.makeText(
                                        context,
                                        "There are no pending goals",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                }
                            } else if (comingFrom.equals("delete", true)) {
                                if (response.body()!!.data.deletedGoals.isNotEmpty()) {
                                    GoalHistory.deleteList.clear()
                                    GoalHistory.deleteList = response.body()!!.data.deletedGoals
                                    context.startActivity(
                                        Intent(context, HistoryDetailActivity::class.java)
                                            .putExtra("comingFrom", "delete")
                                    )
                                } else {
                                    Toast.makeText(
                                        context,
                                        "There are no deleted goals",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                }
                            } else {
                                if (response.body()!!.data.completedGoals.isNotEmpty()) {
                                    GoalHistory.completedList.clear()
                                    GoalHistory.completedList =
                                        response.body()!!.data.completedGoals
                                    context.startActivity(
                                        Intent(context, HistoryDetailActivity::class.java)
                                            .putExtra("comingFrom", "completed")
                                    )
                                } else {
                                    Toast.makeText(
                                        context,
                                        "There are no completed goals",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }

                        } else {
                            Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }

                    override fun onFailure(call: Call<GoalHistoryResponse>, t: Throwable) {
                        CommonUtils.dismissDialoge()

                    }

                })

        } catch (e: Exception) {
            CommonUtils.dismissDialoge()
            e.toString()
        }
    }

    fun getEvent(context: Context) {
        try {
            CommonUtils.showProgressDialige(context)
            var apiInterface = ApiClientWyh.getClient(CommonUtils.getBaseUrlForAPI(context))
                .create(ApiInterfaceWyh::class.java)
            apiInterface.getDiaryEventList(SharedPref.getAuthToken())
                .enqueue(object : Callback<DiaryEventListResponse> {
                    override fun onResponse(
                        call: Call<DiaryEventListResponse>,
                        response: Response<DiaryEventListResponse>
                    ) {
                        if (response.code() == 200 && response.body() != null && response.isSuccessful) {
                            CommonUtils.dismissDialoge()
                            val data = response.body()!!.data
                            getEventListData = response.body()!!.data
                            setData(response.body()!!.data, context)


                        }
                    }

                    override fun onFailure(call: Call<DiaryEventListResponse>, t: Throwable) {
                        CommonUtils.dismissDialoge()

                    }

                })

        } catch (e: Exception) {
            CommonUtils.dismissDialoge()
            e.toString()
        }
    }

    private fun setData(data: List<DiaryEventListResponse.Datum>?, context: Context) {
        levelList.add("Select your activity")
        engList.add("Select your activity")
        topUpList.add("Select your activity")
        if (data != null && data.isNotEmpty()) {
            for (item in data) {
                if (item.eventCategory.lowercase(Locale.getDefault()) == "topup") {
                    topUpList.add(item.eventName)
                    topUpListData.add(item)
                } else if (item.eventCategory.lowercase(Locale.getDefault()) == "eng") {
                    engList.add(item.eventName)
                    engListData.add(item)
                } else {
                    levelList.add(item.eventName)
                    levelListData.add(item)
                }
            }
        }

        val intent2 = Intent(context, AddDiaryActivity::class.java)
        intent2.putExtra("came_from", "share")
        intent2.putExtra("levelData", Gson().toJson(levelListData))
        intent2.putExtra("leveldataStr", Gson().toJson(levelList))
        intent2.putExtra("engData", Gson().toJson(engListData))
        intent2.putExtra("engdataStr", Gson().toJson(engList))
        intent2.putExtra("topUpData", Gson().toJson(topUpListData))
        intent2.putExtra("topUpdataStr", Gson().toJson(topUpList))
        context.startActivity(intent2)
    }

    fun showSharePopup(context: Context, fileName: String?) {
        try {
            val alertBuilder = AlertDialog.Builder(context, R.style.CustomAlertDialogInfo)
            val binding: ShareOptionPopUpBinding = DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.share_option_pop_up,
                null,
                false
            )
            alertBuilder.setView(binding.getRoot())
            val alertDialog = alertBuilder.create()
            alertDialog.setCancelable(true)
            if (!alertDialog.isShowing) alertDialog.show()
            binding.tvMsg.setText("Share FaceScan Report")
            binding.ivWhatsapp.setOnClickListener { v ->
                activityTracker("A_DB_DB_Tile_FS_Share_Whtsapp", context)
                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.type = "text/plain"
                shareIntent.putExtra(Intent.EXTRA_TEXT, fileName)
                context.startActivity(shareIntent)
                alertDialog.dismiss()
            }
            val displayRectangle = Rect()
            val window = (context as NewDashboardActivity).window
            window.decorView.getWindowVisibleDisplayFrame(displayRectangle)
            alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            alertDialog.window!!
                .setLayout(
                    (displayRectangle.width() * 0.88f).toInt(),
                    RelativeLayout.LayoutParams.WRAP_CONTENT
                )
        } catch (e: java.lang.Exception) {
            e.toString()
        }
    }

    fun getDassAnalysis(context: Context) {
        try {
            CommonUtils.showProgressDialige(context)
            val request =
                IntegrationIdRequest(context.getResources().getString(R.string.dass21_i_id))
            val apiInterfaceWyh = ApiClientWyh.getClient(CommonUtils.getBaseUrlForAPI(context))
                .create(ApiInterfaceWyh::class.java)

            val call: Call<DassAnalysisResponse> =
                apiInterfaceWyh.getDassAnalysis(SharedPref.getAuthToken(), request)

            call.enqueue(object : Callback<DassAnalysisResponse?> {
                override fun onResponse(
                    call: Call<DassAnalysisResponse?>,
                    response: Response<DassAnalysisResponse?>
                ) {
                    CommonUtils.dismissDialoge()
                    if (response.body() != null && response.code() == 200 && response.body()!!.data != null) {
                        SharedPref.putDASSAnalysis(Gson().toJson(response.body()))
                        val isShown = SharedPref.getWellBeingIntroShownDAS()

                        if (NewDashboardHelper.Companion.activityName.equals(
                                "das",
                                ignoreCase = true
                            )
                        ) {
                            context.startActivity(
                                Intent(
                                    context,
                                    Dass21QuestionsActivity::class.java
                                )
                            )
                        } else {
                            if (SharedPref.getDASSAnalysis() != "") {
                                val dassAnalysisResponse: DassAnalysisResponse = Gson().fromJson(
                                    SharedPref.getDASSAnalysis(),
                                    DassAnalysisResponse::class.java
                                )
                                val isAnalysis = dassAnalysisResponse?.data != null
                                if (!isShown) {
                                    SharedPref.putWellBeingIntroShownDAS(true)
                                    val intent =
                                        Intent(context, WellBeingDisclaimerActivity::class.java)
                                    intent.putExtra("came_from", "KnowYourDAS")
                                    intent.putExtra("CategoryName", "DAS Score")
                                    intent.putExtra("isAnalysis", isAnalysis)
                                    intent.putExtra("shownScreen", true)
                                    context.startActivity(intent)
                                } else {
                                    if (isAnalysis) {
                                        context.startActivity(
                                            Intent(
                                                context,
                                                Dass21AnalysisActivity::class.java
                                            ).putExtra("comingFrom", "")
                                        )
                                    } else {
                                        context.startActivity(
                                            Intent(
                                                context,
                                                Dass21QuestionsActivity::class.java
                                            )
                                        )
                                    }
                                }

                            } else {
                                if (NewDashboardHelper.activityName != null && NewDashboardHelper.activityName.equals("das",ignoreCase = true)
                                ) {
                                    context.startActivity(
                                        Intent(
                                            context,
                                            Dass21QuestionsActivity::class.java
                                        )
                                    )
                                } else {
                                    if (!isShown) {
                                        SharedPref.putWellBeingIntroShownDAS(true)
                                        val intent =
                                            Intent(context, WellBeingDisclaimerActivity::class.java)
                                        intent.putExtra("came_from", "KnowYourDAS")
                                        intent.putExtra("CategoryName", "DAS Score")
                                        intent.putExtra("isAnalysis", false)
                                        intent.putExtra("shownScreen", true)
                                        context.startActivity(intent)
                                    } else {
                                        context.startActivity(
                                            Intent(
                                                context,
                                                Dass21QuestionsActivity::class.java
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    } else {
                        val isShown = SharedPref.getWellBeingIntroShownDAS()
                        if (!isShown) {
                            SharedPref.putWellBeingIntroShownDAS(true)
                            val intent = Intent(context, WellBeingDisclaimerActivity::class.java)
                            intent.putExtra("came_from", "KnowYourDAS")
                            intent.putExtra("CategoryName", "DAS Score")
                            intent.putExtra("isAnalysis", false)
                            intent.putExtra("shownScreen", true)
                            context.startActivity(intent)
                        } else {
                            context.startActivity(
                                Intent(
                                    context,
                                    Dass21QuestionsActivity::class.java
                                )
                            )
                        }
                    }
                }

                override fun onFailure(call: Call<DassAnalysisResponse?>, t: Throwable) {
                    CommonUtils.dismissDialoge()
                    Analytics.logEvent(
                        context,
                        context.javaClass.getName(),
                        context.getString(R.string.get_das_analysis_failed)
                    )
                }
            })
        } catch (e: Exception) {
            CommonUtils.dismissDialoge()
            e.toString()
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
                        CommonUtils.dismissDialoge()
                        if (response.body() != null && response.code() == 200) {
                            SharedPref.putHeartAgeAnalysis(Gson().toJson(response.body()))
                            heartAgeAnalysisResponse = Gson().fromJson<HeartAgeAnalysisResponse>(
                                Gson().toJson(response.body()),
                                HeartAgeAnalysisResponse::class.java
                            )
                            val isAnalysis =
                                heartAgeAnalysisResponse != null && heartAgeAnalysisResponse.getData() != null
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
                                        Intent(context, WellBeingDisclaimerActivity::class.java)
                                    intent.putExtra("came_from", "HeartAge")
                                    intent.putExtra("CategoryName", "Heart Age")
                                    intent.putExtra("isAnalysis", isAnalysis)
                                    intent.putExtra("shownScreen", true)
                                    context.startActivity(intent)
                                } else {
                                    val intent: Intent = if (isAnalysis) {
                                        Intent(context, HeartAgeAnalysisActivity::class.java)
                                    } else {
                                        Intent(context, HeartAgeQuestionsActivity::class.java)
                                    }
                                    context.startActivity(intent)
                                }
                            }
                        } else {
                        }
                    }

                    override fun onFailure(call: Call<HeartAgeAnalysisResponse?>, t: Throwable) {
                        CommonUtils.dismissDialoge()
                    }
                })
        } catch (e: java.lang.Exception) {
            CommonUtils.dismissDialoge()
            e.printStackTrace()
        }
    }
}