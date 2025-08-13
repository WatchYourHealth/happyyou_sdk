package com.wyh.happyyousdk.ChallangesModule.Activities

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.wyh.happyyousdk.APIEncryption.APIInterface
import com.wyh.happyyousdk.APIEncryption.RetrofitHandler
import com.wyh.happyyousdk.ChallangesModule.Adapter.TribeViewMoreAdapter
import com.wyh.happyyousdk.ChallangesModule.ClickInterface.TribeChallengeClick
import com.wyh.happyyousdk.R
import com.wyh.happyyousdk.SDKConstants
import com.wyh.happyyousdk.dashboard.NewDashboardActivity
import com.wyh.happyyousdk.databinding.ActivityTribeChallengeDashboardBinding
import com.wyh.happyyousdk.databinding.TribeInfoViewMorePopupBinding
import com.wyh.happyyousdk.model.request.challengeTribe.GetCommunityRankDetailsTribeDetails
import com.wyh.happyyousdk.model.request.challengeTribe.GetCommunityRankDetailsTribeList
import com.wyh.happyyousdk.model.request.challengeTribe.ViewMoreTribeResponseDataModel
import com.wyh.happyyousdk.model.request.challengeTribe.ViewMoreTribeResponseModel
import com.wyh.happyyousdk.rewards.SampleFragmentPagerAdapter
import com.wyh.happyyousdk.utils.CommonUtils
import com.wyh.happyyousdk.utils.SharedPref
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.ln
import kotlin.math.pow

