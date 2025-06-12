package com.wyh.happyyousdk.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RewardsData {

    @SerializedName("ActivityID")
    @Expose
    private int ActivityID;

    @SerializedName("IsFromPreviousLevel")
    @Expose
    private boolean IsFromPreviousLevel;

    @SerializedName("ActivityName")
    @Expose
    private String ActivityName;


    @SerializedName("ActivityTag")
    @Expose
    private String ActivityTag;

    @SerializedName("Point")
    @Expose
    private String Point;

    @SerializedName("ActivityDesc")
    @Expose
    private String ActivityDesc;

    @SerializedName("howTo")
    @Expose
    private String howTo;

    @SerializedName("whatTo")
    @Expose
    private String whatTo;

    @SerializedName("whyTo")
    @Expose
    private String whyTo;

    @SerializedName("ActivityImagePath")
    @Expose
    private String ActivityImagePath;

    @SerializedName("IsCompleted")
    @Expose
    private Boolean IsCompleted;

    @SerializedName("EventType")
    @Expose
    private String EventType;

    @SerializedName("IsStarted")
    @Expose
    private Boolean IsStarted;

    @SerializedName("RedirectTo")
    @Expose
    private String RedirectTo;

    @SerializedName("ProgressPercentage")
    @Expose
    private int ProgressPercentage;

    public int getActivityID() {
        return ActivityID;
    }

    public boolean getIsFromPreviousLevel() {
        return IsFromPreviousLevel;
    }

    public String getActivityName() {
        return ActivityName;
    }

    public String getActivityTag() {
        return ActivityTag;
    }

    public String getPoint() {
        return Point;
    }

    public String getActivityDesc() {
        return ActivityDesc;
    }

    public String getHowTo() {
        return howTo;
    }

    public String getWhatTo() {
        return whatTo;
    }

    public String getWhyTo() {
        return whyTo;
    }

    public String getActivityImagePath() {
        return ActivityImagePath;
    }

    public Boolean getIsCompleted() {
        return IsCompleted;
    }

    public String getEventType() {
        return EventType;
    }

    public Boolean getIsStarted() {
        return IsStarted;
    }

    public String getRedirectTo() {
        return RedirectTo;
    }

    public int getProgressPercentage() {
        return ProgressPercentage;
    }

}
