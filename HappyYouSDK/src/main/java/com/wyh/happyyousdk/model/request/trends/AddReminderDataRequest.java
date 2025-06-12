package com.wyh.happyyousdk.model.request.trends;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddReminderDataRequest {

    @SerializedName("reminderName")
    @Expose
    private String reminderName;
    @SerializedName("count")
    @Expose
    private Integer count;
    @SerializedName("timestamp")
    @Expose
    private String timestamp;

    public AddReminderDataRequest(String reminderName, Integer count, String timestamp) {
        super();
        this.reminderName = reminderName;
        this.count = count;
        this.timestamp = timestamp;
    }

    public String getReminderName() {
        return reminderName;
    }

    public void setReminderName(String reminderName) {
        this.reminderName = reminderName;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

}
