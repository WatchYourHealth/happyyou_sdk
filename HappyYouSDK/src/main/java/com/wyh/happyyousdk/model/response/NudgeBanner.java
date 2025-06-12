package com.wyh.happyyousdk.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NudgeBanner {

    @SerializedName("bannerId")
    @Expose
    public String bannerId;

    @SerializedName("imageUrl")
    @Expose
    public String imageUrl;

    @SerializedName("disclaimer")
    @Expose
    public String disclaimer;

    @SerializedName("redirectionUrl")
    @Expose
    public String redirectionUrl;

    @SerializedName("title")
    @Expose
    public String title;

    @SerializedName("disclaimerImageUrl")
    @Expose
    public String disclaimerImageUrl;

    @SerializedName("redirectionKey")
    @Expose
    public String redirectionKey;

    @SerializedName("openInChrome")
    @Expose
    public Boolean openInChrome;

    @SerializedName("sequence")
    @Expose
    public String sequence;

    @SerializedName("happyMartKey")
    @Expose
    public String happyMartKey;

    @SerializedName("happyMartValue")
    @Expose
    public String happyMartValue;

    public String getBannerId() {
        return bannerId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getDisclaimer() {
        return disclaimer;
    }

    public String getRedirectionUrl() {
        return redirectionUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getDisclaimerImageUrl() {
        return disclaimerImageUrl;
    }

    public String getRedirectionKey() {
        return redirectionKey;
    }

    public Boolean getOpenInChrome() {
        return openInChrome;
    }

    public String getSequence() {
        return sequence;
    }

    public String getHappyMartKey() {
        return happyMartKey;
    }

    public String getHappyMartValue() {
        return happyMartValue;
    }
}
