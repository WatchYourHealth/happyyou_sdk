package com.wyh.happyyousdk.play_and_win.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.SpinWheel.onRewardClick;
import com.wyh.happyyousdk.databinding.ItemGrridPlaywinBinding;
import com.wyh.happyyousdk.databinding.ItemRewardCardsGridBinding;
import com.wyh.happyyousdk.model.response.GetQuizQuestions;
import com.wyh.happyyousdk.model.response.QuestionModel;
import com.wyh.happyyousdk.model.response.playwin.QuizFeedbackModel;
import com.wyh.happyyousdk.model.response.playwin.QuizathonModel;
import com.wyh.happyyousdk.model.response.postloginreward.RewardItem;

import java.util.ArrayList;
import java.util.List;

public class GridPlaywinAdapter extends RecyclerView.Adapter<GridPlaywinAdapter.GridCardsViewHolder> {

    List<QuizathonModel> rewardItemList = new ArrayList<>();
    List<GetQuizQuestions> questionModelList = new ArrayList<>();
    List<QuizFeedbackModel> quizFeedbackModels = new ArrayList<>();
    Context mContext;
    String type;

    onRewardClick rewardClick;

    public GridPlaywinAdapter(List<QuizFeedbackModel> quizFeedbackModels, List<QuizathonModel> rewardItemList, List<GetQuizQuestions> questionModelList, String type, onRewardClick onRewardClick) {
        this.quizFeedbackModels = quizFeedbackModels;
        this.rewardItemList = rewardItemList;
        this.questionModelList = questionModelList;
        this.type = type;
        this.rewardClick = onRewardClick;
    }

    @NonNull
    @Override
    public GridCardsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        ItemGrridPlaywinBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_grrid_playwin, parent, false);
        return new GridCardsViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull GridCardsViewHolder holder, int position) {
        PlayWinCardViewAdapter adapter = new PlayWinCardViewAdapter(getFeedbackSubList(position), getSubList(position), getSubListQuestion(position), mContext, type, new onRewardClick() {
            @Override
            public void onRewardClick(Object item, String type) {
                rewardClick.onRewardClick(item, type);
            }
        });
        holder.binding.gvCardPalywin.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        if (quizFeedbackModels != null && quizFeedbackModels.size() > 0) {
            return (int) Math.ceil((double) quizFeedbackModels.size() / 2);
        } else if (rewardItemList != null && rewardItemList.size() > 0) {
            return (int) Math.ceil((double) rewardItemList.size() / 2);
        } else if (questionModelList != null && questionModelList.size() > 0) {
            return (int) Math.ceil((double) questionModelList.size() / 2);
        }
        return 0;
    }

    public class GridCardsViewHolder extends RecyclerView.ViewHolder {
        ItemGrridPlaywinBinding binding;

        public GridCardsViewHolder(@NonNull ItemGrridPlaywinBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }


    public List<QuizFeedbackModel> getFeedbackSubList(int index) {
        if (quizFeedbackModels != null && quizFeedbackModels.size() > 0) {
            // Determine the start and end indices for the sublist
            int start = index * 2;
            int end = Math.min(start + 2, quizFeedbackModels.size());
            // If start index exceeds the size of the list, return an empty list
            if (start >= quizFeedbackModels.size()) {
                return new ArrayList<>();
            }
            // Return the sublist
            return quizFeedbackModels.subList(start, end);
        }
        return null;
    }

    public List<QuizathonModel> getSubList(int index) {
        if (rewardItemList != null && rewardItemList.size() > 0) {
            // Determine the start and end indices for the sublist
            int start = index * 2;
            int end = Math.min(start + 2, rewardItemList.size());
            // If start index exceeds the size of the list, return an empty list
            if (start >= rewardItemList.size()) {
                return new ArrayList<>();
            }
            // Return the sublist
            return rewardItemList.subList(start, end);
        }
        return null;
    }

    public List<GetQuizQuestions> getSubListQuestion(int index) {
        // Determine the start and end indices for the sublist
        if (questionModelList != null && questionModelList.size() > 0) {
            int start = index * 2;
            int end = Math.min(start + 2, questionModelList.size());
            // If start index exceeds the size of the list, return an empty list
            if (start >= questionModelList.size()) {
                return new ArrayList<>();
            }
            // Return the sublist
            return questionModelList.subList(start, end);
        }
        return null;
    }

    private void adjustGridViewHeight(GridView gridView, int itemCount) {
        int rows = (int) Math.ceil((double) itemCount / gridView.getNumColumns());
        int itemHeight = mContext.getResources().getDimensionPixelSize(R.dimen.grid_item_height); // Define this dimension in res/values/dimens.xml
        int verticalSpacing = gridView.getVerticalSpacing();
        int totalHeight = rows * itemHeight + (rows - 1) * verticalSpacing;

        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        params.height = totalHeight;
        gridView.setLayoutParams(params);
    }
}
