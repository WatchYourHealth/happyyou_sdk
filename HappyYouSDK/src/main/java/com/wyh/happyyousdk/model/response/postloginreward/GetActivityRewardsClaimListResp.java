package com.wyh.happyyousdk.model.response.postloginreward;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.wyh.happyyousdk.model.response.playwin.ClaimReClaimRewardModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GetActivityRewardsClaimListResp implements Serializable {
    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Boolean getPoolingReq() {
        return isPoolingReq;
    }

    public void setPoolingReq(Boolean poolingReq) {
        isPoolingReq = poolingReq;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("isPoolingReq")
    @Expose
    private Boolean isPoolingReq;
    private Data data;

    public static class Data {
        @SerializedName("userRewardsClaimModels")
        private ArrayList<ClaimReClaimRewardModel> userRewardsClaimModels;
        @SerializedName("quizActivityListModelLists")
        private UuizActivityListModelLists quizActivityListModelLists;
        @SerializedName("quizActivityPopupModels")
        private ArrayList<ClaimReClaimRewardModel> quizActivityPopupModel;

        public ArrayList<ClaimReClaimRewardModel> getQuizActivityPopupModel() {
            return quizActivityPopupModel;
        }

        public void setQuizActivityPopupModel(ArrayList<ClaimReClaimRewardModel> quizActivityPopupModel) {
            this.quizActivityPopupModel = quizActivityPopupModel;
        }

        public ArrayList<ClaimReClaimRewardModel> getUserRewardsClaimModels() {
            return userRewardsClaimModels;
        }

        public void setUserRewardsClaimModels(ArrayList<ClaimReClaimRewardModel> userRewardsClaimModels) {
            this.userRewardsClaimModels = userRewardsClaimModels;
        }


        public UuizActivityListModelLists getQuizActivityListModelLists() {
            return quizActivityListModelLists;
        }

        public void setQuizActivityListModelLists(UuizActivityListModelLists quizActivityListModelLists) {
            this.quizActivityListModelLists = quizActivityListModelLists;
        }

    }

    public static  class UuizActivityListModelLists{
        @SerializedName("wip")
        private List<RewardItem> wip;
        @SerializedName("activeList")
        private List<RewardItem>activeList;
        @SerializedName("completedList")
        private List<RewardItem>completedList;

        public List<RewardItem> getWip() {
            return wip;
        }

        public void setWip(List<RewardItem> wip) {
            this.wip = wip;
        }

        public List<RewardItem> getActiveList() {
            return activeList;
        }

        public void setActiveList(List<RewardItem> activeList) {
            this.activeList = activeList;
        }

        public List<RewardItem> getCompletedList() {
            return completedList;
        }

        public void setCompletedList(List<RewardItem> completedList) {
            this.completedList = completedList;
        }

        public List<RewardItem> getExpiredList() {
            return expiredList;
        }

        public void setExpiredList(List<RewardItem> expiredList) {
            this.expiredList = expiredList;
        }

        @SerializedName("expiredList")
        private List<RewardItem>expiredList;
    }
    public static  class UserRewardsActivityClaimModels{
        @SerializedName("wip")
        private List<RewardItem> wip;
        @SerializedName("activeList")
        private List<RewardItem>activeList;
        @SerializedName("completedList")
        private List<RewardItem>completedList;
        @SerializedName("expiredList")
        private List<RewardItem>expiredList;
        public List<RewardItem> getWip() {
            return wip;
        }

        public void setWip(List<RewardItem> wip) {
            this.wip = wip;
        }

        public List<RewardItem> getActiveList() {
            return activeList;
        }

        public void setActiveList(List<RewardItem> activeList) {
            this.activeList = activeList;
        }

        public List<RewardItem> getCompletedList() {
            return completedList;
        }

        public void setCompletedList(List<RewardItem> completedList) {
            this.completedList = completedList;
        }

        public List<RewardItem> getExpiredList() {
            return expiredList;
        }

        public void setExpiredList(List<RewardItem> expiredList) {
            this.expiredList = expiredList;
        }


    }

}
