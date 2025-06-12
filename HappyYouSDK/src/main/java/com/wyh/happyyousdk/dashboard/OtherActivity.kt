package com.wyh.happyyousdk.dashboard

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.wyh.happyyousdk.APIEncryption.APILogs
import com.wyh.happyyousdk.R
import com.wyh.happyyousdk.dashboard.Fragments.BadgesFragment
import com.wyh.happyyousdk.dashboard.Fragments.StampsFragment
import com.wyh.happyyousdk.dashboard.Fragments.VoucherFragments
import com.wyh.happyyousdk.dashboard.helper.NewDashboardHelper
import com.wyh.happyyousdk.databinding.ActivityOtherBinding
import com.wyh.happyyousdk.profile.DialogActivity
import com.wyh.happyyousdk.profile.Fragments.DareReferralFragment
import com.wyh.happyyousdk.profile.Fragments.ReferenceFragment
import com.wyh.happyyousdk.profile.Fragments.TribeFragment
import com.wyh.happyyousdk.rewards.SampleFragmentPagerAdapter

class OtherActivity : AppCompatActivity() {


    lateinit var binding: ActivityOtherBinding
    var stamp = ""
    var voucher = ""
    var badges = ""
    var comingFrom = ""
    var currentIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_other)


        if (intent.getStringExtra("stamp") != null) {
            stamp = intent.getStringExtra("stamp").toString()
        }

        if (intent.getStringExtra("voucher") != null) {
            voucher = intent.getStringExtra("voucher").toString()
        }

        if (intent.getStringExtra("badges") != null) {
            badges = intent.getStringExtra("badges").toString()
        }

        if (intent.getStringExtra("comingFrom") != null) {
            comingFrom = intent.getStringExtra("comingFrom").toString()
            if (comingFrom.equals("Offers", ignoreCase = true) || comingFrom.equals("Voucher", ignoreCase = true)) {
                comingFrom = "Voucher"
                if (intent.getIntExtra("current", 0) != 0) {
                    currentIndex = intent.getIntExtra("current", 0)
                }
            } else if (comingFrom.equals("Badge", ignoreCase = true)) {
                if (intent.getIntExtra("current", 0) != 0) {
                    currentIndex = intent.getIntExtra("current", 0)
                }
            }


        }

        if (comingFrom.equals("spinner", true)) {
            currentIndex = 2
        }

        setupViewPager(binding.otherViewpager)
        binding.otherTabs.setupWithViewPager(binding.otherViewpager)

        binding.otherCancelImg.setOnClickListener {
            APILogs.activityTracker("A_DB_Other_Close", this)
            finish()
        }

        binding.otherTabs.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab!!.text!! == "Stamps") {
                    APILogs.activityTracker("A_DB_Other_Stamp", this@OtherActivity)
                } else if (tab!!.text!! == "Badges") {
                    APILogs.activityTracker("A_DB_Other_Badges", this@OtherActivity)

                } else {
                    APILogs.activityTracker("A_DB_Other_Voucher", this@OtherActivity)

                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })
    }


    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = SampleFragmentPagerAdapter(supportFragmentManager)
        adapter.addFragment(StampsFragment(this, NewDashboardHelper.stamList, stamp), "Stamps")
        adapter.addFragment(BadgesFragment(this, NewDashboardHelper.badgeList, badges), "Badges")
        adapter.addFragment(
            VoucherFragments(this, NewDashboardHelper.voucherList, voucher),
            "Voucher"
        )
        viewPager.adapter = adapter
        viewPager.currentItem = currentIndex
    }
}