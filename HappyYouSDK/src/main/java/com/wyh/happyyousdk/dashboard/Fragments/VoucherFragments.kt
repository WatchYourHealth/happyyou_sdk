package com.wyh.happyyousdk.dashboard.Fragments

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
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.wyh.happyyousdk.APIEncryption.APILogs
import com.wyh.happyyousdk.R
import com.wyh.happyyousdk.dashboard.NudgeDialogue
import com.wyh.happyyousdk.dashboard.adapter.VoucherAdapter
import com.wyh.happyyousdk.dashboard.adapter.voucherClick
import com.wyh.happyyousdk.databinding.PopUpScratchCardScratchableBinding
import com.wyh.happyyousdk.databinding.VoucherLayoutBinding
import com.wyh.happyyousdk.happyMarket.HappyMartCategory
import com.wyh.happyyousdk.happyMarket.HappyMartDisclaimerActivity
import com.wyh.happyyousdk.happyMarket.HappyMartOthersSection
import com.wyh.happyyousdk.model.CommonSuccessResponse
import com.wyh.happyyousdk.model.request.VoucherIdRequest
import com.wyh.happyyousdk.model.response.rewards.RewardsCollectible
import com.wyh.happyyousdk.model.response.voucherListData
import com.wyh.happyyousdk.network.ApiClientWyh
import com.wyh.happyyousdk.network.ApiInterfaceWyh
import com.wyh.happyyousdk.rewards.RewardsCollectiblesActivity
import com.wyh.happyyousdk.utils.Analytics
import com.wyh.happyyousdk.utils.CommonUtils
import com.wyh.happyyousdk.utils.CommonUtils.formatDate
import com.wyh.happyyousdk.utils.Constants
import com.wyh.happyyousdk.utils.SharedPref
import dev.skymansandy.scratchcardlayout.listener.ScratchListener
import dev.skymansandy.scratchcardlayout.ui.ScratchCardLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class VoucherFragments(
    val mcontext: Context,
    val list: ArrayList<voucherListData>,
    val voucherCount: String
) : Fragment(),
    ScratchListener, voucherClick {

    lateinit var binding: VoucherLayoutBinding
    lateinit var adapter: VoucherAdapter
    var voucherIdRequest: VoucherIdRequest? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.voucher_layout, container, false)

        binding.voucherCountTv.text = "Your Voucher Count is $voucherCount"

        if (list.isNotEmpty()) {
            binding.voucherNoDataTv.visibility = View.GONE
            binding.voucherParentLayout.visibility = View.VISIBLE
            if (list.size < 25) {
                binding.voucherViewMoreBtn.visibility = View.GONE
            } else {
                binding.voucherViewMoreBtn.visibility = View.VISIBLE
            }
        } else {
            binding.voucherNoDataTv.visibility = View.VISIBLE
            binding.voucherParentLayout.visibility = View.GONE
            binding.voucherViewMoreBtn.visibility = View.GONE
        }



        binding.voucherCancelBtn.setOnClickListener {
            (mcontext as Activity).finish()

        }

        binding.voucherViewMoreBtn.setOnClickListener {
            mcontext.startActivity(Intent(mcontext, RewardsCollectiblesActivity::class.java))

        }

        adapter = VoucherAdapter(mcontext, list, this)
        binding.voucherRv.layoutManager = LinearLayoutManager(mcontext)
        binding.voucherRv.hasFixedSize()
        binding.voucherRv.adapter = adapter

        return binding.root
    }


    @SuppressLint("SetTextI18n")
    private fun showScratchCard(rewardsCollectible: voucherListData) {
        var IsMaskedCodeShown = true
        var originalCode = ""
        var maskedCode = ""
        val alertBuilder = AlertDialog.Builder(context)
        val binding: PopUpScratchCardScratchableBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.pop_up_scratch_card_scratchable, null, false
        )
        alertBuilder.setView(binding.root)
        val alertDialog = alertBuilder.create()
        alertDialog.setCancelable(true)
        binding.llCopy.visibility = View.VISIBLE
        binding.voucherExpiryTv.visibility = View.VISIBLE


        if (rewardsCollectible.voucherCode != null) {
            originalCode = rewardsCollectible.voucherCode
            maskedCode = NudgeDialogue.maskVoucherCode(rewardsCollectible.voucherCode)
        }


        if (rewardsCollectible.voucherCode != null) {
            binding.tvAmount.visibility = View.GONE
            if (rewardsCollectible.status.equals("Active", true)) {
                binding.btnRedeem.visibility = View.VISIBLE
                binding.btnRedeem.text = "Claim"
                binding.viewVoucher.visibility = View.VISIBLE
                binding.tvCopy.visibility = View.VISIBLE
            } else if (rewardsCollectible.status.equals(
                    "Expired",
                    true
                ) || rewardsCollectible.status.equals("Redeemed", true)
            ) {
                binding.btnRedeem.visibility = View.GONE
                binding.tvCopy.visibility = View.GONE
            }
        } else {
            binding.llCopy.visibility = View.GONE
            binding.btnRedeem.visibility = View.VISIBLE
            binding.tvAmount.visibility = View.GONE
            binding.tvCopy.visibility = View.GONE
        }

        if (rewardsCollectible.validity != null && rewardsCollectible.validity != "") {
            val date = rewardsCollectible.validity.split(" ")
            if (date.isNotEmpty()) {
                binding.voucherExpiryTv.text = "Expiry Date : \n${
                    formatDate(
                        date[0], "MM/dd/yyyy",
                        "dd/MM/yyyy"
                    )
                } ${
                    NudgeDialogue.convertTo12HourFormat(date[1])
                }"
            } else {
                binding.voucherExpiryTv.text = ""

            }

        }

        binding.viewVoucher.setOnClickListener {
            if (IsMaskedCodeShown) {
                IsMaskedCodeShown = false
                binding.tvCouponCode.text = maskedCode
                binding.viewVoucher.setImageDrawable(context?.getDrawable(R.drawable.invisible_eye))
            } else {
                IsMaskedCodeShown = true
                binding.tvCouponCode.text = originalCode
                binding.viewVoucher.setImageDrawable(context?.getDrawable(R.drawable.baseline_remove_red_eye_24))
            }
        }

        binding.scratchView.setScratchListener(this)
        if (rewardsCollectible.isScratched) {
            binding.scratchView.onFullReveal()
        } else {
            voucherIdRequest = VoucherIdRequest(rewardsCollectible.id)
        }
        binding.tvTitle.text = rewardsCollectible.voucherName
        binding.tvValue.text = rewardsCollectible.voucherValue.toString() + " off"
        binding.tvDescription.text = rewardsCollectible.voucherDesc
        binding.tvCouponCode.text = rewardsCollectible.voucherCode
        binding.tvAmount.text = "₹" + rewardsCollectible.voucherValue
        if (!alertDialog.isShowing) alertDialog.show()
        binding.btnRedeem.setOnClickListener { view ->
            alertDialog.dismiss()
            if (binding.btnRedeem.text.equals("Claim")) {
                context?.startActivity(
                    Intent(context, HappyMartOthersSection::class.java)
                        .putExtra("points", "")
                        .putExtra("comingFrom", "spinner")
                        .putExtra("couponCode", rewardsCollectible.voucherCode)
                )
                (context as Activity).finish()
            } else {
                context?.startActivity(
                    Intent(context, HappyMartCategory::class.java)
                        .putExtra("comingFrom", Constants.DASHBOARD_BANNER)
                        .putExtra("selectedPartner", rewardsCollectible.partnerName)
                        .putExtra("toolbarname", rewardsCollectible.partnerName)
                        .putExtra("disclaimer", rewardsCollectible.partnerName)
                        .putExtra("redirectionURL", rewardsCollectible.partnerUrl)
                        .putExtra("disclaimerURL", rewardsCollectible.partnerLogo)
                )
            }

        }


        binding.btnNegative.setOnClickListener {
            APILogs.activityTracker("A_DB_Other_Voucher_View_Close", mcontext)
            alertDialog.dismiss()
        }


        binding.tvCopy.setOnClickListener { view ->
            APILogs.activityTracker("A_DB_Other_Voucher_View_Copy", mcontext)
            val clipboard = mcontext.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("label", rewardsCollectible.voucherCode)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(mcontext, "Copied", Toast.LENGTH_SHORT).show()
        }

        Glide.with(mcontext)
            .load(rewardsCollectible.voucherLogo)
            .error(R.drawable.dummy_image)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .into(binding.ivVendorLogo)

        val displayRectangle = Rect()
        val window: Window = (mcontext as Activity).window
        window.decorView.getWindowVisibleDisplayFrame(displayRectangle)
        alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.window!!.setLayout(
            (displayRectangle.width() *
                    0.7f).toInt(), (displayRectangle.height() * 0.6f).toInt()
        )
    }


    override fun onScratchComplete() {
        if (voucherIdRequest != null) {
            updateScratchStatus()
        }
    }

    override fun onScratchProgress(
        scratchCardLayout: ScratchCardLayout,
        atLeastScratchedPercent: Int
    ) {

    }

    override fun onScratchStarted() {

    }


    private fun updateScratchStatus() {
        val apiInterfaceWyh = ApiClientWyh.getClient(CommonUtils.getBaseUrlForAPI(context))
            .create(ApiInterfaceWyh::class.java)
        val call: Call<CommonSuccessResponse> =
            apiInterfaceWyh.updateScratchStatus(SharedPref.getAuthToken(), voucherIdRequest)
        call.enqueue(object : Callback<CommonSuccessResponse?> {
            override fun onResponse(
                call: Call<CommonSuccessResponse?>,
                response: Response<CommonSuccessResponse?>
            ) {
                if (response.code() == 200 && response.body() != null) {
                    Analytics.logEvent(
                        mcontext,
                        mcontext!!.javaClass.name,
                        mcontext.resources.getString(R.string.scratch_coupon_success)
                    )
                    voucherIdRequest = null
                    /*allCollectibles.get(0).setIsscrached(true)
                    activityCollectibles.get(0).setIsscrached(true)
                    collectiblesAdapter.notifyItemChanged(0)*/
                } else {
                    Analytics.logEvent(
                        mcontext,
                        mcontext!!.javaClass.name,
                        mcontext.resources.getString(R.string.scratch_coupon_failed)
                    )
                }
            }

            override fun onFailure(call: Call<CommonSuccessResponse?>, t: Throwable) {
                Analytics.logEvent(
                    mcontext,
                    mcontext!!.javaClass.name,
                    mcontext.resources.getString(R.string.scratch_coupon_failed)
                )
            }
        })
    }

    override fun voucherClick(data: voucherListData) {
        showScratchCard(data)
    }

}