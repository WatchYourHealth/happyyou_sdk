package com.wyh.happyyousdk.model.response.postloginreward;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RewardItem implements Serializable {
    @SerializedName("activityTransId")
    @Expose
    private Integer activityTransId;
    @SerializedName("quadrantPosition")
    @Expose
    private Integer quadrantPosition;
    @SerializedName("rewardType")
    @Expose
    private String rewardType;
    @SerializedName("rewardDescription")
    @Expose
    private String rewardDescription;
    @SerializedName("rewardName")
    @Expose
    private String rewardName;
    @SerializedName("activity")
    @Expose
    private String activity;
    @SerializedName("isStarted")
    @Expose
    private Boolean isStarted;
    @SerializedName("expireOn")
    @Expose
    private String expireOn;
    @SerializedName("whatToDo")
    @Expose
    private String whatToDo;
    @SerializedName("howToDo")
    @Expose
    private String howToDo;
    @SerializedName("whyToDo")
    @Expose
    private String whyToDo;
    @SerializedName("redirectionKey")
    @Expose
    private String redirectionKey;
    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("activityTitle")
    @Expose
    private String activityTitle;
    @SerializedName("activityDecription")
    @Expose
    private String activityDecription;
    @SerializedName("startedDate")
    @Expose
    private String startedDate;
    @SerializedName("completedOn")
    @Expose
    private String completedOn;
    @SerializedName("isRewardGiven")
    @Expose
    private boolean isRewardGiven;
    @SerializedName("withoutActivity")
    @Expose
    private boolean withoutActivity;
    @SerializedName("isCompleted")
    @Expose
    private boolean isCompleted;
    @SerializedName("isExpired")
    @Expose
    private boolean isExpired;
    @SerializedName("activityImageUploaded")
    @Expose
    private boolean activityImageUploaded = false;
    @SerializedName("imageStatus")
    @Expose
    private String imageStatus;

    public boolean getActivityImageUploaded() {
        return activityImageUploaded;
    }

    public void setActivityImageUploaded(boolean activityImageUploaded) {
        this.activityImageUploaded = activityImageUploaded;
    }

    public String getImageStatus() {
        return imageStatus;
    }

    public void setImageStatus(String imageStatus) {
        this.imageStatus = imageStatus;
    }

    public Boolean getStarted() {
        return isStarted;
    }

    public void setStarted(Boolean started) {
        isStarted = started;
    }

    public String getActivityTitle() {
        return activityTitle;
    }

    public void setActivityTitle(String activityTitle) {
        this.activityTitle = activityTitle;
    }

    public String getActivityDecription() {
        return activityDecription;
    }

    public void setActivityDecription(String activityDecription) {
        this.activityDecription = activityDecription;
    }

    public String getStartedDate() {
        return startedDate;
    }

    public void setStartedDate(String startedDate) {
        this.startedDate = startedDate;
    }

    public String getCompletedOn() {
        return completedOn;
    }

    public void setCompletedOn(String completedOn) {
        this.completedOn = completedOn;
    }

    public boolean isRewardGiven() {
        return isRewardGiven;
    }

    public void setRewardGiven(boolean rewardGiven) {
        isRewardGiven = rewardGiven;
    }

    public boolean isWithoutActivity() {
        return withoutActivity;
    }

    public void setWithoutActivity(boolean withoutActivity) {
        this.withoutActivity = withoutActivity;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public boolean isExpired() {
        return isExpired;
    }

    public void setExpired(boolean expired) {
        isExpired = expired;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public String getRedirectionKey() {
        return redirectionKey;
    }

    public void setRedirectionKey(String redirectionKey) {
        this.redirectionKey = redirectionKey;
    }

    public Integer getActivityTransId() {
        return activityTransId;
    }

    public void setActivityTransId(Integer activityTransId) {
        this.activityTransId = activityTransId;
    }

    public Integer getQuadrantPosition() {
        return quadrantPosition;
    }

    public void setQuadrantPosition(Integer quadrantPosition) {
        this.quadrantPosition = quadrantPosition;
    }

    public String getRewardType() {
        return rewardType;
    }

    public void setRewardType(String rewardType) {
        this.rewardType = rewardType;
    }

    public String getRewardDescription() {
        return rewardDescription;
    }

    public void setRewardDescription(String rewardDescription) {
        this.rewardDescription = rewardDescription;
    }

    public String getRewardName() {
        return rewardName;
    }

    public void setRewardName(String rewardName) {
        this.rewardName = rewardName;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public Boolean getIsStarted() {
        return isStarted;
    }

    public void setIsStarted(Boolean isStarted) {
        this.isStarted = isStarted;
    }

    public String getExpireOn() {
        return expireOn;
    }

    public void setExpireOn(String expireOn) {
        this.expireOn = expireOn;
    }

    public String getWhatToDo() {
        return whatToDo;
    }

    public void setWhatToDo(String whatToDo) {
        this.whatToDo = whatToDo;
    }

    public String getHowToDo() {
        return howToDo;
    }

    public void setHowToDo(String howToDo) {
        this.howToDo = howToDo;
    }

    public String getWhyToDo() {
        return whyToDo;
    }

    public void setWhyToDo(String whyToDo) {
        this.whyToDo = whyToDo;
    }
}
