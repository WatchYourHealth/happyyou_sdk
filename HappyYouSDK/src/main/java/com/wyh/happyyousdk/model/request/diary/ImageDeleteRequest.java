package com.wyh.happyyousdk.model.request.diary;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ImageDeleteRequest {
    @SerializedName("diaryId")
    @Expose
    private int diaryId;

    public ImageDeleteRequest(int diaryId) {
        this.diaryId = diaryId;
    }

    public int getCommunityId() {
        return diaryId;
    }

    public void setCommunityId(int diaryId) {
        this.diaryId = diaryId;
    }
}
