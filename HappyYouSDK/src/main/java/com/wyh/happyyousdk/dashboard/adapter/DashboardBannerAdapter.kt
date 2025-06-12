package com.wyh.happyyousdk.dashboard.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.wyh.happyyousdk.APIEncryption.APILogs
import com.wyh.happyyousdk.APIEncryption.APILogs.activityTracker
import com.wyh.happyyousdk.R
import com.wyh.happyyousdk.SpinWheel.Utilities.MessageInfoDialog
import com.wyh.happyyousdk.dashboard.NewDashboardActivity
import com.wyh.happyyousdk.dashboard.RedirectionMethod.redirectionScreen
import com.wyh.happyyousdk.dashboard.helper.NewDashboardHelper
import com.wyh.happyyousdk.dashboard.model.GetBannerResponse
import com.wyh.happyyousdk.ehr.EhrActivity
import com.wyh.happyyousdk.model.ClientResponseDashboard
import com.wyh.happyyousdk.model.RedirectionModel
import com.wyh.happyyousdk.model.response.BannerQuizModel
import com.wyh.happyyousdk.model.response.PolicyDetails
import com.wyh.happyyousdk.model.response.UserDetail
import com.wyh.happyyousdk.network.ApiClientWyh
import com.wyh.happyyousdk.network.ApiInterfaceWyh
import com.wyh.happyyousdk.policyDetail.PolicyListActivity
import com.wyh.happyyousdk.policyDetail.PolicySearchActivity
import com.wyh.happyyousdk.policyDetail.PolicyViewMore
import com.wyh.happyyousdk.profile.ProfileActivity
import com.wyh.happyyousdk.quizathon.QuizathonActivity
import com.wyh.happyyousdk.quizathon.QuizathonScoreActivity
import com.wyh.happyyousdk.quizathon.dialog.QuizMessageDialog
import com.wyh.happyyousdk.utils.CommonUtils
import com.wyh.happyyousdk.utils.CommonUtils.formatDateFromString
import com.wyh.happyyousdk.utils.CommonUtils.isDateExpired
import com.wyh.happyyousdk.utils.dialog.RegistrationDialog
import de.hdodenhof.circleimageview.CircleImageView

