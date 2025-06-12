package com.wyh.happyyousdk.model.response.dashboard;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ActOMeterTribe {
    @SerializedName("communityId")
    @Expose
    private int communityId;
    @SerializedName("communityName")
    @Expose
    private String communityName;
    @SerializedName("communityType")
    @Expose
    private String communityType;
    @SerializedName("membersCount")
    @Expose
    private int membersCount;

    public int getMembersCount() {
        return membersCount;
    }

    public void setMembersCount(int membersCount) {
        this.membersCount = membersCount;
    }

    public int getCommunityId() {
        return communityId;
    }

    public void setCommunityId(int communityId) {
        this.communityId = communityId;
    }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public String getCommunityType() {
        return communityType;
    }

    public void setCommunityType(String communityType) {
        this.communityType = communityType;
    }
}
