package com.wyh.happyyousdk.Goals.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.wyh.happyyousdk.Goals.Adapters.HIstoryDetailsAdapter
import com.wyh.happyyousdk.R
import com.wyh.happyyousdk.dashboard.NewDashboardActivity
import com.wyh.happyyousdk.databinding.ActivityHistoryDetailBinding

class HistoryDetailActivity : AppCompatActivity() {

    lateinit var historyDetailAdapter : HIstoryDetailsAdapter
    lateinit var binding : ActivityHistoryDetailBinding
    var comingFrom  = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this,R.layout.activity_history_detail)

        binding.commonToolBar.llBack.setOnClickListener {
            finish()
        }

        binding.ivHome.setOnClickListener {
            val intent = Intent(this, NewDashboardActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }

        comingFrom = intent.getStringExtra("comingFrom").toString()

        if(comingFrom.equals("delete",true)){
            binding.historyDetailGoalsDetailsRV.layoutManager = GridLayoutManager(this,3)
            binding.historyDetailGoalsDetailsRV.hasFixedSize()
            historyDetailAdapter = HIstoryDetailsAdapter(this,GoalHistory.deleteList,comingFrom)
            binding.historyDetailGoalsDetailsRV.adapter = historyDetailAdapter
        }else if (comingFrom.equals("completed",true)){
            binding.historyDetailGoalsDetailsRV.layoutManager = GridLayoutManager(this,3)
            binding.historyDetailGoalsDetailsRV.hasFixedSize()
            historyDetailAdapter = HIstoryDetailsAdapter(this,GoalHistory.completedList,comingFrom)
            binding.historyDetailGoalsDetailsRV.adapter = historyDetailAdapter

        }else {
            binding.historyDetailGoalsDetailsRV.layoutManager = GridLayoutManager(this,3)
            binding.historyDetailGoalsDetailsRV.hasFixedSize()
            historyDetailAdapter = HIstoryDetailsAdapter(this,GoalHistory.pendingList,comingFrom)
            binding.historyDetailGoalsDetailsRV.adapter = historyDetailAdapter

        }
    }

}