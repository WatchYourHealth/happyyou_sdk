package com.wyh.happyyousdk.happyMarket

import android.app.AlertDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.gson.Gson
import com.wyh.happyyousdk.APIEncryption.RetrofitHandler
import com.wyh.happyyousdk.ChallangesModule.helperClass
import com.wyh.happyyousdk.R
import com.wyh.happyyousdk.dashboard.NudgeDialogue
import com.wyh.happyyousdk.dashboard.OtherActivity
import com.wyh.happyyousdk.dashboard.helper.NewDashboardHelper
import com.wyh.happyyousdk.databinding.ActivityHappyMartOthersSectionBinding
import com.wyh.happyyousdk.databinding.EditNickNameLayoutBinding
import com.wyh.happyyousdk.databinding.PopUpScratchCardScratchableBinding
import com.wyh.happyyousdk.databinding.SpinnerCancelLayoutBinding
import com.wyh.happyyousdk.happyMarket.adapter.HappyMartOtherSectionAdapter
import com.wyh.happyyousdk.happyMarket.adapter.itemClickEvent
import com.wyh.happyyousdk.model.request.AllocateVoucherRequest
import com.wyh.happyyousdk.model.response.*
import com.wyh.happyyousdk.network.ApiClientWyh
import com.wyh.happyyousdk.network.ApiInterfaceWyh
import com.wyh.happyyousdk.utils.Analytics
import com.wyh.happyyousdk.utils.CommonUtils
import com.wyh.happyyousdk.utils.SharedPref
import dev.skymansandy.scratchcardlayout.listener.ScratchListener
import dev.skymansandy.scratchcardlayout.ui.ScratchCardLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HappyMartOthersSection : AppCompatActivity(), itemClickEvent, ScratchListener {

    lateinit var binding: ActivityHappyMartOthersSectionBinding
    var totalPoints = 0;
    lateinit var happyMartSubCategoryChildResponse: HappyMartSubCategoryChildResponse
    var subCategoryList: ArrayList<HappyMartSubCategoryChildResponse> = arrayListOf()
    var comingFrom = "";
    var coupunCode = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_happy_mart_others_section)
        setContentView(binding.root)


        getKliActivities(this)
        totalPoints = intent.getIntExtra("points", 0)

        if (intent.getStringExtra("comingFrom") != null) {
            comingFrom = intent.getStringExtra("comingFrom").toString()
        }

        if (comingFrom.equals("spinner", true)) {
            if (intent.getStringExtra("couponCode") != null) {
                coupunCode = intent.getStringExtra("couponCode").toString()
            }
            val category = HappyMartSubCategoryChildResponse(1)
            showPopupOthers(category, this, "spinner")
        }


        binding.includeBack.tvBack.setTextColor(resources.getColor(R.color.white))
        binding.includeBack.ivBack.setColorFilter(resources.getColor(R.color.white))
        binding.includeBack.llBack.setOnClickListener {
            finish()
        }

    }

    override fun click(subCategoryData: HappyMartSubCategoryChildResponse) {
        if (subCategoryData.vendorName.equals("Voucher Redemption", true)) {
            if (subCategoryData.isParent!!) {
                showPopupOthers(subCategoryData, this, "happyMart")
            }
        } else {
            val intent = Intent(this, OtherSectionDisclaimer::class.java)
            intent.putExtra("vendorName", subCategoryData.vendorName)
                .putExtra("vendorLogo", subCategoryData.vendorLogo)
                .putExtra("redirectionUrl", subCategoryData.redirectionUrl)
                .putExtra("points", totalPoints)
            startActivity(intent)
        }

    }

    private fun getKliActivities(context: Context) {
        try {
            CommonUtils.showProgressDialige(context)
            val apiInterfaceWyh = ApiClientWyh.getClient(CommonUtils.getBaseUrlForAPI(context))
                .create<ApiInterfaceWyh>(ApiInterfaceWyh::class.java)
            val call: Call<GetKliActivitiesResponse> =
                apiInterfaceWyh.getKliActivities(SharedPref.getAuthToken())
            call.enqueue(object : Callback<GetKliActivitiesResponse?> {
                override fun onResponse(
                    call: Call<GetKliActivitiesResponse?>,
                    response: Response<GetKliActivitiesResponse?>
                ) {
                    CommonUtils.dismissDialoge()
                    if (response.code() == 200 && response.body() != null && response.body()!!.isSuccess) {
                        if (response.body()!!.data.isNotEmpty()) {

                            helperClass.othersCategory.forEach {
                                if (it.vendorName.equals("Voucher Redemption", true)) {
                                    helperClass.othersCategory.remove(it)
                                }
                            }
                            response.body()!!.data.forEach {
                                happyMartSubCategoryChildResponse =
                                    HappyMartSubCategoryChildResponse(
                                        it.activityId,
                                        it.activityName,
                                        it.activityLogo,
                                        "",
                                        it.coupon
                                    )

                                helperClass.othersCategory.add(happyMartSubCategoryChildResponse)
                            }

                            binding.happyMartOtherSectionList.hasFixedSize()
                            binding.happyMartOtherSectionList.layoutManager =
                                GridLayoutManager(this@HappyMartOthersSection, 3)
                            val adapter = HappyMartOtherSectionAdapter(
                                this@HappyMartOthersSection,
                                helperClass.othersCategory,
                                this@HappyMartOthersSection
                            )
                            binding.happyMartOtherSectionList.adapter = adapter


                        } else {
                            binding.happyMartOtherSectionList.hasFixedSize()
                            binding.happyMartOtherSectionList.layoutManager =
                                GridLayoutManager(this@HappyMartOthersSection, 3)
                            val adapter = HappyMartOtherSectionAdapter(
                                this@HappyMartOthersSection,
                                helperClass.othersCategory,
                                this@HappyMartOthersSection
                            )
                            binding.happyMartOtherSectionList.adapter = adapter
                        }
                    } else {
                        Toast.makeText(
                            context,
                            resources.getString(R.string.error_string),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<GetKliActivitiesResponse?>, t: Throwable) {
                    CommonUtils.dismissDialoge()
                    Analytics.logEvent(
                        context,
                        context.javaClass.getName(),
                        getString(R.string.get_profile_bookmark_failed)
                    )
                    Toast.makeText(
                        context,
                        resources.getString(R.string.error_string),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        } catch (e: Exception) {
            CommonUtils.dismissDialoge()
            e.toString()
        }

    }


    private fun showPopupOthers(
        data: HappyMartSubCategoryChildResponse,
        context: Context,
        comingFrom: String
    ) {
        val alertBuilder = AlertDialog.Builder(context)
        val bindingVoucher: EditNickNameLayoutBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.edit_nick_name_layout, null, false
        )
        alertBuilder.setView(bindingVoucher.root)
        val alertDialog = alertBuilder.create()
        alertDialog.setCancelable(true)
        if (!alertDialog.isShowing) alertDialog.show()
        bindingVoucher.tvName.text = "Coupon"
        bindingVoucher.edtName.visibility = View.GONE
        bindingVoucher.edtVoucher.visibility = View.VISIBLE

        bindingVoucher.edtVoucher.filters = arrayOf<InputFilter>(LengthFilter(30))
        if (comingFrom.equals("spinner", true)) {
            bindingVoucher.edtVoucher.setText(coupunCode)
        }


        bindingVoucher.btnCancel.setOnClickListener {
            alertDialog.dismiss()
            if (bindingVoucher.edtVoucher.text.toString() != "" && !bindingVoucher.edtVoucher.text.toString()
                    .isEmpty()
            ) {
                cancelDialog()
            }

        }

        bindingVoucher.btnSubmit.setOnClickListener { v ->
            if (bindingVoucher.edtVoucher.text.toString() != "" && !bindingVoucher.edtVoucher.text.toString()
                    .isEmpty()
            ) {
                val view = binding.root
                if (view != null) {
                    val imm =
                        getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(view.windowToken, 0)
                }
                allocateVoucher(
                    data.id!!,
                    bindingVoucher.edtVoucher.text.toString().trim(),
                    alertDialog,
                    context
                )
            } else {
                Toast.makeText(context, "Please enter coupon code", Toast.LENGTH_SHORT).show()
            }
        }
        val displayRectangle = Rect()
        val window = window
        window.decorView.getWindowVisibleDisplayFrame(displayRectangle)
        alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.window!!.setLayout(
            (displayRectangle.width() * 0.88f).toInt(),
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )
    }


    private fun allocateVoucher(
        activityId: Int,
        voucher: String,
        alertDialog: AlertDialog,
        context: Context
    ) {
        val request = AllocateVoucherRequest(voucher, activityId, "")
        val apiInterfaceWyh = ApiClientWyh.getClient(CommonUtils.getBaseUrlForAPI(context))
            .create<ApiInterfaceWyh>(ApiInterfaceWyh::class.java)
        val call: Call<AllocateVoucherResponse> =
            apiInterfaceWyh.allocateVoucher(SharedPref.getAuthToken(), request)
        call.enqueue(object : Callback<AllocateVoucherResponse?> {
            override fun onResponse(
                call: Call<AllocateVoucherResponse?>,
                response: Response<AllocateVoucherResponse?>
            ) {
                Log.d("res", Gson().toJson(response.code()))
                Log.d("res", Gson().toJson(response.body()))
                if (response.code() == 200 && response.body() != null) {
                    Analytics.logEvent(
                        context,
                        context.javaClass.name,
                        getString(R.string.profile_update_nick_name_success)
                    )
                    if (response.body()!!.success) {
                        alertDialog.dismiss()
                        Handler().postDelayed({
                            showScratchCard(
                                response.body()!!.data,
                                R.drawable.scratch_card_orange_new,
                                context
                            )
                        }, 500)
                    } else {
                        Toast.makeText(context, response.body()!!.msg, Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Analytics.logEvent(
                        context,
                        context.javaClass.name,
                        getString(R.string.profile_update_nick_name_failed)
                    )
                }
            }

            override fun onFailure(call: Call<AllocateVoucherResponse?>, t: Throwable) {
                Analytics.logEvent(
                    context,
                    context.javaClass.name,
                    getString(R.string.profile_update_nick_name_failed)
                )
            }
        })
    }


    private fun showScratchCard(
        allocateVoucherData: AllocateVoucherData,
        drawable: Int,
        context: Context
    ) {
        var IsMaskedCodeShown = true
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
        binding.scratchView.onFullReveal()
        binding.tvEarned.visibility = View.VISIBLE
        binding.viewVoucher.visibility = View.VISIBLE
        binding.btnRedeem.visibility = View.VISIBLE

        if (allocateVoucherData.voucherCode != null) {
            binding.llCopy.visibility = View.VISIBLE
            binding.llScratchview.visibility = View.GONE
        } else {
            binding.llCopy.visibility = View.GONE
            binding.llScratchview.visibility = View.VISIBLE
        }

        val originalCode = allocateVoucherData.voucherCode
        val maskedCode = NudgeDialogue.maskVoucherCode(allocateVoucherData.voucherCode)

        binding.scratchView.setScratchListener(this@HappyMartOthersSection)
        binding.scratchView.setScratchDrawable(ContextCompat.getDrawable(context, drawable))
        binding.tvTitle.text = allocateVoucherData.voucherName
        binding.tvValue.text = allocateVoucherData.voucherValue + " off"
        binding.tvDescription.text = allocateVoucherData.voucherDesc
        binding.tvCouponCode.text = maskedCode
        binding.tvAmount.text = "₹ ${allocateVoucherData.voucherValue}"


        binding.btnNegative.setOnClickListener {
            alertDialogScratched.dismiss()
            cancelDialog()
        }

        binding.tvCopy.setOnClickListener { view ->
            val clipboard =
                context.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            val clip =
                ClipData.newPlainText("label", allocateVoucherData.voucherCode)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(context, "Copied", Toast.LENGTH_SHORT).show()
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

        binding.btnRedeem.setOnClickListener {
            getRewardList(this)
        }

        binding.ivClose.setOnClickListener { view -> alertDialogScratched.dismiss() }
        Glide.with(context)
            .load(allocateVoucherData.voucherLogo)
            .error(R.drawable.dummy_image)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .into(binding.ivVendorLogo)
        val displayRectangle = Rect()
        val window = window
        window.decorView.getWindowVisibleDisplayFrame(displayRectangle)
        alertDialogScratched.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialogScratched.window!!.setLayout(
            (displayRectangle.width() *
                    0.7f).toInt(), (displayRectangle.height() * 0.5f).toInt()
        )
    }

    override fun onScratchComplete() {

    }

    override fun onScratchProgress(
        scratchCardLayout: ScratchCardLayout,
        atLeastScratchedPercent: Int
    ) {

    }

    override fun onScratchStarted() {

    }

    fun cancelDialog() {
        try {
            try {
                val alertBuilder = AlertDialog.Builder(this)
                val binding: SpinnerCancelLayoutBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(this), R.layout.spinner_cancel_layout, null, false
                )

                alertBuilder.setView(binding.root)
                binding.scratchView.onFullReveal()

                val alertDialogScratched = alertBuilder.create()
                alertDialogScratched.setCancelable(true)


                binding.ivClose.setOnClickListener {
                    alertDialogScratched.dismiss()
                }

                if (!alertDialogScratched.isShowing) alertDialogScratched.show()
                val displayRectangle = Rect()
                val window = window
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
        } catch (e: Exception) {
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

                            val intent =
                                Intent(this@HappyMartOthersSection, OtherActivity::class.java)
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
                                    .putExtra("comingFrom", "spinner")
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


}