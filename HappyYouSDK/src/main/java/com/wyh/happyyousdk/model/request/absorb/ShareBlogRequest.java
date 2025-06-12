package com.wyh.happyyousdk.model.request.absorb;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ShareBlogRequest {

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("contentId")
    @Expose
    private Integer contentId;
    @SerializedName("communityIds")
    @Expose
    private List<CommunityId> communityIds;

    public ShareBlogRequest(String type, Integer contentId, List<CommunityId> communityIds) {
        super();
        this.type = type;
        this.communityIds = communityIds;
        this.contentId = contentId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getContentId() {
        return contentId;
    }

    public void setContentId(Integer contentId) {
        this.contentId = contentId;
    }

    public List<CommunityId> getCommunityIds() {
        return communityIds;
    }

    public void setCommunityIds(List<CommunityId> communityIds) {
        this.communityIds = communityIds;
    }

    public static class CommunityId {

        @SerializedName("communityId")
        @Expose
        private Integer communityId;

        /**
         * No args constructor for use in serialization
         */
        public CommunityId() {
        }

        public CommunityId(Integer communityId) {
            super();
            this.communityId = communityId;
        }

        public Integer getCommunityId() {
            return communityId;
        }

        public void setCommunityId(Integer communityId) {
            this.communityId = communityId;
        }

    }


}
