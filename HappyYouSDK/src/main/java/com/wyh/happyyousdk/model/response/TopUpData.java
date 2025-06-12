package com.wyh.happyyousdk.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TopUpData {

    @SerializedName("Topupicon")
    @Expose
    private String TopupIcon;

    @SerializedName("TopUpID")
    @Expose
    private int TopUpID;

    @SerializedName("TopUpTag")
    @Expose
    private String TopUpTag;

    @SerializedName("TopUpName")
    @Expose
    private String TopUpName;

    @SerializedName("TopUpDesc")
    @Expose
    private String TopUpDesc;

    @SerializedName("Point")
    @Expose
    private int Point;

    @SerializedName("EventType")
    @Expose
    private String EventType;

    @SerializedName("whyTo")
    @Expose
    private String whyTo;

    @SerializedName("howTo")
    @Expose
    private String howTo;

    @SerializedName("whatTo")
    @Expose
    private String whatTo;

    @SerializedName("IsActive")
    @Expose
    private Boolean IsActive;

    @SerializedName("IsCompleted")
    @Expose
    private Boolean IsCompleted;

    @SerializedName("IsStarted")
    @Expose
    private Boolean IsStarted;

    @SerializedName("RedirectTo")
    @Expose
    private String RedirectTo;

    @SerializedName("RecurrenceDays")
    @Expose
    private String RecurrenceDays;

    @SerializedName("ProgressPercentage")
    @Expose
    private int ProgressPercentage;

    public String getTopupIcon() {
        return TopupIcon;
    }

    public int getTopUpID() {
        return TopUpID;
    }

    public String getTopUpTag() {
        return TopUpTag;
    }

    public String getTopUpName() {
        return TopUpName;
    }

    public String getTopUpDesc() {
        return TopUpDesc;
    }

    public int getPoint() {
        return Point;
    }

    public String getEventType() {
        return EventType;
    }

    public String getWhyTo() {
        return whyTo;
    }

    public String getHowTo() {
        return howTo;
    }

    public String getWhatTo() {
        return whatTo;
    }

    public Boolean getActive() {
        return IsActive;
    }

    public Boolean getIsCompleted() {
        return IsCompleted;
    }

    public Boolean getIsStarted() {
        return IsStarted;
    }

    public String getRedirectTo() {
        return RedirectTo;
    }

    public String getRecurrenceDays() {
        return RecurrenceDays;
    }

    public int getProgressPercentage() {
        return ProgressPercentage;
    }
}
