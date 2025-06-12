package com.wyh.happyyousdk.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FetchRemindersByUUIDResponse {

    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("data")
    @Expose
    private List<Datum> data;
    @SerializedName("freevoucher")
    @Expose
    private Object freevoucher;
    @SerializedName("enGTokens")
    @Expose
    private Object enGTokens;
    @SerializedName("rewards")
    @Expose
    private Object rewards;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public Object getFreevoucher() {
        return freevoucher;
    }

    public void setFreevoucher(Object freevoucher) {
        this.freevoucher = freevoucher;
    }

    public Object getEnGTokens() {
        return enGTokens;
    }

    public void setEnGTokens(Object enGTokens) {
        this.enGTokens = enGTokens;
    }

    public Object getRewards() {
        return rewards;
    }

    public void setRewards(Object rewards) {
        this.rewards = rewards;
    }

    public class Datum {

        @SerializedName("id")
        @Expose
        private Integer id;
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
        @SerializedName("createdAt")
        @Expose
        private String createdAt;
        @SerializedName("createdBy")
        @Expose
        private String createdBy;
        @SerializedName("updatedAt")
        @Expose
        private Object updatedAt;
        @SerializedName("updatedBy")
        @Expose
        private Object updatedBy;
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
        @SerializedName("titleTopic")
        @Expose
        private String titleTopic;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
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

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getCreatedBy() {
            return createdBy;
        }

        public void setCreatedBy(String createdBy) {
            this.createdBy = createdBy;
        }

        public Object getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(Object updatedAt) {
            this.updatedAt = updatedAt;
        }

        public Object getUpdatedBy() {
            return updatedBy;
        }

        public void setUpdatedBy(Object updatedBy) {
            this.updatedBy = updatedBy;
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

        public String getTitleTopic() {
            return titleTopic;
        }

        public void setTitleTopic(String communityId) {
            this.titleTopic = titleTopic;
        }
    }
}