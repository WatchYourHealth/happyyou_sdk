package com.wyh.happyyousdk.SpinWheel.Activities

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.gson.Gson
import com.wyh.happyyousdk.APIEncryption.APIInterface
import com.wyh.happyyousdk.APIEncryption.APILogs
import com.wyh.happyyousdk.APIEncryption.RetrofitHandler
import com.wyh.happyyousdk.R
import com.wyh.happyyousdk.dashboard.NudgeDialogue
import com.wyh.happyyousdk.dashboard.helper.NewDashboardHelper
import com.wyh.happyyousdk.databinding.ActivityDashboardWheelBinding
import com.wyh.happyyousdk.model.request.AssignRewardsRequest
import com.wyh.happyyousdk.model.request.LockRewardsRequest
import com.wyh.happyyousdk.model.response.AssignRewardsResponse
import com.wyh.happyyousdk.model.response.LockRewardResponse
import com.wyh.happyyousdk.utils.CommonUtils
import com.wyh.happyyousdk.utils.SharedPref
import com.wyh.happyyousdk.utils.wheelview.OnRotationListener
import com.wyhsdk.sharedPreferences.SharedPreference
import dev.skymansandy.scratchcardlayout.listener.ScratchListener
import dev.skymansandy.scratchcardlayout.ui.ScratchCardLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.UUID

class DashboardWheelActivity : AppCompatActivity(), ScratchListener {

