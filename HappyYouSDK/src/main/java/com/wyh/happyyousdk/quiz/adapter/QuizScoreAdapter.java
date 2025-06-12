package com.wyh.happyyousdk.quiz.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.databinding.QuizScoreCardItemBinding;
import com.wyh.happyyousdk.model.response.QuestionModel;

import java.util.List;


public class QuizScoreAdapter extends RecyclerView.Adapter<QuizScoreAdapter.MyViewHolder> {

    Context context;
    List<QuestionModel> list;
    int selectedPosition = -1;

    public QuizScoreAdapter(Context context, List<QuestionModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public QuizScoreAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        QuizScoreCardItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.quiz_score_card_item, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull QuizScoreAdapter.MyViewHolder holder, int position) {
        QuestionModel data = list.get(position);
        String userAns = "";
        String yourAns = "NA";
        String question = "Q."+(position+1)+ " " + data.getQuestion();

        if(data.getUserAns() != null){
            userAns = data.getUserAns().toString();
            yourAns = userAns;
        }

        holder.binding.scoreYourAnswer.setText(yourAns);
        holder.binding.scoreQuestion.setText(question);
        holder.binding.scoreAnswer.setText(data.getAnswer());
        holder.binding.answerExplain.setText(data.getExplanation());

        if(data.getAnswer().equals(yourAns)){
            holder.binding.cvScoreItem.setCardBackgroundColor(context.getResources().getColor(R.color.score_green_light));
            holder.binding.scoreYourAnswer.setTextColor(context.getResources().getColor(R.color.score_green_dark));
        }else{
            holder.binding.cvScoreItem.setCardBackgroundColor(context.getResources().getColor(R.color.score_red_light));
            holder.binding.scoreYourAnswer.setTextColor(context.getResources().getColor(R.color.score_red_dark));
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        QuizScoreCardItemBinding binding;

        public MyViewHolder(@NonNull QuizScoreCardItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
