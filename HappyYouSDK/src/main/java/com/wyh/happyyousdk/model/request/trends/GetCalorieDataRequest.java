package com.wyh.happyyousdk.model.request.trends;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetCalorieDataRequest {

    @SerializedName("GUID")
    @Expose
    private String guid;
    @SerializedName("DateOfServing")
    @Expose
    private String dateOfServing;

    public GetCalorieDataRequest(String guid, String dateOfServing) {
        super();
        this.guid = guid;
        this.dateOfServing = dateOfServing;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getDateOfServing() {
        return dateOfServing;
    }

    public void setDateOfServing(String dateOfServing) {
        this.dateOfServing = dateOfServing;
    }

}
