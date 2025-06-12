package com.wyh.happyyousdk.quiz.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.databinding.DiaryListItemBinding;
import com.wyh.happyyousdk.databinding.QuizOptionItemBinding;
import com.wyh.happyyousdk.diary.model.DiaryListDataResponse;
import com.wyh.happyyousdk.quiz.QuizActivity;
import com.wyh.happyyousdk.utils.CommonUtils;

import java.util.List;


public class QuizOptionAdapter extends RecyclerView.Adapter<QuizOptionAdapter.MyViewHolder> {

    Context context;
    List<String> list;
    private final QuizOptionAdapter.OnItemClickListener listener;
    String selectedPosition;

    public QuizOptionAdapter(Context context, List<String> list, String selectedPosition, QuizOptionAdapter.OnItemClickListener listener) {
        this.context = context;
        this.list = list;
        this.selectedPosition = selectedPosition;
        this.listener = listener;
        ((QuizActivity) context).setAttempted(false);
    }

    @NonNull
    @Override
    public QuizOptionAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        QuizOptionItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.quiz_option_item, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull QuizOptionAdapter.MyViewHolder holder, int position) {
        String data = list.get(position);
        holder.binding.btnOptionItem.setText(data);
        checkSelectedOption(selectedPosition, position, holder);
        holder.binding.btnOptionItem.setOnClickListener(v -> {
            //selectedPosition = list.get(position);
            notifyDataSetChanged();
            listener.onClick(position, data);
            ((QuizActivity) context).setAttempted(list.size() - 1 != position);
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        QuizOptionItemBinding binding;

        public MyViewHolder(@NonNull QuizOptionItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public interface OnItemClickListener {
        void onClick(int position, String name);
    }

    public void checkSelectedOption(String selected_index, int position, QuizOptionAdapter.MyViewHolder holder) {
        if (selected_index.equalsIgnoreCase(list.get(position))) {
            holder.binding.btnOptionItem.setBackgroundResource(R.drawable.pink_rc_bg_8dp);
            //((QuizActivity) context).setAttempted(true);
            ((QuizActivity) context).setAttempted(list.size() - 1 != position);
        } else {
            holder.binding.btnOptionItem.setBackgroundResource(R.drawable.blue_rc_bg_8dp);
        }
    }
}
