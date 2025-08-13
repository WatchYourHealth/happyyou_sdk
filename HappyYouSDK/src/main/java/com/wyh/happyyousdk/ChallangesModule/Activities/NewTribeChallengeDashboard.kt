package com.wyh.happyyousdk.ChallangesModule.Activities

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.LegendEntry
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.google.gson.Gson
import com.wyh.happyyousdk.APIEncryption.APIInterface
import com.wyh.happyyousdk.APIEncryption.RetrofitHandler
import com.wyh.happyyousdk.ChallangesModule.Activities.TribeChallengeDashboard
import com.wyh.happyyousdk.ChallangesModule.Adapter.TribeMemberAdapter
import com.wyh.happyyousdk.ChallangesModule.Adapter.TribeViewMoreAdapter
import com.wyh.happyyousdk.ChallangesModule.ClickInterface.TribeChallengeClick
import com.wyh.happyyousdk.ChallangesModule.helperClass
import com.wyh.happyyousdk.R
import com.wyh.happyyousdk.SDKConstants

import com.wyh.happyyousdk.dashboard.NewDashboardActivity
import com.wyh.happyyousdk.databinding.ActivityNewTribeChallengeDashboardBinding
import com.wyh.happyyousdk.databinding.TribeInfoViewMorePopupBinding
import com.wyh.happyyousdk.model.request.challengeTribe.*
import com.wyh.happyyousdk.model.response.BadgesData
import com.wyh.happyyousdk.trends.MarkerDataClass
import com.wyh.happyyousdk.utils.CommonUtils
import com.wyh.happyyousdk.utils.Constants.DAILY
import com.wyh.happyyousdk.utils.MyMarkerViewForCommunity
import com.wyh.happyyousdk.utils.SharedPref
import com.wyhsdk.sharedPreferences.SharedPreference
import dev.skymansandy.scratchcardlayout.listener.ScratchListener
import dev.skymansandy.scratchcardlayout.ui.ScratchCardLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.absoluteValue

class NewTribeChallengeDashboard : AppCompatActivity(), ScratchListener {
    var challengeID: String = ""
    private var periodIndex = 0
    lateinit var binding: ActivityNewTribeChallengeDashboardBinding
    var tribeNames : ArrayList<String> = ArrayList<String>()
    var communityIDList : ArrayList<Int> = ArrayList<Int>()
    var namesForGraph : ArrayList<String> = ArrayList<String>()
    var badgesList = ArrayList<BadgesData>()

