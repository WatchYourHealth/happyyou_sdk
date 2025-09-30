package com.wyh.happyyousdk.dashboard

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.wyh.happyyousdk.APIEncryption.RetrofitHandler
import com.wyh.happyyousdk.NeedSupportActivity
import com.wyh.happyyousdk.R
import com.wyh.happyyousdk.SDKConstants
import com.wyh.happyyousdk.WellBeingDisclaimerActivity
import com.wyh.happyyousdk.absorb.HealthHacksActivity
import com.wyh.happyyousdk.absorb.adapter.HealthHacksHealthTvAdapter
import com.wyh.happyyousdk.absorb.adapter.HealthHacksQuickReadsAdapter
import com.wyh.happyyousdk.addFamily.AddFamilyActivity
import com.wyh.happyyousdk.common.adapter.IndicatorsAdapter
import com.wyh.happyyousdk.dashboard.adapter.LifestyleHorizontalAdapter
import com.wyh.happyyousdk.dashboard.helper.NewDashboardHelper
import com.wyh.happyyousdk.dass21.Dass21AnalysisActivity
import com.wyh.happyyousdk.dass21.Dass21QuestionsActivity
import com.wyh.happyyousdk.databinding.ActivitySearchBinding
import com.wyh.happyyousdk.databinding.ShareOptionPopUpBinding
import com.wyh.happyyousdk.ehr.AddNewEhrRecord
import com.wyh.happyyousdk.ehr.EhrActivity
import com.wyh.happyyousdk.ehr.HealthLockerPhotosActivity
import com.wyh.happyyousdk.ehr.HealthRecordTypeWiseReportActivity
import com.wyh.happyyousdk.happyMarket.HappyMartCategory
import com.wyh.happyyousdk.happyMarket.HappyMartDisclaimerActivity
import com.wyh.happyyousdk.happyMarket.Model.HappyMartListModel
import com.wyh.happyyousdk.happyMarket.NewHappyMartActivity
import com.wyh.happyyousdk.hra.HRAAnalysisActivity
import com.wyh.happyyousdk.hra.HRAQuestionsActivity
import com.wyh.happyyousdk.ice.ICEDashboardActivity
import com.wyh.happyyousdk.model.CommonSuccessResponse
import com.wyh.happyyousdk.model.request.CommunityIDs
import com.wyh.happyyousdk.model.request.FacnScanInTribeRequest
import com.wyh.happyyousdk.model.request.HappyMartSearchData
import com.wyh.happyyousdk.model.request.IntegrationIdRequest
import com.wyh.happyyousdk.model.request.LifestyleSearchData
import com.wyh.happyyousdk.model.request.SearchRequest
import com.wyh.happyyousdk.model.request.SearchResponse
import com.wyh.happyyousdk.model.request.absorb.AddBookmarkRequest
import com.wyh.happyyousdk.model.request.absorb.VideoBookmarkRequest
import com.wyh.happyyousdk.model.request.hra.GetAnalysisRequest
import com.wyh.happyyousdk.model.response.FaceScanInTribeResponse
import com.wyh.happyyousdk.model.response.absorb.AddBookmarkResponse
import com.wyh.happyyousdk.model.response.absorb.GetDashboardDataResponse
import com.wyh.happyyousdk.model.response.absorb.GetDashboardDataResponse.Data.HealthTv
import com.wyh.happyyousdk.model.response.absorb.GetDashboardDataResponse.Data.QucikRead
import com.wyh.happyyousdk.model.response.dass21.DassAnalysisResponse
import com.wyh.happyyousdk.model.response.ehr.HealthRecordTypeResponse
import com.wyh.happyyousdk.model.response.getAnalysis.GetAnalysisResponse
import com.wyh.happyyousdk.network.ApiClientWyh
import com.wyh.happyyousdk.network.ApiInterfaceWyh
import com.wyh.happyyousdk.notification.NotificationDrawerActivity
import com.wyh.happyyousdk.policyDetails.PolicyDetailsActivity
import com.wyh.happyyousdk.profile.ProfileActivity
import com.wyh.happyyousdk.syncDevice.ConnectApp
import com.wyh.happyyousdk.syncDevice.SyncDeviceActivity
import com.wyh.happyyousdk.trends.TrendsActivity
import com.wyh.happyyousdk.unwind.UnwindActivity
import com.wyh.happyyousdk.utils.Analytics
import com.wyh.happyyousdk.utils.CommonUtils
import com.wyh.happyyousdk.utils.Constants
import com.wyh.happyyousdk.utils.Constants.SearchKey
import com.wyh.happyyousdk.utils.SharedPref
import com.wyh.happyyousdk.utils.SnapHelperOneByOne
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar
import kotlin.math.ceil

class SearchActivity : AppCompatActivity(){