class DashboardBannerAdapter(
    val context: Context,
    val list: ArrayList<PolicyDetails>,
    var clientsPolicyData: java.util.ArrayList<ClientResponseDashboard> = arrayListOf(),
    val bannerList: ArrayList<GetBannerResponse.Data>,
    val userDetail: UserDetail
) : RecyclerView.Adapter<DashboardBannerAdapter.MyViewHolder>() {
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userName = itemView.findViewById<TextView>(R.id.new_user_name)
        val userAge = itemView.findViewById<TextView>(R.id.new_user_age)
        val userGender = itemView.findViewById<TextView>(R.id.new_user_gender)
        val policyNum = itemView.findViewById<TextView>(R.id.user_policy_num)
        val abhaNum = itemView.findViewById<TextView>(R.id.abha_id)
        val addAbhaId = itemView.findViewById<RelativeLayout>(R.id.add_abha_id)
        val llPolicyNumber = itemView.findViewById<LinearLayout>(R.id.llPolicyNumber)
        val rlLinkedPolicies = itemView.findViewById<RelativeLayout>(R.id.rlLinkedPolicies)
        val btnEditProfile = itemView.findViewById<ImageView>(R.id.btnEditProfile)
        val btnLinkedPolicy = itemView.findViewById<RelativeLayout>(R.id.btnLinkedPolicy)
        val llAddPolicy = itemView.findViewById<LinearLayout>(R.id.llAddPolicy)
        val userPolicyExpiryDate = itemView.findViewById<TextView>(R.id.user_policy_expiry_date)
        val ivProfile = itemView.findViewById<CircleImageView>(R.id.ivProfile)
        val separatedView = itemView.findViewById<View>(R.id.separated_view)
        val policyLayout = itemView.findViewById<RelativeLayout>(R.id.single_policy_layout)
        val bannerLayoutOne = itemView.findViewById<LinearLayout>(R.id.banner_layout_one)
        val bannerLayoutTwo = itemView.findViewById<LinearLayout>(R.id.banner_layout_two)
        val bannerImgOne = itemView.findViewById<ImageView>(R.id.banner_img_one)
        val bannerImgTwo = itemView.findViewById<ImageView>(R.id.banner_img_two)
        val addMorePolicy = itemView.findViewById<RelativeLayout>(R.id.add_policy_layout)
        val addAbhaID = itemView.findViewById<RelativeLayout>(R.id.add_abha_id)
        val healthLocker = itemView.findViewById<ImageView>(R.id.health_locker)
        val policyLayoutExp = itemView.findViewById<LinearLayout>(R.id.policy_layout)
        val policyAbhaSeparator = itemView.findViewById<View>(R.id.policy_abha_separator)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.user_details_layout, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {;
        val size: Int = if (list.size == 0)
            bannerList.size + 1
        else
            list.size + bannerList.size
        Log.d("size", "getItemCount: $size")
        return size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        if (bannerList.size == 0) {
            setUserDetail(holder)
        } else if (bannerList.size == 1) {
            if (position == 0) {
                holder.bannerLayoutOne.visibility = View.VISIBLE
                holder.bannerLayoutTwo.visibility = View.GONE
                holder.policyLayout.visibility = View.GONE
                Glide.with(context).load(bannerList[0].imageUrl).into(holder.bannerImgOne)

                holder.bannerLayoutOne.setOnClickListener {
                    APILogs.activityTracker("A_BANNER_${bannerList[0].bannerId}", context)
                    if (bannerList[0].redirectionKey != null) {
                        val redirectionModel = RedirectionModel(
                            bannerList[0].redirectionKey,
                            bannerList.get(0).getOpenInChrome(),
                            "DASHBOARD_BANNER",
                            bannerList.get(0).getRedirectionUrl(),
                            bannerList.get(0).getTitle(),
                            bannerList.get(0).getDisclaimer(),
                            bannerList.get(0).getDisclaimerImageUrl(),
                            "",
                            0,
                            bannerList.get(0).getBannerId(),
                            bannerList.get(0).getHappyMartKey(),
                            bannerList.get(0).getHappyMartValue(),
                            "actOMeterTribeList.get(0).getCommunityName()",
                            "actOMeterTribeList.get(0).getCommunityType()",
                            bannerList.get(0).getCompleted(),
                            GsonBuilder().setLenient().create().fromJson(
                                bannerList[0].bannerQuizModel,
                                BannerQuizModel::class.java
                            ),
                            bannerList[0].rewardDate
                        )
                        if (bannerList[0].redirectionKey.equals("playnwin", ignoreCase = false)) {
                            if (!isDateExpired(bannerList[0].quizRegistrationDate)) {
                                if (!bannerList[0].quizathonRegistered && bannerList[0].isRegistrationAllowed) {

                                    val dialog = RegistrationDialog(
                                        context,
                                        bannerList[0].quizathonRegistrationQuestion,
                                        bannerList[0].regTitle,
                                        bannerList[0].quizathonId,
                                        createIntent(bannerList[0]),
                                        bannerList[0].quizathonStarted,
                                        bannerList[0].quizathonTitle
                                    )

                                    dialog.iconUrl = bannerList[0].regIcon

                                    dialog.show()
                                } else if (!!bannerList[0].isQuizathonRegistered) {
                                    if (bannerList[0].quizBurnPoints > 0.toString()) {
                                        (context as? NewDashboardActivity)?.burnType = "QuizEntry"
                                        (context as? NewDashboardActivity)?.index = 0
                                        (context as? NewDashboardActivity)?.click = "QuizEntry"
                                        (context as? NewDashboardActivity)?.showBurnQuizDialog(
                                                bannerList[0].quizBurnPoints.toInt(),
                                                "Quiz",
                                                bannerList[0].burnRegIcon,
                                                "You can participate in the quiz using " + bannerList[0].quizBurnPoints + " HappyYou points",
                                                bannerList[0].quizathonId
                                        )
                                    }else if (bannerList[0].quizathonStarted) {
                                        val intent = Intent(context, QuizathonActivity::class.java)
                                        intent.putExtra("data", bannerList[0].quizathonQuestion)
                                        intent.putExtra(
                                                "CategoryName",
                                                bannerList[0].quizathonTitle ?: ""
                                        )
                                        intent.putExtra(
                                                "Category",
                                                bannerList[0].quizathonTitle ?: ""
                                        )
                                        intent.putExtra("comingFrom", "")
                                        intent.putExtra("IsRetake", false)
                                        intent.putExtra("RetakeId", 0)
                                        intent.putExtra(
                                                "rewardDate",
                                                bannerList[0].quizathonRewardDate
                                        )
                                        intent.putExtra("qId", bannerList[0].quizathonId)
                                        context.startActivity(createIntent(bannerList[0]))
                                    }
                                    }else {
                                    if (bannerList[0].quizathonStarted) {
                                        val intent = Intent(context, QuizathonActivity::class.java)
                                        intent.putExtra("data", bannerList[0].quizathonQuestion)
                                        intent.putExtra(
                                            "CategoryName",
                                            bannerList[0].quizathonTitle ?: ""
                                        )
                                        intent.putExtra(
                                            "Category",
                                            bannerList[0].quizathonTitle ?: ""
                                        )
                                        intent.putExtra("comingFrom", "")
                                        intent.putExtra("IsRetake", false)
                                        intent.putExtra("RetakeId", 0)
                                        intent.putExtra(
                                            "rewardDate",
                                            bannerList[0].quizathonRewardDate
                                        )
                                        intent.putExtra("qId", bannerList[0].quizathonId)
                                        context.startActivity(createIntent(bannerList[0]))
                                    } else {
                                        Toast.makeText(
                                            context,
                                            "Registration is already completed.", Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            } else {
                                if (!bannerList[0].isRegistrationAllowed && bannerList[0].registrationPointBurn >= 0) {
                                    (context as? NewDashboardActivity)?.burnType = "QuizRegistration"
                                    (context as? NewDashboardActivity)?.index = 0
                                    (context as? NewDashboardActivity)?.click = "QuizRegistration"
                                    (context as? NewDashboardActivity)?.showBurnQuizDialog(
                                        bannerList[0].registrationPointBurn,
                                        bannerList[0].burnRegTitle,
                                        bannerList[0].burnRegIcon,
                                        bannerList[0].burnRegMessage,
                                        bannerList[0].quizathonId
                                    )
                                } else {
                                    if (bannerList[0].isRegistrationAllowed) {
                                        val dialog = RegistrationDialog(
                                            context,
                                            bannerList[0].quizathonRegistrationQuestion,
                                            bannerList[0].regTitle,
                                            bannerList[0].quizathonId,
                                            createIntent(bannerList[0]),
                                            bannerList[0].quizathonStarted,
                                            bannerList[0].quizathonTitle
                                        )
                                        dialog.iconUrl = bannerList[0].regIcon
                                        dialog.show()
                                    } else {
                                        if (bannerList[0].dialogDetail != null && bannerList[0].dialogDetail.type != null){
                                            var apiInterfaceWyh: ApiInterfaceWyh? = null
                                            apiInterfaceWyh = ApiClientWyh.getClient(CommonUtils.getBaseUrlForAPI(context)).create(ApiInterfaceWyh::class.java)
                                            val dialog = QuizMessageDialog(context, bannerList[0].dialogDetail, apiInterfaceWyh)
                                            dialog.show()
                                        }
                                        else{
                                            val messageInfoDialog = MessageInfoDialog(
                                                    context,
                                                    ContextCompat.getDrawable(
                                                            context,
                                                            R.drawable.ic_oops
                                                    ),
                                                    "Oops!",
                                                    ContextCompat.getString(context, R.string.quiz_reg_over)
                                            )
                                            messageInfoDialog.show()
                                        }
                                    }
                                }

                            }
                        } else if (!bannerList[0].isRegistered && bannerList[0].registrationData != null && bannerList[0].registrationData.isNotEmpty()) {
                            val rdialog = RegistrationDialog(
                                context,
                                bannerList[0].registrationID,
                                bannerList[0].dialogTitle,
                                redirectionModel,
                                bannerList[0].registrationData,bannerList[0].isRegistered
                            )
                            rdialog.show()

                        } else {
                            if (bannerList[0].isRegistered != null && bannerList[0].isRegistered) {
                                Toast.makeText(
                                    context,
                                    "Registration is already completed.", Toast.LENGTH_SHORT
                                ).show()
                            }
                            redirectionScreen(redirectionModel, context)
                        }
                    }
                }
            } else {
                setUserDetail(holder)
            }
        } else if (bannerList.size == 2) {
            if (position == 0) {
                holder.bannerLayoutOne.visibility = View.VISIBLE
                holder.bannerLayoutTwo.visibility = View.GONE
                holder.policyLayout.visibility = View.GONE
                Glide.with(context).load(bannerList[0].imageUrl).into(holder.bannerImgOne)
                Glide.with(context).load(bannerList[1].imageUrl).into(holder.bannerImgTwo)

                holder.bannerLayoutOne.setOnClickListener {
                    APILogs.activityTracker("A_BANNER_${bannerList[0].bannerId}", context)
                    if (bannerList[0].redirectionKey != null) {
                        val redirectionModel = RedirectionModel(
                            bannerList[0].redirectionKey,
                            bannerList[0].openInChrome,
                            "DASHBOARD_BANNER",
                            bannerList[0].redirectionUrl,
                            bannerList[0].title,
                            bannerList[0].disclaimer,
                            bannerList[0].disclaimerImageUrl,
                            "",
                            0,
                            bannerList[0].bannerId,
                            bannerList[0].happyMartKey,
                            bannerList[0].happyMartValue,
                            "actOMeterTribeList.get(0).getCommunityName()",
                            "actOMeterTribeList.get(0).getCommunityType()",
                            bannerList[0].completed,
                            Gson().fromJson(
                                bannerList[0].bannerQuizModel,
                                BannerQuizModel::class.java
                            ),
                            bannerList[0].rewardDate
                        )

                        if (bannerList[0].redirectionKey.equals("playnwin", ignoreCase = false)) {
                            if (!isDateExpired(bannerList[0].quizRegistrationDate)) {
                                if (!bannerList[0].quizathonRegistered ) {

                                    val dialog = RegistrationDialog(
                                        context,
                                        bannerList[0].quizathonRegistrationQuestion,
                                        bannerList[0].regTitle,
                                        bannerList[0].quizathonId,
                                        createIntent(bannerList[0]),
                                        bannerList[0].quizathonStarted,
                                        bannerList[0].quizathonTitle,

                                        )
                                    dialog.iconUrl = bannerList[0].regIcon
                                    dialog.show()
                                } else if (!!bannerList[0].isQuizathonRegistered) {
                                    if (bannerList[0].quizBurnPoints > 0.toString()) {
                                        (context as? NewDashboardActivity)?.burnType = "QuizEntry"
                                        (context as? NewDashboardActivity)?.index = 0
                                        (context as? NewDashboardActivity)?.click = "QuizEntry"
                                        //NewDashboardHelper.quizathonModelRegister = bannerList[0].quizathonRegistrationQuestion
                                        (context as? NewDashboardActivity)?.showBurnQuizDialog(
                                                bannerList[0].quizBurnPoints.toInt(),
                                                "Quiz",
                                                bannerList[0].burnRegIcon,
                                                "You can participate in the quiz using " + bannerList[0].quizBurnPoints + " HappyYou points",
                                                bannerList[0].quizathonId
                                        )
                                    }else if (bannerList[0].quizathonStarted) {
                                        val intent = Intent(context, QuizathonActivity::class.java)
                                        intent.putExtra("data", bannerList[0].quizathonQuestion)
                                        intent.putExtra(
                                                "CategoryName",
                                                bannerList[0].quizathonTitle ?: ""
                                        )
                                        intent.putExtra(
                                                "Category",
                                                bannerList[0].quizathonTitle ?: ""
                                        )
                                        intent.putExtra("comingFrom", "")
                                        intent.putExtra("IsRetake", false)
                                        intent.putExtra("RetakeId", 0)
                                        intent.putExtra(
                                                "rewardDate",
                                                bannerList[0].quizathonRewardDate
                                        )
                                        intent.putExtra("qId", bannerList[0].quizathonId)
                                        context.startActivity(createIntent(bannerList[0]))
                                    }else if (bannerList[0].dialogDetail != null && bannerList[0].dialogDetail.type != null){
                                        var apiInterfaceWyh: ApiInterfaceWyh? = null
                                        apiInterfaceWyh = ApiClientWyh.getClient(CommonUtils.getBaseUrlForAPI(context)).create(ApiInterfaceWyh::class.java)
                                        val dialog = QuizMessageDialog(context, bannerList[0].dialogDetail, apiInterfaceWyh)
                                        dialog.show()
                                    }
                                }else {
                                    if (bannerList[0].quizathonStarted) {
                                        val intent = Intent(context, QuizathonActivity::class.java)
                                        intent.putExtra("data", bannerList[0].quizathonQuestion)
                                        intent.putExtra(
                                            "CategoryName",
                                            bannerList[0].quizathonTitle ?: ""
                                        )
                                        intent.putExtra(
                                            "Category",
                                            bannerList[0].quizathonTitle ?: ""
                                        )
                                        intent.putExtra("comingFrom", "")
                                        intent.putExtra("IsRetake", false)
                                        intent.putExtra("RetakeId", 0)
                                        intent.putExtra(
                                            "rewardDate",
                                            bannerList[0].quizathonRewardDate
                                        )
                                        intent.putExtra("qId", bannerList[0].quizathonId)
                                        context.startActivity(createIntent(bannerList[0]))
                                    } else {
                                        Toast.makeText(
                                            context,
                                            "Registration is already completed.", Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            } else {
                                if (!bannerList[0].isRegistrationAllowed && bannerList[0].registrationPointBurn >= 0) {
                                    (context as? NewDashboardActivity)?.burnType = "QuizRegistration"
                                    (context as? NewDashboardActivity)?.index = 0
                                    (context as? NewDashboardActivity)?.click = "QuizRegistration"
                                    (context as? NewDashboardActivity)?.showBurnQuizDialog(
                                        bannerList[0].registrationPointBurn,
                                        bannerList[0].burnRegTitle,
                                        bannerList[0].burnRegIcon,
                                        bannerList[0].burnRegMessage,
                                        bannerList[0].quizathonId
                                    )
                                } else {
                                    if (bannerList[0].isRegistrationAllowed) {
                                        val dialog = RegistrationDialog(
                                            context,
                                            bannerList[0].quizathonRegistrationQuestion,
                                            bannerList[0].regTitle,
                                            bannerList[0].quizathonId,
                                            createIntent(bannerList[0]),
                                            bannerList[0].quizathonStarted,
                                            bannerList[0].quizathonTitle
                                        )
                                        dialog.iconUrl = bannerList[0].regIcon
                                        dialog.show()
                                    } else {
                                        if (!bannerList[0].isRegistrationAllowed && bannerList[0].registrationPointBurn >= 0) {
                                            (context as? NewDashboardActivity)?.burnType = "QuizRegistration"
                                            (context as? NewDashboardActivity)?.index = 0
                                            (context as? NewDashboardActivity)?.click = "QuizRegistration"
                                            (context as? NewDashboardActivity)?.showBurnQuizDialog(
                                                bannerList[0].registrationPointBurn,
                                                bannerList[0].burnRegTitle,
                                                bannerList[0].burnRegIcon,
                                                bannerList[0].burnRegMessage,
                                                bannerList[0].quizathonId
                                            )
                                        } else {
                                            if (bannerList[0].isRegistrationAllowed) {
                                                val dialog = RegistrationDialog(
                                                    context,
                                                    bannerList[0].quizathonRegistrationQuestion,
                                                    bannerList[0].regTitle,
                                                    bannerList[0].quizathonId,
                                                    createIntent(bannerList[0]),
                                                    bannerList[0].quizathonStarted,
                                                    bannerList[0].quizathonTitle
                                                )
                                                dialog.iconUrl = bannerList[0].regIcon
                                                dialog.show()
                                            } else {
                                                if (bannerList[0].dialogDetail != null && bannerList[0].dialogDetail.type != null){
                                                    var apiInterfaceWyh: ApiInterfaceWyh? = null
                                                    apiInterfaceWyh = ApiClientWyh.getClient(CommonUtils.getBaseUrlForAPI(context)).create(ApiInterfaceWyh::class.java)
                                                    val dialog = QuizMessageDialog(context, bannerList[0].dialogDetail, apiInterfaceWyh)
                                                    dialog.show()
                                                }
                                                else{
                                                    val messageInfoDialog = MessageInfoDialog(
                                                            context,
                                                            ContextCompat.getDrawable(
                                                                    context,
                                                                    R.drawable.ic_oops
                                                            ),
                                                            "Oops!",
                                                            ContextCompat.getString(context, R.string.quiz_reg_over)
                                                    )
                                                    messageInfoDialog.show()
                                                }
                                            }
                                        }
                                    }
                                }

                            }
                        } else if (!bannerList[0].isRegistered && bannerList[0].registrationData != null && bannerList[0].registrationData.isNotEmpty()) {
                            val rdialog = RegistrationDialog(
                                context,
                                bannerList[0].registrationID,
                                bannerList[0].dialogTitle,
                                redirectionModel,
                                bannerList[0].registrationData,bannerList[0].isRegistered
                            )
                            rdialog.show()

                        } else {
                            if (bannerList[0].isRegistered != null && bannerList[0].isRegistered) {
                                Toast.makeText(
                                    context,
                                    "Registration is already completed.", Toast.LENGTH_SHORT
                                ).show()
                            }
                            redirectionScreen(redirectionModel, context)
                        }
                    } else if (bannerList[0].redirectionUrl != null && !bannerList[0].redirectionUrl.equals(
                            ""
                        )
                    ) {
                        val redirectionModel = RedirectionModel(
                            "",
                            bannerList[0].openInChrome,
                            "DASHBOARD_BANNER",
                            bannerList[0].redirectionUrl,
                            bannerList[0].title,
                            bannerList[0].disclaimer,
                            bannerList[0].disclaimerImageUrl,
                            "",
                            0,
                            bannerList[0].bannerId,
                            bannerList[0].happyMartKey,
                            bannerList[0].happyMartValue,
                            "actOMeterTribeList.get(0).getCommunityName()",
                            "actOMeterTribeList.get(0).getCommunityType()",
                            bannerList[0].completed,
                            Gson().fromJson(
                                bannerList[0].bannerQuizModel,
                                BannerQuizModel::class.java
                            ),
                            bannerList[0].rewardDate
                        )

                        if (bannerList[0].redirectionKey.equals("playnwin", ignoreCase = false)) {
                            if (!isDateExpired(bannerList[0].quizRegistrationDate)) {
                                if (!bannerList[0].quizathonRegistered && bannerList[0].isRegistrationAllowed) {

                                    val dialog = RegistrationDialog(
                                        context,
                                        bannerList[0].quizathonRegistrationQuestion,
                                        bannerList[0].regTitle,
                                        bannerList[0].quizathonId,
                                        createIntent(bannerList[0]),
                                        bannerList[0].quizathonStarted,
                                        bannerList[0].quizathonTitle
                                    )
                                    dialog.iconUrl = bannerList[0].regIcon
                                    dialog.show()
                                } else if (!!bannerList[0].isQuizathonRegistered) {
                                    if (bannerList[0].quizBurnPoints > 0.toString()) {
                                        (context as? NewDashboardActivity)?.burnType = "QuizEntry"
                                        (context as? NewDashboardActivity)?.index = 0
                                        (context as? NewDashboardActivity)?.click = "QuizEntry"
                                        //NewDashboardHelper.quizathonModelRegister = bannerList[0].quizathonRegistrationQuestion
                                        (context as? NewDashboardActivity)?.showBurnQuizDialog(
                                                bannerList[0].quizBurnPoints.toInt(),
                                                "Quiz",
                                                bannerList[0].burnRegIcon,
                                                "You can participate in the quiz using " + bannerList[0].quizBurnPoints + " HappyYou points",
                                                bannerList[0].quizathonId
                                        )
                                    }else if (bannerList[0].quizathonStarted) {
                                        val intent = Intent(context, QuizathonActivity::class.java)
                                        intent.putExtra("data", bannerList[0].quizathonQuestion)
                                        intent.putExtra(
                                                "CategoryName",
                                                bannerList[0].quizathonTitle ?: ""
                                        )
                                        intent.putExtra(
                                                "Category",
                                                bannerList[0].quizathonTitle ?: ""
                                        )
                                        intent.putExtra("comingFrom", "")
                                        intent.putExtra("IsRetake", false)
                                        intent.putExtra("RetakeId", 0)
                                        intent.putExtra(
                                                "rewardDate",
                                                bannerList[0].quizathonRewardDate
                                        )
                                        intent.putExtra("qId", bannerList[0].quizathonId)
                                        context.startActivity(createIntent(bannerList[0]))
                                    }else if (bannerList[0].dialogDetail != null && bannerList[0].dialogDetail.type != null){
                                        var apiInterfaceWyh: ApiInterfaceWyh? = null
                                        apiInterfaceWyh = ApiClientWyh.getClient(CommonUtils.getBaseUrlForAPI(context)).create(ApiInterfaceWyh::class.java)
                                        val dialog = QuizMessageDialog(context, bannerList[0].dialogDetail, apiInterfaceWyh)
                                        dialog.show()
                                    }
                                }else if (!!bannerList[0].isQuizathonRegistered) {
                                    if (bannerList[0].quizBurnPoints > 0.toString()) {
                                        (context as? NewDashboardActivity)?.burnType = "QuizEntry"
                                        (context as? NewDashboardActivity)?.index = 0
                                        (context as? NewDashboardActivity)?.click = "QuizEntry"
                                        //NewDashboardHelper.quizathonModelRegister = bannerList[0].quizathonRegistrationQuestion
                                        (context as? NewDashboardActivity)?.showBurnQuizDialog(
                                                bannerList[0].quizBurnPoints.toInt(),
                                                "Quiz",
                                                bannerList[0].burnRegIcon,
                                                "You can participate in the quiz using " + bannerList[0].quizBurnPoints + " HappyYou points",
                                                bannerList[0].quizathonId
                                        )
                                    }else if (bannerList[0].quizathonStarted) {
                                        val intent = Intent(context, QuizathonActivity::class.java)
                                        intent.putExtra("data", bannerList[0].quizathonQuestion)
                                        intent.putExtra(
                                                "CategoryName",
                                                bannerList[0].quizathonTitle ?: ""
                                        )
                                        intent.putExtra(
                                                "Category",
                                                bannerList[0].quizathonTitle ?: ""
                                        )
                                        intent.putExtra("comingFrom", "")
                                        intent.putExtra("IsRetake", false)
                                        intent.putExtra("RetakeId", 0)
                                        intent.putExtra(
                                                "rewardDate",
                                                bannerList[0].quizathonRewardDate
                                        )
                                        intent.putExtra("qId", bannerList[0].quizathonId)
                                        context.startActivity(createIntent(bannerList[0]))
                                    }else if (bannerList[0].dialogDetail != null && bannerList[0].dialogDetail.type != null){
                                        var apiInterfaceWyh: ApiInterfaceWyh? = null
                                        apiInterfaceWyh = ApiClientWyh.getClient(CommonUtils.getBaseUrlForAPI(context)).create(ApiInterfaceWyh::class.java)
                                        val dialog = QuizMessageDialog(context, bannerList[0].dialogDetail, apiInterfaceWyh)
                                        dialog.show()
                                    }
                                }else if (!!bannerList[0].isQuizathonRegistered) {
                                    if (bannerList[0].quizBurnPoints > 0.toString()) {
                                        (context as? NewDashboardActivity)?.burnType = "QuizEntry"
                                        (context as? NewDashboardActivity)?.index = 0
                                        (context as? NewDashboardActivity)?.click = "QuizEntry"
                                        //NewDashboardHelper.quizathonModelRegister = bannerList[0].quizathonRegistrationQuestion
                                        (context as? NewDashboardActivity)?.showBurnQuizDialog(
                                                bannerList[0].quizBurnPoints.toInt(),
                                                "Quiz",
                                                bannerList[0].burnRegIcon,
                                                "You can participate in the quiz using " + bannerList[0].quizBurnPoints + " HappyYou points",
                                                bannerList[0].quizathonId
                                        )
                                    }else if (bannerList[0].quizathonStarted) {
                                        val intent = Intent(context, QuizathonActivity::class.java)
                                        intent.putExtra("data", bannerList[0].quizathonQuestion)
                                        intent.putExtra(
                                                "CategoryName",
                                                bannerList[0].quizathonTitle ?: ""
                                        )
                                        intent.putExtra(
                                                "Category",
                                                bannerList[0].quizathonTitle ?: ""
                                        )
                                        intent.putExtra("comingFrom", "")
                                        intent.putExtra("IsRetake", false)
                                        intent.putExtra("RetakeId", 0)
                                        intent.putExtra(
                                                "rewardDate",
                                                bannerList[0].quizathonRewardDate
                                        )
                                        intent.putExtra("qId", bannerList[0].quizathonId)
                                        context.startActivity(createIntent(bannerList[0]))
                                    }else if (bannerList[0].dialogDetail != null && bannerList[0].dialogDetail.type != null){
                                        var apiInterfaceWyh: ApiInterfaceWyh? = null
                                        apiInterfaceWyh = ApiClientWyh.getClient(CommonUtils.getBaseUrlForAPI(context)).create(ApiInterfaceWyh::class.java)
                                        val dialog = QuizMessageDialog(context, bannerList[0].dialogDetail, apiInterfaceWyh)
                                        dialog.show()
                                    }
                                }else {
                                    if (bannerList[0].quizathonStarted) {
                                        val intent = Intent(context, QuizathonActivity::class.java)
                                        intent.putExtra("data", bannerList[0].quizathonQuestion)
                                        intent.putExtra(
                                            "CategoryName",
                                            bannerList[0].quizathonTitle ?: ""
                                        )
                                        intent.putExtra(
                                            "Category",
                                            bannerList[0].quizathonTitle ?: ""
                                        )
                                        intent.putExtra("comingFrom", "")
                                        intent.putExtra("IsRetake", false)
                                        intent.putExtra("RetakeId", 0)
                                        intent.putExtra(
                                            "rewardDate",
                                            bannerList[0].quizathonRewardDate
                                        )
                                        intent.putExtra("qId", bannerList[0].quizathonId)
                                        context.startActivity(createIntent(bannerList[0]))
                                    } else {
                                        Toast.makeText(
                                            context,
                                            "Registration is already completed.", Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            } else {

                                if (!bannerList[0].isRegistrationAllowed && bannerList[0].registrationPointBurn >= 0) {
                                    (context as? NewDashboardActivity)?.burnType = "QuizRegistration"
                                    (context as? NewDashboardActivity)?.index = 0
                                    (context as? NewDashboardActivity)?.click = "QuizRegistration"
                                    (context as? NewDashboardActivity)?.showBurnQuizDialog(
                                        bannerList[0].registrationPointBurn,
                                        bannerList[0].burnRegTitle,
                                        bannerList[0].burnRegIcon,
                                        bannerList[0].burnRegMessage,
                                        bannerList[0].quizathonId
                                    )
                                } else {
                                    if (bannerList[0].isRegistrationAllowed) {
                                        val dialog = RegistrationDialog(
                                            context,
                                            bannerList[0].quizathonRegistrationQuestion,
                                            bannerList[0].regTitle,
                                            bannerList[0].quizathonId,
                                            createIntent(bannerList[0]),
                                            bannerList[0].quizathonStarted,
                                            bannerList[0].quizathonTitle
                                        )
                                        dialog.iconUrl = bannerList[0].regIcon
                                        dialog.show()
                                    } else {
                                        if (bannerList[0].dialogDetail != null && bannerList[0].dialogDetail.type != null){
                                            var apiInterfaceWyh: ApiInterfaceWyh? = null
                                            apiInterfaceWyh = ApiClientWyh.getClient(CommonUtils.getBaseUrlForAPI(context)).create(ApiInterfaceWyh::class.java)
                                            val dialog = QuizMessageDialog(context, bannerList[0].dialogDetail, apiInterfaceWyh)
                                            dialog.show()
                                        }
                                        else{
                                            val messageInfoDialog = MessageInfoDialog(
                                                    context,
                                                    ContextCompat.getDrawable(
                                                            context,
                                                            R.drawable.ic_oops
                                                    ),
                                                    "Oops!",
                                                    ContextCompat.getString(context, R.string.quiz_reg_over)
                                            )
                                            messageInfoDialog.show()
                                        }

                                    }
                                }

                            }
                        } else if (!bannerList[0].isRegistered && bannerList[0].registrationData != null && bannerList[0].registrationData.isNotEmpty()) {
                            val rdialog = RegistrationDialog(
                                context,
                                bannerList[0].registrationID,
                                bannerList[0].dialogTitle,
                                redirectionModel,
                                bannerList[0].registrationData,
                                    bannerList[0].isRegistered
                            )
                            rdialog.show()

                        } else {
                            if (bannerList[0].isRegistered != null && bannerList[0].isRegistered) {
                                Toast.makeText(
                                    context,
                                    "Registration is already completed.", Toast.LENGTH_SHORT
                                ).show()
                            }
                            redirectionScreen(redirectionModel, context)
                        }
                    }
                }

            } else if (position == 1) {
                holder.bannerLayoutOne.visibility = View.GONE
                holder.bannerLayoutTwo.visibility = View.VISIBLE
                holder.policyLayout.visibility = View.GONE
                Glide.with(context).load(bannerList[1].imageUrl).into(holder.bannerImgTwo)

                holder.bannerImgTwo.setOnClickListener {
                    APILogs.activityTracker("A_BANNER_${bannerList[1].bannerId}", context)
                    if (bannerList[1].redirectionKey != null) {
                        val redirectionModel = RedirectionModel(
                            bannerList[1].redirectionKey,
                            bannerList[1].openInChrome,
                            "DASHBOARD_BANNER",
                            bannerList[1].redirectionUrl,
                            bannerList[1].title,
                            bannerList[1].disclaimer,
                            bannerList[1].disclaimerImageUrl,
                            "",
                            0,
                            bannerList[1].bannerId,
                            bannerList[1].happyMartKey,
                            bannerList[1].happyMartValue,
                            "actOMeterTribeList.get(0).getCommunityName()",
                            "actOMeterTribeList.get(0).getCommunityType()",
                            bannerList[1].completed,
                            Gson().fromJson(
                                bannerList[1].bannerQuizModel,
                                BannerQuizModel::class.java
                            ),
                            bannerList[1].rewardDate
                        )

                        if (bannerList[1].redirectionKey.equals("playnwin", ignoreCase = false)) {
                            if (!isDateExpired(bannerList[1].quizRegistrationDate)) {
                                if (!bannerList[1].quizathonRegistered) {

                                    val dialog = RegistrationDialog(
                                        context,
                                        bannerList[1].quizathonRegistrationQuestion,
                                        bannerList[1].regTitle,
                                        bannerList[1].quizathonId,
                                        createIntent(bannerList[1]),
                                        bannerList[1].quizathonStarted,
                                        bannerList[1].quizathonTitle
                                    )
                                    dialog.iconUrl = bannerList[1].regIcon
                                    dialog.show()
                                } else if (!!bannerList[1].isQuizathonRegistered) {
                                    if (bannerList[1].quizBurnPoints > 0.toString()) {
                                        (context as? NewDashboardActivity)?.burnType = "QuizEntry"
                                        (context as? NewDashboardActivity)?.index = 1
                                        (context as? NewDashboardActivity)?.click = "QuizEntry"
                                        //NewDashboardHelper.quizathonModelRegister = bannerList[0].quizathonRegistrationQuestion
                                        (context as? NewDashboardActivity)?.showBurnQuizDialog(
                                                bannerList[1].quizBurnPoints.toInt(),
                                               "Quiz",
                                                bannerList[1].burnRegIcon,
                                                "You can participate in the quiz using " + bannerList[1].quizBurnPoints + " HappyYou points",
                                                bannerList[1].quizathonId
                                        )
                                    }else if (bannerList[1].quizathonStarted) {
                                        val intent = Intent(context, QuizathonActivity::class.java)
                                        intent.putExtra("data", bannerList[1].quizathonQuestion)
                                        intent.putExtra(
                                                "CategoryName",
                                                bannerList[1].quizathonTitle ?: ""
                                        )
                                        intent.putExtra(
                                                "Category",
                                                bannerList[1].quizathonTitle ?: ""
                                        )
                                        intent.putExtra("comingFrom", "")
                                        intent.putExtra("IsRetake", false)
                                        intent.putExtra("RetakeId", 1)
                                        intent.putExtra(
                                                "rewardDate",
                                                bannerList[1].quizathonRewardDate
                                        )
                                        intent.putExtra("qId", bannerList[1].quizathonId)
                                        context.startActivity(createIntent(bannerList[1]))
                                    }else if (bannerList[1].dialogDetail != null && bannerList[1].dialogDetail.type != null){
                                        var apiInterfaceWyh: ApiInterfaceWyh? = null
                                        apiInterfaceWyh = ApiClientWyh.getClient(CommonUtils.getBaseUrlForAPI(context)).create(ApiInterfaceWyh::class.java)
                                        val dialog = QuizMessageDialog(context, bannerList[1].dialogDetail, apiInterfaceWyh)
                                        dialog.show()
                                    }
                                }else {
                                    if (bannerList[1].quizathonStarted) {
                                        val intent = Intent(context, QuizathonActivity::class.java)
                                        intent.putExtra("data", bannerList[1].quizathonQuestion)
                                        intent.putExtra(
                                            "CategoryName",
                                            bannerList[1].quizathonTitle ?: ""
                                        )
                                        intent.putExtra(
                                            "Category",
                                            bannerList[1].quizathonTitle ?: ""
                                        )
                                        intent.putExtra("comingFrom", "")
                                        intent.putExtra("IsRetake", false)
                                        intent.putExtra("RetakeId", 0)
                                        intent.putExtra(
                                            "rewardDate",
                                            bannerList[1].quizathonRewardDate
                                        )
                                        intent.putExtra("qId", bannerList[1].quizathonId)
                                        context.startActivity(createIntent(bannerList[1]))
                                    } else {
                                        Toast.makeText(
                                            context,
                                            "Registration is already completed.", Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            } else {

                                if (!bannerList[1].isRegistrationAllowed && bannerList[1].registrationPointBurn >= 0) {
                                    (context as? NewDashboardActivity)?.burnType = "QuizRegistration"
                                    (context as? NewDashboardActivity)?.index = 1
                                    (context as? NewDashboardActivity)?.click = "QuizRegistration"
                                    (context as? NewDashboardActivity)?.showBurnQuizDialog(
                                        bannerList[1].registrationPointBurn,
                                        bannerList[1].burnRegTitle,
                                        bannerList[1].burnRegIcon,
                                        bannerList[1].burnRegMessage,
                                        bannerList[1].quizathonId
                                    )
                                } else {
                                    if (bannerList[1].isRegistrationAllowed&&bannerList[1].isRegistered) {
                                        val dialog = RegistrationDialog(
                                            context,
                                            bannerList[1].quizathonRegistrationQuestion,
                                            bannerList[1].regTitle,
                                            bannerList[1].quizathonId,
                                            createIntent(bannerList[1]),
                                            bannerList[1].quizathonStarted,
                                            bannerList[1].quizathonTitle
                                        )
                                        dialog.iconUrl = bannerList[1].regIcon
                                        dialog.show()
                                    } else {
                                         if (bannerList[1].dialogDetail != null && bannerList[1].dialogDetail.type != null){
                                            var apiInterfaceWyh: ApiInterfaceWyh? = null
                                            apiInterfaceWyh = ApiClientWyh.getClient(CommonUtils.getBaseUrlForAPI(context)).create(ApiInterfaceWyh::class.java)
                                            val dialog = QuizMessageDialog(context, bannerList[1].dialogDetail, apiInterfaceWyh)
                                            dialog.show()
                                        }
                                       else {
                                             val messageInfoDialog = MessageInfoDialog(
                                                     context,
                                                     ContextCompat.getDrawable(
                                                             context,
                                                             R.drawable.no_reward_img
                                                     ),
                                                     "Oops!",
                                                     ContextCompat.getString(context, R.string.quiz_reg_over)
                                             )
                                             messageInfoDialog.show()
                                        }

                                    }
                                }


                            }
                        } else if (!bannerList[1].isRegistered && bannerList[1].registrationData != null && bannerList[1].registrationData.isNotEmpty()) {
                            val rdialog = RegistrationDialog(
                                context,
                                bannerList[1].registrationID,
                                bannerList[1].dialogTitle,
                                redirectionModel,
                                bannerList[1].registrationData,
                                    bannerList[1].isRegistered
                            )
                            rdialog.show()
                        } else {
                            if (bannerList[1].isRegistered != null && bannerList[1].isRegistered) {
                                Toast.makeText(
                                    context,
                                    "Registration is already completed.", Toast.LENGTH_SHORT
                                ).show()
                            }
                            redirectionScreen(redirectionModel, context)
                        }
                    } else if (bannerList[1].redirectionUrl != null && !bannerList[1].redirectionUrl.equals(
                            ""
                        )
                    ) {
                        val redirectionModel = RedirectionModel(
                            "",
                            bannerList[1].openInChrome,
                            "DASHBOARD_BANNER",
                            bannerList[1].redirectionUrl,
                            bannerList[1].title,
                            bannerList[1].disclaimer,
                            bannerList[1].disclaimerImageUrl,
                            "",
                            0,
                            bannerList[1].bannerId,
                            bannerList[1].happyMartKey,
                            bannerList[1].happyMartValue,
                            "actOMeterTribeList.get(0).getCommunityName()",
                            "actOMeterTribeList.get(0).getCommunityType()",
                            bannerList[1].completed,
                            Gson().fromJson(
                                bannerList[1].bannerQuizModel,
                                BannerQuizModel::class.java
                            ),
                            bannerList[1].rewardDate
                        )

                        if (bannerList[1].redirectionKey.equals("playnwin", ignoreCase = false)) {
                            if (!isDateExpired(bannerList[1].quizRegistrationDate)) {
                                if (!bannerList[1].quizathonRegistered) {

                                    val dialog = RegistrationDialog(
                                        context,
                                        bannerList[1].quizathonRegistrationQuestion,
                                        bannerList[1].regTitle,
                                        bannerList[1].quizathonId,
                                        createIntent(bannerList[1]),
                                        bannerList[1].quizathonStarted,
                                        bannerList[1].quizathonTitle
                                    )
                                    dialog.iconUrl = bannerList[1].regIcon
                                    dialog.show()
                                } else if (!!bannerList[1].isQuizathonRegistered) {
                                    if (bannerList[1].quizBurnPoints > 0.toString()) {
                                        (context as? NewDashboardActivity)?.burnType = "QuizEntry"
                                        (context as? NewDashboardActivity)?.index = 1
                                        (context as? NewDashboardActivity)?.click = "QuizEntry"
                                        //NewDashboardHelper.quizathonModelRegister = bannerList[0].quizathonRegistrationQuestion
                                        (context as? NewDashboardActivity)?.showBurnQuizDialog(
                                                bannerList[1].quizBurnPoints.toInt(),
                                                "Quiz",
                                                bannerList[1].burnRegIcon,
                                                "You can participate in the quiz using " + bannerList[1].quizBurnPoints + " HappyYou points",
                                                bannerList[1].quizathonId
                                        )
                                    }else if (bannerList[1].quizathonStarted) {
                                        val intent = Intent(context, QuizathonActivity::class.java)
                                        intent.putExtra("data", bannerList[1].quizathonQuestion)
                                        intent.putExtra(
                                                "CategoryName",
                                                bannerList[1].quizathonTitle ?: ""
                                        )
                                        intent.putExtra(
                                                "Category",
                                                bannerList[1].quizathonTitle ?: ""
                                        )
                                        intent.putExtra("comingFrom", "")
                                        intent.putExtra("IsRetake", false)
                                        intent.putExtra("RetakeId", 1)
                                        intent.putExtra(
                                                "rewardDate",
                                                bannerList[1].quizathonRewardDate
                                        )
                                        intent.putExtra("qId", bannerList[1].quizathonId)
                                        context.startActivity(createIntent(bannerList[1]))
                                    }else if (bannerList[1].dialogDetail != null && bannerList[1].dialogDetail.type != null){
                                        var apiInterfaceWyh: ApiInterfaceWyh? = null
                                        apiInterfaceWyh = ApiClientWyh.getClient(CommonUtils.getBaseUrlForAPI(context)).create(ApiInterfaceWyh::class.java)
                                        val dialog = QuizMessageDialog(context, bannerList[1].dialogDetail, apiInterfaceWyh)
                                        dialog.show()
                                    }
                                }else {
                                    if (bannerList[1].quizathonStarted) {
                                        val intent = Intent(context, QuizathonActivity::class.java)
                                        intent.putExtra("data", bannerList[1].quizathonQuestion)
                                        intent.putExtra(
                                            "CategoryName",
                                            bannerList[1].quizathonTitle ?: ""
                                        )
                                        intent.putExtra(
                                            "Category",
                                            bannerList[1].quizathonTitle ?: ""
                                        )
                                        intent.putExtra("comingFrom", "")
                                        intent.putExtra("IsRetake", false)
                                        intent.putExtra("RetakeId", 0)
                                        intent.putExtra(
                                            "rewardDate",
                                            bannerList[1].quizathonRewardDate
                                        )
                                        intent.putExtra("qId", bannerList[1].quizathonId)
                                        context.startActivity(createIntent(bannerList[1]))
                                    } else {
                                        Toast.makeText(
                                            context,
                                            "Registration is already completed.", Toast.LENGTH_SHORT
                                        ).show()


                                    }
                                }
                            } else {
                                if (!bannerList[1].isRegistrationAllowed && bannerList[1].registrationPointBurn >= 0) {
                                    (context as? NewDashboardActivity)?.burnType = "QuizRegistration"
                                    (context as? NewDashboardActivity)?.index = 1
                                    (context as? NewDashboardActivity)?.click = "QuizRegistration"
                                    (context as? NewDashboardActivity)?.showBurnQuizDialog(
                                        bannerList[1].registrationPointBurn,
                                        bannerList[1].burnRegTitle,
                                        bannerList[1].burnRegIcon,
                                        bannerList[1].burnRegMessage,
                                        bannerList[1].quizathonId
                                    )
                                } else {
                                    if (bannerList[1].isRegistrationAllowed) {
                                        val dialog = RegistrationDialog(
                                            context,
                                            bannerList[1].quizathonRegistrationQuestion,
                                            bannerList[1].regTitle,
                                            bannerList[1].quizathonId,
                                            createIntent(bannerList[1]),
                                            bannerList[1].quizathonStarted,
                                            bannerList[1].quizathonTitle
                                        )
                                        dialog.iconUrl = bannerList[1].regIcon
                                        dialog.show()
                                    } else {
                                        if (bannerList[1].dialogDetail != null && bannerList[1].dialogDetail.type != null){
                                            var apiInterfaceWyh: ApiInterfaceWyh? = null
                                            apiInterfaceWyh = ApiClientWyh.getClient(CommonUtils.getBaseUrlForAPI(context)).create(ApiInterfaceWyh::class.java)
                                            val dialog = QuizMessageDialog(context, bannerList[1].dialogDetail, apiInterfaceWyh)
                                            dialog.show()
                                        }
                                        else{
                                            val messageInfoDialog = MessageInfoDialog(
                                                    context,
                                                    ContextCompat.getDrawable(
                                                            context,
                                                            R.drawable.ic_oops
                                                    ),
                                                    "Oops!",
                                                    ContextCompat.getString(context, R.string.quiz_reg_over)
                                            )
                                            messageInfoDialog.show()
                                        }
                                    }
                                }
                            }
                        } else if (!bannerList[1].isRegistered && bannerList[1].registrationData != null && bannerList[1].registrationData.isNotEmpty()) {
                            val rdialog = RegistrationDialog(
                                context,
                                bannerList[1].registrationID,
                                bannerList[1].dialogTitle,
                                redirectionModel,
                                bannerList[1].registrationData,
                                    bannerList[1].isRegistered
                            )
                            rdialog.show()

                        } else {
                            if (bannerList[1].isRegistered != null && bannerList[1].isRegistered) {
                                Toast.makeText(
                                    context,
                                    "Registration is already completed.", Toast.LENGTH_SHORT
                                ).show()
                            }
                            redirectionScreen(redirectionModel, context)
                        }
                    }
                }
            } else {
                setUserDetail(holder)
            }
        }
    }

    fun setUserDetail(holder: MyViewHolder) {
        try {
            holder.userName.text = userDetail.name
            if (userDetail.gender != null) {
                holder.separatedView.visibility = View.VISIBLE
                holder.userGender.text = userDetail.gender
            } else {
                holder.separatedView.visibility = View.GONE
                holder.userGender.text = ""
            }

            if (userDetail.age != null && userDetail.age.isNotEmpty()) {
                holder.userAge.text = "${userDetail.age} Yrs"
            } else {
                holder.userAge.text = ""
            }

            if (userDetail.abhaNumber != null && userDetail.abhaNumber.isNotEmpty()) {
                holder.abhaNum.text = userDetail.abhaNumber

            } else {
                holder.addAbhaId.visibility = View.VISIBLE
                holder.abhaNum.visibility = View.GONE
            }

            if (userDetail.profileImage != null && userDetail.profileImage.isNotEmpty()) {
                Glide.with(context)
                    .load(userDetail.profileImage)
                    .into(holder.ivProfile)
            }
            if (userDetail.isCorporateEmployee != null && userDetail.isCorporateEmployee == 1) {
                holder.llPolicyNumber.visibility = View.GONE
                holder.llAddPolicy.visibility = View.GONE
                holder.userPolicyExpiryDate.text = "NA"
                holder.policyLayoutExp.visibility = View.GONE
                holder.rlLinkedPolicies.visibility = View.GONE
                holder.policyAbhaSeparator.visibility = View.GONE

            } else {
                holder.llAddPolicy.visibility = View.VISIBLE
                holder.policyLayoutExp.visibility = View.VISIBLE
                holder.policyAbhaSeparator.visibility = View.VISIBLE
                holder.rlLinkedPolicies.visibility = View.VISIBLE
                holder.llPolicyNumber.visibility = View.VISIBLE
            }
            if (clientsPolicyData.isNotEmpty()) {
                //holder.llPolicyNumber.visibility = View.VISIBLE
                //holder.rlLinkedPolicies.visibility = View.VISIBLE
                holder.llAddPolicy.visibility = View.GONE
                holder.policyNum.text =
                    clientsPolicyData.get(holder.absoluteAdapterPosition - bannerList.size).policyNo
                if (clientsPolicyData[holder.absoluteAdapterPosition - bannerList.size].endDate != null) {
                    var formattedEndDate: String? =
                        clientsPolicyData[holder.absoluteAdapterPosition - bannerList.size].endDate
                    try {
                        if (formattedEndDate != null) {
                            formattedEndDate = formatDateFromString(
                                "yyyy-MM-dd'T'HH:mm",
                                "dd/MM/yyyy",
                                formattedEndDate
                            )
                        }
                    } catch (e: Exception) {

                    }
                    holder.userPolicyExpiryDate.text = formattedEndDate

                } else {
                    holder.userPolicyExpiryDate.text = "NA"

                }

            } else {
                if (userDetail.isCorporateEmployee == 0) {
                    holder.llPolicyNumber.visibility = View.GONE
                    holder.llAddPolicy.visibility = View.VISIBLE
                    holder.rlLinkedPolicies.visibility = View.GONE
                    holder.userPolicyExpiryDate.text = "NA"
                }

            }

            holder.addMorePolicy.setOnClickListener {
                activityTracker("Android_DASHBOARD_ADD_POLICY_BTN", context)
                val intent = Intent(context, PolicySearchActivity::class.java)
                context.startActivity(intent)
            }

            holder.btnLinkedPolicy.setOnClickListener {
                val intent = Intent(context, PolicyListActivity::class.java)
                intent.putExtra("intent", "Dashboard")
                context.startActivity(intent)
                activityTracker("Android_POLICY_MENU_CLICK", context)
            }
            holder.policyNum.setOnClickListener {
                val intent = Intent(context, PolicyViewMore::class.java)
                intent.putExtra("clientID", clientsPolicyData.get(0).clientId)
                intent.putExtra("policyNo", clientsPolicyData.get(0).policyNo)
                intent.putExtra("intent", "Dashboard");
                context.startActivity(intent)
            }

            holder.addAbhaID.setOnClickListener {

            }


            holder.btnEditProfile.setOnClickListener {
                APILogs.activityTracker("A_DB_PC_Edit", context)
                val intent = Intent(context, ProfileActivity::class.java)
                intent.putExtra("points", NewDashboardHelper.totalpoints)
                intent.putExtra("currentLevel", NewDashboardHelper.level)
                context.startActivity(intent)
            }

            holder.llAddPolicy.setOnClickListener {
                activityTracker("Android_DASHBOARD_ADD_POLICY_BTN", context)
                val intent = Intent(context, PolicySearchActivity::class.java)
                context.startActivity(intent)
            }

            holder.healthLocker.setOnClickListener {
                APILogs.activityTracker("A_DB_PC_Health Locker", context)
                val intent = Intent(context, EhrActivity::class.java)
                context.startActivity(intent)
            }

        } catch (e: Exception) {
            e.toString()
        }
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
        intent.putExtra("retakePointstoBurn", banner.retakePointstoBurn)
        intent.putExtra("showNextGameButton", banner.isShowNextGameButton())
        intent.putExtra("nextGameRedirectTo", banner.getRedirectTo())
        intent.putExtra("quizModel", banner.getQuizathonModel())
        intent.putExtra("subCategory", banner.quizathonTitle)
        return intent
    }

}