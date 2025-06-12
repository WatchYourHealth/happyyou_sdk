package com.wyh.happyyousdk.quizathon.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.wyh.happyyousdk.APIEncryption.APILogs;
import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.SpinWheel.onRewardClick;
import com.wyh.happyyousdk.databinding.ItemRewardCardBinding;
import com.wyh.happyyousdk.databinding.ItemRewardCardscratchBinding;
import com.wyh.happyyousdk.model.response.quizathon.GetQuizScratchListResponse;

import java.util.ArrayList;
import java.util.List;

import dev.skymansandy.scratchcardlayout.listener.ScratchListener;
import dev.skymansandy.scratchcardlayout.ui.ScratchCardLayout;

public class QuizScratchViewAdapter extends BaseAdapter {
    public List<GetQuizScratchListResponse.QuizScratchItem> rewardItemList = new ArrayList<>();
    Context context;
    private int[] res = {
            R.drawable.reward_card_bg_1,
            R.drawable.reward_card_bg_2,
            R.drawable.reward_card_bg_3,
    };

    private int bgCount = -1;

    private String type;
    onRewardClick rewardClick;

    public QuizScratchViewAdapter(List<GetQuizScratchListResponse.QuizScratchItem> rewardItemList, Context context, String type, onRewardClick rewardClick) {
        this.rewardItemList = rewardItemList;
        this.context = context;
        this.type = type;
        this.rewardClick = rewardClick;
        bgCount = -1;
    }

    @Override
    public int getCount() {
        return rewardItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return "";
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ItemRewardCardscratchBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_reward_cardscratch, parent, false);
        String rewardname = rewardItemList.get(position).getRewardTitle();
        if (rewardname.length() > 15) {
            rewardname = rewardItemList.get(position).getRewardTitle().substring(0, 13) + "...";
        }

        binding.tvRewardTitle.setText(rewardname);
        //binding.clParentdata.setBackground(ContextCompat.getDrawable(context, res[position]));
        binding.tvShowRedeemedBanner.setVisibility(View.GONE);
        if (type.equalsIgnoreCase("Active")) {
            binding.clParentInvisible.setVisibility(View.GONE);
        } else if (type.equalsIgnoreCase("Redeemed")) {
            binding.clParentdata.setAlpha(0.3f);
            binding.tvRedeemed.setVisibility(View.INVISIBLE);
            binding.tvShowRedeemedBanner.setVisibility(View.VISIBLE);
            binding.clParentInvisible.setVisibility(View.GONE);
        } else if (type.equalsIgnoreCase("Expired")) {
            binding.clParentInvisible.setVisibility(View.VISIBLE);
            binding.tvRedeemed.setVisibility(View.INVISIBLE);
            binding.clParentdata.setVisibility(View.GONE);
        }
        binding.clParentdata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type.equalsIgnoreCase("Active")) {
                    APILogs.INSTANCE.activityTracker("A_SPIN_MYACTIVITIES_ACTIVETILE_CLICKED", context);
                } else if (type.equalsIgnoreCase("Redeemed")) {
                    APILogs.INSTANCE.activityTracker("A_SPIN_MYACTIVITIES_REDEEMED_CLICKED", context);
                } else if (type.equalsIgnoreCase("Expired")) {
                    APILogs.INSTANCE.activityTracker("A_SPIN_MYACTIVITIES_EXPIREDTILE_CLICKED", context);
                }

                rewardClick.onRewardClick(rewardItemList.get(position), type);

            }
        });


        return binding.getRoot();
    }
}
