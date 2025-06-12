package com.wyh.happyyousdk.quizathon.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.databinding.QuizOptionItemBinding;
import com.wyh.happyyousdk.quizathon.QuizathonActivity;

import java.util.ArrayList;
import java.util.List;


public class QuizathonOptionAdapter extends RecyclerView.Adapter<QuizathonOptionAdapter.MyViewHolder> {

    Context context;
    List<String> list;
    List<String> answers = new ArrayList<>();
    private final QuizathonOptionAdapter.OnItemClickListener listener;
    String selectedPosition;
    String questionType;
    boolean isClicked = false;

    public QuizathonOptionAdapter(Context context, String questionType, List<String> list, List<String> answers, String selectedPosition, QuizathonOptionAdapter.OnItemClickListener listener) {
        this.context = context;
        this.list = list;
        this.answers = answers;
        if (this.answers == null) {
            this.answers = new ArrayList<>();
        }
        this.selectedPosition = selectedPosition;
        this.listener = listener;
        this.questionType = questionType;
        ((QuizathonActivity) context).setAttempted(false);
    }

    @NonNull
    @Override
    public QuizathonOptionAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        QuizOptionItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.quiz_option_item, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull QuizathonOptionAdapter.MyViewHolder holder, int position) {
        String data = list.get(position);
        Log.d("Clicked", "Check is refresh : " + isClicked);
        holder.binding.btnOptionItem.setText(data);
        if (!questionType.equalsIgnoreCase("Multi")) {
            checkSelectedOption(selectedPosition, position, holder);
        } else {
            checkCorrectAnswer(data, holder);
        }
        holder.binding.btnOptionItem.setOnClickListener(v -> {
            //selectedPosition = list.get(position);
            if (questionType.equalsIgnoreCase("Multi")) {
                if (!answers.contains(data.toLowerCase())) {
                    answers.add(data);
                } else {
                    answers.remove(data.toLowerCase());
                }
                notifyDataSetChanged();
            } else {
                Log.d("Clicked", "Check is clicked : " + isClicked);
                if (isClicked) {
                    return;
                }
                isClicked = true;
                selectedPosition = data;
                notifyDataSetChanged();
                listener.onClick(position, data);
                ((QuizathonActivity) context).setAttempted(true);
                new android.os.Handler().postDelayed(() -> {
                    isClicked = false;
                }, 200);
            }
        });

    }

    public List<String> getAnswers() {
        return answers;
    }

    public void setAnswers(List<String> answers) {
        this.answers = answers;
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

    public void checkCorrectAnswer(String option, QuizathonOptionAdapter.MyViewHolder holder) {
        try {
            answers.replaceAll(s -> s.trim().toLowerCase());
            if (answers.contains(option.toLowerCase())) {
                holder.binding.btnOptionItem.setBackgroundResource(R.drawable.pink_rc_bg_8dp);
                //((QuizathonActivity) context).setAttempted(true);
            } else {
                holder.binding.btnOptionItem.setBackgroundResource(R.drawable.blue_rc_bg_8dp);
            }
        } catch (Exception ex) {
            holder.binding.btnOptionItem.setBackgroundResource(R.drawable.blue_rc_bg_8dp);
        }
    }

    public void checkSelectedOption(String selected_index, int position, QuizathonOptionAdapter.MyViewHolder holder) {
        if (selected_index.equalsIgnoreCase(list.get(position))) {
            holder.binding.btnOptionItem.setBackgroundResource(R.drawable.pink_rc_bg_8dp);
            ((QuizathonActivity) context).setAttempted(true);
        } else {
            holder.binding.btnOptionItem.setBackgroundResource(R.drawable.blue_rc_bg_8dp);
        }
    }
}
