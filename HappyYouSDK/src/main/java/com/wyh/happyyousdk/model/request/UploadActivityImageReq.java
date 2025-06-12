package com.wyh.happyyousdk.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UploadActivityImageReq implements Serializable {
    public UploadActivityImageReq(String activityEvent, String activityDescription, String transId) {
        ActivityEvent = activityEvent;
        ActivityDescription = activityDescription;
        TransId = transId;
    }

    public String getActivityEvent() {
        return ActivityEvent;
    }

    public void setActivityEvent(String activityEvent) {
        ActivityEvent = activityEvent;
    }

    public String getActivityDescription() {
        return ActivityDescription;
    }

    public void setActivityDescription(String activityDescription) {
        ActivityDescription = activityDescription;
    }

    public String getTransId() {
        return TransId;
    }

    public void setTransId(String transId) {
        TransId = transId;
    }

    @SerializedName("ActivityEvent")
    @Expose
    private String ActivityEvent;
    @SerializedName("ActivityDescription")
    @Expose
    private String ActivityDescription;
    @SerializedName("TransId")
    @Expose
    private String TransId;
    }
