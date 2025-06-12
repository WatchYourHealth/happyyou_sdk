package com.wyh.happyyousdk.model.request.absorb;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetDashboardDataRequest {

    @SerializedName("category")
    @Expose
    private String category;

    @SerializedName("Searchkey")
    @Expose
    private String Searchkey;

    public GetDashboardDataRequest(String category, String searchkey) {
        this.category = category;
        Searchkey = searchkey;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

}
