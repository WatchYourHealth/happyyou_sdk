package com.wyh.happyyousdk.ChallangesModule.Walkathon.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.wyh.happyyousdk.APIEncryption.APILogs
import com.wyh.happyyousdk.ChallangesModule.Walkathon.Adapters.BranchItemAdapter
import com.wyh.happyyousdk.R
import com.wyh.happyyousdk.databinding.BranchFragmentBinding
import com.wyh.happyyousdk.model.response.WinnerBranchStep


class BranchFragment(private val data: ArrayList<WinnerBranchStep>) : Fragment() {
    lateinit var binding: BranchFragmentBinding;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.branch_fragment, container, false)

        activity?.let { APILogs.activityTracker("A_WALKATHON_BRANCH_WINNER", it) }
        val adapter = BranchItemAdapter(data)
        binding.rv.adapter = adapter


        return binding.root
    }

}