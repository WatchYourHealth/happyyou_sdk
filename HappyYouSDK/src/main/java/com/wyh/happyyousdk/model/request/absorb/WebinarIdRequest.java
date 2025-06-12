package com.wyh.happyyousdk.model.request.absorb;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WebinarIdRequest {

    @SerializedName("webinarId")
    @Expose
    private Integer webinarId;

    /**
     * No args constructor for use in serialization
     *
     */
    public WebinarIdRequest() {
    }

    /**
     *
     * @param webinarId
     */
    public WebinarIdRequest(Integer webinarId) {
        super();
        this.webinarId = webinarId;
    }

    public Integer getWebinarId() {
        return webinarId;
    }

    public void setWebinarId(Integer webinarId) {
        this.webinarId = webinarId;
    }

}
