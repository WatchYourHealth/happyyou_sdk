package com.wyh.happyyousdk.model.request.absorb;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VideoBookmarkRequest {

    @SerializedName("fileId")
    @Expose
    private int fileId;
    @SerializedName("tag")
    @Expose
    private String tag;
    @SerializedName("isBookmarked")
    @Expose
    private Boolean isBookmarked;

    public VideoBookmarkRequest(int fileId, String tag, Boolean isBookmarked) {
        this.fileId = fileId;
        this.tag = tag;
        this.isBookmarked = isBookmarked;
    }

    public int getFileId() {
        return fileId;
    }

    public void setFileId(Integer fileId) {
        this.fileId = fileId;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Boolean getIsBookmarked() {
        return isBookmarked;
    }

    public void setIsBookmarked(Boolean isBookmarked) {
        this.isBookmarked = isBookmarked;
    }

}
