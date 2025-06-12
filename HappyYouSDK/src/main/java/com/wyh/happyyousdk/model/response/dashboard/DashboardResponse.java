package com.wyh.happyyousdk.model.response.dashboard;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.wyh.happyyousdk.model.RewardsModel;
import com.wyh.happyyousdk.model.response.FeedbackResponse;

public class DashboardResponse extends FeedbackResponse {
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("rewards")
    @Expose
    private RewardsModel rewards;
    @SerializedName("success")
    @Expose
    private boolean success;
    @SerializedName("data")
    @Expose
    private DashboardData dashboardData;



    public RewardsModel getRewards() {
        return rewards;
    }

    public void setRewards(RewardsModel rewards) {
        this.rewards = rewards;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public DashboardData getDashboardData() {
        return dashboardData;
    }

    public void setDashboardData(DashboardData dashboardData) {
        this.dashboardData = dashboardData;
    }
}
