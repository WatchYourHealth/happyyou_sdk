package com.wyh.happyyousdk.happyMarket

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.wyh.happyyousdk.APIEncryption.APIInterface
import com.wyh.happyyousdk.APIEncryption.APILogs
import com.wyh.happyyousdk.APIEncryption.RetrofitHandler
import com.wyh.happyyousdk.ChallangesModule.helperClass
import com.wyh.happyyousdk.R
import com.wyh.happyyousdk.TermsAndConditionActivity
import com.wyh.happyyousdk.WebActivity

import com.wyh.happyyousdk.databinding.ActivityHappyMartCategoryBinding
import com.wyh.happyyousdk.happyMarket.adapter.CategoryAdapter
import com.wyh.happyyousdk.happyMarket.adapter.HappyMartPartnerOtherAdapter
import com.wyh.happyyousdk.happyMarket.adapter.onClickCategory
import com.wyh.happyyousdk.model.request.HappyMartCategoryRequest
import com.wyh.happyyousdk.model.request.rewards.GetRewardsDashboardRequest
import com.wyh.happyyousdk.model.response.CategoryData
import com.wyh.happyyousdk.model.response.HappyMartCategoryResponse
import com.wyh.happyyousdk.model.response.HappyMartSubCategoryChildResponse
import com.wyh.happyyousdk.model.response.HappyMartSubCategoryResponse
import com.wyh.happyyousdk.model.response.rewards.LevelDashboardResponse
import com.wyh.happyyousdk.network.ApiClientWyh
import com.wyh.happyyousdk.network.ApiInterfaceWyh
import com.wyh.happyyousdk.utils.Analytics
import com.wyh.happyyousdk.utils.CommonUtils
import com.wyh.happyyousdk.utils.Constants
import com.wyh.happyyousdk.utils.SharedPref
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URLEncoder
import java.text.DecimalFormat
import kotlin.math.roundToInt


