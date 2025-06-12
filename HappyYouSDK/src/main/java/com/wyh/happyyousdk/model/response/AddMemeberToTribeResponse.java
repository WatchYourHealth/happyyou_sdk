package com.wyh.happyyousdk.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.wyh.happyyousdk.model.EnGTokensModel;
import com.wyh.happyyousdk.model.FreeVoucher;
import com.wyh.happyyousdk.model.RewardsModel;

public class AddMemeberToTribeResponse {

    @SerializedName("msg")
    @Expose
    public String msg;

    @SerializedName("success")
    @Expose
    public Boolean success;

    @SerializedName("feedbackDetails")
    @Expose
    public FeedbackResponseData feedbackDetails;

    @SerializedName("rewards")
    @Expose
    private RewardsModel rewards;

    @SerializedName("freevoucher")
    @Expose
    private FreeVoucher freeVoucher;

    @SerializedName("enGTokens")
    @Expose
    private EnGTokensModel enGTokens;

    public String getMsg() {
        return msg;
    }

    public Boolean getSuccess() {
        return success;
    }

    public FeedbackResponseData getFeedbackDetails() {
        return feedbackDetails;
    }

    public RewardsModel getRewards() {
        return rewards;
    }

    public FreeVoucher getFreeVoucher() {
        return freeVoucher;
    }

    public EnGTokensModel getEnGTokens() {
        return enGTokens;
    }
}
