package com.wyh.happyyousdk.happyMarket

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.wyh.happyyousdk.APIEncryption.RetrofitHandler
import com.wyh.happyyousdk.R
import com.wyh.happyyousdk.SDKConstants
import com.wyh.happyyousdk.common.adapter.IndicatorsAdapter

import com.wyh.happyyousdk.dashboard.NewDashboardActivity
import com.wyh.happyyousdk.dashboard.helper.NewDashboardHelper
import com.wyh.happyyousdk.databinding.ActivityNewHappyMartBinding
import com.wyh.happyyousdk.happyMarket.adapter.HappyMartListAdapter
import com.wyh.happyyousdk.happyMarket.adapter.HappyMartListAdapter.HappyMartClick
import com.wyh.happyyousdk.happyMarket.adapter.MyPurchasesAdapter
import com.wyh.happyyousdk.model.MyOrderResponse
import com.wyh.happyyousdk.model.PopUpShowModel
import com.wyh.happyyousdk.model.request.HappyMartSearchData
import com.wyh.happyyousdk.model.request.SearchRequest
import com.wyh.happyyousdk.model.request.SearchResponse
import com.wyh.happyyousdk.model.request.login.RefreshTokenRequest
import com.wyh.happyyousdk.model.response.GetHappyMartTiles
import com.wyh.happyyousdk.model.response.HappyMartTilesData
import com.wyh.happyyousdk.model.response.login.RefreshTokenResponse
import com.wyh.happyyousdk.network.ApiClientWyh
import com.wyh.happyyousdk.network.ApiInterfaceWyh
import com.wyh.happyyousdk.utils.Analytics
import com.wyh.happyyousdk.utils.CommonUtils
import com.wyh.happyyousdk.utils.Constants
import com.wyh.happyyousdk.utils.Master
import com.wyh.happyyousdk.utils.SharedPref
import com.wyhsdk.sharedPreferences.SharedPreference
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Locale

class NewHappyMartActivity : AppCompatActivity(), MyPurchasesAdapter.ClickListenerInterface, HappyMartClick {

    lateinit var binding : ActivityNewHappyMartBinding
    var happyMartList : ArrayList<HappyMartTilesData> = arrayListOf()
    lateinit var happyMartListAdapter : HappyMartListAdapter
    var myOrderDataList: List<MyOrderResponse.Data>? = emptyList()
    var myPurchasesList: ArrayList<String>? = arrayListOf()
    var purchaseArray = java.util.ArrayList<String>()
    var searchList : ArrayList<HappyMartTilesData> = arrayListOf()
    var newPurchaseList: ArrayList<String> = ArrayList()

    lateinit var myPurchasesAdapter: MyPurchasesAdapter
    var positionCurrentActivities = 0
    var totalPoints = 0
    lateinit var context: Context



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = this

        binding = DataBindingUtil.setContentView(this,R.layout.activity_new_happy_mart)
        setContentView(binding.root)
        SharedPref.init(this)
        binding.includeToolbar.tvBack.text = "Happy Mart"
        binding.includeToolbar.tvBack.setTextColor(resources.getColor(R.color.white))
        binding.includeToolbar.ivBack.setColorFilter(resources.getColor(R.color.white))
        binding.includeToolbar.ivMenu.visibility = View.VISIBLE
        binding.includeToolbar.ivMenu.setColorFilter(resources.getColor(R.color.white))
        totalPoints = intent.getIntExtra("points", 0)

        binding.includeToolbar.ivMenu.setOnClickListener {
            showPopup(binding.includeToolbar.ivMenu)
        }

        binding.includeToolbar.llBack.setOnClickListener {
            finish()
        }

