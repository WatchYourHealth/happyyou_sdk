package com.wyh.happyyousdk.dashboard.Fragments

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
import com.wyh.happyyousdk.ChallangesModule.Activities.BadgesActivity
import com.wyh.happyyousdk.R
import com.wyh.happyyousdk.dashboard.adapter.BadgesAdapter
import com.wyh.happyyousdk.databinding.BadgesLayoutBinding
import com.wyh.happyyousdk.model.response.badgeListData

class BadgesFragment(val mcontext: Context,val list: ArrayList<badgeListData>, val badgesCount: String) : Fragment() {

    lateinit var binding : BadgesLayoutBinding
    lateinit var adapter : BadgesAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.badges_layout,container,false)
        binding.badgeCountTv.text = "Your Badge Count is $badgesCount"

        if(list.isNotEmpty()){
            binding.badgesNoDataTv.visibility = View.GONE
            binding.badgesParentLayout.visibility = View.VISIBLE
            if(list.size < 25){
                binding.badgesViewMoreBtn.visibility = View.GONE
            }else{
                binding.badgesViewMoreBtn.visibility = View.VISIBLE
            }
        }else{
            binding.badgesNoDataTv.visibility = View.VISIBLE
            binding.badgesParentLayout.visibility = View.GONE
            binding.badgesViewMoreBtn.visibility = View.GONE
        }



        binding.badgesCancelBtn.setOnClickListener {
            (mcontext as Activity).finish()
        }

        binding.badgesViewMoreBtn.setOnClickListener {
            mcontext.startActivity(Intent(context, BadgesActivity::class.java))

        }

        adapter = BadgesAdapter(mcontext,list)
        binding.badgesRv.layoutManager = LinearLayoutManager(mcontext)
        binding.badgesRv.hasFixedSize()
        binding.badgesRv.adapter = adapter
        return binding.root
    }


}