    var communityID = 0
    lateinit var memberAdapter: TribeMemberAdapter
    private var axisPadding = 0.5f
    var mv: MyMarkerViewForCommunity? = null
    private var periodLastIndex:Int = 0
    var isTribeSpinnerSet = false
    var rankType = ""
    lateinit var transactionID : String





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_new_tribe_challenge_dashboard)
        challengeID = intent.extras?.getString("challengeID") ?: ""
        SharedPref.init(this)
        SharedPreference.init(this)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        binding.rvMembers.layoutManager = layoutManager
        memberAdapter = TribeMemberAdapter(this)
        binding.rvMembers.adapter = memberAdapter
        rankType = "Tribe"
        binding.leaderboardTitle.text = "Community Leaderboard"
        binding.nameTitleTv.text = "Tribe Name "

        Glide.with(this@NewTribeChallengeDashboard)
            .load(CommonUtils.getBaseUrlForAPI(this@NewTribeChallengeDashboard) + SDKConstants.endPointForImages + "ic_tree_cloud_bg.png")
            .into(object : CustomTarget<Drawable?>() {
                override fun onResourceReady(
                    resource: Drawable,
                    transition: Transition<in Drawable?>?
                ) {
                    binding.rlHeader.setBackground(resource)
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                }
            })

        getCommunityData("Tribe",communityID)

        binding.includeToolbar.llBack.setOnClickListener { finish() }
        binding.ivHome.setOnClickListener {
            val intent = Intent(this, NewDashboardActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }


        binding.mainSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if(!isTribeSpinnerSet){
                    isTribeSpinnerSet = true
                }else {
                    if (tribeNames[p2].equals("All")) {
                        rankType = "Tribe"
                        binding.btnViewMore.visibility = View.VISIBLE
                        getCommunityData("Tribe",0)
                        binding.leaderboardTitle.text = "Community Leaderboard"
                        binding.nameTitleTv.text = "Tribe Name"
                    } else {
                        rankType = "Users"
                        binding.btnViewMore.visibility = View.GONE
                        communityID = communityIDList[p2-1]
                        binding.leaderboardTitle.text = "Tribe Leaderboard"
                        getCommunityData("Users",communityID)
                        binding.nameTitleTv.text = "User Name"
                    }
                }

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }


        binding.ivPrev.setOnClickListener {
            try {
                if (periodIndex.absoluteValue < periodLastIndex) {
                    periodIndex -= 1
                    getCommunityData(rankType,communityID)
                }
            } catch (_: Exception) {

            }
        }

        binding.ivNext.setOnClickListener {
            try {
                if (periodIndex != 0) {
                    periodIndex += 1
                    getCommunityData(rankType,communityID)
                }
            } catch (_: Exception) {

            }
        }

        binding.btnViewMore.setOnClickListener {
            viewAllTribes()
        }
    }

    private fun setPrevAndNextIcon(){
        if(periodIndex == 0){
            binding.ivNext.visibility = View.GONE
        }else{
            binding.ivNext.visibility = View.VISIBLE
        }

        if(periodIndex.absoluteValue >= periodLastIndex){
            binding.ivPrev.visibility = View.GONE
        }else{
            binding.ivPrev.visibility = View.VISIBLE
        }
    }

    fun getCommunityData(rankType: String,communityID: Int){
        try{
            CommonUtils.showProgressDialige(this)
            val apiInterface = RetrofitHandler.getRetrofitInstance().create(APIInterface::class.java)
            val request = GetCommunityRankDetailsRequest(challengeID,communityID,
                DAILY,periodIndex,"STEPS",rankType)
            apiInterface.getCommunityRankDetails(SharedPref.getAuthToken(),request).enqueue(object : Callback<GetCommunityRankDetailsResponse>{
                override fun onResponse(call: Call<GetCommunityRankDetailsResponse>, response: Response<GetCommunityRankDetailsResponse>) {
                    CommonUtils.dismissDialoge()
                    tribeNames.clear()
                    communityIDList.clear()
                    tribeNames.add("All")
                    if(response.code() == 200 && response.isSuccessful){
                        Log.d("Authtoken","ChallangeResponse " + Gson().toJson(response.body()))
                        if(response.body()!!.data.tribeChallengeDashboard.tribeDetails != null){
                            binding.myRank.text = "My Rank: ${response.body()!!.data.tribeChallengeDashboard.tribeDetails.userRank}"
                            binding.tribeName.text = response.body()!!.data.tribeChallengeDashboard.tribeDetails.tribeName
                            binding.totalRank.text = response.body()!!.data.tribeChallengeDashboard.tribeDetails.tribeRank
                            binding.communityGoal.text = "Community Goal: ${response.body()!!.data.tribeChallengeDashboard.tribeDetails.communitySteps} / ${response.body()!!.data.tribeChallengeDashboard.tribeDetails.challengeGoal}"
                            binding.tribeSteps.text = "Tribe Steps: ${response.body()!!.data.tribeChallengeDashboard.tribeDetails.tribeSteps}"
                        }
                        response.body()!!.data.tribeChallengeDashboard.tribeLists.forEachIndexed { index, item ->
                            tribeNames.add(item.communityName)
                            communityIDList.add(item.communityId.toInt())
                        }
                        if(!isTribeSpinnerSet){
                            val tribeSpinnerAdapter: ArrayAdapter<String> = ArrayAdapter<String>(this@NewTribeChallengeDashboard,
                                R.layout.spinner_item, tribeNames
                            )
                            tribeSpinnerAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item)
                            binding.mainSpinner.adapter = tribeSpinnerAdapter
                        }
                        memberAdapter.submitList(response.body()!!.data.tribeChallengeDashboard.tribeUserDetails)


                        if(rankType.equals("Users",true)){
                            if(response.body()!!.data.stackedGraphModel != null){
                                binding.tvDateRange.text = getString(
                                    R.string.set_text_2_item,
                                    combineDate(response.body()!!.data.stackedGraphModel.dataPoints[0].recordDate),
                                    combineDate(response.body()!!.data.stackedGraphModel.dataPoints.last().recordDate)
                                )
                                setGraph(periodIndex,response.body()!!.data.stackedGraphModel,
                                    response.body()!!.data.tribeChallengeDashboard.tribeUserDetails)
                            }
                        }else{
                            setGraph(periodIndex,response.body()!!.data.stackedGraphModel,
                                response.body()!!.data.tribeChallengeDashboard.tribeUserDetails)
                        }

                        if(response.body()!!.data != null)
                            if(response.body()!!.data!!.tribeChallengeDashboard.badges != null){
                                if(response.body()!!.data!!.tribeChallengeDashboard.badges.isNotEmpty()){
                                    badgesList = response.body()!!.data.tribeChallengeDashboard.badges
                                    helperClass.scracthDialoge(this@NewTribeChallengeDashboard,this@NewTribeChallengeDashboard,
                                        response.body()!!.data.tribeChallengeDashboard.badges[0],badgesList,"tribeChallenge")

                                    transactionID = response.body()!!.data.tribeChallengeDashboard.badges[0].transactionId

                                }
                            }

                        setPrevAndNextIcon()


                    }
                }

                override fun onFailure(call: Call<GetCommunityRankDetailsResponse>, t: Throwable) {
                    CommonUtils.dismissDialoge()
                }

            })


        }catch (e: Exception){
            CommonUtils.dismissDialoge()
            e.toString()
        }
    }

    fun viewAllTribes(){
        try{
            CommonUtils.showProgressDialige(this)
            val apiInterface = RetrofitHandler.getRetrofitInstance().create(APIInterface::class.java)
            apiInterface.viewAllTribes(SharedPref.getAuthToken()).enqueue(object : Callback<ViewMoreTribeResponseModel> {
                override fun onResponse(call: Call<ViewMoreTribeResponseModel>, response: Response<ViewMoreTribeResponseModel>) {
                    CommonUtils.dismissDialoge()
                    if (response.code() == 200 && response.isSuccessful) {
                        if (response.body() != null && response.body()!!.data.isNotEmpty()) {
                            viewMorePopUp(response.body()!!.data)
                        }
                    }
                }

                override fun onFailure(call: Call<ViewMoreTribeResponseModel>, t: Throwable) {
                    CommonUtils.dismissDialoge()
                }

            })

        }catch (e: Exception){
            CommonUtils.dismissDialoge()
            e.toString()
        }
    }


    fun viewMorePopUp(dataList: List<ViewMoreTribeResponseDataModel>) {
        val alertBuilder = AlertDialog.Builder(this)
        val dialogBinding: TribeInfoViewMorePopupBinding = DataBindingUtil.inflate(
            LayoutInflater.from(this),
            R.layout.tribe_info_view_more_popup,
            null,
            false
        )
        alertBuilder.setView(dialogBinding.root)
        val alertDialog = alertBuilder.create()

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        dialogBinding.rvTribe.layoutManager = layoutManager
        dialogBinding.rvTribe.setHasFixedSize(true)

        val memberAdapter = TribeViewMoreAdapter(this, object : TribeChallengeClick {
            override fun onClick(data: ViewMoreTribeResponseDataModel) {
                /*alertDialog.dismiss()
                communityId = data.communityId.toInt()
                userApiCAll(communityId = data.communityId.toInt(), isCame = ISLoadingEnum.Main)
                binding.viewpager.currentItem = 1*/
            }
        })

        dialogBinding.rvTribe.adapter = memberAdapter

        dialogBinding.btnClosed.setOnClickListener {
            alertDialog.dismiss()
        }

        memberAdapter.submitList(dataList)
        alertDialog.show()

        alertDialog.setCancelable(true)

        val displayRectangle = Rect()
        val window = window

        window.decorView.getWindowVisibleDisplayFrame(displayRectangle)
        alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        alertDialog.window!!.setLayout(
            (displayRectangle.width() *
                    0.8f).toInt(), RelativeLayout.LayoutParams.WRAP_CONTENT
        )


    }

    private fun setGraph(periodType: Int, graphData: StackedGraphModel, newGraphData : kotlin.collections.List<GetCommunityRankDetailsTribeUserDetail>) {
        try {
            binding.tribeChart.clear()
            binding.tribeChart.setTouchEnabled(true)
            binding.tribeChart.isDragEnabled = false
            binding.tribeChart.setScaleEnabled(false)
            binding.tribeChart.isScaleXEnabled = false
            binding.tribeChart.isScaleYEnabled = false
            binding.tribeChart.setPinchZoom(false)
            binding.tribeChart.isDoubleTapToZoomEnabled = false
            binding.tribeChart.legend.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
            binding.tribeChart.legend.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT

            val rightAxis1: YAxis = binding.tribeChart.axisRight
            rightAxis1.isEnabled = false
            rightAxis1.setDrawLabels(false)
            rightAxis1.setDrawGridLines(false)
            rightAxis1.axisMinimum = 0f
            rightAxis1.setDrawAxisLine(true)

            val leftAxis1: YAxis = binding.tribeChart.axisLeft
            leftAxis1.setDrawAxisLine(true)
            leftAxis1.setDrawLabels(true)
            leftAxis1.axisMinimum = 0f // VitalsActivity.this replaces setStartAtZero(true)

            leftAxis1.isEnabled = true
            leftAxis1.setDrawLimitLinesBehindData(false)
            leftAxis1.setDrawGridLines(false)

            val xAxis1: XAxis = binding.tribeChart.xAxis
            xAxis1.setDrawGridLines(false)
            xAxis1.labelRotationAngle = -20f
            xAxis1.position = XAxis.XAxisPosition.BOTTOM
            xAxis1.setDrawAxisLine(true)
            xAxis1.granularity = 1f

            binding.tribeChart.setExtraOffsets(0f, 0f, 0f, 16f)
            binding.tribeChart.setDrawBorders(false)
            binding.tribeChart.description.isEnabled = false
            newGraphData.forEachIndexed { index, item ->
                namesForGraph.add(item.name)
            }
            if(rankType.equals("Tribe",true)){
                binding.tribeChart.xAxis.valueFormatter = IndexAxisValueFormatter(namesForGraph)
            }else{
                binding.tribeChart.xAxis.valueFormatter = IndexAxisValueFormatter(
                    CommonUtils.getWeekStrings(DAILY, resources)
                )
            }

            val entries1 = java.util.ArrayList<BarEntry>()
            entries1.clear()
            var set1: BarDataSet? = null
            binding.tribeChart.legend.isWordWrapEnabled = true
            if(rankType.equals("Tribe",true)){
                entries1.addAll(CommonUtils.getWeeklyTribeDataBar(newGraphData))
                var markerDataClass = MarkerDataClass()
                val userNames: kotlin.collections.ArrayList<String> = kotlin.collections.ArrayList<String>()
                entries1.forEachIndexed { index, item ->
                    markerDataClass = item.data as MarkerDataClass
                    userNames.add(markerDataClass.name)
                }
                set1 = if (userNames.size == 1) BarDataSet(
                    entries1,
                    userNames[0]
                ) else BarDataSet(entries1, "")

//                set1.stackLabels = userNames.toTypedArray()
                set1.setColors(getColors(userNames.size), this)
                val legends : ArrayList<LegendEntry> = getLegendsForTribeChart(userNames, set1.colors)
                binding.tribeChart.legend.setCustom(legends)

//                binding.tribeChart.legend.setExtra(getColors(userNames.size), userNames.toTypedArray())

            }else{
                entries1.addAll(CommonUtils.getWeeklyDataBarChallenges(graphData))
                var markerDataClass = MarkerDataClass()
                if (entries1.size > 0) {
                    markerDataClass = entries1[0].data as MarkerDataClass
                }

                val userNames: Array<String> = markerDataClass.userName.toTypedArray()
                binding.tribeChart.legend.resetCustom()

                set1 = if (userNames.size == 1) BarDataSet(
                    entries1,
                    userNames[0]
                ) else BarDataSet(entries1, "")
//                binding.tribeChart.legend.setExtra(null, null)

                set1.stackLabels = userNames

                set1.setColors(getColors(userNames.size), this)


            }

//        if (entries1.size() > 0 && mv != null){
//            mv.refreshContent(entries1.get(0), new Highlight(entries1.get(0).getX(), entries1.get(0).getY(), 0));
//        }

            binding.tribeChart.legend.isEnabled = true
            binding.tribeChart.description.isEnabled = false

            set1.setDrawValues(false)

            val barData: BarData = BarData(set1)

            barData.setValueTextColor(Color.WHITE)
            barData.setValueTextSize(9f)
            barData.barWidth = 0.25f

            xAxis1.axisMinimum = -axisPadding
            xAxis1.setAxisMaxValue(barData.xMax + axisPadding)

            mv = MyMarkerViewForCommunity(this, R.layout.barchart_marker_view_layout)
            binding.tribeChart.marker = mv


            binding.tribeChart.data = barData
            binding.tribeChart.invalidate()
        } catch (_: Exception) {

        }

    }

    private fun getLegendsForTribeChart(userNames: ArrayList<String>, colors: MutableList<Int>): ArrayList<LegendEntry> {
        val legends : ArrayList<LegendEntry> = kotlin.collections.ArrayList()
        userNames.forEachIndexed { index, item ->
            legends.add(LegendEntry(userNames[index], Legend.LegendForm.DEFAULT, Float.NaN,
                Float.NaN, null, colors[index]))
        }
        return legends
    }

    fun getColors(size: Int): IntArray {
        var size = size
        if (size == 0) size = 1
        val colors = intArrayOf(
            R.color.blue_cyan, R.color.blue, R.color.orange, R.color.happy_dark_grey,
            R.color.btn_blue, R.color.dark_pink, R.color.light_blue, R.color.light_orange,
            R.color.purple_500, R.color.kotakDarkBrown
        )
        val colorsNew = IntArray(size)
        for (i in 0 until size) {
            colorsNew[i] = colors[i]
        }
        return colorsNew
    }

    private fun combineDate(date: String): String {
        val dateFormat = convertDate(date)
        val month = DateFormat.format("MMM", dateFormat)
        val day = DateFormat.format("dd", dateFormat)

        return "$month $day"
    }

    private fun convertDate(date: String): Date? {
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        return sdf.parse(date)
    }

    override fun onScratchComplete() {

    }

    override fun onScratchProgress(scratchCardLayout: ScratchCardLayout, atLeastScratchedPercent: Int) {
        if(atLeastScratchedPercent > 20){
            Log.d("AuthToken","Scracth Completed")
            scratchCardLayout.onFullReveal()
            helperClass.scratchComplete(this,SharedPref.getTransactionID())
        }
    }

    override fun onScratchStarted() {

    }
}