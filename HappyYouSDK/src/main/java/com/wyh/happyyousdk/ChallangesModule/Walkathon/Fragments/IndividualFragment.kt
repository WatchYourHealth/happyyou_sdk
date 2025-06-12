package com.wyh.happyyousdk.ChallangesModule.Walkathon.Fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.wyh.happyyousdk.APIEncryption.APILogs
import com.wyh.happyyousdk.ChallangesModule.Walkathon.Adapters.BranchItemAdapter
import com.wyh.happyyousdk.ChallangesModule.Walkathon.Adapters.IndividualItemAdapter
import com.wyh.happyyousdk.R
import com.wyh.happyyousdk.databinding.IndividualFragmentBinding
import com.wyh.happyyousdk.model.response.WinnerUserStep
import com.wyh.happyyousdk.profile.ConnectionListModel


class IndividualFragment(private val data: ArrayList<WinnerUserStep>) : Fragment() {

    lateinit var binding: IndividualFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.individual_fragment, container, false)
        val adapter = IndividualItemAdapter(data)
        binding.rv.adapter = adapter

        activity?.let { APILogs.activityTracker("A_WALKATHON_INDIVIDUAL_WINNER", it) }

        return binding.root
    }

}