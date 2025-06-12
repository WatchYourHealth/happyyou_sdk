package com.wyh.happyyousdk.model.response.diary;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.wyh.happyyousdk.model.CommonSuccessResponse;

public class DiaryDetailsResponse extends CommonSuccessResponse {

    @SerializedName("data")
    @Expose
    public DairyDetailsDataResponse data;

    public DairyDetailsDataResponse getData() {
        return data;
    }

    public void setData(DairyDetailsDataResponse data) {
        this.data = data;
    }
}
