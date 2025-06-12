package com.wyh.happyyousdk.model.response.notification;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NotificationDataResponse {

    @SerializedName("notifyDate")
    @Expose
    private String notifyDate;
    @SerializedName("drawer")
    @Expose
    private List<NotificationDrawerResponse> drawer;

    public String getNotifyDate() {
        return notifyDate;
    }

    public void setNotifyDate(String notifyDate) {
        this.notifyDate = notifyDate;
    }

    public List<NotificationDrawerResponse> getDrawer() {
        return drawer;
    }

    public void setDrawer(List<NotificationDrawerResponse> drawer) {
        this.drawer = drawer;
    }

}