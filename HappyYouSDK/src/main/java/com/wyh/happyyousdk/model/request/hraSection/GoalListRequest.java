package com.wyh.happyyousdk.model.request.hraSection;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GoalListRequest {

    @SerializedName("CategoryID")
    @Expose
    private Integer categoryID;
    @SerializedName("UUID")
    @Expose
    private String uuid;

    public GoalListRequest(Integer categoryID,String uuid) {
        super();
        this.categoryID = categoryID;
        this.uuid=uuid;
    }

    public Integer getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(Integer categoryID) {
        this.categoryID = categoryID;
    }
    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
