package com.wyh.happyyousdk.common.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.wyh.happyyousdk.APIEncryption.APIInterface;
import com.wyh.happyyousdk.APIEncryption.RetrofitHandler;
import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.SpinWheel.onRewardClick;
import com.wyh.happyyousdk.databinding.AdapterClaimReclaimBinding;
import com.wyh.happyyousdk.model.response.playwin.ClaimReClaimRewardModel;
import com.wyh.happyyousdk.quizathon.dialog.ClaimReClaimDialog;

import java.util.List;

public class ClaimReClaimAdapter extends BaseAdapter {
    Context context;
    List<ClaimReClaimRewardModel> fileShareResps;
    int[] res;
    int bgCount = -1;
    boolean isAllItemsLoaded = false;
    APIInterface apiInterfaceWyh;
    onRewardClick rewardClick;
    public ClaimReClaimAdapter(Context context, List<ClaimReClaimRewardModel> policyDetailsList, onRewardClick rewardClick) {
        this.context = context;
        this.rewardClick = rewardClick;
        this.fileShareResps = policyDetailsList;
        apiInterfaceWyh = RetrofitHandler.apiInterface();
    }

    @Override
    public int getCount() {
        return fileShareResps.size();
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
        res = new int[]{R.drawable.ic_light_pink_button_bg, R.drawable.ic_light_blue_button_bg, R.drawable.ic_light_orange_button_bg,
                R.drawable.ic_light_blue_button_bg, R.drawable.ic_light_orange_button_bg, R.drawable.ic_light_pink_button_bg
        };
        @SuppressLint("ViewHolder") AdapterClaimReclaimBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.adapter_claim_reclaim, parent, false);
        ClaimReClaimRewardModel data = fileShareResps.get(position);

        if (bgCount == res.length - 1) {
            bgCount = 0;
        } else {
            bgCount++;
        }
        binding.llMain.setBackground(ContextCompat.getDrawable(context, res[bgCount]));
        binding.tvTitle1.setText(data.getQuizTitle());
        binding.tvTitle3.setText(data.getQuizDesc());
        if (data.isClaim())
        {
            binding.tvTitle2.setText("Claim");

        }
        else
        {
            binding.tvTitle2.setText("Reclaim");
        }
        Glide.with(context)
                .load(data.getQuizLogo())
                .error(R.drawable.ic_image_holder_reward)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(binding.ivRewardIcon);
        binding.llMain.setOnClickListener(view -> {

            ClaimReClaimDialog claimReclaimDialog = new ClaimReClaimDialog(
                    context,
                    data.getTransId(),
                    "" + data.getReclaimBurnValue(),
                    data.getRewardTitle(),
                    data.getRewardTitleIcon(),
                    data.getRewardDescription(),
                    data.isClaim(),
                    data,rewardClick);
            claimReclaimDialog.show();
        });
        if (fileShareResps.size() - 1 == position)
            isAllItemsLoaded = true;
        return binding.getRoot();
    }
}