class HappyMartCategory : AppCompatActivity(), onClickCategory,
    HappyMartPartnerOtherAdapter.onOthersItemClick {

    lateinit var binding: ActivityHappyMartCategoryBinding
    var categoryID = ""
    var category = ""
    var comingFrom = ""
    lateinit var apiInterface: APIInterface
    lateinit var categoryAdapter: CategoryAdapter
    lateinit var othersAdapter: HappyMartPartnerOtherAdapter
    var responseData: HappyMartSubCategoryResponse? = null
    var categoryResponseData: ArrayList<CategoryData> = arrayListOf()
    var happyMartUrl = ""
    var totalPoint = 0.0
    var enterAmount = "0"
    var format = DecimalFormat("0.##")
    var disclaimer = ""
    var redirectTo = ""
    var redirectionURL = ""
    var disclaimerURL = ""
    var isAlive = false
    lateinit var context: Context


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_happy_mart_category)
        categoryID = intent.getStringExtra("id").toString()
        category = intent.getStringExtra("category").toString()
        comingFrom = intent.getStringExtra("comingFrom").toString()
        context = this
        apiInterface = RetrofitHandler.apiInterface()
        binding.includeBack.tvBack.text = category

        if (comingFrom.equals(Constants.DASHBOARD_BANNER, true)) {
            disclaimer = intent.getStringExtra("disclaimer") ?: ""
            redirectionURL = intent.getStringExtra("redirectionURL") ?: ""
            disclaimerURL = intent.getStringExtra("disclaimerURL") ?: ""

            binding.includeBack.tvBack.text = disclaimer
            Log.d("AuthToken", "$category $disclaimer $redirectionURL $disclaimerURL $comingFrom")
            setBannerDislaimer()
        } else if (comingFrom.equals(Constants.NOTIFICATION, true)) {
            disclaimer = intent.getStringExtra("disclaimer") ?: ""
            redirectionURL = intent.getStringExtra("redirectionURL") ?: ""
            disclaimerURL = intent.getStringExtra("disclaimerURL") ?: ""
            redirectTo = intent.getStringExtra("redirectTo") ?: ""

            binding.includeBack.tvBack.text = disclaimer

            disclaimer = redirectTo
            Log.d("AuthToken", "$category $disclaimer $redirectionURL $disclaimerURL $comingFrom ")
            setBannerDislaimer()
        } else {
            getCategories()
        }
        binding.includeBack.tvBack.setTextColor(resources.getColor(R.color.white))
        binding.includeBack.ivBack.setColorFilter(resources.getColor(R.color.white))

        binding.includeBack.llBack.setOnClickListener {
            binding.edtAmount.setText("")
            if (comingFrom.equals(Constants.DASHBOARD_BANNER,true)) {
                finish()
            } else {
                if (binding.discalimerLayout.visibility == View.VISIBLE) {
                    binding.tvInfo.visibility = View.VISIBLE
                    binding.happyMartCategoryRv.visibility = View.VISIBLE
                    binding.discalimerLayout.visibility = View.GONE
                    binding.checkBox.isChecked = false

                } else {
                    if(comingFrom.equals("redirection",true) || comingFrom.equals("search",true) || comingFrom.equals("dashboard",true)){
                        finish()
                    }else{
                        val intent = Intent(this, NewHappyMartActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
            }

        }


        binding.edtAmount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0!!.isNotEmpty()) {
                    try {
                        val amount: Double = p0.toString().toInt() * 0.2
                        enterAmount = String.format("%.2f", amount)
                        binding.enterWalletAmount.text = String.format("%.2f", amount)
                    } catch (ex: NumberFormatException) {
                        ex.printStackTrace()
                    }
                }else{
                    binding.enterWalletAmount.text = "00.00"

                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })


        val textCondition = SpannableString("I accept the Terms of Use")
        val termsAndCondition: ClickableSpan = object : ClickableSpan() {
            override fun onClick(view: View) {
                val intent = Intent(this@HappyMartCategory, TermsAndConditionActivity::class.java)
                startActivity(intent)
            }
        }
        textCondition.setSpan(termsAndCondition, 13, 25, 0)
        binding.tvTandc.movementMethod = LinkMovementMethod.getInstance()
        binding.tvTandc.setText(textCondition, TextView.BufferType.SPANNABLE)

        binding.tvCopy.setOnClickListener { view ->
            val clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            val clip =
                ClipData.newPlainText("label", binding.tvCouponCode.text.toString())
            clipboard.setPrimaryClip(clip)
            Toast.makeText(this, "Copied", Toast.LENGTH_SHORT).show()
        }


        binding.btnProceed.setOnClickListener(object : View.OnClickListener {

            override fun onClick(p0: View?) {
                if (!binding.checkBox.isChecked) {
                    Toast.makeText(this@HappyMartCategory, "Please accept the Terms of Use", Toast.LENGTH_SHORT).show()
                    return
                }
                APILogs.activityTracker("A_HAPPY_MART_PROCEED_${disclaimer}",context)
                if (comingFrom.equals(Constants.DASHBOARD_BANNER, true) || comingFrom.equals(Constants.NOTIFICATION, true)) {
                    if (disclaimer.equals("get vist", true) || disclaimer.equals("GetVisit", true) || disclaimer.equals("visitor", true)||disclaimer.equals("get visit", true)) {
                        HappyMartDisclaimerActivity().getUserType(this@HappyMartCategory,redirectionURL)
                    } else if (disclaimer.equals("health assure", true) || disclaimer.equals("connect & heal", true) || disclaimer.equals("healthassure", true) || disclaimer.equals("connected", true)) {
                        diagonistics(disclaimer,redirectionURL)
                    } else if (disclaimer.equals("amaha", true)) {
                        HappyMartDisclaimerActivity().getAmahaKey(this@HappyMartCategory)
                    } else if(redirectionURL.contains("zoom")){
                        val uri = Uri.parse(redirectionURL.replace("zoomus://", "https://"))
                        val sendIntent = Intent(Intent.ACTION_VIEW, uri)
                        startActivity(sendIntent)
                    }else if (redirectionURL.contains("{uuid}")) {
                        if(isAlive){
                            happyMartUrl = redirectionURL.replace("{uuid}", URLEncoder.encode(SharedPref.getAesUuid()))
                        }else{
                            happyMartUrl = redirectionURL.replace("{uuid}", SharedPref.getAesUuid())
                        }
                        openWebView(happyMartUrl, "")

                    } else if (redirectionURL.contains("userId=") || redirectionURL.contains("UID=") || redirectionURL.contains("ku=")
                    ) {
                        happyMartUrl = redirectionURL + SharedPref.getAesUuid()
                        openWebView(happyMartUrl, "")
                    } else if(redirectionURL.contains("{guid}")){
                        happyMartUrl = redirectionURL.replace("{guid}", SharedPref.getAesUuid())
                        openWebView(happyMartUrl, "")
                    }
                    else {
                        happyMartUrl = redirectionURL
                        openWebView(happyMartUrl, "")

                    }

                } else {
                    if (responseData!!.vendorName.equals("get vist", true) || responseData!!.vendorName.equals("GetVisit", true)||responseData!!.vendorName.equals("get visit", true) ) {
                        HappyMartDisclaimerActivity().getUserType(this@HappyMartCategory, responseData!!.redirectionUrl)
                    } else if (category.equals("Diagnostics and LAB Tests", true)) {
                        diagonistics(responseData!!.vendorName!!,responseData!!.redirectionUrl!!)
                    } else if (responseData!!.vendorName.equals("connect & heal", true)||responseData!!.vendorName.equals("health assure", true)) {
                        diagonistics(responseData!!.vendorName!!,responseData!!.redirectionUrl!!)
                    } else if (responseData!!.vendorName.equals("amaha", true)) {
                        HappyMartDisclaimerActivity().getAmahaKey(this@HappyMartCategory)
                    } else {
                        openWebView(happyMartUrl, "")
                    }
                }

            }
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        binding.edtAmount.setText("")
        if (comingFrom.equals(Constants.DASHBOARD_BANNER,true)) {
            finish()
        } else {
            if (binding.discalimerLayout.visibility == View.VISIBLE) {
                if(binding.checkBox.isChecked){
                    binding.checkBox.isChecked = false
                }
                binding.tvInfo.visibility = View.VISIBLE
                binding.happyMartCategoryRv.visibility = View.VISIBLE
                binding.discalimerLayout.visibility = View.GONE

            } else {
                if(comingFrom.equals("redirection",true) || comingFrom.equals("search",true) || comingFrom.equals("dashboard", true)){
                    finish()
                }else{
                    val intent = Intent(this@HappyMartCategory, NewHappyMartActivity::class.java)
                    startActivity(intent)
                    finish()
                }

            }
        }
    }

    private fun setDisclaimerPage(subCategoryData: HappyMartSubCategoryResponse) {
        try {
            binding.tvInfo.visibility = View.GONE
            binding.happyMartCategoryRv.visibility = View.GONE
            binding.discalimerLayout.visibility = View.VISIBLE
            if(binding.checkBox.isChecked){
                binding.checkBox.isChecked = false
            }
            Glide.with(this).load(subCategoryData.vendorLogo).into(binding.partnerImage)

            if (subCategoryData.vendorName.equals("Pharmeasy", true)) {
                binding.llCoupon.visibility = View.VISIBLE
            } else {
                binding.llCoupon.visibility = View.GONE
            }

            if (SharedPref.getCurrentLevel() >= 6) {
                /*if (category.equals("Diagnostics and LAB Tests", ignoreCase = true)) {
                    if (!responseData!!.vendorName.equals("GetVisit", ignoreCase = true)||!responseData!!.vendorName.equals("get visit", ignoreCase = true)||!responseData!!.vendorName.equals("get vist", ignoreCase = true)) {
                        binding.llAmount.visibility = View.VISIBLE
                        getRewardsDashboardData()
                    } else {
                        binding.llAmount.visibility = View.GONE
                    }*/

                    if (responseData!!.vendorName.equals("health assure", true)||responseData!!.vendorName.equals("connect & heal", true)||responseData!!.vendorName.equals("healthassure", true)||responseData!!.vendorName.equals("connected", true)) {
                        binding.llAmount.visibility = View.VISIBLE
                        getRewardsDashboardData()
                    } else {
                        binding.llAmount.visibility = View.GONE
                    }


            }
            binding.tvDisclaimer.loadData(
                resources.getString(R.string.disclaimer),
                "text/html",
                "utf-8"
            )

        } catch (e: Exception) {
            e.toString()
        }
    }

    fun setBannerDislaimer() {
        try {
            binding.tvInfo.visibility = View.GONE
            binding.happyMartCategoryRv.visibility = View.GONE
            binding.discalimerLayout.visibility = View.VISIBLE
            Glide.with(this).load(disclaimerURL).into(binding.partnerImage)
            binding.tvDisclaimer.loadData(
                resources.getString(R.string.disclaimer),
                "text/html",
                "utf-8"
            )

            if (comingFrom.equals(Constants.NOTIFICATION, true)) {
                if (redirectionURL.contains("{uuid}")) {
                    if(isAlive){
                        happyMartUrl = redirectionURL.replace("{uuid}", URLEncoder.encode(SharedPref.getAesUuid()))
                    }else{
                        happyMartUrl = redirectionURL.replace("{uuid}", SharedPref.getAesUuid())
                    }
                } else if (redirectionURL.contains("userId=") || redirectionURL.contains("UID=") || redirectionURL.contains("ku=")
                ) {
                    happyMartUrl = redirectionURL + SharedPref.getAesUuid()
                }else if(redirectionURL.contains("{guid}")){
                    happyMartUrl = redirectionURL.replace("{guid}", SharedPref.getAesUuid())
                } else {
                    happyMartUrl = redirectionURL
                }
            }

        } catch (e: Exception) {
            e.toString()
        }
    }

    fun getCategories() {
        try {
            CommonUtils.showProgressDialige(this)
            if(categoryID.equals("14",true)){
                isAlive = true
            }
            val request = HappyMartCategoryRequest(categoryID)
            apiInterface.getCategories(SharedPref.getAuthToken(), request)
                .enqueue(object : Callback<HappyMartCategoryResponse> {
                    override fun onResponse(
                        call: Call<HappyMartCategoryResponse>,
                        response: Response<HappyMartCategoryResponse>
                    ) {
                        CommonUtils.dismissDialoge()
                        if (response.isSuccessful && response.body() != null && response.code() == 200) {
                            if (response.body()!!.data.isEmpty()) {
                                binding.llComingSoon.visibility = View.VISIBLE
                            } else {
                                binding.llComingSoon.visibility = View.GONE
                                categoryResponseData = response.body()!!.data
                                if (category.equals("others", true)) {
                                    setUpOthers()
                                } else {
                                    binding.happyMartCategoryRv.visibility = View.VISIBLE
                                    binding.happyMartCategoryRv.hasFixedSize()
                                    binding.happyMartCategoryRv.layoutManager = GridLayoutManager(this@HappyMartCategory, 2)
                                    categoryAdapter = CategoryAdapter(this@HappyMartCategory,
                                        response.body()!!.data,
                                        this@HappyMartCategory)
                                    binding.happyMartCategoryRv.adapter = categoryAdapter
                                }

                            }

                        }
                    }

                    override fun onFailure(call: Call<HappyMartCategoryResponse>, t: Throwable) {
                        CommonUtils.dismissDialoge()

                    }

                })
        } catch (e: Exception) {
            CommonUtils.dismissDialoge()
        }
    }

    fun setUpOthers() {
        try {
            binding.llOthers.visibility = View.VISIBLE
            othersAdapter = HappyMartPartnerOtherAdapter(this, categoryResponseData, this)
            binding.rvOther.adapter = othersAdapter
        } catch (e: Exception) {
            e.toString()
        }
    }

    fun getRewardsDashboardData() {
        try {
            CommonUtils.showProgressDialige(this)
            val request = GetRewardsDashboardRequest("")
            val apiInterface = ApiClientWyh.getClient(CommonUtils.getBaseUrlForAPI(this))
                .create<ApiInterfaceWyh>(ApiInterfaceWyh::class.java)
            apiInterface.getRewardsDashboardData(SharedPref.getAuthToken(), request)
                .enqueue(object : Callback<LevelDashboardResponse> {
                    override fun onResponse(
                        call: Call<LevelDashboardResponse>,
                        response: Response<LevelDashboardResponse>
                    ) {
                        CommonUtils.dismissDialoge()
                        if (response.code() == 200 && response.isSuccessful && response.body() != null) {
                            Analytics.logEvent(
                                this@HappyMartCategory,
                                this@HappyMartCategory.javaClass.getName(),
                                getString(R.string.get_rewards_dash_board_data_success)
                            )
                            val totalAmount = response.body()!!.data.equivalentAmount
                            totalPoint = (totalAmount / 0.2).roundToInt().toDouble()
                            binding.walletAmount.text =
                                "Your available points: ${format.format(totalPoint)}"
                        } else {
                            Analytics.logEvent(
                                this@HappyMartCategory,
                                this@HappyMartCategory.javaClass.getName(),
                                getString(R.string.get_rewards_dash_board_data_success)
                            )
                        }

                    }

                    override fun onFailure(call: Call<LevelDashboardResponse>, t: Throwable) {
                        CommonUtils.dismissDialoge()

                    }

                })
        } catch (e: Exception) {
            CommonUtils.dismissDialoge()
            e.toString()
        }

    }

    private fun checkAmount(): Boolean {
        return if (!binding.edtAmount.text.toString().isEmpty()) {
            totalPoint >= java.lang.Double.valueOf(binding.edtAmount.text.toString())
        } else {
            false
        }
    }

    private fun diagonistics(selectedPartner: String, redirectionURL: String) {
        try {
            if (SharedPref.getCurrentLevel() < 6) {
                if (selectedPartner == "health_assure" || selectedPartner == "Health assure" || selectedPartner.equals("healthassure", true)) {
                    HappyMartDisclaimerActivity().getHACustomerRegistration(this, enterAmount)
                } else if (selectedPartner.equals("GetVisit", ignoreCase = true) || selectedPartner.equals("visitor", true)||selectedPartner.equals("Get Visit", ignoreCase = true)||selectedPartner.equals("get vist", ignoreCase = true)) {
                    HappyMartDisclaimerActivity().getUserType(this@HappyMartCategory,redirectionURL)
                } else {
                    HappyMartDisclaimerActivity().cHCustomerRegistration(this)
                }
            } else {
                if (!binding.enterWalletAmount.text.toString().equals("00.00")) {
                    if (checkAmount()) {
                        if (binding.enterWalletAmount.text.toString().toDouble().toInt() > 0) {
                            HappyMartDisclaimerActivity().updateUserRewardBalance(this, selectedPartner,binding.enterWalletAmount.text.toString())
                        } else {
                            Toast.makeText(
                                this,
                                "Please enter a valid amount.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(this, "Please enter a valid amount.", Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {
                    if (selectedPartner == "health_assure" || selectedPartner == "Health assure" || selectedPartner.equals("healthassure", true)) {
                        HappyMartDisclaimerActivity().getHACustomerRegistration(this, enterAmount)
                    } else if (selectedPartner.equals("GetVisit", ignoreCase = true) || selectedPartner.equals("visitor", true)|| selectedPartner.equals("get vist", true)||selectedPartner.equals("get visit", true)) {
                        HappyMartDisclaimerActivity().getUserType(this@HappyMartCategory,redirectionURL)
                    } else {
                        HappyMartDisclaimerActivity().cHCustomerRegistration(this)
                    }
                }
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    fun openWebView(url: String, loginUrl: String) {
        try {
            startActivity(
                Intent(this, WebActivity::class.java)
                    .putExtra("Url", url)
                    .putExtra("comingFrom","HappyMartCategory")
                    .putExtra("loginUrl", loginUrl)
            )
        } catch (e: Exception) {

        }
    }

    override fun onClick(response: HappyMartSubCategoryResponse) {

        if (response.redirectionUrl!!.contains("{uuid}")) {
            if(isAlive){
                happyMartUrl = response.redirectionUrl!!.replace("{uuid}", URLEncoder.encode(SharedPref.getAesUuid()))
            }else{
                happyMartUrl = response.redirectionUrl!!.replace("{uuid}", SharedPref.getAesUuid())
            }
        } else if (response.redirectionUrl!!.contains("userId=") || response.redirectionUrl!!.contains("UID=") || response.redirectionUrl!!.contains("ku=")
        ) {
            happyMartUrl = response.redirectionUrl + SharedPref.getAesUuid()
        } else if (response.redirectionUrl!!.contains("{guid}")){
            happyMartUrl = response.redirectionUrl!!.replace("{guid}", SharedPref.getAesUuid())
        }
        else {
            happyMartUrl = response.redirectionUrl!!
        }

        responseData = response
        setDisclaimerPage(response)
    }


    override fun onClick(categoryData: ArrayList<HappyMartSubCategoryChildResponse>?) {
        helperClass.othersCategory = categoryData!!
        startActivity(
            Intent(this@HappyMartCategory, HappyMartOthersSection::class.java)
                .putExtra("points", totalPoint)
                .putExtra("comingFrom","happyMart")
        )
    }


}