    lateinit var apiInterface: APIInterface
    var context: Context? = null
    var rewardPosition = 0
    lateinit var binding: ActivityDashboardWheelBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard_wheel)
        context = this
        SharedPref.init(context)
        SharedPreference.init(context)
        apiInterface = RetrofitHandler.apiInterface()
        binding.lwv.addWheelItems(CommonUtils.wheelItems)
        var arrowImage: ImageView = binding.lwv.findViewById(R.id.iv_arrow)
        arrowImage.visibility = View.GONE

        binding.ivClose.setOnClickListener {
            SharedPref.setSpinWheelStatus(false)
            finish()
        }

        binding.clickToSpin.setOnClickListener {
            APILogs.activityTracker("SPIN_DASHBOARD_CLICK", this@DashboardWheelActivity)
            binding.clickToDisabledSpin.visibility = View.VISIBLE
            binding.clickToSpin.visibility = View.GONE
            binding.lwv.rotateWheelTo(rewardPosition)

        }

        binding.lwv.setLuckyWheelReachTheTarget {
            getSpinRewards(context as DashboardWheelActivity)
        }

        binding.lwv.onRotationListener = OnRotationListener { }

        lockReward()

    }


    private fun lockReward() {
        try {
            CommonUtils.showProgressDialige(this)
            val uniqueId = UUID.randomUUID().toString()
            Log.d("AuthToken", uniqueId)
            val lockRewardsRequest = LockRewardsRequest(uniqueId)
            apiInterface.lockRewards(lockRewardsRequest)
                .enqueue(object : Callback<LockRewardResponse?> {
                    override fun onResponse(
                        call: Call<LockRewardResponse?>,
                        response: Response<LockRewardResponse?>
                    ) {
                        CommonUtils.dismissDialoge()
                        if (response.body() != null && response.code() == 200 && response.isSuccessful) {
                            SharedPref.putTempUUID(uniqueId)
                            if (response.body()!!.data.quadrantPosition != null) {
                                rewardPosition = response.body()!!.data.quadrantPosition
                            }
                        }
                    }

                    override fun onFailure(call: Call<LockRewardResponse?>, t: Throwable) {
                        CommonUtils.dismissDialoge()
                    }
                })
        } catch (e: Exception) {
            CommonUtils.dismissDialoge()
            e.printStackTrace()
        }
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
                        if (response.body() != null && response.code() == 200 && response.isSuccessful) {
                            NewDashboardHelper.isShowUserPopup = false
                            if (NewDashboardHelper.rewardsModel != null && NewDashboardHelper.rewardsModel.reward != null && NewDashboardHelper.rewardsModel.reward.contains(
                                    "First Login"
                                )
                            ) {
                                NewDashboardHelper.showFirstLoginPopup = true
                            }
                            Log.d("AuthToken", "SpinWheel " + Gson().toJson(response.body()))
                            binding.spinDialogLayout.visibility = View.GONE
                            binding.ivClose.visibility = View.GONE
                            if (response.body()!!.data != null) {
                                if (response.body()!!.data.rewardType != null) {
                                    if (response.body()!!.data.isrewardawarded == 1) {
                                        if (response.body()!!.data.rewardType.equals(
                                                "Points",
                                                true
                                            )
                                        ) {
                                            APILogs.activityTracker(
                                                "SPIN_DASHBOARD_POINTS",
                                                this@DashboardWheelActivity
                                            )
                                            NudgeDialogue.showSpinnerPointsPopup(
                                                response.body()!!.data.rewardValue,
                                                response.body()!!.data.rewardTitle,
                                                context,
                                                NewDashboardHelper.rewardsModel,
                                                "WheelDashboard",
                                                response.body()!!.data.rewardDescription,
                                                response.body()!!.data.rewardHeader1,
                                                response.body()!!.data.rewardHeader2
                                            )
                                        } else if (response.body()!!.data.rewardType.equals(
                                                "Offers",
                                                true
                                            )
                                        ) {
                                            APILogs.activityTracker(
                                                "SPIN_DASHBOARD_OFFER",
                                                this@DashboardWheelActivity
                                            )
                                            NudgeDialogue.showSpinnerOffer(
                                                context,
                                                response.body()!!.data.partnerLogo,
                                                response.body()!!.data.rewardDescription,
                                                response.body()!!.data.partnerName,
                                                response.body()!!.data.partnerUrl,
                                                NewDashboardHelper.rewardsModel,
                                                "WheelDashboard",
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
                                            APILogs.activityTracker(
                                                "SPIN_DASHBOARD_VOUCHER",
                                                this@DashboardWheelActivity
                                            )
                                            NudgeDialogue.showSpinnerVoucher(
                                                context,
                                                response.body()!!.data.rewardLogo,
                                                response.body()!!.data.couponCode,
                                                response.body()!!.data.rewardDescription,
                                                response.body()!!.data.rewardType,
                                                response.body()!!.data.rewardTitle,
                                                response.body()!!.data.rewardValue,
                                                NewDashboardHelper.rewardsModel,
                                                response.body()!!.data.expiryInHours,
                                                "WheelDashboard",
                                                response.body()!!.data.rewardHeader1,
                                                response.body()!!.data.rewardHeader2
                                            )
                                        }
                                    } else if (response.body()!!.data.isrewardawarded == -1) {
                                        APILogs.activityTracker(
                                            "SPIN_DASHBOARD_REWARD_NOT_ELIGIBLE",
                                            this@DashboardWheelActivity
                                        )
                                        NudgeDialogue.spinnerCancelDialog(
                                            context,
                                            NewDashboardHelper.rewardsModel,
                                            context.resources.getString(R.string.rewards_not_eligible),
                                            "WheelDashboard_Not_Eligible",
                                            response.body()!!.data.rewardType
                                        )
                                    } else if (response.body()!!.data.isrewardawarded == 0) {
                                        APILogs.activityTracker(
                                            "SPIN_DASHBOARD_REWARD_EXPIRED",
                                            this@DashboardWheelActivity
                                        )
                                        NudgeDialogue.spinnerCancelDialog(
                                            context,
                                            NewDashboardHelper.rewardsModel,
                                            context.resources.getString(R.string.rewards_expired),
                                            "WheelDashboard_Expired",
                                            ""
                                        )
                                    }
                                } else if (response.body()!!.data.isrewardawarded == -1) {
                                    APILogs.activityTracker(
                                        "SPIN_DASHBOARD_REWARD_NOT_ELIGIBLE",
                                        this@DashboardWheelActivity
                                    )
                                    NudgeDialogue.spinnerCancelDialog(
                                        context,
                                        NewDashboardHelper.rewardsModel,
                                        context.resources.getString(R.string.rewards_not_eligible),
                                        "WheelDashboard_Not_Eligible",
                                        response.body()!!.data.rewardType
                                    )
                                } else if (response.body()!!.data.isrewardawarded == 0) {
                                    APILogs.activityTracker(
                                        "SPIN_DASHBOARD_REWARD_EXPIRED",
                                        this@DashboardWheelActivity
                                    )
                                    NudgeDialogue.spinnerCancelDialog(
                                        context,
                                        NewDashboardHelper.rewardsModel,
                                        context.resources.getString(R.string.rewards_expired),
                                        "WheelDashboard_Expired",
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

    override fun onScratchComplete() {

    }

    override fun onScratchProgress(
        scratchCardLayout: ScratchCardLayout,
        i: Int
    ) {
        if (i >= 20 && NudgeDialogue.voucherIdRequest != null && !NewDashboardHelper.isRewards) {
            NewDashboardHelper.updateScratchStatus(this)
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

}