    private lateinit var binding: ActivitySearchBinding
    private lateinit var lifestyleAdapter: LifestyleHorizontalAdapter
    private lateinit var wellBeingAdapter: LifestyleHorizontalAdapter
    private lateinit var myZoneAdapter: LifestyleHorizontalAdapter
    private lateinit var serviceAdapter: LifestyleHorizontalAdapter
    private lateinit var othersAdapter: LifestyleHorizontalAdapter
    private lateinit var activeZoneAdapter: LifestyleHorizontalAdapter
    private lateinit var apiInterfaceWyh: ApiInterfaceWyh
    private lateinit var context: Context
    private var searchKey: String = ""
    private var lifeStyleListModel = ArrayList<HappyMartListModel>()
    private var happyMartListModel = ArrayList<HappyMartListModel>()
    private var wellBeingList = ArrayList<HappyMartListModel>()
    private var myZoneList = ArrayList<HappyMartListModel>()
    private var serviceList = ArrayList<HappyMartListModel>()
    private var activeZoneList = ArrayList<HappyMartListModel>()
    private var othersList = ArrayList<HappyMartListModel>()
    var positionHealthTv: Int = 0
    var positionReads: Int = 0
    var positionLifeStyle: Int = 0
    var positionHappyMart: Int = 0
    var getAnalysisResponse: GetAnalysisResponse? = null
    var dassAnalysisResponse: DassAnalysisResponse? = null
    lateinit var healthRecordTypeResponse: HealthRecordTypeResponse
    var diagnosticId = 0
    var hospitalId = 0
    var drPrescriptionId = 0
    var dietPlanId = 0
    var fitnessPlusId = 0
    var vaccineCertificateId = 0
    var myPhotosId = 0
    var otherDocsId = 0
    var faceScanID = 0
    var list = java.util.ArrayList<CommunityIDs>()
    var tribeListId: ArrayList<Int> = java.util.ArrayList()
    lateinit var alertDialog: AlertDialog
    val mainStartCalender: Calendar = Calendar.getInstance()
    var gender = ""
    var user = "self"
    var otherGender = ""
    private val FACE_SCAN_REQUEST_CODE = 20
    var dobStr: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search)

        context = this

        apiInterfaceWyh =
            ApiClientWyh.getClient(CommonUtils.getBaseUrlForAPI(context)).create<ApiInterfaceWyh>(
                ApiInterfaceWyh::class.java
            )

        binding.la404Bear.setAnimationFromUrl(CommonUtils.getBaseUrlForAPI(context) + SDKConstants.endPointForImages + "anim_bear_404.json")

        binding.includeToolbar.llBack.setOnClickListener { finish() }
        //binding.includeToolbar.tvBack.text = getString(R.string.search)
        binding.includeToolbar.tvBack.setTextColor(getColor(R.color.white))
        binding.includeToolbar.ivMenu.setColorFilter(getColor(R.color.white))
        binding.includeToolbar.ivBack.setColorFilter(getColor(R.color.white))


        binding.ivHome.setOnClickListener {
            val intent = Intent(this, NewDashboardActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }


        searchKey = intent.getStringExtra(SearchKey) ?: ""

        Log.d("searchKey", "" + searchKey)

        searchCall()

        val linearSnapHelper: LinearSnapHelper = SnapHelperOneByOne()
        linearSnapHelper.attachToRecyclerView(binding.lifeStyleRecyler)

        val linearSnapHelper1: LinearSnapHelper = SnapHelperOneByOne()
        linearSnapHelper1.attachToRecyclerView(binding.happyMartRecyler)

        val linearSnapHelper2: LinearSnapHelper = SnapHelperOneByOne()
        linearSnapHelper2.attachToRecyclerView(binding.rvQuickReads)

        val linearSnapHelper3: LinearSnapHelper = SnapHelperOneByOne()
        linearSnapHelper3.attachToRecyclerView(binding.rvHealthTv)

        if (searchKey.isNotEmpty()) {
            binding.searchEt.setText(searchKey)
            binding.searchEt.isEnabled = true
            binding.searchBarLayout.show()
        }

        binding.searchImg.setOnClickListener {
            searchItem()
        }

        binding.searchEt.setOnEditorActionListener { _, _, _ ->
            searchItem()
            false
        }

        binding.searchEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(p0: Editable?) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
        })
    }

    private fun searchCall() {
        binding.pBar.visibility = View.VISIBLE
        binding.scrollView.visibility = View.GONE
        val request: SearchRequest = SearchRequest(searchKey = searchKey)
        val call = apiInterfaceWyh.dashboardSearch(SharedPref.getAuthToken(), request)
        Log.d("search url", Gson().toJson(call.request().url()))
        Log.d("search request", Gson().toJson(request))
        call.enqueue(object : Callback<SearchResponse> { override fun onResponse(call: Call<SearchResponse>, response: Response<SearchResponse>) {
                binding.pBar.visibility = View.GONE
                binding.scrollView.visibility = View.VISIBLE
                var videos: List<HealthTv> = emptyList()
                var blogs: List<GetDashboardDataResponse.Data.QucikRead> = emptyList()
                var hm: List<HappyMartSearchData> = emptyList()
                var ls: List<LifestyleSearchData> = emptyList()
                var myZone: List<HappyMartSearchData> = emptyList()
                var wellBeingList: List<HappyMartSearchData> = emptyList()
                var servicesList: List<HappyMartSearchData> = emptyList()
                var othersList: List<HappyMartSearchData> = emptyList()
                var activeZone: List<HappyMartSearchData> = emptyList()
                var isData: Boolean = false


                Log.d("search response", Gson().toJson(response.body()))
                if (response.code() == 200 && response.body() != null) {
                    videos = response.body()!!.data.videos
                    blogs = response.body()!!.data.blogs
                    hm = response.body()!!.data.happyMartSearchData
                    ls = response.body()!!.data.lifestyleSearchData
                    myZone = response.body()!!.data.myZone
                    wellBeingList = response.body()!!.data.wellBeing
                    servicesList = response.body()!!.data.servicesData
                    othersList = response.body()!!.data.othersData
                    activeZone = response.body()!!.data.activeZoneData


                    if (videos.isNotEmpty()) {
                        isData = true
                        binding.llHealthTv.visibility = View.VISIBLE
                        setHealthTvData(videos)
                    } else {
                        binding.llHealthTv.visibility = View.GONE
                    }

                    if (blogs.isNotEmpty()) {
                        isData = true
                        binding.quickReadLayout.visibility = View.VISIBLE
                        setQuickReadData(blogs)
                    } else {
                        binding.quickReadLayout.visibility = View.GONE
                    }

                    if (hm.isNotEmpty()) {
                        isData = true
                        binding.llHappyMart.visibility = View.VISIBLE
                        setUpHappyMart(hm)
                    } else {
                        binding.llHappyMart.visibility = View.GONE
                    }

                    if (ls.isNotEmpty()) {
                        isData = true
                        binding.llLifeStyle.visibility = View.VISIBLE
                        setUpLifeStyle(ls)
                    } else {
                        binding.llLifeStyle.visibility = View.GONE
                    }
                    if (activeZone.isNotEmpty()) {
                        isData = true
                        binding.llSearchActiveZone.visibility = View.VISIBLE
                        setActiveZone(activeZone)
                    } else {
                        binding.llSearchActiveZone.visibility = View.GONE
                    }
                    if (myZone.isNotEmpty()) {
                        isData = true
                        binding.llSearchMyZone.visibility = View.VISIBLE
                        setMyZone(myZone)
                    } else {
                        binding.llSearchMyZone.visibility = View.GONE
                    }
                    if (wellBeingList.isNotEmpty()) {
                        isData = true
                        binding.llSearchMyWellBeing.visibility = View.VISIBLE
                        setWellBeing(wellBeingList)
                    } else {
                        binding.llSearchMyWellBeing.visibility = View.GONE
                    }
                    if (servicesList.isNotEmpty()) {
                        isData = true
                        binding.llSearchService.visibility = View.VISIBLE
                        setServices(servicesList)
                    } else {
                        binding.llSearchService.visibility = View.GONE
                    }
                    if (othersList.isNotEmpty()) {
                        isData = true
                        binding.llSearchOthers.visibility = View.VISIBLE
                        setOtherData(othersList)
                    } else {
                        binding.llSearchOthers.visibility = View.GONE
                    }


                    if (isData) {
                        binding.scrollView.visibility = View.VISIBLE
                        binding.llNoRecordFound.visibility = View.GONE
                    } else {
                        binding.llNoRecordFound.visibility = View.VISIBLE
                        binding.pBar.visibility = View.GONE
                        binding.scrollView.visibility = View.GONE
                    }

                } else {
                    binding.llNoRecordFound.visibility = View.VISIBLE
                    binding.pBar.visibility = View.GONE
                    binding.scrollView.visibility = View.GONE
                }

            }

            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                binding.pBar.visibility = View.GONE
                binding.scrollView.visibility = View.GONE
                binding.llNoRecordFound.visibility = View.VISIBLE
                Toast.makeText(
                    context,
                    resources.getString(R.string.error_string),
                    Toast.LENGTH_SHORT
                ).show()
            }

        })
    }

    private fun setHealthTvData(healthTvList: List<HealthTv>) {
        if (healthTvList.size > 9) {
            binding.tvMore.visibility = View.VISIBLE
        } else {
            binding.tvMore.visibility = View.GONE
        }

        binding.qrMore.setOnClickListener {
            val intent = Intent(context, HealthHacksQuickReadsAdapter::class.java)
            intent.putExtra(SearchKey, searchKey)
            startActivity(intent)
        }

        val absorbHealthTvAdapter = HealthHacksHealthTvAdapter(
            context, healthTvList
        ) { id, isBookmark ->
            addVideoBookmark(isBookmark, id)
        }

        val absorbHealthTvLinearLayoutManager = LinearLayoutManager(context)
        absorbHealthTvLinearLayoutManager.orientation = RecyclerView.HORIZONTAL
        binding.rvHealthTv.layoutManager = absorbHealthTvLinearLayoutManager
        binding.rvHealthTv.adapter = absorbHealthTvAdapter
        binding.rvHealthTv.onFlingListener = null
        val indicatorSize: Int = if (healthTvList.size > 9) {
            ceil(9.0 / 2.0).toInt()
        } else {
            ceil(healthTvList.size.toString().toDouble() / 2.0).toInt()
        }
        if (indicatorSize > 1) {
            binding.rvHealthTvIndicator.visibility = View.VISIBLE
        } else {
            binding.rvHealthTvIndicator.visibility = View.GONE
        }
        val absorbHealthTvLinearLayoutManager1 = LinearLayoutManager(context)
        absorbHealthTvLinearLayoutManager1.orientation = RecyclerView.HORIZONTAL
        val absorbHealthTvIndicatorsAdapter = IndicatorsAdapter(context, indicatorSize, 0)
        binding.rvHealthTvIndicator.adapter = absorbHealthTvIndicatorsAdapter
        binding.rvHealthTvIndicator.layoutManager = absorbHealthTvLinearLayoutManager1
        //noinspection InvalidSetHasFixedSize
        binding.rvHealthTvIndicator.setHasFixedSize(true)
        binding.rvHealthTv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    positionHealthTv =
                        if (absorbHealthTvLinearLayoutManager.findFirstCompletelyVisibleItemPosition() != -1) {
                            absorbHealthTvLinearLayoutManager.findFirstCompletelyVisibleItemPosition()
                        } else absorbHealthTvLinearLayoutManager.findFirstVisibleItemPosition()
                    absorbHealthTvIndicatorsAdapter.updateSelectedIndex(positionHealthTv)
                }
            }
        })
    }


    private fun setQuickReadData(quickReadList: List<QucikRead>) {

        if (quickReadList.size > 9) {
            binding.qrMore.visibility = View.VISIBLE
        } else {
            binding.qrMore.visibility = View.GONE
        }

        binding.qrMore.setOnClickListener {
            val intent = Intent(context, HealthTv::class.java)
            intent.putExtra(SearchKey, searchKey)
            startActivity(intent)
        }

        val quickReadLinearLayoutManager = LinearLayoutManager(context)
        val absorbQuickReadsAdapter = HealthHacksQuickReadsAdapter(
            context, quickReadList
        ) { articleCode, isBookmark ->
            addBookmark(articleCode, isBookmark)
        }
        quickReadLinearLayoutManager.orientation = RecyclerView.HORIZONTAL
        binding.rvQuickReads.layoutManager = quickReadLinearLayoutManager
        binding.rvQuickReads.adapter = absorbQuickReadsAdapter
        binding.rvQuickReads.onFlingListener = null
        val indicatorSize = if (quickReadList.size > 9) {
            ceil(9.0 / 2.0).toInt()
        } else {
            ceil(quickReadList.size.toString().toDouble() / 2.0).toInt()
        }
        if (indicatorSize > 1) {
            binding.rvQuickReadsIndicator.visibility = View.VISIBLE
        } else {
            binding.rvQuickReadsIndicator.visibility = View.GONE
        }

        val quickReadLinearLayoutManager1 = LinearLayoutManager(context)
        quickReadLinearLayoutManager1.orientation = RecyclerView.HORIZONTAL
        val quickReadIndicatorsAdapter = IndicatorsAdapter(context, indicatorSize, 0)
        binding.rvQuickReadsIndicator.adapter = quickReadIndicatorsAdapter
        binding.rvQuickReadsIndicator.layoutManager = quickReadLinearLayoutManager1
        //noinspection InvalidSetHasFixedSize
        binding.rvQuickReadsIndicator.setHasFixedSize(true)
        binding.rvQuickReads.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    positionReads =
                        if (quickReadLinearLayoutManager.findFirstCompletelyVisibleItemPosition() != -1) {
                            quickReadLinearLayoutManager.findFirstCompletelyVisibleItemPosition()
                        } else quickReadLinearLayoutManager.findFirstVisibleItemPosition()
                    quickReadIndicatorsAdapter.updateSelectedIndex(positionReads)
                }
            }
        })
    }

    private fun setUpLifeStyle(lifestyleSearchData: List<LifestyleSearchData>) {
        lifeStyleListModel.clear()

        if (lifestyleSearchData.size > 9) {
            binding.lsMore.visibility = View.VISIBLE
        } else {
            binding.lsMore.visibility = View.GONE
        }

        binding.lsMore.setOnClickListener {
            val intent = Intent(context, LifestyleActivity::class.java)
            intent.putExtra(SearchKey, searchKey)
            startActivity(intent)
        }

        try {
            for (item in lifestyleSearchData) {
                val img: Int = if (item.result.equals("H2O", ignoreCase = true)) {
                    R.drawable.ic_h2o_intake_white
                } else if (item.result.equals("Pillow Time", ignoreCase = true)) {
                    R.drawable.ic_sleep_star_white
                } else if (item.result.equals("Happy Footprints", ignoreCase = true)) {
                    R.drawable.ic_foot_prints_white
                } else if (item.result.equals("Cal - Count", ignoreCase = true)) {
                    R.drawable.ic_cal_burned_white
                } else if (item.result.equals("Active Hours", ignoreCase = true)) {
                    R.drawable.ic_stand_time_white
                } else if (item.result.equals("Weight Watcher", ignoreCase = true)) {
                    R.drawable.ic_ideal_weight_white
                } else {
                    R.drawable.ic_h2o_intake_white
                }
                lifeStyleListModel.add(HappyMartListModel(img, item.result))
            }

            val manager = LinearLayoutManager(context)
            manager.orientation = LinearLayoutManager.HORIZONTAL
            binding.lifeStyleRecyler.layoutManager = manager
            binding.lifeStyleRecyler.hasFixedSize()

            Log.d("search list", "" + lifeStyleListModel.size)


            lifestyleAdapter = LifestyleHorizontalAdapter(
                context, lifeStyleListModel
            ) { lifeStyleTitle ->
                if (lifeStyleTitle.equals("H2O", ignoreCase = true)) {
                    val intent = Intent(context, TrendsActivity::class.java)
                    intent.putExtra("activityType", Constants.WATER)
                    startActivity(intent)
                }
                if (lifeStyleTitle.equals("Pillow Time", ignoreCase = true)) {
                    val intent = Intent(context, TrendsActivity::class.java)
                    intent.putExtra("activityType", Constants.SLEEP)
                    startActivity(intent)
                }
                if (lifeStyleTitle.equals("Happy Footprints", ignoreCase = true)) {
                    val intent = Intent(context, TrendsActivity::class.java)
                    intent.putExtra("activityType", Constants.STEPS)
                    startActivity(intent)
                }
                if (lifeStyleTitle.equals("Cal - Count", ignoreCase = true)) {
                    val intent = Intent(context, TrendsActivity::class.java)
                    intent.putExtra("activityType", Constants.CALORIE)
                    startActivity(intent)
                }
                if (lifeStyleTitle.equals("Active Hours", ignoreCase = true)) {
                    val intent = Intent(context, TrendsActivity::class.java)
                    intent.putExtra("activityType", Constants.ACTIVEHOURS)
                    startActivity(intent)
                }
                if (lifeStyleTitle.equals("Weight Watcher", ignoreCase = true)) {
                    val intent = Intent(context, TrendsActivity::class.java)
                    intent.putExtra("activityType", Constants.WEIGHT)
                    startActivity(intent)
                }
            }
            binding.lifeStyleRecyler.adapter = lifestyleAdapter

            val indicatorSize = if (lifeStyleListModel.size > 9) {
                ceil(9.0 / 3.0).toInt()
            } else {
                ceil(lifeStyleListModel.size.toString().toDouble() / 3.0).toInt()
            }
            if (indicatorSize > 1) {
                binding.rvLifeStyleIndicator.visibility = View.VISIBLE
            } else {
                binding.rvLifeStyleIndicator.visibility = View.GONE
            }
            val quickReadLinearLayoutManager1 = LinearLayoutManager(context)
            quickReadLinearLayoutManager1.orientation = RecyclerView.HORIZONTAL
            val quickReadIndicatorsAdapter = IndicatorsAdapter(context, indicatorSize, 0)
            binding.rvLifeStyleIndicator.adapter = quickReadIndicatorsAdapter
            binding.rvLifeStyleIndicator.layoutManager = quickReadLinearLayoutManager1
            //noinspection InvalidSetHasFixedSize
            binding.rvLifeStyleIndicator.setHasFixedSize(true)
            binding.lifeStyleRecyler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        positionLifeStyle =
                            if (manager.findFirstCompletelyVisibleItemPosition() != -1) {
                                manager.findFirstCompletelyVisibleItemPosition()
                            } else manager.findFirstVisibleItemPosition()
                        quickReadIndicatorsAdapter.updateSelectedIndex(positionLifeStyle)
                    }
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setUpHappyMart(happyMartSearchData: List<HappyMartSearchData>) {
        happyMartListModel.clear()

        if (happyMartSearchData.size > 9) {
            binding.hmMore.visibility = View.VISIBLE
        } else {
            binding.hmMore.visibility = View.GONE
        }

        binding.hmMore.setOnClickListener { _ ->
            val intent = Intent(context, NewHappyMartActivity::class.java)
            intent.putExtra(SearchKey, searchKey)
            startActivity(intent)
        }
        try {
            for (item in happyMartSearchData) {
                val img: Int = if (item.result.equals("Tele-Consultation", ignoreCase = true)) {
                    R.drawable.ic_tele_consultation
                } else if (item.result.equals("Diagnostics and LAB Tests", ignoreCase = true)) {
                    R.drawable.ic_diagnostic
                } else if (item.result.equals("Pharmacy", ignoreCase = true)) {
                    R.drawable.ic_pharmacy
                } else if (item.result.equals("Physical Consultation", ignoreCase = true)) {
                    R.drawable.ic_physical_consultation
                } else if (item.result.equals("Fitness & Nutrition", ignoreCase = true)) {
                    R.drawable.ic_fitness_and_nutrition
                } else if (item.result.equals("Mental Wellbeing", ignoreCase = true)) {
                    R.drawable.ic_mental_welbeing
                } else if (item.result.equals("Hobby", ignoreCase = true)) {
                    R.drawable.ic_hobby
                } else if (item.result.equals("Devices and Electronics", ignoreCase = true)) {
                    R.drawable.ic_device_electronics
                } else {
                    R.drawable.ic_device_electronics
                }
                happyMartListModel.add(HappyMartListModel(img, item.result))
            }

            val manager = LinearLayoutManager(context)
            manager.orientation = LinearLayoutManager.HORIZONTAL
            binding.happyMartRecyler.layoutManager = manager
            binding.happyMartRecyler.hasFixedSize()

            Log.d("search title", "" + happyMartListModel[0].happyMartTile)
            Log.d("search list", "" + happyMartListModel.size)



            lifestyleAdapter = LifestyleHorizontalAdapter(
                context, happyMartListModel
            ) { happyMartTitle ->
                if (happyMartTitle.equals("Tele-Consultation", ignoreCase = true)) {
                    val intent = Intent(context, HappyMartCategory::class.java)
                    intent.putExtra("id", "1")
                    intent.putExtra("category", "Tele-Consultation")
                    intent.putExtra("comingFrom","search")
                    startActivity(intent)
                }
                if (happyMartTitle.equals("Diagnostics and LAB Tests", ignoreCase = true)) {
                    val intent = Intent(context, HappyMartCategory::class.java)
                    intent.putExtra("id", "2")
                    intent.putExtra("category", "Diagnostics and LAB Tests")
                    intent.putExtra("comingFrom","search")
                    startActivity(intent)
                }
                if (happyMartTitle.equals("Pharmacy", ignoreCase = true)) {
                    val intent = Intent(context, HappyMartCategory::class.java)
                    intent.putExtra("id", "3")
                    intent.putExtra("category", "Pharmacy")
                    intent.putExtra("comingFrom","search")
                    startActivity(intent)
                }
                if (happyMartTitle.equals("Physical Consultation", ignoreCase = true)) {
                    val intent = Intent(context, HappyMartCategory::class.java)
                    intent.putExtra("id", "4")
                    intent.putExtra("category", "Physical Consultation")
                    intent.putExtra("comingFrom","search")
                    startActivity(intent)
                }
                if (happyMartTitle.equals("Fitness & Nutrition", ignoreCase = true)) {
                    val intent = Intent(context, HappyMartCategory::class.java)
                    intent.putExtra("id", "5")
                    intent.putExtra("category", "Fitness & Nutrition")
                    intent.putExtra("comingFrom","search")
                    startActivity(intent)
                }
                if (happyMartTitle.equals("Mental Wellbeing", ignoreCase = true)) {
                    val intent = Intent(context, HappyMartCategory::class.java)
                    intent.putExtra("id", "6")
                    intent.putExtra("category", "Mental Wellbeing")
                    intent.putExtra("comingFrom","search")
                    startActivity(intent)
                }
                if (happyMartTitle.equals("Hobby", ignoreCase = true)) {
                    val intent = Intent(context, HappyMartCategory::class.java)
                    intent.putExtra("id", "7")
                    intent.putExtra("category", "Hobby")
                    intent.putExtra("comingFrom","search")
                    startActivity(intent)
                }
                if (happyMartTitle.equals("Devices and Electronics", ignoreCase = true) || happyMartTitle.equals("Devices and Merchandise", ignoreCase = true)) {
                    val intent = Intent(context, HappyMartCategory::class.java)
                    intent.putExtra("id", "12")
                    intent.putExtra("category", "Devices & Merchandise")
                    intent.putExtra("comingFrom","search")
                    startActivity(intent)
                }
                if (happyMartTitle.equals("Travel and Hotels", ignoreCase = true)) {
                    val intent = Intent(context, HappyMartCategory::class.java)
                    intent.putExtra("id", "9")
                    intent.putExtra("category", "Travel and Hotels")
                    intent.putExtra("comingFrom","search")
                    startActivity(intent)
                }

                if (happyMartTitle.equals("Merchandise", ignoreCase = true)) {
                    val intent = Intent(context, HappyMartCategory::class.java)
                    intent.putExtra("id", "12")
                    intent.putExtra("category", "Devices & Merchandise")
                    intent.putExtra("comingFrom","search")
                    startActivity(intent)
                }

                if (happyMartTitle.equals("Others", ignoreCase = true)) {
                    val intent = Intent(context, HappyMartDisclaimerActivity::class.java)
                    intent.putExtra("came_from", Constants.HappyMartOther)
                    intent.putExtra("toolbarname", "Others")
                    intent.putExtra("comingFrom","search")
                    startActivity(intent)
                }
            }
            binding.happyMartRecyler.adapter = lifestyleAdapter


            val indicatorSize = if (happyMartListModel.size > 9) {
                ceil(9.0 / 3.0).toInt()
            } else {
                ceil(happyMartListModel.size.toString().toDouble() / 3.0).toInt()
            }
            if (indicatorSize > 1) {
                binding.rvHappyMartIndicator.visibility = View.VISIBLE
            } else {
                binding.rvHappyMartIndicator.visibility = View.GONE
            }
            val quickReadLinearLayoutManager1 = LinearLayoutManager(context)
            quickReadLinearLayoutManager1.orientation = RecyclerView.HORIZONTAL
            val quickReadIndicatorsAdapter = IndicatorsAdapter(context, indicatorSize, 0)
            binding.rvHappyMartIndicator.adapter = quickReadIndicatorsAdapter
            binding.rvHappyMartIndicator.layoutManager = quickReadLinearLayoutManager1
            //noinspection InvalidSetHasFixedSize
            binding.rvHappyMartIndicator.setHasFixedSize(true)
            binding.happyMartRecyler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        positionHappyMart =
                            if (manager.findFirstCompletelyVisibleItemPosition() != -1) {
                                manager.findFirstCompletelyVisibleItemPosition()
                            } else manager.findFirstVisibleItemPosition()
                        quickReadIndicatorsAdapter.updateSelectedIndex(positionHappyMart)
                    }
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setActiveZone(activeZone: List<HappyMartSearchData>) {
        try {
            activeZoneList.clear()

            if (activeZone.size > 9) {
                binding.activeZoneMore.visibility = View.VISIBLE
            } else {
                binding.activeZoneMore.visibility = View.GONE
            }
            binding.searchActiveZoneRecyler.layoutManager =
                LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
            binding.searchActiveZoneRecyler.hasFixedSize()

            for (item in activeZone) {
                val img: Int = if (item.result.equals("Zen Zone", ignoreCase = true)) {
                    R.drawable.ic_zen_zone_white
                } else {
                    R.drawable.ic_zen_zone_white
                }
                activeZoneList.add(HappyMartListModel(img, item.result))
            }

            activeZoneAdapter = LifestyleHorizontalAdapter(this, activeZoneList,
                LifestyleHorizontalAdapter.LifeStyleClick {
                    if (it.equals("Zen Zone")) {
                        val intent = Intent(context, TrendsActivity::class.java)
                        intent.putExtra("activityType", Constants.MEDITATION)
                        startActivity(intent)
                    }
                })

            binding.searchActiveZoneRecyler.adapter = activeZoneAdapter

        } catch (e: Exception) {
            e.toString()
        }
    }

    private fun setWellBeing(wellBeing: List<HappyMartSearchData>) {
        try {
            wellBeingList.clear()

            if (wellBeing.size > 9) {
                binding.myWellBeingMore.visibility = View.VISIBLE
            } else {
                binding.myWellBeingMore.visibility = View.GONE
            }

            val manager = LinearLayoutManager(context)
            manager.orientation = LinearLayoutManager.HORIZONTAL
            binding.searchMyWellBeingRecyler.layoutManager = manager
            binding.searchMyWellBeingRecyler.hasFixedSize()

            for (item in wellBeing) {
                val img: Int = if (item.result.equals("Health Score", ignoreCase = true)) {
                    R.drawable.ic_know_your_health_white
                }  else if (item.result.equals("Immunity score", true)) {
                    R.drawable.ic_immunity
                } else if (item.result.equals("DAS Score", true)) {
                    R.drawable.ic_dass_21
                } else if (item.result.equals("Face Scan", true) || item.result.equals("Vitals", true)) {
                    R.drawable.ic_facescan_white
                } else {
                    R.drawable.ic_zen_zone_white
                }
                wellBeingList.add(HappyMartListModel(img, item.result))
            }

            wellBeingAdapter = LifestyleHorizontalAdapter(this, wellBeingList,
                LifestyleHorizontalAdapter.LifeStyleClick {
                    if (it.equals("Health Score", true)) {
                        getAnalyasis()
                    }
                    if (it.equals("Immunity score", true)) {
                        NewDashboardHelper.activityName=null
                        NewDashboardHelper.getIRAScore(this@SearchActivity,"search")
                    }
                    if (it.equals("DAS Score", true)) {
                        getDasAnalysis()
                    }
                })

            binding.searchMyWellBeingRecyler.adapter = wellBeingAdapter

            val indicatorSize = if (wellBeingList.size > 9) {
                ceil(9.0 / 3.0).toInt()
            } else {
                ceil(wellBeingList.size.toString().toDouble() / 3.0).toInt()
            }
            if (indicatorSize > 1) {
                binding.rvSearchMyWellBeingIndicator.visibility = View.VISIBLE
            } else {
                binding.rvSearchMyWellBeingIndicator.visibility = View.GONE
            }
            val quickReadLinearLayoutManager1 = LinearLayoutManager(context)
            quickReadLinearLayoutManager1.orientation = RecyclerView.HORIZONTAL
            val quickReadIndicatorsAdapter = IndicatorsAdapter(context, indicatorSize, 0)
            binding.rvSearchMyWellBeingIndicator.adapter = quickReadIndicatorsAdapter
            binding.rvSearchMyWellBeingIndicator.layoutManager = quickReadLinearLayoutManager1
            //noinspection InvalidSetHasFixedSize
            binding.rvSearchMyWellBeingIndicator.setHasFixedSize(true)
            binding.searchMyWellBeingRecyler.addOnScrollListener(object :
                RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        positionHappyMart =
                            if (manager.findFirstCompletelyVisibleItemPosition() != -1) {
                                manager.findFirstCompletelyVisibleItemPosition()
                            } else manager.findFirstVisibleItemPosition()
                        quickReadIndicatorsAdapter.updateSelectedIndex(positionHappyMart)
                    }
                }
            })
        } catch (e: Exception) {
            e.toString()
        }
    }

    fun showSharePopup(context: Context, fileName: String) {
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
            binding.tvMsg.setText("Share")

            binding.ivWhatsapp.setOnClickListener { v ->
                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.type = "text/plain"
                shareIntent.putExtra(Intent.EXTRA_TEXT, fileName)
                context.startActivity(shareIntent)
                alertDialog.dismiss()
            }
            val displayRectangle = Rect()
            val window = (context as SearchActivity).window
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

    fun shareFaceScan(fileName: String?) {
        try {
            CommonUtils.showProgressDialige(this)
            for (i in tribeListId.indices) {
                val communityIDs = CommunityIDs(tribeListId.get(i))
                list.add(communityIDs)
            }
            val request = FacnScanInTribeRequest(fileName, list)
            val apiInterface = RetrofitHandler.apiInterface()
            apiInterface.shareFaceScan(SharedPref.getAuthToken(), request)
                .enqueue(object : Callback<FaceScanInTribeResponse> {
                    override fun onResponse(
                        call: Call<FaceScanInTribeResponse>,
                        response: Response<FaceScanInTribeResponse>
                    ) {
                        CommonUtils.dismissDialoge()
                        if (response.body()!!.success && response.code() == 200) {
                            Toast.makeText(
                                this@SearchActivity,
                                "Message send",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<FaceScanInTribeResponse>, t: Throwable) {
                        CommonUtils.dismissDialoge()
                    }
                })
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    private fun setServices(services: List<HappyMartSearchData>) {
        try {
            serviceList.clear()

            if (services.size > 9) {
                binding.searchServiceMore.visibility = View.VISIBLE
            } else {
                binding.searchServiceMore.visibility = View.GONE
            }

            val manager = LinearLayoutManager(context)
            manager.orientation = LinearLayoutManager.HORIZONTAL
            binding.searchServiceRecyler.layoutManager = manager
            binding.searchServiceRecyler.hasFixedSize()


            for (item in services) {
                val img: Int = if (searchKey.equals("Document", ignoreCase = true) || searchKey.contains("Docu", ignoreCase = true)) {
                    R.drawable.ic_other_document
                } else if (searchKey.equals("Upload", true) || searchKey.contains("Upl", true)) {
                    R.drawable.ic_diagnostic
                } else if (searchKey.equals("Diagnostics", true) || searchKey.contains(
                        "diag",
                        true
                    )
                ) {
                    R.drawable.ic_diagnostic
                } else if (searchKey.equals("Hospitals", true) || searchKey.contains("hos", true)) {
                    R.drawable.ic_hospital
                } else if (searchKey.equals("Doctor", true) || searchKey.contains(
                        "doct",
                        true
                    ) || searchKey.contains("pres", true)
                ) {
                    R.drawable.ic_doctors_prescription
                } else if (searchKey.equals("Diet Plans", true) || searchKey.contains(
                        "Die",
                        true
                    ) || searchKey.contains("pla", true)
                ) {
                    R.drawable.ic_diet_plan
                } else if (searchKey.equals("Fitness Plus", true) || searchKey.contains(
                        "Fit",
                        true
                    ) || searchKey.contains("plu", true)
                ) {
                    R.drawable.ic_fitness
                } else if (searchKey.equals("Vaccination Certifiacate", true) || searchKey.contains(
                        "Vac",
                        true
                    ) || searchKey.contains("cert", true)
                ) {
                    R.drawable.ic_vaccination_certificate
                } else if (searchKey.equals("My Photos", true) || searchKey.contains(
                        "Pho",
                        true
                    ) || searchKey.contains("my", true)
                ) {
                    R.drawable.ic_my_photos
                } else if (searchKey.equals("Blog", true)) {
                    R.drawable.ic_quick_read_white
                } else if (searchKey.equals("Videos", true)) {
                    R.drawable.ic_healthtv_white
                } else if (searchKey.equals("TV", true)) {
                    R.drawable.ic_healthtv_white
                } else if (item.result.contains("locker", true)) {
                    R.drawable.ic_lock
                } else if (item.result.contains("hacks", true)) {
                    R.drawable.ic_healthtv_white
                } else {
                    R.drawable.ic_lock
                }

                serviceList.add(HappyMartListModel(img, item.result))
            }

            serviceAdapter = LifestyleHorizontalAdapter(this, serviceList,
                LifestyleHorizontalAdapter.LifeStyleClick {
                    if (searchKey.contains("Blog", true)) {
                        startActivity(Intent(this@SearchActivity, HealthHacksActivity::class.java))
                    } else if (searchKey.contains("Video", true)) {
                        startActivity(Intent(this@SearchActivity, HealthHacksActivity::class.java))
                    } else if (searchKey.contains("TV", true)) {
                        startActivity(Intent(this@SearchActivity, HealthHacksActivity::class.java))
                    }
                    else if(it.contains("health locker",true)){
                        startActivity(Intent(this@SearchActivity, EhrActivity::class.java))

                    }
                    else if (it.contains("hacks", true)) {
                        startActivity(Intent(this@SearchActivity, HealthHacksActivity::class.java))
                    } else {
                        getHealthRecord(searchKey,this@SearchActivity)
                    }
                })

            binding.searchServiceRecyler.adapter = serviceAdapter

            val indicatorSize = if (serviceList.size > 9) {
                ceil(9.0 / 3.0).toInt()
            } else {
                ceil(serviceList.size.toString().toDouble() / 3.0).toInt()
            }
            if (indicatorSize > 1) {
                binding.rvSearchServiceIndicator.visibility = View.VISIBLE
            } else {
                binding.rvSearchServiceIndicator.visibility = View.GONE
            }
            val quickReadLinearLayoutManager1 = LinearLayoutManager(context)
            quickReadLinearLayoutManager1.orientation = RecyclerView.HORIZONTAL
            val quickReadIndicatorsAdapter = IndicatorsAdapter(context, indicatorSize, 0)
            binding.rvSearchServiceIndicator.adapter = quickReadIndicatorsAdapter
            binding.rvSearchServiceIndicator.layoutManager = quickReadLinearLayoutManager1
            //noinspection InvalidSetHasFixedSize
            binding.rvSearchServiceIndicator.setHasFixedSize(true)
            binding.searchServiceRecyler.addOnScrollListener(object :
                RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        positionHappyMart =
                            if (manager.findFirstCompletelyVisibleItemPosition() != -1) {
                                manager.findFirstCompletelyVisibleItemPosition()
                            } else manager.findFirstVisibleItemPosition()
                        quickReadIndicatorsAdapter.updateSelectedIndex(positionHappyMart)
                    }
                }
            })

        } catch (e: Exception) {
            e.toString()
        }
    }

    private fun setMyZone(myZone: List<HappyMartSearchData>) {
        try {
            myZoneList.clear()
            if (myZone.size > 9) {
                binding.myZoneMore.visibility = View.VISIBLE
            } else {
                binding.myZoneMore.visibility = View.GONE
            }
            val manager = LinearLayoutManager(context)
            manager.orientation = LinearLayoutManager.HORIZONTAL
            binding.searchMyZoneRecyler.layoutManager =
                LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
            binding.searchMyZoneRecyler.hasFixedSize()

            for (item in myZone) {
                val img: Int =
                    if (searchKey.contains("Trib", true) || searchKey.contains("Memb", true) ||
                        searchKey.contains("Fam", true) || searchKey.contains("Fri", true)
                        || searchKey.contains("Coll", true)
                    ) {
                        R.drawable.ic_tribe_white
                    } else if (item.result.equals("unwind", ignoreCase = true)) {
                        R.drawable.ic_riddles_icon
                    } else {
                        R.drawable.ic_tribe_white
                    }

                /* else if(searchKey.contains("Tho",true) || searchKey.contains("Aff",true) ||
                    searchKey.contains("Jok",true) || searchKey.contains("Qui",true) ||
                    searchKey.contains("Rid",true) || searchKey.contains("day",true) || searchKey.contains("dia",true)){
                    R.drawable.ic_riddles_icon
                }*/

                myZoneList.add(HappyMartListModel(img, item.result))
            }



            myZoneAdapter = LifestyleHorizontalAdapter(this, myZoneList
            ) {
                if (it.equals("unwind", ignoreCase = true)) {
                    startActivity(
                        Intent(this@SearchActivity, UnwindActivity::class.java)
                            .putExtra("comingFrom", "searchActivity")
                    )
                }
            }

            binding.searchMyZoneRecyler.adapter = myZoneAdapter

            val indicatorSize = if (myZoneList.size > 9) {
                ceil(9.0 / 3.0).toInt()
            } else {
                ceil(myZoneList.size.toString().toDouble() / 3.0).toInt()
            }
            if (indicatorSize > 1) {
                binding.rvSearchMyZoneIndicator.visibility = View.VISIBLE
            } else {
                binding.rvSearchMyZoneIndicator.visibility = View.GONE
            }
            val quickReadLinearLayoutManager1 = LinearLayoutManager(context)
            quickReadLinearLayoutManager1.orientation = RecyclerView.HORIZONTAL
            val quickReadIndicatorsAdapter = IndicatorsAdapter(context, indicatorSize, 0)
            binding.rvSearchMyZoneIndicator.adapter = quickReadIndicatorsAdapter
            binding.rvSearchMyZoneIndicator.layoutManager = quickReadLinearLayoutManager1
            //noinspection InvalidSetHasFixedSize
            binding.rvSearchMyZoneIndicator.setHasFixedSize(true)
            binding.searchMyZoneRecyler.addOnScrollListener(object :
                RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        positionHappyMart =
                            if (manager.findFirstCompletelyVisibleItemPosition() != -1) {
                                manager.findFirstCompletelyVisibleItemPosition()
                            } else manager.findFirstVisibleItemPosition()
                        quickReadIndicatorsAdapter.updateSelectedIndex(positionHappyMart)
                    }
                }
            })


        } catch (e: Exception) {
            e.toString()
        }
    }

    private fun setOtherData(others: List<HappyMartSearchData>) {
        try {
            othersList.clear()

            if (others.size > 9) {
                binding.searchOthersMore.visibility = View.VISIBLE
            } else {
                binding.searchOthersMore.visibility = View.GONE
            }

            val manager = LinearLayoutManager(context)
            manager.orientation = LinearLayoutManager.HORIZONTAL
            binding.searchOthersRecyler.layoutManager =
                LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
            binding.searchOthersRecyler.hasFixedSize()


            for (item in others) {
                val img: Int = if (item.result.equals("ice", ignoreCase = true)) {
                    R.drawable.ice
                } else if (item.result.equals("profile", ignoreCase = true)) {
                    R.drawable.ic_person_white
                } else if (item.result.equals("add family", ignoreCase = true)) {
                    R.drawable.ic_family_hub
                } else if (item.result.equals("sync your device", ignoreCase = true)) {
                    R.drawable.ic_sync_apps
                } else if (item.result.equals("kotak policy", ignoreCase = true)) {
                    R.drawable.ic_kotak_policy_white
                } else if (item.result.equals("need support", ignoreCase = true)) {
                    R.drawable.ic_need_support_white
                } else if (item.result.equals("notification", ignoreCase = true)) {
                    R.drawable.ic_filter_white
                } else if (item.result.equals("scan qr", ignoreCase = true)) {
                    R.drawable.ic_qrscaner_white
                } else {
                    R.drawable.ic_person_white
                }

                /*if(searchKey.contains("Amb",true) || searchKey.equals("police",true)
                || searchKey.contains("Fir",true) || searchKey.contains("Emer",true)|| searchKey.contains("cont") || searchKey.contains("Doc",true) ||
                searchKey.contains("Call",true) ||  searchKey.contains("Tut",true) || searchKey.contains("SOS",true)
                || searchKey.contains("Fir",true)|| searchKey.contains("Aid",true) || searchKey.contains("CPR",true) || searchKey.contains("Str",true) || searchKey.contains("Seiz",true) || searchKey.contains("Loss",true) ||
                searchKey.contains("breat",true) || searchKey.contains("Dog",true) || searchKey.contains("bite") || searchKey.contains("Snake",true) || searchKey.contains("Inj",true) || searchKey.contains("Woun",true) || searchKey.contains("Epil",true)
                || searchKey.contains("Blood Loss",true) || searchKey.contains("Fire",true) || searchKey.contains("guid") || searchKey.contains("Earth",true) || searchKey.contains("Flo",true) || item.result.equals("ice",true)){
                R.drawable.ice
            }else if(searchKey.contains("Prof",true)){
                R.drawable.ic_person
            }else if(searchKey.contains("Fam",true) || searchKey.contains("Add",true)){
                R.drawable.ic_family_hub
            }else if(searchKey.contains("Sync",true)){
                R.drawable.ic_sync_apps
            }else if(searchKey.contains("Poli",true) || searchKey.contains("Kot",true)){
                R.drawable.ic_kotak_policy
            }else if(searchKey.contains("Ref",true) || searchKey.contains("Refer") || searchKey.contains("code")){
                R.drawable.ic_share
            }else if(searchKey.contains("Supp",true)){
                R.drawable.ic_need_support
            }else if(searchKey.contains("Noti",true)){
                R.drawable.ic_filter
            }else if(searchKey.contains("Scan",true) || searchKey.contains("QR",true)){
                R.drawable.ic_qrscaner
            }
            else {
                R.drawable.ice
            }*/

                othersList.add(HappyMartListModel(img, item.result))
            }

            othersAdapter = LifestyleHorizontalAdapter(
                this@SearchActivity, othersList
            ) {

                if (it.equals("ice", ignoreCase = true)) {
                    startActivity(Intent(this@SearchActivity, ICEDashboardActivity::class.java))
                } else if (it.equals("profile", ignoreCase = true)) {
                    startActivity(Intent(this@SearchActivity, ProfileActivity::class.java))
                } else if (it.equals("add family", ignoreCase = true)) {
                    startActivity(Intent(this@SearchActivity, AddFamilyActivity::class.java))
                } else if (it.equals("sync your device", ignoreCase = true)) {
                    startActivity(Intent(this@SearchActivity, ConnectApp::class.java))
                } else if (it.equals("kotak policy", ignoreCase = true)) {
                    startActivity(
                        Intent(
                            this@SearchActivity,
                            PolicyDetailsActivity::class.java
                        )
                    )
                } else if (it.equals("need support", ignoreCase = true)) {
                    startActivity(Intent(this@SearchActivity, NeedSupportActivity::class.java))
                } else if (it.equals("notifcation", ignoreCase = true) || it.equals("notification", ignoreCase = true)) {
                    startActivity(
                        Intent(
                            this@SearchActivity,
                            NotificationDrawerActivity::class.java
                        )
                    )
                } else if (it.equals("scan qr", ignoreCase = true)) {
                    startActivity(Intent(this@SearchActivity, ScanQRActivity::class.java))
                } else if (it.equals("add referral code", ignoreCase = true)){
                    AddReferralCodeDialogBox.getInstance().addReferralCode("referralCode", this@SearchActivity)
                }
                /*
                                    if(searchKey.contains("Amb",true) || searchKey.contains("police",true)
                                        || searchKey.contains("Fir",true) || searchKey.contains("Emer",true)|| searchKey.contains("cont") || searchKey.contains("Doc",true) ||
                                        searchKey.contains("Call",true) ||  searchKey.contains("Tut",true) || searchKey.contains("SOS",true)
                                        || searchKey.contains("Fir",true)|| searchKey.contains("Aid",true) || searchKey.contains("CPR",true) || searchKey.contains("Str",true) || searchKey.contains("Seiz",true) || searchKey.contains("Loss",true) ||
                                        searchKey.contains("breat",true) || searchKey.contains("Dog",true) || searchKey.contains("bite") || searchKey.contains("Snake",true) || searchKey.contains("Inj",true) || searchKey.contains("Woun",true) || searchKey.contains("Epil",true)
                                        || searchKey.contains("Blood Loss",true) || searchKey.contains("Fire",true) ||searchKey.contains("brig",true) || searchKey.contains("guid") || searchKey.contains("Earth",true) || searchKey.contains("Flo",true) ){

                                            startActivity(Intent(this@SearchActivity, ICEDashboardActivity::class.java))

                                    }else if(searchKey.contains("Prof",true)){
                                        startActivity(Intent(this@SearchActivity, ProfileActivity::class.java))

                                    }else if(searchKey.contains("Fam",true) || searchKey.contains("Add",true)){
                                        startActivity(Intent(this@SearchActivity, AddFamilyActivity::class.java))

                                    }else if(searchKey.contains("Sync",true)){
                                        startActivity(Intent(this@SearchActivity, SyncDeviceActivity::class.java))

                                    }else if(searchKey.contains("Poli",true) || searchKey.contains("Kot",true)){
                                        startActivity(Intent(this@SearchActivity, PolicyDetailsActivity::class.java))

                                    }else if(searchKey.contains("Ref",true) || searchKey.contains("Refer") || searchKey.contains("code")){

                                    }else if(searchKey.contains("Supp",true)){
                                        startActivity(Intent(this@SearchActivity, NeedSupportActivity::class.java))
                                    }else if(searchKey.contains("Noti",true)){
                                        startActivity(Intent(this@SearchActivity, RemindersActivity::class.java))

                                    }else if(searchKey.contains("Scan",true) || searchKey.contains("QR",true)){
                                        startActivity(Intent(this@SearchActivity, ScanQRActivity::class.java))
                                    }
                                    else {

                                    }*/
            }

            binding.searchOthersRecyler.adapter = othersAdapter


        } catch (e: Exception) {
            e.toString()
        }
    }


    private fun addVideoBookmark(isBookmark: Boolean, id: Int) {
        val progressDialog = ProgressDialog(context)
        val apiInterfaceWyh = ApiClientWyh.getClient(CommonUtils.getBaseUrlForAPI(context)).create(
            ApiInterfaceWyh::class.java
        )
        if (progressDialog != null && !progressDialog.isShowing) progressDialog.show()
        val request = VideoBookmarkRequest(id, "video", isBookmark)
        val call = apiInterfaceWyh.addBookmarkVideo(SharedPref.getAuthToken(), request)
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
                        context.getString(R.string.health_hacks_add_bookmark_video_success)
                    )
                    Log.d("BookMark", Gson().toJson(response.body()))
                    if (isBookmark) {
                        Toast.makeText(
                            context,
                            "Successfully added to Bookmark",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(context, "Remove from Bookmark", Toast.LENGTH_SHORT).show()
                    }
                    searchCall()
                } else {
                    Analytics.logEvent(
                        context,
                        context.javaClass.name,
                        context.getString(R.string.health_hacks_add_bookmark_video_failed)
                    )
                    Toast.makeText(
                        context,
                        context.resources.getString(R.string.error_string),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<CommonSuccessResponse?>, t: Throwable) {
                if (progressDialog != null && progressDialog.isShowing) progressDialog.dismiss()
                Analytics.logEvent(
                    context,
                    context.javaClass.name,
                    context.getString(R.string.health_hacks_add_bookmark_video_failed)
                )
                Toast.makeText(
                    context,
                    context.resources.getString(R.string.error_string),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun addBookmark(articleCode: String, isBookmark: Boolean) {
        val progressDialog = ProgressDialog(context)
        val apiInterfaceWyh = ApiClientWyh.getClient(CommonUtils.getBaseUrlForAPI(context)).create(
            ApiInterfaceWyh::class.java
        )
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
                        getString(R.string.health_hacks_add_bookmark_video_success)
                    )
                    Log.d("BookMark", Gson().toJson(response.body()))
                    if (isBookmark) {
                        Toast.makeText(
                            context,
                            "Successfully added to Bookmark",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(context, "Remove from Bookmark", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Analytics.logEvent(
                        context,
                        context.javaClass.name,
                        getString(R.string.health_hacks_add_bookmark_video_failed)
                    )
                    Toast.makeText(
                        context,
                        context.resources.getString(R.string.error_string),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                searchCall()
            }

            override fun onFailure(call: Call<AddBookmarkResponse?>, t: Throwable) {
                if (progressDialog != null && progressDialog.isShowing) progressDialog.dismiss()
                Analytics.logEvent(
                    context,
                    context.javaClass.name,
                    getString(R.string.health_hacks_add_bookmark_video_failed)
                )
                Toast.makeText(
                    context,
                    context.resources.getString(R.string.error_string),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }


    private fun searchItem() {
        if (!searchKey.equals(binding.searchEt.text.toString(), ignoreCase = true)) {
            if (binding.searchEt.text.toString().isEmpty()) {
                Toast.makeText(this, "Please enter at least one character", Toast.LENGTH_SHORT)
                    .show()
            } else {
                searchKey = binding.searchEt.text.toString()
                CommonUtils.hideKeyboard(this)
                searchCall()
            }
        }
    }

    private fun getAnalyasis() {
        try {
            CommonUtils.showProgressDialige(this)
            val request = GetAnalysisRequest(getString(R.string.hra_i_id), "")


            apiInterfaceWyh.getAnalysis(SharedPref.getAuthToken(), request)
                .enqueue(object : Callback<GetAnalysisResponse> {
                    override fun onResponse(
                        call: Call<GetAnalysisResponse>,
                        response: Response<GetAnalysisResponse>
                    ) {
                        CommonUtils.dismissDialoge()
                        if (response != null) {
                            if (response.body() != null && response.code() == 200) {
                                val isShown = SharedPref.getWellBeingIntroShownHealth()
                                val isAnalysis =
                                    getAnalysisResponse != null && getAnalysisResponse!!.analysisData != null && getAnalysisResponse!!.analysisData
                                        .getScore() > 0
                                SharedPref.putAnalysisStatus(isAnalysis)
                                Log.d("reponse", Gson().toJson(getAnalysisResponse))
                                if (!isShown) {
                                    SharedPref.putWellBeingIntroShownHealth(true)
                                    gotoIntroPage(
                                        "KnowYourHealth",
                                        "Health Score",
                                        isAnalysis,
                                        true
                                    )
                                } else {
                                    val intent: Intent
                                    intent = if (isAnalysis) {
                                        Intent(context, HRAAnalysisActivity::class.java)
                                    } else {
                                        Intent(context, HRAQuestionsActivity::class.java)
                                    }
                                    startActivity(intent)
                                }
                            }
                        }

                    }

                    override fun onFailure(call: Call<GetAnalysisResponse>, t: Throwable) {
                        CommonUtils.dismissDialoge()
                    }

                })

        } catch (e: Exception) {
            CommonUtils.dismissDialoge()
            e.toString()
        }
    }

    private fun getDasAnalysis() {
        try {
            CommonUtils.showProgressDialige(this)
            val request = IntegrationIdRequest(resources.getString(R.string.dass21_i_id))
            apiInterfaceWyh.getDassAnalysis(SharedPref.getAuthToken(), request)
                .enqueue(object : Callback<DassAnalysisResponse> {
                    override fun onResponse(
                        call: Call<DassAnalysisResponse>,
                        response: Response<DassAnalysisResponse>
                    ) {
                        CommonUtils.dismissDialoge()
                        if (response.body() != null && response.code() == 200) {
                            dassAnalysisResponse = Gson().fromJson<DassAnalysisResponse>(
                                SharedPref.getDASSAnalysis(),
                                DassAnalysisResponse::class.java
                            )
                            val isAnalysis =
                                dassAnalysisResponse != null && dassAnalysisResponse!!.getData() != null
                            val isShown = SharedPref.getWellBeingIntroShownDAS()
                            if (!isShown) {
                                SharedPref.putWellBeingIntroShownDAS(true)
                                gotoIntroPage("KnowYourDAS", "DAS Score", isAnalysis, true)
                            } else {
                                val intent: Intent = if (isAnalysis) {
                                    Intent(context, Dass21AnalysisActivity::class.java).putExtra("comingFrom","")
                                } else {
                                    Intent(context, Dass21QuestionsActivity::class.java)
                                }
                                startActivity(intent)
                            }
                        }
                    }

                    override fun onFailure(call: Call<DassAnalysisResponse>, t: Throwable) {

                    }

                })

        } catch (e: Exception) {
            e.toString()
        }
    }

     fun getHealthRecord(recordName: String,context: Context) {
        try {
            CommonUtils.showProgressDialige(context)
            apiInterfaceWyh =
                ApiClientWyh.getClient(CommonUtils.getBaseUrlForAPI(context)).create<ApiInterfaceWyh>(
                    ApiInterfaceWyh::class.java
                )
            apiInterfaceWyh.fetchHealthRecordTypes(SharedPref.getAuthToken())
                .enqueue(object : Callback<HealthRecordTypeResponse> {
                    override fun onResponse(
                        call: Call<HealthRecordTypeResponse>,
                        response: Response<HealthRecordTypeResponse>
                    ) {
                        CommonUtils.dismissDialoge()
                        if (response != null) {
                            if (response.body() != null && response.code() == 200) {
                                healthRecordTypeResponse = response.body()!!
                                getHealthRecordType()
                                if (recordName.equals("Document", true) || recordName.contains("docu", true) ||
                                    recordName.equals("Other documents", true)) {
                                    context.startActivity(Intent(context, HealthRecordTypeWiseReportActivity::class.java)
                                            .putExtra("healthRecordId", otherDocsId)
                                            .putExtra("healthRecordName", "Other Documents")
                                            .putExtra("toolbar", "Other Documents")
                                    )
                                }
                                if (recordName.equals("Upload", true) || recordName.contains(
                                        "upl",
                                        true
                                    )
                                ) {
                                    context.startActivity(
                                        Intent(context, AddNewEhrRecord::class.java)
                                            .putExtra("viewType", "all")
                                    )
                                }
                                if (recordName.equals("Diagnostics", true) || recordName.contains("diag", true)
                                ) {
                                    context.startActivity(Intent(context, HealthRecordTypeWiseReportActivity::class.java)
                                            .putExtra("healthRecordId", diagnosticId)
                                            .putExtra("healthRecordName", "Diagnostics")
                                            .putExtra("toolbar", "Diagnostics")
                                    )
                                }
                                if (recordName.equals(
                                        "Hospitals",
                                        true
                                    ) || recordName.contains("hos", true)
                                ) {
                                    context.startActivity(
                                        Intent(
                                            context,
                                            HealthRecordTypeWiseReportActivity::class.java
                                        )
                                            .putExtra("healthRecordId", hospitalId)
                                            .putExtra("healthRecordName", "Hospitals")
                                            .putExtra("toolbar", "Hospitals")
                                    )
                                }
                                if (recordName.equals("Doctor", true) || recordName.contains(
                                        "doct",
                                        true
                                    ) || recordName.contains("pres", true)
                                ) {
                                    context.startActivity(
                                        Intent(
                                            context,
                                            HealthRecordTypeWiseReportActivity::class.java
                                        )
                                            .putExtra("healthRecordId", drPrescriptionId)
                                            .putExtra("healthRecordName", "Doctor's Prescription")
                                            .putExtra("toolbar", "Doctor's Prescription")
                                    )
                                }
                                if (recordName.equals(
                                        "Diet Plans",
                                        true
                                    ) || recordName.contains(
                                        "Die",
                                        true
                                    ) || recordName.contains("pla", true)
                                ) {
                                    context.startActivity(
                                        Intent(
                                            context,
                                            HealthRecordTypeWiseReportActivity::class.java
                                        )
                                            .putExtra("healthRecordId", dietPlanId)
                                            .putExtra("healthRecordName", "Diet Plans")
                                            .putExtra("toolbar", "Diet Plans")
                                    )
                                }
                                if (recordName.equals(
                                        "Fitness Plus",
                                        true
                                    ) || recordName.contains(
                                        "Fit",
                                        true
                                    ) || recordName.contains("plu")
                                ) {
                                    context.startActivity(
                                        Intent(
                                            context,
                                            HealthRecordTypeWiseReportActivity::class.java
                                        )
                                            .putExtra("healthRecordId", fitnessPlusId)
                                            .putExtra("healthRecordName", "Fitness Plus")
                                            .putExtra("toolbar", "Fitness Plus")
                                    )
                                }
                                if (recordName.equals(
                                        "Vaccination certificates",
                                        true
                                    ) || recordName.contains(
                                        "Vac",
                                        true
                                    ) || recordName.contains("cert", true)
                                ) {
                                    context.startActivity(
                                        Intent(
                                            context,
                                            HealthRecordTypeWiseReportActivity::class.java
                                        )
                                            .putExtra("healthRecordId", vaccineCertificateId)
                                            .putExtra(
                                                "healthRecordName",
                                                "Vaccination Certificates"
                                            )
                                            .putExtra("toolbar", "Vaccination Certificates")
                                    )
                                }
                                if (recordName.equals(
                                        "My Photos",
                                        true
                                    ) || recordName.contains(
                                        "Pho",
                                        true
                                    ) || searchKey.contains("my", true)
                                ) {
                                    context.startActivity(
                                        Intent(
                                            context,
                                            HealthLockerPhotosActivity::class.java
                                        )
                                    )
                                }

                                if(recordName.equals("Face scan reports",true)){
                                    val i = Intent(
                                        context,
                                        HealthRecordTypeWiseReportActivity::class.java
                                    )
                                    i.putExtra("healthRecordId", faceScanID)
                                    i.putExtra("healthRecordName", "FaceScan")
                                    i.putExtra("toolbar", "FaceScan Report")
                                    context.startActivity(i)
                                }
                                if(recordName.contains("lock",true) || recordName.contains("locker",true)){
                                    context.startActivity(Intent(context, EhrActivity::class.java)
                                    )
                                }

                            }
                        }
                    }

                    override fun onFailure(call: Call<HealthRecordTypeResponse>, t: Throwable) {
                        CommonUtils.dismissDialoge()

                    }

                })

        } catch (e: Exception) {
            CommonUtils.dismissDialoge()
            e.toString()
        }
    }


    private fun getHealthRecordType() {
        if (healthRecordTypeResponse.data.size > 0) {
            for (i in healthRecordTypeResponse.data.indices) {
                if (healthRecordTypeResponse.data[i].key == "Diet Plans") {
                    dietPlanId = healthRecordTypeResponse.getData().get(i).getId()
                } else if (healthRecordTypeResponse.getData().get(i).getKey() == "Diagnostics") {
                    diagnosticId = healthRecordTypeResponse.getData().get(i).getId()
                } else if (healthRecordTypeResponse.getData().get(i)
                        .getKey() == "Doctor's Prescription"
                ) {
                    drPrescriptionId = healthRecordTypeResponse.getData().get(i).getId()
                } else if (healthRecordTypeResponse.getData().get(i).getKey() == "Fitness Plus") {
                    fitnessPlusId = healthRecordTypeResponse.getData().get(i).getId()
                } else if (healthRecordTypeResponse.getData().get(i)
                        .getKey() == "Vaccination Certificates"
                ) {
                    vaccineCertificateId = healthRecordTypeResponse.getData().get(i).getId()
                } else if (healthRecordTypeResponse.getData().get(i)
                        .getKey() == "Other Documents"
                ) {
                    otherDocsId = healthRecordTypeResponse.getData().get(i).getId()
                } else if (healthRecordTypeResponse.getData().get(i).getKey() == "My Photos") {
                    myPhotosId = healthRecordTypeResponse.getData().get(i).getId()
                } else if (healthRecordTypeResponse.getData().get(i).getKey() == "Hospitals") {
                    hospitalId = healthRecordTypeResponse.getData().get(i).getId()
                }else if (healthRecordTypeResponse.getData().get(i).getKey() == "Face scan reports") {
                    faceScanID = healthRecordTypeResponse.getData().get(i).getId()
                }
            }
        }
    }

    private fun gotoIntroPage(
        cameFrom: String,
        categoryName: String,
        isAnalysis: Boolean,
        shownScreen: Boolean
    ) {
        val intent = Intent(context, WellBeingDisclaimerActivity::class.java)
        intent.putExtra("came_from", cameFrom)
        intent.putExtra("CategoryName", categoryName)
        intent.putExtra("isAnalysis", isAnalysis)
        intent.putExtra("shownScreen", shownScreen)
        startActivityForResult(intent, 101)
    }

}