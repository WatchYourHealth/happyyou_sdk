package com.wyh.happyyousdk.model.request.ehr;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeleteIdRequest {

    @SerializedName("ID")
    @Expose
    private Integer id;

    public DeleteIdRequest(Integer id) {
        super();
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}
