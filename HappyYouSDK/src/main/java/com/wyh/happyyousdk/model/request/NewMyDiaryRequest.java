package com.wyh.happyyousdk.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NewMyDiaryRequest {

    @SerializedName("IsMyDiary")
    @Expose
    private Boolean IsMyDiary;

    public NewMyDiaryRequest(Boolean isMyDiary) {
        IsMyDiary = isMyDiary;
    }
}
