package com.wyh.happyyousdk.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FeedbackModel {
    @SerializedName("featureModuleId")
    @Expose
    private String featureModuleId;
    @SerializedName("feedbackDesc")
    @Expose
    private String feedbackDesc;
    @SerializedName("commentLength")
    @Expose
    private String commentLength;
    @SerializedName("featureModuleName")
    @Expose
    private String featureModuleName;
    @SerializedName("moduleConfigId")
    @Expose
    private int moduleConfigId;


    public String getFeatureModuleId() {
        return featureModuleId;
    }

    public void setFeatureModuleId(String featureModuleId) {
        this.featureModuleId = featureModuleId;
    }

    public String getFeedbackDesc() {
        return feedbackDesc;
    }

    public void setFeedbackDesc(String feedbackDesc) {
        this.feedbackDesc = feedbackDesc;
    }

    public String getCommentLength() {
        return commentLength;
    }

    public void setCommentLength(String commentLength) {
        this.commentLength = commentLength;
    }

    public String getFeatureModuleName() {
        return featureModuleName;
    }

    public void setFeatureModuleName(String featureModuleName) {
        this.featureModuleName = featureModuleName;
    }

    public int getModuleConfigId() {
        return moduleConfigId;
    }

    public void setModuleConfigId(int moduleConfigId) {
        this.moduleConfigId = moduleConfigId;
    }
}
