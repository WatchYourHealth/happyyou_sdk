package com.wyh.happyyousdk.common.adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

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
public class ClaimReClaimAdapterNew extends RecyclerView.Adapter<ClaimReClaimAdapterNew.ViewHolder> {
    private Context context;
    private List<ClaimReClaimRewardModel> fileShareResps;
    private int[] res;
    private int bgCount = -1;
    private APIInterface apiInterfaceWyh;
    onRewardClick rewardClick;

    public ClaimReClaimAdapterNew(Context context, List<ClaimReClaimRewardModel> policyDetailsList, onRewardClick onRewardClick) {
        this.rewardClick = onRewardClick;
        this.context = context;
        this.fileShareResps = policyDetailsList;
        apiInterfaceWyh = RetrofitHandler.apiInterface();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AdapterClaimReclaimBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.adapter_claim_reclaim,
                parent,
                false
        );
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        res = new int[]{
                R.drawable.ic_light_pink_button_bg,
                R.drawable.ic_light_blue_button_bg,
                R.drawable.ic_light_orange_button_bg,
                R.drawable.ic_light_blue_button_bg,
                R.drawable.ic_light_orange_button_bg,
                R.drawable.ic_light_pink_button_bg
        };

        ClaimReClaimRewardModel data = fileShareResps.get(position);

        if (bgCount == res.length - 1) {
            bgCount = 0;
        } else {
            bgCount++;
        }
        holder.binding.llMain.setBackground(ContextCompat.getDrawable(context, res[bgCount]));
        holder.binding.tvTitle1.setText(data.getQuizTitle());
        holder.binding.tvTitle3.setText(data.getQuizDesc());

        if (data.isClaim()) {
            holder.binding.tvTitle2.setText("Claim");
        } else {
            holder.binding.tvTitle2.setText("Reclaim");
        }

        Glide.with(context)
                .load(data.getQuizLogo())
                .error(R.drawable.ic_image_holder_reward)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(holder.binding.ivRewardIcon);

        holder.binding.llMain.setOnClickListener(view -> {
            ClaimReClaimDialog claimReclaimDialog = new ClaimReClaimDialog(
                    context,
                    data.getTransId(),
                    "" + data.getReclaimBurnValue(),
                    data.getRewardTitle(),
                    data.getRewardTitleIcon(),
                    data.getRewardDescription(),
                    data.isClaim(),
                    data,rewardClick
            );
            claimReclaimDialog.show();
        });
    }

    @Override
    public int getItemCount() {
        return fileShareResps.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        AdapterClaimReclaimBinding binding;

        public ViewHolder(@NonNull AdapterClaimReclaimBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
