package com.wyh.happyyousdk.ChallangesModule.Activities

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wyh.happyyousdk.ChallangesModule.Adapter.TribeMemberAdapter
import com.wyh.happyyousdk.R
import com.wyh.happyyousdk.databinding.FragmentTribeChallengeBinding
import com.wyh.happyyousdk.utils.CommonUtils

class TribeChallengeFragment : Fragment() {
    private lateinit var binding: FragmentTribeChallengeBinding
    private lateinit var viewModel: ChallengeViewModel
    lateinit var memberAdapter: TribeMemberAdapter
    private lateinit var mContext: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentTribeChallengeBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity())[ChallengeViewModel::class.java]
        val challengeID = (activity as TribeChallengeDashboard).challengeID

        mContext = requireContext()
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(activity)
        binding.rvMembers.layoutManager = layoutManager
        memberAdapter = TribeMemberAdapter(mContext)
        binding.rvMembers.adapter = memberAdapter

        viewModel.getCommunityRankDetails(communityId = 0, challengeID = challengeID, periodIndex = 0, rankTypes = "Tribe", isCame = ISLoadingEnum.Main)

        getObserveData()

        binding.btnViewMore.setOnClickListener {
            (activity as TribeChallengeDashboard).viewMoreApi()
        }

        return binding.root
    }


    private fun setProgressbar(isLoading: Boolean) {
        if (isLoading) {
            binding.pBar.visibility = View.VISIBLE
            binding.scrollView.visibility = View.GONE
        } else {
            binding.scrollView.visibility = View.VISIBLE
            binding.pBar.visibility = View.GONE
        }
    }


    private fun getObserveData() {

        viewModel.isLoadingUserData.observe(requireActivity(), Observer {
            setProgressbar(it)
        })

        viewModel.tribeLiveData.observe(requireActivity(), Observer {
            if (it != null) {
                if (it.tribeChallengeDashboard.tribeUserDetails.isNotEmpty()) {
                    var list = it.tribeChallengeDashboard.tribeUserDetails
                    if(list.size > 5){
                        list = list.subList(0, 5)
                        binding.btnViewMore.visibility = View.VISIBLE
                    }else{
                        binding.btnViewMore.visibility = View.GONE
                    }
                    memberAdapter.submitList(list)
                }
            }
        })

    }

}