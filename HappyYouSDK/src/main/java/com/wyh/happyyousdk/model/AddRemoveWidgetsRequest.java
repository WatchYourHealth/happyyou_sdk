package com.wyh.happyyousdk.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddRemoveWidgetsRequest {
    @SerializedName("widgetId")
    @Expose
    private Integer widgetId;
    @SerializedName("isSet")
    @Expose
    private Boolean isSet;

    public AddRemoveWidgetsRequest(Integer widgetId, Boolean isSet) {
        this.widgetId = widgetId;
        this.isSet = isSet;
    }

    public Integer getWidgetId() {
        return widgetId;
    }

    public void setWidgetId(Integer widgetId) {
        this.widgetId = widgetId;
    }

    public Boolean getIsSet() {
        return isSet;
    }

    public void setIsSet(Boolean isSet) {
        this.isSet = isSet;
    }
}
