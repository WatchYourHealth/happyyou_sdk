package com.wyh.happyyousdk.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.wyh.happyyousdk.model.response.playwin.ClaimReClaimRewardModel;

import java.io.Serializable;
import java.util.ArrayList;

public class GetRewardsClaimListResp implements Serializable {
    @SerializedName("success")
    @Expose
    private Boolean success;


    @SerializedName("msg")
    @Expose
    private String msg;

    @SerializedName("data")
    @Expose
    private ClaimReclaimData data;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ClaimReclaimData getData() {
        return data;
    }

    public void setData(ClaimReclaimData data) {
        this.data = data;
    }

    public static  class  ClaimReclaimData{
        @SerializedName("userRewardsClaimModels")
        @Expose
        private ArrayList<ClaimReClaimRewardModel> userRewardClaims;

        @SerializedName("quizActivityPopupModels")
        @Expose
        private ArrayList<ClaimReClaimRewardModel> activiRewardClaim;

        public ArrayList<ClaimReClaimRewardModel> getUserRewardClaims() {
            return userRewardClaims;
        }

        public void setUserRewardClaims(ArrayList<ClaimReClaimRewardModel> userRewardClaims) {
            this.userRewardClaims = userRewardClaims;
        }

        public ArrayList<ClaimReClaimRewardModel> getActiviRewardClaim() {
            return activiRewardClaim;
        }

        public void setActiviRewardClaim(ArrayList<ClaimReClaimRewardModel> activiRewardClaim) {
            this.activiRewardClaim = activiRewardClaim;
        }
    }

}
