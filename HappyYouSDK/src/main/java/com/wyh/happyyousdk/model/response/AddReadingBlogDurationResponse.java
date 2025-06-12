package com.wyh.happyyousdk.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.wyh.happyyousdk.model.CommonSuccessResponse;
import com.wyh.happyyousdk.model.request.absorb.AddBlogDurationRequest;

public class AddReadingBlogDurationResponse extends CommonSuccessResponse {

    @SerializedName("data")
    @Expose
    private AddBlogDurationRequest data;


    public AddBlogDurationRequest getData() {
        return data;
    }

    public void setData(AddBlogDurationRequest data) {
        this.data = data;
    }
}
