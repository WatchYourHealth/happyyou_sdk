package com.wyh.happyyousdk.model.response.dashboard;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.wyh.happyyousdk.model.RewardsModel;
import com.wyh.happyyousdk.model.response.Usertype;

import java.util.List;

public class UsertypeDashboardData {

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
    private List <Usertype> dashboardData;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public RewardsModel getRewards() {
        return rewards;
    }

    public void setRewards(RewardsModel rewards) {
        this.rewards = rewards;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<Usertype> getDashboardData() {
        return dashboardData;
    }

    public void setDashboardData(List<Usertype> dashboardData) {
        this.dashboardData = dashboardData;
    }
}
