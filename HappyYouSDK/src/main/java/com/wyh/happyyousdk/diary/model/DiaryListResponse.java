package com.wyh.happyyousdk.diary.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.wyh.happyyousdk.model.CommonSuccessResponse;

import java.util.List;

public class DiaryListResponse extends CommonSuccessResponse {
    @SerializedName("data")
    @Expose
    private List<DiaryListDataResponse> data;


    public List<DiaryListDataResponse> getData() {
        return data;
    }

    public void setData(List<DiaryListDataResponse> data) {
        this.data = data;
    }
}
