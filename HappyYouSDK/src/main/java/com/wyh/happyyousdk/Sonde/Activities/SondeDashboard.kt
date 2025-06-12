package com.wyh.happyyousdk.Sonde.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.wyh.happyyousdk.R
import com.wyh.happyyousdk.Sonde.Adapters.SondeDashboardAdapter
import com.wyh.happyyousdk.Sonde.Interface.ItemClick

import com.wyh.happyyousdk.dashboard.NewDashboardActivity
import com.wyh.happyyousdk.databinding.ActivitySondeDashboardBinding
import com.wyh.happyyousdk.utils.SharedPref

class SondeDashboard : AppCompatActivity(), ItemClick {

    lateinit var binding : ActivitySondeDashboardBinding
    var sondeItem = arrayOf("Mental Wellbeing","Respiratory")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SharedPref.init(this)

        binding = DataBindingUtil.setContentView(this,R.layout.activity_sonde_dashboard)
        setContentView(binding.root)

        init()
    }

    private fun init(){
        binding.includeToolbar.llBack.setOnClickListener {
            finish()
        }

        binding.sondeDashboardIvHome.setOnClickListener {
            val intent = Intent(this, NewDashboardActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }

        binding.sondeDashboardRecycler.layoutManager = GridLayoutManager(this,3)
        binding.sondeDashboardRecycler.hasFixedSize()
        val adapter = SondeDashboardAdapter(this,sondeItem,this)
        binding.sondeDashboardRecycler.adapter = adapter
    }

    override fun onDashBoardItemClick(title: String) {
        if(title.equals("Mental Wellbeing",true)){
            startActivity(Intent(this,RespiratoryCheck::class.java)
                .putExtra("comingFrom","metalFitness"))
        }else {
            startActivity(Intent(this,RespiratoryCheck::class.java)
                .putExtra("comingFrom","respiratory"))
        }
    }


}