package com.wyh.happyyousdk.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NudgeRedirection {
    @SerializedName("nudgeFeature")
    @Expose
    public NudgeFeature nudgeFeature;

    @SerializedName("nudgeBanner")
    @Expose
    public NudgeBanner nudgeBanner;

    public NudgeFeature getNudgeFeature() {
        return nudgeFeature;
    }

    public NudgeBanner getNudgeBanner() {
        return nudgeBanner;
    }
}
