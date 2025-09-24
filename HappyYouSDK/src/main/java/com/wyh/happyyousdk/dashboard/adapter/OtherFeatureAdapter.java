package com.wyh.happyyousdk.dashboard.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.databinding.FeatureLayoutBinding;
import com.wyh.happyyousdk.databinding.OthersFragmentLayoutBinding;

import java.util.ArrayList;

public class OtherFeatureAdapter extends RecyclerView.Adapter<OtherFeatureAdapter.ViewHolder> {

    Context context;
    ArrayList<String> featureList;
    FeatureClick featureClick;
    int[] res;
    int bgCount = -1;

    ArrayList<Integer> icons = new ArrayList<>();



    public OtherFeatureAdapter(Context context, ArrayList<String> featureList, FeatureClick featureClick) {
        this.context = context;
        this.featureList = featureList;
        this.featureClick = featureClick;
    }

    @NonNull
    @Override
    public OtherFeatureAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        FeatureLayoutBinding view = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.feature_layout,parent,false);
        icons.add(R.drawable.icons_health_locker);
        icons.add(R.drawable.icons_digi_coach);
        icons.add(R.drawable.icons_challenges);
        icons.add(R.drawable.icons_unwind);
        icons.add(R.drawable.webinar_icon);
        icons.add(R.drawable.spin_nd_wheel);

        res = new int[]{R.drawable.ic_light_pink_button_bg, R.drawable.ic_light_blue_button_bg, R.drawable.ic_light_orange_button_bg,
                R.drawable.ic_light_blue_button_bg, R.drawable.ic_light_orange_button_bg, R.drawable.ic_light_pink_button_bg,
                R.drawable.ic_light_orange_button_bg, R.drawable.ic_light_pink_button_bg, R.drawable.ic_light_blue_button_bg};

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OtherFeatureAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {


        if (bgCount == res.length - 1) {
            bgCount = 0;
        } else {
            bgCount++;
        }

        holder.binding.tvFeatureName.setText(featureList.get(position));
        holder.binding.rlMainLayout.setBackground(context.getResources().getDrawable(res[bgCount]));
        holder.binding.rlMainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                featureClick.onClick(featureList.get(position));
            }
        });

        holder.binding.ivAddFamily.setImageDrawable(context.getDrawable(icons.get(position)));
    }

    @Override
    public int getItemCount() {
        return featureList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        FeatureLayoutBinding binding;


        public ViewHolder(@NonNull FeatureLayoutBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }
    }

    public interface FeatureClick {
         void onClick(String featureName);
    }
}
