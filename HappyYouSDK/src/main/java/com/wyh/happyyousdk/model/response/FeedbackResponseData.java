package com.wyh.happyyousdk.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class FeedbackResponseData implements Serializable {
    @SerializedName("feedbackModel")
    @Expose
    private FeedbackModel feedbackModel;
    @SerializedName("customFeedbackRespModel")
    @Expose
    private CustomFeedbackRespModel customFeedbackRespModel;
    @SerializedName("starConfig")
    @Expose
    private List<FeedbackStarConfig> starConfig;

    public FeedbackModel getFeedbackModel() {
        return feedbackModel;
    }



    public void setFeedbackModel(FeedbackModel feedbackModel) {
        this.feedbackModel = feedbackModel;
    }

    public List<FeedbackStarConfig>  getStarConfig() {
        return starConfig;
    }

    public void setStarConfig(List<FeedbackStarConfig> starConfig) {
        this.starConfig = starConfig;
    }

    public CustomFeedbackRespModel getCustomFeedbackRespModel() {
        return customFeedbackRespModel;
    }

    public void setCustomFeedbackRespModel(CustomFeedbackRespModel customFeedbackRespModel) {
        this.customFeedbackRespModel = customFeedbackRespModel;
    }
}
