package com.wyh.happyyousdk.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.wyh.happyyousdk.model.response.FeedbackResponseData;

import java.io.Serializable;

public class FeedbackResponse implements Serializable {

    @SerializedName("feedbackDetails")
    @Expose
    private FeedbackResponseData feedbackDetails;

    public FeedbackResponseData getFeedbackDetails() {
        return feedbackDetails;
    }

    public void setFeedbackDetails(FeedbackResponseData feedbackDetails) {
        this.feedbackDetails = feedbackDetails;
    }
}
