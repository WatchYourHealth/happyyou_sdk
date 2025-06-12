package com.wyh.happyyousdk.actometer.adapter;

import static com.wyh.happyyousdk.utils.Constants.DAILY;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.actometer.ActOMeterActivity;
import com.wyh.happyyousdk.databinding.ActometerTagsAdapterBinding;
import java.util.List;

public class ActometerDiagnosticTagsAdapter extends RecyclerView.Adapter<ActometerDiagnosticTagsAdapter.MyViewHolder> {

    Context context;
    List<String> tagNameList;
    private int selectedItem = 0;
    private int lastSelected = 0;
    String periodType, activityType;
    int communityId;

    public ActometerDiagnosticTagsAdapter(Context context, List<String> tagNameList, int selectedItem, int communityId, String activityType, String periodType) {
        this.context = context;
        this.tagNameList = tagNameList;
        this.selectedItem = selectedItem;
        this.communityId = communityId;
        this.activityType = activityType;
        this.periodType = periodType;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ActometerTagsAdapterBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.actometer_tags_adapter, parent, false);
        return new MyViewHolder(binding);
    }

    public void updatePeriodType(String periodType) {
        this.periodType = periodType;
    }

    public void updateActivityType(String activityType) {
        this.activityType = activityType;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.binding.tvArticleTag.setText(tagNameList.get(position));
        //        holder.binding.tvArticleTag.setBackground(context.getDrawable(R.drawable.wyh_round_btn_grey));
        int background = (position == selectedItem) ? R.drawable.ic_blue_button_bg : R.drawable.ic_gray_button_bg;

        holder.binding.tvArticleTag.setBackground(context.getDrawable(background));

        holder.binding.tvArticleTag.setOnClickListener(view -> {
            String tagName = activityType;
            if (context instanceof ActOMeterActivity) {
                String diagnosticType;
                if (tagNameList.get(position).equals("Blood Pressure")) {
                    diagnosticType = "Systolic";
                } else {
                    diagnosticType = tagNameList.get(position);
                }
                ((ActOMeterActivity) context).fetchCommunityGraphDataSingle(periodType, tagName, communityId, DAILY, diagnosticType);
            }

            //Save the position of the last selected item
            lastSelected = selectedItem;
            //Save the position of the current selected item
            selectedItem = position;

            //This update the last item selected
            notifyItemChanged(lastSelected);

            //This update the item selected
            notifyItemChanged(selectedItem);
        });

    }

    @Override
    public int getItemCount() {
        return tagNameList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ActometerTagsAdapterBinding binding;

        public MyViewHolder(@NonNull ActometerTagsAdapterBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
