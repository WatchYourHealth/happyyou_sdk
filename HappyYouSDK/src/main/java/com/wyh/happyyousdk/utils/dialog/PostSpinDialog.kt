package com.wyh.happyyousdk.utils.dialog

import android.app.Activity
import android.app.AlertDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.wyh.happyyousdk.APIEncryption.RetrofitHandler
import com.wyh.happyyousdk.R
import com.wyh.happyyousdk.SpinWheel.Activities.DashboardWheelActivity
import com.wyh.happyyousdk.SpinWheel.rewardDialogCloseListener
import com.wyh.happyyousdk.SpinWheel.spinRewardCallBack
import com.wyh.happyyousdk.dashboard.NewDashboardActivity
import com.wyh.happyyousdk.dashboard.NudgeDialogue
import com.wyh.happyyousdk.dashboard.NudgeDialogue.convertTo12HourFormat
import com.wyh.happyyousdk.dashboard.NudgeDialogue.maskVoucherCode
import com.wyh.happyyousdk.dashboard.OtherActivity
import com.wyh.happyyousdk.dashboard.helper.NewDashboardHelper
import com.wyh.happyyousdk.databinding.CustomPopupStampsBinding
import com.wyh.happyyousdk.databinding.LayoutNewPointsPopUpBinding
import com.wyh.happyyousdk.databinding.SpinPointsBadgeLayoutBinding
import com.wyh.happyyousdk.databinding.SpinPointsRewardsLayoutBinding
import com.wyh.happyyousdk.databinding.SpinPopupStampsBinding
import com.wyh.happyyousdk.databinding.SpinnerVoucherLayoutBinding
import com.wyh.happyyousdk.happyMarket.HappyMartCategory
import com.wyh.happyyousdk.happyMarket.HappyMartOthersSection
import com.wyh.happyyousdk.ice.YoutubeActivity
import com.wyh.happyyousdk.model.RewardsModel
import com.wyh.happyyousdk.model.response.AddOnPoints
import com.wyh.happyyousdk.model.response.RewardListResponse
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
import java.text.SimpleDateFormat
import java.util.Locale

object PostSpinDialog {


    fun showPostSpinnerPointsPopup(
        rewardsTitle: String,
        context: Context,
        comingFrom: String,
        desc: String,
        headerOne: String,
        headerTwo: String,
        scratchListener: ScratchListener
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

            (context as Activity).finish()
        }

        binding.ivClose.setOnClickListener {

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

    fun showPostSpinnerBadgePopup(
        context: Context,
        comingFrom: String,
        rewardLogo: String,
        rewardtitle: String,
        desc: String,
        headerOne: String,
        headerTwo: String,
        scratchListener: ScratchListener
    ) {
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
            val intent = Intent(context, RewardsActivity::class.java)
            intent.putExtra("currentIndex", 0)
            context.startActivity(intent)
            alertDialog.dismiss()

            (context as Activity).finish()

        }

        binding.ivClose.setOnClickListener {
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

    fun showPostSpinnerVoucher(
        context: Context,
        rewardLogo: String,
        couponCode: String,
        rewardDesc: String,
        rewardtitle: String,
        expiry: String,
        comingFrom: String,
        headerOne: String,
        headerTwo: String,
        scratchListener: ScratchListener
    ) {
        try {
            var IsMaskedCodeShown = true
            val alertBuilder = AlertDialog.Builder(context)
            val binding: SpinnerVoucherLayoutBinding = DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.spinner_voucher_layout, null, false
            )

            alertBuilder.setView(binding.root)
            NudgeDialogue.alertDialogScratched = alertBuilder.create()
            NudgeDialogue.alertDialogScratched.setCancelable(true)

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
                        "Expiry Date : \n${date[0]} ${convertTo12HourFormat(date[1])}"
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
                NudgeDialogue.alertDialogScratched.dismiss()
                getRewardList(context, "voucher")
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
                NudgeDialogue.alertDialogScratched.dismiss()
                context.startActivity(
                    Intent(context, HappyMartOthersSection::class.java)
                        .putExtra("points", "")
                        .putExtra("comingFrom", "spinner")
                        .putExtra("couponCode", couponCode)
                )
                (context as Activity).finish()


            }
            binding.ivClose.setOnClickListener {
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
            NudgeDialogue.alertDialogScratched.window!!.setLayout(
                (displayRectangle.width() *
                        0.7f).toInt(), (displayRectangle.height() * 0.7f).toInt()
            )


        } catch (e: Exception) {
            e.toString()
        }
    }


    fun showPostSpinnerOffer(
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
        scratchListener: ScratchListener
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
            binding.scratchView.setScratchListener(scratchListener)


            if (!SharedPref.getIsScratchedFirstCard()) SharedPref.putIsScratchedFirstCard(true)
            binding.offerTitle.text = rewardTitle
            binding.descriptionTv.text = partnerDesc


            if (expiry != null && expiry != "") {
                val date = expiry.split(" ")
                if (date.isNotEmpty()) {
                    binding.voucherExpiryTv.text =
                        "Expiry Date : \n${date[0]} ${convertTo12HourFormat(date[1])}"
                } else {
                    binding.voucherExpiryTv.text = ""
                }

            }


            binding.viewRewards.setOnClickListener { view ->
                alertDialog.dismiss()
                getRewardList(context, "voucher")
            }

            binding.claimRewards.setOnClickListener {

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
                (context as Activity).finish()
            }


            binding.ivClose.setOnClickListener {

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

    fun showStampsPopup(
        headerOne: String,
        headerTwo: String,
        name: String,
        points: String,
        context: Context,
        scratchListener: ScratchListener
    ) {
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
            val intent = Intent(context, RewardsActivity::class.java)
            intent.putExtra("currentIndex", 1)
            context.startActivity(intent)
            alertDialog.dismiss()

            (context as Activity).finish()
        }


        binding.ivClose.setOnClickListener {

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

    //---------------------------------------------------------//


    fun showPostSpinnerPointsPopupCallBack(
        rewardsTitle: String,
        context: Context,
        comingFrom: String,
        desc: String,
        headerOne: String,
        headerTwo: String,
        scratchListener: ScratchListener,
        rewardDialogListener: rewardDialogCloseListener
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

            (context as Activity).finish()
        }

        binding.ivClose.setOnClickListener {
            rewardDialogListener.onDialogDismiss()
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

            (context as Activity).finish()

        }


        binding.ivClose.setOnClickListener {
            rewardDialogListener.onDialogDismiss()
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
            var isClaimClicked = false
            var IsMaskedCodeShown = true
            val alertBuilder = AlertDialog.Builder(context)
            val binding: SpinnerVoucherLayoutBinding = DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.spinner_voucher_layout, null, false
            )

            alertBuilder.setView(binding.root)
            NudgeDialogue.alertDialogScratched = alertBuilder.create()
            NudgeDialogue.alertDialogScratched.setCancelable(true)
            NudgeDialogue.alertDialogScratched.setCanceledOnTouchOutside(true)

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
                (context as Activity).finish()


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
            NudgeDialogue.alertDialogScratched.window!!.setLayout(
                (displayRectangle.width() *
                        0.7f).toInt(), (displayRectangle.height() * 0.7f).toInt()
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
            val alertBuilder = AlertDialog.Builder(context)
            var isClaimClicked = false
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
                (context as Activity).finish()
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
            alertDialog.window!!.setLayout(
                (displayRectangle.width() *
                        0.7f).toInt(), (displayRectangle.height() * 0.7f).toInt()
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

            (context as Activity).finish()
        }



        binding.ivClose.setOnClickListener {
            rewardDialogListener.onDialogDismiss()
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

}