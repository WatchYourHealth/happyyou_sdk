package com.wyh.happyyousdk.model.request.absorb;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetQuickReadRequest {

    @SerializedName("itemType")
    @Expose
    private String itemType;
    @SerializedName("tagName")
    @Expose
    private String tagName;
    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("communityId")
    @Expose
    private int communityId;

    public GetQuickReadRequest(String itemType, String tagName, String userId, int communityId) {
        super();
        this.itemType = itemType;
        this.tagName = tagName;
        this.userId = userId;
        this.communityId = communityId;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getCommunityId() {
        return communityId;
    }

    public void setCommunityId(int communityId) {
        this.communityId = communityId;
    }
}
