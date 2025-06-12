package com.wyh.happyyousdk.quizathon.adapter;

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
import com.wyh.happyyousdk.databinding.QuizScoreCardItemBinding;
import com.wyh.happyyousdk.model.response.QuestionModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class QuizScoreAdapter extends RecyclerView.Adapter<QuizScoreAdapter.MyViewHolder> {

    Context context;
    List<QuestionModel> list;
    int selectedPosition = -1;
    boolean isFeedBack=false;

    public QuizScoreAdapter(Context context, List<QuestionModel> list,boolean isFeedBack) {
        this.context = context;
        this.list = list;
        this.isFeedBack = isFeedBack;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        QuizScoreCardItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.quiz_score_card_item, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        QuestionModel data = list.get(position);
        String userAns = "";
        String yourAns = "NA";
        String question = "Q." + (position + 1) + " " + data.getQuestion();

        if(isFeedBack){
            holder.binding.scoreAnswer.setVisibility(View.GONE);
            holder.binding.layoutScoreAnswer.setVisibility(View.GONE);
            holder.binding.explainScoreAnswer.setVisibility(View.GONE);
        }

        if (data.getUserAns() != null) {
            userAns = data.getUserAns().toString();
            yourAns = userAns;
        }

        holder.binding.scoreYourAnswer.setText(yourAns);
        holder.binding.scoreQuestion.setText(question);
        holder.binding.scoreAnswer.setText(data.getAnswer());
        holder.binding.answerExplain.setText(data.getExplanation());
        if (data.getQuestionType().equalsIgnoreCase("Multi")) {
            List<String> correctAnswer = new ArrayList<>();
            if (data.getAnswer() != null && data.getAnswer().contains(",")) {
                correctAnswer = Arrays.asList(data.getAnswer().split(","));
            } else {
                correctAnswer.add(data.getAnswer());
            }
            data.setAnswer(convertListToString(sortListsEqual(correctAnswer)));
        }
        if(!isFeedBack) {
            if(data.isExactMatch() || !data.getQuestionType().equalsIgnoreCase("TextArea") && !data.getQuestionType().equalsIgnoreCase("EditText")) {
                if (data.getAnswer().equalsIgnoreCase(yourAns)) {
                    holder.binding.cvScoreItem.setCardBackgroundColor(context.getResources().getColor(R.color.score_green_light));
                    holder.binding.scoreYourAnswer.setTextColor(context.getResources().getColor(R.color.score_green_dark));
                } else {
                    holder.binding.cvScoreItem.setCardBackgroundColor(context.getResources().getColor(R.color.score_red_light));
                    holder.binding.scoreYourAnswer.setTextColor(context.getResources().getColor(R.color.score_red_dark));
                }
            }else{
                if(data.getKeywords() != null && data.getKeywords().size() > 0) {
                    if (areAllStringsPresent(data.getKeywords(), yourAns)) {
                        holder.binding.cvScoreItem.setCardBackgroundColor(context.getResources().getColor(R.color.score_green_light));
                        holder.binding.scoreYourAnswer.setTextColor(context.getResources().getColor(R.color.score_green_dark));
                    } else {
                        holder.binding.cvScoreItem.setCardBackgroundColor(context.getResources().getColor(R.color.score_red_light));
                        holder.binding.scoreYourAnswer.setTextColor(context.getResources().getColor(R.color.score_red_dark));
                    }
                }else{
                    if (data.getAnswer().equalsIgnoreCase(yourAns)) {
                        holder.binding.cvScoreItem.setCardBackgroundColor(context.getResources().getColor(R.color.score_green_light));
                        holder.binding.scoreYourAnswer.setTextColor(context.getResources().getColor(R.color.score_green_dark));
                    } else {
                        holder.binding.cvScoreItem.setCardBackgroundColor(context.getResources().getColor(R.color.score_red_light));
                        holder.binding.scoreYourAnswer.setTextColor(context.getResources().getColor(R.color.score_red_dark));
                    }
                }
            }
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

    public boolean areAllStringsPresent(List<String> keywords, String input) {
        if (keywords == null || input == null || keywords.isEmpty()) {
            return false;
        }

        String trimmedInput = input.trim().toLowerCase(); // Trim and normalize case

        for (String keyword : keywords) {
            if (keyword == null || keyword.trim().isEmpty()) {
                continue; // Skip null or empty keywords
            }

            if (!trimmedInput.contains(keyword.trim().toLowerCase())) {
                return false; // If any keyword is not found
            }
        }

        return true;
    }

}
