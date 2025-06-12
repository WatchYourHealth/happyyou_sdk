package com.wyh.happyyousdk.dashboard.Fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.wyh.happyyousdk.R
import com.wyh.happyyousdk.dashboard.adapter.StampsAdapter
import com.wyh.happyyousdk.dashboard.helper.NewDashboardHelper
import com.wyh.happyyousdk.databinding.StampsLayoutBinding
import com.wyh.happyyousdk.model.response.StampListData
import com.wyh.happyyousdk.rewards.RewardsActivity
import com.wyh.happyyousdk.rewards.RewardsHistoryActivity

class StampsFragment(val mcontext: Context,val list: ArrayList<StampListData>,val stampsCount: String) : Fragment() {


    lateinit var binding : StampsLayoutBinding
    lateinit var adapter : StampsAdapter

    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.stamps_layout,container,false)



        if(list.isNotEmpty()){
            binding.stampsNoDataTv.visibility = View.GONE
            binding.stampsParentLayout.visibility = View.VISIBLE
            if(list.size < 25){
                binding.stampsViewMoreBtn.visibility = View.GONE
            }else{
                binding.stampsViewMoreBtn.visibility = View.VISIBLE

            }
        }else{
            binding.stampsNoDataTv.visibility = View.VISIBLE
            binding.stampsParentLayout.visibility = View.GONE
            binding.stampsViewMoreBtn.visibility = View.GONE

        }


        binding.stampsCancelBtn.setOnClickListener {
            (mcontext as Activity).finish()

        }

        binding.stampsViewMoreBtn.setOnClickListener {
            val intent = Intent(context, RewardsHistoryActivity::class.java)
            intent.putExtra("comingFrom","stamps")

            mcontext.startActivity(intent)
        }

        //binding.stampCountTv.text = "Your Stamps count is $stampsCount"

        binding.earnedStampsTv.text = "Earned Stamps: ${NewDashboardHelper.stampCount}"
        binding.availableStampsTv.text = "Available Stamps: ${NewDashboardHelper.availableStamps}"
        adapter = StampsAdapter(mcontext,list)
        binding.stampsRv.layoutManager = LinearLayoutManager(mcontext)
        binding.stampsRv.hasFixedSize()
        binding.stampsRv.adapter = adapter

        return binding.root
    }
}