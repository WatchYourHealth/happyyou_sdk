package com.wyh.happyyousdk.model.request;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpdateReminderSettingRequest {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("active")
    @Expose
    private Integer active;
    @SerializedName("appointmentDate")
    @Expose
    private String appointmentDate;
    @SerializedName("communityId")
    @Expose
    private Integer communityId;
    @SerializedName("endTime")
    @Expose
    private String endTime;
    @SerializedName("intervalORFixedT")
    @Expose
    private String intervalORFixedT;
    @SerializedName("isPredefined")
    @Expose
    private Boolean isPredefined;
    @SerializedName("isReminderOn")
    @Expose
    private Integer isReminderOn;
    @SerializedName("isRepetitive")
    @Expose
    private Boolean isRepetitive;
    @SerializedName("reminderBy")
    @Expose
    private String reminderBy;
    @SerializedName("reminderName")
    @Expose
    private String reminderName;
    @SerializedName("reminderTitle")
    @Expose
    private String reminderTitle;
    @SerializedName("startTime")
    @Expose
    private String startTime;
    @SerializedName("uuid")
    @Expose
    private String uuid;
    @SerializedName("weekDays")
    @Expose
    private String weekDays;

    public UpdateReminderSettingRequest(Integer id, Integer active, String appointmentDate, Integer communityId, String endTime, String intervalORFixedT, Boolean isPredefined, Integer isReminderOn, Boolean isRepetitive, String reminderBy, String reminderName, String reminderTitle, String startTime, String uuid, String weekDays) {
        super();
        this.id = id;
        this.active = active;
        this.appointmentDate = appointmentDate;
        this.communityId = communityId;
        this.endTime = endTime;
        this.intervalORFixedT = intervalORFixedT;
        this.isPredefined = isPredefined;
        this.isReminderOn = isReminderOn;
        this.isRepetitive = isRepetitive;
        this.reminderBy = reminderBy;
        this.reminderName = reminderName;
        this.reminderTitle = reminderTitle;
        this.startTime = startTime;
        this.uuid = uuid;
        this.weekDays = weekDays;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Integer communityId) {
        this.communityId = communityId;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getIntervalORFixedT() {
        return intervalORFixedT;
    }

    public void setIntervalORFixedT(String intervalORFixedT) {
        this.intervalORFixedT = intervalORFixedT;
    }

    public Boolean getIsPredefined() {
        return isPredefined;
    }

    public void setIsPredefined(Boolean isPredefined) {
        this.isPredefined = isPredefined;
    }

    public Integer getIsReminderOn() {
        return isReminderOn;
    }

    public void setIsReminderOn(Integer isReminderOn) {
        this.isReminderOn = isReminderOn;
    }

    public Boolean getIsRepetitive() {
        return isRepetitive;
    }

    public void setIsRepetitive(Boolean isRepetitive) {
        this.isRepetitive = isRepetitive;
    }

    public String getReminderBy() {
        return reminderBy;
    }

    public void setReminderBy(String reminderBy) {
        this.reminderBy = reminderBy;
    }

    public String getReminderName() {
        return reminderName;
    }

    public void setReminderName(String reminderName) {
        this.reminderName = reminderName;
    }

    public String getReminderTitle() {
        return reminderTitle;
    }

    public void setReminderTitle(String reminderTitle) {
        this.reminderTitle = reminderTitle;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getWeekDays() {
        return weekDays;
    }

    public void setWeekDays(String weekDays) {
        this.weekDays = weekDays;
    }

}
