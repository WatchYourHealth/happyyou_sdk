package com.wyh.happyyousdk.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClickEventRequest {


    @SerializedName("EventType")
    @Expose
    private String EventType;

    @SerializedName("SubType")
    @Expose
    private String SubType;

    @SerializedName("SubTypeId")
    @Expose
    private String SubTypeId;

    @SerializedName("Action")
    @Expose
    private String Action;

    @SerializedName("Content")
    @Expose
    private String Content;


    public ClickEventRequest(String eventType, String subType, String subTypeId, String action, String content) {
        EventType = eventType;
        SubType = subType;
        SubTypeId = subTypeId;
        Action = action;
        Content = content;
    }
}
