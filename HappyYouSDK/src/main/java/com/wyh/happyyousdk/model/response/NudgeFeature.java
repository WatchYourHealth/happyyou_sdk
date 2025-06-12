package com.wyh.happyyousdk.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NudgeFeature {

    @SerializedName("firstRedirection")
    @Expose
    private String firstRedirection;

    @SerializedName("secondRedirection")
    @Expose
    private String secondRedirection;


    @SerializedName("featureName")
    @Expose
    private String featureName;

    @SerializedName("activityName")
    @Expose
    private String activityName;

    @SerializedName("firstRedirectionURL")
    @Expose
    private String firstRedirectionURL;

    @SerializedName("secondRedirectionURL")
    @Expose
    private String secondRedirectionURL;

    @SerializedName("thirdRedirectionURL")
    @Expose
    private String thirdRedirectionURL;

    @SerializedName("fourthRedirectionURL")
    @Expose
    private String fourthRedirectionURL;

    public String getFirstRedirection() {
        return firstRedirection;
    }

    public String getSecondRedirection() {
        return secondRedirection;
    }

    public String getFeatureName() {
        return featureName;
    }

    public String getActivityName() {
        return activityName;
    }

    public String getFirstRedirectionURL() {
        return firstRedirectionURL;
    }

    public String getSecondRedirectionURL() {
        return secondRedirectionURL;
    }

    public String getThirdRedirectionURL() {
        return thirdRedirectionURL;
    }

    public String getFourthRedirectionURL() {
        return fourthRedirectionURL;
    }
}
