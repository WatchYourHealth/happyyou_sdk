package com.wyh.happyyousdk.quizathon.adapter;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.wyh.happyyousdk.quizathon.QuizathonActivity.sortListsEqual;
import static com.wyh.happyyousdk.utils.CommonUtils.convertListToString;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.databinding.ItemStreakLayoutBinding;
import com.wyh.happyyousdk.databinding.QuizScoreCardItemBinding;
import com.wyh.happyyousdk.model.response.QuestionModel;
import com.wyh.happyyousdk.model.response.playwin.StreakModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class QuizStreakAdapter extends RecyclerView.Adapter<QuizStreakAdapter.MyViewHolder> {

    Context context;
    List<StreakModel> list;

    public QuizStreakAdapter(Context context, List<StreakModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemStreakLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_streak_layout, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        StreakModel data = list.get(position);
        if(!data.isQuizTaken()){
            holder.binding.ivFlameStreak.setAlpha(0.5f);
        }

        holder.binding.tvDay.setText("Day "+(position+1));
        if(position == 0){
            holder.binding.viewStart.setVisibility(GONE);
        }else if(position == (list.size()-1)){
            holder.binding.viewEnd.setVisibility(GONE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ItemStreakLayoutBinding binding;

        public MyViewHolder(@NonNull ItemStreakLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
