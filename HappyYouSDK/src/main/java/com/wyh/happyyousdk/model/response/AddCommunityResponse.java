package com.wyh.happyyousdk.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddCommunityResponse {

    @SerializedName("msg")
    @Expose
    private String msg;

    @SerializedName("success")
    @Expose
    private Boolean success;

    @SerializedName("feedbackDetails")
    @Expose
    private FeedbackResponseData feedbackDetails;

    public String getMsg() {
        return msg;
    }

    public Boolean getSuccess() {
        return success;
    }

    public FeedbackResponseData getFeedbackDetails() {
        return feedbackDetails;
    }
}
