package com.wyh.happyyousdk.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MultipleGraphDataRequest {

    @SerializedName("communityId")
    @Expose
    private Integer communityId;
    @SerializedName("periodType")
    @Expose
    private String periodType;
    @SerializedName("periodIndex")
    @Expose
    private Integer periodIndex;
    @SerializedName("activityType")
    @Expose
    private String activityType;
    @SerializedName("diagnosticType")
    @Expose
    private String diagnosticType;
    @SerializedName("diagnosticPeriodType")
    @Expose
    private String diagnosticPeriodType;
    @SerializedName("diagnosticPeriodIndex")
    @Expose
    private Integer diagnosticPeriodIndex;

    public MultipleGraphDataRequest(Integer communityId, String periodType, Integer periodIndex, String activityType, String diagnosticType, String diagnosticPeriodType, Integer diagnosticPeriodIndex) {
        super();
        this.communityId = communityId;
        this.periodType = periodType;
        this.periodIndex = periodIndex;
        this.activityType = activityType;
        this.diagnosticType = diagnosticType;
        this.diagnosticPeriodType = diagnosticPeriodType;
        this.diagnosticPeriodIndex = diagnosticPeriodIndex;
    }

    public Integer getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Integer communityId) {
        this.communityId = communityId;
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

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public String getDiagnosticType() {
        return diagnosticType;
    }

    public void setDiagnosticType(String diagnosticType) {
        this.diagnosticType = diagnosticType;
    }

    public String getDiagnosticPeriodType() {
        return diagnosticPeriodType;
    }

    public void setDiagnosticPeriodType(String diagnosticPeriodType) {
        this.diagnosticPeriodType = diagnosticPeriodType;
    }

    public Integer getDiagnosticPeriodIndex() {
        return diagnosticPeriodIndex;
    }

    public void setDiagnosticPeriodIndex(Integer diagnosticPeriodIndex) {
        this.diagnosticPeriodIndex = diagnosticPeriodIndex;
    }

}
