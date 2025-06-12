package com.wyh.happyyousdk.quiz.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.databinding.ItemQuizRewardHistoryBinding;
import com.wyh.happyyousdk.model.response.quiz.TriviaHistoryData;
import com.wyh.happyyousdk.quiz.QuizActivity;
import com.wyh.happyyousdk.quiz.QuizScoreActivity;
import com.wyh.happyyousdk.utils.dialog.PostSpinDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class QuizTriviaHistoryAdapter extends RecyclerView.Adapter<QuizTriviaHistoryAdapter.QuizTriviaHistoryViewholder> {
    List<TriviaHistoryData> triviaHistoryDataList = new ArrayList<>();
    Context context;

    public QuizTriviaHistoryAdapter(List<TriviaHistoryData> triviaHistoryDataList) {
        this.triviaHistoryDataList = triviaHistoryDataList;
    }

    @NonNull
    @Override
    public QuizTriviaHistoryViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        ItemQuizRewardHistoryBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_quiz_reward_history, parent, false);
        return new QuizTriviaHistoryViewholder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull QuizTriviaHistoryViewholder holder, int position) {
        TriviaHistoryData triviaHistoryData = triviaHistoryDataList.get(position);
        holder.binding.tvCardTitle.setText(triviaHistoryData.getActivityTitle() != null ? triviaHistoryData.getActivityTitle() : "");
        String assignedOn = triviaHistoryData.getAssignedOn();
        assignedOn = formatDateString(assignedOn, "dd/MM/yyyy");
//        if (assignedOn != null && assignedOn != "") {
//            String[] date = assignedOn.split(" ");
//            if (date.length > 1) {
//                assignedOn = date[0];
//            } else {
//                assignedOn = "";
//            }
//        }
        String completedOn = triviaHistoryData.getCompletedOn();
        completedOn = formatDateString(completedOn, "dd/MM/yyyy");
//        if (completedOn != null && completedOn != "") {
//            String[] date = completedOn.split(" ");
//            if (date.length > 1) {
//                completedOn = date[0];
//            } else {
//                completedOn = "";
//            }
//        }

        holder.binding.tvStatus.setText(triviaHistoryData.getStatus());
        holder.binding.tvAssigned.setText(assignedOn);
        holder.binding.tvCompleted.setText(completedOn);
        holder.binding.tvGetTriviaScore.setText("" + triviaHistoryData.getTriviaScore() + "/" + triviaHistoryData.getTriviaTotalScore());
        holder.binding.tvVoucherDetails.setText(triviaHistoryData.getRewardTitle() != null ? triviaHistoryData.getRewardTitle() : "");
        if (!triviaHistoryData.getStatus().equalsIgnoreCase("expired")) {

            if (triviaHistoryData.getRewardType() != null) {
                holder.binding.clRewardEarnedSection.setVisibility(View.VISIBLE);
            } else {
                holder.binding.clRewardEarnedSection.setVisibility(View.GONE);
            }
        } else {
            holder.binding.clRewardEarnedSection.setVisibility(View.GONE);
            holder.binding.btnViewQuiz.setVisibility(View.GONE);
        }

        holder.binding.btnViewYourRewards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostSpinDialog.INSTANCE.getRewardRedirections(context, triviaHistoryData.getRewardType());
            }
        });

        holder.binding.btnViewQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, QuizScoreActivity.class);
                intent.putExtra("data", triviaHistoryData.getAnswerJson());
                intent.putExtra("your_score", triviaHistoryData.getTriviaScore());
                intent.putExtra("total_score", triviaHistoryData.getTriviaTotalScore());
                intent.putExtra("CategoryName", triviaHistoryData.getCategory());
                intent.putExtra("isFromAdapter", true);
                intent.putExtra("comingFrom", "quiz");
                intent.putExtra("rewardText", "");

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return triviaHistoryDataList.size();
    }

    public class QuizTriviaHistoryViewholder extends RecyclerView.ViewHolder {
        ItemQuizRewardHistoryBinding binding;

        public QuizTriviaHistoryViewholder(@NonNull ItemQuizRewardHistoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public String formatDateString(String dateString, String desiredFormat) {
        try {
            // Parse the input dateString into a Date object with the correct format
            SimpleDateFormat inputFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.getDefault());
            Date date = inputFormat.parse(dateString);

            if (date == null) {
                throw new IllegalArgumentException("Invalid date format");
            }

            // Format the Date object into the desired format
            SimpleDateFormat outputFormat = new SimpleDateFormat(desiredFormat, Locale.getDefault());
            return outputFormat.format(date);

        } catch (Exception e) {
            // Handle parsing or formatting exceptions
            e.printStackTrace();
            if (dateString != null && dateString != "") {
                String[] date = dateString.split(" ");
                if (date.length > 1) {
                    dateString = date[0];
                } else {
                    dateString = "";
                }
            }
            return dateString; // Return error message if the date is invalid or an exception occurs
        }
    }
}
