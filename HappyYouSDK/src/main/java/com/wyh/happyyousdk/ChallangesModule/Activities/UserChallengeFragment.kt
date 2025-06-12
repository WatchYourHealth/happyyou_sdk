package com.wyh.happyyousdk.ChallangesModule.Activities

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.wyh.happyyousdk.ChallangesModule.Adapter.TribeMemberAdapter
import com.wyh.happyyousdk.R
import com.wyh.happyyousdk.databinding.FragmentUserChallengeBinding
import com.wyh.happyyousdk.model.request.challengeTribe.GetCommunityRankDetailsTribeList
import com.wyh.happyyousdk.model.request.challengeTribe.StackedGraphModel
import com.wyh.happyyousdk.trends.MarkerDataClass
import com.wyh.happyyousdk.utils.CommonUtils
import com.wyh.happyyousdk.utils.MyMarkerView
import com.wyh.happyyousdk.utils.MyMarkerViewForCommunity
import com.wyhsdk.utils.Utilities
import java.text.SimpleDateFormat
import java.util.Date
import java.util.concurrent.TimeUnit
import kotlin.math.absoluteValue

class UserChallengeFragment : Fragment() {
    private lateinit var binding: FragmentUserChallengeBinding

    private var spinnerIndex: Int = 0
    private lateinit var mContext: Context
    private var axisPadding = 0.5f
    var mv: MyMarkerViewForCommunity? = null
    private var periodIndex = 0
    private var communityId = 0
    private var startDate: String = ""
    private var endDate: String = ""
    lateinit var memberAdapter: TribeMemberAdapter
    var tribeLists: List<GetCommunityRankDetailsTribeList> = ArrayList()
    private var challengeID: String = ""
    private lateinit var viewModel: ChallengeViewModel
    private var periodLastIndex:Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentUserChallengeBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity())[ChallengeViewModel::class.java]

        mContext = requireContext()
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(activity)
        binding.rvMembers.layoutManager = layoutManager
        memberAdapter = TribeMemberAdapter(mContext)
        binding.rvMembers.adapter = memberAdapter
        challengeID = (activity as TribeChallengeDashboard).challengeID

        getObserveData()

        /*binding.btnViewMore.setOnClickListener {
            (activity as TribeChallengeDashboard).viewMorePopUp()
        }*/

        binding.ivPrev.setOnClickListener {
            try {
                if (periodIndex.absoluteValue < periodLastIndex) {
                    periodIndex -= 1
                    (activity as TribeChallengeDashboard).userApiCAll(communityId = communityId, periodIndex = periodIndex, isCame = ISLoadingEnum.Graph)
                }
            } catch (_: Exception) {

            }
        }

        binding.ivNext.setOnClickListener {
            try {
                if (periodIndex != 0) {
                    periodIndex += 1
                    (activity as TribeChallengeDashboard).userApiCAll(communityId = communityId, periodIndex = periodIndex, isCame = ISLoadingEnum.Graph)
                }
            } catch (_: Exception) {

            }
        }

        return binding.root
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


    private fun getObserveData() {

        viewModel.isLoadingUserData.observe(requireActivity(), Observer {
            if (it) {
                binding.pBar.visibility = View.VISIBLE
                binding.scrollView.visibility = View.GONE
            } else {
                binding.scrollView.visibility = View.VISIBLE
                binding.pBar.visibility = View.GONE
            }
        })

        viewModel.isLoadingUserDataTwo.observe(requireActivity(), Observer {
            if(it == null){
                binding.spBar.visibility = View.GONE
            } else if(it) {
                binding.spBar.visibility = View.VISIBLE
            } else {
                binding.spBar.visibility = View.GONE
            }
        })

        viewModel.isLoadingUserDataGraph.observe(requireActivity(), Observer {
            if(it == null){
                binding.gpBar.visibility = View.GONE
                binding.tribeChart.visibility = View.VISIBLE
            } else if(it) {
                binding.gpBar.visibility = View.VISIBLE
                binding.tribeChart.visibility = View.GONE
            } else {
                binding.gpBar.visibility = View.GONE
                binding.tribeChart.visibility = View.VISIBLE

            }
        })

        viewModel.userDataLiveData.observe(requireActivity(), Observer {
            if (it != null) {
                if (it.tribeChallengeDashboard.tribeUserDetails.isNotEmpty()) {
                    tribeLists = it.tribeChallengeDashboard.tribeLists
                    startDate = CommonUtils.formatDateFromString(
                        "MM/dd/yyyy",
                        "yyyy-MM-dd",
                        it.tribeChallengeDashboard.challengeDetails.startDate
                    )
                    endDate = CommonUtils.formatDateFromString(
                        "MM/dd/yyyy",
                        "yyyy-MM-dd",
                        it.tribeChallengeDashboard.challengeDetails.endDate
                    )

                    memberAdapter.submitList(it.tribeChallengeDashboard.tribeUserDetails)



                    if(binding.spinnerTribes.adapter == null){
                        val todayDateNew = Utilities.getTodayDateNew()
                        val sdf = SimpleDateFormat("yyyy-MM-dd")
                        val date = sdf.parse(startDate)
                        val date1 = sdf.parse(todayDateNew)
                        val diff = date1.time - date.time
                        val days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS).toInt()
                        periodLastIndex = (days / 7)
                        setSpinner()
                    }
                    if((activity as TribeChallengeDashboard).communityId != -1){
                        setSpinner()
                    }

                    setPrevAndNextIcon()
                }

                if(it.stackedGraphModel.dataPoints.isNotEmpty()){
                    binding.tvDateRange.text = getString(
                        R.string.set_text_2_item,
                        combineDate(it.stackedGraphModel.dataPoints[0].recordDate),
                        combineDate(it.stackedGraphModel.dataPoints.last().recordDate)
                    )
                    setGraph("DAILY", it.stackedGraphModel)
                }
            }
        })

    }

    private fun setSpinner() {
        val tribeNames: ArrayList<String> = ArrayList()
        var mPosition = 0

        tribeLists.forEachIndexed { index, item ->
            tribeNames.add(item.communityName)
            if(item.communityId.toInt() == (activity as TribeChallengeDashboard).communityId && (activity as TribeChallengeDashboard).communityId != -1){
                spinnerIndex = index
                mPosition = index
                (activity as TribeChallengeDashboard).communityId = -1
            }

        }
        /*for (item in tribeLists) {
            tribeNames.add(item.communityName)
            if(item.communityId.toInt() == (activity as TribeChallengeDashboard).communityId){

            }
        }*/
        val tribeSpinnerAdapter: ArrayAdapter<String> = ArrayAdapter<String>(mContext,
            R.layout.spinner_item, tribeNames
        )
        tribeSpinnerAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item)
        binding.spinnerTribes.prompt = "Select Tribe"
        binding.spinnerTribes.adapter = tribeSpinnerAdapter
        binding.spinnerTribes.setSelection(mPosition)

        binding.spinnerTribes.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                communityId = tribeLists[position].communityId.toInt()
                Log.d("communityId1", "$communityId")
                if (spinnerIndex != position) {
                    spinnerIndex = position
                    (activity as TribeChallengeDashboard).userApiCAll(communityId= communityId, isCame = ISLoadingEnum.Spinner)
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }

    }


    private fun setGraph(periodType: String, graphData: StackedGraphModel) {
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
            xAxis1.position = XAxis.XAxisPosition.BOTTOM
            xAxis1.setDrawAxisLine(true)
            xAxis1.granularity = 1f

            binding.tribeChart.setExtraOffsets(0f, 0f, 0f, 16f)
            binding.tribeChart.setDrawBorders(false)
            binding.tribeChart.description.isEnabled = false
            binding.tribeChart.xAxis.valueFormatter = IndexAxisValueFormatter(
                CommonUtils.getWeekStrings(
                    periodType,
                    mContext.resources
                )
            )


            val entries1 = java.util.ArrayList<BarEntry>()
            entries1.clear()
            entries1.addAll(CommonUtils.getWeeklyDataBarChallenges(graphData)
            )


//        if (entries1.size() > 0 && mv != null){
//            mv.refreshContent(entries1.get(0), new Highlight(entries1.get(0).getX(), entries1.get(0).getY(), 0));
//        }
            var set1: BarDataSet? = null


            var markerDataClass = MarkerDataClass()
            if (entries1.size > 0) {
                markerDataClass = entries1[0].data as MarkerDataClass
            }

            val userNames: Array<String> = markerDataClass.userName.toTypedArray()

            set1 = if (userNames.size == 1) BarDataSet(
                entries1,
                userNames[0]
            ) else BarDataSet(entries1, "")

            set1.stackLabels = userNames

            set1.setColors(getColors(userNames.size), mContext)
            binding.tribeChart.legend.isEnabled = true
            binding.tribeChart.description.isEnabled = false

            set1.setDrawValues(false)

            val barData: BarData = BarData(set1)

            barData.setValueTextColor(Color.WHITE)
            barData.setValueTextSize(9f)
            barData.barWidth = 0.25f

            xAxis1.axisMinimum = -axisPadding
            xAxis1.setAxisMaxValue(barData.xMax + axisPadding)

            mv = MyMarkerViewForCommunity(mContext, R.layout.barchart_marker_view_layout)
            binding.tribeChart.marker = mv


            binding.tribeChart.data = barData
            binding.tribeChart.invalidate()
        } catch (_: Exception) {

        }

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
}