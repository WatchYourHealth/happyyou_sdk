package com.wyh.happyyousdk.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.wyh.happyyousdk.model.CommonSuccessResponse;
import com.wyh.happyyousdk.model.request.absorb.AddBlogDurationRequest;
import com.wyh.happyyousdk.model.request.addFamily.AddFamilyRequest;

import java.util.List;

public class AddFamilyResponse extends CommonSuccessResponse {

    @SerializedName("data")
    @Expose
    private List<AddFamilyRequest> data;


    public List<AddFamilyRequest> getData() {
        return data;
    }

    public void setData(List<AddFamilyRequest> data) {
        this.data = data;
    }

}