class TribeChallengeDashboard : AppCompatActivity() {
    lateinit var binding: ActivityTribeChallengeDashboardBinding
    var challengeID: String = ""
    var communityId = -1
    private var tabIndex: Int = 0
    private lateinit var viewModel: ChallengeViewModel
    var tribeLists: List<GetCommunityRankDetailsTribeList> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_tribe_challenge_dashboard)
        challengeID = intent.extras?.getString("challengeID") ?: ""
        viewModel = ViewModelProvider(this)[ChallengeViewModel::class.java]

        binding.includeToolbar.llBack.setOnClickListener { finish() }
        binding.includeToolbar.tvBack.text = getString(R.string.challenge)
        binding.includeToolbar.ivMenu.visibility = View.GONE

        Glide.with(this@TribeChallengeDashboard)
            .load(CommonUtils.getBaseUrlForAPI(this@TribeChallengeDashboard) + SDKConstants.endPointForImages + "ic_tree_cloud_bg.png")
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

        apiCalling()
        setupViewPager()
        binding.slidingTabs.setupWithViewPager(binding.viewpager)
        //setupTabIcons()

        binding.ivHome.setOnClickListener {
            val intent = Intent(this, NewDashboardActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }

        userApiCAll(0, isCame = ISLoadingEnum.Main)


        /*binding.slidingTabs.addOnTabSelectedListener(object : OnTabSelectedListener {
            @RequiresApi(api = Build.VERSION_CODES.M)
            override fun onTabSelected(tab: TabLayout.Tab) {

                if(tab.text.toString().equals("Tribe Leaderboard", true)){
                    userApiCAll(0, isCame = ISLoadingEnum.Main)

                   *//* if(viewModel.userDataLiveData.value?.tribeChallengeDashboard == null){
                    }*//*
                }

            }

            @RequiresApi(api = Build.VERSION_CODES.M)
            override fun onTabUnselected(tab: TabLayout.Tab) {
                //tab.icon!!.setTint(resources.getColor(R.color.happy_dark_grey, theme))
            }

            override fun onTabReselected(tab: TabLayout.Tab) {}
        })*/

    }


    fun userApiCAll(communityId: Int, periodIndex: Int = 0, isCame: ISLoadingEnum) {
        Log.d("communityId2", "$communityId")
        viewModel.getCommunityRankDetails(
            communityId = communityId, challengeID = challengeID,
            periodIndex = periodIndex,
            rankTypes = "Users",
            isCame = isCame
        )
    }


    fun getSpinnerData() {
        val tribeNames: ArrayList<String> = ArrayList()
        viewModel.tribeLiveData.observe(this) {
            if (it != null) {
                if (it.tribeChallengeDashboard.tribeUserDetails.isNotEmpty()) {
                    tribeLists = it.tribeChallengeDashboard.tribeLists
                    tribeLists.forEach {
                        tribeNames.add(it.communityName)
                    }

                    val tribeSpinnerAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
                        this,
                        R.layout.spinner_item, tribeNames
                    )
                    tribeSpinnerAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item)
                    binding.mainSpinner.adapter = tribeSpinnerAdapter
                }
            }
        }
    }

    private fun apiCalling() {
        //UserChallengeFragment().getCommunityRankDetails(0, challengeID)
        viewModel.tribeLiveData.observe(this, Observer {
            val tribeDetails: GetCommunityRankDetailsTribeDetails =
                it.tribeChallengeDashboard.tribeDetails
            if (tribeDetails != null) {
                //binding.tvCommunityStep.text = "All Tribe Steps: ${tribeDetails.communitySteps}"
                binding.myRank.text = "My Rank: ${tribeDetails.userRank}"
                binding.tribeName.text = tribeDetails.tribeName
                binding.totalRank.text = tribeDetails.tribeRank
                binding.communityGoal.text =
                    "Community Goal: ${tribeDetails.communitySteps} / ${tribeDetails.challengeGoal}"
                binding.tribeSteps.text = "Tribe Steps: ${tribeDetails.tribeSteps}"
            }
        })

        viewModel.userDataLiveData.observe(this, Observer {
            val tribeDetails: GetCommunityRankDetailsTribeDetails =
                it.tribeChallengeDashboard.tribeDetails
            if (tribeDetails != null) {
                //binding.tvCommunityStep.text = "All Tribe Steps: ${tribeDetails.communitySteps}"
                binding.myRank.text = "My Rank: ${tribeDetails.userRank}"
                binding.tribeName.text = tribeDetails.tribeName
                binding.totalRank.text = tribeDetails.tribeRank
                binding.communityGoal.text =
                    "Community Goal: ${tribeDetails.communitySteps} / ${tribeDetails.challengeGoal}"
                binding.tribeSteps.text = "Tribe Steps: ${tribeDetails.tribeSteps}"
            }
        })
    }


    fun getFormatedNumber(count: Long): String {
        if (count < 1000) return "" + count
        val exp = (ln(count.toDouble()) / ln(1000.0)).toInt()
        return String.format("%.1f %c", count / 1000.0.pow(exp.toDouble()), "kMGTPE"[exp - 1])
    }

    private fun setupTabIcons() {
        binding.slidingTabs.getTabAt(0)?.setIcon(R.drawable.level_blue)
        binding.slidingTabs.getTabAt(1)?.setIcon(R.drawable.level_blue)
    }

    private fun setupViewPager() {
        val adapter = SampleFragmentPagerAdapter(supportFragmentManager)
        adapter.addFragment(TribeChallengeFragment(), "Community Leaderboard")
        adapter.addFragment(UserChallengeFragment(), "Tribe Leaderboard")
        binding.viewpager.adapter = adapter
        binding.viewpager.currentItem = tabIndex
    }

    fun viewMoreApi() {
        try {
            CommonUtils.showProgressDialige(this)
            val apiInterface =
                RetrofitHandler.getRetrofitInstance().create(APIInterface::class.java)
            apiInterface.viewAllTribes(SharedPref.getAuthToken())
                .enqueue(object :
                    Callback<ViewMoreTribeResponseModel> {
                    override fun onResponse(
                        call: Call<ViewMoreTribeResponseModel>,
                        response: Response<ViewMoreTribeResponseModel>
                    ) {
                        CommonUtils.dismissDialoge()
                        if (response.code() == 200 && response.isSuccessful) {
                            if (response.body() != null && response.body()!!.data.isNotEmpty()) {
                                viewMorePopUp(response.body()!!.data)
                            }
                        }
                    }

                    override fun onFailure(
                        call: Call<ViewMoreTribeResponseModel>,
                        t: Throwable
                    ) {
                        CommonUtils.dismissDialoge()
                    }

                })

        } catch (e: Exception) {
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
                alertDialog.dismiss()
                communityId = data.communityId.toInt()
                userApiCAll(communityId = data.communityId.toInt(), isCame = ISLoadingEnum.Main)
                binding.viewpager.currentItem = 1
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

}