package com.wyh.happyyousdk.model.request.trends;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FetchGraphRequest {
    @SerializedName("ActivityType")
    @Expose
    private String activityType;
    @SerializedName("PeriodType")
    @Expose
    private String periodType;
    @SerializedName("PeriodIndex")
    @Expose
    private Integer periodIndex;
    @SerializedName("communityId")
    @Expose
    private Integer communityId;

    public FetchGraphRequest(String activityType, String periodType, Integer periodIndex) {
        super();
        this.activityType = activityType;
        this.periodType = periodType;
        this.periodIndex = periodIndex;
    }

    public FetchGraphRequest(String activityType, String periodType, Integer periodIndex, Integer communityId) {
        super();
        this.activityType = activityType;
        this.periodType = periodType;
        this.periodIndex = periodIndex;
        this.communityId = communityId;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public String getPeriodType() {
        return periodType;
    }

    public void setPeriodType(String periodType) {
        this.periodType = periodType;
    }

    public Integer getPeriodIndex() {
        return periodIndex;
    }

    public void setPeriodIndex(Integer periodIndex) {
        this.periodIndex = periodIndex;
    }

    public Integer getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Integer communityId) {
        this.communityId = communityId;
    }

}