        binding.ivHome.setOnClickListener {
            val intent = Intent(this, NewDashboardActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }

        binding.happyMartNewPurchase.setOnClickListener {
            val intent = Intent(context, MoreMyPurchasesActivity::class.java)
            startActivity(intent)
        }
        //requestCallPermission()
        getMyOrder()

        val customObj = JSONObject()
        try {
            customObj.put("PAGE_ID", "HappyMartDashboard")
        } catch (e: JSONException) {
            throw RuntimeException(e)
        }
        


        binding.happyMartSearchImg.setOnClickListener {
            if (binding.happyMartSearchLayout.visibility === View.VISIBLE) {
                binding.happyMartMyRecycler.visibility = View.VISIBLE
                binding.happyMartNoResultTv.visibility = View.GONE
                happyMartListAdapter = HappyMartListAdapter(this,happyMartList , this);
                binding.happyMartMyRecycler.adapter = happyMartListAdapter
                binding.happyMartMyRecycler.setItemViewCacheSize(12)
                myPurchasesAdapter = MyPurchasesAdapter(this, myPurchasesList, this@NewHappyMartActivity)
                binding.happyMartMyPurchasesRecycler.adapter = myPurchasesAdapter
                binding.happyMartSearchEt.setText("")
                binding.happyMartSearchEt.isEnabled = false
                binding.happyMartSearchLayout.hide()
            } else {
                binding.happyMartSearchEt.isEnabled = true
                binding.happyMartSearchLayout.show()
            }
            //getSearchResult(binding.happyMartSearchEt.text.toString())
        }

        binding.searchCancel.setOnClickListener {
            binding.happyMartSearchEt.setText("")
        }

        binding.happyMartSearchEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(p0!!.isEmpty()){
                    binding.happyMartMyRecycler.visibility = View.VISIBLE
                    binding.searchCancel.visibility = View.GONE
                    binding.happyMartNoResultTv.visibility = View.GONE
                    happyMartListAdapter = HappyMartListAdapter(this@NewHappyMartActivity,happyMartList,this@NewHappyMartActivity);
                    binding.happyMartMyRecycler.adapter = happyMartListAdapter
                    binding.happyMartMyRecycler.setItemViewCacheSize(12)
                    if(myOrderDataList!!.isEmpty()){
                        binding.happyMartNewPurchase.visibility = View.GONE
                    }else {
                        binding.happyMartNewPurchase.visibility = View.VISIBLE

                    }
                    myPurchasesAdapter = MyPurchasesAdapter(this@NewHappyMartActivity, myPurchasesList, this@NewHappyMartActivity)
                    binding.happyMartNoResultTv.visibility = View.GONE
                    binding.happyMartMyPurchasesRecycler.visibility = View.VISIBLE
                    binding.happyMartMyPurchasesIndicator.visibility = View.VISIBLE
                    binding.happyMartMyPurchasesRecycler.adapter = myPurchasesAdapter
                }else{
                    binding.searchCancel.visibility = View.VISIBLE
                    searchList.clear()
                    newPurchaseList.clear()

                    if (binding.happyMartNewPurchase.visibility === View.VISIBLE) {
                        for (j in myPurchasesList!!.indices) {
                            if (purchaseArray[j].lowercase(Locale.getDefault()).contains(
                                    p0.toString().lowercase(Locale.getDefault()))) {
                                newPurchaseList.add(myPurchasesList!![j])

                            }
                        }
                    }
                    if (newPurchaseList.size == 0) {
                        binding.happyMartNoResultTv.visibility = View.VISIBLE
                        binding.happyMartMyPurchasesRecycler.visibility = View.GONE
                        binding.happyMartNewPurchase.visibility = View.GONE
                        binding.happyMartMyPurchasesIndicator.visibility = View.GONE
                    } else {
                        binding.happyMartNoResultTv.visibility = View.GONE
                        binding.happyMartNewPurchase.visibility = View.VISIBLE
                        binding.happyMartMyPurchasesRecycler.visibility = View.VISIBLE
                        binding.happyMartMyPurchasesIndicator.visibility = View.VISIBLE
                        myPurchasesAdapter = MyPurchasesAdapter(this@NewHappyMartActivity, newPurchaseList, this@NewHappyMartActivity)
                        binding.happyMartMyPurchasesRecycler.adapter = myPurchasesAdapter
                    }
                    getSearchResult(binding.happyMartSearchEt.text.toString())
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })
    }

    private fun requestCallPermission() {
        try {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.CALL_PHONE
                ), 1
            )
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
        Log.d("AuthToken","onPaused")
    }


    private fun getMyOrder() {
        Log.d("AuthToken","API Called")
        CommonUtils.showProgressDialige(this)
        val apiInterfaceWyh = ApiClientWyh.getClient(CommonUtils.getBaseUrlForAPI(this)).create<ApiInterfaceWyh>(ApiInterfaceWyh::class.java)
        val call: Call<MyOrderResponse> = apiInterfaceWyh.getAllOrders(SharedPref.getAuthToken())
        call.enqueue(object : Callback<MyOrderResponse?> {
            override fun onResponse(call: Call<MyOrderResponse?>, response: Response<MyOrderResponse?>) {
                CommonUtils.dismissDialoge()
                if (response.code() == 200 && response.body() != null && response.body()!!.success && response.code() == 200) {
                    Analytics.logEvent(this@NewHappyMartActivity, this@NewHappyMartActivity.javaClass.getName(), getString(R.string.get_all_orders_success))
                    getHappyMartTiles()
                    Log.d("response", Gson().toJson(response.body()))
                    binding.happyMartMyPurchasesRecycler.visibility = View.VISIBLE
                    myOrderDataList = response.body()!!.data
                    if ((myOrderDataList as MutableList<MyOrderResponse.Data>?)!!.size > 0) {
                        for (data in (myOrderDataList as MutableList<MyOrderResponse.Data>?)!!) {
                            if (!myPurchasesList!!.contains(data.vendor)) {
                                myPurchasesList!!.add(data.vendor)
                            }
                        }
                        binding.happyMartNewPurchase.visibility = View.VISIBLE
                        binding.happyMartMyPurchasesRecycler.visibility = View.VISIBLE
                        getOriginalValues()
                        setActivitiesAdapter()
                    } else {
                        binding.happyMartNewPurchase.visibility = View.GONE
                        binding.happyMartMyPurchasesRecycler.visibility = View.GONE
                    }
                    if (response.body()!!.enGTokens != null && response.body()!!.enGTokens.tokens != null) {
                        NewDashboardHelper.popUpShowModels.add(PopUpShowModel(Constants.TokenStamp, response.body()!!.enGTokens.tokens))
                    }
                    if (response.body()!!.enGTokens != null && response.body()!!.enGTokens.bonusTokens != null) {
                        NewDashboardHelper.popUpShowModels.add(
                            PopUpShowModel(Constants.TokenStampBounce, response.body()!!.enGTokens.bonusTokens))
                    }
                    if (response.body()!!.feedbackDetails != null && response.body()!!.feedbackDetails.feedbackModel != null && response.body()!!
                            .feedbackDetails.starConfig != null) {
                        NewDashboardHelper.popUpShowModels.add(PopUpShowModel(Constants.FeedbackPOPUP, ""))
                        NewDashboardHelper.feedbackResponseData = response.body()!!.feedbackDetails
                    }
                    HappyMartActivity().showRewardsPopupDialogBox(this@NewHappyMartActivity)
                }else if(response.code() == 401){
                    refreshAuthToken()
                }else {
                    binding.happyMartNewPurchase.visibility = View.GONE
                    getHappyMartTiles()
                    Analytics.logEvent(context, "", "A_107_${response.code()}_${SharedPref.getEncryptedMobileNo()}")

                }
            }

            override fun onFailure(call: Call<MyOrderResponse?>, t: Throwable) {
                CommonUtils.dismissDialoge()
                binding.happyMartNewPurchase.visibility = View.GONE
                getHappyMartTiles()
                Analytics.logEvent(context, "", "A_107_Failed_${SharedPref.getEncryptedMobileNo()}")
            }
        })
    }


    private fun refreshAuthToken() {
        CommonUtils.showProgressDialige(context)
        val deviceModel = Build.BRAND + " " + Build.MODEL
        val osVersion = Build.VERSION.RELEASE + "(" + Build.VERSION.SDK_INT + ")"
        val appVersion = SDKConstants.appVersionName
        val request = RefreshTokenRequest(deviceModel, osVersion, appVersion)
        val apiInterfaceWyh = ApiClientWyh.getClient(CommonUtils.getBaseUrlForAPI(context)).create(ApiInterfaceWyh::class.java)
        val call = apiInterfaceWyh.refreshToken(SharedPref.getAuthToken(), request)
        Log.d("AuthToken", SharedPref.getAuthToken() + "URL: " + call.request().url())
        call.enqueue(object : Callback<RefreshTokenResponse?> {
            override fun onResponse(call: Call<RefreshTokenResponse?>, response: Response<RefreshTokenResponse?>) {
                CommonUtils.dismissDialoge()
                Log.d("AuthToken", "Refresh Res: " + Gson().toJson(response.body()))
                if (response.code() == 200 && response.body() != null && response.body()!!.isSuccess && response.body()!!.data.authToken != null && response.body()!!.data.authToken != ""
                ) {
                    Analytics.logEvent(context, context.javaClass.name, getString(R.string.refresh_token_success))
                    SharedPref.putAuthToken("Bearer " + response.body()!!.data.authToken)
                    SharedPreference.putAuthToken("Bearer " + response.body()!!.data.authToken)

                    Log.d("call", "refreshAuthToken")
                } else {
                    Analytics.logEvent(context, context.javaClass.name, getString(R.string.refresh_token_failed))
                    /*Toast.makeText(context, resources.getString(R.string.session_time_out), Toast.LENGTH_SHORT).show()
                    val intent = Intent(context, MobileNumberActivity::class.java)
                    startActivity(intent)
                    SharedPref.clearSharedPref()
                    finishAffinity()*/
                    Master.logOut(context)
                }
            }

            override fun onFailure(call: Call<RefreshTokenResponse?>, t: Throwable) {
                CommonUtils.dismissDialoge()
                Analytics.logEvent(context, context.javaClass.name, getString(R.string.refresh_token_failed))
                /*Toast.makeText(context, resources.getString(R.string.session_time_out), Toast.LENGTH_SHORT).show()
                val intent = Intent(context, MobileNumberActivity::class.java)
                startActivity(intent)
                SharedPref.clearSharedPref()
                finishAffinity()*/
                Master.logOut(context)
            }
        })
    }


    private fun showPopup(v: View) {
        val popup = PopupMenu(this, v)
        val inflater = popup.menuInflater
        inflater.inflate(R.menu.menu_happy_mart, popup.menu)
        popup.show()
        popup.setOnMenuItemClickListener { menuItem: MenuItem ->
            if (menuItem.itemId == R.id.menu_my_order) {
                val intent = Intent(this, MyOrderActivity::class.java)
                intent.putExtra("vendor", "")
                startActivity(intent)
            }
            false
        }
    }

    fun getHappyMartTiles(){
        try{
            Log.d("AuthToken","HappyMartTiles")
            CommonUtils.showProgressDialige(this)
            val apiInertaface = RetrofitHandler.apiInterface()
            apiInertaface.getHappyMartTiles(SharedPref.getAuthToken()).enqueue(object : Callback<GetHappyMartTiles>{
                override fun onResponse(call: Call<GetHappyMartTiles>, response: Response<GetHappyMartTiles>) {
                    CommonUtils.dismissDialoge()
                    if(response.isSuccessful && response.body() != null){
                        if(response.body()!!.data.size != 0){
                            happyMartList = response.body()!!.data
                            binding.happyMartMyRecycler.hasFixedSize()
                            binding.happyMartMyRecycler.layoutManager = GridLayoutManager(this@NewHappyMartActivity,3)
                            happyMartListAdapter = HappyMartListAdapter(this@NewHappyMartActivity,happyMartList,this@NewHappyMartActivity)
                            binding.happyMartMyRecycler.adapter = happyMartListAdapter
                            binding.happyMartMyRecycler.setItemViewCacheSize(12)
                        }
                    }
                }

                override fun onFailure(call: Call<GetHappyMartTiles>, t: Throwable) {
                    CommonUtils.dismissDialoge()

                }

            })
        }catch (e: Exception){
            CommonUtils.dismissDialoge()
            e.toString()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    private fun getOriginalValues() {
        try {
            for (i in myPurchasesList!!.indices) {
                if (myPurchasesList!![i].replace(" ", "").equals("medpayopd", ignoreCase = true)) {
                    purchaseArray.add("Physical Consultation")
                } else if (myPurchasesList!![i].replace(" ", "")
                        .equals("pharmeasy", ignoreCase = true)
                ) {
                    purchaseArray.add("Pharmacy")
                } else if (myPurchasesList!![i].replace(" ", "").equals(
                        "healthassure",
                        ignoreCase = true
                    ) || myPurchasesList!![i].replace(" ", "")
                        .equals("connectandheal", ignoreCase = true)
                ) {
                    purchaseArray.add("Diagnostics and LAB Tests")
                } else if (myPurchasesList!![i].replace(" ", "")
                        .equals("coachpro", ignoreCase = true)
                ) {
                    purchaseArray.add("Fitness n Nutrition")
                } else if (myPurchasesList!![i].replace(" ", "")
                        .equals("mentalWellbeing", ignoreCase = true)
                ) {
                    purchaseArray.add("Mental Wellbeing")
                } else if (myPurchasesList!![i].replace(" ", "")
                        .equals("hobbytribe", ignoreCase = true)
                ) {
                    purchaseArray.add("Hobby")
                } else if (myPurchasesList!![i].replace(" ", "")
                        .equals("actofit", ignoreCase = true)
                ) {
                    purchaseArray.add("Devices and Electronics")
                } else if (myPurchasesList!![i].replace(" ", "")
                        .equals("vouchers", ignoreCase = true)
                ) {
                    purchaseArray.add("Others")
                } else if (myPurchasesList!![i].replace(" ", "")
                        .equals("medpaypharmacy", ignoreCase = true)
                ) {
                    purchaseArray.add("Medpay Pharmacy")
                } else if (myPurchasesList!![i].replace(" ", "")
                        .equals("GetVisit", ignoreCase = true)
                ) {
                    purchaseArray.add("Tele-Consultation")
                } else {
                    purchaseArray.add("Others")
                }
            }
        } catch (e: java.lang.Exception) {
        }
    }

    private fun getSearchResult(searchKey: String){
        try{
            val request = SearchRequest(searchKey)
            val apiInterfaceWyh = ApiClientWyh.getClient(CommonUtils.getBaseUrlForAPI(this)).create<ApiInterfaceWyh>(ApiInterfaceWyh::class.java)
            apiInterfaceWyh.dashboardSearch(SharedPref.getAuthToken(),request).enqueue(object : Callback<SearchResponse>{
                override fun onResponse(call: Call<SearchResponse>, response: Response<SearchResponse>) {
                    if(response.body() != null && response.isSuccessful){
                        if(response.body()!!.data.happyMartSearchData.isNotEmpty()){
                            binding.happyMartMyRecycler.visibility = View.VISIBLE
                            binding.happyMartNoResultTv.visibility = View.GONE
                            searchData(response.body()!!.data.happyMartSearchData)
                        }else{
                            binding.happyMartMyRecycler.visibility = View.GONE
                            binding.happyMartNoResultTv.visibility = View.VISIBLE
                        }
                    }
                }

                override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                }

            })
        }catch (e: Exception){
            e.toString()
        }
    }

    private fun searchData(searchListData : List<HappyMartSearchData>){
        try{
            searchListData.forEach{ searchData ->
                happyMartList.forEach {happyMartData ->
                    try{
                        Log.d("AuthToken","search${happyMartData.CategoryName}")
                           if(searchData.result.equals(happyMartData.CategoryName,true)){
                               if(!searchList.contains(happyMartData))
                                   searchList.add(happyMartData)
                        }
                        binding.happyMartMyRecycler.hasFixedSize()
                        binding.happyMartMyRecycler.layoutManager = GridLayoutManager(this@NewHappyMartActivity,3)
                        happyMartListAdapter = HappyMartListAdapter(this@NewHappyMartActivity,searchList,this@NewHappyMartActivity)
                        binding.happyMartMyRecycler.adapter = happyMartListAdapter
                        binding.happyMartMyRecycler.setItemViewCacheSize(12)

                    }catch (e: Exception){
                        e.toString()
                    }

                }
            }


        }catch (e: Exception){
            e.toString()
        }
    }

    private fun setActivitiesAdapter() {
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = RecyclerView.HORIZONTAL
        myPurchasesAdapter = MyPurchasesAdapter(this, myPurchasesList, this)
        binding.happyMartMyPurchasesRecycler.adapter = myPurchasesAdapter
        binding.happyMartMyPurchasesRecycler.layoutManager = linearLayoutManager
        if (myPurchasesList!!.size > 3) {
            binding.happyMartMyPurchasesIndicator.visibility = View.VISIBLE
            val indicatorSize =
                Math.ceil(myPurchasesList!!.size.toString().toDouble() / 3.0).toInt()
            val linearLayoutManager1 = LinearLayoutManager(this)
            linearLayoutManager1.orientation = RecyclerView.HORIZONTAL
            val indicatorsAdapter = IndicatorsAdapter(this, indicatorSize, 0)
            binding.happyMartMyPurchasesIndicator.setAdapter(indicatorsAdapter)
            binding.happyMartMyPurchasesIndicator.setLayoutManager(linearLayoutManager1)
            binding.happyMartMyPurchasesIndicator.setHasFixedSize(true)
            binding.happyMartMyPurchasesRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        if (linearLayoutManager.findFirstCompletelyVisibleItemPosition() != -1) {
                            positionCurrentActivities =
                                linearLayoutManager.findFirstCompletelyVisibleItemPosition()
                        } else positionCurrentActivities =
                            linearLayoutManager.findFirstVisibleItemPosition()
                        indicatorsAdapter.updateSelectedIndex(positionCurrentActivities)
                    }
                }
            })
        } else {
            binding.happyMartMyPurchasesIndicator.visibility = View.GONE
        }
    }

    override fun onItemClick(vendor: String?) {
        val intent = Intent(this, MyOrderActivity::class.java)
        intent.putExtra("vendor", vendor)
        startActivity(intent)
    }

    override fun onClick(happyMartTitle: HappyMartTilesData) {
        startActivity(Intent(this@NewHappyMartActivity,HappyMartCategory::class.java)
                .putExtra("category",happyMartTitle.CategoryName)
                .putExtra("id",happyMartTitle.Id.toString())
                .putExtra("comingFrom","HappyMart"))
       // finish()
    }


}