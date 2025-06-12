package com.wyh.happyyousdk.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeleteReminderRequest {

    @SerializedName("uuid")
    @Expose
    private String uuid;
    @SerializedName("reminderId")
    @Expose
    private Integer reminderId;

    public DeleteReminderRequest(String uuid, Integer reminderId) {
        super();
        this.uuid = uuid;
        this.reminderId = reminderId;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Integer getReminderId() {
        return reminderId;
    }

    public void setReminderId(Integer reminderId) {
        this.reminderId = reminderId;
    }

}
