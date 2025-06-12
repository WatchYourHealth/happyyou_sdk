package com.wyh.happyyousdk.utils.dialog

import android.app.Activity
import android.app.AlertDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.wyh.happyyousdk.APIEncryption.RetrofitHandler
import com.wyh.happyyousdk.R
import com.wyh.happyyousdk.SpinWheel.Activities.DashboardWheelActivity
import com.wyh.happyyousdk.SpinWheel.rewardDialogCloseListener
import com.wyh.happyyousdk.dashboard.NewDashboardActivity
import com.wyh.happyyousdk.dashboard.NudgeDialogue
import com.wyh.happyyousdk.dashboard.NudgeDialogue.convertTo12HourFormat
import com.wyh.happyyousdk.dashboard.NudgeDialogue.maskVoucherCode
import com.wyh.happyyousdk.dashboard.OtherActivity
import com.wyh.happyyousdk.dashboard.helper.NewDashboardHelper
import com.wyh.happyyousdk.dashboard.helper.NewDashboardHelper.Companion.featureName
import com.wyh.happyyousdk.dashboard.helper.NewDashboardHelper.Companion.trasactionId
import com.wyh.happyyousdk.databinding.SpinPointsBadgeLayoutBinding
import com.wyh.happyyousdk.databinding.SpinPointsRewardsLayoutBinding
import com.wyh.happyyousdk.databinding.SpinPopupStampsBinding
import com.wyh.happyyousdk.databinding.SpinnerVoucherLayoutBinding
import com.wyh.happyyousdk.happyMarket.HappyMartCategory
import com.wyh.happyyousdk.happyMarket.HappyMartOthersSection
import com.wyh.happyyousdk.ice.YoutubeActivity
import com.wyh.happyyousdk.model.CommonSuccessResponse
import com.wyh.happyyousdk.model.request.quizathon.ActivityRewardRequest
import com.wyh.happyyousdk.model.request.quizathon.QuizScratchRequest
import com.wyh.happyyousdk.model.response.RewardListResponse
import com.wyh.happyyousdk.model.response.playwin.DialogModel
import com.wyh.happyyousdk.network.ApiClientWyh
import com.wyh.happyyousdk.network.ApiInterfaceWyh
import com.wyh.happyyousdk.quizathon.QuizathonScoreActivity
import com.wyh.happyyousdk.quizathon.dialog.QuizMessageDialog
import com.wyh.happyyousdk.rewards.RewardsActivity
import com.wyh.happyyousdk.utils.Analytics
import com.wyh.happyyousdk.utils.CommonUtils
import com.wyh.happyyousdk.utils.CommonUtils.formatDate
import com.wyh.happyyousdk.utils.Constants
import com.wyh.happyyousdk.utils.SharedPref
import dev.skymansandy.scratchcardlayout.listener.ScratchListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object QuizRewardDialog {

    var isApiCalled: Boolean = false;
    var isDirectReward: Boolean = false;

    //region Non Scratch
    fun showPostSpinnerPointsPopupCallBack(
        rewardsTitle: String,
        context: Context,
        comingFrom: String,
        desc: String,
        headerOne: String,
        headerTwo: String,
        scratchListener: ScratchListener,
        rewardDialogListener: rewardDialogCloseListener,
        isDirectReward:Boolean=false
    ) {
        isApiCalled = false
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
        alertDialog.setCanceledOnTouchOutside(false)

        if (comingFrom.equals("Dashboard", true)) {
            binding.scratchView.setScratchListener(scratchListener)
        } else {
            binding.scratchView.setScratchListener(scratchListener)
        }
        if (!alertDialog.isShowing) alertDialog.show()

        binding.mainLayout.background =
            context.resources.getDrawable(R.drawable.one_sided_curved_border_info)
//        if(!isDirectReward) {
//            binding.scratchView.onFullReveal()
//        }
        binding.scratchView.onFullReveal()
        binding.tvPoints.text = rewardsTitle
        binding.pointsDesc.text = desc
        binding.headerOne.text = headerOne
        binding.headerTwo.text = headerTwo
        binding.ivClose.text = "Close"

        binding.btnPositive.setOnClickListener {
            val intent = Intent(context, RewardsActivity::class.java)
            intent.putExtra("currentIndex", 0)
            context.startActivity(intent)
            alertDialog.dismiss()
            backActivity(context)
        }

        binding.ivClose.setOnClickListener {
            rewardDialogListener.onDialogDismiss()
            alertDialog.dismiss()

        }

        val displayRectangle = Rect()
        val window = (context as Activity).window

        window.decorView.getWindowVisibleDisplayFrame(displayRectangle)
        alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val (dialogWidth, dialogHeight) = getDialogDimensions(context)
        alertDialog.window!!.setLayout(
            dialogWidth, dialogHeight
        )

    }

    fun showPostSpinnerBadgePopupCallBack(
        context: Context,
        comingFrom: String,
        rewardLogo: String,
        rewardtitle: String,
        desc: String,
        headerOne: String,
        headerTwo: String,
        scratchListener: ScratchListener,
        rewardDialogListener: rewardDialogCloseListener,
        isDirectReward:Boolean=false
    ) {
        isApiCalled = false
        val alertBuilder = AlertDialog.Builder(context)
        val binding: SpinPointsBadgeLayoutBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.spin_points_badge_layout,
            null,
            false
        )
        alertBuilder.setView(binding.root)
        val alertDialog = alertBuilder.create()

        alertDialog.setCancelable(false)
        alertDialog.setCanceledOnTouchOutside(false)

        Glide.with(context)
            .load(rewardLogo)
            .error(R.drawable.dummy_image)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .into(binding.ivScratch)


        binding.scratchView.setScratchListener(scratchListener)

        if (!alertDialog.isShowing) alertDialog.show()

        binding.mainLayout.background =
            context.resources.getDrawable(R.drawable.one_sided_curved_border_info)
        /*if(!isDirectReward) {
            binding.scratchView.onFullReveal()
        }*/
        binding.scratchView.onFullReveal()
        binding.pointsDesc.text = desc
        binding.tvTitle.text = rewardtitle
        binding.headerOne.text = headerOne
        binding.headerTwo.text = headerTwo
        binding.ivClose.text = "Close"

        binding.btnPositive.setOnClickListener {
            getRewardList(context, "badge")
            alertDialog.dismiss()
            backActivity(context)

        }


        binding.ivClose.setOnClickListener {
            rewardDialogListener.onDialogDismiss()
            alertDialog.dismiss()
        }

        val displayRectangle = Rect()
        val window = (context as Activity).window

        window.decorView.getWindowVisibleDisplayFrame(displayRectangle)
        alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val (dialogWidth, dialogHeight) = getDialogDimensions(context)
        alertDialog.window!!.setLayout(
            dialogWidth, dialogHeight
        )
    }

    fun showPostSpinnerVoucherCallBack(
        context: Context,
        rewardLogo: String,
        couponCode: String,
        rewardDesc: String,
        rewardtitle: String,
        expiry: String,
        comingFrom: String,
        headerOne: String,
        headerTwo: String,
        scratchListener: ScratchListener,
        rewardDialogListener: rewardDialogCloseListener,
        isDirectReward:Boolean=false
    ) {
        try {
            isApiCalled = false
            var isClaimClicked = false
            var IsMaskedCodeShown = true
            val alertBuilder = AlertDialog.Builder(context)
            val binding: SpinnerVoucherLayoutBinding = DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.spinner_voucher_layout, null, false
            )

            alertBuilder.setView(binding.root)
            NudgeDialogue.alertDialogScratched = alertBuilder.create()
            NudgeDialogue.alertDialogScratched.setCancelable(false)
            NudgeDialogue.alertDialogScratched.setCanceledOnTouchOutside(false)

            NudgeDialogue.alertDialogScratched.setOnDismissListener({
                if (!isClaimClicked) {
                    rewardDialogListener.onDialogDismiss()
                    NudgeDialogue.alertDialogScratched.dismiss()
                }
            })


            Log.d("AuthToken", "Logo $rewardLogo")
            binding.mainLayout.background =
                context.resources.getDrawable(R.drawable.one_sided_curved_border_info)
            binding.scratchView.onFullReveal()
            binding.scratchView.setScratchListener(scratchListener)

            if (!NudgeDialogue.alertDialogScratched.isShowing) NudgeDialogue.alertDialogScratched.show()

            val originalCode = couponCode
            val maskedCode = maskVoucherCode(couponCode)


            if (expiry != null && expiry != "") {
                val date = expiry.split(" ")
                if (date.isNotEmpty()) {
                    binding.voucherExpiryTv.text =
                        "Expiry Date : \n${
                            formatDate(
                                date[0],
                                "MM/dd/yyyy",
                                "dd/MM/yyyy"
                            )
                        } ${convertTo12HourFormat(date[1])}"
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
                isClaimClicked = true
                NudgeDialogue.alertDialogScratched.dismiss()
                getRewardList(context, "voucher")
                if (context is YoutubeActivity) {
                    (context as Activity).finish()
                }
            }

            binding.viewVoucher.setOnClickListener {
                isClaimClicked = true
                if (IsMaskedCodeShown) {
                    IsMaskedCodeShown = false
                    binding.tvCouponCode.text = maskedCode
                    binding.viewVoucher.setImageDrawable(context.getDrawable(R.drawable.invisible_eye))
                } else {
                    IsMaskedCodeShown = true
                    binding.tvCouponCode.text = originalCode
                    binding.viewVoucher.setImageDrawable(context.getDrawable(R.drawable.baseline_remove_red_eye_24))
                }
                if (context is YoutubeActivity) {
                    (context as Activity).finish()
                }
            }



            binding.claimRewards.setOnClickListener {
                isClaimClicked = true
                NewDashboardHelper.isShowUserPopup = true
                SharedPref.setSpinWheelStatus(false)
                NudgeDialogue.alertDialogScratched.dismiss()
                context.startActivity(
                    Intent(context, HappyMartOthersSection::class.java)
                        .putExtra("points", "")
                        .putExtra("comingFrom", "spinner")
                        .putExtra("couponCode", couponCode)
                )

                backActivity(context)


            }
            binding.ivClose.setOnClickListener {
                isClaimClicked = true
                rewardDialogListener.onDialogDismiss()
                NudgeDialogue.alertDialogScratched.dismiss()
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
            NudgeDialogue.alertDialogScratched.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val (dialogWidth, dialogHeight) = getDialogDimensions(context)
            NudgeDialogue.alertDialogScratched.window!!.setLayout(
                dialogWidth, dialogHeight
            )


        } catch (e: Exception) {
            e.toString()
        }
    }


    fun showPostSpinnerOfferCallBack(
        context: Context,
        partnerLogo: String,
        partnerDesc: String,
        partnerName: String,
        partnerurl: String,
        comingFrom: String,
        expiry: String,
        rewardTitle: String,
        headerOne: String,
        headerTwo: String,
        scratchListener: ScratchListener,
        rewardDialogListener: rewardDialogCloseListener,
        isDirectReward:Boolean=false
    ) {
        try {
            isApiCalled = false
            val alertBuilder = AlertDialog.Builder(context)
            var isClaimClicked = false
            val binding: SpinnerVoucherLayoutBinding = DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.spinner_voucher_layout, null, false
            )

            alertBuilder.setView(binding.root)
            val alertDialog = alertBuilder.create()
            alertDialog.setCancelable(false)
            alertDialog.setCanceledOnTouchOutside(false)

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
            binding.scratchView.setScratchListener(scratchListener)


            if (!SharedPref.getIsScratchedFirstCard()) SharedPref.putIsScratchedFirstCard(true)
            binding.offerTitle.text = rewardTitle
            binding.descriptionTv.text = partnerDesc


            if (expiry != null && expiry != "") {
                val date = expiry.split(" ")
                if (date.isNotEmpty()) {
                    binding.voucherExpiryTv.text =
                        "Expiry Date : \n${
                            formatDate(
                                date[0],
                                "MM/dd/yyyy",
                                "dd/MM/yyyy"
                            )
                        } ${convertTo12HourFormat(date[1])}"
                } else {
                    binding.voucherExpiryTv.text = ""
                }

            }


            binding.viewRewards.setOnClickListener { view ->
                isClaimClicked = true
                alertDialog.dismiss()
                getRewardList(context, "voucher")
                if (context is YoutubeActivity) {
                    (context as Activity).finish()
                }
            }

            binding.claimRewards.setOnClickListener {
                isClaimClicked = true
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
                backActivity(context)
            }


            binding.ivClose.setOnClickListener {
                isClaimClicked = true
                rewardDialogListener.onDialogDismiss()
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
            val (dialogWidth, dialogHeight) = getDialogDimensions(context)
            alertDialog.window!!.setLayout(
                dialogWidth, dialogHeight
            )

        } catch (e: Exception) {
            e.toString()
        }
    }

    fun showStampsPopupCallBack(
        headerOne: String,
        headerTwo: String,
        name: String,
        points: String,
        context: Context,
        scratchListener: ScratchListener,
        rewardDialogListener: rewardDialogCloseListener,
        isDirectReward:Boolean=false
    ) {
        isApiCalled = false
        val alertBuilder = AlertDialog.Builder(context)
        val binding: SpinPopupStampsBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.spin_popup_stamps,
            null,
            false
        )
        alertBuilder.setView(binding.root)
        val alertDialog = alertBuilder.create()

        alertDialog.setCancelable(false)
        alertDialog.setCanceledOnTouchOutside(false)

        binding.scratchView.setScratchListener(scratchListener)

        if (!alertDialog.isShowing) alertDialog.show()

        binding.mainLayout.background =
            context.resources.getDrawable(R.drawable.one_sided_curved_border_info)
        /*if(!isDirectReward) {
            binding.scratchView.onFullReveal()
        }*/
        binding.scratchView.onFullReveal()
        binding.headerOne.text = headerOne
        binding.headerTwo.text = headerTwo
        binding.tvTitle.text = name
        binding.tvDescription2.text = "No. of Stamps: $points"
        binding.ivClose.text = "Close"


        binding.btnPositive.setOnClickListener {
            getRewardList(context, "Stamps")
//            val intent = Intent(context, RewardsActivity::class.java)
//            intent.putExtra("currentIndex", 1)
//            context.startActivity(intent)
            alertDialog.dismiss()
            backActivity(context)
        }



        binding.ivClose.setOnClickListener {
            rewardDialogListener.onDialogDismiss()
            alertDialog.dismiss()

        }

        val displayRectangle = Rect()
        val window = (context as Activity).window

        window.decorView.getWindowVisibleDisplayFrame(displayRectangle)
        alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val (dialogWidth, dialogHeight) = getDialogDimensions(context)
        alertDialog.window!!.setLayout(
            dialogWidth, dialogHeight
        )
    }
    //endregion

    //region WithScratch
    fun showPostSpinnerPointsPopupCallBack(
        rewardsTitle: String,
        context: Context,
        comingFrom: String,
        desc: String,
        headerOne: String,
        headerTwo: String,
        scratchListener: ScratchListener,
        rewardDialogListener: rewardDialogCloseListener,
    ) {
        isApiCalled = false
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
        alertDialog.setCanceledOnTouchOutside(false)

        if (comingFrom.equals("Dashboard", true)) {
            binding.scratchView.setScratchListener(scratchListener)
        } else {
            binding.scratchView.setScratchListener(scratchListener)
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
            val intent = Intent(context, RewardsActivity::class.java)
            intent.putExtra("currentIndex", 0)
            context.startActivity(intent)
            alertDialog.dismiss()
            backActivity(context)
        }

        binding.ivClose.setOnClickListener {
            rewardDialogListener.onDialogDismiss()
            alertDialog.dismiss()

        }

        val displayRectangle = Rect()
        val window = (context as Activity).window

        window.decorView.getWindowVisibleDisplayFrame(displayRectangle)
        alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val (dialogWidth, dialogHeight) = getDialogDimensions(context)
        alertDialog.window!!.setLayout(
            dialogWidth, dialogHeight
        )

    }

    fun showPostSpinnerBadgePopupCallBack(
        context: Context,
        comingFrom: String,
        rewardLogo: String,
        rewardtitle: String,
        desc: String,
        headerOne: String,
        headerTwo: String,
        scratchListener: ScratchListener,
        rewardDialogListener: rewardDialogCloseListener
    ) {
        isApiCalled = false
        val alertBuilder = AlertDialog.Builder(context)
        val binding: SpinPointsBadgeLayoutBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.spin_points_badge_layout,
            null,
            false
        )
        alertBuilder.setView(binding.root)
        val alertDialog = alertBuilder.create()

        alertDialog.setCancelable(false)
        alertDialog.setCanceledOnTouchOutside(false)

        Glide.with(context)
            .load(rewardLogo)
            .error(R.drawable.dummy_image)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .into(binding.ivScratch)


        binding.scratchView.setScratchListener(scratchListener)

        if (!alertDialog.isShowing) alertDialog.show()

        binding.mainLayout.background =
            context.resources.getDrawable(R.drawable.one_sided_curved_border_info)
        //binding.scratchView.visibility = View.INVISIBLE
        binding.scratchView.onFullReveal()
        binding.pointsDesc.text = desc
        binding.tvTitle.text = rewardtitle
        binding.headerOne.text = headerOne
        binding.headerTwo.text = headerTwo

        binding.btnPositive.setOnClickListener {
            getRewardList(context, "badge")
            alertDialog.dismiss()
            backActivity(context)

        }


        binding.ivClose.setOnClickListener {
            rewardDialogListener.onDialogDismiss()
            alertDialog.dismiss()
        }

        val displayRectangle = Rect()
        val window = (context as Activity).window

        window.decorView.getWindowVisibleDisplayFrame(displayRectangle)
        alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val (dialogWidth, dialogHeight) = getDialogDimensions(context)
        alertDialog.window!!.setLayout(
            dialogWidth, dialogHeight
        )
    }

    fun showPostSpinnerVoucherCallBack(
        context: Context,
        rewardLogo: String,
        couponCode: String,
        rewardDesc: String,
        rewardtitle: String,
        expiry: String,
        comingFrom: String,
        headerOne: String,
        headerTwo: String,
        scratchListener: ScratchListener,
        rewardDialogListener: rewardDialogCloseListener
    ) {
        try {
            isApiCalled = false
            var isClaimClicked = false
            var IsMaskedCodeShown = true
            val alertBuilder = AlertDialog.Builder(context)
            val binding: SpinnerVoucherLayoutBinding = DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.spinner_voucher_layout, null, false
            )

            alertBuilder.setView(binding.root)
            NudgeDialogue.alertDialogScratched = alertBuilder.create()
            NudgeDialogue.alertDialogScratched.setCancelable(false)
            NudgeDialogue.alertDialogScratched.setCanceledOnTouchOutside(false)

            NudgeDialogue.alertDialogScratched.setOnDismissListener({
                if (!isClaimClicked) {
                    rewardDialogListener.onDialogDismiss()
                    NudgeDialogue.alertDialogScratched.dismiss()
                }
            })


            Log.d("AuthToken", "Logo $rewardLogo")
            binding.mainLayout.background =
                context.resources.getDrawable(R.drawable.one_sided_curved_border_info)
            binding.scratchView.onFullReveal()
            binding.scratchView.setScratchListener(scratchListener)

            if (!NudgeDialogue.alertDialogScratched.isShowing) NudgeDialogue.alertDialogScratched.show()

            val originalCode = couponCode
            val maskedCode = maskVoucherCode(couponCode)


            if (expiry != null && expiry != "") {
                val date = expiry.split(" ")
                if (date.isNotEmpty()) {
                    binding.voucherExpiryTv.text =
                        "Expiry Date : \n${
                            formatDate(
                                date[0],
                                "MM/dd/yyyy",
                                "dd/MM/yyyy"
                            )
                        } ${convertTo12HourFormat(date[1])}"
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
                isClaimClicked = true
                NudgeDialogue.alertDialogScratched.dismiss()
                getRewardList(context, "voucher")
                if (context is YoutubeActivity) {
                    (context as Activity).finish()
                }
            }

            binding.viewVoucher.setOnClickListener {
                isClaimClicked = true
                if (IsMaskedCodeShown) {
                    IsMaskedCodeShown = false
                    binding.tvCouponCode.text = maskedCode
                    binding.viewVoucher.setImageDrawable(context.getDrawable(R.drawable.invisible_eye))
                } else {
                    IsMaskedCodeShown = true
                    binding.tvCouponCode.text = originalCode
                    binding.viewVoucher.setImageDrawable(context.getDrawable(R.drawable.baseline_remove_red_eye_24))
                }
                if (context is YoutubeActivity) {
                    (context as Activity).finish()
                }
            }



            binding.claimRewards.setOnClickListener {
                isClaimClicked = true
                NewDashboardHelper.isShowUserPopup = true
                SharedPref.setSpinWheelStatus(false)
                NudgeDialogue.alertDialogScratched.dismiss()
                context.startActivity(
                    Intent(context, HappyMartOthersSection::class.java)
                        .putExtra("points", "")
                        .putExtra("comingFrom", "spinner")
                        .putExtra("couponCode", couponCode)
                )
                backActivity(context)


            }
            binding.ivClose.setOnClickListener {
                isClaimClicked = true
                rewardDialogListener.onDialogDismiss()
                NudgeDialogue.alertDialogScratched.dismiss()
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
            NudgeDialogue.alertDialogScratched.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val (dialogWidth, dialogHeight) = getDialogDimensions(context)
            NudgeDialogue.alertDialogScratched.window!!.setLayout(
                dialogWidth, dialogHeight
            )


        } catch (e: Exception) {
            e.toString()
        }
    }


    fun showPostSpinnerOfferCallBack(
        context: Context,
        partnerLogo: String,
        partnerDesc: String,
        partnerName: String,
        partnerurl: String,
        comingFrom: String,
        expiry: String,
        rewardTitle: String,
        headerOne: String,
        headerTwo: String,
        scratchListener: ScratchListener,
        rewardDialogListener: rewardDialogCloseListener
    ) {
        try {
            isApiCalled = false
            val alertBuilder = AlertDialog.Builder(context)
            var isClaimClicked = false
            val binding: SpinnerVoucherLayoutBinding = DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.spinner_voucher_layout, null, false
            )

            alertBuilder.setView(binding.root)
            val alertDialog = alertBuilder.create()
            alertDialog.setCancelable(false)
            alertDialog.setCanceledOnTouchOutside(false)

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
            binding.scratchView.setScratchListener(scratchListener)


            if (!SharedPref.getIsScratchedFirstCard()) SharedPref.putIsScratchedFirstCard(true)
            binding.offerTitle.text = rewardTitle
            binding.descriptionTv.text = partnerDesc


            if (expiry != null && expiry != "") {
                val date = expiry.split(" ")
                if (date.isNotEmpty()) {
                    binding.voucherExpiryTv.text =
                        "Expiry Date : \n${
                            formatDate(
                                date[0],
                                "MM/dd/yyyy",
                                "dd/MM/yyyy"
                            )
                        } ${convertTo12HourFormat(date[1])}"
                } else {
                    binding.voucherExpiryTv.text = ""
                }

            }


            binding.viewRewards.setOnClickListener { view ->
                isClaimClicked = true
                alertDialog.dismiss()
                getRewardList(context, "voucher")
                if (context is YoutubeActivity) {
                    (context as Activity).finish()
                }
            }

            binding.claimRewards.setOnClickListener {
                isClaimClicked = true
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
                backActivity(context)
            }


            binding.ivClose.setOnClickListener {
                isClaimClicked = true
                rewardDialogListener.onDialogDismiss()
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
            val (dialogWidth, dialogHeight) = getDialogDimensions(context)
            alertDialog.window!!.setLayout(
                dialogWidth, dialogHeight
            )

        } catch (e: Exception) {
            e.toString()
        }
    }

    fun showStampsPopupCallBack(
        headerOne: String,
        headerTwo: String,
        name: String,
        points: String,
        context: Context,
        scratchListener: ScratchListener,
        rewardDialogListener: rewardDialogCloseListener
    ) {
        isApiCalled = false
        val alertBuilder = AlertDialog.Builder(context)
        val binding: SpinPopupStampsBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.spin_popup_stamps,
            null,
            false
        )
        alertBuilder.setView(binding.root)
        val alertDialog = alertBuilder.create()

        alertDialog.setCancelable(false)
        alertDialog.setCanceledOnTouchOutside(false)

        binding.scratchView.setScratchListener(scratchListener)

        if (!alertDialog.isShowing) alertDialog.show()

        binding.mainLayout.background =
            context.resources.getDrawable(R.drawable.one_sided_curved_border_info)
        //binding.scratchView.visibility = View.INVISIBLE
        binding.scratchView.onFullReveal()
        binding.headerOne.text = headerOne
        binding.headerTwo.text = headerTwo
        binding.tvTitle.text = name
        binding.tvDescription2.text = "No. of Stamps: $points"


        binding.btnPositive.setOnClickListener {
            getRewardList(context, "Stamps")
//            val intent = Intent(context, RewardsActivity::class.java)
//            intent.putExtra("currentIndex", 1)
//            context.startActivity(intent)
            alertDialog.dismiss()
            backActivity(context);
        }



        binding.ivClose.setOnClickListener {
            rewardDialogListener.onDialogDismiss()
            alertDialog.dismiss()

        }

        val displayRectangle = Rect()
        val window = (context as Activity).window

        window.decorView.getWindowVisibleDisplayFrame(displayRectangle)
        alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val (dialogWidth, dialogHeight) = getDialogDimensions(context)
        alertDialog.window!!.setLayout(
            dialogWidth, dialogHeight
        )
    }
    //endregion


    fun getRewardList(context: Context, comingFrom: String) {
        try {
            CommonUtils.showProgressDialige(context)
            val apiInterface = RetrofitHandler.apiInterface()
            apiInterface.getRewardList(SharedPref.getAuthToken()).enqueue(object :
                Callback<RewardListResponse> {
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
                                response.body()!!.data.rewardsSummary.stampCount ?: ""
                            )
                            .putExtra(
                                "voucher",
                                response.body()!!.data.rewardsSummary.voucherCount ?: ""
                            )
                            .putExtra(
                                "badges",
                                response.body()!!.data.rewardsSummary.badgeCount ?: ""
                            )
                            .putExtra("comingFrom", comingFrom)

                        if (comingFrom.equals(
                                "Voucher",
                                ignoreCase = true
                            ) || comingFrom.equals("Offers", ignoreCase = true)
                        ) {
                            intent.putExtra("current", 2)
                        }
                        if (comingFrom.equals("badge", ignoreCase = true)) {
                            intent.putExtra("current", 1)
                        }
                        if (comingFrom.equals("Stamps", ignoreCase = true)) {
                            intent.putExtra("current", 0)
                        }
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


    public fun FetchQuizReward(context: Context,transactionId: String) {
        val activityRewardRequest = ActivityRewardRequest(trasactionId, featureName)
        val apiInterface = RetrofitHandler.apiInterface()
        val call = apiInterface.FetchQuizReward(SharedPref.getAuthToken(), activityRewardRequest)

        call.enqueue(object : Callback<CommonSuccessResponse?> {
            override fun onResponse(
                call: Call<CommonSuccessResponse?>,
                response: Response<CommonSuccessResponse?>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    trasactionId = null
                    featureName = null
                    if (response.body()!!.quizathonRewardData != null) {

                    }
                }
            }

            override fun onFailure(call: Call<CommonSuccessResponse?>, t: Throwable) {
            }
        })
    }


    fun getRewardRedirections(context: Context, comingFrom: String) {
        try {
            if (comingFrom.equals("Points")) {
                val intent = Intent(context, RewardsActivity::class.java)
                intent.putExtra("currentIndex", 0)
                context.startActivity(intent)
            } else {

                CommonUtils.showProgressDialige(context)
                val apiInterface = RetrofitHandler.apiInterface()
                apiInterface.getRewardList(SharedPref.getAuthToken()).enqueue(object :
                    Callback<RewardListResponse> {
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
                                    response.body()!!.data.rewardsSummary.stampCount ?: ""
                                )
                                .putExtra(
                                    "voucher",
                                    response.body()!!.data.rewardsSummary.voucherCount ?: ""
                                )
                                .putExtra(
                                    "badges",
                                    response.body()!!.data.rewardsSummary.badgeCount ?: ""
                                )
                                .putExtra("comingFrom", comingFrom)
                            if (comingFrom.equals("Badge")) {
                                intent.putExtra("current", 1)
                            } else if (comingFrom.equals(
                                    "Voucher",
                                    ignoreCase = true
                                ) || comingFrom.equals("Offers", ignoreCase = true)
                            ) {
                                intent.putExtra("current", 2)
                            }
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
            }
        } catch (e: Exception) {
            CommonUtils.dismissDialoge()
            e.toString()
        }
    }

    fun QuizScratchCard(context: Context, activityTransId: String, IsScratched: Boolean) {
        if (!isApiCalled) {
            isApiCalled = true
            val apiInterface = RetrofitHandler.apiInterface()
            var quizScratachRequest: QuizScratchRequest =
                QuizScratchRequest(activityTransId, IsScratched)
            apiInterface.QuizScratchCard(SharedPref.getAuthToken(), quizScratachRequest)
                .enqueue(object : Callback<CommonSuccessResponse> {
                    override fun onResponse(
                        call: Call<CommonSuccessResponse>,
                        response: Response<CommonSuccessResponse>
                    ) {

                    }

                    override fun onFailure(call: Call<CommonSuccessResponse>, t: Throwable) {

                    }

                })
        }
    }


    fun getDialogDimensions(
        context: Context,
        widthFactor: Float = 0.67f,
        heightFactor: Float = 0.80f
    ): Pair<Int, Int> {
        val displayMetrics = context.resources.displayMetrics
        val screenHeight = displayMetrics.heightPixels
        val screenWidth = displayMetrics.widthPixels

        val dialogHeight = when {
            screenHeight > 2000 -> (screenHeight * 0.5).toInt() // Large screens
            screenHeight > 1200 -> (screenHeight * heightFactor).toInt() // Medium screens
            else -> (screenHeight * 0.7).toInt() // Small screens
        }

        val dialogWidth = (screenWidth * widthFactor).toInt()
        return Pair(dialogWidth, dialogHeight)
    }

    fun showFutureRewardDialog(context: Context,dialogModel:DialogModel,claimDate:String){
        val apiInterfaceWyh: ApiInterfaceWyh = ApiClientWyh.getClient(CommonUtils.getBaseUrlForAPI(context)).create(
            ApiInterfaceWyh::class.java
        )
        val dialog = QuizMessageDialog(
            context,
            dialogModel,claimDate, apiInterfaceWyh
        )
        dialog.show()
    }

    fun backActivity(context:Context ){
        if (context !is NewDashboardActivity && context !is QuizathonScoreActivity) {
            (context as Activity).finish()
        }
    }
}