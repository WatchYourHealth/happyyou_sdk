package com.wyh.happyyousdk.SpinWheel;

import com.wyh.happyyousdk.model.response.AssignRewardsResponse;
import com.wyh.happyyousdk.model.response.playwin.QuizathonRewardData;

public interface spinRewardCallBack {
    void onRewardRecieved(AssignRewardsResponse.SpinRewardsData spinRewardsData);
    void onQuizRewardRecieved(QuizathonRewardData quizathonRewardData);
}
