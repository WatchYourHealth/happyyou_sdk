package com.wyh.happyyousdk.absorb.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.absorb.HealthHacksActivity;
import com.wyh.happyyousdk.absorb.HealthTvListingActivity;
import com.wyh.happyyousdk.absorb.QuickReadDashboard;
import com.wyh.happyyousdk.databinding.AbsorbTagsAdapterBinding;
import com.wyh.happyyousdk.model.response.absorb.GetDashboardDataResponse;

import java.util.List;
import java.util.Objects;

public class HealthHacksTagsAdapter extends RecyclerView.Adapter<HealthHacksTagsAdapter.MyViewHolder> {

    Context context;
    List<GetDashboardDataResponse.Data.TagName> tagNameList;
    String selectedTagPosition = "";
    public int selectedItem = 0;
    int lastSelected = 0;

    public HealthHacksTagsAdapter(Context context, List<GetDashboardDataResponse.Data.TagName> tagNameList, String selectedTagPosition, int selectedItem) {
        this.context = context;
        this.tagNameList = tagNameList;
        this.selectedTagPosition = selectedTagPosition;
        this.selectedItem = selectedItem;
    }

    public HealthHacksTagsAdapter(Context context, List<GetDashboardDataResponse.Data.TagName> tagNameList) {
        this.context = context;
        this.tagNameList = tagNameList;
    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AbsorbTagsAdapterBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.absorb_tags_adapter, parent, false);
        return new MyViewHolder(binding);
    }

    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        if(Objects.equals(tagNameList.get(position).getTagName(), "Happy Footprint")){
            holder.binding.tvArticleTag.setText("Happy Footprints");
        }else if(Objects.equals(tagNameList.get(position).getTagName(), "Calories")){
            holder.binding.tvArticleTag.setText("Cal-Count");
        }else if(Objects.equals(tagNameList.get(position).getTagName(), "Meditation")){
            holder.binding.tvArticleTag.setText("Zen Zone");
        }else{
            holder.binding.tvArticleTag.setText(tagNameList.get(position).getTagName());
        }

        int background = (position == selectedItem) ? R.drawable.ic_blue_button_bg : R.drawable.ic_gray_button_bg;
        holder.binding.tvArticleTag.setBackground(context.getDrawable(background));



        holder.binding.tvArticleTag.setOnClickListener(view -> {
            if (context instanceof HealthHacksActivity) {
                ((HealthHacksActivity) context).getAbsorbDashboard(position == 0 ? "" : tagNameList.get(position).getTagName(),"");
            } else if (context instanceof QuickReadDashboard) {
                ((QuickReadDashboard) context).getQuickReadDashboardAPI(position == 0 ? "" : tagNameList.get(position).getTagName());
            }else if (context instanceof HealthTvListingActivity){
                ((HealthTvListingActivity) context).getAbsorbDashboard(position == 0 ? "" : tagNameList.get(position).getTagName());
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
        AbsorbTagsAdapterBinding binding;

        public MyViewHolder(@NonNull AbsorbTagsAdapterBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void selectedTags(String tagName, MyViewHolder viewHolder){
        try{
            for(int i=0 ; i < tagNameList.size() ; i++){
                if(tagNameList.get(i).getTagName().equalsIgnoreCase(tagName)){
                    viewHolder.binding.tvArticleTag.setBackground(context.getDrawable(R.drawable.ic_blue_button_bg));
                }else{
                    viewHolder.binding.tvArticleTag.setBackground(context.getDrawable(R.drawable.ic_gray_button_bg));

                }
            }
        }catch (Exception e){
            Log.e("HappyYou", "Error occurred", e);
        }

    }
}
