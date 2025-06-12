package com.wyh.happyyousdk.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class AddReminderRequest {

    @SerializedName("uuid")
    @Expose
    private String uuid;
    @SerializedName("startTime")
    @Expose
    private String startTime;
    @SerializedName("endTime")
    @Expose
    private String endTime;
    @SerializedName("reminderName")
    @Expose
    private String reminderName;
    @SerializedName("intervalORFixedT")
    @Expose
    private String intervalORFixedT;
    @SerializedName("isRepetitive")
    @Expose
    private Boolean isRepetitive;
    @SerializedName("isReminderOn")
    @Expose
    private Integer isReminderOn;
    @SerializedName("active")
    @Expose
    private Integer active;
    @SerializedName("appointmentDate")
    @Expose
    private String appointmentDate;
    @SerializedName("isPredefined")
    @Expose
    private Boolean isPredefined;
    @SerializedName("reminderTitle")
    @Expose
    private String reminderTitle;
    @SerializedName("weekDays")
    @Expose
    private String weekDays;
    @SerializedName("reminderBy")
    @Expose
    private String reminderBy;
    @SerializedName("communityId")
    @Expose
    private Integer communityId;

    public AddReminderRequest(String uuid, String startTime, String endTime, String reminderName, String intervalORFixedT,
                              Boolean isRepetitive, Integer isReminderOn, Integer active, String appointmentDate,
                              Boolean isPredefined, String reminderTitle, String weekDays, String reminderBy, Integer communityId) {
        super();
        this.uuid = uuid;
        this.startTime = startTime;
        this.endTime = endTime;
        this.reminderName = reminderName;
        this.intervalORFixedT = intervalORFixedT;
        this.isRepetitive = isRepetitive;
        this.isReminderOn = isReminderOn;
        this.active = active;
        this.appointmentDate = appointmentDate;
        this.isPredefined = isPredefined;
        this.reminderTitle = reminderTitle;
        this.weekDays = weekDays;
        this.reminderBy = reminderBy;
        this.communityId = communityId;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getReminderName() {
        return reminderName;
    }

    public void setReminderName(String reminderName) {
        this.reminderName = reminderName;
    }

    public String getIntervalORFixedT() {
        return intervalORFixedT;
    }

    public void setIntervalORFixedT(String intervalORFixedT) {
        this.intervalORFixedT = intervalORFixedT;
    }

    public Boolean getIsRepetitive() {
        return isRepetitive;
    }

    public void setIsRepetitive(Boolean isRepetitive) {
        this.isRepetitive = isRepetitive;
    }

    public Integer getIsReminderOn() {
        return isReminderOn;
    }

    public void setIsReminderOn(Integer isReminderOn) {
        this.isReminderOn = isReminderOn;
    }

    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public Boolean getIsPredefined() {
        return isPredefined;
    }

    public void setIsPredefined(Boolean isPredefined) {
        this.isPredefined = isPredefined;
    }

    public String getReminderTitle() {
        return reminderTitle;
    }

    public void setReminderTitle(String reminderTitle) {
        this.reminderTitle = reminderTitle;
    }

    public String getWeekDays() {
        return weekDays;
    }

    public void setWeekDays(String weekDays) {
        this.weekDays = weekDays;
    }

    public String getReminderBy() {
        return reminderBy;
    }

    public void setReminderBy(String reminderBy) {
        this.reminderBy = reminderBy;
    }

    public Integer getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Integer communityId) {
        this.communityId = communityId;
    }

}
