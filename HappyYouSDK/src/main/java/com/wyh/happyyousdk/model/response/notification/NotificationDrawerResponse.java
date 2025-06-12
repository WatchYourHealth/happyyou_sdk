package com.wyh.happyyousdk.model.response.notification;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;



public class NotificationDrawerResponse {
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("notificationMessgae")
    @Expose
    private String notificationMessgae;
    @SerializedName("notificationTime")
    @Expose
    private String notificationTime;

    @SerializedName("communityId")
    @Expose
    private String communityId;

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        communityId = communityId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNotificationMessgae() {
        return notificationMessgae;
    }

    public void setNotificationMessgae(String notificationMessgae) {
        this.notificationMessgae = notificationMessgae;
    }

    public String getNotificationTime() {
        return notificationTime;
    }

    public void setNotificationTime(String notificationTime) {
        this.notificationTime = notificationTime;
    }
}
